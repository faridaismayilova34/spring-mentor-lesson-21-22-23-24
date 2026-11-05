package org.example.springles21.dto.request;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TalkItemRequestDto {

    @NotBlank(message = "Title is required")
    @Size(min = 5, max = 150, message = "Title must be 5-150 characters")
    private String title;

    @NotBlank(message = "Room is required")
    private String room;

    @NotNull(message = "Duration is required")
    private Integer durationMinutes;

    @NotNull(message = "Talk must be scheduled in the future")
    @Future(message = "Talk must be scheduled in the future")
    private LocalDateTime scheduledAt;
}