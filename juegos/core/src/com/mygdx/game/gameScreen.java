/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mygdx.game;

/**
 *
 * @author Infante96
 */
import java.util.Iterator;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.GL20;
//import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.TimeUtils;

public class gameScreen implements Screen {
    
  	final gotas game;

	Texture dropImageup;
        Texture dropImagendown;
        
	Texture bucketImage;
	Sound dropSound;
	Music rainMusic;
	OrthographicCamera camera;
	Rectangle bucket;
	Array<Rectangle> raindrops;
	long lastDropTime;
	int dropsGathered;
        

	public gameScreen(final gotas gam) {
            
		this.game = gam;

		// load the images for the droplet and the bucket, 64x64 pixels each
		dropImagendown = new Texture(Gdx.files.internal("columnadown.png"));
                dropImageup = new Texture(Gdx.files.internal("columnaup.png"));
                
		bucketImage = new Texture(Gdx.files.internal("bucket2.png"));

		// load the drop sound effect and the rain background "music"
		dropSound = Gdx.audio.newSound(Gdx.files.internal("drop.wav"));
		rainMusic = Gdx.audio.newMusic(Gdx.files.internal("rain.mp3"));
		rainMusic.setLooping(true);

		// create the camera and the SpriteBatch
		camera = new OrthographicCamera();
		camera.setToOrtho(false, 800, 480);

		bucket = new Rectangle();
		bucket.x = 0;
		bucket.y = 260; 
				
		bucket.width = 64;
		bucket.height = 64;
             
		// create the raindrops array and spawn the first raindrop
		raindrops = new Array<Rectangle>();
		spawnRaindrop();

	}

	private void spawnRaindrop() {
		Rectangle raindrop = new Rectangle();
		raindrop.x = MathUtils.random(0, 800 - 64);
		//posicion de la gota en salida
                raindrop.y = 480;
		raindrop.width = 45;
		raindrop.height = 45;
		raindrops.add(raindrop);
		lastDropTime = TimeUtils.nanoTime();
	}

	@Override
	public void render(float delta) {
		// clear the screen with a dark blue color. The
		// arguments to glClearColor are the red, green
		// blue and alpha component in the range [0,1]
		// of the color to be used to clear the screen.
		Gdx.gl.glClearColor(0, 0, 0.2f, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		// tell the camera to update its matrices.
		camera.update();

		// tell the SpriteBatch to render in the
		// coordinate system specified by the camera.
		game.batch.setProjectionMatrix(camera.combined);

		// begin a new batch and draw the bucket and
		// all drops
		game.batch.begin();
		game.font.draw(game.batch, "Drops Collected: " + dropsGathered, 0, 480);
		game.batch.draw(bucketImage, bucket.x, bucket.y);
		for (Rectangle raindrop : raindrops) {
                    //gotas horizontal
			game.batch.draw(dropImagendown, raindrop.y,0);
		}
                for (Rectangle raindrop : raindrops) {
                    //gotas horizontal
			game.batch.draw(dropImageup,480 , 480);
		}
		game.batch.end();
                boolean upTouched = Gdx.input.isTouched() && Gdx.input.getY() < Gdx.graphics.getHeight() / 2;
                bucket.y -= 350 * Gdx.graphics.getDeltaTime();
		// process user input
		if (Gdx.input.isTouched()) {
			Vector3 touchPos = new Vector3();
			touchPos.set(Gdx.input.getX(), Gdx.input.getY(), 0);
			camera.unproject(touchPos);
			bucket.y = touchPos.y - 64 / 2;
		}
               
                
                 if (Gdx.input.isKeyPressed(Input.Keys.UP) || upTouched) {
                        bucket.y += 23;
                }   
                if(bucket.y < 0) bucket.y = 0;
                if(bucket.y < 0) bucket.y = 0;
                if(bucket.y > 480 - 64) bucket.y = 480 - 64;
		// check if we need to create a new raindrop
		if (TimeUtils.nanoTime() - lastDropTime > 1000000000)
			spawnRaindrop();

                
                if(bucket.y <= 0){
                    
                }
		// move the raindrops, remove any that are beneath the bottom edge of
		// the screen or that hit the bucket. In the later case we play back
		// a sound effect as well.
		Iterator<Rectangle> iter = raindrops.iterator();
		while (iter.hasNext()) {
                    Rectangle raindrop = iter.next();
                        if(dropsGathered < 10){
                            raindrop.y -= 500 * Gdx.graphics.getDeltaTime(); 
                        } 
                        if (dropsGathered < 20 && dropsGathered >= 10){
                            raindrop.y -= 600 * Gdx.graphics.getDeltaTime(); 
                        } 
                        if (dropsGathered >= 20){
                            raindrop.y -= 700 * Gdx.graphics.getDeltaTime(); 
                        }
			if (raindrop.y + 64 < 0)
				iter.remove();
                        
                        //cuando choc
                        if(bucket.y >= raindrop.y && bucket.x >= raindrop.x ){
                                dropsGathered++;
                        
                        }
			/*if (raindrop.overlaps(bucket)) {
				dropsGathered++;
				dropSound.play();
                                gotas g = new gotas();
				iter.remove();
			}*/
		}
	}

	@Override
	public void resize(int width, int height) {
	}

	@Override
	public void show() {
		// start the playback of the background music
		// when the screen is shown
		rainMusic.play();
	}

	@Override
	public void hide() {
	}

	@Override
	public void pause() {
	}

	@Override
	public void resume() {
	}

	@Override
	public void dispose() {
		dropImagendown.dispose();
		bucketImage.dispose();
		dropSound.dispose();
		rainMusic.dispose();
	}

}
