package ru.practicum.shareit.item.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.util.DefaultUriBuilderFactory;
import ru.practicum.shareit.client.BaseClient;
import ru.practicum.shareit.item.comment.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;

import java.util.Map;

public class ItemClient extends BaseClient {

    private static final String API_PREFIX = "/items";

    @Autowired
    public ItemClient(@Value("${share-server.url}") String serverUrl, RestTemplateBuilder builder) {
        super(
                builder
                        .uriTemplateHandler(new DefaultUriBuilderFactory(serverUrl + API_PREFIX))
                        .requestFactory(HttpComponentsClientHttpRequestFactory::new)
                        .build()
        );
    }

    public ResponseEntity<Object> addItem(ItemDto itemDto, Long userId) {
    return post("", userId, itemDto);
    }

    public ResponseEntity<Object> updateItem(Long itemId, ItemDto itemDto, Long userId) {
    return patch("/" + itemId, userId, itemDto);
    }

    public ResponseEntity<Object> getItemById(Long itemId, Long userId) {
    return get("/" + itemId, userId);
    }

    public ResponseEntity<Object> getItemsByUser(Long userId, Integer from, Integer size) {
        Map<String, Object> parameters = Map.of("from", from, "size", size);
        return get("?from={from}&size={size}", userId, parameters);
    }

    public ResponseEntity<Object> getAvailableItems(Long userId, String text, Integer from, Integer size) {
        return get()
    }

    public ResponseEntity<Object> addComment(Long itemId, Long authorId, CommentDto commentDto){
        return post()
    }
}