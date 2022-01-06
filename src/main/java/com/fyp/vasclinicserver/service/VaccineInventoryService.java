package com.fyp.vasclinicserver.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fyp.vasclinicserver.mapper.PagingMapper;
import com.fyp.vasclinicserver.mapper.VaccineInventoryMapper;
import com.fyp.vasclinicserver.model.Clinic;
import com.fyp.vasclinicserver.model.Vaccine;
import com.fyp.vasclinicserver.model.VaccineInventory;
import com.fyp.vasclinicserver.payload.VaccineInventoryRequest;
import com.fyp.vasclinicserver.payload.VaccineInventoryResponse;
import com.fyp.vasclinicserver.payload.VaccineResponse;
import com.fyp.vasclinicserver.repository.VaccineInventoryRepository;
import com.fyp.vasclinicserver.repository.VaccineRepository;
import com.fyp.vasclinicserver.util.TimeUtil;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@AllArgsConstructor
@Transactional
@Service
public class VaccineInventoryService {
    private final VaccineInventoryRepository vaccineInventoryRepository;
    private final VaccineRepository vaccineRepository;

    private final ClinicService clinicService;

    private final VaccineInventoryMapper vaccineInventoryMapper;

    public Page<VaccineInventoryResponse> getClinicVaccineInventory(String sort, String range, String filter) throws JsonProcessingException {
        Clinic clinic = clinicService.getCurrentClinic();
        List<VaccineInventory> inventories = vaccineInventoryRepository.findByClinic(clinic);
        List<VaccineInventoryResponse> responses = inventories.stream()
                .map(vaccineInventoryMapper::mapToVaccineInventoryResponse)
                .collect(Collectors.toList());
        //filter
        Map<String, Object> filterNode = PagingMapper.mapToFilterNode(filter);
        Set<String> keys = filterNode.keySet();
        if (keys.size() > 0) {
            String[] qKeys = new String[]{"q", "id","vaccines"};
            // q: all text search
            if (keys.contains(qKeys[0])) {
                Object value = filterNode.get(qKeys[0]);
                if (value instanceof String && !(((String) value).trim()).isEmpty()) {
                    responses = responses.stream()
                            .filter(inventoryResponse -> inventoryResponse.getId().contains((String) value)
                                    || inventoryResponse.getExpiryDate().contains((String) value)
                                    || inventoryResponse.getMfgDate().contains((String) value)
                                    || inventoryResponse.getStock().toString().contains((String) value)
                                    || inventoryResponse.getUnitPrice().toString().contains((String) value)
                            ).collect(Collectors.toList());
                }
            }
            // id: filter by id
            if (keys.contains(qKeys[1])) {
                Object value = filterNode.get(qKeys[1]);
                if (value instanceof String && !(((String) value).trim()).isEmpty()) {
                    responses = responses.stream()
                            .filter(inventoryResponse -> inventoryResponse.getId().contains((String) value)
                            ).collect(Collectors.toList());
                }
            }
            if (keys.contains(qKeys[2])) {
                Object value = filterNode.get(qKeys[1]);
                if (value instanceof String && !(((String) value).trim()).isEmpty())  {
                    responses = responses.stream()
                            .filter(inventoryResponse -> inventoryResponse.getVaccineId().contains((String)value)
                            ).collect(Collectors.toList());
                }
            }
        }
        return PagingMapper.mapToPage(responses,sort,range);
    }

    public VaccineInventoryResponse save(VaccineInventoryRequest vaccineInventoryRequest) {
        Clinic clinic = clinicService.getCurrentClinic();
        Vaccine vaccine = vaccineRepository.getById(vaccineInventoryRequest.getVaccineId());
        VaccineInventory vaccineInventory = new VaccineInventory();
        vaccineInventory.setVaccine(vaccine);
        vaccineInventory.setClinic(clinic);
        vaccineInventory.setMfgDate(TimeUtil.convertStringDateToInstant(vaccineInventoryRequest.getMfgDate(), TimeUtil.MFG_DATE_FORMAT));
        vaccineInventory.setExpiryDate(TimeUtil.convertStringDateToInstant(vaccineInventoryRequest.getExpiryDate(), TimeUtil.EXPIRY_DATE_FORMAT));
        vaccineInventory.setStock(vaccineInventoryRequest.getStock());
        vaccineInventory.setUnitPrice(vaccineInventoryRequest.getUnitPrice());
        VaccineInventory savedVaccineInventory = vaccineInventoryRepository.save(vaccineInventory);
        return vaccineInventoryMapper.mapToVaccineInventoryResponse(savedVaccineInventory);
    }

}
