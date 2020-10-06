package com.example.demo.controllers;

import com.example.demo.TestUtils;
import com.example.demo.model.persistence.Cart;
import com.example.demo.model.persistence.User;
import com.example.demo.model.persistence.UserOrder;
import com.example.demo.model.persistence.repositories.OrderRepository;
import com.example.demo.model.persistence.repositories.UserRepository;
import org.junit.Before;
import org.junit.Test;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class OrderControllerTest {

    private final UserRepository userRepository = mock(UserRepository.class);
    private final OrderRepository orderRepository = mock(OrderRepository.class);
    private final BCryptPasswordEncoder encoder = mock(BCryptPasswordEncoder.class);
    private final String username = "username";
    private final String password = "password";
    private OrderController orderController;

    @Before
    public void setUp() {
        orderController = new OrderController();
        TestUtils.injectObjects(orderController, "userRepository", userRepository);
        TestUtils.injectObjects(orderController, "orderRepository", orderRepository);
    }

    @Test
    public void submitOrderWithValidUsername() {
        User user = TestUtils.createUser();
        Cart cart = TestUtils.createCart();
        cart.setUser(user);
        user.setCart(cart);

        when(userRepository.findByUsername(user.getUsername())).thenReturn(user);

        ResponseEntity<UserOrder> response = orderController.submit(user.getUsername());

        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());

        UserOrder userOrder = response.getBody();

        assertNotNull(userOrder);
        assertEquals(Arrays.asList(cart.getItems().get(0), cart.getItems().get(1), cart.getItems().get(2)), userOrder.getItems());
        assertEquals(user, userOrder.getUser());
    }

    @Test
    public void submitOrderWithInvalidUsername() {
        User user = TestUtils.createUser();
        Cart cart = TestUtils.createCart();
        cart.setUser(user);
        user.setCart(cart);

        when(userRepository.findByUsername(user.getUsername())).thenReturn(null);

        ResponseEntity<UserOrder> response = orderController.submit(user.getUsername());
        assertNotNull(response);
        assertNotEquals(200, response.getStatusCodeValue());
    }

    @Test
    public void getOrderHistoryWithValidUsername() {
        User user = TestUtils.createUser();
        List<UserOrder> userOrders = TestUtils.createUserOrder();

        when(userRepository.findByUsername(user.getUsername())).thenReturn(user);
        when(orderRepository.findByUser(user)).thenReturn(Arrays.asList(userOrders.get(0), userOrders.get(1)));

        ResponseEntity<List<UserOrder>> response = orderController.getOrdersForUser(user.getUsername());
        assertNotNull(response);
        List<UserOrder> responseBody = response.getBody();
        assertEquals(Arrays.asList(userOrders.get(0), userOrders.get(1)), responseBody);
        assertEquals(200, response.getStatusCodeValue());
    }

    @Test
    public void getOrderHistoryWithInvalidUsername() {
        User user = TestUtils.createUser();
        List<UserOrder> userOrders = TestUtils.createUserOrder();

        when(userRepository.findByUsername(user.getUsername())).thenReturn(null);
        when(orderRepository.findByUser(user)).thenReturn(null);

        ResponseEntity<List<UserOrder>> response = orderController.getOrdersForUser(user.getUsername());
        assertNotNull(response);
        assertNotEquals(200, response.getStatusCodeValue());
    }
}
