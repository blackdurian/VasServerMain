package com.fyp.vasclinicserver.service;

import com.fyp.vasclinicserver.repository.ClinicRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@AllArgsConstructor
@Transactional
@Service
public class ShiftBoardService {
    private final ClinicRepository clinicRepository;

}
