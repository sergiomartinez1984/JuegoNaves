package extra;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;

public class AssetShip {

    private final AssetManager assetManager;
    private final TextureAtlas textureAtlas;

    //Esta clase será la encargada de obtener los recursos de la carpeta assest de Android para poder utilizarlos en nuestro juego.
    //
    public AssetShip(AssetManager assetManager, TextureAtlas textureAtlas) {
        this.assetManager = new AssetManager();
        this.textureAtlas = textureAtlas;
    }
}
