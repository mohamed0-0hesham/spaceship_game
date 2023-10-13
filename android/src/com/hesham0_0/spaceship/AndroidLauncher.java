package com.hesham0_0.spaceship;

import android.os.Bundle;
import android.util.Log;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.backends.android.AndroidApplication;
import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration;


public class AndroidLauncher extends AndroidApplication {
//	private GameViewModel gameViewModel ;
	@Override
	protected void onCreate (Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		AndroidApplicationConfiguration config = new AndroidApplicationConfiguration();
		PointsUpdateListener pointsUpdateListener = points -> runOnUiThread(() -> Log.d("points","points = "+points));
		SpaceshipGame game=new SpaceshipGame(pointsUpdateListener);
		initialize(game, config);
	}
}
