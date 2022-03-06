package screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.ListIterator;
import java.util.Locale;
import actor.EnemyShip;
import actor.Explosion;
import actor.Laser;
import actor.PlayerShip;

public class GameScreen extends BaseScreen {

    //declaraciones de todas las variables,de las distintas partes del juego

    //pantalla del juego
    private final Camera camera;
    private final Viewport viewport;

    //graficos
    private final SpriteBatch batch;
    private TextureAtlas textureAtlas;
    private Texture explosionTexture;

    //sonidos
    private Music musicbg;
    private Sound explosionSound;


    private TextureRegion[] background;
    private float backgroundHeight;

    private TextureRegion playerShipTextureRegion,playerShieldTextureRegion,
    enemyShipTextureRegion,enemyShieldTextureRegion,
    playerLaserTextureRegion,enemyLaserTextureRegion;


    //Tiempo
    private float[] backgroundOffSet = {0,0,0,0};
    private float backgroundMaxScrollingSpeed;
    private float timeBetweenEnemySpawns = 2f;
    private float enemySpawnTimer = 0;


    //Mundo
    protected final float WORLD_WIDTH = 72;
    protected final float WORLD_HEIGHT = 128;
    private final float TOUCH_MOVEMENT = 5f;


    //Objetos del juego , y listas vinculadas de los enemigos,los laser y las explosiones,con esto nos evitamos tener por ejemplo en las explosiones
    //multiples explosiones en el fondo,por eso se enlaza la lista al objeto explosiones
    private PlayerShip playerShip;
    private LinkedList<EnemyShip> enemyShipList;
    private LinkedList<Laser>playerLaserList;
    private LinkedList<Laser>enemyLaserList;
    private LinkedList<Explosion>explosionsList;

    private int score = 0;

    //cabeceras para el texto de la puntuacion y demas
    BitmapFont font;
    float margilVertical,hudLeftX,hudRightX,hudCentreX,hudRow1y,hudRow2y,hudSectionWidth;

    GameScreen(Main main){
        super(main);

        camera = new OrthographicCamera();
        viewport = new StretchViewport(WORLD_WIDTH,WORLD_HEIGHT,camera);

        //configuracion del texture Atlas
        textureAtlas = new TextureAtlas("images.atlas");

        //reproduccion de los sonidos
        this.musicbg = main.assetManager.getMusicBg();
        this.explosionSound = main.assetManager.getSoundImpacto();

        //Ajustes del fondo
        background = new TextureRegion[4];
        background[0] = textureAtlas.findRegion("fondoespacio00");
        background[1] = textureAtlas.findRegion("fondoespacio01");
        background[2] = textureAtlas.findRegion("fondoespacio02");
        background[3] = textureAtlas.findRegion("fondoespacio03");

        backgroundHeight = WORLD_HEIGHT * 2;
        backgroundMaxScrollingSpeed = (float) WORLD_HEIGHT / 4;

        //Inicializar las regiones de texturas del fondo

        playerShipTextureRegion = textureAtlas.findRegion("playerShip2_blue");
        enemyShipTextureRegion = textureAtlas.findRegion("enemyBlack1");
        playerShieldTextureRegion = textureAtlas.findRegion("shield1");
        enemyShieldTextureRegion = textureAtlas.findRegion("shield2");
        playerLaserTextureRegion = textureAtlas.findRegion("laserBlue13");
        enemyLaserTextureRegion = textureAtlas.findRegion("laserRed02");

        explosionTexture = new Texture("explosion.png");

        //Configuracion de objetos del juego
        playerShip = new PlayerShip(WORLD_WIDTH/2,WORLD_HEIGHT/4,
                10,10,
                48,1,
                0.8f,4,65,0.5f,
                playerShipTextureRegion,playerShieldTextureRegion,playerLaserTextureRegion);

        enemyShipList = new LinkedList<>();
        playerLaserList = new LinkedList<>();
        enemyLaserList = new LinkedList<>();
        explosionsList = new LinkedList<>();

        batch = new SpriteBatch();

        prepareHUD();
    }
    //metodo
    private void prepareHUD() {
        //Primero creamos una fuente de mapa de bits,
        FreeTypeFontGenerator fontGenerator = new FreeTypeFontGenerator(Gdx.files.internal("Fonts/EdgeOfTheGalaxyRegular-OVEa6.otf"));
        FreeTypeFontGenerator.FreeTypeFontParameter fontParameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
        fontParameter.size = 72;
        fontParameter.borderWidth = 3.6f;
        fontParameter.color = new Color(1,1,1,0.3f);
        fontParameter.borderColor = new Color(0,0,0,0.3f);

        font = fontGenerator.generateFont(fontParameter);

        //Escalar tamaño de la fuente para adaptarse al mundo
        font.getData().setScale(0.08f);

        //Calcular los margenes del HUD
        margilVertical = font.getCapHeight()/2;
        hudLeftX = margilVertical;
        hudRightX = WORLD_WIDTH * 2 / 3 - hudLeftX;
        hudCentreX = WORLD_WIDTH / 3;
        hudRow1y = WORLD_HEIGHT - margilVertical;
        hudRow2y = hudRow1y - margilVertical - font.getCapHeight();
        hudSectionWidth = WORLD_WIDTH / 3;
    }

