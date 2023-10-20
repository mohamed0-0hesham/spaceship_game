package com.hesham0_0.spaceship.ui.main;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;
import android.widget.TextView;

import com.badlogic.gdx.backends.android.AndroidFragmentApplication;
import com.hesham0_0.spaceship.R;

public class MainActivity extends AppCompatActivity implements AndroidFragmentApplication.Callbacks {

    private GameViewModel gameViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        gameViewModel = new ViewModelProvider(this).get(GameViewModel.class);
        TextView scoreText = findViewById(R.id.score);
        GameFragment gameFragment = new GameFragment();
        getSupportFragmentManager()
                .beginTransaction()
                .add(R.id.container, gameFragment)
                .addToBackStack("0")
                .commit();
        gameViewModel.points.observe(this,points -> {
            scoreText.setText(getResources().getString(R.string.score_game_text, points));
        });
    }

    @Override
    public void exit() {

    }
}