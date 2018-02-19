package com.controller;

import com.model.Hero;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


@Controller
@CrossOrigin
@RequestMapping("heroes")
public class HeroController {
    @RequestMapping(value = "/test", method = RequestMethod.GET)
    public @ResponseBody ResponseEntity<?> getHeroes(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        Hero h1 = new Hero(1L, "SASHA");
        Hero h2 = new Hero(2L, "MISHA");

        List<Hero> list = new ArrayList<>();
        list.add(h1);
        list.add(h2);

        return new ResponseEntity<>(list, HttpStatus.OK);

    }

    @RequestMapping(value = "/mnb", method = RequestMethod.POST)
    public @ResponseBody ResponseEntity<?> registration(@RequestBody Hero hero) {


        return new ResponseEntity<>(hero, HttpStatus.OK);
    }
}
