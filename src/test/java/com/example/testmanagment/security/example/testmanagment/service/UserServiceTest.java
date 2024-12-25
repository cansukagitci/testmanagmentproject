package com.example.testmanagment.security.example.testmanagment.service;

import com.example.testmanagment.model.User;
import com.example.testmanagment.model.UserResponse;
import com.example.testmanagment.repository.UserRepository;
import com.example.testmanagment.service.UserService;
import com.example.testmanagment.util.JwtUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;


public class UserServiceTest {

    @InjectMocks
    private UserService userService;

    @Mock
    private UserRepository userRepository;

    @Mock
    private JwtUtil jwtUtil;

    private User testUser;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        testUser = new User();
        testUser.setId(1L);
        testUser.setUsername("eeee");
        testUser.setEmail("test@example.com");
        testUser.setPassword("password");
    }
    //kullanıcı zaten var test
    @Test
    public void testRegisterUser_UserAlreadyExists() {
        // Arrange
        when(userRepository.findByUsername(testUser.getUsername())).thenReturn(testUser);

        // Act
        UserResponse response = userService.registerUser(testUser);

        // Assert
        assertNotNull(response);
        assertEquals(1, response.getResult().size());
        assertFalse(response.getResult().get(0).isStatus());
        assertEquals("SERVICE_RESPONSE_FAILURE: User already exists", response.getResult().get(0).getMessage());
    }
    //Kullanıcı giriş test
    @Test
    public void testRegisterUser_Success() {
        // Arrange
        when(userRepository.findByUsername(testUser.getUsername())).thenReturn(null);
        when(userRepository.save(any(User.class))).thenReturn(testUser);

        // Act
        UserResponse response = userService.registerUser(testUser);

        // Assert
        assertNotNull(response);
        assertEquals(1, response.getResult().size());
        assertTrue(response.getResult().get(0).isStatus());
        assertEquals("SERVICE_RESPONSE_SUCCESS", response.getResult().get(0).getMessage());
    }

    //Kullanıcı silme test
    @Test
    public void testDeleteUser_UserNotFound() {
        // Arrange
        doThrow(new RuntimeException("User not found")).when(userRepository).deleteById(testUser.getId());

        // Act
        UserResponse response = userService.deleteUser(testUser.getId());

        // Assert
        assertNotNull(response);
        assertEquals(1, response.getResult().size());
        assertFalse(response.getResult().get(0).isStatus());
        assertTrue(response.getResult().get(0).getMessage().contains("SERVICE_RESPONSE_FAILURE"));
    }

    //Kullanıcı güncelleme test
    @Test
    public void testUpdateUser_Success() {
        // Arrange
        User updatedUser = new User();
        updatedUser.setUsername("updatedUser");
        updatedUser.setEmail("updated@example.com");
        updatedUser.setPassword("newPassword");

        when(userRepository.findById(testUser.getId())).thenReturn(Optional.of(testUser));
        when(userRepository.save(any(User.class))).thenReturn(testUser);

        // Act
        UserResponse response = userService.updateUser(testUser.getId(), updatedUser);

        // Assert
        assertNotNull(response);
        assertEquals(1, response.getResult().size());
        assertTrue(response.getResult().get(0).isStatus());
        assertEquals("SERVICE_RESPONSE_SUCCESS", response.getResult().get(0).getMessage());
        assertEquals("updatedUser", testUser.getUsername());
        assertEquals("updated@example.com", testUser.getEmail());


        verify(userRepository).save(testUser);
    }

    //Kullanıcı bulunamadı test

    @Test
    public void testUpdateUser_UserNotFound() {
        // Arrange
        User updatedUser = new User();
        updatedUser.setUsername("updatedUser");
        updatedUser.setEmail("updated@example.com");
        updatedUser.setPassword("newPassword");

        when(userRepository.findById(testUser.getId())).thenReturn(Optional.empty());

        // Act
        UserResponse response = userService.updateUser(testUser.getId(), updatedUser);

        // Assert
        assertNotNull(response);
        assertEquals(1, response.getResult().size());
        assertFalse(response.getResult().get(0).isStatus());
        assertEquals("SERVICE_RESPONSE_FAILURE: User not found", response.getResult().get(0).getMessage());

        // Save methodunun çağrılmadığını doğrula
        verify(userRepository, never()).save(any(User.class));
    }



}
