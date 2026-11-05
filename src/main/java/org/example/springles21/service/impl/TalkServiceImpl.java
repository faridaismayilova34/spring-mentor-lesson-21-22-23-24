package org.example.springles21.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.springles21.dto.request.TalkRequestDto;
import org.example.springles21.dto.response.TalkResponseDto;
import org.example.springles21.entity.Speaker;
import org.example.springles21.entity.Talk;
import org.example.springles21.exception.BusinessRuleException;
import org.example.springles21.exception.ResourceNotFoundException;
import org.example.springles21.mapper.TalkMapper;
import org.example.springles21.repository.SpeakerRepository;
import org.example.springles21.repository.TalkRepository;
import org.example.springles21.service.TalkService;

import org.example.springles21.service.specific.TalkSpecifications;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class TalkServiceImpl implements TalkService {

    private static final Set<String> ALLOWED_SORT_FIELDS = Set.of("title", "room", "durationMinutes");

    private final TalkRepository talkRepository;
    private final SpeakerRepository speakerRepository;
    private final TalkMapper talkMapper;

    private Talk findTalkOrThrow(Long id) {
        return talkRepository.findById(id)
                .orElseThrow(() -> {
                    log.warn("Talk with id {} not found", id);
                    return new ResourceNotFoundException("Talk with id " + id + " not found");
                });
    }

    private Speaker findSpeakerOrThrow(Long speakerId) {
        return speakerRepository.findById(speakerId)
                .orElseThrow(() -> {
                    log.warn("Speaker with id {} not found", speakerId);
                    return new ResourceNotFoundException("Speaker with id " + speakerId + " not found");
                });
    }

    private void validateDuration(Integer durationMinutes) {
        if (durationMinutes == null || durationMinutes < 15 || durationMinutes > 90) {
            log.warn("Talk duration {} is invalid", durationMinutes);
            throw new BusinessRuleException("A talk lasts between 15 and 90 minutes");
        }
    }

    private Pageable buildPageable(int page, int size, String sortBy, String direction) {
        if (size < 1 || size > 50) {
            log.warn("Invalid page size: {}", size);
            throw new BusinessRuleException("Size must be between 1 and 50");
        }
        if (page < 0) {
            log.warn("Invalid page number: {}", page);
            throw new BusinessRuleException("Page must be 0 or more");
        }
        if (sortBy == null || !ALLOWED_SORT_FIELDS.contains(sortBy)) {
            log.warn("Invalid sortBy field: {}", sortBy);
            throw new BusinessRuleException("sortBy must be one of: title, room, durationMinutes");
        }
        Sort.Direction dir;
        if ("asc".equalsIgnoreCase(direction)) {
            dir = Sort.Direction.ASC;
        } else if ("desc".equalsIgnoreCase(direction)) {
            dir = Sort.Direction.DESC;
        } else {
            log.warn("Invalid direction: {}", direction);
            throw new BusinessRuleException("Direction must be asc or desc");
        }
        // id tie-breaker: sort dəyərləri təkrarlananda səhifələr qarışmasın
        Sort sort = Sort.by(dir, sortBy).and(Sort.by(Sort.Direction.ASC, "id"));
        return PageRequest.of(page, size, sort);
    }

    @Override
    @Transactional
    public TalkResponseDto createTalk(TalkRequestDto requestDto) {
        Speaker speaker = findSpeakerOrThrow(requestDto.getSpeakerId());

        long talkCount = talkRepository.countBySpeakerId(speaker.getId());
        if (talkCount >= 3) {
            log.warn("Speaker {} already has {} talks", speaker.getId(), talkCount);
            throw new BusinessRuleException("A speaker can have at most 3 talks");
        }

        validateDuration(requestDto.getDurationMinutes());

        Talk talk = talkMapper.toEntity(requestDto);
        talk.setSpeaker(speaker);
        Talk saved = talkRepository.save(talk);
        log.info("Talk created with id {}", saved.getId());
        return talkMapper.toDto(saved);
    }

    @Override
    public Page<TalkResponseDto> getAllTalks(int page, int size, String sortBy, String direction) {
        log.debug("Fetching talks: page={}, size={}, sortBy={}", page, size, sortBy);
        Pageable pageable = buildPageable(page, size, sortBy, direction);
        return talkRepository.findAll(pageable).map(talkMapper::toDto);
    }

    @Override
    public Page<TalkResponseDto> searchTalks(String title, String room, Integer minDuration, Integer maxDuration,
                                             int page, int size, String sortBy, String direction) {
        log.debug("Searching talks: title={}, room={}, minDuration={}, maxDuration={}, page={}, size={}, sortBy={}",
                title, room, minDuration, maxDuration, page, size, sortBy);

        if (minDuration != null && maxDuration != null && minDuration > maxDuration) {
            log.warn("minDuration {} is greater than maxDuration {}", minDuration, maxDuration);
            throw new BusinessRuleException("minDuration must not be greater than maxDuration");
        }
        Pageable pageable = buildPageable(page, size, sortBy, direction);

        Specification<Talk> spec = Specification
                .where(TalkSpecifications.titleContains(title))
                .and(TalkSpecifications.inRoom(room))
                .and(TalkSpecifications.minDuration(minDuration))
                .and(TalkSpecifications.maxDuration(maxDuration));

        return talkRepository.findAll(spec, pageable).map(talkMapper::toDto);
    }

    @Override
    public TalkResponseDto getTalkById(Long id) {
        log.debug("Fetching talk {}", id);
        return talkMapper.toDto(findTalkOrThrow(id));
    }

    @Override
    @Transactional
    public TalkResponseDto updateTalk(Long id, TalkRequestDto requestDto) {
        log.debug("Updating talk {}", id);
        Talk talk = findTalkOrThrow(id);
        Speaker speaker = findSpeakerOrThrow(requestDto.getSpeakerId());

        validateDuration(requestDto.getDurationMinutes());

        talkMapper.updateTalkFromDto(requestDto, talk);
        talk.setSpeaker(speaker);
        Talk updated = talkRepository.save(talk);
        log.info("Talk updated with id {}", updated.getId());
        return talkMapper.toDto(updated);
    }

    @Override
    @Transactional
    public void deleteTalk(Long id) {
        log.debug("Deleting talk {}", id);
        Talk talk = findTalkOrThrow(id);
        talkRepository.delete(talk);
        log.info("Talk deleted with id {}", id);
    }
}