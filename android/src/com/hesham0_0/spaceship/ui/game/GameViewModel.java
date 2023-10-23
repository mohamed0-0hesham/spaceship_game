package com.hesham0_0.spaceship.ui.game;

import androidx.lifecycle.ViewModel;
import androidx.lifecycle.MutableLiveData;

public class GameViewModel extends ViewModel {
    public MutableLiveData<GameState> gameState = new MutableLiveData<>(GameState.PLAYING);
    public MutableLiveData<Integer> points = new MutableLiveData<>(0);
    public MutableLiveData<Integer> healthDecrease = new MutableLiveData<>(0);
}
