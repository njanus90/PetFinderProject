package com.example.petfinderproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
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
import android.widget.RelativeLayout;

import com.google.firebase.auth.FirebaseAuth;
public class MainActivity extends AppCompatActivity {

    RelativeLayout containerView;
    Toolbar myToolbar;
    String user;


    FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Gets the tool bar to set it after you login
        myToolbar = findViewById(R.id.toolbar);

        mAuth = FirebaseAuth.getInstance();

        containerView = findViewById(R.id.fragmentLayout);
        getSupportFragmentManager().beginTransaction()
                .add(R.id.fragmentLayout, new WelcomeFragment(), "welcome-screen")
                .commit();

        //TODO: Potential consistent login implementation, not tested
        /*if(mAuth.getCurrentUser() == null){
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.contentView, new LoginFragment())
                    .commit();
        } else {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.contentView, new MainFragment())
                    .commit();
        }*/
    }

    //Handles the creation of the toolbar
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.toolbar, menu);
        return true;
    }

    //Handles clicking of the toolbar
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.navProfile:
                // User chose the "Settings" item, show the app settings UI...
                this.user = user;
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fragmentLayout, MyProfileFragment.newInstance(new User(mAuth.getCurrentUser().getUid(),mAuth.getCurrentUser().getDisplayName(),mAuth.getCurrentUser().getEmail())), "my-profile-screen")
                        .addToBackStack(null)
                        .commit();
                return true;

            case R.id.navLogOut:
                // User chose the "Favorite" action, mark the current item
                // as a favorite...
                //TODO: Implement a logout on the user data
                this.user = null;
                setTitle(user);
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fragmentLayout, LoginFragment.newInstance(), "login-screen")
                        //TEMPORARY: Look for pop back stack in previous assignments
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