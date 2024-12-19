package com.backspark.socksinventory.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "socks")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Socks {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @Column(nullable = false)
    private String color;

    @NotNull
    @Min(0)
    @Max(100)
    @Column(name = "cotton_part", nullable = false)
    private Integer cottonPart;

    @NotNull
    @Min(0)
    @Column(nullable = false)
    private Integer quantity;

}

