package com.example.petfinderproject;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link MyPostsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MyPostsFragment extends Fragment {
    RecyclerView recyclerViewMYPosts;
    LinearLayoutManager layoutManager;
    RecyclerViewAdapterHome adapter;
    FirebaseAuth auth;

    private static final String ARG_USER = "user";
    private static final String TAG = "SWAG";
    private String user;
    ArrayList<PetPost> posts = new ArrayList<>();

    public MyPostsFragment() {
        // Required empty public constructor
    }

    public static MyPostsFragment newInstance(String user) {
        MyPostsFragment fragment = new MyPostsFragment();
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
        View view = inflater.inflate(R.layout.fragment_my_posts, container, false);
        auth = FirebaseAuth.getInstance();

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        Log.d("TAG", auth.getCurrentUser().getUid());
        CollectionReference postOrder = db.collection("users").document(auth.getCurrentUser().getUid()).collection("posts");
        postOrder.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                posts.clear();
                //Log.d(TAG, value.getDocuments().get(0).get("PetName").toString());
                for(QueryDocumentSnapshot doc: value) {

                    posts.add(new PetPost(doc.get("lost").toString(),doc.get("PetName").toString(),doc.get("User").toString(),doc.get("details").toString(),null));
                }
                adapter.notifyDataSetChanged();
            }
        });

        recyclerViewMYPosts = view.findViewById(R.id.recyclerViewMYPosts);

        recyclerViewMYPosts.setHasFixedSize(true);
        //gives the recyclerView a layoutManager
        layoutManager = new LinearLayoutManager(getActivity());
        recyclerViewMYPosts.setLayoutManager(layoutManager);

        //makes an RecuclerViewAdapter which we created and sets it to the recyclerView
        adapter = new RecyclerViewAdapterHome(posts, getActivity());
        recyclerViewMYPosts.setAdapter(adapter);

        return view;
    }
}