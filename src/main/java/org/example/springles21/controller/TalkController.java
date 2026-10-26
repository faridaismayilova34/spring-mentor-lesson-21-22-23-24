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
import org.example.springles21.dto.request.TalkRequestDto;
import org.example.springles21.dto.response.TalkResponseDto;
import org.example.springles21.service.TalkService;
import org.example.springles21.service.impl.TalkServiceImpl;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Validated
@RestController
@RequestMapping("/api/talks")
@RequiredArgsConstructor
@Tag(name = "Talk Controller", description = "APIs for managing talks (GET, POST, PUT, DELETE)")
public class TalkController {

    private final TalkService talkService;

    @PostMapping
    @Operation(summary = "Create a new talk", description = "Creates a new talk session")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Talk successfully created",
                    content = @Content(schema = @Schema(implementation = TalkResponseDto.class))),
            @ApiResponse(responseCode = "400", description = "Validation error or business rule violation"),
            @ApiResponse(responseCode = "404", description = "Speaker not found"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<TalkResponseDto> createTalk(@Valid @RequestBody TalkRequestDto requestDto) {
        return new ResponseEntity<>(talkService.createTalk(requestDto), HttpStatus.CREATED);
    }


    @GetMapping
    @Operation(summary = "Get all talks", description = "Retrieves a list of all scheduled talks")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Talks retrieved successfully",
                    content = @Content(array = @ArraySchema(schema = @Schema(implementation = TalkResponseDto.class)))),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<List<TalkResponseDto>> getAllTalks() {
        return ResponseEntity.ok(talkService.getAllTalks());
    }


    @GetMapping("/{id}")
    @Operation(summary = "Get talk by ID", description = "Retrieves details of a talk by its ID")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Talk found",
                    content = @Content(schema = @Schema(implementation = TalkResponseDto.class))),
            @ApiResponse(responseCode = "400", description = "Invalid ID format"),
            @ApiResponse(responseCode = "404", description = "Talk not found"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<TalkResponseDto> getTalksById(@PathVariable
                                                        @Positive(message = "Id must be a positive number") Long id) {
        return ResponseEntity.ok(talkService.getTalkById(id));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update a talk", description = "Updates an existing talk by its ID")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Talk successfully updated",
                    content = @Content(schema = @Schema(implementation = TalkResponseDto.class))),
            @ApiResponse(responseCode = "400", description = "Validation error or rule violation"),
            @ApiResponse(responseCode = "404", description = "Talk or speaker not found"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<TalkResponseDto> updateTalk(@PathVariable @Positive(message = "Id must be a positive number") Long id,
                                                      @Valid @RequestBody TalkRequestDto requestDto) {
        return ResponseEntity.ok(talkService.updateTalk(id, requestDto));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete a talk", description = "Deletes a talk session by its ID")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Talk successfully deleted (No Content)"),
            @ApiResponse(responseCode = "400", description = "Invalid ID format"),
            @ApiResponse(responseCode = "404", description = "Talk not found"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<Void> deleteTalk(@PathVariable
                                           @Positive(message = "Id must be a positive number") Long id) {
        talkService.deleteTalk(id);
        return ResponseEntity.noContent().build();
    }
}
