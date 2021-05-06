package com.example.petfinderproject;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.GeoPoint;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;


/*
* Fragment to display the maps feature.
 */
public class MapsFragment extends Fragment {

    private GoogleMap mMap;
    FirebaseFirestore db;
    FirebaseAuth mAuth;
    ArrayList<PetPost> posts = new ArrayList<>();

    private OnMapReadyCallback callback = new OnMapReadyCallback() {

        /**
         * Manipulates the map once available.
         * This callback is triggered when the map is ready to be used.
         * This is where we can add markers or lines, add listeners or move the camera.
         * In this case, we just add a marker near Sydney, Australia.
         * If Google Play services is not installed on the device, the user will be prompted to
         * install it inside the SupportMapFragment. This method will only be triggered once the
         * user has installed Google Play services and returned to the app.
         */
        @Override
        public void onMapReady(GoogleMap googleMap) {
            //Default Location
            LatLng UNCC = new LatLng(35.30727236657719, -80.7351532734925);
            //googleMap.addMarker(new MarkerOptions().position(UNCC).title("UNCC"));
            googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(UNCC, 15), 2000, null);


            mAuth = FirebaseAuth.getInstance();
            db = FirebaseFirestore.getInstance();
            mMap = googleMap;

            CollectionReference postOrder = db.collection("users");
            postOrder.addSnapshotListener(new EventListener<QuerySnapshot>() {
                @Override
                public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                    for (QueryDocumentSnapshot doc : value) {
                        db.collection("users").document(doc.getId()).collection("posts").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                for(int i = 0; i < task.getResult().getDocuments().size(); i++){
                                    LatLng location = new LatLng(Double.valueOf(task.getResult().getDocuments().get(i).get("lat").toString()), Double.valueOf(task.getResult().getDocuments().get(i).get("lng").toString()));
                                    mMap.addMarker(new MarkerOptions().position(location).title(task.getResult().getDocuments().get(i).get("PetName").toString()));

                                    String lost = task.getResult().getDocuments().get(i).get("lost").toString();
                                    String petName = task.getResult().getDocuments().get(i).get("PetName").toString();
                                    HashMap UserName = (HashMap) (task.getResult().getDocuments().get(i).get("user"));
                                    String details = task.getResult().getDocuments().get(i).get("details").toString();
                                    String image = task.getResult().getDocuments().get(i).get("image").toString();
                                    String lat = task.getResult().getDocuments().get(i).get("lat").toString();
                                    String lng = task.getResult().getDocuments().get(i).get("lng").toString();
                                    User u = new User(UserName.get("name").toString(), UserName.get("id").toString(), UserName.get("email").toString());
                                    posts.add(new PetPost(lost, petName, u, details, image, lat, lng));
                                }
                            }
                        });
                    }
                }
            });

            mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
                @Override
                public boolean onMarkerClick(Marker marker) {
                    for(int i =0; i < posts.size(); i++){
                        if(marker.getPosition().latitude == Double.valueOf(posts.get(i).lat) && marker.getPosition().longitude == Double.valueOf(posts.get(i).lng)){
                            getFragmentManager().beginTransaction()
                                    .replace(R.id.fragmentLayout, InDepthPostFragment.newInstance(posts.get(i)), "MyPosts")
                                    .addToBackStack(null)
                                    .commit();
                        }
                    }

                    marker.getPosition();

                    return false;
                }
            });

            /*db.collection("users").document(mAuth.getUid()).collection("posts").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                @Override
                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                    LatLng location = new LatLng(Double.valueOf(task.getResult().getDocuments().get(0).get("lat").toString()), Double.valueOf(task.getResult().getDocuments().get(0).get("lng").toString()));
                    mMap.addMarker(new MarkerOptions().position(location).title("Marker"));
                }
            });*/
        }
    };

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_maps, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        SupportMapFragment mapFragment =
                (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
        if (mapFragment != null) {
            mapFragment.getMapAsync(callback);
        }
    }
}