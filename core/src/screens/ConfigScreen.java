package screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;

public class ConfigScreen extends BaseScreen {

    private Stage stage;
    private TextButton returnButton;
    private static TextButton musicButton;
    private TextButton soundButton;
    private Skin skin;
    private static boolean musicActivate = true;
    private static boolean soundActivate = true;
    private static Color color;

    public ConfigScreen(Main mainGame) {
        super(mainGame);

        stage = new Stage();
        setButtons();
        color = returnButton.getColor();

        if (musicActivate) {
            musicButton.setColor(color);
        } else {
            musicButton.setColor(Color.GRAY);
        }

        if (soundActivate) {
            soundButton.setColor(color);
        } else {
            soundButton.setColor(Color.GRAY);
        }
    }

    @Override
    public void dispose() {
        stage.dispose();
    }

    public void show() {
        Gdx.input.setInputProcessor(stage);
        addConfigScreen();
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

        returnButton = new TextButton("Return", skin);
        returnButton.setPosition(30, 100);
        returnButton.setTransform(true);
        returnButton.setScale(0.3f);

        musicButton = new TextButton("Music", skin);
        musicButton.setPosition(100, 450);
        musicButton.setTransform(true);
        musicButton.setScale(0.5f);

        soundButton = new TextButton("Sound", skin);
        soundButton.setPosition(100, 375);
        soundButton.setTransform(true);
        soundButton.setScale(0.5f);

        returnButton.addCaptureListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                mainGame.setScreen(new InitialScreen(mainGame));
            }
        });

        musicButton.addCaptureListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                configurationMusic();
            }
        });

        soundButton.addCaptureListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                configurationSound();
            }
        });

    }

    private void addConfigScreen() {
        // Image initialGame = new Image(mainGame.assetManager.getInitial());
        // initialGame.setSize(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());

        stage.addActor(returnButton);
        stage.addActor(musicButton);
        stage.addActor(soundButton);
    }

    public void configurationMusic() {
        if (musicActivate == true) {
            musicActivate = false;
            musicButton.setColor(Color.GRAY);
        } else {
            musicActivate = true;
            musicButton.setColor(color);
        }
    }

    public void configurationSound() {
        if (soundActivate == true) {
            soundActivate = false;
            soundButton.setColor(Color.GRAY);
        } else {
            soundActivate = true;
            soundButton.setColor(color);
        }
    }

    public boolean soundOnOff() {
        return soundActivate;
    }

    public boolean musicOnOff() {
        return musicActivate;
    }
}