package com.example.petfinderproject;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class User {
    String name, bio;
    ArrayList<PetPost> posts;
    Array pets;

    public User(String name) {
        this.name = name;
    }



    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
