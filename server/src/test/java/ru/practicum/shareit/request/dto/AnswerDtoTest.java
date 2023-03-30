package ru.practicum.shareit.request.dto;

import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

@JsonTest
public class AnswerDtoTest {
    @Autowired
    private JacksonTester<AnswerDto> json;
    private final Long requestId = 1L;

    @Test
    @SneakyThrows
    public void answerDtoTest() {
        AnswerDto answer = new AnswerDto(1L, "answer", "desc", true, requestId);
        String testData = String.format("{\"id\":1,\"name\":\"answer\",\"description\":\"desc\",\"available\":true,\"requestId\":1}");
        AnswerDto answerDto = json.parseObject(testData);
        assertThat(answerDto.equals(answer)).isTrue();
    }
}
