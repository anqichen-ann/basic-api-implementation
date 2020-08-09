package com.thoughtworks.rslist.service;

import com.thoughtworks.rslist.domain.Vote;
import com.thoughtworks.rslist.dto.RsEventDto;
import com.thoughtworks.rslist.dto.UserDto;
import com.thoughtworks.rslist.dto.VoteDto;
import com.thoughtworks.rslist.respository.RsEventRepository;
import com.thoughtworks.rslist.respository.UserRepository;
import com.thoughtworks.rslist.respository.VoteRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Service;

import java.util.Optional;

public class rsService {
    final RsEventRepository rsEventRepository;
    final UserRepository userRepository;
    final VoteRepository voteRepository;

    public rsService(RsEventRepository rsEventRepository, UserRepository userRepository, VoteRepository voteRepository) {
        this.rsEventRepository = rsEventRepository;
        this.userRepository = userRepository;
        this.voteRepository = voteRepository;
    }

    public void vote(Vote vote, int rsEventId) {
        Optional<RsEventDto> rsEventDto = rsEventRepository.findById(rsEventId);
        Optional<UserDto> userDto = userRepository.findById(vote.getUserId());
        if (!rsEventDto.isPresent() || !userDto.isPresent()
            || vote.getVoteNum() > userDto.get().getVoteNum()) {
            throw new RuntimeException();
        }
        VoteDto voteDto =
                VoteDto.builder()
                .user(userDto.get())
                .rsEvent(rsEventDto.get())
                .localDateTime(vote.getLocalDateTime())
                .voteNum(vote.getVoteNum())
                .build();
        voteRepository.save(voteDto);
        UserDto addUserDto = userDto.get();
        addUserDto.setVoteNum(addUserDto.getVoteNum()-vote.getVoteNum());
        userRepository.save(addUserDto);
        RsEventDto newRsEventDto = rsEventDto.get();
        newRsEventDto.setVoteNum(newRsEventDto.getVoteNum()+vote.getVoteNum());
        rsEventRepository.save(newRsEventDto);
    }
}
