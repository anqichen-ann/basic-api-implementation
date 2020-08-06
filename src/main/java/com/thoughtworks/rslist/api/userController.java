package com.thoughtworks.rslist.api;

import com.thoughtworks.rslist.domain.User;
import com.thoughtworks.rslist.dto.UserDto;
import com.thoughtworks.rslist.respository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;

@RestController
public class userController {
    List<User> userList = new ArrayList<>();
    @Autowired
    UserRepository userRepository;
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

}
