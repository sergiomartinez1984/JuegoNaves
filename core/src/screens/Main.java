package screens;

import com.badlogic.gdx.Game;
import java.util.Random;
import extra.AssetShip;

public class Main extends Game {
    public GameScreen gameScreen;
    public GameOverScreen gameOverScreen;
    public InitialScreen initialScreen;
    //AssetMan sera la clase encargada de adminstrarnos los recuros necesarios
    public AssetShip assetManager;
    public static Random random = new Random();

    @Override
    public void create() {
        this.assetManager = new AssetShip();
        this.gameOverScreen = new GameOverScreen(this);
        this.gameScreen = new GameScreen(this);
        this.initialScreen = new InitialScreen(this);
        setScreen(initialScreen);
    }
}
