package actor;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;

public class Explosion {

    //como libgdx tiene una clase de animacion,vamos a crear una animacion privada de una textura de region

    private Animation<TextureRegion>explosionAnimation;
    // variable de tipo float que hara de temporizador de nuestra animacion explosion
    private float explosionTimer;
    //creamos un rectangulo privado que sera el cuadro delimitador de nuestra animación,en verdad copiamos el rectangulo delimitador de la nave
    //para que tengan el mismo tamaño
    private Rectangle boundingBox;
    //creamos un constructor

    public Explosion(Texture texture, Rectangle boundingBox, float totalAnimacionTimer){
        this.boundingBox = boundingBox;

        //ahora la textura de la explosion tendremos que dividirla para hacerla mas pequeña,luego la convertiremos en una array para usar
        //nuestra animacion,hay que darle un ancho y un alto,yo en particular le he dado 64 pixeles;
        //si hacemos 64 x 4 nos da un total de 256 pixeles que seria el ancho de mi imagen,ya que la hemos dividido en cuatro secciones
        //el metodo split convierte un array bidemensional en unidimensional, toda esta informacion esta consultada en internet

        TextureRegion[][] textureRegions2D = TextureRegion.split(texture,64,64);

        //convertimos a una array
        TextureRegion[]textureRegions1D = new TextureRegion[16];
        //declaramos una variable int,que hara la función de indice para realizar un seguimiento
        int index = 0;
        for(int i = 0; i < 4;i++){
            for (int j = 0; j < 4;j++){
                textureRegions1D[index] = textureRegions2D[i][j];
                index++;
            }
        }
        //ahora introducimos nuestra array unidimensional al objeto de animacion
        explosionAnimation = new Animation<TextureRegion>(totalAnimacionTimer/16,textureRegions1D);
        explosionTimer = 0;
    }
    //metodo de actualizacion,que coge el tiempom de Delta, e incrementara el temporizador de la explosion como
    //ya lo hicimos con los laseres

    public void update(float deltaTime){
        explosionTimer += deltaTime;
    }

    //metodo de dibujo, que toma el conjunto de sprites,y dibujara el recuadro correcto de la animacion

    public void draw(SpriteBatch batch){
        batch.draw(explosionAnimation.getKeyFrame(explosionTimer),
                boundingBox.x,
                boundingBox.y,
                boundingBox.width,
                boundingBox.height);
    }
    //metodo booleano que se encargara de pregunarle a la animacion si ya terminó
    public boolean isFinished(){
        return explosionAnimation.isAnimationFinished(explosionTimer);
    }

}
