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

public class InitialScreen extends BaseScreen {
  private final Camera camera;
  private Stage stage;
  private TextButton playButton;
  private TextButton configButton;
  private TextButton exitButton;
  private TextButton scoreButton;
  private Skin skin;

  public InitialScreen(Main main) {
    super(main);
    camera = new OrthographicCamera();

    stage = new Stage();

    setButtons();
  }

  @Override
  public void dispose() {
    stage.dispose();
  }

  public void show() {
    Gdx.input.setInputProcessor(stage);
    addInitialScreen();
  }

  public void hide() {
    Gdx.input.setInputProcessor(null);
  }

  public void render(float deltaTime) {
    Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
    stage.draw();
  }

  private void setButtons() {
    skin = new Skin(Gdx.files.internal("button/glassy-ui.json"));

    playButton = new TextButton("Play", skin);
    playButton.setPosition(30, 25);
    playButton.setTransform(true);
    playButton.setScale(0.3f);

    scoreButton = new TextButton("Score", skin);
    scoreButton.setPosition(100, 25);
    scoreButton.setTransform(true);
    scoreButton.setScale(0.3f);

    configButton = new TextButton("Settings", skin);
    configButton.setPosition(170, 25);
    configButton.setTransform(true);
    configButton.setScale(0.3f);

    exitButton = new TextButton("Exit", skin);
    exitButton.setPosition(240, 25);
    exitButton.setTransform(true);
    exitButton.setScale(0.3f);

    playButton.addCaptureListener(new ChangeListener() {
      @Override
      public void changed(ChangeEvent event, Actor actor) {
        mainGame.setScreen(new GameScreen(mainGame));
      }
    });

    scoreButton.addCaptureListener(new ChangeListener() {
      @Override
      public void changed(ChangeEvent event, Actor actor) {
      }
    });

    configButton.addCaptureListener(new ChangeListener() {
      @Override
      public void changed(ChangeEvent event, Actor actor) {
        mainGame.setScreen(new ConfigScreen(mainGame));
      }
    });

    exitButton.addCaptureListener(new ChangeListener() {
      @Override
      public void changed(ChangeEvent event, Actor actor) {
        System.exit(0);
      }
    });
  }

  private void addInitialScreen() {
    Image initialGame = new Image(mainGame.assetManager.getInitial());
    initialGame.setSize(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());

    stage.addActor(initialGame);
    stage.addActor(playButton);
    stage.addActor(scoreButton);
    stage.addActor(configButton);
    stage.addActor(exitButton);
  }
}
