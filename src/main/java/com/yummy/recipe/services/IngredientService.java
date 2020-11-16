package com.yummy.recipe.services;

import com.yummy.recipe.models.Ingredient;
import com.yummy.recipe.models.Recipe;
import com.yummy.recipe.repositories.IngredientRepository;
import com.yummy.recipe.repositories.RecipeRepository;
import com.yummy.recipe.repositories.UnitOfMeasureRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class IngredientService {


    private final IngredientRepository ingredientRepository;


    private final RecipeRepository recipeRepository;
    private final UnitOfMeasureRepository unitOfMeasureRepository;

    public IngredientService(IngredientRepository ingredientRepository, RecipeRepository recipeRepository, UnitOfMeasureRepository unitOfMeasureRepository) {
        this.ingredientRepository = ingredientRepository;
        this.recipeRepository = recipeRepository;
        this.unitOfMeasureRepository = unitOfMeasureRepository;
    }

    //save an ingredient

    public Ingredient saveOrUpdate(Ingredient ingredient,Long recipeId){
        // retrieve the recipe id
        Recipe recipe= recipeRepository.findById(recipeId).orElse(null);

        if(recipe==null){
            return new Ingredient();
        }

        // retrieve the ingredient by id
        Ingredient ingredient1 = recipe.getIngredients()
                .stream()
                .filter(ingredient2 -> ingredient2.getId()==ingredient.getId())
                .findFirst()
                .get();
        if(ingredient1!=null){
            ingredient1.setAmount(ingredient.getAmount());
            ingredient1.setDescription(ingredient.getDescription());
            ingredient1.setUnitOfMeasure( unitOfMeasureRepository.findById
                    (ingredient.getUnitOfMeasure().getId())
                    .orElseThrow(()-> new RuntimeException("Unit of Measure is not found")));
        }
        else{
            // add a new ingredient
            ingredient1.setRecipe(recipe);
            recipe.addIngredient(ingredient1);
        }
        ingredientRepository.save(ingredient1);
        recipeRepository.save(recipe);
        return ingredient1;
    }

    // get one an ingredient details

    public Ingredient getIngredientById( Long recipeId, Long ingredientId){
        if(recipeId==null){
            throw new RuntimeException("Invalid recipe id");
        }
        Recipe recipe = recipeRepository.findById(recipeId).orElse(null);
        if(recipe==null){
            throw new RuntimeException("No recipe was found");
        }
        Ingredient ingredient=
               recipe.getIngredients().stream()
                .filter(ingredient1 -> ingredient1.getId()==ingredientId).findFirst().get();
        if(ingredient== null){
            throw new RuntimeException("Invalid Ingredient id");
        }
        return ingredient;
    }
}
