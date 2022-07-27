package com.kbe.apigateway.dto;

import lombok.*;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CompletePizzaDTO {

    private Long id;
    private String name;
    private Double price;
    private List<IngredientDTO> ingredients;
}
