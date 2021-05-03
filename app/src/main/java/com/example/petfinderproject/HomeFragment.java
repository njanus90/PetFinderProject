package com.example.petfinderproject;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ScrollView;

import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.Source;

import java.util.ArrayList;
import java.util.HashMap;

/*
 * This Fragment does most the work. It is the screen you see when you get logged in
 * the user has a few options here. You can click to see all your posts, you can scroll
 * through all the lost/found pets ordered by the closest in your vacinity, you can click on
 * one of the posts to see it more indepth, you can click add a post button to add a post, and finally
 * you can click the map button to see the locations of the posts.
 */
public class HomeFragment extends Fragment {

    //all the variables we need
    private FirebaseAuth mAuth;
    private static final String ARG_USER = "user";
    private static final String TAG = "SWAG";
    private User user;
    Button allPostsButton, addLostOrFoundButton, mapButton;
    RecyclerView recyclerView;
    LinearLayoutManager layoutManager;
    RecyclerViewAdapterHome adapter;
    ArrayList<PetPost> posts = new ArrayList<>();

    public HomeFragment() {
        // Required empty public constructor
    }
    //the following two methods take in a user from another fragment
    public static HomeFragment newInstance(User user) {
        HomeFragment fragment = new HomeFragment();
        Bundle args = new Bundle();
        args.putParcelable(ARG_USER, user);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            user = getArguments().getParcelable(ARG_USER);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        //fills the variables
        mAuth = FirebaseAuth.getInstance();
        allPostsButton = view.findViewById(R.id.allPostsButton);
        addLostOrFoundButton = view.findViewById(R.id.addLostOrFoundButton);
        mapButton = view.findViewById(R.id.mapButton);
        recyclerView = view.findViewById(R.id.recyclerView);

        getData();


        //sets the recyclerview to the adapter we made for it
        recyclerView.setHasFixedSize(true);
        //gives the recyclerView a layoutManager
        layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);

        //makes an RecuclerViewAdapter which we created and sets it to the recyclerView
        adapter = new RecyclerViewAdapterHome(posts, getActivity());
        recyclerView.setAdapter(adapter);


        //if the button to add a post is clicked it moves to that fragment
        addLostOrFoundButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getFragmentManager().beginTransaction()
                        .replace(R.id.fragmentLayout, AddPetFragment.newInstance(user),"addPost")
                        .addToBackStack(null)
                        .commit();
            }
        });

        //if the map button is clicked it moves to the map fragment
        mapButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getFragmentManager().beginTransaction()
                        .replace(R.id.fragmentLayout, new MapsFragment(),"maps")
                        .addToBackStack(null)
                        .commit();
            }
        });

        //if the user clicks on the my posts button it moves to that fragment
        allPostsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getFragmentManager().beginTransaction()
                        .replace(R.id.fragmentLayout, MyPostsFragment.newInstance(user.id), "MyPosts")
                        .addToBackStack(null)
                        .commit();
            }
        });

        return view;
    }

    //this method gets all the data needed in the homepage from the firestore database
    private void getData(){
        //gets the firebase instance
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        db.collection("users")
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                        //clears the posts arrayso we don't see things more than once
                        posts.clear();
                        // checks to see if there are no errors
                        if(error == null) {
                            //loops through the users collection in firestore
                            for (QueryDocumentSnapshot document : value) {
                                //this gets the data from the post collection in the firestore it loops through all the
                                // post collection each user has and adds all their posts to an array that we pass into
                                // the recyclerview to list them on the main page.
                                db.collection("users").document(document.getId()).collection("posts").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                    @Override
                                    public void onComplete(@NonNull Task<QuerySnapshot> task) {

                                        for (int i = 0; i < task.getResult().getDocuments().size(); i++) {

                                            String lost = task.getResult().getDocuments().get(i).get("lost").toString();
                                            String petName = task.getResult().getDocuments().get(i).get("PetName").toString();
                                            HashMap UserName = (HashMap) (task.getResult().getDocuments().get(i).get("user"));
                                            String details = task.getResult().getDocuments().get(i).get("details").toString();
                                            String image = task.getResult().getDocuments().get(i).get("image").toString();
                                            //String lat = task.getResult().getDocuments().get(i).get("lat").toString();
                                            //String lng = task.getResult().getDocuments().get(i).get("lng").toString();
                                            User u = new User(UserName.get("name").toString(), UserName.get("id").toString(), UserName.get("email").toString());
                                            posts.add(new PetPost(lost, petName, u, details, image, null, null));
                                        }
                                        adapter.notifyDataSetChanged();
                                    }
                                });
                            }
                            adapter.notifyDataSetChanged();
                        }
                    }
                });
    }
}