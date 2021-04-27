package com.example.petfinderproject;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCanceledListener;

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
        holder.postPetName.setText(holder.p.name);
        holder.postPetDetails.setText(holder.p.details);
        //redundant code once we change the information in the firestore
        if (holder.p.status.equals("true")) {
            holder.textView30.setText("LOST");
        } else{
            holder.textView30.setText("FOUND");
        }
    }

    @Override
    public int getItemCount() {return posts.size();}

    public class UserViewholder extends RecyclerView.ViewHolder {
        TextView postPetName;
        TextView postPetDetails;
        TextView textView30;
        PetPost p;


        public UserViewholder(@NonNull View itemView) {
            super(itemView);
            postPetName = itemView.findViewById(R.id.postPetName);
            postPetDetails = itemView.findViewById(R.id.postPetDetails);
            textView30 = itemView.findViewById(R.id.textView30);

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