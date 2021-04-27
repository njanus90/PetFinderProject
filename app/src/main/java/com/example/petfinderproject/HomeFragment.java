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

public class HomeFragment extends Fragment {
    private FirebaseAuth mAuth;

    private static final String ARG_USER = "user";
    private static final String TAG = "SWAG";
    private String user;

    Button allPostsButton, addLostOrFoundButton, chatButton, mapButton;
    RecyclerView recyclerView;
    LinearLayoutManager layoutManager;
    RecyclerViewAdapterHome adapter;

    ArrayList<User> users = new ArrayList<>();
    ArrayList<PetPost> posts = new ArrayList<>();
    //ArrayAdapter<User> adapter;
    User curUser;

    public HomeFragment() {
        // Required empty public constructor
    }

    public static HomeFragment newInstance(String user) {
        HomeFragment fragment = new HomeFragment();
        Bundle args = new Bundle();
        args.putString(ARG_USER, user);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            user = getArguments().getString(ARG_USER);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        mAuth = FirebaseAuth.getInstance();


        allPostsButton = view.findViewById(R.id.allPostsButton);
        addLostOrFoundButton = view.findViewById(R.id.addLostOrFoundButton);
        mapButton = view.findViewById(R.id.mapButton);
        recyclerView = view.findViewById(R.id.recyclerView);
        getData();



        recyclerView.setHasFixedSize(true);
        //gives the recyclerView a layoutManager
        layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);

        //makes an RecuclerViewAdapter which we created and sets it to the recyclerView
        adapter = new RecyclerViewAdapterHome(posts, getActivity());
        recyclerView.setAdapter(adapter);



        allPostsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.fromHomeToAllPosts(user);
            }
        });

        addLostOrFoundButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.fromHomeToAdd(user);
            }
        });

        mapButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getFragmentManager().beginTransaction().replace(R.id.fragmentLayout, new MapsFragment()).commit();
            }
        });


        //TODO: Implement a ListView of scrollable and clickable posts near the user

        return view;
    }

    private void getData(){
        //Retrieves data from Database
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        db.collection("users")
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {

                        users.clear();
                        posts.clear();
                        for (QueryDocumentSnapshot document: value){

                            Log.d("SWAG", document.getData().get("name").toString());
                            //ArrayList<String> likedBy = (ArrayList<String>)document.get("likedBy");
                            users.add(new User(document.getData().get("name").toString()));
                            Log.d(TAG, document.getId());
                            //this gets the data from the post collection in the firestore it loops through all the
                            // documents (users) and adds all their posts to an array that we pass into
                            // the recyclerview to list them on the main page.
                            db.collection("users").document(document.getId()).collection("posts").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                @Override
                                public void onComplete(@NonNull Task<QuerySnapshot> task) {

                                    for(int i =0; i < task.getResult().getDocuments().size(); i++) {
                                        //Log.d(TAG, task.getResult().getDocuments().get(i).get("lost/found").toString());
                                        String lost = task.getResult().getDocuments().get(i).get("lost").toString();
                                        String petName = task.getResult().getDocuments().get(i).get("PetName").toString();
                                        String UserId = task.getResult().getDocuments().get(i).get("User").toString();
                                        String details = task.getResult().getDocuments().get(i).get("details").toString();
                                        //String lat = task.getResult().getDocuments().get(i).get("lat").toString();
                                        //String lng = task.getResult().getDocuments().get(i).get("lng").toString();

                                        posts.add(new PetPost(lost, petName ,UserId,details,null,null,null));
                                    }
                                    adapter.notifyDataSetChanged();
                                }
                            });

                            getActivity().runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    //TODO: Get the name in the title working
                                    getActivity().setTitle(document.getString("name"));
                                    allPostsButton.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            getFragmentManager().beginTransaction()
                                                    .replace(R.id.fragmentLayout, MyPostsFragment.newInstance(mAuth.getUid()), "MyPosts")
                                                    .commit();
                                        }
                                    });
                                }
                            });
                        }
                        adapter.notifyDataSetChanged();
                    }
                });
    }
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof HomeFragment.homeInterface) {
            mListener = (HomeFragment.homeInterface)context;
        } else {
            throw new RuntimeException(context.toString() + " must implement IListener");
        }
    }

    HomeFragment.homeInterface mListener;
    public interface homeInterface {
        public void fromHomeToAllPosts(String user);
        public void fromHomeToAdd(String user);
        public void fromHomeToMap(String user);
        public void fromHomeToChat(String user);
    }
}