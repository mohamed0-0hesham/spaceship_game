package com.hesham0_0.spaceship.ui.menu;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;

import com.hesham0_0.spaceship.R;

public class MenuActivity extends AppCompatActivity {

    private MenuViewModel menuViewModel;
    private MenuFragment menuFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);
        menuViewModel = new ViewModelProvider(this).get(MenuViewModel.class);
        if (savedInstanceState == null) {
            menuFragment = MenuFragment.newInstance();
            navToFragment(menuFragment);
        }
    }
    private void navToFragment(Fragment fragment){
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.container, fragment)
                .commitNow();
    }
}