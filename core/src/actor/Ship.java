package actor;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class Ship {

    //caracteristicas de la nave
    //utilizamos medidas del mundo * segundo,en metros,asi no tenemos que preocuparnos por el tamaño de la pantalla para los calculos
    //tambien utilizo una varieable int escudo para saber cuanta cantidad de escudo le  queda a la nave

    float movementSpeed;
    int shield;

    //posicion y dimension de la nave
    //ancho y alto de la nave, y la posicion de la nave con las coordenadas X e Y
    float xPosition, yPosition;
    float width, height;


    //graficos
    //necesitamos dos regiones de textura,una para la nave y otra para el escudo
    TextureRegion shipTextureRegion, shieldTextureRegion, laserTextureRegion;

    public Ship(float movementSpeed, int shield,
                float width, float height,
                float xCentre, float yCentre,
                TextureRegion shipTextureRegion, TextureRegion shieldTextureRegion, TextureRegion laserTextureRegion) {
        this.movementSpeed = movementSpeed;
        this.shield = shield;
        this.xPosition = xCentre - width/2;
        this.yPosition = yCentre - height/2;
        this.width = width;
        this.height = height;
        this.shipTextureRegion = shipTextureRegion;
        this.shieldTextureRegion = shieldTextureRegion;
        this.laserTextureRegion = laserTextureRegion;
    }

    public void draw(Batch batch){
        batch.draw(shipTextureRegion,xPosition,yPosition,width,height);
        if (shield > 0){
            batch.draw(shieldTextureRegion,xPosition,yPosition,width,height);


        }
    }

}
