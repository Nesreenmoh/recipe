package com.yummy.recipe.services;

import com.yummy.recipe.exceptions.NotFoundException;
import com.yummy.recipe.models.Recipe;
import com.yummy.recipe.repositories.RecipeRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class RecipeServiceTest {

    @InjectMocks
    RecipeService recipeService;

    @Mock
    RecipeRepository recipeRepository;

    Recipe recipe;

    @BeforeEach
    void setUp() {
        // create a mock
        MockitoAnnotations.initMocks(this);

        recipeService = new RecipeService(recipeRepository);
    }

    @Test
    void getAllRecipes() {
        Recipe recipe1= new Recipe();
        List<Recipe> myRecipes= new ArrayList<>();
        myRecipes.add(recipe1);
        when(recipeRepository.findAll()).thenReturn(myRecipes);

        Set<Recipe> recipes = recipeService.getAllRecipes();

        //Now checking the size of the recipes
        assertEquals(recipes.size(), 1);

        // check how many times the recipeRepository has been called
        verify(recipeRepository,times(1)).findAll();
    }

    @Test
    void getRecipeById() {
        Recipe recipe = new Recipe();
        recipe.setId(3L);

        when(recipeRepository.findById(anyLong())).thenReturn(Optional.of(recipe));
        Recipe returnedRecipe = recipeService.getRecipeById(3L);

        assertEquals(recipe,returnedRecipe);
        verify(recipeRepository, times(1)).findById(anyLong());
        verify(recipeRepository, never()).findAll();
    }

    @Test
    void getRecipeByIdTestNotFound() {

        // when
        when(recipeRepository.findById(anyLong())).thenReturn(Optional.empty());

        // assert that NotFound exception will be raised
        Assertions.assertThrows(NotFoundException.class, () ->{
            Recipe returnedRecipe = recipeService.getRecipeById(3L);
        });

    }



    @Test
    void deleteRecipe() {

        //given
        recipe = new Recipe();
        recipe.setId(4L);
        // when

        recipeService.deleteRecipe(4L);
        //then
        //verify(respositoryMock,times(Int)).Method inside the repository
        verify(recipeRepository, times(1)).deleteById(anyLong());
    }
}