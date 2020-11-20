package com.yummy.recipe.services;

import com.yummy.recipe.exceptions.NotFoundException;
import com.yummy.recipe.models.Ingredient;
import com.yummy.recipe.models.Recipe;
import com.yummy.recipe.repositories.RecipeRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

@Slf4j
@Service
public class RecipeService {


    private final RecipeRepository recipeRepository;

    public RecipeService(RecipeRepository recipeRepository) {
        this.recipeRepository = recipeRepository;
    }

    // retrieve all recipes
    public Set<Recipe> getAllRecipes() {
        log.debug("I am in get all method");
        Set<Recipe> recipesSet = new HashSet<>();
        recipeRepository.findAll().iterator().forEachRemaining(recipesSet::add);
        return recipesSet;
    }

    public Recipe getRecipeById(Long id) {
        Optional<Recipe> recipe = recipeRepository.findById(id);
        if (!recipe.isPresent()) {
            throw new NotFoundException("Recipe is not found");
        }
        return recipe.get();
    }

    public Recipe saveOrUpdate(Recipe recipe){
        return recipeRepository.save(recipe);
    }

    public void deleteRecipe(Long id){
        recipeRepository.deleteById(id);
    }

}
