package com.thoughtworks.rslist.dto;

import com.thoughtworks.rslist.domain.User;
import com.thoughtworks.rslist.domain.Vote;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Date;

@Entity
@Table(name = "vote")
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class VoteDto {
    @Id
    @GeneratedValue
    private int id;
    private int voteNum;
    private LocalDateTime localDateTime;

    @ManyToOne
    @JoinColumn(name = "rs_event_id")
    private RsEventDto rsEvent;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private UserDto user;


}
