package com.festival.Service;

import com.festival.DTO.FestivalDTO;
import com.festival.Entity.Festival;
import com.festival.Repository.FestivalRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class FestivalService {
    private final FestivalRepository festivalRepository;
    public List<FestivalDTO> getFestivalsByDate(String date) {
        List<Festival> festivals=festivalRepository.findByEventstartdateLessThanEqualAndEventenddateGreaterThanEqualOrderByEventenddate(date, date);
        return festivals.stream().map(FestivalDTO::new).collect(Collectors.toList());
    }
    public List<FestivalDTO> searchFestivals(String searchTerm) {
        String search=searchTerm.trim();
        return festivalRepository.findByTitleContainingOrAddr1Containing(search, search)
                .stream().distinct().map(FestivalDTO::new).collect(Collectors.toList());
    }
    public List<FestivalDTO> fetchFestivals() {
        List<Festival> festivals=festivalRepository.findAll();
        return festivals.stream().map(FestivalDTO::new).collect(Collectors.toList());
    }
    public FestivalDTO festivalInfo(Long id) {
        Festival festival=festivalRepository.findById(id).orElseThrow(()->new RuntimeException("축제 정보가 없습니다"));
        return new FestivalDTO(festival);
    }
}
