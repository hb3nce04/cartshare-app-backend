package hu.unideb.cartshare.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import hu.unideb.cartshare.component.JwtFilter;
import hu.unideb.cartshare.component.JwtUtils;
import hu.unideb.cartshare.model.dto.request.CreateListItemRequestDto;
import hu.unideb.cartshare.model.dto.request.UpdateListItemRequestDto;
import hu.unideb.cartshare.model.dto.response.ListItemResponseDto;
import hu.unideb.cartshare.service.ListItemService;
import hu.unideb.cartshare.service.user.UserDetailsServiceImpl;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.io.IOException;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ListItemController.class)
class ListItemControllerTest {

    private static final UUID TEST_ITEM_ID = UUID.fromString("2d522d18-8bc3-41b0-9de1-4fe56a29774a");
    private static final UUID TEST_LIST_ID = UUID.fromString("ea5768b5-88c4-40a0-a639-0a6819f849e9");

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private ListItemService listItemService;

    @MockitoBean
    private JwtUtils jwtUtils;

    @MockitoBean
    private JwtFilter jwtFilter;

    @MockitoBean
    private UserDetailsServiceImpl userDetailsService;

    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() throws ServletException, IOException {

        doAnswer(invocation -> {
            HttpServletRequest request = invocation.getArgument(0);
            HttpServletResponse response = invocation.getArgument(1);
            FilterChain chain = invocation.getArgument(2);
            chain.doFilter(request, response);
            return null;
        }).when(jwtFilter).doFilter(any(), any(), any());
    }


    @Test
    @WithMockUser
    void create_ShouldReturnNewItem_WhenDataIsValid() throws Exception {
        CreateListItemRequestDto requestDto = new CreateListItemRequestDto();
        requestDto.setName("Milk");
        requestDto.setQuantity(2.0);
        requestDto.setUnit("liter");
        requestDto.setListId(TEST_LIST_ID.toString());

        ListItemResponseDto responseDto = new ListItemResponseDto();
        responseDto.setId(TEST_ITEM_ID);
        responseDto.setName("Milk");

        when(listItemService.create(any(CreateListItemRequestDto.class))).thenReturn(responseDto);

        mockMvc.perform(post("/list-items/add")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Milk"));
    }

    @Test
    @WithMockUser
    void create_ShouldReturnBadRequest_WhenNameIsMissing() throws Exception {
        CreateListItemRequestDto requestDto = new CreateListItemRequestDto();

        requestDto.setQuantity(2.0);
        requestDto.setUnit("liter");
        requestDto.setListId(TEST_LIST_ID.toString());

        mockMvc.perform(post("/list-items/add")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDto)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @WithMockUser
    void update_ShouldReturnNoContent_WhenSuccessful() throws Exception {
        UpdateListItemRequestDto requestDto = new UpdateListItemRequestDto();
        requestDto.setName("Bread");
        requestDto.setQuantity(1.0);
        requestDto.setUnit("kg");
        requestDto.setIsChecked(true);

        doNothing().when(listItemService).update(any(UUID.class), any(UpdateListItemRequestDto.class));

        mockMvc.perform(put("/list-items/{id}", TEST_ITEM_ID)
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDto)))
                .andExpect(status().isNoContent()); // 204

        verify(listItemService, times(1)).update(eq(TEST_ITEM_ID), any(UpdateListItemRequestDto.class));
    }

    @Test
    @WithMockUser
    void update_ShouldReturnBadRequest_WhenQuantityIsZero() throws Exception {
        UpdateListItemRequestDto requestDto = new UpdateListItemRequestDto();
        requestDto.setName("Bread");
        requestDto.setQuantity(0.0);
        requestDto.setUnit("kg");
        requestDto.setIsChecked(true);

        mockMvc.perform(put("/list-items/{id}", TEST_ITEM_ID)
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDto)))
                .andExpect(status().isBadRequest());
    }


    @Test
    @WithMockUser
    void delete_ShouldReturnNoContent_WhenSuccessful() throws Exception {
        doNothing().when(listItemService).delete(TEST_ITEM_ID);

        mockMvc.perform(delete("/list-items/{id}", TEST_ITEM_ID)
                        .with(csrf()))
                .andExpect(status().isNoContent());

        verify(listItemService, times(1)).delete(TEST_ITEM_ID);
    }
}
