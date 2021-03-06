package com.example.petfinderproject;

import android.os.Parcel;
import android.os.Parcelable;
import android.widget.ImageView;

/*
* class to hold all the information the posts need
* holds the name of the pet, the details of the post, the status of the pet (lost or found)
* the last known location of the pet, the user who posted the post,
* and the location of the picture of the pet in firebase storage.
 */

public class PetPost implements Parcelable {

    String name, details, status, lat, lng;
    User user;
    //TODO: Some Map variable maybe?
    String petPic;

    public PetPost(String status, String name, User user, String details, String petPic, String lat, String lng) {
        this.status = status;
        this.name = name;
        this.details = details;
        this.petPic = petPic;
        this.user = user;
        this.lat = lat;
        this.lng = lng;
    }

    protected PetPost(Parcel in) {
        name = in.readString();
        details = in.readString();
        status = in.readString();
        lat = in.readString();
        lng = in.readString();
        user = in.readParcelable(User.class.getClassLoader());
        petPic = in.readString();
    }

    public static final Creator<PetPost> CREATOR = new Creator<PetPost>() {
        @Override
        public PetPost createFromParcel(Parcel in) {
            return new PetPost(in);
        }

        @Override
        public PetPost[] newArray(int size) {
            return new PetPost[size];
        }
    };

    //just some setters and getters
    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
    }

    public String getPetPic() {
        return petPic;
    }

    public void setPetPic(String petPic) {
        this.petPic = petPic;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name);
        dest.writeString(details);
        dest.writeParcelable(user,1);
        dest.writeString(status);
        dest.writeString(lat);
        dest.writeString(lng);
        dest.writeString(petPic);
    }
}
