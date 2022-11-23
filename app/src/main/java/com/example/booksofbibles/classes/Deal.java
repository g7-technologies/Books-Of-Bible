package com.example.booksofbibles.classes;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Deal implements Serializable {
    int id;
    String name;
    private List<Token> itemList = new ArrayList<Token>();

    public Deal() {
    }

    public List<Token> getItemList() {
        return itemList;
    }

    public void setItemList(List<Token> itemList) {
        this.itemList = itemList;
    }

    public Deal(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
