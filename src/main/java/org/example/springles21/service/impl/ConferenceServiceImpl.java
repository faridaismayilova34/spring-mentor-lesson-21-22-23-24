package org.example.springles21.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.springles21.dto.request.SpeakerWithTalksRequestDto;
import org.example.springles21.dto.request.TalkItemRequestDto;
import org.example.springles21.dto.request.TalkRequestDto;
import org.example.springles21.dto.response.SpeakerResponseDto;
import org.example.springles21.dto.response.SpeakerWithTalksResponseDto;
import org.example.springles21.dto.response.TalkResponseDto;
import org.example.springles21.service.ConferenceService;
import org.example.springles21.service.SpeakerService;
import org.example.springles21.service.TalkService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class ConferenceServiceImpl implements ConferenceService {

    private final SpeakerService speakerService;
    private final TalkService talkService;

    @Override
    @Transactional   // EKSPERİMENT: ikinci yoxlamada bu sətri şərhə al
    public SpeakerWithTalksResponseDto registerSpeakerWithTalks(SpeakerWithTalksRequestDto requestDto) {
        log.debug("Registering speaker {} with {} talks",
                requestDto.getSpeaker().getEmail(), requestDto.getTalks().size());

        SpeakerResponseDto speaker = speakerService.createSpeaker(requestDto.getSpeaker());

        List<TalkResponseDto> talks = new ArrayList<>();
        for (TalkItemRequestDto item : requestDto.getTalks()) {
            TalkRequestDto talkDto = TalkRequestDto.builder()
                    .title(item.getTitle())
                    .room(item.getRoom())
                    .durationMinutes(item.getDurationMinutes())
                    .scheduledAt(item.getScheduledAt())
                    .speakerId(speaker.getId())
                    .build();
            talks.add(talkService.createTalk(talkDto));
        }

        log.info("Speaker {} registered with {} talks", speaker.getId(), talks.size());
        return new SpeakerWithTalksResponseDto(speaker, talks);
    }
}