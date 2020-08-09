package com.thoughtworks.rslist;

import com.thoughtworks.rslist.respository.RsEventRepository;
import com.thoughtworks.rslist.respository.UserRepository;
import com.thoughtworks.rslist.respository.VoteRepository;
import com.thoughtworks.rslist.service.rsService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AppConfig {

    RsEventRepository rsEventRepository;
    UserRepository userRepository;
    VoteRepository voteRepository;

    @Bean
    public rsService userRsService() {
        return new rsService( rsEventRepository,  userRepository,  voteRepository);
    }
}
