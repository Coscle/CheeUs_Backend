package com.cheeus.adminReport.service;

import com.cheeus.adminReport.dto.AdminReportDto;

import java.util.List;
import java.util.Optional;

public interface AdminReportService {
    List<AdminReportDto> findAll();
    List<AdminReportDto> findAllFreeboard();
    List<AdminReportDto> findAllShortform();
    List<AdminReportDto> findAllEventboard();
    Optional<AdminReportDto> findById(int id);
    void insert(AdminReportDto board);
    void update(AdminReportDto board);
    void delete(int id);
}
