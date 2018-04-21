package com.sbsgames.flappybird;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import java.util.Random;

public class FlappyBird extends ApplicationAdapter {
	private SpriteBatch batch;
	private Texture background;
	private Texture[] birds;
	private Texture[] tubes;

	private int flapState = 0, flapCount = 0;
	private final int FLAP_TIME = 500, GRAVITY = 2;
	private float birdY, velocity = 0, gap = 400, gapHalf = gap / 2;

	private float maxTubeOffset, tubeVelocity = 4;
	private int numberOfTubes = 4;
	private float[] tubeX = new float[numberOfTubes], tubeOffset = new float[numberOfTubes];

	private Random random;
	private int gameState = 0;
	private float distanceBetweenTubes;
	
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

		maxTubeOffset = (Gdx.graphics.getHeight() / 2) - gapHalf - 100;
		distanceBetweenTubes = Gdx.graphics.getWidth() * .75f;

		random = new Random();

		for (int i = 0; i <numberOfTubes; i++) {
			tubeOffset[i] = (random.nextFloat() - 0.5f) * (Gdx.graphics.getHeight() - gap - gapHalf);
			tubeX[i] = (Gdx.graphics.getWidth() / 2) - (tubes[0].getWidth() / 2) + i * distanceBetweenTubes;
		}

	}

	@Override
	public void render () {

		batch.begin();
		batch.draw(background, 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());  // display background in full screen bottom layer

		if(gameState != 0) {

			if (Gdx.input.justTouched()) {
				velocity = -30;
			}


			//  the next layer
			for (int i = 0; i <numberOfTubes; i++) {

				if(tubeX[i] < -tubes[0].getWidth()) {
					tubeX[i] += numberOfTubes * distanceBetweenTubes;
				} else {
					tubeX[i] -= tubeVelocity;
				}

				batch.draw(tubes[0], tubeX[i], (Gdx.graphics.getHeight() / 2) + gapHalf + tubeOffset[i]);
				batch.draw(tubes[1], tubeX[i], (Gdx.graphics.getHeight() / 2) - gapHalf - tubes[1].getHeight() + tubeOffset[i]);
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

		batch.draw(birds[flapState], (Gdx.graphics.getWidth() / 2) - (birds[flapState].getWidth() / 2), birdY);
		batch.end();
	}
	
	@Override
	public void dispose () {
		batch.dispose();
		background.dispose();
	}
}
