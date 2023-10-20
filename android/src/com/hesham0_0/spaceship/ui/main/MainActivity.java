package com.hesham0_0.spaceship.ui.main;

import static java.lang.Math.max;
import static java.lang.Math.min;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;
import android.animation.ObjectAnimator;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import com.badlogic.gdx.backends.android.AndroidFragmentApplication;
import com.hesham0_0.spaceship.R;
import com.hesham0_0.spaceship.ui.GameOverFragment;

public class MainActivity extends AppCompatActivity implements AndroidFragmentApplication.Callbacks {
    private GameViewModel gameViewModel;
    private TextView scoreText;
    private ImageView pauseBtn;
    private ProgressBar health_bar;
    private int health = 100;
    private GameFragment gameFragment;
    private GameOverFragment gameOverFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        gameViewModel = new ViewModelProvider(this).get(GameViewModel.class);
        initUi();
        gameFragment = new GameFragment();
        gameOverFragment = new GameOverFragment();
        getSupportFragmentManager()
                .beginTransaction()
                .add(R.id.container, gameFragment)
                .addToBackStack("0")
                .commit();
    }
    private void navToFrag(Fragment fragment){
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.container, fragment);
        transaction.addToBackStack(null); // Optional
        transaction.commit();
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
//        gameViewModel.currentScreen.observe(this,screen->{
//            switch (screen){
//                case 1:
//                    navToFrag(gameFragment);
//                    break;
//                case 2:
//                    navToFrag(gameOverFragment);
//                    break;
//            }
//        });
        gameViewModel.healthDecrease.observe(this, points -> {
            health = min(100,max(0, health + points * 3));
            decreaseHealthBar(health);
//            if (health == 0) {
//                gameViewModel.gameState.setValue(GameState.STOPPED);
//                gameViewModel.currentScreen.setValue(2);
//            }
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