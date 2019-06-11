package com.kajal.mynotes.ui;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.kajal.mynotes.adapters.MainViewPagerAdapter;
import com.kajal.mynotes.R;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private Toolbar toolbar;
    private ViewPager viewPager;
    private MainViewPagerAdapter mainViewPagerAdapter;
    private TabLayout tabLayout;

    private int[] tabIcons = { R.drawable.ic_notes_gray, R.drawable.ic_notifications_gray };
    private int[] tabLabels = { R.string.notes_tab, R.string.notifications_tab };
    private int[] tabActiveIcons = { R.drawable.ic_notes_white, R.drawable.ic_notifications_white };

    private DrawerLayout drawer;

    private FirebaseAuth mAuth;
    private GoogleSignInClient googleSignInClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mAuth = FirebaseAuth.getInstance();
        // Configure Google Sign In
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
        googleSignInClient = GoogleSignIn.getClient(this, gso);

        if(mAuth.getCurrentUser() != null) {

            toolbar = findViewById(R.id.toolbar_main);
            setSupportActionBar(toolbar);

            drawer = findViewById(R.id.nav_drawer_layout);
            NavigationView navigationView = findViewById(R.id.nav_view);
            navigationView.setNavigationItemSelectedListener(this);

            ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar,
                    R.string.navigation_drawer_open, R.string.navigation_drawer_close);
            drawer.addDrawerListener(toggle);
            toggle.syncState();

            viewPager = findViewById(R.id.viewpager_main);
            mainViewPagerAdapter = new MainViewPagerAdapter(getSupportFragmentManager());
            viewPager.setAdapter(mainViewPagerAdapter);

            tabLayout = findViewById(R.id.tabLayout_main);
            tabLayout.setupWithViewPager(viewPager);
            tabLayout.getTabAt(0).setIcon(R.drawable.ic_notes_gray);
            tabLayout.getTabAt(1).setIcon(R.drawable.ic_notifications_gray);

            setCustomTab();
            tabLayout.addOnTabSelectedListener(new TabLayout.BaseOnTabSelectedListener() {
                @Override
                public void onTabSelected(TabLayout.Tab tab) {
                    //this method changes the selected tab icon & img to bright white
                    changeToTabSelected(tab);
                }

                @Override
                public void onTabUnselected(TabLayout.Tab tab) {
                    //this method changes the selected tab icon & img back to gray
                    changeToTabUnselected(tab);

                }

                @Override
                public void onTabReselected(TabLayout.Tab tab) {

                }
            });

        }

    }

    @Override
    protected void onStart() {
        super.onStart();

        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();

        if(currentUser == null){
            sendToSignIn();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_toolbar,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()){
            case R.id.menu_settings :
                sendToSettings();
                break;
        }

        return true;
    }

    @Override
    public void onBackPressed() {
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    private void setCustomTab(){

        for (int i = 0; i < tabLayout.getTabCount(); i++) {
            // inflate the Parent LinearLayout Container for the tab
            // from the layout nav_tab.xml file that we created 'R.layout.nav_tab
            LinearLayout tab = (LinearLayout) LayoutInflater.from(this).inflate(R.layout.tab_single, null);

            // get child TextView and ImageView from this layout for the icon and label
            TextView tab_label =  tab.findViewById(R.id.nav_label);
            ImageView tab_icon =  tab.findViewById(R.id.nav_icon);

            // set the label text by getting the actual string value by its id
            // by getting the actual resource value `getResources().getString(string_id)`
            tab_label.setText(getResources().getString(tabLabels[i]));

            // set the Notes to be active at first
            if(i == 0) {
                tab_label.setTextColor(getResources().getColor(R.color.BrightWhiteColor));
                tab_icon.setImageResource(tabActiveIcons[i]);
            } else {
                tab_label.setTextColor(getResources().getColor(R.color.GrayColor));
                tab_icon.setImageResource(tabIcons[i]);
            }

            // finally publish this custom view to navigation tab
            tabLayout.getTabAt(i).setCustomView(tab);
        }

    }

    private void changeToTabSelected(TabLayout.Tab tab) {
        View tabView = tab.getCustomView();

        // get inflated children Views the icon and the label by their id
        TextView tab_label = tabView.findViewById(R.id.nav_label);
        ImageView tab_icon = tabView.findViewById(R.id.nav_icon);

        // change the label color, by getting the color resource value
        tab_label.setTextColor(getResources().getColor(R.color.BrightWhiteColor));
        // change the image Resource
        // i defined all icons in an array ordered in order of tabs appearances
        // call tab.getPosition() to get active tab index.
        tab_icon.setImageResource(tabActiveIcons[tab.getPosition()]);
    }

    private void changeToTabUnselected(TabLayout.Tab tab) {
        View tabView = tab.getCustomView();
        TextView tab_label =  tabView.findViewById(R.id.nav_label);
        ImageView tab_icon =  tabView.findViewById(R.id.nav_icon);

        // back to the black color
        tab_label.setTextColor(getResources().getColor(R.color.GrayColor));
        // and the icon resouce to the old black image
        // also via array that holds the icon resources in order
        // and get the one of this tab's position
        tab_icon.setImageResource(tabIcons[tab.getPosition()]);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        switch (menuItem.getItemId()){
            case R.id.nav_profile:
            {
                Intent intent = new Intent(MainActivity.this, ProfileActivity.class);
                startActivity(intent);
                break;
            }
            case R.id.nav_starred:
            {
                Intent intent = new Intent(MainActivity.this,ProfileActivity.class);
                startActivity(intent);
                break;
            }
            case R.id.nav_help:
            {
                Intent intent = new Intent(MainActivity.this,ProfileActivity.class);
                startActivity(intent);
                break;
            }
            case R.id.nav_donate:
            {
                Toast.makeText(this,"Donated",Toast.LENGTH_LONG).show();
                break;
            }
            case R.id.nav_share:
            {
                Toast.makeText(this,"Shared",Toast.LENGTH_LONG).show();
                break;
            }
            case R.id.nav_signOut:
            {
                signOut();

                finish();

                Toast.makeText(getApplication(),"Signed Out",Toast.LENGTH_LONG).show();
                sendToSignIn();
                break;
            }
        }
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void signOut() {
        FirebaseAuth.getInstance().signOut();
        googleSignInClient.signOut();
    }

    private void sendToSignIn(){
        Intent signInIntent = new Intent(this, SignInActivity.class);
        startActivity(signInIntent);
        finish();
    }

    private void sendToSettings() {
        Intent settingsIntent = new Intent(MainActivity.this, SettingsActivity.class);
        startActivity(settingsIntent);
    }

}
