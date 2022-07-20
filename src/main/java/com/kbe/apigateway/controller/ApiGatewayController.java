package com.kbe.apigateway.controller;
import com.kbe.apigateway.entity.Ingredient;
import com.kbe.apigateway.entity.Pizza;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.annotation.security.RolesAllowed;
import java.util.ArrayList;
import java.util.List;


@SpringBootApplication
@RestController
@CrossOrigin
@RequestMapping("/hello")
public class ApiGatewayController {
    @RequestMapping("/{helloNumber}")
    @RolesAllowed("user")
    public ResponseEntity<String> gethello(@PathVariable int helloNumber){

        return ResponseEntity.ok("hello"+helloNumber);
    }
    @RequestMapping("/hi")
    @RolesAllowed("user")
    public ResponseEntity<String> gethi() {
        System.out.println("hirequestsuccess");
        return ResponseEntity.ok("hi");
    }
    @RequestMapping("/ingredients")
    @GetMapping
    @RolesAllowed("user")
    public ResponseEntity<List<Ingredient>> ingredients() {
        System.out.println("ingredients");
        Ingredient ingredient=new Ingredient(10L,"tomate","jaa","Deutschland",'b',100,1,150000.100,20.000);
        Ingredient ingredient2=new Ingredient(10L,"Mozarella","jaa","Deutschland",'b',100,1,150.0,10.25);
        Ingredient ingredient3=new Ingredient(10L,"Thunfisch","jaa","Deutschland",'b',100,1,150.0,10.0);
        List<Ingredient>   ingredientlist= new ArrayList();
        ingredientlist.add(ingredient);
        ingredientlist.add(ingredient2);
        ingredientlist.add(ingredient3);
        return ResponseEntity.ok(ingredientlist);






    }@RequestMapping("/pizzas")
    @GetMapping
    @RolesAllowed("user")
    public ResponseEntity<List<Ingredient>> getpizzas() {
        System.out.println("pizzas");

        Ingredient ingredient = new Ingredient(10L, "tomate", "jaa", "Deutschland", 'b', 100, 1, 150.0, 10.0);
        Ingredient ingredient2 = new Ingredient(10L, "Mozarella", "jaa", "Deutschland", 'b', 100, 1, 150.0, 10.0);
        Ingredient ingredient3 = new Ingredient(10L, "Thunfisch", "jaa", "Deutschland", 'b', 100, 1, 150.0, 10.0);
        List<Ingredient> ingredientlist = new ArrayList();
        ingredientlist.add(ingredient);
        ingredientlist.add(ingredient2);
        ingredientlist.add(ingredient3);
        Pizza pizza=new Pizza(10L,"Tuna",ingredientlist);
        Ingredient ingredient4 = new Ingredient(10L, "tomate", "jaa", "Deutschland", 'b', 100, 1, 150.0, 10.0);
        Ingredient ingredient5 = new Ingredient(10L, "Mozarella", "jaa", "Deutschland", 'b', 100, 1, 150.0, 10.0);
        Ingredient ingredient6 = new Ingredient(10L, "Thunfisch", "jaa", "Deutschland", 'b', 100, 1, 150.0, 10.0);
        List<Ingredient> ingredientlist2 = new ArrayList();
        ingredientlist2.add(ingredient4);
        ingredientlist2.add(ingredient5);
        ingredientlist2.add(ingredient6);
        return ResponseEntity.ok(ingredientlist);


    }
}
