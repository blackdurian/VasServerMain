package com.fyp.vasclinicserver.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fyp.vasclinicserver.mapper.PagingMapper;
import com.fyp.vasclinicserver.mapper.VaccineOrderMapper;
import com.fyp.vasclinicserver.model.Clinic;
import com.fyp.vasclinicserver.model.Vaccine;
import com.fyp.vasclinicserver.model.VaccineOrder;
import com.fyp.vasclinicserver.model.enums.VaccineOrderStatus;
import com.fyp.vasclinicserver.payload.VaccineOrderRequest;
import com.fyp.vasclinicserver.payload.VaccineOrderResponse;
import com.fyp.vasclinicserver.repository.VaccineOrderRepository;
import com.fyp.vasclinicserver.repository.VaccineRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Slf4j
@AllArgsConstructor
@Transactional
@Service
public class VaccineOrderService {
    private final VaccineOrderRepository vaccineOrderRepository;
    private final VaccineOrderMapper vaccineOrderMapper;
    private final VaccineRepository vaccineRepository;

    private final ClinicService clinicService;

    public Page<VaccineOrderResponse> getAllVaccineOrders(String sort, String range, String filter) throws JsonProcessingException {
        Map<String, Object> filterNode =  PagingMapper.mapToFilterNode(filter);
        Optional<String> firstKey = filterNode.keySet().stream().findFirst();
        List<VaccineOrderResponse> vaccineOrders  = vaccineOrderRepository.findAll().stream()
                .map(vaccineOrderMapper::mapToVaccineOrderResponse)
                .collect(Collectors.toList());

        if (firstKey.isPresent()) {
            String key = firstKey.get();
            Object value = filterNode.get(key);
            // q: all text search
            if(key.equals("q")&& value instanceof String){
                vaccineOrders = vaccineOrders.stream().filter(v -> v.getVaccineId().contains((String) value)
                        || v.getUuid().contains((String) value)
                        || v.getClinicId().contains((String) value)
                        || v.getStatus().contains((String) value)
                ).collect(Collectors.toList());
            }
        }
        return PagingMapper.mapToPage(vaccineOrders,sort,range);
    }


    public VaccineOrderResponse save(VaccineOrderRequest vaccineOrderRequest) {
         Clinic clinic = clinicService.getCurrentClinic();
        Vaccine vaccine = vaccineRepository.getById(vaccineOrderRequest.getVaccineId());
         VaccineOrder vaccineOrder = new VaccineOrder();
         vaccineOrder.setClinic(clinic);
         vaccineOrder.setVaccine(vaccine);
         vaccineOrder.setUnit(vaccineOrderRequest.getUnit());
         vaccineOrder.setStatus(VaccineOrderStatus.ORDERED);
         vaccineOrder.setUuid(UUID.randomUUID().toString());
         VaccineOrder savedVaccineOrder = vaccineOrderRepository.save(vaccineOrder);
         return vaccineOrderMapper.mapToVaccineOrderResponse(savedVaccineOrder);
    }

    public Page<VaccineOrderResponse> getCurrentClinicVaccineOrders(String sort, String range, String filter) throws JsonProcessingException {
        Clinic clinic = clinicService.getCurrentClinic();
        Map<String, Object> filterNode =  PagingMapper.mapToFilterNode(filter);
        Optional<String> firstKey = filterNode.keySet().stream().findFirst();
        List<VaccineOrderResponse> vaccineOrders  = vaccineOrderRepository.findByClinic(clinic).stream()
                .map(vaccineOrderMapper::mapToVaccineOrderResponse)
                .collect(Collectors.toList());
        if (firstKey.isPresent()) {
            String key = firstKey.get();
            Object value = filterNode.get(key);
            // q: all text search
            if(key.equals("q")&& value instanceof String){
                vaccineOrders = vaccineOrders.stream().filter(v -> v.getVaccineId().contains((String) value)
                        || v.getUuid().contains((String) value)
                        || v.getStatus().contains((String) value)
                ).collect(Collectors.toList());
            }
        }
        return PagingMapper.mapToPage(vaccineOrders,sort,range);
    }

}
