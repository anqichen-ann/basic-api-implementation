package com.thoughtworks.rslist;

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
    @Autowired
    UserDto userDto;

    @BeforeEach
    void setUp() {
        userDto = userRepository.save(UserDto.builder().email("a@b.com").age(19).gender("female")
                .phone("18888888888").userName("ann").voteNum(10).build());
        rsEventRepository.deleteAll();
    }

    @Test
    @Order(1)
    void get_contextLoads() throws Exception {
        mockMvc.perform(get("/rs/list")).andExpect(jsonPath("$", hasSize(3)))
                .andExpect(jsonPath("$[0].eventName", is("第一条事件")))
                .andExpect(jsonPath("$[0].keyWord", is("无标签")))
                .andExpect(jsonPath("$[0]",not(hasKey("user"))))
                .andExpect(jsonPath("$[1].eventName", is("第二条事件")))
                .andExpect(jsonPath("$[1].keyWord", is("无标签")))
                .andExpect(jsonPath("$[0]",not(hasKey("user"))))
                .andExpect(jsonPath("$[2].eventName", is("第三条事件")))
                .andExpect(jsonPath("$[2].keyWord", is("无标签")))
                .andExpect(jsonPath("$[0]",not(hasKey("user"))))
                .andExpect(status().isOk());
    }

    @Test
    @Order(2)
    void should_get_index_list() throws Exception {
        mockMvc.perform(get("/rs/1"))
                .andExpect(jsonPath("$.eventName", is("第一条事件")))
                .andExpect(jsonPath("$.keyWord", is("无标签")))
                .andExpect(jsonPath("$",not(hasKey("user"))))
                .andExpect(status().isOk());
        mockMvc.perform(get("/rs/2"))
                .andExpect(jsonPath("$.eventName", is("第二条事件")))
                .andExpect(jsonPath("$.keyWord", is("无标签")))
                .andExpect(jsonPath("$",not(hasKey("user"))))
                .andExpect(status().isOk());
        mockMvc.perform(get("/rs/3"))
                //.andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$.eventName", is("第三条事件")))
                .andExpect(jsonPath("$.keyWord", is("无标签")))
                .andExpect(jsonPath("$",not(hasKey("user"))))
                .andExpect(status().isOk());
    }

    @Test
    @Order(3)
    void get_list_between() throws Exception {
        mockMvc.perform(get("/rs/list?start=1&end=2"))
                .andExpect(jsonPath("$[0].eventName", is("第一条事件")))
                .andExpect(jsonPath("$[0].keyWord", is("无标签")))
                .andExpect(jsonPath("$[1].eventName", is("第二条事件")))
                .andExpect(jsonPath("$[1].keyWord", is("无标签")))
                .andExpect(status().isOk());
        mockMvc.perform(get("/rs/list?start=2&end=3"))
                .andExpect(jsonPath("$[0].eventName", is("第二条事件")))
                .andExpect(jsonPath("$[0].keyWord", is("无标签")))
                .andExpect(jsonPath("$[1].eventName", is("第三条事件")))
                .andExpect(jsonPath("$[1].keyWord", is("无标签")))
                .andExpect(status().isOk());
        mockMvc.perform(get("/rs/list?start=1&end=3"))
                .andExpect(jsonPath("$[0].eventName", is("第一条事件")))
                .andExpect(jsonPath("$[0].keyWord", is("无标签")))
                .andExpect(jsonPath("$[1].eventName", is("第二条事件")))
                .andExpect(jsonPath("$[1].keyWord", is("无标签")))
                .andExpect(jsonPath("$[2].eventName", is("第三条事件")))
                .andExpect(jsonPath("$[2].keyWord", is("无标签")))
                .andExpect(status().isOk());
    }


    @Test
    @Order(4)
    public void should_add_new_rsEvent() throws Exception {
//        ObjectMapper objectMapper = new ObjectMapper();
//        User user = new User("annie","female",20,"a@b.com","17777777777");
//        RsEvent rsEvent = new RsEvent("猪肉涨价啦", "经济", user);
//        String jsonString = objectMapper.writeValueAsString(rsEvent);
        String jsonString = "{\"eventName\":\"猪肉涨价啦\",\"keyWord\":\"经济\",\"user\": {\"name\":\"ann\"," +
                "\"age\": 19,\"gender\": \"female\",\"email\": \"a@b.com\",\"phone\": \"18888888888\"}}";
        mockMvc.perform(post("/rs/event").content(jsonString).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated());
        mockMvc.perform(get("/rs/list")).andExpect(jsonPath("$", hasSize(4)))
                .andExpect(jsonPath("$[0].eventName", is("第一条事件")))
                .andExpect(jsonPath("$[0].keyWord", is("无标签")))
                .andExpect(jsonPath("$[1].eventName", is("第二条事件")))
                .andExpect(jsonPath("$[1].keyWord", is("无标签")))
                .andExpect(jsonPath("$[2].eventName", is("第三条事件")))
                .andExpect(jsonPath("$[2].keyWord", is("无标签")))
                .andExpect(jsonPath("$[3].eventName", is("猪肉涨价啦")))
                .andExpect(jsonPath("$[3].keyWord", is("经济")))
                .andExpect(status().isOk());
    }

    @Test
    @Order(5)
    public void should_delete_rsEvent() throws Exception {
        mockMvc.perform(delete("/rs/1"))
                .andExpect(status().isOk());
        mockMvc.perform(get("/rs/list"))
                .andExpect(jsonPath("$", hasSize(3)))
                .andExpect(jsonPath("$[0].eventName", is("第二条事件")))
                .andExpect(jsonPath("$[0].keyWord", is("无标签")))
                .andExpect(jsonPath("$[1].eventName", is("第三条事件")))
                .andExpect(jsonPath("$[1].keyWord", is("无标签")))
                .andExpect(jsonPath("$[2].eventName", is("猪肉涨价啦")))
                .andExpect(jsonPath("$[2].keyWord", is("经济")))
                .andExpect(status().isOk());
    }

    @Test
    public void should_change_rslist() throws Exception {
        mockMvc.perform(patch("/rs/list?id=1&keyWord=case"))
                .andExpect(jsonPath("$[0].keyWord", is("case")))
                .andExpect(status().isOk());
    }

    @Test
    public void should_change_rsEvent_through_body() throws Exception {
        RsEvent rsEvent = new RsEvent("股票崩了","经济", 1);
        String jsonString = new ObjectMapper().writeValueAsString(rsEvent);
        mockMvc.perform(patch("/rs/list/3").content(jsonString).contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[2].eventName", is("股票崩了")))
                .andExpect(jsonPath("$[2].keyWord", is("经济")))
                .andExpect(status().isOk());

    }

    @Test
    public void should_throw_exception_get_list_between() throws Exception {
        mockMvc.perform(get("/rs/list?start=0&end=2"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error",is("invalid request param")));

    }

    @Test
    public void throw_exception_when_index_not_valid() throws Exception {
        mockMvc.perform(get("/rs/0"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error",is("invalid index")));

    }

    @Test
    public void throw_exception_when_user_invalid() throws Exception {
        String jsonString = "{\"eventName\":\"元气满满\",\"keyWord\":\"娱乐\",\"user\": {\"name\":\"元气满满的乘风破浪\"," +
                "\"age\": 30,\"gender\": \"male\",\"email\": \"xzq@b.com\",\"phone\": \"12345678901\"}}";
        mockMvc.perform(post("/rs/event").content(jsonString).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error", is("invalid param")));
    }

    @Test
    public void throw_exception_when_eventName_null() throws Exception {
        String jsonString = "{\"eventName\":null,\"keyWord\":\"娱乐\",\"user\": {\"name\":\"xzq\"," +
                "\"age\": 35,\"gender\": \"male\",\"email\": \"xzq@b.com\",\"phone\": \"12345678901\"}}";
        mockMvc.perform(post("/rs/event").content(jsonString).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void throw_exception_when_keyWord_null() throws Exception {
        String jsonString = "{\"eventName\":\"keyWordError\",\"keyWord\":null,\"user\": {\"name\":\"xzq2\"," +
                "\"age\": 35,\"gender\": \"male\",\"email\": \"xzq@b.com\",\"phone\": \"12345678901\"}}";
        mockMvc.perform(post("/rs/event").content(jsonString).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void throw_exception_when_user_null() throws Exception {
        String jsonString = "{\"eventName\":\"userError\",\"keyWord\":\"娱乐\",\"user\": null}";
        mockMvc.perform(post("/rs/event").content(jsonString).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void should_add_newUser() throws Exception {
        User user = new User("newUser","male",35,"xzq@b.com","12345678901");
        RsEvent rsEvent = new RsEvent("addNewUser","娱乐",1);
//        String jsonString = "{\"eventName\":\"addNewUser\",\"keyWord\":\"娱乐\",\"user\": {\"name\":\"newUser\"," +
//                "\"age\": 35,\"gender\": \"male\",\"email\": \"xzq@b.com\",\"phone\": \"12345678901\"}}";
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.configure(USE_ANNOTATIONS, false);
        String jsonString = objectMapper.writeValueAsString(rsEvent);
        mockMvc.perform(post("/rs/event").content(jsonString).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated());
        mockMvc.perform(get("/users"))
                .andExpect(jsonPath("$[-1].user_name", is("newUser")));
    }

    @Test
    public void should_not_add_User() throws Exception {
        String jsonString = "{\"eventName\":\"notAddUser\",\"keyWord\":\"娱乐\",\"user\": {\"name\":\"newUser\"," +
                "\"age\": 21,\"gender\": \"female\",\"email\": \"abcd@b.com\",\"phone\": \"11234567890\"}}";
        mockMvc.perform(post("/rs/event").content(jsonString).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated());
        mockMvc.perform(get("/users"))
                .andExpect(jsonPath("$[-1].user_email", not("abcd@b.com")));
    }

    @Test
    public void should_get_header_response () throws Exception {
        String jsonString = "{\"eventName\":\"headerTest\",\"keyWord\":\"SpringBoot\",\"user\": {\"name\":\"hearUser\"," +
                "\"age\": 21,\"gender\": \"female\",\"email\": \"abcd@b.com\",\"phone\": \"11234567890\"}}";
        mockMvc.perform(post("/rs/event").content(jsonString).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(header().string("index","5"));
    }

    @Test
    public void should_show_all_users() throws Exception {
        mockMvc.perform(get("/users"))
                .andExpect(jsonPath("$[0].user_name", is("rsList")))
                .andExpect(jsonPath("$[0].user_gender", is("female")))
                .andExpect(jsonPath("$[0].user_email", is("rsListAdd@b.com")))
                .andExpect(jsonPath("$[0].user_phone", is("17777777777")))
                .andExpect(jsonPath("$[0].user_age", is(20)));
    }

    @Test
    public void should_add_new_rsEvent_when_user_exists() throws Exception {

        String jsonString = "{\"eventName\":\"猪肉涨价啦\",\"keyWord\":\"经济\",\"userId\": " + userDto.getId() + "}";
        mockMvc.perform(post("/rs/event").content(jsonString).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated());
        List<RsEventDto> allRsEventDto =  rsEventRepository.findAll();
        assertNotNull(allRsEventDto);
        assertEquals("猪肉涨价啦", allRsEventDto.get(0).getEventName());
        assertEquals("经济", allRsEventDto.get(0).getKeyword());
        assertEquals(userDto.getId(), allRsEventDto.get(0).getUserDto().getId());

    }

    @Test
    public void should_not_add_new_rsEvent_when_user_not_exists() throws Exception {
        String jsonString = "{\"eventName\":\"猪肉涨价啦\",\"keyWord\":\"经济\",\"userId\": 100 }";
        mockMvc.perform(post("/rs/event").content(jsonString).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());


    }


}
