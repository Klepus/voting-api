package com.github.klepus.menuvotingapi.web.user;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.klepus.menuvotingapi.tos.VoteTo;
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

import java.time.LocalDateTime;

import static com.github.klepus.menuvotingapi.web.testdata.AllTestData.*;
import static com.github.klepus.menuvotingapi.web.testdata.UserTestData.USER;
import static com.github.klepus.menuvotingapi.web.testdata.UserTestData.USER_PASSWORD;
import static com.github.klepus.menuvotingapi.util.TestUtil.*;
import static com.github.klepus.menuvotingapi.web.RestEndpoints.POST_VOTE_FOR_RESTAURANT;
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
class UserControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @Test
    @CacheEvict(cacheNames = { "listOfTos", "mapOfTos" }, allEntries = true)
    public void voteForRestaurant_Create() throws Exception {
        ResultActions perform = mockMvc.perform(post(POST_VOTE_FOR_RESTAURANT, "2")
                .contentType(MediaType.APPLICATION_JSON)
                .with(userHttpBasic(USER, USER_PASSWORD))
                .content(""));

        String result = perform.andExpect(status().isCreated())
                .andDo(print())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andReturn()
                .getResponse()
                .getContentAsString();

        VoteTo actual = objectMapper.readValue(result, new TypeReference<>() {
        });
        actual.setDateTime(LocalDateTime.of(
                actual.getDateTime().toLocalDate(),
                actual.getDateTime().toLocalTime().withSecond(0).withNano(0)
        ));

        assertEquals(createVoteTo(NEW_VOTE_7_U1), actual);
    }
}