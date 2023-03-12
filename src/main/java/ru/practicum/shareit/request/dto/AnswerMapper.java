package ru.practicum.shareit.request.dto;

import ru.practicum.shareit.request.model.Answer;

public class AnswerMapper {
    public static Answer dtoToAnswer(AnswerDto answerDto){
        return Answer.builder()
                .itemId(answerDto.getItemId())
                .itemName(answerDto.getItemName())
                .ownerId(answerDto.getOwnerId())
                .build();
    }
    public static AnswerDto answerToDto(Answer answer){
        return AnswerDto.builder()
                .itemId(answer.getItemId())
                .itemName(answer.getItemName())
                .ownerId(answer.getOwnerId())
                .build();
    }
}
