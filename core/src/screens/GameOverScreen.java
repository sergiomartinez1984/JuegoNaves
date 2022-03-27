package screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;



public class GameOverScreen extends BaseScreen {
  private final Camera camera;
  private Stage stage;
  private TextButton retryButton;
  private TextButton homeButton;
  private Skin skin;

  public GameOverScreen(Main main) {
    super(main); 
    camera = new OrthographicCamera();
    stage = new Stage();
    setButtons();
  }

  public void addGameOver() {
    Image gameOver = new Image(mainGame.assetManager.getGameOver());
    gameOver.setSize(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
    stage.addActor(gameOver);
    stage.addActor(retryButton);
    stage.addActor(homeButton);
  }

  @Override
  public void dispose() {
    stage.dispose();
  }

  public void show() {
    Gdx.input.setInputProcessor(stage);
    addGameOver(); 
  }

  public void render(float deltaTime) {
    Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
    stage.draw();
  }

  private void setButtons() {
    skin = new Skin(Gdx.files.internal("button/glassy-ui.json"));

    retryButton = new TextButton("Retry", skin);
    retryButton.setPosition(30, 25);
    retryButton.setTransform(true);
    retryButton.setScale(0.3f);

    homeButton = new TextButton("Home", skin);
    homeButton.setPosition(125, 25);
    homeButton.setTransform(true);
    homeButton.setScale(0.3f);

    retryButton.addCaptureListener(new ChangeListener() {
        @Override
        public void changed(ChangeEvent event, Actor actor) {
            mainGame.setScreen(new GameScreen(mainGame));
        }
    });

    homeButton.addCaptureListener(new ChangeListener() {
      @Override
      public void changed(ChangeEvent event, Actor actor) {
          mainGame.setScreen(new InitialScreen(mainGame));
      }
    });
  } 
}
