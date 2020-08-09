package com.thoughtworks.rslist.respository;

import com.thoughtworks.rslist.dto.VoteDto;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.List;

public interface VoteRepository extends PagingAndSortingRepository<VoteDto, Integer> {
    @Override
    List<VoteDto> findAll();

    //@Query(value = "select v from VoteDto v where v.userDto.id = :userId and v.rsEventDto.id = :rsEventId", nativeQuery=true )
    List<VoteDto> findAllByUserIdAndRsEventId(int userId, int rsEventId, Pageable pageable);
}
