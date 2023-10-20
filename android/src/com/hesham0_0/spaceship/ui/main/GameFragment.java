package com.hesham0_0.spaceship.ui.main;

import androidx.lifecycle.ViewModelProvider;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration;
import com.badlogic.gdx.backends.android.AndroidFragmentApplication;
import com.hesham0_0.spaceship.PointsUpdateListener;
import com.hesham0_0.spaceship.SpaceshipGame;

public class GameFragment extends AndroidFragmentApplication {
    private SpaceshipGame game;
    private GameViewModel gameViewModel ;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        gameViewModel = new ViewModelProvider(requireActivity()).get(GameViewModel.class);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        gameViewModel = new ViewModelProvider(requireActivity()).get(GameViewModel.class);
        // Inflate the layout for this fragment
        AndroidApplicationConfiguration config = new AndroidApplicationConfiguration();
        PointsUpdateListener pointsUpdateListener = new PointsUpdateListener() {
            @Override
            public void onPointsUpdated(int points) {
                runOnUiThread(() -> gameViewModel.points.setValue(points));
            }

            @Override
            public void onHealthChanges(int points) {
                runOnUiThread(() -> gameViewModel.healthDecrease.setValue(points));
            }
        };
        game = new SpaceshipGame(pointsUpdateListener);

        return initializeForView(game, config);

    }
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        gameViewModel.gameState.observe(this.getViewLifecycleOwner(),gameState -> game.alterGameStatus(gameState!=GameState.PLAYING));

    }
}