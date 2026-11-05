package org.example.springles21.dto.response;

import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class SpeakerWithTalksResponseDto {
    private SpeakerResponseDto speaker;
    private List<TalkResponseDto> talks;
}