package ru.practicum.shareit.request.dto;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import ru.practicum.shareit.request.model.Answer;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

class AnswerMapperTest {
    private Answer answer;

    @BeforeEach
    private void init() {
        answer = Answer.builder()
                .itemId(1L)
                .itemName("name")
                .available(true)
                .description("description")
                .requestId(1L)
                .build();
    }

    @Test
    @DisplayName("Тест перевода Answer в AnswerDto")
    void answerToDtoAnswerValidTest() {
        AnswerDto answerDto = AnswerMapper.answerToDto(answer);
        assertThat(answerDto.getId().equals(answer.getItemId())).isTrue();
        assertThat(answerDto.getAvailable().equals(answer.getAvailable())).isTrue();
        assertThat(answerDto.getRequestId().equals(answer.getRequestId())).isTrue();
        assertThat(answerDto.getName().equals(answer.getItemName())).isTrue();
        assertThat(answerDto.getDescription().equals(answer.getDescription())).isTrue();
    }

    @Test
    @DisplayName("Тест перевода Answer available = Null в AnswerDto")
    void answerToDtoTestAvailableIsNull() {
        answer = Answer.builder()
                .itemId(1L)
                .itemName("name")
                .available(null)
                .description("description")
                .requestId(1L)
                .build();
        AnswerDto answerDto = AnswerMapper.answerToDto(answer);
        assertThat(answerDto.getId().equals(answer.getItemId())).isTrue();
        assertThat(answerDto.getAvailable() == null).isTrue();
        assertThat(answerDto.getRequestId().equals(answer.getRequestId())).isTrue();
        assertThat(answerDto.getName().equals(answer.getItemName())).isTrue();
        assertThat(answerDto.getDescription().equals(answer.getDescription())).isTrue();
    }

    @Test
    @DisplayName("Тест перевода Answer Name = Null в AnswerDto")
    void answerToDtoTestNameIsNull() {
        answer = Answer.builder()
                .itemId(1L)
                .itemName(null)
                .available(true)
                .description("description")
                .requestId(1L)
                .build();
        AnswerDto answerDto = AnswerMapper.answerToDto(answer);
        assertThat(answerDto.getId().equals(answer.getItemId())).isTrue();
        assertThat(answerDto.getAvailable().equals(answer.getAvailable())).isTrue();
        assertThat(answerDto.getRequestId().equals(answer.getRequestId())).isTrue();
        assertThat(answerDto.getName() == null).isTrue();
        assertThat(answerDto.getDescription().equals(answer.getDescription())).isTrue();
    }

    @Test
    @DisplayName("Тест перевода Answer description = Null в AnswerDto")
    void answerToDtoTestDescriptionIsNull() {
        answer = Answer.builder()
                .itemId(1L)
                .itemName("name")
                .available(true)
                .description(null)
                .requestId(1L)
                .build();
        AnswerDto answerDto = AnswerMapper.answerToDto(answer);
        assertThat(answerDto.getId().equals(answer.getItemId())).isTrue();
        assertThat(answerDto.getAvailable().equals(answer.getAvailable())).isTrue();
        assertThat(answerDto.getRequestId().equals(answer.getRequestId())).isTrue();
        assertThat(answerDto.getName().equals(answer.getItemName())).isTrue();
        assertThat(answerDto.getDescription() == null).isTrue();
    }

    @Test
    @DisplayName("Тест перевода Answer requestId = Null в AnswerDto")
    void answerToDtoTestRequestIdIsNull() {
        answer = Answer.builder()
                .itemId(1L)
                .itemName("name")
                .available(true)
                .description("description")
                .requestId(null)
                .build();
        AnswerDto answerDto = AnswerMapper.answerToDto(answer);
        assertThat(answerDto.getId().equals(answer.getItemId())).isTrue();
        assertThat(answerDto.getAvailable().equals(answer.getAvailable())).isTrue();
        assertThat(answerDto.getRequestId() == null).isTrue();
        assertThat(answerDto.getName().equals(answer.getItemName())).isTrue();
        assertThat(answerDto.getDescription().equals(answer.getDescription())).isTrue();
    }
}