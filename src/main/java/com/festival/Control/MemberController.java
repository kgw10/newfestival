package com.festival.Control;

import com.festival.DTO.MemberDTO;
import com.festival.Entity.Member;
import com.festival.Service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.util.HashMap;
import java.util.Map;

@Controller
@RequiredArgsConstructor
@RequestMapping("/member")
public class MemberController {

    private final MemberService memberService;

    // 로그인 페이지로 이동
    @GetMapping("/signIn")
    public String loginPage(Model model) {
        return "member/login";  // 로그인 페이지로 이동
    }

    // 로그인 처리
    @PostMapping("/signIn")
    public String login(@RequestParam("id") String id,  // userId 대신 id 사용
                        @RequestParam("password") String password,
                        Model model, HttpSession session) {
        if (memberService.validateLogin(id, password)) {  // 로그인 검증
            session.setAttribute("userId", id);  // 세션에 id 저장
            return "redirect:/";  // 로그인 후 메인 페이지로 리다이렉트
        } else {
            model.addAttribute("loginFailMsg", "아이디 또는 비밀번호가 잘못되었습니다.");
            return "member/login";  // 로그인 실패 시 로그인 페이지로 돌아감
        }
    }


    // 로그아웃 처리
    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();  // 세션 무효화
        return "redirect:/";  // 로그아웃 후 메인 페이지로 리다이렉트
    }

    // 아이디/비밀번호 찾기 페이지로 이동
    @GetMapping("/findInfo")
    public String findInfoPage(Model model) {
        return "member/findInfo";  // 아이디/비밀번호 찾기 페이지
    }

    // 아이디 찾기 처리
    @PostMapping("/findId")
    public String findId(
            @RequestParam("name") String name,
            @RequestParam("email") String email,
            Model model) {

        if (name == null || email == null) {
            model.addAttribute("findFailMsg", "이름과 이메일을 입력해 주세요.");
            return "member/foundInfo";
        }

        String foundId = memberService.findIdByNameAndEmail(name, email);
        if (foundId != null) {
            model.addAttribute("foundId", foundId);
        } else {
            model.addAttribute("findFailMsg", "이름과 이메일에 해당하는 아이디가 없습니다.");
        }

        return "member/foundInfo";
    }

    // 비밀번호 찾기 처리
    @PostMapping("/findPassword")
    public String findPassword(
            @RequestParam("userId") String userId,
            @RequestParam("nameForPassword") String nameForPassword,
            @RequestParam("emailForPassword") String emailForPassword,
            Model model) {

        if (userId == null || nameForPassword == null || emailForPassword == null) {
            model.addAttribute("resetFailMsg", "아이디, 이름, 이메일을 입력해 주세요.");
            return "member/foundInfo";
        }

        // 비밀번호 찾기 서비스 호출
        String foundPassword = memberService.findPasswordByIdNameAndEmail(userId, nameForPassword, emailForPassword);

        if (foundPassword != null) {
            model.addAttribute("foundPassword", foundPassword);  // 비밀번호 모델에 추가
        } else {
            model.addAttribute("resetFailMsg", "정보가 일치하지 않습니다.");
        }

        return "member/foundInfo";
    }



    // 회원가입 페이지로 이동
    @GetMapping("/signUp")
    public String signUpPage(Model model) {
        model.addAttribute("memberDTO", new MemberDTO());
        return "member/join";  // 회원가입 페이지
    }

    // 회원가입 처리
    @PostMapping("/signUp")
    public String signUp(@Valid @ModelAttribute MemberDTO memberDTO, Model model) {

        // 이메일 앞부분과 도메인 결합
        String email = memberDTO.getEmail() + "@" + memberDTO.getEmailDomain();
        memberDTO.setEmail(email);  // 합쳐진 이메일을 memberDTO에 설정


        // 아이디와 닉네임 중복 여부 확인
        if (memberService.isUserIdExists(memberDTO.getId())) {
            model.addAttribute("signUpFailMsg", "아이디가 중복입니다.");
            return "member/join "; // 폼 페이지에 머무르며 오류 메시지 표시
        }

        if (memberService.isNicknameExists(memberDTO.getNickname())) {
            model.addAttribute("signUpFailMsg", "닉네임이 중복입니다.");
            return "member/join"; // 폼 페이지에 머무르며 오류 메시지 표시
        }



        boolean isRegistered = memberService.registerMember(memberDTO);
        if (isRegistered) {
            model.addAttribute("signUpSuccessMsg", "회원가입이 완료되었습니다.");
            return "redirect:/member/signIn";  // 회원가입 후 로그인 페이지로 리다이렉트
        } else {
            model.addAttribute("signUpFailMsg", "회원가입에 실패하였습니다.");
            return "member/join";  // 회원가입 실패 시 회원가입 페이지로 돌아감
        }
    }

    // 아이디로 회원 조회
    @GetMapping("/find")
    public String findMember(Model model, @RequestParam("userId") String userId) {
        // 아이디로 회원 조회
        Member member = memberService.findMemberById(userId); // 실제 서비스에서 회원을 조회
        if (member != null) {
            String maskedId = maskId(member.getId()); // 아이디 마스킹 처리
            model.addAttribute("maskedId", maskedId);
        } else {
            model.addAttribute("findFailMsg", "해당 아이디를 찾을 수 없습니다.");
        }
        return "findMemberResult";  // 결과를 보여줄 뷰 이름
    }

    // 아이디의 앞 6자리만 보이고, 나머지는 '*'로 처리하는 메서드
    private String maskId(String id) {
        if (id.length() > 6) {
            return id.substring(0, 6) + "******"; // 앞 6자만 보이고 나머지 '*'로 처리
        }
        return id;
    }

    // 로그인 상태에 따라 버튼에 필요한 정보 제공 (로그인 상태 체크)
    @ModelAttribute
    public void addLoginInfo(HttpSession session, Model model) {
        // 로그인 상태 확인
        String userId = (String) session.getAttribute("userId");
        if (userId != null) {
            // 로그인 상태일 경우 세션에 userId 존재
            model.addAttribute("isLoggedIn", true);
        } else {
            // 로그인 안 된 상태
            model.addAttribute("isLoggedIn", false);
        }
    }

    // 아이디 중복 확인 처리
    @GetMapping("/checkDuplicateId")
    @ResponseBody  // JSON 응답을 보냄
    public Map<String, Boolean> checkDuplicateId(@RequestParam("id") String id) {
        boolean isDuplicate = memberService.isUserIdExists(id);
        Map<String, Boolean> response = new HashMap<>();
        response.put("isDuplicate", isDuplicate);
        return response;  // 중복 여부 반환
    }

    // 닉네임 중복 확인 처리
    @GetMapping("/checkDuplicateNickname")
    @ResponseBody  // JSON 응답을 보냄
    public Map<String, Boolean> checkDuplicateNickname(@RequestParam("nickname") String nickname) {
        boolean isDuplicate = memberService.isNicknameExists(nickname);
        Map<String, Boolean> response = new HashMap<>();
        response.put("isDuplicate", isDuplicate);
        return response;  // 중복 여부 반환
    }
}

//    // 아이디 중복 확인 처리
//    @GetMapping("/checkDuplicateId")
//    @ResponseBody  // JSON 응답을 보냄
//    public Map<String, Boolean> checkDuplicateId(@RequestParam("id") String id) {
//        boolean isDuplicate = memberService.isUserIdExists(id);
//        Map<String, Boolean> response = new HashMap<>();
//        response.put("isDuplicate", isDuplicate);
//        return response;  // 중복 여부 반환
//    }
//
//    // 닉네임 중복 확인 처리
//    @GetMapping("/checkDuplicateNickname")
//    @ResponseBody  // JSON 응답을 보냄
//    public Map<String, Boolean> checkDuplicateNickname(@RequestParam("nickname") String nickname) {
//        boolean isDuplicate = memberService.isNicknameExists(nickname);
//        Map<String, Boolean> response = new HashMap<>();
//        response.put("isDuplicate", isDuplicate);
//        return response;  // 중복 여부 반환
//    }
