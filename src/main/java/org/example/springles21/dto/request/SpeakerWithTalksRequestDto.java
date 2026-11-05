package org.example.springles21.dto.request;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.util.List;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SpeakerWithTalksRequestDto {

    @NotNull(message = "Speaker is required")
    @Valid
    private SpeakerRequestDto speaker;

    @NotEmpty(message = "At least one talk is required")
    @Valid
    private List<TalkItemRequestDto> talks;
}