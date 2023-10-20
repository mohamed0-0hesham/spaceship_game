package com.hesham0_0.spaceship.ui;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.hesham0_0.spaceship.R;
import com.hesham0_0.spaceship.ui.main.GameViewModel;

public class GameOverFragment extends Fragment {

    private GameViewModel gameViewModel;
    private TextView scoreText;
    private TextView playBtn;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        gameViewModel = new ViewModelProvider(requireActivity()).get(GameViewModel.class);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_game_over, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        scoreText = view.findViewById(R.id.score_View);
        scoreText.setText(getResources().getString(R.string.game_over_score, gameViewModel.points.getValue()));
        playBtn = view.findViewById(R.id.play_btn);
        playBtn.setOnClickListener(v -> gameViewModel.currentScreen.setValue(1));
    }
}