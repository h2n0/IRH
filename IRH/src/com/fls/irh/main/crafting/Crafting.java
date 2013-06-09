package com.fls.irh.main.crafting;

import java.util.ArrayList;
import java.util.List;

import com.fls.irh.main.entity.Chest;
import com.fls.irh.main.entity.Oven;
import com.fls.irh.main.item.resource.Resource;

public class Crafting
{
    public static final List<Recipe> anvilRecipes = new ArrayList<Recipe>();
    public static final List<Recipe> ovenRecipes = new ArrayList<Recipe>();
    public static final List<Recipe> furnaceRecipes = new ArrayList<Recipe>();
    public static final List<Recipe> workbenchRecipes = new ArrayList<Recipe>();

    static
    {
        try
        {
            workbenchRecipes.add(new FurnitureRecipe(Oven.class).addCost(Resource.stone, 15));

            workbenchRecipes.add(new FurnitureRecipe(Chest.class).addCost(Resource.wood, 10));
        } catch (Exception e)
        {
            throw new RuntimeException(e);
        }
    }
}