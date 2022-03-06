package screens;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Game;

import java.util.Random;

import extra.AssetShip;

public class Main extends Game {
    public GameScreen gameScreen;
    public GameOverScreen gameOverScreen;
    //AssetMan sera la clase encargada de adminstrarnos los recuros necesarios
    public AssetShip assetManager;
    public static Random random = new Random();

    @Override
    public void create() {
        this.assetManager = new AssetShip();
        this.gameOverScreen = new GameOverScreen(this);
        this.gameScreen = new GameScreen(this);
        setScreen(gameScreen);
    }


	/*GameScreen gameScreen;

	public static Random random = new Random();
	public AssetShip assetManager;


	@Override
	public void create() {
		this.assetManager = new AssetShip();

		gameScreen = new GameScreen(this) {
			@Override
			public void render(float deltaTime, Main main) {

			}
		};
		this.setScreen(gameScreen);
	}

	@Override
	public void dispose() {
		gameScreen.dispose();
	}

	@Override
	public void render() {
		super.render();
	}

	@Override
	public void resize(int width, int height) {
		gameScreen.resize(width, height);
	}*/
}
