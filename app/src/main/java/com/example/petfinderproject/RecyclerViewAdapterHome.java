package com.example.petfinderproject;

import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
/*
* Adapter for all the recyclerviews used. (RecyclerViews are just scrollable lists essentially)
* takes in a list of posts and the fragment activity. Displays the list of posts and uses
* the activity to create another fragment. You can click on a post to see it more indepth.
 */
public class RecyclerViewAdapterHome extends RecyclerView.Adapter<RecyclerViewAdapterHome.UserViewholder> {
    //variables we need
    ArrayList<PetPost> posts;
    FragmentActivity A;
    // instance for firebase storage and StorageReference
    StorageReference ref;
    FirebaseStorage storage;

    public RecyclerViewAdapterHome(ArrayList<PetPost> posts, FragmentActivity A){
        this.posts = posts;
        this.A = A;
    }
    @NonNull
    @Override
    public RecyclerViewAdapterHome.UserViewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //inflates the layout and creates a viewholder
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.fragment_post,parent,false);
        RecyclerViewAdapterHome.UserViewholder userViewholder = new RecyclerViewAdapterHome.UserViewholder(view);

        return userViewholder;
    }

    @Override
    public void onBindViewHolder(@NonNull UserViewholder holder, int position) {
        holder.p = posts.get(position);
        holder.textView30.setText(holder.p.status);
        holder.textView39.setText(holder.p.user.name);

        //gets the storage instance
        storage = FirebaseStorage.getInstance();
        // Reference to an image file in Cloud Storage using the url of the image
        ref = storage.getReferenceFromUrl(holder.p.petPic);

        //downloads the URL of the image and puts it in the required imageview using picasso
        ref.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Picasso.get().load(uri).into(holder.imageView2);
            }
        });
    }

    @Override
    public int getItemCount() {return posts.size();}

    public class UserViewholder extends RecyclerView.ViewHolder {
        //variables we need
        TextView postPetName;
        TextView postPetDetails;
        TextView textView30;
        TextView textView39;
        PetPost p;
        ImageView imageView2;


        public UserViewholder(@NonNull View itemView) {
            super(itemView);

            //fill the variables
            postPetName = itemView.findViewById(R.id.postPetName);
            postPetDetails = itemView.findViewById(R.id.postPetDetails);
            textView30 = itemView.findViewById(R.id.textViewLostFound);
            textView39 = itemView.findViewById(R.id.textViewUserName);
            imageView2 = itemView.findViewById(R.id.imageView2);

            // when a button in the home page scrollable list is clicked it goes to an indepth
            // view of the post
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                   A.getSupportFragmentManager().beginTransaction()
                            .replace(R.id.fragmentLayout, InDepthPostFragment.newInstance(p), "MyPosts")
                            .addToBackStack(null)
                            .commit();
                }
            });

        }
    }
}
