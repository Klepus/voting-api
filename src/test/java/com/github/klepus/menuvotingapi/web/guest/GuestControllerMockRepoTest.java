package com.github.klepus.menuvotingapi.web.guest;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.klepus.menuvotingapi.repository.DishRepository;
import com.github.klepus.menuvotingapi.repository.RestaurantRepository;
import com.github.klepus.menuvotingapi.repository.VoteRepository;
import com.github.klepus.menuvotingapi.tos.RestaurantTo;
import com.github.klepus.menuvotingapi.tos.VoteTo;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.data.domain.Sort;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static com.github.klepus.menuvotingapi.util.TestUtil.*;
import static com.github.klepus.menuvotingapi.util.ToUtil.createTosFromVotes;
import static com.github.klepus.menuvotingapi.web.RestEndpoints.*;
import static com.github.klepus.menuvotingapi.web.testdata.AllTestData.createRestaurantTosList;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class GuestControllerMockRepoTest {
    @MockBean
    private RestaurantRepository restaurantRepository;

    @MockBean
    private DishRepository dishRepository;

    @MockBean
    private VoteRepository voteRepository;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @Test
    @CacheEvict(cacheNames = { "listOfTos", "mapOfTos" }, allEntries = true)
    public void findAll_emptyRestaurantList() throws Exception {
        when(restaurantRepository
                .findAll(Sort.by(Sort.Direction.ASC, "id")))
                .thenReturn(Collections.emptyList());

        String actual = mockMvc.perform(get(GET_ALL_RESTAURANTS)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(print())
                .andReturn()
                .getResponse()
                .getContentAsString();

        List<RestaurantTo> expected = createRestaurantTosList(Collections.emptyList());
        assertEquals(objectMapper.writeValueAsString(expected), actual);

        verify(restaurantRepository, times(1)).findAll(Sort.by(Sort.Direction.ASC, "id"));
    }

    @Test
    @CacheEvict(cacheNames = { "listOfTos", "mapOfTos" }, allEntries = true)
    public void findAll_emptyVotesList() throws Exception {
        when(voteRepository
                .getAll(any(), any()))
                .thenReturn(Optional.empty());

        String actual = mockMvc.perform(get(GET_CURRENT_VOTES)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(print())
                .andReturn()
                .getResponse()
                .getContentAsString();

        List<VoteTo> expected = createTosFromVotes(Collections.emptyList());
        assertEquals(objectMapper.writeValueAsString(expected), actual);

        verify(voteRepository, times(1)).getAll(TODAY, TODAY);
    }

    @Test
    @CacheEvict(cacheNames = { "listOfTos", "mapOfTos" }, allEntries = true)
    public void findAllMenus() throws Exception {
        when(dishRepository
                .getDishesBetween(any(), any()))
                .thenReturn(Optional.empty());

        String actual = mockMvc.perform(get(GET_ALL_CURRENT_MENUS)
                .param("startDate", YESTERDAY_STRING)
                .param("endDate", TODAY_STRING)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(print())
                .andReturn()
                .getResponse()
                .getContentAsString();

        assertEquals(objectMapper.writeValueAsString(Collections.emptyMap()), actual);
    }
}
