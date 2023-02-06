package ru.practicum.shareit.request;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.shareit.user.dto.model.ItemRequest;

import java.util.List;

public interface ItemRequestRepository extends JpaRepository<ItemRequest, Long> {
    List<ItemRequest> findAllByRequestUserId_OrderByCreatedDesc(Long requestorId);

    List<ItemRequest> findByRequestUserIdNot(long requesterId, Pageable pageable);

    List<ItemRequest> findByRequestUserIdNot(long requesterId);
}