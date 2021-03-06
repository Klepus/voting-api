package com.github.klepus.menuvotingapi.web.guest;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.klepus.menuvotingapi.tos.*;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.http.MediaType;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlConfig;
import org.springframework.test.context.jdbc.SqlGroup;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import static com.github.klepus.menuvotingapi.util.ToUtil.createTosFromVotes;
import static com.github.klepus.menuvotingapi.web.RestEndpoints.*;
import static com.github.klepus.menuvotingapi.web.testdata.AllTestData.*;
import static com.github.klepus.menuvotingapi.util.TestUtil.TODAY_STRING;
import static com.github.klepus.menuvotingapi.util.TestUtil.YESTERDAY_STRING;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@SqlGroup({
        @Sql(executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD, scripts = "classpath:schema.sql", config = @SqlConfig(encoding = "UTF-8")),
        @Sql(executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD, scripts = "classpath:data.sql", config = @SqlConfig(encoding = "UTF-8"))
})
class GuestControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @Test
    @Disabled
    @CacheEvict(cacheNames = { "listOfTos", "mapOfTos" }, allEntries = true)
    public void findAll() throws Exception {
        String actual = mockMvc.perform(get(GET_ALL_RESTAURANTS)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(print())
                .andReturn()
                .getResponse()
                .getContentAsString();

        List<RestaurantTo> expected = createRestaurantTosList(Arrays.asList(RESTAURANT_1, RESTAURANT_2, RESTAURANT_3));
        assertEquals(objectMapper.writeValueAsString(expected), actual);
    }

    @Test
    @Disabled
    @CacheEvict(cacheNames = { "listOfTos", "mapOfTos" }, allEntries = true)
    public void findAllMenus() throws Exception {
        String actual = mockMvc.perform(get(GET_ALL_CURRENT_MENUS)
                .param("startDate", YESTERDAY_STRING)
                .param("endDate", TODAY_STRING)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(print())
                .andReturn()
                .getResponse()
                .getContentAsString();

        Map<LocalDate, List<MenuTo>> expected = createMenuTosMap(Arrays.asList(
                DISH_15_R1, DISH_16_R1,
                DISH_17_R2, DISH_18_R2,
                DISH_19_R3, DISH_20_R3,
                DISH_21_R1, DISH_22_R1,
                DISH_23_R2, DISH_24_R2));

        assertEquals(objectMapper.writeValueAsString(expected), actual);
    }

    @Test
    @CacheEvict(cacheNames = { "listOfTos", "mapOfTos" }, allEntries = true)
    public void getSingleRestaurantMenu() throws Exception {
        int restaurantId = 1;
        String actual = mockMvc.perform(get(GET_CURRENT_RESTAURANT_MENU_BY_ID, restaurantId)
                .param("id", String.valueOf(restaurantId))
                .param("startDate", YESTERDAY_STRING)
                .param("endDate", YESTERDAY_STRING)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(print())
                .andReturn()
                .getResponse()
                .getContentAsString();

        Map<LocalDate, List<MenuTo>> expected = createMenuTosMap(Arrays.asList(DISH_15_R1, DISH_16_R1));
        assertEquals(objectMapper.writeValueAsString(expected), actual);
    }

    @Test
    public void findAllVotes() throws Exception {
        String actual = mockMvc.perform(get(GET_CURRENT_VOTES + "?startDate=" + YESTERDAY_STRING)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(print())
                .andReturn()
                .getResponse()
                .getContentAsString();

        List<VoteTo> expected = createTosFromVotes(Arrays.asList(VOTE_5_U1, VOTE_6_U2));
        assertEquals(objectMapper.writeValueAsString(expected), actual);
    }
}