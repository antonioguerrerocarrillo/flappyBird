package com.mygdx.game;


import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.TimeUtils;
import java.util.Iterator;

public class gameScreen implements Screen {
  private Texture imagenGota;
  private Texture imagenGota2;
   private Texture imagenCubo;
   private Texture imagen;
  // private Texture imagen;
   private Texture hola;
   private Sound sonicoCaidaGota;
   private Music musicaLluvia;
   private SpriteBatch batch, batchi;
   private OrthographicCamera camara, camaras;
   private Rectangle cubo;
   private Array<Rectangle> gotasLluvia;
   private long tiempoCaidaUltimaGota;
   boolean canJump = false;
   float xVelocity = 0;
   float yVelocity = 0;
   final float MAX_VELOCITY = 30f;
   final float GRAVITY = -2.5f;
   gotas game;
   int score = 0;

 
   private void creaGotaLluvia() {
      Rectangle gotaLluvia = new Rectangle();
      gotaLluvia.x = 800;
      gotaLluvia.y = MathUtils.random(-200, -50);
      gotaLluvia.width = 64;
      gotaLluvia.height = 300;
      gotasLluvia.add(gotaLluvia);
      
      Rectangle gotaLluvia2 = new Rectangle();
      gotaLluvia2.x = 800;
      gotaLluvia2.y = gotaLluvia.y + 550; 
      gotaLluvia2.width = 64;
      gotaLluvia2.height = 300;
      gotasLluvia.add(gotaLluvia2);
      tiempoCaidaUltimaGota = TimeUtils.nanoTime();
    }

  gameScreen(gotas game) {
       this.game = game;
      // carga las imágenes de las gotas de lluvia y del cubo, cada una de 64x64 píxeles
      
      imagen= new Texture(Gdx.files.internal("descarga.png"));
      imagenGota = new Texture(Gdx.files.internal("tronco.png"));
      imagenGota2 = new Texture(Gdx.files.internal("tronco2.png"));
      imagenCubo = new Texture(Gdx.files.internal("bucket.png"));

      // carga de sonido para la caída de la gota y la música de fondo
      sonicoCaidaGota = Gdx.audio.newSound(Gdx.files.internal("drop.mp3"));
      musicaLluvia = Gdx.audio.newMusic(Gdx.files.internal("rain.mp3"));

      // se aplica que la música se repita en bucle, comienza la reproducción de la música de fondo
      musicaLluvia.setLooping(true);
      musicaLluvia.play();

      // crea la cámara ortográfica y el lote de sprites
      camara = new OrthographicCamera();
      camara.setToOrtho(false, 800, 480);
      batch = new SpriteBatch();
      batchi = new SpriteBatch();
      // crea un rectángulo (clase Rectangle) para representar lógicamente el cubo
      cubo = new Rectangle();
      cubo.x = 220; // centra el cubo horizontal
      cubo.y = 800 / 2 - 64 / 2; // esquina inferior izquierda del cubo estará a 20 píxeles del límite inferior
      cubo.width = 150;
      cubo.height = 61;
      // crea el vector de gotas y crea la primera gota
      gotasLluvia = new Array<Rectangle>();
      creaGotaLluvia();
   }

   @Override
   public void render(float delta) {
      // limpia la pantalla con un color azul oscuro. Los argumentos RGB de la función glClearcColor están en el rango entre 0 y 1
      //game.batch.draw(imagen, 0, 0,800,480);
      Gdx.gl.glClearColor(0, 0, 0.2f, 1);
      Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

      // ordenada a la cámara actualizar sus matrices
      camara.update();

      // indica al lote de sprites que se represente en las coordenadas específicas de la cámara
      game.batch.setProjectionMatrix(camara.combined);

      
      // comienza un nuevo proceso y dibuja el cubo y las gotas
      game.batch.begin();
      game.batch.draw(imagen, 0, 0, 800,480);
      game.font.draw(game.batch, "Puntuación " + score, 0, 480);
      game.batch.draw(imagenCubo, cubo.x, cubo.y);
      int contador = 1;
      for(Rectangle gotaLluvia: gotasLluvia) {
         
         if(contador % 2 == 0){
             game.batch.draw(imagenGota2, gotaLluvia.x, gotaLluvia.y);
         } else {
             game.batch.draw(imagenGota, gotaLluvia.x, gotaLluvia.y);
         }
         
         contador++;
         
      }
      game.batch.end();
      
      

      // lectura de entrada
      if(Gdx.input.isTouched()) {
         Vector3 posicionTocada = new Vector3();
         posicionTocada.set(Gdx.input.getX(), Gdx.input.getY(), 0);
         camara.unproject(posicionTocada);
         cubo.y = posicionTocada.y - 64 / 2;
      }
       boolean upTouched = Gdx.input.isTouched() && Gdx.input.getY() < Gdx.graphics.getHeight() / 2;
       //caida
        cubo.y -= 650 * Gdx.graphics.getDeltaTime();
         if (Gdx.input.isKeyPressed(Input.Keys.UP) || upTouched) {
             //salto
               cubo.y +=  + 23;
        }
      // nos aseguramos de que el cubo permanezca entre los límites de la pantalla
      if(cubo.y < 0) cubo.y = 0;
      if(cubo.y > 480 - 64) cubo.y = 480 - 64;

      // comprueba si ha pasado un segundo desde la última gota, para crear una nueva
      if(TimeUtils.nanoTime() - tiempoCaidaUltimaGota + 1000000 > 700000000) creaGotaLluvia();

      // recorre las gotas y borra aquellas que hayan llegado al suelo (límite inferior de la pantalla) o toquen el cubo, en ese caso se reproduce sonido.
      Iterator<Rectangle> iter = gotasLluvia.iterator();
      Boolean segundo = false;
      while(iter.hasNext()) {
         Rectangle gotaLluvia = iter.next();
         gotaLluvia.x -= 400 * Gdx.graphics.getDeltaTime();
         if(segundo == false){
            if(gotaLluvia.x + 64 < 0){
             iter.remove();
             score = score + 1;
            } 
            segundo = true;
         } else {
            if(gotaLluvia.x + 64 < 0){
             iter.remove();
             segundo = false;
             
            }  
         }
             
        LooseScreen a = new LooseScreen(game);
         if(gotaLluvia.overlaps(cubo)) {
            sonicoCaidaGota.play();
            a.puntos(score);
            game.setScreen((Screen) a);
            dispose();
        }
      }
   }

   @Override
   public void dispose() {
      // liberamos todos los recursos
      imagenGota.dispose();
      imagenCubo.dispose();
      sonicoCaidaGota.dispose();
      musicaLluvia.dispose();
      
      
   }

    @Override
    public void show() {
        musicaLluvia.play();
    }

    @Override
    public void pause() {
   //     throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void resume() {
  //      throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void hide() {
//        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void resize(int i, int i1) {
//        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}