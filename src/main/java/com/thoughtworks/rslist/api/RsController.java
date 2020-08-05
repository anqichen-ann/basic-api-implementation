package com.thoughtworks.rslist.api;

import org.springframework.web.bind.annotation.RestController;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.thoughtworks.rslist.domain.RsEvent;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@RestController
public class RsController {
  private List<RsEvent> rsList = rsEventListInit();
  public List<RsEvent> rsEventListInit() {
     List<RsEvent> rsList = new ArrayList<>();
     rsList.add(new RsEvent("第一条事件", "无标签"));
      rsList.add(new RsEvent("第二条事件", "无标签"));
      rsList.add(new RsEvent("第三条事件", "无标签"));
    return rsList;

  }

  @GetMapping("/rs/{index}")
    public RsEvent get_index_list(@PathVariable  int index) {
      return rsList.get(index-1);
  }

  @GetMapping("/rs/list")
    public List<RsEvent> get_list_between(@RequestParam(required = false) Integer start, @RequestParam(required = false) Integer end) {
      if (start != null || end != null) {
          return rsList.subList(start-1,end);
      }
      return rsList;
  }

  @PostMapping("/rs/event")
    public void should_add_rsevent(@RequestBody String jsonString) throws JsonProcessingException {
      ObjectMapper objectMapper = new ObjectMapper();
      RsEvent rsEvent = objectMapper.readValue(jsonString, RsEvent.class);
      rsList.add(rsEvent);
  }

  @DeleteMapping("/rs/{index}")
    public void should_delete_rsEvent(@PathVariable int index) {
      //System.out.println("delete"+index);
      rsList.remove(index-1);
  }

  @PatchMapping("/rs/list")
  public List<RsEvent> should_change_reEvent(@RequestParam int id, @RequestParam String keyWord) {
    rsList.get(id-1).setKeyWord(keyWord);
    return rsList;
  }

  @PatchMapping("/rs/list/{index}")
  public List<RsEvent> should_change_rsEvent_through_body(@PathVariable int index, @RequestBody RsEvent rsEvent) {
    String newName = rsEvent.getEventName();
    String newKeyWord = rsEvent.getKeyWord();
    RsEvent deRsEvent = rsList.get(index-1);
    deRsEvent.setKeyWord(newKeyWord);
    deRsEvent.setEventName(newName);
    return rsList;
  }
}