    @Override
    public void render(float deltaTime) {
        batch.begin();

        renderBackground(deltaTime);

        detectImput(deltaTime);
        playerShip.update(deltaTime);

        spawnEnemyShips(deltaTime);

        ListIterator<EnemyShip>enemyShipListIterator = enemyShipList.listIterator();
        while (enemyShipListIterator.hasNext()) {
            EnemyShip enemyShip = enemyShipListIterator.next();
            moveEnemy(enemyShip,deltaTime);
            enemyShip.update(deltaTime);
            enemyShip.draw(batch);
        }
        //nave jugador
        playerShip.draw(batch);

        //disparos lasers
        renderLasers(deltaTime);

        //detectar colisiones entre los lasers y la nave
        detectCollisions();

        //explosiones

        renderExplosions(deltaTime);

        updateAndRenderHUD();

        batch.end();
    }

    private void updateAndRenderHUD(){
        font.draw(batch,"Score",hudLeftX,hudRow1y,hudSectionWidth, Align.left,false);
        font.draw(batch,"Escudo",hudCentreX,hudRow1y,hudSectionWidth,Align.center,false);
        font.draw(batch,"Vidas",hudRightX,hudRow1y,hudSectionWidth,Align.right,false);

        //segunda fila de renderiazdo de texto para que coincida con el puntaje
        font.draw(batch,String.format(Locale.getDefault(),"%06d",score),hudLeftX,hudRow2y,hudSectionWidth,Align.left,false);
        font.draw(batch,String.format(Locale.getDefault(),"%02d",playerShip.shield),hudCentreX,hudRow2y,hudSectionWidth,Align.center,false);
        font.draw(batch,String.format(Locale.getDefault(),"%02d",playerShip.lives),hudRightX,hudRow2y,hudSectionWidth,Align.right,false);

    }

    private void spawnEnemyShips(float deltaTime){
        enemySpawnTimer += deltaTime;

        if (enemySpawnTimer > timeBetweenEnemySpawns) {
            enemyShipList.add(new EnemyShip(Main.random.nextFloat() * (WORLD_WIDTH - 10) + 5,
                    WORLD_HEIGHT - 5,
                    10, 10,
                    48, 2,
                    0.3f, 5, 50, 0.8f,
                    enemyShipTextureRegion, enemyShieldTextureRegion, enemyLaserTextureRegion));
            enemySpawnTimer -= timeBetweenEnemySpawns;
        }
    }

