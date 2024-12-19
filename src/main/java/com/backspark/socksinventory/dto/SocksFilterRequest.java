package com.backspark.socksinventory.dto;

import lombok.Data;

@Data
public class SocksFilterRequest {
    private String color;
    private String operation;
    private Integer cottonPart;
    private Integer cottonPartFrom;
    private Integer cottonPartTo;
    private String sortBy;
    private String direction;
}
