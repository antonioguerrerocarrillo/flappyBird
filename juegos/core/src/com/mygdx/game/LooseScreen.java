/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mygdx.game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;

public class LooseScreen implements Screen {
        private Texture  imagenFinal;
        final gotas game;
	OrthographicCamera camera;
        int score;
	public LooseScreen(final gotas gam) {
		game = gam;
		camera = new OrthographicCamera();
		camera.setToOrtho(false, 800, 480);

	}
        public LooseScreen() {
                game = null;
		camera = new OrthographicCamera();
		camera.setToOrtho(false, 800, 480);

	}

       public void puntos(int puntos){
          
            this.score = puntos;
             //System.out.println("puntos aqui " + puntos + " score " + score);
       }
        @Override
	public void render(float delta) {
		Gdx.gl.glClearColor(0, 0, 0.2f, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
                imagenFinal = new Texture(Gdx.files.internal("fondoFinal.jpg"));
                //System.out.println("puntos "+ score);
		camera.update();
		game.batch.setProjectionMatrix(camera.combined);

		game.batch.begin();
                 game.batch.draw(imagenFinal, 0, 0, 800,480);
		game.font.draw(game.batch, "bien has optenido!! ", 300, 350);
		game.font.draw(game.batch, "una puntuacion total! ", 300, 300);
                game.font.draw(game.batch, "de = " + score + "!! ", 300, 250);
		game.batch.end();

		if (Gdx.input.isTouched()) {
                    game.setScreen((Screen) new gameScreen(game));
                    dispose();
		}
	}

	@Override
	public void resize(int width, int height) {
	}

	@Override
	public void show() {
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
	}

 

}