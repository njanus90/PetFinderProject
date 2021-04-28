package com.example.petfinderproject;

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

import com.google.android.gms.tasks.OnCanceledListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class RecyclerViewAdapterHome extends RecyclerView.Adapter<RecyclerViewAdapterHome.UserViewholder> {
    ArrayList<PetPost> posts;
    FragmentActivity A;

    public RecyclerViewAdapterHome(ArrayList<PetPost> posts, FragmentActivity A){
        this.posts = posts;
        this.A = A;
    }
    @NonNull
    @Override
    public RecyclerViewAdapterHome.UserViewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.fragment_post,parent,false);
        RecyclerViewAdapterHome.UserViewholder userViewholder = new RecyclerViewAdapterHome.UserViewholder(view);

        return userViewholder;
    }

    @Override
    public void onBindViewHolder(@NonNull UserViewholder holder, int position) {
        holder.p = posts.get(position);
        //holder.postPetName.setText(holder.p.name);
        //holder.postPetDetails.setText(holder.p.details);
        holder.textView30.setText(holder.p.status);
        //sets the image
        Picasso.get().load(holder.p.image).into(holder.imageView2);
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
