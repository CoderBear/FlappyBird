package com.sbsgames.flappybird;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class FlappyBird extends ApplicationAdapter {
	SpriteBatch batch;
	Texture background;
	Texture[] birds;
	Texture[] tubes;

	int flapState = 0;
	
	@Override
	public void create () {
		batch = new SpriteBatch();
		background = new Texture("bg.png");
		birds = new Texture[2];
		birds[0] = new Texture("bird.png");
		birds[1] = new Texture("bird2.png");

		tubes = new Texture[2];
		tubes[0] = new Texture("toptube.png");
		tubes[1] = new Texture("bottomtube.png");

	}

	@Override
	public void render () {

		if(flapState == 0) {
			flapState = 1;
		} else {
			flapState = 0;
		}

		batch.begin();
		batch.draw(background, 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());  // display background in full screen
		batch.draw(birds[flapState], (Gdx.graphics.getWidth() / 2) - (birds[flapState].getWidth() / 2), (Gdx.graphics.getHeight() / 2) - (birds[flapState].getHeight() / 2));
		batch.end();
	}
	
	@Override
	public void dispose () {
		batch.dispose();
		background.dispose();
	}
}