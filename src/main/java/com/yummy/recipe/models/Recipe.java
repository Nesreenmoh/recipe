package com.yummy.recipe.models;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.URL;

import javax.persistence.*;
import javax.validation.Valid;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.HashSet;
import java.util.Set;

@Setter
@Getter
@Entity
public class Recipe {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Description should not be blank")
    @Size(min = 2, max = 255, message = "Description should be between 2 and 255 characters")
    private String description;

    @Min(value = 1, message = "PrepTime should be between 1 and 999")
    @Max(value = 999, message = "PrepTime should be between 1 and 999")
    private Integer prepTime;

    @Min(value = 1, message = "CookTime should be between 1 and 999")
    @Max(value = 999, message = "CookTime should be between 1 and 999")
    private Integer cookTime;

    @Min(value = 1, message = "servings should be between 1 and 100")
    @Max(value = 100, message = "servings should be between 1 and 100")
    private Integer servings;

    private String source;

    @URL(message = "Invalid URL")
    @NotBlank(message = "URL should not be blank")
    private String url;


    @Lob
    @NotBlank(message = "Directions should not be blank")
    private String directions;

    @Enumerated(value = EnumType.STRING)
    private Difficulty difficulty;

    @Lob
    private Byte[] image;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "recipe")
    private Set<Ingredient> ingredients = new HashSet<>();

    @OneToOne(cascade = CascadeType.ALL)
    private Notes notes;

    @ManyToMany
    @JoinTable(name = "recipe_category",
            joinColumns = @JoinColumn(name = "recipe_id"),
            inverseJoinColumns = @JoinColumn(name = "category_id"))
    private Set<Category> categories = new HashSet<>();

    public Recipe() {
    }

    public void setNotes(Notes notes) {

        this.notes = notes;
        notes.setRecipe(this);
    }

    public Recipe addIngredient(Ingredient ingredient) {
        ingredient.setRecipe(this);
        this.ingredients.add(ingredient);
        return this;
    }

}
