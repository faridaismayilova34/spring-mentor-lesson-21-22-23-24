package org.example.springles21.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import org.example.springles21.dto.request.SpeakerRequestDto;
import org.example.springles21.dto.response.SpeakerResponseDto;
import org.example.springles21.dto.response.TalkResponseDto;
import org.example.springles21.service.SpeakerService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Validated
@RestController
@RequestMapping("/api/speakers")
@RequiredArgsConstructor
@Tag(name = "Speaker Controller", description = "API for managing speakers")
public class SpeakerController {

    private final SpeakerService speakerService;

    @PostMapping
    @Operation(summary = "Create a new speaker", description = "Creates and registers a new speaker")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Speaker successfully created",
                    content = @Content(schema = @Schema(implementation = SpeakerResponseDto.class))),
            @ApiResponse(responseCode = "400", description = "Validation error / Bad request"),
            @ApiResponse(responseCode = "409", description = "Duplicate email conflict"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<SpeakerResponseDto> createSpeaker(@Valid @RequestBody SpeakerRequestDto requestDto) {
        return new ResponseEntity<>(speakerService.createSpeaker(requestDto), HttpStatus.CREATED);
    }


    @GetMapping
    @Operation(summary = "Get all speakers", description = "Retrieves a list of all existing speakers")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Speakers retrieved successfully",
                    content = @Content(array = @ArraySchema(schema = @Schema(implementation = SpeakerResponseDto.class)))),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<List<SpeakerResponseDto>> getAllSpeakers() {
        return ResponseEntity.ok(speakerService.getAllSpeakers());
    }


    @GetMapping("/{id}")
    @Operation(summary = "Get speaker by ID", description = "Retrieves details of a specific speaker by ID")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Speaker found",
                    content = @Content(schema = @Schema(implementation = SpeakerResponseDto.class))),
            @ApiResponse(responseCode = "400", description = "Invalid ID format"),
            @ApiResponse(responseCode = "404", description = "Speaker not found"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<SpeakerResponseDto> getSpeakerById(@PathVariable
                                                             @Positive(message = "Id must be a positive number") Long id) {
        return ResponseEntity.ok(speakerService.getSpeakerById(id));
    }


    @GetMapping("/{id}/talks")
    @Operation(summary = "Get talks by speaker ID", description = "Retrieves all talks associated with a specific speaker ID")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Talks retrieved successfully",
                    content = @Content(array = @ArraySchema(schema = @Schema(implementation = TalkResponseDto.class)))),
            @ApiResponse(responseCode = "400", description = "Invalid ID format"),
            @ApiResponse(responseCode = "404", description = "Speaker not found"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<List<TalkResponseDto>> getTalksBySpeakerId(@PathVariable
                                                                     @Positive(message = "Id must be a positive number") Long id) {
        return ResponseEntity.ok(speakerService.getTalksBySpeakerId(id));
    }
}