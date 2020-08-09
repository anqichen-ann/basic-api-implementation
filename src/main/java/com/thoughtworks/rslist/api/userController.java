package com.thoughtworks.rslist.api;

import com.thoughtworks.rslist.domain.User;
import com.thoughtworks.rslist.dto.UserDto;
import com.thoughtworks.rslist.respository.RsEventRepository;
import com.thoughtworks.rslist.respository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.transaction.Transactional;
import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
public class userController {
    UserRepository userRepository;

    public userController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @PostMapping("/user")
    public void user_register(@RequestBody @Valid User user) {
        UserDto userDto = new UserDto();
        userDto.setAge(user.getAge());
        userDto.setEmail(user.getEmail());
        userDto.setGender(user.getGender());
        userDto.setPhone(user.getPhone());
        userDto.setUserName(user.getName());
        userDto.setVoteNum(user.getVoteNum());
        userRepository.save(userDto);

    }

    @DeleteMapping("/user/{id}")
    public ResponseEntity should_delete_user_and_rsEvent(@PathVariable int id) {
        userRepository.deleteById(id);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/user/{id}")
    public ResponseEntity should_get_user_by_id(@PathVariable int id) {
        Optional<UserDto> user = userRepository.findById(id);
        return ResponseEntity.ok(user.get());
    }

}
