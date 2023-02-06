package ru.practicum.shareit.item;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.practicum.shareit.item.model.Item;

import java.util.List;

public interface ItemRepository extends JpaRepository<Item, Long> {
    List<Item> findItemsByOwnerId(Long ownerId);

    @Query(" select i from Item as i " +
            "where lower(i.name) like (concat('%', lower(:search), '%')) " +
            " or lower(i.description) like (concat('%', lower(:search), '%')) " +
            " and i.available = true")
    List<Item> findItemsByText(@Param("search") String text);

    List<Item> findItemsByItemRequestId(Long requestId);
}