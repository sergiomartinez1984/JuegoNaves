package actor;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;

abstract public class Ship {

    //caracteristicas de la nave
    //utilizamos medidas del mundo * segundo,en metros,asi no tenemos que preocuparnos por el tamaño de la pantalla para los calculos
    //tambien utilizo una varieable int escudo para saber cuanta cantidad de escudo le  queda a la nave

    float movementSpeed;
    int shield;

    //posicion y dimension de la nave
    //ancho y alto de la nave, y la posicion de la nave con las coordenadas X e Y
    Rectangle boundingBox;

    //informacion del Laser
    float laserWidth, laserHeight;
    float laserMovementSpeed;
    float timeBetweenShot;
    float timeSinceLastShop = 0;


    //graficos
    //necesitamos dos regiones de textura,una para la nave y otra para el escudo
    TextureRegion shipTextureRegion, shieldTextureRegion, laserTextureRegion;

    public Ship(float xCentre, float yCentre,
                float width, float height,
                float movementSpeed, int shield,
                float laserWidth,float laserHeight,float laserMovementSpeed,
                float timeBetweenShot,
                TextureRegion shipTextureRegion, TextureRegion shieldTextureRegion, TextureRegion laserTextureRegion) {
        this.movementSpeed = movementSpeed;
        this.shield = shield;

        this.boundingBox = new Rectangle(xCentre - width/2,yCentre - height/2,width,height);
        this.laserWidth = laserWidth;
        this.laserHeight = laserHeight;
        this.laserMovementSpeed = laserMovementSpeed;
        this.timeBetweenShot = timeBetweenShot;
        this.shipTextureRegion = shipTextureRegion;
        this.shieldTextureRegion = shieldTextureRegion;
        this.laserTextureRegion = laserTextureRegion;
    }

    public void update(float deltaTime){
        timeSinceLastShop += deltaTime;
    }

    public boolean canFireLaser(){
      return  (timeSinceLastShop - timeBetweenShot >=0);
    }

    public abstract Laser[]fireLaser();

    public Boolean intersects(Rectangle otherRectangle){

        return boundingBox.overlaps(otherRectangle);
    }

    public void hit(Laser laser){
        if (shield > 0){
            shield --;
        }
    }


    public void draw(Batch batch){
        batch.draw(shipTextureRegion,boundingBox.x,boundingBox.y,boundingBox.width,boundingBox.height);
        if (shield > 0){
            batch.draw(shieldTextureRegion,boundingBox.x,boundingBox.y,boundingBox.width,boundingBox.height);
        }
    }

}
