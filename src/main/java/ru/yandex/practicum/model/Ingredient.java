package ru.yandex.practicum.model;


public class Ingredient {
    private String _id;
    private String name;

    public Ingredient(String _id, String name) {
        this._id = _id;
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    @Override
    public String toString() {
        return "Ingredient{" +
                "_id='" + _id + '\'' +
                ", name='" + name + '\'' +
                '}';
    }
}