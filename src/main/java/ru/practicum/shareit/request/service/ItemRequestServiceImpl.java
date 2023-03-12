package ru.practicum.shareit.request.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.exception.ResourceNotFoundException;
import ru.practicum.shareit.item.dto.ItemMapper;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.storage.ItemRepository;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.dto.ItemRequestMapper;
import ru.practicum.shareit.request.model.Answer;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.request.storage.ItemRequestRepository;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.storage.UserRepository;

import java.util.*;
import java.util.stream.Collectors;

@Service
@Slf4j
@Transactional(readOnly = true)
public class ItemRequestServiceImpl implements ItemRequestService {
    private final ItemRequestRepository itemRequestRepository;
    private final UserRepository userRepository;
    private final ItemRepository itemRepository;

    @Autowired
    public ItemRequestServiceImpl(ItemRequestRepository itemRequestRepository,
                                  UserRepository userRepository,
                                  ItemRepository itemRepository) {
        this.itemRequestRepository = itemRequestRepository;
        this.userRepository = userRepository;
        this.itemRepository = itemRepository;
    }
@Transactional
    @Override
    public ItemRequestDto addItemRequest(ItemRequestDto itemRequestDto, Long userId) {
        log.info("Получен запрос на создание запроса от пользователя с Id = " + userId);
        Optional<User> user = Optional.ofNullable(userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException(" Пользователь с " + userId + " не найден")));
        ItemRequest request = ItemRequestMapper.itemRequestCreate(itemRequestDto, user.get());
        return ItemRequestMapper.itemRequestToDto(itemRequestRepository.save(request));
    }

    @Override
    public List<ItemRequestDto> getAllItemRequestsUser(Long userId) {
        Optional<User> user = Optional.ofNullable(userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException(" Пользователь с " + userId + " не найден")));

        List<ItemRequest> itemRequestsForUser = getItemRequest(userId);
        Map<Long, List<Answer>> answersForItemRequest = getAnswers(itemRequestsForUser);
        return creatingItemRequestDtoWithAnswers(answersForItemRequest, itemRequestsForUser);
    }

    @Override
    public Page<ItemRequestDto> getAllItemRequests(Long userId, Pageable pageable) {
        return null;
    }

    @Override
    public ItemRequestDto getItemRequestById(Long itemRequestId, Long userId) {
        return null;
    }
    private List<ItemRequest> getItemRequest(Long userId){
        return itemRequestRepository.findByRequestorIdOrderByCreatedDesc(userId);
    }
    private Map<Long, List<Answer>> getAnswers(List<ItemRequest> itemRequest) {
        Map<Long, List<Item>> answers = itemRepository.findAllByRequestIn(itemRequest).stream()
                .collect(Collectors.groupingBy(i->i.getRequest().getId()));
        Map<Long, List<Answer>> answerList = new HashMap<>();
       for(Long i: answers.keySet()){
           List<Answer> itemToAnswerList = answers.get(i).stream().map(ItemMapper::answerCreateForItem).collect(Collectors.toList());
           answerList.put(i, itemToAnswerList);
       }
        return answerList;
    }
    private  List<ItemRequestDto> creatingItemRequestDtoWithAnswers(Map<Long, List<Answer>> answersForItemRequest,
                                                                    List<ItemRequest> itemRequestsForUser){
        List<ItemRequestDto> itemRequestDtos = new ArrayList<>();
        if (!answersForItemRequest.isEmpty()) {
            for (ItemRequest itemRequest : itemRequestsForUser) {
                ItemRequestDto ItemRequestDto = ItemRequestMapper
                        .itemRequestAndListAnswersToDto(itemRequest, answersForItemRequest.get(itemRequest.getId()));
                itemRequestDtos.add(ItemRequestDto);
            }
        } else {
            for (ItemRequest itemRequest : itemRequestsForUser) {
                ItemRequestDto ItemRequestDto = ItemRequestMapper
                        .itemRequestAndListAnswersToDto(itemRequest, new ArrayList<>());
                itemRequestDtos.add(ItemRequestDto);
            }
        }
        return itemRequestDtos;
    }
}
