package hu.unideb.cartshare.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import hu.unideb.cartshare.component.JwtFilter;
import hu.unideb.cartshare.component.JwtUtils;
import hu.unideb.cartshare.model.dto.request.ListRequestDto;
import hu.unideb.cartshare.model.dto.response.ListItemResponseDto;
import hu.unideb.cartshare.model.dto.response.ListResponseDto;
import hu.unideb.cartshare.service.ListItemService;
import hu.unideb.cartshare.service.ListService;
import hu.unideb.cartshare.service.user.UserDetailsServiceImpl;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.io.IOException;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ListController.class)
class ListControllerTest {

    private static final UUID TEST_LIST_ID = UUID.fromString("ea5768b5-88c4-40a0-a639-0a6819f849e9");
    private static final UUID ANOTHER_LIST_ID = UUID.fromString("a8c69d27-d189-4eca-889a-2157933d7fd6");

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ListService listService;

    @MockBean
    private ListItemService listItemService;

    @MockBean
    private JwtUtils jwtUtils;

    @MockBean
    private JwtFilter jwtFilter;

    @MockBean
    private UserDetailsServiceImpl userDetailsService;

    @Autowired
    private ObjectMapper objectMapper;

    private ListResponseDto mockListResponse;

    @BeforeEach
    void setUp() throws ServletException, IOException {
        mockListResponse = new ListResponseDto();
        mockListResponse.setId(TEST_LIST_ID);
        mockListResponse.setName("Test list");
        mockListResponse.setItems(Set.of());

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
    void findAllOwnedLists_ShouldReturnLists_WhenCalled() throws Exception {
        List<ListResponseDto> ownedLists = List.of(mockListResponse);
        when(listService.findAllOwnedLists()).thenReturn(ownedLists);

        mockMvc.perform(get("/lists/owned")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(TEST_LIST_ID.toString()))
                .andExpect(jsonPath("$[0].name").value("Test list"));
    }


    @Test
    @WithMockUser
    void findAllAllJoinedLists_ShouldReturnLists_WhenCalled() throws Exception {
        List<ListResponseDto> joinedLists = List.of(mockListResponse);
        when(listService.findAllAllJoinedLists()).thenReturn(joinedLists);

        mockMvc.perform(get("/lists/joined")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value("Test list"));
    }


    @Test
    @WithMockUser
    void create_ShouldReturnNewList_WhenDataIsValid() throws Exception {
        ListRequestDto requestDto = new ListRequestDto();
        requestDto.setName("New list's name");

        ListResponseDto createdList = new ListResponseDto();
        createdList.setId(ANOTHER_LIST_ID);
        createdList.setName("New list's name");
        when(listService.create(any(ListRequestDto.class))).thenReturn(createdList);

        mockMvc.perform(post("/lists/new")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("New list's name"))
                .andExpect(jsonPath("$.id").value(ANOTHER_LIST_ID.toString()));
    }

    @Test
    @WithMockUser
    void create_ShouldReturnBadRequest_WhenNameIsMissing() throws Exception {
        ListRequestDto requestDto = new ListRequestDto();

        mockMvc.perform(post("/lists/new")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDto)))
                .andExpect(status().isBadRequest());
    }


    @Test
    @WithMockUser
    void findItemsByListId_ShouldReturnItems_WhenListExists() throws Exception {
        ListItemResponseDto itemDto = new ListItemResponseDto();
        itemDto.setName("Bread");
        Set<ListItemResponseDto> items = Set.of(itemDto);

        when(listItemService.findItemsByListId(TEST_LIST_ID)).thenReturn(items);

        mockMvc.perform(get("/lists/{id}/items", TEST_LIST_ID)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value("Bread"));
    }


    @Test
    @WithMockUser
    void update_ShouldReturnNoContent_WhenSuccessful() throws Exception {
        ListRequestDto updateDto = new ListRequestDto();
        updateDto.setName("New name");

        doNothing().when(listService).update(eq(TEST_LIST_ID), any(ListRequestDto.class));

        mockMvc.perform(patch("/lists/{id}", TEST_LIST_ID)
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateDto)))
                .andExpect(status().isNoContent());

        verify(listService, times(1)).update(eq(TEST_LIST_ID), any(ListRequestDto.class));
    }

    @Test
    @WithMockUser
    void joinById_ShouldReturnList_WhenSuccessful() throws Exception {
        when(listService.joinById(TEST_LIST_ID)).thenReturn(mockListResponse);

        mockMvc.perform(post("/lists/join/{id}", TEST_LIST_ID)
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(TEST_LIST_ID.toString()));
    }


    @Test
    @WithMockUser
    void leaveById_ShouldReturnNoContent_WhenSuccessful() throws Exception {
        doNothing().when(listService).leaveById(TEST_LIST_ID);

        mockMvc.perform(delete("/lists/leave/{id}", TEST_LIST_ID)
                        .with(csrf()))
                .andExpect(status().isNoContent()); // 204

        verify(listService, times(1)).leaveById(TEST_LIST_ID);
    }


    @Test
    @WithMockUser
    void delete_ShouldReturnNoContent_WhenSuccessful() throws Exception {
        doNothing().when(listService).delete(TEST_LIST_ID);

        mockMvc.perform(delete("/lists/{id}", TEST_LIST_ID)
                        .with(csrf()))
                .andExpect(status().isNoContent());

        verify(listService, times(1)).delete(TEST_LIST_ID);
    }
}