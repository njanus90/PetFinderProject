package com.example.petfinderproject;
//We don't actually use this right now.
// We just don't associate particular users having perticular pets.
public class Pet {

    String type, name, color, breed, age;

    public Pet(String type, String name, String color, String breed, String age){
        this.type = type;
        this.name = name;
        this.color = color;
        this.breed = breed;
        this.age = age;
    }
}
