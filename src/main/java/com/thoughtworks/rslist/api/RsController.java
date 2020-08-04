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
}
