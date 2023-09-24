package com.hesham0_0.spaceship.ui.main;

import androidx.lifecycle.ViewModel;
import androidx.lifecycle.MutableLiveData;

public class GameViewModel extends ViewModel {
    public MutableLiveData<GameState> gameState = new MutableLiveData<>(GameState.PLAYING);
    public MutableLiveData<Integer> points = new MutableLiveData<>(0);
    public final MutableLiveData<Boolean> allowToShoot = new MutableLiveData<>(true);
    public void setGameState(GameState gameState) {
        this.gameState.setValue(gameState);
    }
}
