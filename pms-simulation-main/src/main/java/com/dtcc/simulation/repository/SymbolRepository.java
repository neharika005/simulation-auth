package com.dtcc.simulation.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.dtcc.simulation.entity.Symbol;

public interface SymbolRepository extends JpaRepository<Symbol, String> {}
