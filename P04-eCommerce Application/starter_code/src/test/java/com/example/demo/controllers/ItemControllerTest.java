package com.example.demo.controllers;

import com.example.demo.TestUtils;
import com.example.demo.model.persistence.Item;
import com.example.demo.model.persistence.repositories.ItemRepository;
import org.junit.Before;
import org.junit.Test;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class ItemControllerTest {

    private final ItemRepository itemRepository = mock(ItemRepository.class);
    private ItemController itemController;

    @Before
    public void setUp() {
        itemController = new ItemController();
        TestUtils.injectObjects(itemController, "itemRepository", itemRepository);
    }

    @Test
    public void getAllItem() {
        Item item1 = TestUtils.createItem(1L, "sand toy", "a supreme toy", BigDecimal.valueOf(33.99));
        Item item2 = TestUtils.createItem(2L, "glove repair kit", "buy one - get one free", BigDecimal.valueOf(5.99));

        when(itemRepository.findAll()).thenReturn(Arrays.asList(item1, item2));

        ResponseEntity<List<Item>> response = itemController.getItems();

        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());
        List<Item> items = response.getBody();

        assertNotNull(items);
        assertArrayEquals(Arrays.asList(item1, item2).toArray(), items.toArray());
    }

    @Test
    public void getItemByValidId() {
        Item item = TestUtils.createItem(1L, "sand toy", "a supreme toy", BigDecimal.valueOf(33.99));

        when(itemRepository.findById(item.getId())).thenReturn(Optional.of(item));

        ResponseEntity<Item> response = itemController.getItemById(item.getId());
        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());
        Item responseItem = response.getBody();
        assertNotNull(responseItem);
        assertEquals(item.getId(), responseItem.getId());
        assertEquals(item.getName(), responseItem.getName());
        assertEquals(item.getDescription(), responseItem.getDescription());
        assertEquals(item.getPrice(), responseItem.getPrice());
    }

    @Test
    public void getItemByInvalidId() {
        when(itemRepository.findById(1L)).thenReturn(Optional.empty());

        final ResponseEntity<Item> response = itemController.getItemById(1L);
        assertNotNull(response);
        assertNotEquals(200, response.getStatusCodeValue());
    }

    @Test
    public void getItemsByValidName() {
        Item item = TestUtils.createItem(1L, "sand toy", "a supreme toy", BigDecimal.valueOf(33.99));

        when(itemRepository.findByName(item.getName())).thenReturn(Arrays.asList(item));

        ResponseEntity<List<Item>> response = itemController.getItemsByName(item.getName());
        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());
        List<Item> responseItems = response.getBody();
        assertNotNull(responseItems);
        assertEquals(item.getId(), responseItems.get(0).getId());
        assertEquals(item.getName(), responseItems.get(0).getName());
        assertEquals(item.getDescription(), responseItems.get(0).getDescription());
        assertEquals(item.getPrice(), responseItems.get(0).getPrice());
    }

    @Test
    public void getItemsByInvalidName() {
        Item item = TestUtils.createItem(1L, "sand toy", "a supreme toy", BigDecimal.valueOf(33.99));

        when(itemRepository.findByName(item.getName())).thenReturn(null);

        ResponseEntity<List<Item>> response = itemController.getItemsByName(item.getName());
        assertNotNull(response);
        assertNotEquals(200, response.getStatusCodeValue());
    }


}