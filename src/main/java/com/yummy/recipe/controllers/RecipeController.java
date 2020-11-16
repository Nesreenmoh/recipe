package com.yummy.recipe.controllers;


import com.yummy.recipe.models.Recipe;
import com.yummy.recipe.services.RecipeService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

import java.util.function.LongToDoubleFunction;

@Slf4j
@Controller
public class RecipeController {


    private final static String RECIPE_RECIPEFORM_URL="/recipe/recipeForm";
    private final RecipeService recipeService;

    public RecipeController(RecipeService recipeService) {
        this.recipeService = recipeService;
    }


    @RequestMapping({"/recipe/{id}/show",})
    public String showById(@PathVariable("id") Long id, Model model)
    {
        log.debug("Getting a recipe details Page");
        model.addAttribute("title", "Recipe Details");
        model.addAttribute("recipe",recipeService.getRecipeById(id) );
        return "recipe/show";
    }

    @GetMapping
    @RequestMapping("/recipe/new")
    public String newRecipe(Model model){
        model.addAttribute("recipe", new Recipe());
        return "recipe/recipeform";
    }

    @GetMapping
    @RequestMapping("/recipe/{id}/update")
    public String newRecipe(@PathVariable("id") Long id,  Model model){
        model.addAttribute("title","Update Recipe");
        model.addAttribute("recipe", recipeService.getRecipeById(id));
        return "/recipe/recipeForm";
    }

    @PostMapping
    @RequestMapping("recipe")
    public String saveOrUpdate(@ModelAttribute Recipe recipe, Model model, Errors errors){
        recipeService.saveOrUpdate(recipe);
        return "redirect:/recipe/"+ recipe.getId() + "/show";

    }

    @PostMapping
    @RequestMapping("recipe/{id}/delete")
    public String deleteRecipeById(@PathVariable("id") Long id, Model model){
        if(id==null){
            throw new RuntimeException("invalid Id ");
        }
        else
        {
            log.debug("Delete Recipe ="+ id);
            recipeService.deleteRecipe(id);
        }
        return "redirect:/index";
    }


}
