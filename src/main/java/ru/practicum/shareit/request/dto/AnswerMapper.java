package ru.practicum.shareit.request.dto;

import ru.practicum.shareit.request.model.Answer;

public class AnswerMapper {
    public static AnswerDto answerToDto(Answer answer) {
        return AnswerDto.builder()
                .id(answer.getItemId())
                .name(answer.getItemName())
                .available(answer.getAvailable())
                .description(answer.getDescription())
                .requestId(answer.getRequestId())
                .build();
    }
}
