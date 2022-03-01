package screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

import java.util.LinkedList;
import java.util.ListIterator;
import java.util.Vector;

import actor.EnemyShip;
import actor.Laser;
import actor.PlayerShip;
import actor.Ship;

class GameScreen implements Screen {

    //Screen
    private final Camera camera;
    private final Viewport viewport;

    //graphics
    private final SpriteBatch batch;
    private TextureAtlas textureAtlas;
    private TextureRegion[] background;
    private float backgroundHeight;

    private TextureRegion playerShipTextureRegion,playerShieldTextureRegion,
    enemyShipTextureRegion,enemyShieldTextureRegion,
    playerLaserTextureRegion,enemyLaserTextureRegion;


    //Timing
    private float[] backgroundOffSet = {0,0,0,0};
    private float backgroundMaxScrollingSpeed;
    private final float TOUCH_MOVEMENT = 0.5f;

    //World
    private final int WORLD_WIDTH = 72;
    private final int WORLD_HEIGHT = 128;



    //Objetos del juego
    private PlayerShip playerShip;
    private EnemyShip enemyShip;
    private LinkedList<Laser>playerLaserList;
    private LinkedList<Laser>enemyLaserList;


    GameScreen(){

        camera = new OrthographicCamera();
        viewport = new StretchViewport(WORLD_WIDTH,WORLD_HEIGHT,camera);

        //configuracion del texture Atlas
        textureAtlas = new TextureAtlas("images.atlas");

        //Ajustes del fondo
        background = new TextureRegion[4];
        background[0] = textureAtlas.findRegion("fondoespacio00");
        background[1] = textureAtlas.findRegion("fondoespacio01");
        background[2] = textureAtlas.findRegion("fondoespacio02");
        background[3] = textureAtlas.findRegion("fondoespacio03");

        backgroundHeight = WORLD_HEIGHT * 2;
        backgroundMaxScrollingSpeed = (float) WORLD_HEIGHT / 4;


        //Inicializar las regiones de texturas

        playerShipTextureRegion = textureAtlas.findRegion("playerShip2_blue");
        enemyShipTextureRegion = textureAtlas.findRegion("enemyBlack1");
        playerShieldTextureRegion = textureAtlas.findRegion("shield1");
        enemyShieldTextureRegion = textureAtlas.findRegion("shield2");
        playerLaserTextureRegion = textureAtlas.findRegion("laserBlue13");
        enemyLaserTextureRegion = textureAtlas.findRegion("laserRed02");


        //Configuracion de objetos del juego
        playerShip = new PlayerShip(WORLD_WIDTH/2,WORLD_HEIGHT/4,
                10,10,
                48,3,
                0.8f,4,65,0.5f,
                playerShipTextureRegion,playerShieldTextureRegion,playerLaserTextureRegion);

        enemyShip = new EnemyShip(Spaceship.random.nextFloat()*(WORLD_WIDTH-10)+5,
                WORLD_HEIGHT - 5,
                10,10,
                48,1,
                0.3f,5,50,0.8f,
                enemyShipTextureRegion,enemyShieldTextureRegion,enemyLaserTextureRegion);

        playerLaserList = new LinkedList<>();
        enemyLaserList = new LinkedList<>();


        batch = new SpriteBatch();

    }




    @Override
    public void render(float deltaTime) {
        batch.begin();

        detectImput(deltaTime);
        moveEnemies(deltaTime);

        playerShip.update(deltaTime);
        enemyShip.update(deltaTime);

        //scroll background
        renderBackground(deltaTime);

        //naves enemigas
        enemyShip.draw(batch);

        //nave jugador
        playerShip.draw(batch);

        //disparos lasers
        renderLasers(deltaTime);

        //detectar colisiones entre los lasers y la nave
        detectCollisions();

        //explosiones

        renderExplosions(deltaTime);

        batch.end();
    }

    private void detectImput(float deltaTime){
        //Movimiento con las teclas del teclado,para la version de escritorio
        float leftLimit,rightLimit,uplimit,downLimit;
        leftLimit = -playerShip.boundingBox.x;
        downLimit = -playerShip.boundingBox.y;
        rightLimit = WORLD_WIDTH - playerShip.boundingBox.x - playerShip.boundingBox.width;
        uplimit =(float) WORLD_HEIGHT/2 - playerShip.boundingBox.y - playerShip.boundingBox.height;

        if (Gdx.input.isKeyPressed(Input.Keys.RIGHT)&& rightLimit > 0){
            playerShip.translate(Math.min(playerShip.movementSpeed * deltaTime,rightLimit),0f);
        }
        if (Gdx.input.isKeyPressed(Input.Keys.UP) && uplimit > 0){
            playerShip.translate(0f, Math.min(playerShip.movementSpeed * deltaTime, uplimit));
        }
        if (Gdx.input.isKeyPressed(Input.Keys.LEFT) && leftLimit < 0){
            playerShip.translate(Math.max(-playerShip.movementSpeed * deltaTime,leftLimit),0f);
        }
        if (Gdx.input.isKeyPressed(Input.Keys.DOWN)&& downLimit < 0){
            playerShip.translate(0f,Math.max(-playerShip.movementSpeed * deltaTime,downLimit));
        }

        //Movimiento al tocar la pantalla del movil
        if(Gdx.input.isTouched()){

            //obtener la pulsacion del lugar donde el usuario esta tocando en la pantalla
            float xTouchPixels = Gdx.input.getX();
            float yTouchPixels = Gdx.input.getY();

            //convertimos las cordenadas a una posicion en el mundo,Vector2es un objeto que almacenara dos valores de coma flotante que son x e y,
            //mejor dicho captura las coordenadas tactiles x e y
            Vector2 touchPoint = new Vector2(xTouchPixels,yTouchPixels);
            touchPoint = viewport.unproject(touchPoint);

            //Calculos de X e Y diferentes
            Vector2 playerShipCentre = new Vector2(
                    playerShip.boundingBox.x + playerShip.boundingBox.width/2,
                    playerShip.boundingBox.y + playerShip.boundingBox.height/2);

            float touchDistance = touchPoint.dst(playerShipCentre);

            if (touchDistance > TOUCH_MOVEMENT){
                float xTouchDifference = touchPoint.x - playerShipCentre.x;
                float yTouchDifference = touchPoint.y - playerShipCentre.y;

            //Escala a la velocidad máxima de la nave
                float xMove = xTouchDifference / touchDistance * playerShip.movementSpeed * deltaTime;
                float yMove = yTouchDifference / touchDistance * playerShip.movementSpeed * deltaTime;

                if (xMove > 0) xMove = Math.min(xMove,rightLimit);
                else xMove = Math.max(xMove,leftLimit);

                if (yMove > 0) yMove = Math.min(yMove,uplimit);
                else yMove = Math.max(yMove,downLimit);

                playerShip.translate(xMove,yMove);
            }
        }
    }

