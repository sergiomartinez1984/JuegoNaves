package screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton.TextButtonStyle;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

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

    stage = new Stage(new FitViewport(640, 360));
    Gdx.input.setInputProcessor(stage);

    setButtons();
  }

  public void addInitialScreen() {
    Image initialGame = new Image(mainGame.assetManager.getInitial());
    initialGame.setSize(640, 360);
    initialGame.setPosition(0, 0);

    stage.addActor(initialGame);
    stage.addActor(playButton);
    stage.addActor(scoreButton);
    stage.addActor(configButton);
    stage.addActor(exitButton);
  }

  @Override
  public void dispose() {
    stage.dispose();
  }

  public void show() {
    addInitialScreen();
  }

  public void render(float deltaTime) {
    Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
    stage.draw();
    if (Gdx.input.justTouched() || Gdx.input.isKeyJustPressed(62)) {
      mainGame.setScreen(new GameScreen(mainGame));
    }
  }

  private void setButtons() {
    skin = new Skin(Gdx.files.internal("button/glassy-ui.json"));

    playButton = new TextButton("Jugar", skin);
    playButton.setPosition(30, 25);
    playButton.setTransform(true);
    playButton.setScale(0.4f);

    scoreButton = new TextButton("Score", skin);
    scoreButton.setPosition(180, 25);
    scoreButton.setTransform(true);
    scoreButton.setScale(0.4f);

    configButton = new TextButton("Settings", skin);
    configButton.setPosition(330, 25);
    configButton.setTransform(true);
    configButton.setScale(0.4f);

    exitButton = new TextButton("Exit", skin);
    exitButton.setPosition(490, 25);
    exitButton.setTransform(true);
    exitButton.setScale(0.4f);
  }
}
