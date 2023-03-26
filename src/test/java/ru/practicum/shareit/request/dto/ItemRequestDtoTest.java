package ru.practicum.shareit.request.dto;

import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

@JsonTest
public class ItemRequestDtoTest {
    @Autowired
    private JacksonTester<ItemRequestDto> json;
    private static final LocalDateTime CREATE = LocalDateTime.of(2024, 10, 23, 17, 19, 33);
    private final List<AnswerDto> items = new ArrayList<>();

    @Test
    @SneakyThrows
    public void itemRequestDtoTest() {
        ItemRequestDto itemRequest = new ItemRequestDto(1L, "desc", CREATE, items);
        String testData = String.format("{\"id\":1,\"description\":\"desc\",\"created\":\"2024-10-23T17:19:33\",\"items\":[]}");
        ItemRequestDto itemRequestDto = json.parseObject(testData);
        assertThat(itemRequestDto.equals(itemRequest)).isTrue();
    }
}
