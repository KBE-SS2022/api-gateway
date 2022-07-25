package com.kbe.apigateway.controller;
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
    public ResponseEntity<String> getHello(@PathVariable int helloNumber){

        return ResponseEntity.ok("hello"+helloNumber);
    }
    @RequestMapping("/hi")
    @RolesAllowed("user")
    public ResponseEntity<String> getHi() {
        System.out.println("hirequestsuccess");
        return ResponseEntity.ok("hi");
    }
    @RequestMapping("/ingredients")
    @GetMapping
    @RolesAllowed("user")
    public ResponseEntity<List<String>> ingredients() {
        System.out.println("ingredients");
        List<String> ingredientlist = new ArrayList();
        return ResponseEntity.ok(ingredientlist);
    }

    @RequestMapping("/pizzas")
    @GetMapping
    @RolesAllowed("user")
    public ResponseEntity<List<String>> getpizzas() {
        System.out.println("pizzas");
        List<String> ingredientList = new ArrayList<>();
        return ResponseEntity.ok(ingredientList);
    }
}
