package actor;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;

public class Laser {

    //posicion y dimensiones
    public Rectangle  boundingBox;

    //Caracteristicas fisicas del Laser
    //unidades del mundo por segundo como hicimos en la nave
    public float movementSpeed;




    //Graficos
    TextureRegion textureRegion;


    public Laser(float xCentre, float ybottom,
                 float width, float height,
                 float movementSpeed, TextureRegion textureRegion) {
        this.boundingBox = new Rectangle(xCentre - width/2,ybottom ,width,height);
        this.movementSpeed = movementSpeed;
        this.textureRegion = textureRegion;
    }

    public void draw(Batch batch){
        batch.draw(textureRegion,boundingBox.x,boundingBox.y,boundingBox.width,boundingBox.height);
    }

  //  public Rectangle getBoundingBox(){
  //      return boundingBox;
  //  }

}
