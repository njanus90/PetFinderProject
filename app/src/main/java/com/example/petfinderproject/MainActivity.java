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

public class MainActivity extends AppCompatActivity implements  WelcomeFragment.accountButtons, LoginFragment.login, HomeFragment.homeInterface,
        AddPetFragment.addInterface, CreateAccountFragment.create, ChangePasswordFragment.changePassword{

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
                        .replace(R.id.fragmentLayout, MyProfileFragment.newInstance(user), "my-profile-screen")
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

    @Override
    public void fromWelcomeToLogin() {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragmentLayout, LoginFragment.newInstance(), "login-screen")
                //.addToBackStack(null)
                .commit();

    }

    @Override
    public void fromWelcomeToCreate() {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragmentLayout, CreateAccountFragment.newInstance(), "create-screen")
                //.addToBackStack(null)
                .commit();
    }

    @Override
    public void fromLoginToHome(String user) {
        //Keeps the user known in the main activity for logout
        this.user = user;
        //Sets the toolbar after logging in
        setSupportActionBar(myToolbar);
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragmentLayout, HomeFragment.newInstance(user), "home-screen")
                //TODO: Make it to where hitting back on this screen closes the app or logs out, for now back stack is needed
                .addToBackStack(null)
                .commit();
    }

    @Override
    public void fromLoginToChangePassword() {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragmentLayout, ChangePasswordFragment.newInstance(), "add-screen")
                //.addToBackStack(null)
                .commit();
    }

    @Override
    public void fromHomeToAllPosts(String user) {
        this.user = user;
    }

    @Override
    public void fromHomeToAdd(String user) {
        this.user = user;
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragmentLayout, AddPetFragment.newInstance(user), "add-screen")
                //.addToBackStack(null)
                .commit();
    }

    @Override
    public void fromHomeToMap(String user) {
        //TODO: Implement map screen
    }

    @Override
    public void fromHomeToChat(String user) {
        //TODO: Implement chat screen
    }

    @Override
    public void fromAddToHome(String user) {
        //Keeps the user known in the main activity for logout
        this.user = user;
        //Sets the toolbar after logging in
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragmentLayout, HomeFragment.newInstance(user), "home-screen")
                //.addToBackStack(null)
                .commit();
    }

    @Override
    public void fromCreateToLogin() {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragmentLayout, LoginFragment.newInstance(), "login-screen")
                //.addToBackStack(null)
                .commit();
    }

    @Override
    public void fromChangeToLogin() {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragmentLayout, LoginFragment.newInstance(), "login-screen")
                //.addToBackStack(null)
                .commit();
    }
}