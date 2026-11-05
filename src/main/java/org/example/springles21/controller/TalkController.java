package org.example.springles21.controller;

import io.swagger.v3.oas.annotations.Operation;
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
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

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
    @Operation(summary = "Get all talks (paged)",
            description = "Returns a page of talks. size: 1-50, sortBy: title | room | durationMinutes, direction: asc | desc")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Talks retrieved successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid page, size, sortBy or direction"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<Page<TalkResponseDto>> getAllTalks(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "title") String sortBy,
            @RequestParam(defaultValue = "asc") String direction) {
        return ResponseEntity.ok(talkService.getAllTalks(page, size, sortBy, direction));
    }

    @GetMapping("/search")
    @Operation(summary = "Search talks",
            description = "Optional filters: title (contains), room (exact), minDuration, maxDuration. Paging params as in GET /api/talks")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Matching talks retrieved successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid paging params or minDuration > maxDuration"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<Page<TalkResponseDto>> searchTalks(
            @RequestParam(required = false) String title,
            @RequestParam(required = false) String room,
            @RequestParam(required = false) Integer minDuration,
            @RequestParam(required = false) Integer maxDuration,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "title") String sortBy,
            @RequestParam(defaultValue = "asc") String direction) {
        return ResponseEntity.ok(talkService.searchTalks(title, room, minDuration, maxDuration,
                page, size, sortBy, direction));
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