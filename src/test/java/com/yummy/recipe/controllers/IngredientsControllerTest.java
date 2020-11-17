package com.yummy.recipe.controllers;

import com.yummy.recipe.models.Ingredient;
import com.yummy.recipe.models.Recipe;
import com.yummy.recipe.services.IngredientService;
import com.yummy.recipe.services.RecipeService;
import com.yummy.recipe.services.UnitOfMeasureService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.assertj.ApplicationContextAssert;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.ui.Model;

import java.util.HashSet;

import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class IngredientsControllerTest {

    @InjectMocks
    IngredientsController ingredientsController;

    @Mock
    RecipeService recipeService;

    @Mock
    IngredientService ingredientService;

    @Mock
    UnitOfMeasureService unitOfMeasureService;

    Recipe recipe;

    MockMvc mockMvc;

    @Mock
    Model model;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(ingredientsController).build();
    }

    @Test
    void listIngredients() throws Exception {
        recipe = new Recipe();
        recipe.setId(1L);
        when(recipeService.getRecipeById(anyLong())).thenReturn(recipe);

        mockMvc.perform(get("/recipe/1/ingredients"))
                .andExpect(status().isOk())
                .andExpect(view().name("recipe/ingredient/list"))
                .andExpect(model().attributeExists("recipe"));
        verify(recipeService, times(1)).getRecipeById(anyLong());
    }

    @Test
    void listIngredientsDetails() throws Exception {
        Ingredient ingredient = new Ingredient();
        //when
        when(ingredientService.getIngredientById(1L, 2L)).thenReturn(ingredient);
        mockMvc.perform(get("/recipe/1/ingredient/2/show"))
                .andExpect(status().isOk())
                .andExpect(view().name("recipe/ingredient/show"))
                .andExpect(model().attributeExists("ingredient"));

        verify(ingredientService, times(1)).getIngredientById(anyLong(), anyLong());

    }

    @Test
    void updateRecipeIngredient() throws Exception {
        //given
        Ingredient ingredient = new Ingredient();
        //when
        when(ingredientService.getIngredientById(1L, 2L)).thenReturn(ingredient);
        when(unitOfMeasureService.ListAllUnitOfMeasures()).thenReturn(new HashSet<>());

        mockMvc.perform(get("/recipe/1/ingredient/2/update"))
                .andExpect(model().attributeExists("ingredient"))
                .andExpect(model().attributeExists("uomList"))
                .andExpect(status().isOk())
                .andExpect(view().name("recipe/ingredient/ingredientform"));


    }

    @Test
    void saveOrUpdate() throws Exception {
        //given
        Ingredient ingredient = new Ingredient();
        ingredient.setId(2L);
        Recipe recipe = new Recipe();
        recipe.setId(1L);
        ingredient.setRecipe(recipe);
        //when
        when(ingredientService.saveOrUpdate(anyObject(),anyLong())).thenReturn(ingredient);
        //then
        mockMvc.perform(post("/recipe/1/ingredient")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .param("id", "")
                .param("description", "some string"))
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/recipe/1/ingredient/2/show"));
    }

    @Test
    void newRecipeIngredient() throws Exception {
        Recipe recipe = new Recipe();
        recipe.setId(2L);

        when(recipeService.getRecipeById(anyLong())).thenReturn(recipe);
        when(unitOfMeasureService.ListAllUnitOfMeasures()).thenReturn(new HashSet<>());

        mockMvc.perform(get("/recipe/2/ingredient/new"))
                .andExpect(status().isOk())
                .andExpect(view().name("recipe/ingredient/ingredientform"))
                .andExpect(model().attributeExists("uomList"))
                .andExpect(model().attributeExists("ingredient"));


       verify(recipeService, times(1)).getRecipeById(anyLong());

    }
}