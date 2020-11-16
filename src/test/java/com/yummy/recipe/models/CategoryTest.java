package com.yummy.recipe.models;

import org.aspectj.lang.annotation.Before;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CategoryTest {

    Category category;

    @BeforeEach
    public void setUp(){
     category=new Category();
    }
    @Test
    void getId() {
        category.setId(5L);
        assertEquals(5L, category.getId());
    }

    @Test
    void getDescription() {
        category.setDescription("");
    }

    @Test
    void getRecipes() {
    }
}