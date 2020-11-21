package com.yummy.recipe.controllers;

import com.yummy.recipe.models.Recipe;
import com.yummy.recipe.services.ImageService;
import com.yummy.recipe.services.RecipeService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class ImageControllerTest {

    ImageController imageController;

    @Mock
    RecipeService recipeService;

    @Mock
    ImageService imageService;

    MockMvc mockMvc;

    @BeforeEach
    void setUp() {

        MockitoAnnotations.initMocks(this);
        imageController = new ImageController(imageService,recipeService);
        mockMvc= MockMvcBuilders.standaloneSetup(imageController).setControllerAdvice(new ExceptionHandlerController())
                .build();
    }

    @Test
    void getImageForm() throws Exception {
        //given


        //when
        mockMvc.perform(get("/recipe/2/image"))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("title"))
                .andExpect(view().name("recipe/imageuploadform"));
        //then
        verify(recipeService,times(1)).getRecipeById(anyLong());
    }

    @Test
    void renderImageFromDB() throws Exception {

        Recipe recipe = new Recipe();
        recipe.setId(1L);

        String s = "Hello World";
        Byte[] byteBoxing = new Byte[s.getBytes().length];

        int i=0;

        for(byte primByte : s.getBytes()){
            byteBoxing[i++]=primByte;
        }

        recipe.setImage(byteBoxing);

        //when

        when(recipeService.getRecipeById(anyLong())).thenReturn(recipe);

        //then
        MockHttpServletResponse response = mockMvc.perform(get("/recipe/1/recipeimage"))
                .andExpect(status().isOk())
                .andReturn().getResponse();

        byte[] responseByte = response.getContentAsByteArray();

        verify(recipeService,times(1)).getRecipeById(anyLong());
        assertEquals(s.getBytes().length,responseByte.length);

    }
}