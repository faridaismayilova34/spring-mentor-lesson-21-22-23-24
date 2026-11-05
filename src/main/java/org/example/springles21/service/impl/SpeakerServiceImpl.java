package org.example.springles21.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.springles21.dto.request.SpeakerRequestDto;
import org.example.springles21.dto.response.SpeakerResponseDto;
import org.example.springles21.dto.response.TalkResponseDto;
import org.example.springles21.entity.Speaker;
import org.example.springles21.exception.DuplicateResourceException;
import org.example.springles21.exception.ResourceNotFoundException;
import org.example.springles21.mapper.SpeakerMapper;
import org.example.springles21.mapper.TalkMapper;
import org.example.springles21.repository.SpeakerRepository;
import org.example.springles21.repository.TalkRepository;
import org.example.springles21.service.SpeakerService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class SpeakerServiceImpl implements SpeakerService {

    private final SpeakerRepository speakerRepository;
    private final TalkRepository talkRepository;
    private final SpeakerMapper speakerMapper;
    private final TalkMapper talkMapper;

    @Override
    @Transactional
    public SpeakerResponseDto createSpeaker(SpeakerRequestDto requestDto) {
        if (speakerRepository.existsByEmail(requestDto.getEmail())) {
            log.warn("Speaker creation failed: email {} already exists", requestDto.getEmail());
            throw new DuplicateResourceException("Speaker with email " + requestDto.getEmail() + " already exists");
        }
        Speaker speaker = speakerMapper.toEntity(requestDto);
        Speaker saved = speakerRepository.save(speaker);
        log.info("Speaker created with id {}", saved.getId());
        return speakerMapper.toDto(saved);
    }

    @Override
    public List<SpeakerResponseDto> getAllSpeakers() {
        log.debug("Fetching all speakers");
        return speakerMapper.toDtoList(speakerRepository.findAll());
    }

    @Override
    public SpeakerResponseDto getSpeakerById(Long id) {
        log.debug("Fetching speaker with id {}", id);
        Speaker speaker = speakerRepository.findById(id)
                .orElseThrow(() -> {
                    log.warn("Speaker with id {} not found", id);
                    return new ResourceNotFoundException("Speaker with id " + id + " not found");
                });
        return speakerMapper.toDto(speaker);
    }

    @Override
    public List<TalkResponseDto> getTalksBySpeakerId(Long speakerId) {
        log.debug("Fetching talks for speaker id {}", speakerId);
        if (!speakerRepository.existsById(speakerId)) {
            log.warn("Speaker with id {} not found when fetching talks", speakerId);
            throw new ResourceNotFoundException("Speaker with id " + speakerId + " not found");
        }
        return talkMapper.toDtoList(talkRepository.findBySpeakerId(speakerId));
    }
}