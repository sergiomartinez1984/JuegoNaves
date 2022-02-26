package screens;

import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

class GameScreen implements Screen {

    //Screen
    private final Camera camera;
    private final Viewport viewport;

    //graphics
    private final SpriteBatch batch;
    //private Texture background
    private Texture[] background;

    //Timing
    //private int backgroundOffSet;
    private float[] backgroundOffSet = {0,0,0,0};
    private float backgroundMaxScrollingSpeed;


    //World
    private final int WORLD_WIDTH = 72;
    private final int WORLD_HEIGHT = 128;


    GameScreen(){

        camera = new OrthographicCamera();
        viewport = new StretchViewport(WORLD_WIDTH,WORLD_HEIGHT,camera);

    //    background = new Texture("fondoespacio.png");
    //    backgroundOffSet = 0;

        background = new Texture[4];
        background[0] = new Texture("fondoespacio00.png");
        background[1] = new Texture("fondoespacio01.png");
        background[2] = new Texture("fondoespacio02.png");
        background[3] = new Texture("fondoespacio03.png");

        backgroundMaxScrollingSpeed = (float) WORLD_HEIGHT / 4;

        batch = new SpriteBatch();
    }




    @Override
    public void render(float deltaTime) {
        batch.begin();

        //scroll background
        renderBackground(deltaTime);

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
            batch.draw(background[layer],
                    0,-backgroundOffSet[layer],
                    WORLD_WIDTH,WORLD_HEIGHT);
            batch.draw(background[layer],
                    0,-backgroundOffSet[layer]+WORLD_HEIGHT,
                    WORLD_WIDTH,WORLD_HEIGHT);
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
