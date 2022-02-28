package screens;

import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

import java.util.LinkedList;

import actor.Laser;
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


    //World
    private final int WORLD_WIDTH = 72;
    private final int WORLD_HEIGHT = 128;


    //Objetos del juego
    private Ship playerShip;
    private Ship enemyShip;
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
        playerShip = new Ship(2,3,10,10,
                        WORLD_WIDTH/2,WORLD_HEIGHT/4,
                playerShipTextureRegion,playerShieldTextureRegion,playerLaserTextureRegion);
        enemyShip = new Ship(2,1,10,10,
                WORLD_WIDTH/2,WORLD_HEIGHT*3/4,
                enemyShipTextureRegion,enemyShieldTextureRegion,enemyLaserTextureRegion);

        playerLaserList = new LinkedList<>();
        enemyLaserList = new LinkedList<>();


        batch = new SpriteBatch();
    }




    @Override
    public void render(float deltaTime) {
        batch.begin();

        //scroll background
        renderBackground(deltaTime);

        //naves enemigas
        enemyShip.draw(batch);

        //nave jugador
        playerShip.draw(batch);

        //disparos laser



        //explosiones








        batch.end();
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
