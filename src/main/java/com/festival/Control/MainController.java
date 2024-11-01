package com.festival.Control;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class MainController {
    @GetMapping("/")
    public String home(){
        return "index";
    }
    @GetMapping("/festival")
    public String festival(){
        return "festival/festival";
    }
    @GetMapping("/party")
    public String party(){
        return "party/party";
    }
    @GetMapping("/diary")
    public String diary(){
        return "diary/diary";
    }
}
