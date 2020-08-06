package com.thoughtworks.rslist.api;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.thoughtworks.rslist.domain.RsEvent;
import com.thoughtworks.rslist.domain.User;
import com.thoughtworks.rslist.exception.RsEventNotValidException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static com.thoughtworks.rslist.domain.UserList.userList;

@RestController
public class RsController {

  @GetMapping("/rs/list")
 public String get() {
    return rsList.toString();
  }
  private List<RsEvent> rsList = rsEventListInit();
  public List<RsEvent> rsEventListInit() {
    User user = new User("rsList","female",20,"rsListAdd@b.com","17777777777");
    userList.add(user);
    List<RsEvent> rsList = new ArrayList<>();
     rsList.add(new RsEvent("第一条事件", "无标签", user));
      rsList.add(new RsEvent("第二条事件", "无标签", user));
      rsList.add(new RsEvent("第三条事件", "无标签", user));
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
      User user = rsEvent.getUser();
      String userName = user.getName();
      Boolean find = false;
      for (User us : userList) {
        if (us.getName().equals(userName)) {
          find = true;
          break;
        }
      }
      if (!find) {
        userList.add(user);
      }
      rsList.add(rsEvent);
      String indexString = (rsList.size()-1) + "";
      return ResponseEntity.created(null).header("index",indexString).build();
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


