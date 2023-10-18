package com.vocab.vocabulary_management.entities;

public class Category {
    private int categoryId;
    private String name;
    private String language;

    public Category(int categoryId, String name, String language) {
        this.categoryId = categoryId;
        this.name = name;
        this.language = language;
    }

    public int getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(int categoryId) {
        this.categoryId = categoryId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }
}
