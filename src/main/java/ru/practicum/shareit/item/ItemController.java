package ru.practicum.shareit.item;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.item.dto.ItemDto;

import javax.validation.Valid;
import java.util.Collection;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/items")
public class ItemController {
    private final ItemService itemService;

    @Autowired
    public ItemController(ItemService itemService) {
        this.itemService = itemService;
    }

    @PostMapping
    public ItemDto addItems(@Valid @RequestBody ItemDto itemDto) {
        log.info("POST-запрос к эндпоинту /items");
        return itemService.addItem(itemDto);
    }

    @GetMapping
    public Collection<ItemDto> getAllItems() {
        log.info("GET-запрос к эндпоинту /items список всех пользователей");
        return itemService.getAllItems();
    }

    @GetMapping("/{itemId}")
    public ItemDto getItemById(@PathVariable Long itemId) {
        log.info("GET-запрос к эндпоинту /items найти пользователя по id: {}", itemId);
        return itemService.getItemById(itemId);
    }

    @GetMapping("/search")
    public List<ItemDto> search(@RequestParam (name = "text") String text) {
        log.info("GET-запрос к эндпоинту /items/search найти: {}", text);
        return null;
    }

    @PatchMapping("/{itemId}")
    public ItemDto updateItem(@Valid @RequestBody ItemDto itemDto, @PathVariable Long itemId) {
        log.info("PATCH-запрос к эндпоинту /items обновить пользователя по id: {}", itemId);
        return itemService.updateItem(itemDto, itemId);
    }

    @DeleteMapping("/{itemId}")
    public void deleteById(@PathVariable Long itemId) {
        log.info("DELETE-запрос к эндпоинту /items удалить пользователя с id: {}", itemId);
        itemService.deleteById(itemId);
    }
}
