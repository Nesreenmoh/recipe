package com.yummy.recipe.controllers;


import com.yummy.recipe.exceptions.NotFoundException;
import com.yummy.recipe.models.Recipe;
import com.yummy.recipe.services.ImageService;
import com.yummy.recipe.services.RecipeService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

@Slf4j
@Controller
public class RecipeController {


    private final static String RECIPE_RECIPEFORM_URL = "/recipe/recipeForm";
    private final RecipeService recipeService;

    @Autowired
    ImageService imageService;

    public RecipeController(RecipeService recipeService) {
        this.recipeService = recipeService;
    }

    @GetMapping("/recipe/{recipeId}/show")
    public String showById(@PathVariable("recipeId") Long recipeId, Model model) {
        log.debug("Getting a recipe details Page");
        model.addAttribute("title", "Recipe Details");
        model.addAttribute("recipe", recipeService.getRecipeById(recipeId));
        return "recipe/show";
    }

    @GetMapping("/recipe/new")
    public String newRecipe(Model model) {
        model.addAttribute("recipe", new Recipe());
        return "recipe/recipeform";
    }

    @GetMapping("/recipe/{id}/update")
    public String newRecipe(@PathVariable("id") Long id, Model model) {
        model.addAttribute("title", "Update Recipe");
        model.addAttribute("recipe", recipeService.getRecipeById(id));
        return "recipe/recipeform";
    }

    /*
    Errors should be placed after you define the @Valid @ModelAttribute("recipe") Recipe recipe
    so Spring will handle any errors from the template
     */

    @PostMapping("/recipe/new")
    public String saveOrUpdate(@Valid @ModelAttribute("recipe") Recipe recipe, Errors errors,Model model ) {
        if(errors.hasErrors()){
            return "recipe/recipeform";
        }
        recipeService.saveOrUpdate(recipe);
        return "redirect:/recipe/" + recipe.getId() + "/show";

    }

    @PostMapping("recipe/{id}/delete")
    public String deleteRecipeById(@PathVariable("id") Long id, Model model) {
        if (id == null) {
            throw new RuntimeException("invalid Id ");
        } else {
            log.debug("Delete Recipe =" + id);
            recipeService.deleteRecipe(id);
        }
        return "redirect:/index";
    }

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(NotFoundException.class)
    public ModelAndView handelNotFound(Exception exception){
        log.error("Handeling not found exception");
        log.error(exception.getMessage());
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("404error");
        modelAndView.addObject("exception", exception);
        return modelAndView;
    }
}
