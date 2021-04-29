package com.example.petfinderproject;

import android.os.Parcel;
import android.os.Parcelable;

import java.lang.reflect.Array;
import java.util.ArrayList;
/*
* This is our user class that we will be using throught the app
* it takes in the name of the user and the id of the user that is used in firebase
* its also Parcelable so we can move it between fragments easily
 */
public class User implements Parcelable {
    String name,id, bio,email;
    ArrayList<PetPost> posts;
    Array pets;

    public User(String name,String id,String email) {
        this.name = name;
        this.id = id;
        this.email = email;

    }


    protected User(Parcel in) {
        name = in.readString();
        id = in.readString();
        bio = in.readString();
        posts = in.createTypedArrayList(PetPost.CREATOR);
        email = in.readString();
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

    public void setEmail(String email){this.email = email;}

    public String getEmail() {return email; }

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
        dest.writeString(email);
    }
}
