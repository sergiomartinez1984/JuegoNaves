package screens;

import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.badlogic.gdx.utils.viewport.Viewport;


public class GameOverScreen extends BaseScreen {
  private final Camera camera;
  private final Viewport viewport;
  private Stage stage;
  private Image gameover;


  public GameOverScreen (Main main) {
    super(main);
    camera = new OrthographicCamera();
      viewport = new StretchViewport(72,128,camera);

      stage = new Stage(new FitViewport(640, 360));
      gameover = new Image();

      stage.addActor(gameover);

  }

  @Override
  public void dispose() {
      stage.dispose();
  }


  public void show(){

  }

  @Override
  public void resize(int width, int height) {

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

  public void render(float deltaTime) {

  }
}
