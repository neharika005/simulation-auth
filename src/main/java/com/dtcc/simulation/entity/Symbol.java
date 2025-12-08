package com.dtcc.simulation.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "symbol")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Symbol {

    @Id
    @Column(name = "symbol")
    private String symbol;

}

