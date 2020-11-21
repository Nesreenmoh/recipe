package com.yummy.recipe.controllers;

import com.yummy.recipe.exceptions.NotFoundException;
import com.yummy.recipe.models.Recipe;
import com.yummy.recipe.services.ImageService;
import com.yummy.recipe.services.RecipeService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.ui.Model;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class RecipeControllerTest {

    RecipeController recipeController;

    @Mock
    RecipeService recipeService;

    @Mock
    ImageService imageService;

    @Mock
    Model model;

    Recipe recipe;

    MockMvc mockMvc;

    @BeforeEach
    void setUp() {

        MockitoAnnotations.initMocks(this);
        recipeController = new RecipeController(recipeService);
        mockMvc = MockMvcBuilders.standaloneSetup(recipeController).build();
    }

    @Test
    void showByIdTest() throws Exception {
        Recipe recipe = new Recipe();
        recipe.setId(3L);


        when(recipeService.getRecipeById(anyLong())).thenReturn(recipe);
        mockMvc.perform(get("/recipe/" + 3L + "/show"))
                .andExpect(view().name("recipe/show"))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("recipe"));
    }

    @Test
    void deleteRecipeById() throws Exception {
        //given
        Recipe recipe = new Recipe();
        recipe.setId(3L);

        //when
        mockMvc.perform(get("/recipe/" + 3L + "/delete"))
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/index"));
        //then
        verify(recipeService, times(1)).deleteRecipe(anyLong());

    }

    @Test
    void newRecipeTest() throws Exception {
        Recipe recipe1 = new Recipe();
        recipe1.setId(4L);
        recipe1.setCookTime(50);

        when(recipeService.getRecipeById(anyLong())).thenReturn(recipe1);

        mockMvc.perform(get("/recipe/new"))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("recipe"))
                .andExpect(view().name("recipe/recipeform"));

        mockMvc.perform(get("/recipe/" + 4L + "/update"))
                .andExpect(model().attributeExists("title"))
                .andExpect(status().isOk())
                .andExpect(view().name("/recipe/recipeForm"))
                .andExpect(model().attributeExists("recipe"));

    }

    @Test
    void getRecipeByIdTestResponseStatusNotFound() throws Exception {

        when(recipeService.getRecipeById(anyLong())).thenThrow(NotFoundException.class);

        mockMvc.perform(get("/recipe/1/show"))
                .andExpect(status().isNotFound());

    }

    @Test
    void handelNotFound() throws Exception {
        when(recipeService.getRecipeById(anyLong())).thenThrow(NotFoundException.class);

        mockMvc.perform(get("/recipe/1/show"))
                .andExpect(status().isNotFound())
                .andExpect(view().name("404error"));
    }
}