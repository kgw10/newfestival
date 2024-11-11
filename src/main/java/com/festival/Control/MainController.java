package com.festival.Control;

import com.festival.DTO.FestivalDTO;
import com.festival.Service.FestivalService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import javax.servlet.http.HttpSession;
import java.util.List;

@Controller
@RequiredArgsConstructor
public class MainController {

    private final FestivalService festivalService;

    @GetMapping("/")
    public String home(Model model, HttpSession session) {
        List<FestivalDTO> festivals = festivalService.fetchFestivals();
        model.addAttribute("festivals", festivals);
        model.addAttribute("totalFestivals",festivals.size());
        Object nicknames=session.getAttribute("nickname");
        if(nicknames!=null){
            String nickname=nicknames.toString();
            model.addAttribute("nickname",nickname);
        }
        return "index";
    }
    @GetMapping("/mypage")
    public String mypage() {
        return "member/mypage";
    }
//    @GetMapping("/festival")
//    public String festival() {
//        return "festival/festival";
//    }
    @GetMapping("/party")
    public String party() {
        return "party/board";
    }
    @GetMapping("/diary")
    public String diary() {
        return "diary/diary";
    }
}
