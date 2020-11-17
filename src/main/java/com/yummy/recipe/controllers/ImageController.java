package com.yummy.recipe.controllers;

import com.yummy.recipe.models.Recipe;
import com.yummy.recipe.services.ImageService;
import com.yummy.recipe.services.RecipeService;
import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;


@Controller
public class ImageController {

    private  final ImageService imageService;
    private final RecipeService recipeService;

    public ImageController(ImageService imageService, RecipeService recipeService) {
        this.imageService = imageService;
        this.recipeService = recipeService;
    }

    @GetMapping("/recipe/{recipeId}/image")
    public String getImageForm(@PathVariable("recipeId") Long recipeId, Model model) {
        model.addAttribute("recipe", recipeService.getRecipeById(recipeId));
        model.addAttribute("title", "Add image");
        return "recipe/imageuploadform";
    }

    @PostMapping("/recipe/{recipeId}/image")
    public String saveImageForm(@PathVariable("recipeId") Long recipeId, @RequestParam("imagefile") MultipartFile imagefile, Model model) {
        imageService.saveImageFile(recipeId, imagefile);
        return "redirect:/recipe/" + recipeId + "/show";
    }

    // get the image from the database using HttpServletResponse
    @GetMapping("/recipe/{recipeId}/recipeimage")
    public void renderImageFromDB(@PathVariable("recipeId") Long recipeId, HttpServletResponse response) throws IOException {
        Recipe recipe = recipeService.getRecipeById(recipeId);
        if(recipe.getImage()!=null){
            byte[] byteArray = new byte[recipe.getImage().length];
            int i = 0;

            for (Byte wrappedByte : recipe.getImage()){
                byteArray[i++] = wrappedByte; //auto unboxing
            }
            response.setContentType("images/jpeg");
            InputStream is = new ByteArrayInputStream(byteArray);
            IOUtils.copy(is, response.getOutputStream());
        }
    }
}
