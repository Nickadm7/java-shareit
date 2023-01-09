package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.practicum.shareit.item.dto.CommentOutDto;
import ru.practicum.shareit.item.model.Comment;

@Component
@RequiredArgsConstructor
public class CommentMapper {
    private final CommentRepository commentRepository;

    public static CommentOutDto toCommentOutDto(Comment comment, Long userId) {
        return new CommentOutDto(
                userId,
                comment.getText(),
                comment.getAuthor().getName(),
                comment.getCreated());
    }

    public static CommentOutDto toCommentOutForFindItemsDto(Comment comment) {
        return new CommentOutDto(
                comment.getId(),
                comment.getText(),
                comment.getAuthor().getName(),
                comment.getCreated());
    }
}