package screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.badlogic.gdx.utils.viewport.Viewport;


public class GameOverScreen extends BaseScreen {
  private final Camera camera;
  private Viewport viewport;
  private Stage stage;
  private Image gameover;
  private World world;


  public GameOverScreen (Main main) {
    super(main);
      world = new World(new Vector2(0, 0), false);
      camera = new OrthographicCamera();
      viewport = new StretchViewport(72,128,camera);

      stage = new Stage(new FitViewport(640, 360));
  }

  public void addGameOver() {
    Image gameOver = new Image(mainGame.assetManager.getGameOver());
    gameOver.setSize(640, 360);
    gameOver.setPosition(0, 0);
    stage.addActor(gameOver);
  }

  @Override
  public void dispose() {
      stage.dispose();
  }


  public void show(){
    addGameOver();
  }

  public void render(float deltaTime) {
    Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
    stage.draw();
    if (Gdx.input.justTouched() || Gdx.input.isKeyJustPressed(62)){
      mainGame.setScreen(new GameScreen(mainGame));
    }
  }
}
