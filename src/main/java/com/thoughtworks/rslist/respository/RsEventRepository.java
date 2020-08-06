package com.thoughtworks.rslist.respository;

import com.thoughtworks.rslist.dto.RsEventDto;
import org.springframework.data.repository.CrudRepository;

import javax.transaction.Transactional;
import java.util.List;

public interface RsEventRepository extends CrudRepository<RsEventDto, Integer> {

    @Override
    List<RsEventDto> findAll();

}
