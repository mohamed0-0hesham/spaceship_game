package com.hesham0_0.spaceship;

import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.backends.android.AndroidApplication;
import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration;
import com.hesham0_0.spaceship.models.Bullet;
import com.hesham0_0.spaceship.models.Rock;
import com.hesham0_0.spaceship.models.Spaceship;

public class AndroidLauncher extends AndroidApplication {
//	private GameViewModel gameViewModel ;
	@Override
	protected void onCreate (Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		AndroidApplicationConfiguration config = new AndroidApplicationConfiguration();
		PointsUpdateListener pointsUpdateListener = points -> runOnUiThread(() -> Log.d("points","points = "+points));
		ApplicationAdapter game=new Main(pointsUpdateListener);
		initialize(game, config);
	}
}
