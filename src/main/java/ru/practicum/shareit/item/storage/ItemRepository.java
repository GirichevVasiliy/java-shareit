package ru.practicum.shareit.item.storage;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.request.model.ItemRequest;

import java.util.List;

@Repository
public interface ItemRepository extends JpaRepository<Item, Long> {
    List<Item> findByOwnerId(Long id);

    List<Item> findByOwnerIdOrderById(Long id);

    @Query("select i from Item i " +
            "where upper(i.name) like upper(concat('%', ?1, '%')) " +
            "   or upper(i.description) like upper(concat('%', ?1, '%')) and i.available = true")
    List<Item> getAvailableItems(String text);

    List<Item> findAllByRequestIn(List<ItemRequest> itemRequest);

    List<Item> findByRequest(ItemRequest itemRequest);
}
