package com.example.petfinderproject;

import android.os.Parcel;
import android.os.Parcelable;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class User implements Parcelable {
    String name,id, bio;
    ArrayList<PetPost> posts;
    Array pets;

    public User(String name,String id) {
        this.name = name;
        this.id = id;
    }


    protected User(Parcel in) {
        name = in.readString();
        id = in.readString();
        bio = in.readString();
        posts = in.createTypedArrayList(PetPost.CREATOR);
    }

    public static final Creator<User> CREATOR = new Creator<User>() {
        @Override
        public User createFromParcel(Parcel in) {
            return new User(in);
        }

        @Override
        public User[] newArray(int size) {
            return new User[size];
        }
    };

    public String getName() {
        return name;
    }

    public String getId(){return id; }

    public void setId(String id){this.id = id;}

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name);
        dest.writeString(id);
        dest.writeString(bio);
        dest.writeTypedList(posts);
    }
}
