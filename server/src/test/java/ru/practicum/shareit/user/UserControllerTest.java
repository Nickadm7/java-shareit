//package ru.practicum.shareit.user;
//
//import com.fasterxml.jackson.databind.ObjectMapper;
//import org.junit.jupiter.api.DisplayName;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
//import org.springframework.boot.test.mock.mockito.MockBean;
//import org.springframework.http.MediaType;
//import org.springframework.test.web.servlet.MockMvc;
//import ru.practicum.shareit.user.dto.UserDto;
//
//
//import java.nio.charset.StandardCharsets;
//import java.util.List;
//
//import static org.hamcrest.Matchers.is;
//import static org.mockito.ArgumentMatchers.any;
//import static org.mockito.Mockito.*;
//import static org.mockito.Mockito.times;
//import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
//
//@WebMvcTest(controllers = UserController.class)
//public class UserControllerTest {
//    @Autowired
//    private MockMvc mockMvc;
//    @MockBean
//    private UserService userService;
//    private final ObjectMapper mapper = new ObjectMapper();
//
//    private final UserDto userDto = new UserDto(1L, "userTestName", "mailtest@mail.ru");
//    private final UserDto userDtoWrongEmail = new UserDto(1L, "userTestName", "mailtestmail.ru");
//
//    @Test
//    @DisplayName("Тест добавления пользователя")
//    void addUserTest() throws Exception {
//        when(userService.addUser(any(UserDto.class)))
//                .thenReturn(userDto);
//
//        mockMvc.perform(post("/users")
//                        .content(mapper.writeValueAsString(userDto))
//                        .characterEncoding(StandardCharsets.UTF_8)
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .accept(MediaType.APPLICATION_JSON))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.id", is(userDto.getId()), Long.class))
//                .andExpect(jsonPath("$.name", is(userDto.getName()), String.class))
//                .andExpect(jsonPath("$.email", is(userDto.getEmail()), String.class));
//
//        verify(userService, times(1))
//                .addUser(any(UserDto.class));
//    }
//
//    @Test
//    @DisplayName("Тест получения пользователя по id")
//    void getUserByIdTest() throws Exception {
//        when(userService.getUserById(any(Long.class)))
//                .thenReturn(userDto);
//
//        mockMvc.perform(get("/users/1"))
//                .andExpect(status().isOk())
//                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
//                .andExpect(jsonPath("$.id", is(userDto.getId()), Long.class))
//                .andExpect(jsonPath("$.name", is(userDto.getName())))
//                .andExpect(jsonPath("$.email", is(userDto.getEmail())));
//    }
//
//    @Test
//    @DisplayName("Тест удалить пользователя по id")
//    void deleteByIdTest() throws Exception {
//        mockMvc.perform(delete("/users/1")
//                        .content(mapper.writeValueAsString(userDto))
//                        .characterEncoding(StandardCharsets.UTF_8)
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .accept(MediaType.APPLICATION_JSON))
//                .andExpect(status().isOk());
//        verify(userService, times(1)).deleteById(anyLong());
//
//    }
//
//    @Test
//    @DisplayName("Тест получение всех пользователей")
//    void getAllUsersTest() throws Exception {
//        when(userService.getAllUsers())
//                .thenReturn(List.of(userDto));
//
//        mockMvc.perform(get("/users")
//                        .content(mapper.writeValueAsString(List.of(userDto)))
//                        .characterEncoding(StandardCharsets.UTF_8)
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .accept(MediaType.APPLICATION_JSON))
//                .andExpect(status().isOk());
//    }
//
//    @Test
//    @DisplayName("Тест обновление пользователя по id")
//    void updateUserTest() throws Exception {
//        when(userService.updateUser(any(), any(Long.class)))
//                .thenReturn(userDto);
//
//        mockMvc.perform(patch("/users/1")
//                        .content(mapper.writeValueAsString(userDto))
//                        .characterEncoding(StandardCharsets.UTF_8)
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .accept(MediaType.APPLICATION_JSON))
//                .andExpect(status().isOk())
//                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
//                .andExpect(jsonPath("$.id", is(userDto.getId()), Long.class))
//                .andExpect(jsonPath("$.name", is(userDto.getName())))
//                .andExpect(jsonPath("$.email", is(userDto.getEmail())));
//    }
//
//    @Test
//    @DisplayName("Тест добавления пользователя неправильный email")
//    void addUserWrongEmailTest() throws Exception {
//
//        mockMvc.perform(post("/users")
//                        .content(mapper.writeValueAsString(userDtoWrongEmail))
//                        .characterEncoding(StandardCharsets.UTF_8)
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .accept(MediaType.APPLICATION_JSON))
//                .andExpect(status().is4xxClientError());
//    }
//}