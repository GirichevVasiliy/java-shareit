package ru.practicum.shareit.request.dto;

import ru.practicum.shareit.request.model.Answer;

public class AnswerMapper {
    public static Answer dtoToAnswer(AnswerDto answerDto){
        return Answer.builder()
                .itemId(answerDto.getId())
                .itemName(answerDto.getName())
                //.ownerId(answerDto.getOwnerId())
                .build();
    }
    public static AnswerDto answerToDto(Answer answer){
        return AnswerDto.builder()
                .id(answer.getItemId())
                .name(answer.getItemName())
                //.ownerId(answer.getOwnerId())
                .available(answer.getAvailable())
                .description(answer.getDescription())
                .requestId(answer.getRequestId())
                .build();
    }
}
