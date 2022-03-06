package actor;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;

import screens.Main;

public class EnemyShip extends Ship{

    Vector2 directionVector;
    float timeSinceLastDirectionChange = 0;
    float directionChangeFrequency = 0.75f;




    public EnemyShip(float xCentre, float yCentre, float width,
                      float height, float movementSpeed, int shield, float laserWidth,
                      float laserHeight, float laserMovementSpeed, float timeBetweenShot,
                      TextureRegion shipTextureRegion,
                      TextureRegion shieldTextureRegion,
                      TextureRegion laserTextureRegion) {
        super(xCentre, yCentre, width, height, movementSpeed, shield, laserWidth, laserHeight, laserMovementSpeed, timeBetweenShot, shipTextureRegion, shieldTextureRegion, laserTextureRegion);
        directionVector = new Vector2(0,-1);
    }

    public Vector2 getDirectionVector() {
        return directionVector;
    }
    //creamos un metodo de direccion del vector aleatorio privado,porque solo se llamara desde el metodo de actualizacion

    private void randomizeDirectionVector(){
        double rumbo = Main.random.nextDouble()*6.283185;// DE 0 A 2 veces PI
        directionVector.x = (float)Math.sin(rumbo);
        directionVector.y = (float)Math.cos(rumbo);

    }

    @Override
    public void update(float deltaTime) {
        super.update(deltaTime);
        timeSinceLastDirectionChange += deltaTime;
        if(timeSinceLastDirectionChange > directionChangeFrequency){
            randomizeDirectionVector();
            timeSinceLastDirectionChange -= directionChangeFrequency;
        }
    }

    @Override
    public Laser[] fireLaser() {
        Laser[]laser = new Laser[2];
        laser[0] = new Laser(boundingBox.x + boundingBox.width * 0.18f,boundingBox.y - laserHeight,
                laserWidth,laserHeight,
                laserMovementSpeed, laserTextureRegion);
        laser[1] = new Laser(boundingBox.x + boundingBox.width * 0.82f,boundingBox.y - laserHeight,
                laserWidth,laserHeight,
                laserMovementSpeed, laserTextureRegion);

        timeSinceLastShop = 0;

        return laser;
    }

    @Override
    public void draw(Batch batch){
        batch.draw(shipTextureRegion,boundingBox.x,boundingBox.y,boundingBox.width,boundingBox.height);
        if (shield > 0){
            batch.draw(shieldTextureRegion,boundingBox.x,boundingBox.y -boundingBox.height *0.2f,boundingBox.width,boundingBox.height);


        }
    }
}
