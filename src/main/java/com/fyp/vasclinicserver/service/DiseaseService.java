package com.fyp.vasclinicserver.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fyp.vasclinicserver.exceptions.ResourceNotFoundException;
import com.fyp.vasclinicserver.mapper.PagingMapper;
import com.fyp.vasclinicserver.model.Disease;
import com.fyp.vasclinicserver.payload.DiseaseRequest;
import com.fyp.vasclinicserver.repository.DiseaseRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.Optional;

@Slf4j
@AllArgsConstructor
@Service
public class DiseaseService {
    private final DiseaseRepository diseaseRepository;

    public Disease save(DiseaseRequest diseaseRequest) {
        return diseaseRepository.save(new Disease(diseaseRequest.getName()));
    }

    public Page<Disease> getAllDiseases(String sort, String range, String filter) throws JsonProcessingException {
        Pageable paging = PagingMapper.mapToPageable(sort,range);
        Map<String,Object> filterNode = PagingMapper.mapToFilterNode(filter);
        Optional<String> firstKey = filterNode.keySet().stream().findFirst();
        // TODO improve Map<String,Object> filter method involved
        if (firstKey.isPresent()) {
            String key = firstKey.get();
            Object value = filterNode.get(key);
            if(key.equals("id")&& value instanceof Long){
                return diseaseRepository.findById((Long) value,paging);
            }
        }
        return diseaseRepository.findAll(paging);
    }

    public Disease getDisease(Long id) {
        return diseaseRepository.findById(id).orElseThrow(() ->new ResourceNotFoundException("ShiftBoards","id",id));
    }
}
