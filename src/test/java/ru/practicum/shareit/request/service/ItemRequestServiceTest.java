package ru.practicum.shareit.request.service;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import ru.practicum.shareit.exception.ResourceNotFoundException;
import ru.practicum.shareit.item.service.ItemService;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.dto.ItemRequestMapper;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.service.UserServiceImpl;
import ru.practicum.shareit.util.CreatePageable;

import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
@AutoConfigureTestDatabase
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class ItemRequestServiceTest {
    @Autowired
    ItemRequestServiceImpl itemRequestService;
    @Autowired
    UserServiceImpl userService;
    @Autowired
    ItemService itemService;
    private UserDto userDto1;
    private UserDto userDto2;
    final Long userId1 = 1L;
    private ItemRequestDto itemRequestDto1;
    private ItemRequestDto itemRequestDto2;

    @BeforeEach
    private void init() {
        userDto1 = UserDto.builder()
                .id(1L)
                .name("user1")
                .email("y@1email.ru")
                .build();
        userDto2 = UserDto.builder()
                .id(2L)
                .name("user2")
                .email("y@2email.ru")
                .build();
        itemRequestDto1 = ItemRequestDto.builder()
                .description("req1")
                .build();
        itemRequestDto2 = ItemRequestDto.builder()
                .description("req2")
                .build();
    }

    @Test
    public void itemRequestIntegrationTest() {
        UserDto saveUserDto1 = userService.addUser(userDto1);
        UserDto saveUserDto2 = userService.addUser(userDto2);

        final Long badIdUser = 99L;
        assertThrows(ResourceNotFoundException.class, () -> itemRequestService.addItemRequest(ItemRequestMapper.itemRequestDtoCreate(itemRequestDto1),
                badIdUser));

        ItemRequestDto saveItemRequestDto1 = itemRequestService.addItemRequest(ItemRequestMapper.itemRequestDtoCreate(itemRequestDto1),
                saveUserDto1.getId());
        assertThat(saveItemRequestDto1.getId().equals(1L)).isTrue();
        assertThat(saveItemRequestDto1.getDescription().equals(itemRequestDto1.getDescription())).isTrue();

        ItemRequestDto saveItemRequestDto2 = itemRequestService.addItemRequest(ItemRequestMapper.itemRequestDtoCreate(itemRequestDto2),
                saveUserDto1.getId());
        assertThat(saveItemRequestDto2.getId().equals(2L)).isTrue();
        assertThat(saveItemRequestDto2.getDescription().equals(itemRequestDto2.getDescription())).isTrue();

        final int size2 = 2;
        final int idRequest0 = 0;
        final int idRequest1 = 1;
        List<ItemRequestDto> requestDtoListForUser1 = itemRequestService.getAllItemRequestsUser(userId1);
        assertThat(requestDtoListForUser1.size() == size2).isTrue();
        assertThat(requestDtoListForUser1.get(idRequest0).getId().equals(saveItemRequestDto2.getId())).isTrue();
        assertThat(requestDtoListForUser1.get(idRequest0).getDescription().equals(saveItemRequestDto2.getDescription())).isTrue();
        assertThat(requestDtoListForUser1.get(idRequest1).getId().equals(saveItemRequestDto1.getId())).isTrue();
        assertThat(requestDtoListForUser1.get(idRequest1).getDescription().equals(saveItemRequestDto1.getDescription())).isTrue();

        final int size0 = 0;
        List<ItemRequestDto> requestDtoListForUser2 = itemRequestService.getAllItemRequestsUser(saveUserDto2.getId());
        assertThat(requestDtoListForUser2.size() == size0).isTrue();

        final Long itemRequestId = 1L;
        ItemRequestDto findItemRequestDtoById = itemRequestService.getItemRequestById(itemRequestId, userId1);
        assertThat(findItemRequestDtoById.getId().equals(saveItemRequestDto1.getId())).isTrue();
        assertThat(findItemRequestDtoById.getDescription().equals(saveItemRequestDto1.getDescription())).isTrue();

        List<ItemRequestDto> dtoList = itemRequestService.getAllItemRequests(2L, CreatePageable.getPageable(0, 2));
        assertThat(dtoList.size() == size2).isTrue();
    }
}
