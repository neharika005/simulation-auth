package com.dtcc.simulation.repository;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.dtcc.simulation.entity.PortfolioId;

public interface PortfolioIdRepository extends JpaRepository<PortfolioId, UUID> {}