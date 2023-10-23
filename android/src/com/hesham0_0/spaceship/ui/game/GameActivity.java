package com.hesham0_0.spaceship.ui.game;

import static java.lang.Math.max;
import static java.lang.Math.min;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import android.animation.ObjectAnimator;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import com.badlogic.gdx.backends.android.AndroidFragmentApplication;
import com.hesham0_0.spaceship.R;
import com.hesham0_0.spaceship.ui.menu.MenuActivity;

public class GameActivity extends AppCompatActivity implements AndroidFragmentApplication.Callbacks {
    private GameViewModel gameViewModel;
    private TextView scoreText;
    private ImageView pauseBtn;
    private ProgressBar health_bar;
    private int health = 1000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        gameViewModel = new ViewModelProvider(this).get(GameViewModel.class);
        initUi();
        GameFragment gameFragment = new GameFragment();
        navToFrag(gameFragment);
    }
    private void navToFrag(Fragment fragment){
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.container, fragment)
                .commitNow();
    }
    private void initUi(){
        scoreText = findViewById(R.id.score);
        pauseBtn = findViewById(R.id.pause_circle);
        health_bar = findViewById(R.id.health_bar_value);
        pauseBtn.setOnClickListener(v -> {
            if (gameViewModel.gameState.getValue() == GameState.PLAYING) {
                gameViewModel.gameState.setValue(GameState.PAUSED);
                pauseBtn.setImageResource(R.drawable.play_circle);
            }else {
                gameViewModel.gameState.setValue(GameState.PLAYING);
                pauseBtn.setImageResource(R.drawable.pause_circle);
            }
        });
        health_bar.setMax(1000);
        decreaseHealthBar(health);
        setObserves();
    }

    private void setObserves() {
        gameViewModel.points.observe(this, points -> scoreText.setText(getResources().getString(R.string.score_game_text, points)));
        gameViewModel.healthDecrease.observe(this, points -> {
            health = min(1000,max(0, health + points * 30));
            decreaseHealthBar(health);
            if (health == 0) {
                navToMenuActivity();
            }
        });
    }

    private void decreaseHealthBar(int value) {
        ObjectAnimator.ofInt(health_bar, "progress", value)
                .setDuration(1000)
                .start();
    }

    @Override
    public void exit() {

    }
    private void navToMenuActivity() {
        Intent intent = new Intent(this, MenuActivity.class);
        startActivity(intent);
        finish();
    }
}