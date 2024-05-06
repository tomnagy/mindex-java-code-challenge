package com.mindex.challenge.service.impl;

import com.mindex.challenge.dao.CompensationRepository;
import com.mindex.challenge.data.Compensation;
import com.mindex.challenge.service.CompensationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.UUID;

@Service
public class CompensationServiceImpl implements CompensationService {

    private static final Logger LOG = LoggerFactory.getLogger(CompensationServiceImpl.class);

    @Autowired
    private CompensationRepository compensationRepository;

    @Override
    public Compensation createCompensation(Compensation compensation) {
        LOG.debug("Creating compensation record [{}]", compensation);

        compensation.setCompensationId(UUID.randomUUID().toString());
        compensationRepository.insert(compensation);

        return compensation;
    }

    @Override
    public Compensation readByCompensationId(String id) {
        LOG.debug("Searching for compensation record with compensationId [{}]", id);

        Compensation compensation = compensationRepository.findByCompensationId(id);

        if (compensation == null) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, "Compensation record not found with compensationId: {} " + id
            );
        }

        return compensation;
    }

    public Compensation readByEmployeeId(String id) {
        LOG.debug("Searching for compensation record with employeeId [{}]", id);

        Compensation compensation = compensationRepository.findByEmployeeId(id);

        if (compensation == null) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, "Compensation record not found with employeeId: {} " + id
            );
        }

        return compensation;
    }
}
