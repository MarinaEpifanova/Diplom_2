package ru.yandex.practicum.model;

import java.util.ArrayList;
import java.util.List;


public class Order {

    public List<Ingredient> ingredients = new ArrayList<>();
    public String name;
    public int number;

    public Order(List<Ingredient> ingredients) {
        this.ingredients = ingredients;
    }

    public void addIngredient(Ingredient ingredient) {
        ingredients.add(ingredient);
    }

    public void removeIngredient(int index) {
        ingredients.remove(index);
    }

    public void moveIngredient(int index, int newIndex) {
        ingredients.add(newIndex, ingredients.remove(index));
    }

    public List<Ingredient> getIngredients() {
        return ingredients;
    }

    public List<String> getIngredientsIds() {
        ArrayList<String> result = new ArrayList<String>();
        for (Ingredient ingredient : ingredients){
            result.add(ingredient.get_id());
        }
        return result;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    @Override
    public String toString() {
        return "Order{" +
                "ingredients=" + ingredients +
                ", name='" + name + '\'' +
                ", number=" + number +
                '}';
    }
}