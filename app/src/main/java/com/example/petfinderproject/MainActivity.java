package com.example.petfinderproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.ClipData;
import android.os.Bundle;
import android.util.Log;

import androidx.fragment.app.FragmentActivity;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.RelativeLayout;

import com.google.firebase.auth.FirebaseAuth;

/*
* this is the main activity which all the fragments are run out of
 */
public class MainActivity extends AppCompatActivity {

    RelativeLayout containerView;
    Toolbar myToolbar;
    FirebaseAuth mAuth;
    //public toolbar items
    public static MenuItem prof;
    public static MenuItem log;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Gets the tool bar to set it after you login
        myToolbar = findViewById(R.id.toolbar);
        setSupportActionBar(myToolbar);

        mAuth = FirebaseAuth.getInstance();
        setTitle(null);

        containerView = findViewById(R.id.fragmentLayout);

        getSupportFragmentManager().beginTransaction()
                .add(R.id.fragmentLayout, new WelcomeFragment(), "welcome-screen")
                .commit();
    }

    //Handles the creation of the toolbar
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.toolbar, menu);

        //the toolbar items
        prof = menu.findItem(R.id.navProfile);
        log = menu.findItem(R.id.navLogOut);

        return true;
    }

    //Handles clicking of the toolbar
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.navProfile:
                // User chose the "Settings" item, show the app settings UI...
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fragmentLayout, MyProfileFragment.newInstance(new User(mAuth.getCurrentUser().getDisplayName(),mAuth.getCurrentUser().getUid(),mAuth.getCurrentUser().getEmail())), "my-profile-screen")
                        .addToBackStack(null)
                        .commit();
                return true;

            case R.id.navLogOut:
                //gets rid of all stuff on backstack

                mAuth.signOut();
                setTitle(null);
                //sets the items in the tool bar invisable
                MainActivity.prof.setVisible(false);
                MainActivity.log.setVisible(false);
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fragmentLayout, new WelcomeFragment(), "welcome-screen")
                        .addToBackStack(null)
                        .commit();
                return true;

            default:

                // If we got here, the user's action was not recognized.
                // Invoke the superclass to handle it.
                return super.onOptionsItemSelected(item);

        }
    }
}