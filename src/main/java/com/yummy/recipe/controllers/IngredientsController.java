package com.yummy.recipe.controllers;

import com.yummy.recipe.models.Ingredient;
import com.yummy.recipe.models.Recipe;
import com.yummy.recipe.models.UnitOfMeasure;
import com.yummy.recipe.services.IngredientService;
import com.yummy.recipe.services.RecipeService;
import com.yummy.recipe.services.UnitOfMeasureService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;

@Slf4j
@Controller
public class IngredientsController {

    private final RecipeService recipeService;
    private final IngredientService ingredientService;
    private final UnitOfMeasureService unitOfMeasureService;

    public IngredientsController(RecipeService recipeService, IngredientService ingredientService, UnitOfMeasureService unitOfMeasureService) {
        this.recipeService = recipeService;
        this.ingredientService = ingredientService;
        this.unitOfMeasureService = unitOfMeasureService;
    }

    @GetMapping
    @RequestMapping("/recipe/{recipeId}/ingredients")
    public String ListIngredients(@PathVariable("recipeId") Long id, Model model) {

        log.debug("Getting all the ingredients of recipe no " + id);
        model.addAttribute("recipe", recipeService.getRecipeById(id));
        return "recipe/ingredient/list";
    }

    @GetMapping
    @RequestMapping("/recipe/{recipeId}/ingredient/{ingredientId}/show")
    public String ListIngredientsDetails(@PathVariable("recipeId") Long recipeId, @PathVariable("ingredientId") Long ingredientId, Model model) {

        System.out.println("Recipe id =" + recipeId);
        System.out.println("Ingredient id =" + ingredientId);
        log.debug("Getting details of ingredient no" + ingredientId);
        model.addAttribute("ingredient", ingredientService.getIngredientById(recipeId, ingredientId));
        return "recipe/ingredient/show";
    }

    // add new ingredient

    @GetMapping
    @RequestMapping("/recipe/{recipeId}/ingredient/new")
    public String newRecipeIngredient(@PathVariable("recipeId") Long recipeId, Model model) {
        // search for a recipe
        Recipe recipe = recipeService.getRecipeById(recipeId);
        // create a new ingredient
        Ingredient ingredient = new Ingredient();
        // add the recipe to the ingredient

        ingredient.setRecipe(recipe);
        ingredient.setUnitOfMeasure(new UnitOfMeasure());

        model.addAttribute("ingredient", ingredient);
        model.addAttribute("uomList", unitOfMeasureService.ListAllUnitOfMeasures());
        return "recipe/ingredient/ingredientform";
    }


    // update ingredient
    @GetMapping
    @RequestMapping("/recipe/{recipeId}/ingredient/{ingredientId}/update")
    public String updateRecipeIngredient(@PathVariable("recipeId") Long recipeId, @PathVariable("ingredientId") Long ingredientId, Model model) {
        model.addAttribute("ingredient", ingredientService.getIngredientById(recipeId, ingredientId));
        model.addAttribute("uomList", unitOfMeasureService.ListAllUnitOfMeasures());
        return "recipe/ingredient/ingredientform";
    }


    @PostMapping
    @RequestMapping("/recipe/{recipeId}/ingredient")
    public String saveOrUpdate(@PathVariable("recipeId") Long recipeId, @ModelAttribute Ingredient ingredient, Model model) {

        Ingredient savedIngredient = ingredientService.saveOrUpdate(ingredient, recipeId);

        log.debug("saved Recipe no =" + recipeId);
        log.debug("saved Ingredient no =" + savedIngredient.getId());

        return "redirect:/recipe/" + recipeId + "/ingredient/" + savedIngredient.getId() + "/show";
    }

    @GetMapping
    @RequestMapping("/recipe/{recipeId}/ingredient/{ingredientId}/delete")
    public String deleteRecipeIngredient(@PathVariable("recipeId") Long recipeId,
                                         @PathVariable("ingredientId") Long ingredientId) {
        if (recipeId == null || ingredientId == null) {
            throw new RuntimeException("Invalid recipe id");
        } else {
            log.debug("Deleteing ingredient number "+ ingredientId);
            ingredientService.deleteIngredientById(recipeId, ingredientId);
        }
        return "redirect:/recipe/" + recipeId + "/ingredients";
    }

}
