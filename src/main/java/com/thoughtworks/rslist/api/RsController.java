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

@RestController
public class RsController {

  @Autowired
  RsEventRepository rsEventRepository;
  @Autowired
  UserRepository userRepository;

    public RsController() throws SQLException {
    }


  @GetMapping("/rs/{rsEventId}")
    public ResponseEntity get_index_list(@PathVariable  int rsEventId) {
      Optional<RsEventDto> rsEventDto = rsEventRepository.findById(rsEventId);
      if (!rsEventDto.isPresent()) {
        throw new RsEventNotValidException("invalid rsEventId");
      }
      return ResponseEntity.ok(rsEventDto.get());
  }

  @GetMapping("/rsEvent")
    public ResponseEntity get_rsEvent( ) {
      return ResponseEntity.ok(rsEventRepository.findAll());
  }


  @GetMapping("/users")
  public ResponseEntity get_all_user() {
    return ResponseEntity.ok(userRepository.findAll());
  }

  @PostMapping("/rs/rsEvent")
    public ResponseEntity should_add_rsEvent(@RequestBody RsEvent rsEvent) throws JsonProcessingException {
    Optional<UserDto> userDto = userRepository.findById(rsEvent.getUserId());
      if (!userDto.isPresent()) {
        return ResponseEntity.badRequest().build();
      }
      RsEventDto rsEventDto = RsEventDto.builder()
              .userDto(userDto.get())
              .keyword(rsEvent.getKeyWord())
              .eventName(rsEvent.getEventName())
              .voteNum(rsEvent.getVoteNum())
              .build();
    rsEventRepository.save(rsEventDto);
      return ResponseEntity.created(null).build();
  }



  @PatchMapping("/rs/{rsEventId}")
  public ResponseEntity should_update_rsEvent(@PathVariable int rsEventId, @RequestBody @Valid RsEvent rsEvent) {
    RsEventDto rsEventDto = rsEventRepository.findById(rsEventId).get();
    if (rsEventDto.getUserDto().getId() == rsEvent.getUserId()) {
      if (rsEvent.getEventName() != null) {
        rsEventDto.setEventName(rsEvent.getEventName());
      }
      if (rsEvent.getKeyWord() != null) {
        rsEventDto.setKeyword(rsEvent.getKeyWord());
      }
      rsEventRepository.save(rsEventDto);
      return ResponseEntity.ok().build();
    }else {
      return ResponseEntity.badRequest().build();
    }

  }

}


