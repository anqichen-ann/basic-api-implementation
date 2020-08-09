package com.thoughtworks.rslist.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.thoughtworks.rslist.domain.Vote;
import com.thoughtworks.rslist.dto.RsEventDto;
import com.thoughtworks.rslist.dto.UserDto;
import com.thoughtworks.rslist.dto.VoteDto;
import com.thoughtworks.rslist.respository.RsEventRepository;
import com.thoughtworks.rslist.respository.UserRepository;
import com.thoughtworks.rslist.respository.VoteRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

class RsServiceTest {
    rsService rsService;
    @Mock
    RsEventRepository rsEventRepository;
    @Mock
    UserRepository userRepository;
    @Mock
    VoteRepository voteRepository;
    LocalDateTime localDateTime;

    @BeforeEach
    void setUp() {
        initMocks(this);
        rsService = new rsService(rsEventRepository, userRepository, voteRepository);
        localDateTime = LocalDateTime.now();
    }

    @Test
    public void should_vote_success() {
        Vote vote = Vote.builder()
                .voteNum(2)
                .localDateTime(localDateTime)
                .userId(1)
                .rsEventId(2)
                .build();
        UserDto userDto = UserDto.builder()
                .userName("xzq")
                .age(25)
                .email("a@b.com")
                .gender("male")
                .phone("12345678987")
                .voteNum(4)
                .build();
        RsEventDto rsEventDto = RsEventDto.builder()
                .userDto(userDto)
                .eventName("eventName")
                .keyword("keyword")
                .voteNum(1)
                .id(1)
                .build();
        when(rsEventRepository.findById(anyInt())).thenReturn(Optional.of(rsEventDto));
        when(userRepository.findById(anyInt())).thenReturn(Optional.of(userDto));

        rsService.vote(vote, 1);

        verify(voteRepository).save(VoteDto.builder()
        .user(userDto)
        .rsEvent(rsEventDto)
        .localDateTime(localDateTime)
        .voteNum(2)
        .build());
        verify(rsEventRepository).save(rsEventDto);
        verify(userRepository).save(userDto);
        assertEquals(userDto.getVoteNum(),2);
        assertEquals(rsEventDto.getVoteNum(), 3);
    }

    @Test
    public void should_throw_exception_when_voteNum_invalid() {
        Vote vote = Vote.builder()
                .voteNum(5)
                .localDateTime(localDateTime)
                .userId(1)
                .rsEventId(2)
                .build();
        UserDto userDto = UserDto.builder()
                .userName("xzq")
                .age(25)
                .email("a@b.com")
                .gender("male")
                .phone("12345678987")
                .voteNum(4)
                .build();
        RsEventDto rsEventDto = RsEventDto.builder()
                .userDto(userDto)
                .eventName("eventName")
                .keyword("keyword")
                .voteNum(1)
                .build();
        when(rsEventRepository.findById(anyInt())).thenReturn(Optional.of(rsEventDto));
        when(userRepository.findById(anyInt())).thenReturn(Optional.of(userDto));

        assertThrows(RuntimeException.class, () -> rsService.vote(vote, 1));
    }

    @Test
    public void should_throw_exception_when_rsEventId_not_exit() {
        Vote vote = Vote.builder()
                .voteNum(2)
                .localDateTime(localDateTime)
                .userId(1)
                .rsEventId(2)
                .build();
        UserDto userDto = UserDto.builder()
                .userName("xzq")
                .age(25)
                .email("a@b.com")
                .gender("male")
                .phone("12345678987")
                .voteNum(4)
                .build();
        RsEventDto rsEventDto = RsEventDto.builder()
                .userDto(userDto)
                .eventName("eventName")
                .keyword("keyword")
                .voteNum(1)
                .id(1)
                .build();
        when(rsEventRepository.findById(anyInt())).thenReturn(Optional.empty());
        when(userRepository.findById(anyInt())).thenReturn(Optional.of(userDto));

        assertThrows(RuntimeException.class, () -> rsService.vote(vote, 1));
    }

    @Test
    public void should_throw_exception_when_userId_not_exit() {
        Vote vote = Vote.builder()
                .voteNum(2)
                .localDateTime(localDateTime)
                .userId(1)
                .rsEventId(2)
                .build();
        UserDto userDto = UserDto.builder()
                .userName("xzq")
                .age(25)
                .email("a@b.com")
                .gender("male")
                .phone("12345678987")
                .voteNum(4)
                .id(1)
                .build();
        RsEventDto rsEventDto = RsEventDto.builder()
                .userDto(userDto)
                .eventName("eventName")
                .keyword("keyword")
                .voteNum(1)
                .id(1)
                .build();
        when(rsEventRepository.findById(anyInt())).thenReturn(Optional.of(rsEventDto));
        when(userRepository.findById(anyInt())).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> rsService.vote(vote, 1));
    }
}
