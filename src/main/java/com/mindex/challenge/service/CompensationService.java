package com.mindex.challenge.service;

import com.mindex.challenge.data.Compensation;

public interface CompensationService {
    Compensation createCompensation(Compensation compensation);
    Compensation readByCompensationId(String id);
    Compensation readByEmployeeId(String id);
}