    private void moveEnemies(float deltaTime){

        float leftLimit,rightLimit,uplimit,downLimit;
        leftLimit = -enemyShip.boundingBox.x;
        downLimit = (float) WORLD_HEIGHT/2 - enemyShip.boundingBox.y;
        rightLimit = WORLD_WIDTH - enemyShip.boundingBox.x - enemyShip.boundingBox.width;
        uplimit = WORLD_HEIGHT - enemyShip.boundingBox.y - enemyShip.boundingBox.height;

        float xMove = enemyShip.getDirectionVector().x * enemyShip.movementSpeed * deltaTime;
        float yMove = enemyShip.getDirectionVector().y * enemyShip.movementSpeed * deltaTime;

        if (xMove > 0) xMove = Math.min(xMove,rightLimit);
        else xMove = Math.max(xMove,leftLimit);

        if (yMove > 0) yMove = Math.min(yMove,uplimit);
        else yMove = Math.max(yMove,downLimit);

        enemyShip.translate(xMove,yMove);
    }


    private void detectCollisions() {
        //Para cada jugador, verificamos si el laser se cruza con una nave enemiga
        ListIterator<Laser> iterator = playerLaserList.listIterator();
        while (iterator.hasNext()) {
            Laser laser = iterator.next();
            if (enemyShip.intersects(laser.boundingBox)){
                //contacto con la nave enemiga
                enemyShip.hit(laser);
                iterator.remove();
            }
        }
        //Para cada enemigo, verificamos si el laser se cruza con una nave jugador

        iterator = enemyLaserList.listIterator();
        while (iterator.hasNext()) {
            Laser laser = iterator.next();
            if (playerShip.intersects(laser.boundingBox)){
                //contacto con la nave jugador
                playerShip.hit(laser);
                iterator.remove();
            }
        }

    }

    private void renderExplosions(float deltaTime){


    }

    private void renderLasers(float deltaTime){

        //crear nuevos lasers
        //lasers jugador
        if(playerShip.canFireLaser()){
            Laser[]lasers = playerShip.fireLaser();
            for (Laser laser : lasers){
                playerLaserList.add(laser);
            }
        }
        //lasers enemigos
        if(enemyShip.canFireLaser()){
            Laser[]lasers = enemyShip.fireLaser();
            for (Laser laser : lasers){
                enemyLaserList.add(laser);
            }
        }
        //pintar lasers


        //borrar viejos lasers
        ListIterator<Laser>iterator = playerLaserList.listIterator();
        while (iterator.hasNext()){
            Laser laser = iterator.next();
            laser.draw(batch);
            laser.boundingBox.y += laser.movementSpeed*deltaTime;
            if (laser.boundingBox.y > WORLD_HEIGHT){
                iterator.remove();
            }

        }
        iterator = enemyLaserList.listIterator();
        while (iterator.hasNext()){
            Laser laser = iterator.next();
            laser.draw(batch);
            laser.boundingBox.y -= laser.movementSpeed*deltaTime;
            if (laser.boundingBox.y + laser.boundingBox.height< 0){
                iterator.remove();
            }

        }

    }

    private void renderBackground(float deltaTime){


        backgroundOffSet[0] += deltaTime * backgroundMaxScrollingSpeed / 8;
        backgroundOffSet[1] += deltaTime * backgroundMaxScrollingSpeed / 4;
        backgroundOffSet[2] += deltaTime * backgroundMaxScrollingSpeed / 2;
        backgroundOffSet[3] += deltaTime * backgroundMaxScrollingSpeed ;

        for (int layer = 0 ; layer < backgroundOffSet.length ; layer ++){
            if (backgroundOffSet[layer] > WORLD_HEIGHT){
                backgroundOffSet[layer] = 0;
            }
            batch.draw(background[layer],0, -backgroundOffSet[layer],
                    WORLD_WIDTH,backgroundHeight);
        }
    }


    @Override
    public void resize(int width, int height) {
        viewport.update(width,height,true);
        batch.setProjectionMatrix(camera.combined);
    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {

    }
    @Override
    public void show() {

    }
}
