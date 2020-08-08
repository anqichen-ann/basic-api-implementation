package com.thoughtworks.rslist.api;

import com.thoughtworks.rslist.dto.RsEventDto;
import com.thoughtworks.rslist.dto.UserDto;
import com.thoughtworks.rslist.dto.VoteDto;
import com.thoughtworks.rslist.respository.RsEventRepository;
import com.thoughtworks.rslist.respository.UserRepository;
import com.thoughtworks.rslist.respository.VoteRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@SpringBootTest
@AutoConfigureMockMvc
public class VoteControllerTests {
    @Autowired
    MockMvc mockMvc;
    @Autowired
    RsEventRepository rsEventRepository;
    @Autowired
    UserRepository userRepository;
    @Autowired
    VoteRepository voteRepository;

    UserDto userDto;
    RsEventDto rsEventDto;
    VoteDto voteDto;
    LocalDateTime localDateTime;

    @BeforeEach
    void setUp() {
        userDto = userRepository.save(UserDto.builder().email("a@b.com").age(19).gender("female")
                .phone("18888888888").userName("ann").voteNum(10).build());
        rsEventDto = rsEventRepository.save(RsEventDto.builder().eventName("猪肉")
                .keyword("经济").userDto(userDto).voteNum(0).build());
        localDateTime = LocalDateTime.of(2020,7,31,0,0,0);

        for (int i =0; i < 8; i++) {
            localDateTime = localDateTime.plusDays(1);
            voteDto = VoteDto.builder().rsEvent(rsEventDto).voteNum( i + 1).user(userDto)
                    .localDateTime(localDateTime).build();
            voteRepository.save(voteDto);
        }
    }

    @AfterEach
    void tearDown() {
        voteRepository.deleteAll();
        rsEventRepository.deleteAll();
        userRepository.deleteAll();
    }


    @Test
    public void should_get_vote_record_pageable() throws Exception {
        mockMvc.perform(get("/voteRecord")
                .param("userId", String.valueOf(userDto.getId()))
                .param("rsEventId", String.valueOf(rsEventDto.getId()))
                .param("pageIndex", "1"))
                .andExpect(jsonPath("$", hasSize(5)))
                .andExpect(jsonPath("$[0].userId", is(userDto.getId())))
                .andExpect(jsonPath("$[0].rsEventId", is(rsEventDto.getId())))
                .andExpect(jsonPath("$[0].voteNum", is(1)))
                .andExpect(jsonPath("$[1].voteNum", is(2)))
                .andExpect(jsonPath("$[2].voteNum", is(3)))
                .andExpect(jsonPath("$[3].voteNum", is(4)))
                .andExpect(jsonPath("$[4].voteNum", is(5)));

        mockMvc.perform(get("/voteRecord")
                .param("userId", String.valueOf(userDto.getId()))
                .param("rsEventId", String.valueOf(rsEventDto.getId()))
                .param("pageIndex", "2"))
                .andExpect(jsonPath("$", hasSize(3)))
                .andExpect(jsonPath("$[0].voteNum", is(6)))
                .andExpect(jsonPath("$[1].voteNum", is(7)))
                .andExpect(jsonPath("$[2].voteNum", is(8)));

    }

    @Test
    public void should_get_voteRecord_between_days() throws Exception {
        LocalDateTime startTime = LocalDateTime.of(2020,7,31,0,0,0);
        LocalDateTime endTime = LocalDateTime.of(2020,8,5,1,0,0);
        DateTimeFormatter df = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        mockMvc.perform(get("/voteRecord/time")
                .param("startTimeString", df.format(startTime))
                .param("endTimeString", df.format(endTime)))
                .andExpect(jsonPath("$",hasSize(5)))
                .andExpect(status().isOk());
    }

    @Test
    public void should_get_null_voteRecord_between_days() throws Exception {
        LocalDateTime startTime = LocalDateTime.of(2020,7,29,0,0,0);
        LocalDateTime endTime = LocalDateTime.of(2020,7,31,1,0,0);
        DateTimeFormatter df = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        mockMvc.perform(get("/voteRecord/time")
                .param("startTimeString", df.format(startTime))
                .param("endTimeString", df.format(endTime)))
                .andExpect(jsonPath("$",hasSize(0)))
                .andExpect(status().isOk());
    }



}
