package com.yummy.recipe.controllers;

import com.yummy.recipe.models.Recipe;
import com.yummy.recipe.services.RecipeService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.ui.Model;

import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class HomeControllerTest {


    HomeController homeController;

    @Mock
    RecipeService recipeService;

    @Mock
    Model model;


    @BeforeEach
    void setUp() {

        MockitoAnnotations.initMocks(this);
        homeController = new HomeController(recipeService);
    }

    // test MVC syntax
    @Test
    void testMockMVC() throws Exception {

        MockMvc mockMvc =MockMvcBuilders.standaloneSetup(homeController).build();
        // test a mock mvc
        mockMvc.perform(get("/"))
                .andExpect(status().isOk())
                .andExpect(view().name("index"));
    }

    @Test
    void homePage() {
        //given
        Set<Recipe> recipes = new HashSet<>();
        Recipe recipe1 = new Recipe();
        Recipe recipe2 = new Recipe();
        recipe1.setId(3L);
        recipe2.setId(5L);
        recipes.add(recipe1);
        recipes.add(recipe2);
        when(recipeService.getAllRecipes()).thenReturn(recipes);
        // define capture
        ArgumentCaptor<Set<Recipe>> argumentCaptor = ArgumentCaptor.forClass(Set.class);

        //when

        //then

        assertEquals("index", homeController.homePage(model));
        verify(recipeService, times(1)).getAllRecipes();
        // using a matcher with attribute "eq"
        verify(model, times(1)).addAttribute(eq("recipes"), argumentCaptor.capture());
        Set<Recipe> argumentCapture = argumentCaptor.getValue();
        assertEquals(2, argumentCapture.size());

    }


}