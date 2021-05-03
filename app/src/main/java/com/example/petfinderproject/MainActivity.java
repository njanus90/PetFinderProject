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
public class MainActivity extends AppCompatActivity {

    RelativeLayout containerView;
    Toolbar myToolbar;
    //String user;

    FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Gets the tool bar to set it after you login
        myToolbar = findViewById(R.id.toolbar);
        setSupportActionBar(myToolbar);

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
                //this.user = mAuth.getCurrentUser().getDisplayName();

                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fragmentLayout, MyProfileFragment.newInstance(new User(mAuth.getCurrentUser().getDisplayName(),mAuth.getCurrentUser().getDisplayName(),mAuth.getCurrentUser().getEmail())), "my-profile-screen")
                        .addToBackStack(null)
                        .commit();
                return true;

            case R.id.navLogOut:
                //gets rid of all stuff on backstack
//                int count = getSupportFragmentManager().getBackStackEntryCount();
//                for (int i = 0; i < count; i++)
//                {
//                    getSupportFragmentManager().popBackStack();
//                }
//                Log.d("SWAG", "Before Signout ");
//                mAuth.signOut();
//                Log.d("SWAG", "After Signout ");
                //TODO: Implement proper sign out
                //this.user = null;
                setTitle("PetFinder");
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fragmentLayout, WelcomeFragment.newInstance(), "welcome-screen")
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