package extra;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;


public class AssetShip {

    private AssetManager assetManager;


    //Esta clase será la encargada de obtener los recursos de la carpeta assest de Android para poder utilizarlos en nuestro juego.
    //
    public AssetShip() {
        this.assetManager = new AssetManager();
        assetManager.load("arcadeGameMusic.mp3",Music.class);
        assetManager.load("explosion.mp3",Sound.class);
        assetManager.finishLoading();
    }
    public Music getMusicBg() {

        return assetManager.get("arcadeGameMusic.mp3");
    }
    public Sound getSoundImpacto() {
        return assetManager.get("explosion.mp3");
    }
}
