package com.fyp.vasclinicserver.mapper;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.util.List;
import java.util.Map;

public class PagingMapper {
    public static Pageable mapToPageable(String sort, String  range) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        List<String> sortList = mapper.readValue(sort, new TypeReference<List<String>>() {});
        List<Integer> rangeList = mapper.readValue(range, new TypeReference<List<Integer>>() {});
      return PageRequest.of(rangeList.get(0), rangeList.get(1),
                Sort.Direction.fromString(sortList.get(1)), sortList.get(0));
    }

    public static Map<String,Object> mapToFilterNode(String filter) throws JsonProcessingException {
        return new ObjectMapper().readValue(filter, new TypeReference<Map<String, Object>>() {});
    }

    public static String mapToContextRange(String range, Page<?> pageResult) throws JsonProcessingException {
        Long totalElements =   pageResult.getTotalElements();
        List<Integer> rangeList = new ObjectMapper().readValue(range, new TypeReference<List<Integer>>() {});
        return String.format("vaccines %s-%s/%d", rangeList.get(0), rangeList.get(1), totalElements);
    }
}
