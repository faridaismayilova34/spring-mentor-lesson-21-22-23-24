package org.example.springles21.service;

import org.example.springles21.dto.request.TalkRequestDto;
import org.example.springles21.dto.response.TalkResponseDto;

import org.springframework.data.domain.Page;

public interface TalkService {
    TalkResponseDto createTalk(TalkRequestDto requestDto);

    Page<TalkResponseDto> getAllTalks(int page, int size, String sortBy, String direction);

    Page<TalkResponseDto> searchTalks(String title, String room, Integer minDuration, Integer maxDuration,
                                      int page, int size, String sortBy, String direction);

    TalkResponseDto getTalkById(Long id);
    TalkResponseDto updateTalk(Long id, TalkRequestDto requestDto);
    void deleteTalk(Long id);
}






//public interface TalkService {
//    TalkResponseDto createTalk(TalkRequestDto requestDto);
//    List<TalkResponseDto> getAllTalks();
//    TalkResponseDto getTalkById(Long id);
//    TalkResponseDto updateTalk(Long id, TalkRequestDto requestDto);
//    void deleteTalk(Long id);
//}
