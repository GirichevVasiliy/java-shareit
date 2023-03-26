package ru.practicum.shareit.request.service;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;
import ru.practicum.shareit.request.dto.ItemRequestDto;

import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@SpringBootTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class ItemRequestServiceTest {
    @Autowired
    ItemRequestServiceImpl itemRequestService;

    @Test
    @Sql(value = {"classpath:forTest.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(value = {"classpath:schema.sql"}, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    public void integrationTestGetAllItemRequestsUser() {
        final Long userId = 1L;
        final int size = 3;
        List<ItemRequestDto> requestDtoList = itemRequestService.getAllItemRequestsUser(userId);
        assertThat(requestDtoList.size() == size).isTrue();
    }
}
