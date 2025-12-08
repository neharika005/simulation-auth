package com.dtcc.simulation.entity;

import java.util.UUID;


import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "portfolio_id")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor

public class PortfolioId {

    @Id
    private UUID portfolio_id;

}


