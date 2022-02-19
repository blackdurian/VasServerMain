package com.fyp.vasclinicserver.mapper;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.data.domain.*;

import java.util.List;
import java.util.Map;

public class PagingMapper {
    public static Pageable mapToPageable(String sort, String  range) throws JsonProcessingException {
        //TODO: Complex object Sort
        //TODO: Fix pagging range
        ObjectMapper mapper = new ObjectMapper();
        List<String> sortList = mapper.readValue(sort, new TypeReference<List<String>>() {});
        List<Integer> rangeList = mapper.readValue(range, new TypeReference<List<Integer>>() {});
        if(rangeList.get(0)<rangeList.get(1)&&rangeList.get(1)>0){
            return PageRequest.of(rangeList.get(0), rangeList.get(1),
                    Sort.Direction.fromString(sortList.get(1)), sortList.get(0));
        }else {
            return PageRequest.of(0, 9, //TODO: get default paging
                    Sort.Direction.fromString(sortList.get(1)), sortList.get(0));
        }

    }

    public static Map<String,Object> mapToFilterNode(String filter) throws JsonProcessingException {
        return new ObjectMapper().readValue(filter, new TypeReference<Map<String, Object>>() {});
    }

    public static String mapToContextRange(String contextName, String range, Page<?> pageResult) throws JsonProcessingException {
        Long totalElements =   pageResult.getTotalElements();
        List<Integer> rangeList = new ObjectMapper().readValue(range, new TypeReference<List<Integer>>() {});
        return String.format("%s %s-%s/%d", contextName, rangeList.get(0), rangeList.get(1), totalElements);
    }

    public static <T> Page<T> mapToPage(List<T> list,Pageable paging){
        return new PageImpl<>(list, paging, list.size());
    }

    public static <T> Page<T> mapToPage(List<T> list, String sort, String  range) throws JsonProcessingException {
        return mapToPage(list,mapToPageable(sort,range));
    }
}
