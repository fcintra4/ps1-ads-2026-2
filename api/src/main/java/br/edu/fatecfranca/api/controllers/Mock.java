package br.edu.fatecfranca.api.controllers;


import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import br.edu.fatecfranca.api.contract.Contract;


@RestController
public class Mock {


   @GetMapping("/")
   public Contract<String> healthy() {
       return Contract.ok("root ok...");
   }
}

