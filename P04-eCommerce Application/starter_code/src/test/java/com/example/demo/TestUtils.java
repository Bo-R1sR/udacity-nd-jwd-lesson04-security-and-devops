package com.example.demo;

import com.example.demo.model.requests.CreateUserRequest;
import com.example.demo.model.requests.ModifyCartRequest;
import org.mockito.internal.util.reflection.FieldSetter;

import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import com.example.demo.model.persistence.*;

public class TestUtils {

    public static void injectObjects(Object target, String fieldName, Object toInject) {

        boolean wasPrivate = false;

        try {
            Field f = target.getClass().getDeclaredField(fieldName);

            if (!f.isAccessible()) {
                f.setAccessible(true);
                wasPrivate = true;
            }
            f.set(target, toInject);

            if (wasPrivate) {
                f.setAccessible(false);
            }

        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }

    }

    public static User createUser() {
        User user = new User();
        user.setUsername("username");
        user.setPassword("password");
        return user;
    }

    public static Item createItem(Long id, String name, String description, BigDecimal price) {
        Item item = new Item();
        item.setId(id);
        item.setName(name);
        item.setDescription(description);
        item.setPrice(price);
        return item;
    }

    public static Cart createCart() {
        Cart cart = new Cart();
        Item item1 = createItem(1L, "sand toy", "a supreme toy", BigDecimal.valueOf(33.99));
        cart.addItem(item1);
        Item item2 = createItem(2L, "glove repair kit", "buy one - get one free", BigDecimal.valueOf(5.99));
        cart.addItem(item2);
        Item item3 = createItem(3L, "finger print sensor", "includes lifetime warrenty", BigDecimal.valueOf(99.99));
        cart.addItem(item3);
        return cart;
    }

    public static List<UserOrder> createUserOrder() {
        Item item1 = createItem(1L, "sand toy", "a supreme toy", BigDecimal.valueOf(33.99));
        Item item2 = createItem(2L, "glove repair kit", "buy one - get one free", BigDecimal.valueOf(5.99));

        User user = createUser();

        List<UserOrder> userOrders = new ArrayList<>();

        UserOrder userOrder1 = new UserOrder();
        userOrder1.setUser(user);
        userOrder1.setItems(Arrays.asList(item1));
        userOrder1.setTotal(item1.getPrice());

        userOrders.add(userOrder1);

        UserOrder userOrder2 = new UserOrder();
        userOrder2.setUser(user);
        userOrder2.setItems(Arrays.asList(item2));
        userOrder2.setTotal(item2.getPrice());

        userOrders.add(userOrder2);

        return userOrders;
    }

    public static ModifyCartRequest createModifyCartRequest() {
        Item item1 = TestUtils.createItem(1L, "sand toy", "a supreme toy", BigDecimal.valueOf(33.99));
        User user = createUser();
        Cart cart = createCart();

        ModifyCartRequest request = new ModifyCartRequest();
        request.setItemId(item1.getId());
        request.setQuantity(10);
        request.setUsername(user.getUsername());

        return request;

    }


}

