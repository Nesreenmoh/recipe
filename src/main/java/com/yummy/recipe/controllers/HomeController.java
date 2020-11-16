package com.yummy.recipe.controllers;

import com.yummy.recipe.services.RecipeService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

@Slf4j
@Controller
public class HomeController {


    private final RecipeService recipeService;

    public HomeController(RecipeService recipeService) {
        this.recipeService = recipeService;
    }

    @RequestMapping({"","/","/index","/index.html"})
    public String homePage(Model model)
    {
        log.debug("Getting Index Page");
        model.addAttribute("title", "Recipes");
        model.addAttribute("recipes",recipeService.getAllRecipes() );
      return "index";
    }

}
