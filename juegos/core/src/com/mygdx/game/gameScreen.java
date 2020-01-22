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
  private Texture imagentubo;
  private Texture imagentubo2;
   private Texture imagenCubo;
   private Texture imagen;
   private Sound sonidocaida;
   private Music musica;
   private SpriteBatch batch, batchi;
   private OrthographicCamera camara, camaras;
   private Rectangle pajaro;
   private Array<Rectangle> tubos;
   private long tiempoTubo;
   boolean canJump = false;
   float xVelocity = 0;
   float yVelocity = 0;
   final float MAX_VELOCITY = 30f;
   final float GRAVITY = -2.5f;
   gotas game;
   int score = 0;

 
   private void crearTubo() {
      Rectangle gotaLluvia = new Rectangle();
      gotaLluvia.x = 800;
      gotaLluvia.y = MathUtils.random(-200, -50);
      gotaLluvia.width = 64;
      gotaLluvia.height = 300;
      tubos.add(gotaLluvia);
      
      Rectangle gotaLluvia2 = new Rectangle();
      gotaLluvia2.x = 800;
      gotaLluvia2.y = gotaLluvia.y + 550; 
      gotaLluvia2.width = 64;
      gotaLluvia2.height = 300;
      tubos.add(gotaLluvia2);
      tiempoTubo = TimeUtils.nanoTime();
    }

  gameScreen(gotas game) {
       this.game = game;
      // carga las imágenes de las gotas de lluvia y del pajaro, cada una de 64x64 píxeles
      
      imagen= new Texture(Gdx.files.internal("descarga.png"));
      imagentubo = new Texture(Gdx.files.internal("tronco.png"));
      imagentubo2 = new Texture(Gdx.files.internal("tronco2.png"));
      imagenCubo = new Texture(Gdx.files.internal("bucket.png"));

      // carga de sonido para la caída de la gota y la música de fondo
      sonidocaida = Gdx.audio.newSound(Gdx.files.internal("drop.mp3"));
      musica = Gdx.audio.newMusic(Gdx.files.internal("rain.mp3"));

      // se aplica que la música se repita en bucle, comienza la reproducción de la música de fondo
      musica.setLooping(true);
      musica.play();

      // crea la cámara ortográfica y el lote de sprites
      camara = new OrthographicCamera();
      camara.setToOrtho(false, 800, 480);
      batch = new SpriteBatch();
      batchi = new SpriteBatch();
      // crea un rectángulo (clase Rectangle) para representar lógicamente el pajaro
      pajaro = new Rectangle();
      pajaro.x = 220; // centra el pajaro horizontal
      pajaro.y = 800 / 2 - 64 / 2; // esquina inferior izquierda del pajaro estará a 20 píxeles del límite inferior
      pajaro.width = 150;
      pajaro.height = 61;
      // crea el vector de gotas y crea la primera gota
      tubos = new Array<Rectangle>();
      crearTubo();
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

      
      // comienza un nuevo proceso y dibuja el pajaro y las gotas
      game.batch.begin();
      game.batch.draw(imagen, 0, 0, 800,480);
      game.font.draw(game.batch, "Puntuación " + score, 0, 480);
      game.batch.draw(imagenCubo, pajaro.x, pajaro.y);
      int contador = 1;
      for(Rectangle gotaLluvia: tubos) {
         
         if(contador % 2 == 0){
             game.batch.draw(imagentubo2, gotaLluvia.x, gotaLluvia.y);
         } else {
             game.batch.draw(imagentubo, gotaLluvia.x, gotaLluvia.y);
         }
         
         contador++;
         
      }
      game.batch.end();
      
      

      // lectura de entrada
      if(Gdx.input.isTouched()) {
         Vector3 posicionTocada = new Vector3();
         posicionTocada.set(Gdx.input.getX(), Gdx.input.getY(), 0);
         camara.unproject(posicionTocada);
         pajaro.y = posicionTocada.y - 64 / 2;
      }
       boolean upTouched = Gdx.input.isTouched() && Gdx.input.getY() < Gdx.graphics.getHeight() / 2;
       //caida
        pajaro.y -= 650 * Gdx.graphics.getDeltaTime();
         if (Gdx.input.isKeyPressed(Input.Keys.UP) || upTouched) {
             //salto
               pajaro.y +=  + 23;
        }
      // nos aseguramos de que el pajaro permanezca entre los límites de la pantalla
      if(pajaro.y < 0) pajaro.y = 0;
      if(pajaro.y > 480 - 64) pajaro.y = 480 - 64;

      // comprueba si ha pasado un segundo desde la última gota, para crear una nueva
      if(TimeUtils.nanoTime() - tiempoTubo + 1000000 > 700000000) crearTubo();

      // recorre las gotas y borra aquellas que hayan llegado al suelo (límite inferior de la pantalla) o toquen el pajaro, en ese caso se reproduce sonido.
      Iterator<Rectangle> iter = tubos.iterator();
      Boolean segundo = false;
      while(iter.hasNext()) {
         Rectangle tubos = iter.next();
         tubos.x -= 400 * Gdx.graphics.getDeltaTime();
         if(segundo == false){
            if(tubos.x + 64 < 0){
             iter.remove();
             score = score + 1;
            } 
            segundo = true;
         } else {
            if(tubos.x + 64 < 0){
             iter.remove();
             segundo = false;
             
            }  
         }
             
        LooseScreen a = new LooseScreen(game);
         if(tubos.overlaps(pajaro)) {
            sonidocaida.play();
            a.puntos(score);
            game.setScreen((Screen) a);
            dispose();
        }
      }
   }

   @Override
   public void dispose() {
      // liberamos todos los recursos
      imagentubo.dispose();
      imagenCubo.dispose();
      sonidocaida.dispose();
      musica.dispose();
      
      
   }

    @Override
    public void show() {
        musica.play();
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