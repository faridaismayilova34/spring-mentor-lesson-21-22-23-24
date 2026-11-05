package org.example.springles21.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.springles21.dto.request.SpeakerWithTalksRequestDto;
import org.example.springles21.dto.response.SpeakerWithTalksResponseDto;
import org.example.springles21.service.ConferenceService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/speakers")
@RequiredArgsConstructor
@Tag(name = "Conference Controller", description = "Use cases that touch several entities at once")
public class ConferenceController {

    private final ConferenceService conferenceService;

    @PostMapping("/with-talks")
    @Operation(summary = "Register a speaker together with talks",
            description = "Creates a speaker and all of their talks in one transaction (all or nothing)")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Speaker and talks created",
                    content = @Content(schema = @Schema(implementation = SpeakerWithTalksResponseDto.class))),
            @ApiResponse(responseCode = "400", description = "Validation error or business rule violation"),
            @ApiResponse(responseCode = "409", description = "Duplicate email conflict"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<SpeakerWithTalksResponseDto> registerSpeakerWithTalks(
            @Valid @RequestBody SpeakerWithTalksRequestDto requestDto) {
        return new ResponseEntity<>(conferenceService.registerSpeakerWithTalks(requestDto), HttpStatus.CREATED);
    }
}
