package com.kbe.apigateway.dto;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class IngredientDTO {

    private Long id;
    private String name;
    private String brand;
    private String countryOrigin;
    private char nutritionScore;
    private Integer calories;
    private Integer amount;
    private Double weight;
    private Double price;

}
