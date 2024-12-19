package com.backspark.socksinventory.dto;

import lombok.Data;

@Data
public class SocksResponse {
    private Long id;
    private String color;
    private Integer cottonPart;
    private Integer quantity;
}
