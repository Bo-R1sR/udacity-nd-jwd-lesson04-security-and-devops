package com.example.demo.controllers;

import com.example.demo.TestUtils;
import com.example.demo.model.persistence.Cart;
import com.example.demo.model.persistence.Item;
import com.example.demo.model.persistence.User;
import com.example.demo.model.persistence.repositories.CartRepository;
import com.example.demo.model.persistence.repositories.ItemRepository;
import com.example.demo.model.persistence.repositories.UserRepository;
import com.example.demo.model.requests.ModifyCartRequest;
import org.junit.Before;
import org.junit.Test;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.Assert.*;;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class CartControllerTest {

    private final UserRepository userRepository = mock(UserRepository.class);
    private final CartRepository cartRepository = mock(CartRepository.class);
    private final ItemRepository itemRepository = mock(ItemRepository.class);
    private CartController cartController;

    @Before
    public void setUp() {
        cartController = new CartController();
        TestUtils.injectObjects(cartController, "userRepository", userRepository);
        TestUtils.injectObjects(cartController, "cartRepository", cartRepository);
        TestUtils.injectObjects(cartController, "itemRepository", itemRepository);
    }


    @Test
    public void addItemsToCartWithValidUser() {
        User user = TestUtils.createUser();
        Cart cart = TestUtils.createCart();
        user.setCart(cart);
        when(userRepository.findByUsername(user.getUsername())).thenReturn(user);

        Item item1 = TestUtils.createItem(1L, "sand toy", "a supreme toy", BigDecimal.valueOf(33.99));

        when(itemRepository.findById(item1.getId())).thenReturn(Optional.of(item1));

        ModifyCartRequest request = TestUtils.createModifyCartRequest();

        ResponseEntity<Cart> response = cartController.addToCart(request);
        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());
        Cart returnedCart = response.getBody();

        assertNotNull(returnedCart);
        assertEquals(returnedCart, cart);
    }

    @Test
    public void addItemsToCartWithInvalidUser() {
        User user = TestUtils.createUser();

        when(userRepository.findByUsername(user.getUsername())).thenReturn(null);

        ModifyCartRequest request = TestUtils.createModifyCartRequest();

        ResponseEntity<Cart> response = cartController.addToCart(request);

        assertNotNull(response);
        assertNotEquals(200, response.getStatusCodeValue());
    }

    @Test
    public void addInvalidItemIdToCart() {
        User user = TestUtils.createUser();
        Item item1 = TestUtils.createItem(1L, "sand toy", "a supreme toy", BigDecimal.valueOf(33.99));

        when(userRepository.findByUsername(user.getUsername())).thenReturn(user);
        when(itemRepository.findById(item1.getId())).thenReturn(Optional.empty());

        ModifyCartRequest request = TestUtils.createModifyCartRequest();

        ResponseEntity<Cart> response = cartController.addToCart(request);

        assertNotNull(response);
        assertNotEquals(200, response.getStatusCodeValue());
    }

    @Test
    public void removeValidItemFromCart() {
        ModifyCartRequest request = TestUtils.createModifyCartRequest();

        User user = TestUtils.createUser();
        Cart cart = TestUtils.createCart();
        user.setCart(cart);
        when(userRepository.findByUsername(user.getUsername())).thenReturn(user);

        Item item1 = TestUtils.createItem(1L, "sand toy", "a supreme toy", BigDecimal.valueOf(33.99));

        when(itemRepository.findById(item1.getId())).thenReturn(Optional.of(item1));



        ResponseEntity<Cart> response = cartController.removeFromCart(request);
        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());
        Cart returnedCart = response.getBody();

        assertNotNull(returnedCart);
        assertEquals(returnedCart, cart);
    }

    @Test
    public void removeItemFromCartWithInvalidUsername() {
        ModifyCartRequest request = TestUtils.createModifyCartRequest();

        when(userRepository.findByUsername(request.getUsername())).thenReturn(null);

        ResponseEntity<Cart> response = cartController.removeFromCart(request);

        assertNotNull(response);
        assertNotEquals(200, response.getStatusCodeValue());
    }

    @Test
    public void removeItemFromCartWithInvalidItem() {
        User user = TestUtils.createUser();
        when(userRepository.findByUsername(user.getUsername())).thenReturn(user);
        ModifyCartRequest request = TestUtils.createModifyCartRequest();

        when(itemRepository.findById(request.getItemId())).thenReturn(Optional.empty());

        ResponseEntity<Cart> response = cartController.removeFromCart(request);

        assertNotNull(response);
        assertNotEquals(200, response.getStatusCodeValue());
    }
}
