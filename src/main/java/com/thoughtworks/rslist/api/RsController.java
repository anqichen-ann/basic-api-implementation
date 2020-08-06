package com.thoughtworks.rslist.api;

import com.thoughtworks.rslist.dto.RsEventDto;
import com.thoughtworks.rslist.dto.UserDto;
import com.thoughtworks.rslist.respository.RsEventRepository;
import com.thoughtworks.rslist.respository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.thoughtworks.rslist.domain.RsEvent;
import com.thoughtworks.rslist.domain.User;
import com.thoughtworks.rslist.exception.RsEventNotValidException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.xml.crypto.Data;
import java.sql.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static com.thoughtworks.rslist.domain.UserList.userList;

@RestController
public class RsController {

  private List<RsEvent> rsList = rsEventListInit();

  @Autowired
  RsEventRepository rsEventRepository;
  @Autowired
  UserRepository userRepository;

    public RsController() throws SQLException {
    }

    public List<RsEvent> rsEventListInit() throws SQLException {

    User user = new User("rsList","female",20,"rsListAdd@b.com","17777777777");
    userList.add(user);
    List<RsEvent> rsList = new ArrayList<>();
     rsList.add(new RsEvent("第一条事件", "无标签", 1));
      rsList.add(new RsEvent("第二条事件", "无标签", 1));
      rsList.add(new RsEvent("第三条事件", "无标签", 1));
    return rsList;

  }

  @GetMapping("/rs/{index}")
    public ResponseEntity get_index_list(@PathVariable  int index) {
      if (index <1 || index > rsList.size()) {
        throw new RsEventNotValidException("invalid index");
      }
      return ResponseEntity.ok(rsList.get(index-1));
  }

  @GetMapping("/rs/list")
    public ResponseEntity get_list_between(@RequestParam(required = false) Integer start, @RequestParam(required = false) Integer end) {
      if (start != null || end != null) {
          return ResponseEntity.ok(rsList.subList(start-1,end));
      }
      return ResponseEntity.ok(rsList);
  }

  @GetMapping("/user")
  public ResponseEntity get_user() {
    return ResponseEntity.ok(userList);
  }

  @GetMapping("/users")
  public ResponseEntity get_user_regular() {
    int length = userList.size();
    User[] myUserList = new User[length];
    userList.toArray(myUserList);
    return ResponseEntity.ok(myUserList);
  }

  @PostMapping("/rs/event")
    public ResponseEntity should_add_rsEvent(@RequestBody @Valid RsEvent rsEvent) throws JsonProcessingException {
    Optional<UserDto> userDto = userRepository.findById(rsEvent.getUserId());
      if (!userDto.isPresent()) {
        return ResponseEntity.badRequest().build();
      }
    RsEventDto rsEventDto = RsEventDto.builder().eventName(rsEvent.getEventName())
            .keyword(rsEvent.getKeyWord()).userDto(userDto.get()).build();
    rsEventRepository.save(rsEventDto);

      return ResponseEntity.created(null).build();
  }

  @DeleteMapping("/rs/{index}")
    public ResponseEntity should_delete_rsEvent(@PathVariable int index) {
      rsList.remove(index-1);
      return ResponseEntity.ok(rsList);
  }

  @PatchMapping("/rs/list")
  public ResponseEntity should_change_reEvent(@RequestParam int id, @RequestParam String keyWord) {
    rsList.get(id-1).setKeyWord(keyWord);
    return ResponseEntity.ok(rsList);
  }

  @PatchMapping("/rs/list/{index}")
  public ResponseEntity should_change_rsEvent_through_body(@PathVariable int index, @RequestBody RsEvent rsEvent) {
    String newName = rsEvent.getEventName();
    String newKeyWord = rsEvent.getKeyWord();
    RsEvent deRsEvent = rsList.get(index-1);
    deRsEvent.setKeyWord(newKeyWord);
    deRsEvent.setEventName(newName);
    return ResponseEntity.ok(rsList);
  }

}


