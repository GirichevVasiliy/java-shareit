package ru.practicum.shareit.request.dto;

import ru.practicum.shareit.request.model.Answer;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.dto.UserMapper;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class ItemRequestMapper {
    public static ItemRequestDto itemRequestDtoCreate(ItemRequestDto itemRequestDto){
        return ItemRequestDto.builder()
                .description(itemRequestDto.getDescription())
                .created(LocalDateTime.now())
                .build();
    }
    public static ItemRequestDto itemRequestAndListAnswersToDto(ItemRequest itemRequest, List<Answer> answers){
        return ItemRequestDto.builder()
                .id(itemRequest.getId())
                .description(itemRequest.getDescription())
                .created(itemRequest.getCreated())
                .requestorId(itemRequest.getRequestor().getId())
                .answers(answers.stream().map(AnswerMapper::answerToDto).collect(Collectors.toList()))
                .build();
    }
    public static ItemRequestDto itemRequestToDto(ItemRequest itemRequest){
        return ItemRequestDto.builder()
                .id(itemRequest.getId())
                .description(itemRequest.getDescription())
                .created(itemRequest.getCreated())
                .requestorId(itemRequest.getRequestor().getId())
                .answers(new ArrayList<>())
                .build();
    }
    public static ItemRequest dtoToItemRequestUserDto(ItemRequestDto itemRequestDto, UserDto userDto){
        return ItemRequest.builder()
                .description(itemRequestDto.getDescription())
                .created(itemRequestDto.getCreated())
                .requestor(UserMapper.dtoToUser(userDto))
                .build();
    }
    public static ItemRequest itemRequestCreate(ItemRequestDto itemRequestDto, User user){
        return ItemRequest.builder()
                .description(itemRequestDto.getDescription())
                .created(itemRequestDto.getCreated())
                .requestor(user)
                .build();
    }
}
