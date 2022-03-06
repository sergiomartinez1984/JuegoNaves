package extra;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;


public class AssetShip {
    private String atlasGameover;
    private String atlasInitial;
    private AssetManager assetManager;
    private TextureAtlas textureAtlas;


    //Esta clase será la encargada de obtener los recursos de la carpeta assest de Android para poder utilizarlos en nuestro juego.
    //
    public AssetShip() {
        this.assetManager = new AssetManager();
        atlasGameover = "gameOver";
        atlasInitial = "PressStart";
        assetManager.load("images.atlas", TextureAtlas.class);
        assetManager.load("arcadeGameMusic.mp3",Music.class);
        assetManager.load("explosion.mp3",Sound.class);
        assetManager.finishLoading();

        textureAtlas = assetManager.get("images.atlas");
    }

    public Music getMusicBg() {
        return assetManager.get("arcadeGameMusic.mp3");
    }

    public Sound getSoundImpacto() {
        return assetManager.get("explosion.mp3");
    }

    public TextureRegion getGameOver(){
        return textureAtlas.findRegion(atlasGameover);
    }

    public TextureRegion getInitial(){
        return textureAtlas.findRegion(atlasInitial);
    }
}
