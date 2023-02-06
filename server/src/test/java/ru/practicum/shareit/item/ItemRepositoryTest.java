package ru.practicum.shareit.item;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

import java.util.List;

@DataJpaTest()
public class ItemRepositoryTest {
    @Autowired
    TestEntityManager em;

    @Autowired
    ItemRepository itemRepository;

    @Test
    @DisplayName("Тест ItemRepository")
    void findItemsByTextTest() {
        User user = createTestUser();
        em.persist(user);
        Item item = createTestItem(user);
        em.persist(item);

        List<Item> itemsActual = itemRepository.findItemsByText("testDescription");

        Assertions.assertEquals(1, itemsActual.size());
        Assertions.assertEquals(item, itemsActual.get(0));
    }

    private User createTestUser() {
        User user = new User();
        user.setName("testname");
        user.setEmail("testemail@mail.ru");
        return user;
    }

    private Item createTestItem(User user) {
        Item item = new Item();
        item.setName("testItemName");
        item.setDescription("testDescription");
        item.setAvailable(Boolean.TRUE);
        item.setOwner(user);
        return item;
    }
}
