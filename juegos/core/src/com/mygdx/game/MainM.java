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

public class MainM implements Screen {
        private Texture  imagenInicio;
        final gotas game;
	OrthographicCamera camera;
        
	public MainM(final gotas gam) {
		game = gam;
                imagenInicio = new Texture(Gdx.files.internal("fondoInicio.jpg"));
		camera = new OrthographicCamera();
		camera.setToOrtho(false, 800, 480);

	}

	@Override
	public void render(float delta) {
		Gdx.gl.glClearColor(0, 0, 0.2f, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		camera.update();
		game.batch.setProjectionMatrix(camera.combined);

		game.batch.begin();
                game.batch.draw(imagenInicio, 0, 0, 800,480);
		game.font.draw(game.batch, "hola este es el super juego!! ", 300, 350);
		game.font.draw(game.batch, "Flappy Birds!", 300, 300);
                game.font.draw(game.batch, "Toca para comenzar ", 300, 300);
		game.batch.end();

		if (Gdx.input.isTouched()) {
			game.setScreen( new gameScreen(game));
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