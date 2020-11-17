package com.yummy.recipe.services;

import com.yummy.recipe.models.Recipe;
import com.yummy.recipe.repositories.RecipeRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.transaction.Transactional;
import java.io.IOException;

@Slf4j
@Service
public class ImageService {

    private final RecipeRepository recipeRepository;

    public ImageService(RecipeRepository recipeRepository) {
        this.recipeRepository = recipeRepository;
    }


    @Transactional
    public void saveImageFile(Long recipeId, MultipartFile file){
        try{
            Recipe recipe = recipeRepository.findById(recipeId).get();

            Byte[] byteObjects = new Byte[file.getBytes().length];

            int i = 0;

            for (byte b : file.getBytes()){
                byteObjects[i++] = b;
            }

            recipe.setImage(byteObjects);

            recipeRepository.save(recipe);
        } catch(IOException ex){
            log.error("Error occured "+ ex);
            System.out.println(ex.getMessage());
        }

    }
}
