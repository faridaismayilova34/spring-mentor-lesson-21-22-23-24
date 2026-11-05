package org.example.springles21.service;

import org.example.springles21.dto.request.SpeakerWithTalksRequestDto;
import org.example.springles21.dto.response.SpeakerWithTalksResponseDto;

public interface ConferenceService {
    SpeakerWithTalksResponseDto registerSpeakerWithTalks(SpeakerWithTalksRequestDto requestDto);
}