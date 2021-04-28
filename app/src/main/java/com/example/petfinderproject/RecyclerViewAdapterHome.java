package com.example.petfinderproject;

import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.gms.tasks.OnCanceledListener;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.UUID;

public class RecyclerViewAdapterHome extends RecyclerView.Adapter<RecyclerViewAdapterHome.UserViewholder> {
    ArrayList<PetPost> posts;
    FragmentActivity A;
    // instance for firebase storage and StorageReference
    StorageReference ref;
    FirebaseStorage storage;
    StorageReference storageReference;
    File localFile;
    Context context;

    public RecyclerViewAdapterHome(ArrayList<PetPost> posts, FragmentActivity A){
        this.posts = posts;
        this.A = A;
    }
    @NonNull
    @Override
    public RecyclerViewAdapterHome.UserViewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.fragment_post,parent,false);
        context = parent.getContext();
        RecyclerViewAdapterHome.UserViewholder userViewholder = new RecyclerViewAdapterHome.UserViewholder(view);

        return userViewholder;
    }

    @Override
    public void onBindViewHolder(@NonNull UserViewholder holder, int position) {
        holder.p = posts.get(position);
        holder.postPetName.setText(holder.p.name);
        holder.postPetDetails.setText(holder.p.details);
        holder.textView30.setText(holder.p.status);

        storage = FirebaseStorage.getInstance();
        // Reference to an image file in Cloud Storage
        //ref = storage.getReference(); holder.p.image
        storageReference = ref.child("images/" + UUID.randomUUID().toString());
        //ref = storageReference.child("images/" + UUID.randomUUID().toString());


        ref.child(holder.p.image).getDownloadUrl().addOnCompleteListener(new OnCompleteListener<Uri>() {
            @Override
            public void onComplete(@NonNull Task<Uri> task) {
                if(task.isSuccessful()){
                    Log.d("SWAG", holder.p.image);
                    String uri = task.getResult().toString();
                    Picasso.get().load(uri).into(holder.imageView2);

                } else {
                    Log.d("SWAG", "NoWork");
                }
            }
        });
//         //Download directly from StorageReference using Glide
//        Glide.with(context)
//                .load(storageReference)
//                .into(holder.imageView2);
    }

    @Override
    public int getItemCount() {return posts.size();}

    public class UserViewholder extends RecyclerView.ViewHolder {
        TextView postPetName;
        TextView postPetDetails;
        TextView textView30;
        PetPost p;
        ImageView imageView2;


        public UserViewholder(@NonNull View itemView) {
            super(itemView);
            postPetName = itemView.findViewById(R.id.postPetName);
            postPetDetails = itemView.findViewById(R.id.postPetDetails);
            textView30 = itemView.findViewById(R.id.textView30);
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
