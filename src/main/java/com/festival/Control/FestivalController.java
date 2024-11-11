package com.festival.Control;

import com.festival.DTO.FestivalDTO;
import com.festival.Service.FestivalService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.List;

@Controller
@RequestMapping("/festival")
@RequiredArgsConstructor
public class FestivalController {
    private final FestivalService festivalService;
    @GetMapping({"","/"})
    public String festival(){
        return "festival/festival";
    }
    @GetMapping("/data")
    @ResponseBody
    public List<FestivalDTO> getFestivalsByDate(@RequestParam String date){
        return festivalService.getFestivalsByDate(date);
    }
//    public List<FestivalDTO> getFestivalDate(){
//        List<FestivalDTO> festivals=festivalService.fetchFestivals();
//        if(festivals==null||festivals.isEmpty()){
//            System.out.println("축제를 찾을 수 없습니다.");
//            return Collections.emptyList();
//        }
//        return festivals;
//    }

//    public ResponseEntity<List<FestivalDTO>> getFestivalsByDate(@RequestParam("date") LocalDate date){
//        List<FestivalDTO> festivals=festivalService.getFestivalsByDate(date);
//        return ResponseEntity.ok(festivals);
//    }
    @GetMapping("/search")
    public String search(@RequestParam(value="keyword", required=false) String searchTerm, Model model){
        if(searchTerm==null||searchTerm.trim().isEmpty()){
            model.addAttribute("festivals",Collections.emptyList());
        }else{
            List<FestivalDTO> searchResults=festivalService.searchFestivals(searchTerm);
            model.addAttribute("festivals",searchResults);
        }
        return "festival/search";
    }
    @GetMapping("info/{id}")
    public String festivalinfo(@PathVariable Long id, Model model){
        FestivalDTO festival=festivalService.festivalInfo(id);
        model.addAttribute("festival",festival);
        return "festival/festivalinfo";
    }
}
