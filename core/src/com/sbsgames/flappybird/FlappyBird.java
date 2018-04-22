package com.sbsgames.flappybird;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Rectangle;

import java.util.Random;

public class FlappyBird extends ApplicationAdapter {
	private SpriteBatch batch;
	private Texture background;
	private Texture gameOverScreen;
	private Texture[] birds;
	private Texture[] tubes;
//	private ShapeRenderer shapeRenderer;

	private int flapState = 0, flapCount = 0;
	private final int FLAP_TIME = 500, GRAVITY = 2;
	private float birdY, velocity = 0, gap = 400, gapHalf = gap / 2;

	private Circle birdCircle = new Circle();
	private float maxTubeOffset, tubeVelocity = 4;

	private int numberOfTubes = 4;
	private float[] tubeX = new float[numberOfTubes], tubeOffset = new float[numberOfTubes];
	private Rectangle[] tubeTopRect = new Rectangle[numberOfTubes];
	private Rectangle[] tubeBottomRect = new Rectangle[numberOfTubes];

	private Random random;
	private int gameState = 0;
	private float distanceBetweenTubes;

	private int score = 0;
	private int scoringTube = 0;
	BitmapFont font;
	
	@Override
	public void create () {
		batch = new SpriteBatch();

		font = new BitmapFont();
		font.setColor(Color.WHITE);
		font.getData().setScale(10f);

		background = new Texture("bg.png");
		gameOverScreen = new Texture("flappybird_fulmer.png");
		birds = new Texture[2];
		birds[0] = new Texture("bird.png");
		birds[1] = new Texture("bird2.png");

//		shapeRenderer = new ShapeRenderer();

		tubes = new Texture[2];
		tubes[0] = new Texture("toptube.png");
		tubes[1] = new Texture("bottomtube.png");

		maxTubeOffset = (Gdx.graphics.getHeight() / 2) - gapHalf - 100;
		distanceBetweenTubes = Gdx.graphics.getWidth() * .75f;

		random = new Random();

		startGame();
	}

	public void startGame() {
        birdY = (Gdx.graphics.getHeight() / 2) - (birds[0].getHeight() / 2);

        for (int i = 0; i <numberOfTubes; i++) {
            tubeOffset[i] = (random.nextFloat() - 0.5f) * (Gdx.graphics.getHeight() - gap - gapHalf);
            tubeX[i] = (Gdx.graphics.getWidth() / 2) - (tubes[0].getWidth() / 2) + Gdx.graphics.getWidth() + i * distanceBetweenTubes;
            tubeTopRect[i] = new Rectangle();
            tubeBottomRect[i] = new Rectangle();
        }
    }

	@Override
	public void render () {

		batch.begin();
		batch.draw(background, 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());  // display background in full screen bottom layer

		if(gameState == 1) {

			if (Gdx.input.justTouched()) {
				velocity = -30;
			}


			//  the next layer
			for (int i = 0; i <numberOfTubes; i++) {

				if(tubeX[i] < -tubes[0].getWidth()) {
					tubeX[i] += numberOfTubes * distanceBetweenTubes;
					tubeOffset[i] = (random.nextFloat() - 0.5f) * (Gdx.graphics.getHeight() - gap - gapHalf);
				} else {
					tubeX[i] -= tubeVelocity;

					if(tubeX[scoringTube] < (Gdx.graphics.getWidth() / 2) ) {
						score++;
						if(scoringTube < (numberOfTubes - 1)) {
							scoringTube++;
						} else {
							scoringTube = 0;
						}
					}
				}

				batch.draw(tubes[0], tubeX[i], (Gdx.graphics.getHeight() / 2) + gapHalf + tubeOffset[i]);
				batch.draw(tubes[1], tubeX[i], (Gdx.graphics.getHeight() / 2) - gapHalf - tubes[1].getHeight() + tubeOffset[i]);

				tubeTopRect[i].set(tubeX[i], (Gdx.graphics.getHeight() / 2) + gapHalf + tubeOffset[i], tubes[0].getWidth(), tubes[0].getHeight());
				tubeBottomRect[i].set(tubeX[i], (Gdx.graphics.getHeight() / 2) - gapHalf - tubes[1].getHeight() + tubeOffset[i], tubes[1].getWidth(), tubes[1].getHeight());
			}

			if(birdY > 0) {
				velocity += GRAVITY;
				birdY -= velocity;
			} else {
			    gameState = 2;
            }

		} else if (gameState == 0) {
			if (Gdx.input.justTouched()) {
				gameState = 1;
			}
		} else if(gameState == 2) {
		    batch.draw(gameOverScreen, (Gdx.graphics.getWidth() / 2) - (gameOverScreen.getWidth() / 2), (Gdx.graphics.getHeight() / 2) - (gameOverScreen.getHeight() / 2));

		    if(Gdx.input.justTouched()) {
		        gameState = 1;
		        startGame();
		        score = 0;
		        scoringTube = 0;
		        velocity = 0;
		        
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
		font.draw(batch, String.valueOf(score), 100, 200);
		batch.end();

		birdCircle.set(Gdx.graphics.getWidth() / 2, birdY + (birds[flapState].getHeight() / 2), birds[flapState].getWidth() / 2);

//		shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
//		shapeRenderer.setColor(Color.RED);
//		shapeRenderer.circle(birdCircle.x, birdCircle.y, birdCircle.radius);
		for (int i = 0; i <numberOfTubes; i++) {
//			shapeRenderer.rect(tubeX[i], (Gdx.graphics.getHeight() / 2) + gapHalf + tubeOffset[i], tubes[0].getWidth(), tubes[0].getHeight());
//			shapeRenderer.rect(tubeX[i], (Gdx.graphics.getHeight() / 2) - gapHalf - tubes[1].getHeight() + tubeOffset[i], tubes[1].getWidth(), tubes[1].getHeight());

			if (Intersector.overlaps(birdCircle, tubeTopRect[i]) || Intersector.overlaps(birdCircle, tubeBottomRect[i])) {
				Gdx.app.log("Collision", "Yes!");
				gameState = 2;
			}
		}
//		shapeRenderer.end();
	}
	
	@Override
	public void dispose () {
		batch.dispose();
		background.dispose();
	}
}
