package ru.practicum.shareit.item;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import ru.practicum.shareit.item.dto.CommentOutDto;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class CommentMapperTest {

    @Test
    @DisplayName("Тест маппера toCommentOutDto")
    void toCommentOutDtoTest() {
        User author = new User(1L, "testName", "testEmail");
        Comment comment = new Comment(2L, "text", null, author, LocalDateTime.now());

        CommentOutDto actual = CommentMapper.toCommentOutDto(comment, 3L);

        assertEquals(3, actual.getId());
        assertEquals("text", actual.getText());
        assertEquals("testName", actual.getAuthorName());
        assert (actual.getCreated() != null);
    }

    @Test
    @DisplayName("Тест маппера toCommentOutForFindItemsDto")
    void toCommentOutForFindItemsDtoTest() {
        User author = new User(1L, "testName", "testEmail");
        Comment comment = new Comment(2L, "text", null, author, LocalDateTime.now());

        CommentOutDto actual = CommentMapper.toCommentOutForFindItemsDto(comment);

        assertEquals(2, actual.getId());
        assertEquals("text", actual.getText());
        assertEquals("testName", actual.getAuthorName());
        assert (actual.getCreated() != null);
    }
}