package com.github.klepus.menuvotingapi.web.admin;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.klepus.menuvotingapi.repository.RestaurantRepository;
import com.github.klepus.menuvotingapi.tos.MenuTo;
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
import org.springframework.test.web.servlet.ResultActions;

import java.time.LocalTime;
import java.util.Arrays;

import static com.github.klepus.menuvotingapi.util.DateTimeUtil.THRESHOLD_TIME;
import static com.github.klepus.menuvotingapi.web.testdata.AllTestData.*;
import static com.github.klepus.menuvotingapi.web.testdata.UserTestData.*;
import static com.github.klepus.menuvotingapi.util.TestUtil.*;
import static com.github.klepus.menuvotingapi.web.RestEndpoints.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@SqlGroup({
        @Sql(executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD, scripts = "classpath:schema.sql", config = @SqlConfig(encoding = "UTF-8")),
        @Sql(executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD, scripts = "classpath:data.sql", config = @SqlConfig(encoding = "UTF-8"))
})
class AdminRestaurantControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    RestaurantRepository restaurantRepository;

    @Autowired
    ObjectMapper objectMapper;

    @Test
    @CacheEvict(cacheNames = { "listOfTos", "mapOfTos" }, allEntries = true)
    public void createRestaurant() throws Exception {
        NEW_RESTAURANT.setId(null);
        String actual = mockMvc.perform(post(POST_ADMIN_CREATE_RESTAURANT)
                .contentType(MediaType.APPLICATION_JSON)
                .with(userHttpBasic(ADMIN, ADMIN_PASSWORD))
                .content(objectMapper.writeValueAsString(createRestaurantTo(NEW_RESTAURANT))))
                .andExpect(status().isCreated())
                .andDo(print())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andReturn()
                .getResponse()
                .getContentAsString();

        NEW_RESTAURANT.setId(4);
        assertEquals(objectMapper.writeValueAsString(createRestaurantTo(NEW_RESTAURANT)), actual);
    }

    @Test
    @CacheEvict(cacheNames = { "listOfTos", "mapOfTos" }, allEntries = true)
    public void updateRestaurant() throws Exception {
        assertEquals(objectMapper.writeValueAsString(RESTAURANT_1), objectMapper.writeValueAsString(restaurantRepository.findById(1)));

        String actual = mockMvc.perform(put(PUT_UPDATE_RESTAURANT_INFO, "1")
                .contentType(MediaType.APPLICATION_JSON)
                .with(userHttpBasic(ADMIN, ADMIN_PASSWORD))
                .content(objectMapper.writeValueAsString(createRestaurantTo(UPD_RESTAURANT))))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andReturn()
                .getResponse()
                .getContentAsString();

        assertEquals(objectMapper.writeValueAsString(createRestaurantTo(UPD_RESTAURANT)), actual);
    }

    @Test
    @CacheEvict(cacheNames = { "listOfTos", "mapOfTos" }, allEntries = true)
    public void deleteRestaurant() throws Exception {
        mockMvc.perform(delete(DELETE_RESTAURANT, "3")
                .contentType(MediaType.APPLICATION_JSON)
                .with(userHttpBasic(ADMIN, ADMIN_PASSWORD)))
                .andExpect(status().isNoContent())
                .andDo(print());
    }

    @Test
    @CacheEvict(cacheNames = { "listOfTos", "mapOfTos" }, allEntries = true)
    public void createMenu() throws Exception {


        ResultActions perform = mockMvc.perform(post(POST_ADMIN_CREATE_MENU, "3")
                .contentType(MediaType.APPLICATION_JSON)
                .with(userHttpBasic(ADMIN, ADMIN_PASSWORD))
                .content(objectMapper.writeValueAsString(new MenuTo(null,null, null,
                        createDishTosSet(Arrays.asList(NEW_DISH_25_R3, NEW_DISH_26_R3))))));
        if (LocalTime.now().isBefore(THRESHOLD_TIME)) {
            String actual = perform
                    .andExpect(status().isCreated())
                    .andDo(print())
                    .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                    .andReturn()
                    .getResponse()
                    .getContentAsString();

            assertEquals(objectMapper.writeValueAsString(createMenuTosMap(Arrays.asList(NEW_DISH_25_R3, NEW_DISH_26_R3))), actual);
        } else {
            perform.andExpect(status().isForbidden())
                    .andDo(print());
        }
    }

    @Test
    @CacheEvict(cacheNames = { "listOfTos", "mapOfTos" }, allEntries = true)
    public void updateMenu() throws Exception {
        String actual = mockMvc.perform(put(PUT_UPDATE_MENU, "2")
                .contentType(MediaType.APPLICATION_JSON)
                .with(userHttpBasic(ADMIN, ADMIN_PASSWORD))
                .content(objectMapper.writeValueAsString(new MenuTo(null,null, null,
                        createDishTosSet(Arrays.asList(UPD_DISH_23_R2, UPD_DISH_24_R2))))))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andReturn()
                .getResponse()
                .getContentAsString();

        UPD_DISH_23_R2.setId(UPD_DISH_23_R2.getId() + 3);
        UPD_DISH_24_R2.setId(UPD_DISH_24_R2.getId() + 1);

        assertEquals(objectMapper.writeValueAsString(createMenuTosMap(Arrays.asList(UPD_DISH_24_R2, UPD_DISH_23_R2))), actual);
    }

    @Test
    @CacheEvict(cacheNames = { "listOfTos", "mapOfTos" }, allEntries = true)
    public void deleteMenu() throws Exception {
        mockMvc.perform(delete(DELETE_MENU, "1")
                .contentType(MediaType.APPLICATION_JSON)
                .with(userHttpBasic(ADMIN, ADMIN_PASSWORD)))
                .andExpect(status().isNoContent())
                .andDo(print());
    }
}