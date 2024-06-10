package com.example.ItauCuboNet.service;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.context.ActiveProfiles;

import com.example.ItauCuboNet.entity.User;
import com.example.ItauCuboNet.repository.UserRepository;

@ActiveProfiles("test")
@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserService userService;

    private User user;

    @BeforeEach
    public void setup() {
        user = User.builder()
                   .id(1L)
                   .name("John Doe")
                   .participation(10.0f)
                   .build();
    }

    @Test
    public void testSaveUserSuccess() throws Exception {
        when(userRepository.findAll()).thenReturn(List.of());
        when(userRepository.save(any(User.class))).thenReturn(user);

        User savedUser = userService.save(user);

        assertNotNull(savedUser);
        assertEquals(user.getName(), savedUser.getName());
        assertEquals(user.getParticipation(), savedUser.getParticipation());
    }

    @Test
    public void testSaveUserExceedsTotalParticipation() {
        when(userRepository.findAll()).thenReturn(List.of(user, User.builder().id(2L).name("Jane Doe").participation(95.0f).build()));

        Exception exception = assertThrows(Exception.class, () -> {
            userService.save(user);
        });

        String expectedMessage = "Total participation cannot exceed 100. Current total: ";
        String actualMessage = exception.getMessage();

        assertTrue(actualMessage.contains(expectedMessage));
    }

    @Test
    public void testUpdateUserSuccess() throws Exception {
        when(userRepository.findByName(anyString())).thenReturn(Optional.of(user));
        when(userRepository.findAll()).thenReturn(List.of(user));
        when(userRepository.save(any(User.class))).thenReturn(user);

        User updatedUser = new User(1L, "John Doe", 20.0f);
        User result = userService.updateUser(updatedUser);

        assertNotNull(result);
        assertEquals(updatedUser.getParticipation(), result.getParticipation());
    }

    @Test
    public void testUpdateUserExceedsTotalParticipation() {
        when(userRepository.findByName(anyString())).thenReturn(Optional.of(user));
        when(userRepository.findAll()).thenReturn(List.of(user, User.builder().id(2L).name("Jane Doe").participation(80.0f).build()));

        User updatedUser = new User(1L, "John Doe", 30.0f);

        Exception exception = assertThrows(Exception.class, () -> {
            userService.updateUser(updatedUser);
        });

        String expectedMessage = "Total participation cannot exceed 100. Current total: ";
        String actualMessage = exception.getMessage();

        assertTrue(actualMessage.contains(expectedMessage));
    }

    @Test
    public void testDeleteUserSuccess() throws Exception {
        when(userRepository.findById(anyLong())).thenReturn(Optional.of(user));
        doNothing().when(userRepository).delete(any(User.class));

        assertDoesNotThrow(() -> userService.deleteUser(user.getId()));

        verify(userRepository, times(1)).delete(user);
    }

    @Test
    public void testDeleteUserNotFound() {
        when(userRepository.findById(anyLong())).thenReturn(Optional.empty());

        Exception exception = assertThrows(Exception.class, () -> {
            userService.deleteUser(1L);
        });

        assertEquals("User not found", exception.getMessage());
    }
}
