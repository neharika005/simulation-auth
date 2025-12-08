package com.dtcc.simulation.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PortfolioCreateRequest {

    private String name;
    private Long number;
    private String address;

    
}