    private void detectImput(float deltaTime){
        //Movimiento con las teclas del teclado,para la version de escritorio
        float leftLimit,rightLimit,uplimit,downLimit;
        leftLimit = -playerShip.boundingBox.x;
        downLimit = -playerShip.boundingBox.y;
        rightLimit = WORLD_WIDTH - playerShip.boundingBox.x - playerShip.boundingBox.width;
        uplimit =(float) WORLD_HEIGHT/2 - playerShip.boundingBox.y - playerShip.boundingBox.height;

        if (Gdx.input.isKeyPressed(Input.Keys.RIGHT)&& rightLimit > 0){
            playerShip.translate(Math.min(playerShip.movementSpeed * deltaTime,rightLimit),0f);
        }
        if (Gdx.input.isKeyPressed(Input.Keys.UP) && uplimit > 0){
            playerShip.translate(0f, Math.min(playerShip.movementSpeed * deltaTime, uplimit));
        }
        if (Gdx.input.isKeyPressed(Input.Keys.LEFT) && leftLimit < 0){
            playerShip.translate(Math.max(-playerShip.movementSpeed * deltaTime,leftLimit),0f);
        }
        if (Gdx.input.isKeyPressed(Input.Keys.DOWN)&& downLimit < 0){
            playerShip.translate(0f,Math.max(-playerShip.movementSpeed * deltaTime,downLimit));
        }

        //Movimiento al tocar la pantalla del movil
        if(Gdx.input.isTouched()){

            //obtener la pulsacion del lugar donde el usuario esta tocando en la pantalla
            float xTouchPixels = Gdx.input.getX();
            float yTouchPixels = Gdx.input.getY();

            //convertimos las cordenadas a una posicion en el mundo,Vector2es un objeto que almacenara dos valores de coma flotante que son x e y,
            //mejor dicho captura las coordenadas tactiles x e y
            Vector2 touchPoint = new Vector2(xTouchPixels,yTouchPixels);
            touchPoint = viewport.unproject(touchPoint);

            //Calculos de X e Y diferentes
            Vector2 playerShipCentre = new Vector2(
                    playerShip.boundingBox.x + playerShip.boundingBox.width/2,
                    playerShip.boundingBox.y + playerShip.boundingBox.height/2);

            float touchDistance = touchPoint.dst(playerShipCentre);

            if (touchDistance > TOUCH_MOVEMENT){
                float xTouchDifference = touchPoint.x - playerShipCentre.x;
                float yTouchDifference = touchPoint.y - playerShipCentre.y;

            //Escala a la velocidad máxima de la nave
                float xMove = xTouchDifference / touchDistance * playerShip.movementSpeed * deltaTime;
                float yMove = yTouchDifference / touchDistance * playerShip.movementSpeed * deltaTime;

                if (xMove > 0) xMove = Math.min(xMove,rightLimit);
                else xMove = Math.max(xMove,leftLimit);

                if (yMove > 0) yMove = Math.min(yMove,uplimit);
                else yMove = Math.max(yMove,downLimit);

                playerShip.translate(xMove,yMove);
            }
        }
    }

    private void moveEnemy(EnemyShip enemyShip,float deltaTime){
        float leftLimit,rightLimit,uplimit,downLimit;
        leftLimit = -enemyShip.boundingBox.x;
        downLimit = (float) WORLD_HEIGHT/2 - enemyShip.boundingBox.y;
        rightLimit = WORLD_WIDTH - enemyShip.boundingBox.x - enemyShip.boundingBox.width;
        uplimit = WORLD_HEIGHT - enemyShip.boundingBox.y - enemyShip.boundingBox.height;

        float xMove = enemyShip.getDirectionVector().x * enemyShip.movementSpeed * deltaTime;
        float yMove = enemyShip.getDirectionVector().y * enemyShip.movementSpeed * deltaTime;

        if (xMove > 0) xMove = Math.min(xMove,rightLimit);
        else xMove = Math.max(xMove,leftLimit);

        if (yMove > 0) yMove = Math.min(yMove,uplimit);
        else yMove = Math.max(yMove,downLimit);

        enemyShip.translate(xMove,yMove);
    }

