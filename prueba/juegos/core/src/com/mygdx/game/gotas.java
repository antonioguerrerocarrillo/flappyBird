package com.mygdx.game;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.TimeUtils;

public class gotas extends Game {

  	SpriteBatch batch;
	BitmapFont font;

         
         @Override
	public void create() {
		batch = new SpriteBatch();
		// Use LibGDX's default Arial font.
		font = new BitmapFont();
		this.setScreen(new MainM(this));
	}

          @Override
	public void render() {
		super.render(); // important!
	}

          @Override
	public void dispose() {
		batch.dispose();
		font.dispose();
	}
     
}