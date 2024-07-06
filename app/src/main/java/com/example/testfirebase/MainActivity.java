package com.example.testfirebase;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import com.example.testfirebase.pages.ChatFragment;
import com.example.testfirebase.pages.MainFragment;
import com.example.testfirebase.pages.ProfileFragemnt;
import com.example.testfirebase.pages.SearchFragment;
import com.example.testfirebase.pages.VipFragment;
import com.google.firebase.firestore.FirebaseFirestore;
import com.lorentzos.flingswipe.SwipeFlingAdapterView;
import java.util.ArrayList;

import android.view.MenuItem;
import androidx.annotation.NonNull;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener  {
    private ArrayList<String> al;
    private ArrayAdapter<String> arrayAdapter;
    private int i;
    FirebaseFirestore firestore;

    BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
            al = new ArrayList<>();
            al.add("php");
            al.add("c");
            al.add("python");
            al.add("java");
            al.add("html");
            al.add("c++");
            al.add("css");
            al.add("javascript");
            arrayAdapter = new ArrayAdapter<>(this, R.layout.item, R.id.helloText, al );

            SwipeFlingAdapterView flingContainer = (SwipeFlingAdapterView) findViewById(R.id.frame);

            flingContainer.setAdapter(arrayAdapter);
            flingContainer.setFlingListener(new SwipeFlingAdapterView.onFlingListener() {
                @Override
                public void removeFirstObjectInAdapter() {
                    // this is the simplest way to delete an object from the Adapter (/AdapterView)
                    Log.d("LIST", "removed object!");
                    al.remove(0);
                    arrayAdapter.notifyDataSetChanged();
                }

                @Override
                public void onLeftCardExit(Object dataObject) {
                    //Do something on the left!
                    //You also have access to the original object.
                    //If you want to use it just cast it (String) dataObject
                    Toast.makeText(MainActivity.this, "Left", Toast.LENGTH_SHORT).show();
                }


                @Override
                public void onRightCardExit(Object dataObject) {
                    // Xử lý hành động khi phần tử được swipe bình thường
                    Toast.makeText(MainActivity.this, "Right", Toast.LENGTH_SHORT).show();
                }
                @Override
                public void onAdapterAboutToEmpty(int itemsInAdapter) {
                    // Ask for more data here
                    al.add("XML ".concat(String.valueOf(i)));
                    arrayAdapter.notifyDataSetChanged();
                    Log.d("LIST", "notified");
                    i++;
                }

                @Override
                public void onScroll(float scrollProgressPercent) {

                }
            });


            // Optionally add an OnItemClickListener
            flingContainer.setOnItemClickListener(new SwipeFlingAdapterView.OnItemClickListener() {
                @Override
                public void onItemClicked(int itemPosition, Object dataObject) {
                    Toast.makeText(MainActivity.this, "Click", Toast.LENGTH_SHORT).show();
                }
            });

            bottomNavigationView
                    = findViewById(R.id.bottomNavigationView);

            bottomNavigationView
                    .setOnNavigationItemSelectedListener(this);
            bottomNavigationView.setSelectedItemId(R.id.main);
        }
            MainFragment mainFragment = new MainFragment();
            VipFragment vipFragment = new VipFragment();
            ChatFragment chatFragment = new ChatFragment();
            SearchFragment searchFragment = new SearchFragment();
            ProfileFragemnt profileFragemnt = new ProfileFragemnt();

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.main:
                getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.MainFragment, mainFragment)
                        .addToBackStack("main_fragment")
                        .commit();
                return true;

            case R.id.vip:
                getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.vip, vipFragment)
                        .addToBackStack(null)
                        .commit();
                return true;

            case R.id.search:
                getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.search, searchFragment)
                        .addToBackStack(null)
                        .commit();
                return true;
            case R.id.chat:
                getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.chat, chatFragment)
                        .addToBackStack(null)
                        .commit();
                return true;
            case R.id.profile:
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.profile, profileFragemnt)
                    .addToBackStack(null)
                    .commit();
        }
        return false;
    }
}