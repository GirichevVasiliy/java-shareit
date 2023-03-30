package ru.practicum.shareit.util;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import ru.practicum.shareit.exception.ValidationForPageableException;

public class PageableFactory {
    public static Pageable getPageableSortDescStart(int from, int size) {
        if (from < 0 || size < 0) {
            throw new ValidationForPageableException("Неверно заданы данные для поиска");
        }
        Sort sortByStart = Sort.by(Sort.Direction.DESC, "start");
        Pageable pageable = PageRequest.of(from / size, size, sortByStart);
        return pageable;
    }

    public static Pageable getPageable(int from, int size) {
        if (from < 0 || size < 0) {
            throw new ValidationForPageableException("Неверно заданы данные для поиска");
        }
        Pageable pageable = PageRequest.of(from / size, size);
        return pageable;
    }

    public static Pageable getPageableSortDescCreated(int from, int size) {
        if (from < 0 || size < 0) {
            throw new ValidationForPageableException("Неверно заданы данные для поиска");
        }
        Sort sortByStart = Sort.by(Sort.Direction.DESC, "created");
        Pageable pageable = PageRequest.of(from / size, size, sortByStart);
        return pageable;
    }
}
