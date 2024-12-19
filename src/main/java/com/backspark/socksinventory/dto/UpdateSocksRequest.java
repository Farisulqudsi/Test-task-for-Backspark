package com.backspark.socksinventory.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
@AllArgsConstructor
public class UpdateSocksRequest {
    @NotBlank
    private String color;

    @NotNull
    @Min(0)
    @Max(100)
    private Integer cottonPart;

    @NotNull
    @Min(1)
    private Integer quantity;
}
