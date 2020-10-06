package com.example.demo.controllers;

import com.example.demo.TestUtils;
import com.example.demo.model.persistence.User;
import com.example.demo.model.persistence.repositories.CartRepository;
import com.example.demo.model.persistence.repositories.UserRepository;
import com.example.demo.model.requests.CreateUserRequest;
import org.junit.Before;
import org.junit.Test;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.Optional;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class UserControllerTest {

    private final UserRepository userRepository = mock(UserRepository.class);
    private final CartRepository cartRepository = mock(CartRepository.class);
    private final BCryptPasswordEncoder encoder = mock(BCryptPasswordEncoder.class);
    private UserController userController;

    @Before
    public void setUp() {
        userController = new UserController();
        TestUtils.injectObjects(userController, "userRepository", userRepository);
        TestUtils.injectObjects(userController, "cartRepository", cartRepository);
        TestUtils.injectObjects(userController, "bCryptPasswordEncoder", encoder);
    }

    @Test
    public void createUser() throws Exception {
        when(encoder.encode("password")).thenReturn("hashedPassword");

        CreateUserRequest r = new CreateUserRequest();
        r.setUsername("username");
        r.setPassword("password");
        r.setConfirmPassword("password");

        ResponseEntity<User> response = userController.createUser(r);
        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());

        User user = response.getBody();
        assertNotNull(user);
        assertEquals(0, user.getId());
        assertEquals("username", user.getUsername());
        assertEquals("hashedPassword", user.getPassword());
    }

    @Test
    public void createUserWithShortPassword() throws Exception {
        CreateUserRequest r = new CreateUserRequest();
        r.setUsername("username");
        r.setPassword("pass");
        r.setConfirmPassword("pass");

        ResponseEntity<User> response = userController.createUser(r);
        assertNotNull(response);
        assertNotEquals(200, response.getStatusCodeValue());
    }

    @Test
    public void createUserWithWrongPasswordConfirm() throws Exception {
        CreateUserRequest r = new CreateUserRequest();
        r.setUsername("username");
        r.setPassword("password");
        r.setConfirmPassword("password22");

        ResponseEntity<User> response = userController.createUser(r);
        assertNotNull(response);
        assertNotEquals(200, response.getStatusCodeValue());
    }

    @Test
    public void findUserByUsernameSuccessful() {

        User user = new User();
        user.setId(0);
        user.setUsername("username");
        user.setPassword("password");

        when(userRepository.findByUsername(user.getUsername())).thenReturn(user);
        ResponseEntity<User> responseEntity = userController.findByUserName("username");

        assertNotNull(responseEntity);
        assertEquals(200, responseEntity.getStatusCodeValue());

        User userReturn;
        userReturn = responseEntity.getBody();
        assertEquals(0, userReturn.getId());
        assertEquals("username", userReturn.getUsername());
        assertEquals("password", userReturn.getPassword());
    }

    @Test
    public void findUserByIdSuccessful() {

        User user = new User();
        user.setId(0);
        user.setUsername("username");
        user.setPassword("password");

        when(userRepository.findById(user.getId())).thenReturn(Optional.of(user));
        ResponseEntity<User> responseEntity = userController.findById(0L);
        assertNotNull(responseEntity);
        assertEquals(200, responseEntity.getStatusCodeValue());

        User userReturn;
        userReturn = responseEntity.getBody();
        assertEquals(0, userReturn.getId());
        assertEquals("username", userReturn.getUsername());
        assertEquals("password", userReturn.getPassword());

    }

    @Test
    public void findUserByUsernameNotSuccessful() {

        User user = new User();
        user.setId(0);
        user.setUsername("username");
        user.setPassword("password");

        when(userRepository.findByUsername(user.getUsername())).thenReturn(user);
        ResponseEntity<User> responseEntity = userController.findByUserName("username2");

        assertNotNull(responseEntity);
        assertNotEquals(200, responseEntity.getStatusCodeValue());
    }

    @Test
    public void findUserByIdNotSuccessful() {

        User user = new User();
        user.setId(0);
        user.setUsername("username");
        user.setPassword("password");

        when(userRepository.findById(user.getId())).thenReturn(Optional.of(user));
        ResponseEntity<User> responseEntity = userController.findById(3L);
        assertNotNull(responseEntity);
        assertNotEquals(200, responseEntity.getStatusCodeValue());
    }


}
