package com.thoughtworks.rslist.api;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.thoughtworks.rslist.domain.User;
import com.thoughtworks.rslist.dto.RsEventDto;
import com.thoughtworks.rslist.dto.UserDto;
import com.thoughtworks.rslist.respository.RsEventRepository;
import com.thoughtworks.rslist.respository.UserRepository;
import jdk.nashorn.internal.ir.annotations.Ignore;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.thoughtworks.rslist.api.RsController;
import com.thoughtworks.rslist.domain.RsEvent;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import javax.xml.ws.Dispatch;

import java.util.List;
import java.util.Optional;

import static com.fasterxml.jackson.databind.MapperFeature.USE_ANNOTATIONS;
import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;



@SpringBootTest
@AutoConfigureMockMvc
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class RsListApplicationTests {
    @Autowired
    MockMvc mockMvc;
    @Autowired
    UserRepository userRepository;
    @Autowired
    RsEventRepository rsEventRepository;

    UserDto userDto;
    RsEventDto rsEventDto;
    ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        userDto = userRepository.save(UserDto.builder().email("a@b.com").age(19).gender("female")
                .phone("18888888888").userName("ann").voteNum(10).build());
        rsEventDto = rsEventRepository.save(RsEventDto.builder().eventName("猪肉")
                .keyword("经济").userDto(userDto).voteNum(0).build());
        objectMapper = new ObjectMapper();

    }

    @Test
    @Order(1)
    void should_get_rsEvent() throws Exception {
        mockMvc.perform(get("/rsEvent"))
                .andExpect(jsonPath("$[0].eventName", is("猪肉")))
                .andExpect(jsonPath("$[0].keyword", is("经济")))
                .andExpect(jsonPath("$[0].voteNum", is(0)))
                .andExpect(jsonPath("$[0]", not(hasKey("userDto"))))
                .andExpect(status().isOk());
    }

    @Test
    @Order(2)
    void should_get_rsEvent_by_rsEventId() throws Exception {
        mockMvc.perform(get("/rs/{rsEventId}", String.valueOf(rsEventDto.getId())))
                .andExpect(jsonPath("$.eventName", is("猪肉")))
                .andExpect(jsonPath("$.keyword", is("经济")))
                .andExpect(jsonPath("$.voteNum", is(0)))
                .andExpect(jsonPath("$", not(hasKey("userDto"))))
                .andExpect(status().isOk());
    }

    @Test
    @Order(2)
    void should_throw_exception_when_rsEventId_invalid() throws Exception {
        mockMvc.perform(get("/rs/{rsEventId}", String.valueOf(100)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error",is("invalid rsEventId")));
    }


    @Test
    public void should_add_new_rsEvent_when_user_exists() throws Exception {
        String jsonString = "{\"eventName\":\"特朗普\",\"keyWord\":\"政治\",\"userId\": " + userDto.getId()+ " }";
        mockMvc.perform(post("/rs/rsEvent").content(jsonString).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated());

    }

    @Test
    public void should_not_add_new_rsEvent_when_user_not_exists() throws Exception {
        String jsonString = "{\"eventName\":\"猪肉涨价啦\",\"keyWord\":\"经济\",\"userId\": 100 }";
        mockMvc.perform(post("/rs/rsEvent").content(jsonString).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());

    }


    @Test
    public void should_show_all_users() throws Exception {
        mockMvc.perform(get("/users"))
                .andExpect(jsonPath("$[0].userName", is("ann")))
                .andExpect(jsonPath("$[0].gender", is("female")))
                .andExpect(jsonPath("$[0].email", is("a@b.com")))
                .andExpect(jsonPath("$[0].phone", is("18888888888")))
                .andExpect(jsonPath("$[0].age", is(19)))
                .andExpect(jsonPath("$[0].voteNum", is(10)));
    }


    @Test
    public void should_update_rsEvent_when_rsEventId_match_userId() throws Exception {
        RsEventDto oldRsEventDto = rsEventRepository.save(RsEventDto.builder().eventName("猪肉")
                .keyword("经济").userDto(userDto).build());
        String jsonString = "{\"eventName\":\"乘风破浪\",\"keyWord\":\"娱乐\",\"userId\": " + userDto.getId() + "}";
        mockMvc.perform(patch("/rs/{rsEventId}",String.valueOf(oldRsEventDto.getId()))
                .content(jsonString).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
        RsEventDto newRsEvent = rsEventRepository.findById(oldRsEventDto.getId()).get();
        assertEquals("乘风破浪", newRsEvent.getEventName());
        assertEquals("娱乐", newRsEvent.getKeyword());


    }

    @Test
    public void should_throw_exception_when_userId_null() throws Exception {
        RsEventDto oldRsEventDto = rsEventRepository.save(RsEventDto.builder().eventName("猪肉")
                .keyword("经济").userDto(userDto).build());
        String jsonString = "{\"eventName\":\"乘风破浪\",\"keyWord\":\"娱乐\",\"userId\": }";
        mockMvc.perform(patch("/rs/{rsEventId}",String.valueOf(oldRsEventDto.getId()))
                .content(jsonString).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void should_update_rsEvent_when_only_eventName() throws Exception {
        RsEventDto oldRsEventDto = rsEventRepository.save(RsEventDto.builder().eventName("猪肉")
                .keyword("经济").userDto(userDto).build());
        String jsonString = "{\"eventName\":\"乘风破浪\",\"keyWord\":null,\"userId\": " + userDto.getId() + "}";
        mockMvc.perform(patch("/rs/{rsEventId}",String.valueOf(oldRsEventDto.getId()))
                .content(jsonString).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
        RsEventDto newRsEvent = rsEventRepository.findById(oldRsEventDto.getId()).get();
        assertEquals("乘风破浪", newRsEvent.getEventName());
        assertEquals("经济", newRsEvent.getKeyword());
    }

    @Test
    public void should_update_rsEvent_when_only_keyword() throws Exception {
        RsEventDto oldRsEventDto = rsEventRepository.save(RsEventDto.builder().eventName("猪肉")
                .keyword("经济").userDto(userDto).build());
        String jsonString = "{\"eventName\":null,\"keyWord\":\"娱乐\",\"userId\": " + userDto.getId() + "}";
        mockMvc.perform(patch("/rs/{rsEventId}",String.valueOf(oldRsEventDto.getId()))
                .content(jsonString).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
        RsEventDto newRsEvent = rsEventRepository.findById(oldRsEventDto.getId()).get();
        assertEquals("猪肉", newRsEvent.getEventName());
        assertEquals("娱乐", newRsEvent.getKeyword());
    }

}
