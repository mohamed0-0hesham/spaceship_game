package com.hesham0_0.spaceship;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;

import com.badlogic.gdx.backends.android.AndroidFragmentApplication;
import com.hesham0_0.spaceship.ui.main.MainFragment;

public class MainActivity extends AppCompatActivity implements AndroidFragmentApplication.Callbacks {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        MainFragment gameFragment = new MainFragment();
        getSupportFragmentManager()
                .beginTransaction()
                .add(R.id.container, gameFragment)
                .addToBackStack("0")
                .commit();
    }

    @Override
    public void exit() {

    }
}