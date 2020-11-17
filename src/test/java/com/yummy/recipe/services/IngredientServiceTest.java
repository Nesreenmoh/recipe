package com.yummy.recipe.services;

import com.yummy.recipe.models.Ingredient;
import com.yummy.recipe.models.Recipe;
import com.yummy.recipe.models.UnitOfMeasure;
import com.yummy.recipe.repositories.IngredientRepository;
import com.yummy.recipe.repositories.RecipeRepository;
import com.yummy.recipe.repositories.UnitOfMeasureRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

class IngredientServiceTest {


    IngredientService ingredientService;

    @Mock
    IngredientRepository ingredientRepository;

    @Mock
    RecipeRepository recipeRepository;

    @Mock
    UnitOfMeasureRepository unitOfMeasureRepository;


    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
        ingredientService = new IngredientService
                (ingredientRepository, recipeRepository, unitOfMeasureRepository);
    }

    @Test
    void getIngredientById() {
        Recipe recipe = new Recipe();
        recipe.setId(1L);
        Ingredient ingredient1 = new Ingredient();
        ingredient1.setId(2L);
        Ingredient ingredient2 = new Ingredient();
        ingredient2.setId(3L);
        Set<Ingredient> ingredients = new HashSet<>();
        ingredients.add(ingredient1);
        ingredients.add(ingredient2);

        recipe.setIngredients(ingredients);

        // here I test the mock
        when(recipeRepository.findById(1L)).thenReturn(Optional.of(recipe));

        Ingredient returnedIngredient = ingredientService.getIngredientById(1L, 2L);

        assertEquals(ingredient1, returnedIngredient);
        assertNotNull(returnedIngredient);
    }

    @Test
    void saveOrUpdate() {
        //given
        UnitOfMeasure unitOfMeasure1 = new UnitOfMeasure();
        unitOfMeasure1.setId(1L);
        Ingredient ingredient1 = new Ingredient();
        ingredient1.setId(2L);
        ingredient1.setUnitOfMeasure(unitOfMeasure1);
        Recipe recipe = new Recipe();
        recipe.setId(3L);
        recipe.addIngredient(ingredient1);
        ingredient1.setRecipe(recipe);


        //when
        when(unitOfMeasureRepository.findById(anyLong())).thenReturn(Optional.of(unitOfMeasure1));
        when(recipeRepository.findById(3L)).thenReturn(Optional.of(recipe));
        when(recipeRepository.save(any())).thenReturn(recipe);


        Ingredient savedIngredient = ingredientService.saveOrUpdate(ingredient1, 3L);
        System.out.println("Saved Ingredient " + savedIngredient.getId());

        //then
        assertEquals(ingredient1.getId(), savedIngredient.getId());
    }

    @Test
    void deleteIngredientById() {
        Recipe recipe = new Recipe();
        recipe.setId(1L);
        Ingredient ingredient = new Ingredient();
        ingredient.setId(2L);
        recipe.addIngredient(ingredient);
        ingredient.setRecipe(recipe);

        //when
        when(recipeRepository.findById(anyLong())).thenReturn(Optional.of(recipe));
        ingredientService.deleteIngredientById(1L, 2L);

        //then
        verify(recipeRepository, times(1)).findById(anyLong());
        verify(recipeRepository, times(1)).save(any(Recipe.class));
        verify(ingredientRepository, times(1)).deleteById(anyLong());

    }
}