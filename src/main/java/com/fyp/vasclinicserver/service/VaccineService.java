package com.fyp.vasclinicserver.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fyp.vasclinicserver.mapper.PagingMapper;
import com.fyp.vasclinicserver.model.Vaccine;
import com.fyp.vasclinicserver.repository.VaccineRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;
import java.util.Optional;

@Slf4j
@AllArgsConstructor
@Service
public class VaccineService {

    private final VaccineRepository vaccineRepository;

    @Transactional(readOnly = true)
    public Page<Vaccine> getAllVaccine(String sort,String  range, String filter) throws JsonProcessingException {
        Pageable paging = PagingMapper.mapToPageable(sort,range);
        Map<String,Object> filterNode = PagingMapper.mapToFilterNode(filter);
        Optional<String> firstKey = filterNode.keySet().stream().findFirst();
        // TODO improve Map<String,Object> filter method involved
        if (firstKey.isPresent()) {
            String key = firstKey.get();
            Object value = filterNode.get(key);
            if(key.equals("id")&& value instanceof String){
                return vaccineRepository.findById((String) value,paging);
            }
        }
        return vaccineRepository.findByDeletedFalse(paging);

    }

    @Transactional(readOnly = true)
    public Vaccine getVaccine(String id) {
        return vaccineRepository.getById(id);
//                .stream()
//                .map( )
//                .collect(toList());
    }

    public void save(Vaccine vaccine) {
        vaccineRepository.save(vaccine);
    }
}
