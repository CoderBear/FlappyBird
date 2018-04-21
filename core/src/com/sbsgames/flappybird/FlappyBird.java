package com.sbsgames.flappybird;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class FlappyBird extends ApplicationAdapter {
	private SpriteBatch batch;
	private Texture background;
	private Texture[] birds;
	private Texture[] tubes;

	private int flapState = 0, flapCount = 0;
	private final int FLAP_TIME = 500, GRAVITY = 2;
	private float birdY, velocity = 0;

	private int gameState = 0;
	
	@Override
	public void create () {
		batch = new SpriteBatch();
		background = new Texture("bg.png");
		birds = new Texture[2];
		birds[0] = new Texture("bird.png");
		birds[1] = new Texture("bird2.png");
		birdY = (Gdx.graphics.getHeight() / 2) - (birds[0].getHeight() / 2);

		tubes = new Texture[2];
		tubes[0] = new Texture("toptube.png");
		tubes[1] = new Texture("bottomtube.png");

	}

	@Override
	public void render () {

		if(gameState != 0) {

			if (Gdx.input.justTouched()) {
				velocity = -30;
			}

			if(birdY > 0 || velocity < 0) {
				velocity += GRAVITY;
				birdY -= velocity;
			}

		} else {
			if (Gdx.input.justTouched()) {
				gameState = 1;
			}
		}
		if (flapCount < FLAP_TIME) {
			flapCount++;
		} else {
			if (flapState == 0) {
				flapState = 1;
			} else {
				flapState = 0;
			}
		}

		batch.begin();
		batch.draw(background, 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());  // display background in full screen
		batch.draw(birds[flapState], (Gdx.graphics.getWidth() / 2) - (birds[flapState].getWidth() / 2), birdY);
		batch.end();
	}
	
	@Override
	public void dispose () {
		batch.dispose();
		background.dispose();
	}
}
