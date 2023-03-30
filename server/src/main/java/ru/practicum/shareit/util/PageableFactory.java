package ru.practicum.shareit.util;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

public class PageableFactory {
    public static Pageable getPageableSortDescStart(int from, int size) {
        Sort sortByStart = Sort.by(Sort.Direction.DESC, "start");
        Pageable pageable = PageRequest.of(from / size, size, sortByStart);
        return pageable;
    }

    public static Pageable getPageable(int from, int size) {
        Pageable pageable = PageRequest.of(from / size, size);
        return pageable;
    }

    public static Pageable getPageableSortDescCreated(int from, int size) {
        Sort sortByStart = Sort.by(Sort.Direction.DESC, "created");
        Pageable pageable = PageRequest.of(from / size, size, sortByStart);
        return pageable;
    }
}