    private void detectCollisions() {
        //Para cada jugador, verificamos si el laser se cruza con una nave enemiga
        ListIterator<Laser> laserListIterator = playerLaserList.listIterator();
        while (laserListIterator.hasNext()) {
            Laser laser = laserListIterator.next();
            ListIterator<EnemyShip>enemyShipListIterator = enemyShipList.listIterator();
            while (enemyShipListIterator.hasNext()){
                EnemyShip enemyShip = enemyShipListIterator.next();

                if (enemyShip.intersects(laser.boundingBox)) {
                    //contacto con la nave enemiga
                   if(enemyShip.hitDetectAndDestroy(laser)) {
                        enemyShipListIterator.remove();
                        explosionsList.add(
                                new Explosion(explosionTexture,
                                        new Rectangle(enemyShip.boundingBox),
                                        0.7f));
                       explosionSound = mainGame.assetManager.getSoundImpacto();
                        score += 150;
                   }
                       laserListIterator.remove();
                       break;
                 }
            }
        }
        //Para cada enemigo, verificamos si el laser se cruza con una nave jugador

        laserListIterator = enemyLaserList.listIterator();
        while (laserListIterator.hasNext()) {
            Laser laser = laserListIterator.next();
            if (playerShip.intersects(laser.boundingBox)){
                //contacto con la nave jugador
                if (playerShip.hitDetectAndDestroy(laser)){
                    explosionsList.add(
                            new Explosion(explosionTexture,
                                    new Rectangle(playerShip.boundingBox),
                                    1.6f));

                    playerShip.lives --;
                    if(playerShip.lives < 0){
                        mainGame.setScreen(new GameOverScreen(mainGame));
                    }
                };
                laserListIterator.remove();
            }
        }
    }

    private void renderExplosions(float deltaTime){
        ListIterator<Explosion>explosionListIterator = explosionsList.listIterator();
        while (explosionListIterator.hasNext()){
            Explosion explosion = explosionListIterator.next();
            explosion.update(deltaTime);
            if (explosion.isFinished()) {
                explosionListIterator.remove();
            }else {
                explosion.draw(batch);
            }
        }
    }

    private void renderLasers(float deltaTime){
        //crear nuevos lasers
        //lasers jugador
        if(playerShip.canFireLaser()){
            Laser[]lasers = playerShip.fireLaser();
            playerLaserList.addAll(Arrays.asList(lasers));
        }
        //lasers enemigos
        ListIterator<EnemyShip>enemyShipListIterator = enemyShipList.listIterator();
        while (enemyShipListIterator.hasNext()){
            EnemyShip enemyShip = enemyShipListIterator.next();
            if(enemyShip.canFireLaser()){
                Laser[]lasers = enemyShip.fireLaser();
                enemyLaserList.addAll(Arrays.asList(lasers));
            }
        }
        //pintar lasers
        //borrar viejos lasers
        ListIterator<Laser>iterator = playerLaserList.listIterator();
        while (iterator.hasNext()){
            Laser laser = iterator.next();
            laser.draw(batch);
            laser.boundingBox.y += laser.movementSpeed*deltaTime;
            if (laser.boundingBox.y > WORLD_HEIGHT){
                iterator.remove();
            }
        }
        iterator = enemyLaserList.listIterator();
        while (iterator.hasNext()){
            Laser laser = iterator.next();
            laser.draw(batch);
            laser.boundingBox.y -= laser.movementSpeed*deltaTime;
            if (laser.boundingBox.y + laser.boundingBox.height< 0){
                iterator.remove();
            }
        }
    }

    private void renderBackground(float deltaTime){
        backgroundOffSet[0] += deltaTime * backgroundMaxScrollingSpeed / 8;
        backgroundOffSet[1] += deltaTime * backgroundMaxScrollingSpeed / 4;
        backgroundOffSet[2] += deltaTime * backgroundMaxScrollingSpeed / 2;
        backgroundOffSet[3] += deltaTime * backgroundMaxScrollingSpeed ;

        for (int layer = 0 ; layer < backgroundOffSet.length ; layer ++){
            if (backgroundOffSet[layer] > WORLD_HEIGHT){
                backgroundOffSet[layer] = 0;
            }
            batch.draw(background[layer],0, -backgroundOffSet[layer],
                    WORLD_WIDTH,backgroundHeight);
        }
    }

    @Override
    public void resize(int width, int height) {
        viewport.update(width,height,true);
        batch.setProjectionMatrix(camera.combined);
    }

    @Override
    public void dispose() {
        musicbg.stop();
    }

    @Override
    public void show() {
        this.musicbg.setLooping(true);
        musicbg.play();
        this.musicbg.setVolume(0.5f);
    }
}
