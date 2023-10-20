package com.hesham0_0.spaceship.ui.main;

import static java.lang.Math.max;
import static java.lang.Math.min;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.lifecycle.ViewModelProvider;
import android.animation.ObjectAnimator;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import com.badlogic.gdx.backends.android.AndroidFragmentApplication;
import com.hesham0_0.spaceship.R;

public class MainActivity extends AppCompatActivity implements AndroidFragmentApplication.Callbacks {

    private GameViewModel gameViewModel;
    private TextView scoreText;
    private ImageView pauseBtn;
    private ProgressBar health_bar;
    private int health = 100;
    private ConstraintLayout pauseLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        gameViewModel = new ViewModelProvider(this).get(GameViewModel.class);
        initUi();
        GameFragment gameFragment = new GameFragment();
        getSupportFragmentManager()
                .beginTransaction()
                .add(R.id.container, gameFragment)
                .addToBackStack("0")
                .commit();

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
        health_bar.setMax(100);
        decreaseHealthBar(health);
        setObserves();
    }

    private void setObserves() {
        gameViewModel.points.observe(this, points -> scoreText.setText(getResources().getString(R.string.score_game_text, points)));
        gameViewModel.healthDecrease.observe(this, points -> {
            health = min(100,max(0, health + points * 3));
            decreaseHealthBar(health);
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
}