package com.yummy.recipe.services;

import com.yummy.recipe.models.Recipe;
import com.yummy.recipe.repositories.RecipeRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

class ImageServiceTest {

    ImageService imageService;

    @Mock
    RecipeRepository recipeRepository;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
        imageService=new ImageService(recipeRepository);
    }

    @Test
    void saveImageFile() throws IOException {
        Long id=1L;
        MultipartFile multipartFile=new MockMultipartFile("imagefile",
                "testing.txt", "text/plain",
                "Spring Framework com.yummy".getBytes());
        Recipe recipe = new Recipe();
        recipe.setId(id);
        when(recipeRepository.findById(anyLong())).thenReturn(Optional.of(recipe));
        ArgumentCaptor<Recipe> argumentCaptor = ArgumentCaptor.forClass(Recipe.class);
        //when
        imageService.saveImageFile(id,multipartFile);
        //then
        verify(recipeRepository,times(1)).save(argumentCaptor.capture());
        Recipe savedRecipe = argumentCaptor.getValue();
        assertEquals(multipartFile.getBytes().length,savedRecipe.getImage().length);

    }
}