package com.kbe.apigateway.entity;

import com.kbe.apigateway.entity.Ingredient;


import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;



public class Pizza {


    private Long id;

    private String name;

    private final static Long REQUIRED_INGREDIENT = 10101L;


    private List<Ingredient> ingredients = new LinkedList<>();

    public Pizza() {}

    public Pizza(Long id, String name) {
        this.id = id;
        this.name = name;
    }

    public Pizza(Long id, String name, List<Ingredient> ingredients) {
        this.id = id;
        this.name = name;
       // if(!containsRequiredIngredients(ingredients))
          //  throw new MissingRequiredIngredientException(
                //    "Pizza with ID " + id + " doesn´t contain Ingredient with ID " + REQUIRED_INGREDIENT);
        this.ingredients = ingredients;
    }

    @Override
    public String toString() {
        return "Pizza {" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", ingredients=" + ingredients +
                '}';
    }

    private Boolean contasinsRequiredIngredient(List<Ingredient> ingredients){
        List<Ingredient> requiredIngredients = ingredients.stream()
                .filter(ingredient -> ingredient.getId().equals(REQUIRED_INGREDIENT))
                .collect(Collectors.toList());
        return !requiredIngredients.isEmpty();
    }

    public Long getId() { return id; }

    public void setId(Long id) { this.id = id; }

    public String getName() { return name; }

    public void setName(String name) { this.name = name; }

    public List<Ingredient> getIngredients() { return ingredients; }

    public void setIngredients(List<Ingredient> ingredients) { this.ingredients = ingredients; }
}