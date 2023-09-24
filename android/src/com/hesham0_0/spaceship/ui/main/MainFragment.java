package com.hesham0_0.spaceship.ui.main;

import androidx.lifecycle.ViewModelProvider;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.badlogic.gdx.backends.android.AndroidApplication;
import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration;
import com.badlogic.gdx.backends.android.AndroidFragmentApplication;
import com.hesham0_0.spaceship.PointsUpdateListener;
import com.hesham0_0.spaceship.R;
import com.hesham0_0.spaceship.SpaceshipGame;

public class MainFragment  extends AndroidFragmentApplication {
    private SpaceshipGame game;
    private GameViewModel gameViewModel ;
    public static MainFragment newInstance() {
        return new MainFragment();
    }


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
        PointsUpdateListener pointsUpdateListener = points -> runOnUiThread(() -> gameViewModel.points.setValue(points));
        game= new SpaceshipGame(pointsUpdateListener);
        game.setSharedValues(1,2,0.5f,5,true);
//        SharedPreferencesManager prefs = SharedPreferencesManager.getInstance(requireContext());
//        game.setSharedValues(
//                prefs.getSpaceshipRockForceValue()
//                ,prefs.getSpaceshipBulletForceValue()
//                ,prefs.getSpaceshipExplosionAnimationValue()
//                ,prefs.getSpaceshipBulletsFrequencyValue()
//                ,prefs.getSpaceshipForceField()
//        );

        return initializeForView(game, config);

    }
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        gameViewModel.gameState.observe(this.getViewLifecycleOwner(),gameState -> {
            game.alterGameStatus(gameState!=GameState.PLAYING);
        });
        gameViewModel.allowToShoot.observe(this.getViewLifecycleOwner(),allowToShoot -> game.setAllowShooting(allowToShoot));
    }
}