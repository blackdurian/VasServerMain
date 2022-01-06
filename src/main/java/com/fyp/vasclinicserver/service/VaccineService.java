package com.fyp.vasclinicserver.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fyp.vasclinicserver.mapper.PagingMapper;
import com.fyp.vasclinicserver.mapper.VaccineMapper;
import com.fyp.vasclinicserver.model.Disease;
import com.fyp.vasclinicserver.model.Vaccine;
import com.fyp.vasclinicserver.payload.VaccineRequest;
import com.fyp.vasclinicserver.payload.VaccineResponse;
import com.fyp.vasclinicserver.repository.DiseaseRepository;
import com.fyp.vasclinicserver.repository.VaccineRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@AllArgsConstructor
@Service
public class VaccineService {

    private final VaccineRepository vaccineRepository;
    private final DiseaseRepository diseaseRepository;
    private final VaccineMapper vaccineMapper;

    @Transactional(readOnly = true)
    public Page<VaccineResponse> getAllVaccine(String sort,String  range, String filter) throws JsonProcessingException {
        List<Vaccine> vaccines = vaccineRepository.findByDeletedFalse();
        List<VaccineResponse> vaccineResponses = vaccines.stream()
                .map(vaccineMapper::mapToVaccineResponse)
                .collect(Collectors.toList());
        //filter
        Map<String, Object> filterNode = PagingMapper.mapToFilterNode(filter);
        Set<String> keys = filterNode.keySet();
        if (keys.size() > 0) {
            String[] qKeys = new String[]{"q", "id","diseases"};
            // q: all text search
            if (keys.contains(qKeys[0])) {
                Object value = filterNode.get(qKeys[0]);
                if (value instanceof String && !(((String) value).trim()).isEmpty()) {
                    vaccineResponses = vaccineResponses.stream()
                            .filter(vaccine -> vaccine.getId().contains((String) value)
                                    || vaccine.getMfgCompany().contains((String) value)
                                    || vaccine.getDoseRequire().toString().contains((String) value)
                                    || vaccine.getName().contains((String) value)
                                    || vaccine.getGapDays().toString().contains((String) value)
                                    || vaccine.getDosesPerVial().toString().contains((String) value)
                                    || vaccine.getMaxStorageDays().toString().contains((String) value)
                            ).collect(Collectors.toList());
                }
            }
            // id: filter by id
            if (keys.contains(qKeys[1])) {
                Object value = filterNode.get(qKeys[1]);
                if (value instanceof String && !(((String) value).trim()).isEmpty()) {
                    vaccineResponses = vaccineResponses.stream()
                            .filter(vaccine -> vaccine.getId().contains((String) value)
                            ).collect(Collectors.toList());
                }
            }
            if (keys.contains(qKeys[2])) {
                Object value = filterNode.get(qKeys[1]);
                if (value instanceof Long) {
                    vaccineResponses = vaccineResponses.stream()
                            .filter(vaccine -> vaccine.getDiseases().contains((Long)value)
                            ).collect(Collectors.toList());
                }
            }
        }
        return PagingMapper.mapToPage(vaccineResponses,sort,range);
    }

    @Transactional(readOnly = true)
    public VaccineResponse getVaccine(String id) {
        Vaccine vaccine = vaccineRepository.getById(id);
        return vaccineMapper.mapToVaccineResponse(vaccine);
    }

    public VaccineResponse save(VaccineRequest vaccineRequest) {
        List<Disease> diseases = diseaseRepository.findAllById(vaccineRequest.getDiseases());
        Vaccine vaccine = new Vaccine();
        vaccine.setName(vaccineRequest.getName());
        vaccine.setDiseases(new HashSet<>(diseases));
        vaccine.setDoseRequire(vaccineRequest.getDoseRequire());
        vaccine.setDosesPerVial(vaccineRequest.getDosesPerVial());
        vaccine.setStorageTempUpperBound(vaccineRequest.getStorageTempUpperBound());
        vaccine.setStorageTempLowerBound(vaccineRequest.getStorageTempLowerBound());
        if (vaccineRequest.getMaxStorageDays() != null) {
            vaccine.setMaxStorageDays(vaccineRequest.getMaxStorageDays().longValue());
        }
        vaccine.setMfgCompany(vaccineRequest.getMfgCompany());
        vaccine.setGapDays(vaccineRequest.getGapDays());
        Vaccine savedVaccine = vaccineRepository.save(vaccine);
        return vaccineMapper.mapToVaccineResponse(savedVaccine);
    }
}
