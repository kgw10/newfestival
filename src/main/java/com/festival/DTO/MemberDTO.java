package com.festival.DTO;

import com.festival.Entity.Member;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Max;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MemberDTO {
    private String name;

    private String id;         // 사용자 ID (String 타입)

    private String password;

    private String email;
    private String emailDomain;
    private String local;

    private String nickname;

    private Integer age;

    private String phone_number;

    @NotNull(message = "동의하지 않았습니다.")
    private Boolean agreeTerms; // 변경된 부분

    public MemberDTO(Member member){
        this.id=member.getId();
        this.nickname= member.getNickname();
    }
}