package com.github.klepus.menuvotingapi.web.user;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.klepus.menuvotingapi.repository.RestaurantRepository;
import com.github.klepus.menuvotingapi.repository.VoteRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlConfig;
import org.springframework.test.context.jdbc.SqlGroup;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.time.LocalTime;
import java.util.Optional;

import static com.github.klepus.menuvotingapi.UserTestData.USER;
import static com.github.klepus.menuvotingapi.UserTestData.USER_PASSWORD;
import static com.github.klepus.menuvotingapi.util.DateTimeUtil.THRESHOLD_TIME;
import static com.github.klepus.menuvotingapi.util.TestUtil.userHttpBasic;
import static com.github.klepus.menuvotingapi.web.RestEndpoints.POST_VOTE_FOR_RESTAURANT;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@SpringBootTest
@AutoConfigureMockMvc
@SqlGroup({
        @Sql(executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD, scripts = "classpath:schema.sql", config = @SqlConfig(encoding = "UTF-8")),
        @Sql(executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD, scripts = "classpath:data.sql", config = @SqlConfig(encoding = "UTF-8"))
})
class UserControllerExceptionTest {
    @MockBean
    private RestaurantRepository restaurantRepository;

    @MockBean
    private VoteRepository voteRepository;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @Test
    public void voteForRestaurant() throws Exception {
        when(restaurantRepository
                .findById(any()))
                .thenReturn(Optional.empty());

        ResultActions perform = mockMvc.perform(post(POST_VOTE_FOR_RESTAURANT, "1")
                .contentType(MediaType.APPLICATION_JSON)
                .with(userHttpBasic(USER, USER_PASSWORD))
                .content(""));

        if (LocalTime.now().isBefore(THRESHOLD_TIME)) {
            perform
                    .andExpect(status().isNotFound())
                    .andDo(print());

            verify(restaurantRepository, times(1)).findById(any());
            verify(voteRepository, times(0)).findByUserIdAndDate(any(), any());
        } else {
            perform.andExpect(status().isForbidden()).andDo(print());
        }
    }
}
