package screens;

import com.badlogic.gdx.Game;

import java.util.Random;

import extra.AssetShip;

public class Main extends Game {

	GameScreen gameScreen;

	public static Random random = new Random();
	public AssetShip assetManager;

	@Override
	public void create() {
		this.assetManager = new AssetShip();
		gameScreen = new GameScreen(this);
		setScreen(gameScreen);

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
	}
}
