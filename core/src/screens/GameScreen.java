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
    // declaraciones de todas las variables,de las distintas partes del juego

    // pantalla del juego
    private final Camera camera;
    private final Viewport viewport;

    // graficos
    private final SpriteBatch batch;
    private TextureAtlas textureAtlas;
    private Texture explosionTexture;

    // sonidos
    private Music musicbg;
    private Sound explosionSound;

    private TextureRegion[] background;
    private float backgroundHeight;

    private TextureRegion playerShipTextureRegion, playerShieldTextureRegion,
            enemyShipTextureRegion, enemyShieldTextureRegion,
            playerLaserTextureRegion, enemyLaserTextureRegion;

    // Tiempo
    private float[] backgroundOffSet = { 0, 0, 0, 0 };
    private float backgroundMaxScrollingSpeed;
    private float timeBetweenEnemySpawns = 2f;
    private float enemySpawnTimer = 0;

    pr otected final float WORLD_WIDTH = 72;
    protected final float WORLD_HEIGHT = 128;
    private final float TOUCH_MOVEMENT = 5f;


    // multiples explosiones en el fondo,por eso se enlaza la lista al objeto 
    // xplosiones
    pr ivate PlayerShip playerShip;
    // 
    private LinkedList<EnemyShip> enemyShipList;
    private LinkedList<Laser>playerLaserList;
    private LinkedList<Laser> enemyLaserList;
    private LinkedList<Explos ion>explosionsList;
 
    private int score = 0;

    //cabeceras para el texto de la puntuacion y demas
    Bi tmapFont font;
    float margilVertical,hudLeftX,hudRightX,hudCentreX,hudRow1y,hudRow2y,hudSectionWidth;
      
    GameScreen(Main main){
        super(main); 

        camera = new OrthographicCamera();
        viewport = new StretchViewport(WORLD_WIDTH,WORLD_HEIGHT,camera);
  
        //configuracion del texture Atlas
        te xtureAtlas = new TextureAtlas("images.atlas");

        //reproduccion de los sonidos
        th is.musicbg = main.assetManager.getMusicBg();
        this.explosionSound = main.assetManager.getSoundImpacto();

        //Ajustes del fondo
        ba ckground = new TextureRegion[4];
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
        pl ayerShip = new PlayerShip(WORLD_WIDTH/2,WORLD_HEIGHT/4,
                10,10,     
                48, 5,
                0.8 f,4,65,0.5f,
                playe rS hip TextureRegion,playerShieldTextureRegion,playerLaserTextureRegion);
  
        enemyShipList = new LinkedList<>();
        playerLaserList = new LinkedList<>();
        enemyLaserList = new LinkedList<>();
        explosionsList = new LinkedList<>();

        batch = new SpriteBatch();

        prepareHUD();
    }
    /

    pr ivate void prepareHUD() {
        //Primero creamos una fuente de mapa de bits,
        Fr eeTypeFontGenerator fontGenerator = new FreeTypeFontGenerator(Gdx.files.internal("Fonts/EdgeOfTheGalaxyRegular-OVEa6.otf"));
        FreeTypeFontGenerator.FreeTypeFontParameter fontParameter = new 
                FreeTypeFontGenerator.FreeTypeFontParameter();
        fontParameter.size = 72;
        fontParameter.borderWidth = 3.6f;
        fontParameter.color = new Color(1,1,1,0.3f);
        fontParameter.borderColor = new Co lo r( 0,0,0,0.3f);
   
        font = fontGenerator.generateFont(fontParameter);

        //Escalar tamaño de la fuente para adaptarse al mundo
        fo nt.getData().setScale(0.08f);

        //Calcular los margenes del HUD
        ma rgilVertical = font.getCapHeight()/2;
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
        while (enemyShipListIte rator.hasNext()) {
            EnemyShip enemyShip = enemyShipListIterator.next();
            moveEnemy(enemyShip,deltaTime);
            enemyShip.update(del taTime);
            enemyShip.draw(batch);
        }
        //nave jugador
        pl ayerShip.draw(batch);

        //disparos lasers
        re nderLasers(deltaTime);

        //detectar colisiones entre los lasers y la nave
        de tectCollisions();

        //explosiones
 
        renderExplosions(deltaTime);

        updateAndRenderHUD();

        batch.end();
    }

    private void updateAndRenderHUD(){
        font.draw(batch,"Score",hudLe ftX,hudRow1y,hudSectionWidth, Align.left,false);
        font.draw(batch, "Escudo" ,hudCentr eX,hudRow 1y,hudSectionWidth,Align.cen ter,false);
        font.draw(batch, "Vidas",h udRightX,hu dRow1y,hu dSectionWidth,Al ign.right,fal se);
      
        //segunda fila de renderiazdo de texto para que coincida con el puntaje
        fo nt.draw(batch,String.format(Locale.getDefault(),"%06d",score),hudLeftX,hudRow2y,hudSectionWidth,Align.left,false);
        font.draw(batch, String.format(Locale.getDefault(), "%02d", playerS hip.shiel d),hudCen treX,hudRow2y,hu
                dSectionWid th,Align.center,false);
        font.draw(batch, String.format(Locale.getDefault(), "%02d", playerShip.lives),h udRightX,hu dRow2y,hu
                dSectionWidth,Al ign.right,fal se);
     
                  
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
        //Movimiento con las teclas del tecla do,para la version de escritorio
        fl oat leftLimit,rightLimit,uplimit,downLimit;
        leftLimit = -pla yerShip.bou ndingBox .x;
        downLimit = -playerShip.boundingBox.y;
        rightLimit = WORLD_WIDTH - playerShip.boundingBox.x - playerShip.boundingBox.width;
        uplimit =(float) WORLD_HEIGHT/2 - playerShip.boundingBox.y - playerShip.boundingBox.height;
   
        if (Gdx.input.isKeyPressed(Input.Keys.RIGHT)&& rightLimit > 0){
            playerShip.translate(Math.min(playerShip .movementSpeed * d eltaTime,rightLimit),0f);
        }  
        if (Gdx.input.isKeyPressed(Input.Keys.UP) && uplimit > 0){
            playerShip.translate(0f, Math.min(playerShip.movement Speed * deltaTime, uplimit));
        }
        if (Gdx.input.isKeyPressed(Input.Keys.LEFT) && leftLimit < 0){
            playerShip.translate(Math.max(-playerShip.movementSpeed *  deltaTime,leftLimit),0f);
        }  
        if (Gdx.input.isKeyPressed(Input.Keys.DOWN)&& downLimit < 0){
            playerShip.translate(0f,Math.max(-playe rShip.movementSpe ed * deltaTime,downLimit));
        }  

        //Movimiento al tocar la pantalla del movil
        if (Gdx.input.isTouched()){
  
            //obtener la pulsacion del lugar donde el usuario esta tocando en la pantalla
            fl oat xTouchPixels = Gdx.input.getX();
            float yTouchPixels = Gdx.input.getY();

            //convertimos las cordenadas a una posicion en el mundo,Vector2es un objeto que almacenara dos valores de coma flotante que son x e y,
            // mejor dicho captura las coordenadas tactiles x e y
            // 
            Ve ctor2 touchPoint = new Vector2(xTouchPixels,yTouchPixels);
            touchPoint = viewport.unproject(touchPoint); 

            //Calculos de X e Y diferentes
            Ve ctor2 playerShipCentre = new Vector2(
                    playerShip.boundingBox.x + playerShip.boundingBox.width/2,
                    playerShip.boundingBox.y + playerShip.boundingBox.heigh t /2);
  
            float touchDistance = touchPoint.dst(playerShipCentre);

            if (touchDistance > TOUCH_MOVEMENT){
                float xTouchDifference = touchP oint.x - playerShipCentre.x;
                float yTouchDifference = touchPoint.y - playerShipCentre.y;


                     float xMove = xTouchDifference / touchDistance * playerShip.movementSpeed * deltaTime;
                float yMove = yTouchDifference / touchDistance * playerShip.movementSpeed * deltaTime;

                if (xMove > 0) xMove = Math.min(xMove,rightLimit);
                else xMove = M
                    th.max(xMove,leftLimit) ;

                     
                if (yMove > 0) yMove = Math.min(yMove,uplimit);
                else yMove = M
                    th.max(yMove,downLimit) ;

                     
                playerShip.translate(xMove,yMove);
            } 
        }
    }

    private void moveEnemy(EnemyShip enemyShip,float deltaTime){
        float leftLimit,rightLimit,uplimit,down Limit; 
        leftLimit = -ene myShip.boun dingBox. x;
        downLimit = (float) WORLD_HEIGHT/2 - enemyShip.boundingBox.y;
        rightLimit = WORLD_WIDTH - enemy S hip.boundingBox.x - enemyShip.boundingBox.width;
        uplimit = WORLD_HEIGHT - enemyShip.boundingBox.y - enemyShip.boundingBox.height;

        float xMove = enemyShip.getDirectionVector().x * enemyShip.movementSpeed * deltaTime;
        float yMove = enemyShip.getDirectionVector().y * enemyShip.movementSpeed * deltaTime;

        if (xMove > 0) xMove = Math.min(xMove,rightLimit);
        else xMove = M
            th.max(xMove,leftLimit) ;

             
        if (yMove > 0) yMove = Math.min(yMove,uplimit);
        else yMove = M
            th.max(yMove,downLimit) ;

             
        enemyShip.translate(xMove,yMove);
    } 

    private void detectCollisions() {
        //Para cada jugador, verificamos si el laser se cruza con una nave enemiga
        Li stIterator<Laser> laserListIterator = playerLaserList.listIterator();
        while (laserListIterator.hasNext()) {
            Laser laser = laserListIterator.next();
            ListIterator<EnemyShip>enemyShipListIterator = enemyShipList.listIterator();
            while (enemyShipListIte rator.hasNext()){
                EnemyShip enemyShip = enemyShipList Iterator.next();

                if (enemyShip.intersects(laser.boundingBox)) {
                    //contacto con la nave enemiga
                   if( enemyShip.hitDetectAndDestro
                         explosionSound.play();
                        enemyShipListIterator.
                         explosionsList.add(
                                new Explosion(explosionTexture,
                                        new Rectangle(enemyShip.boundingBox),
                                        0.7f));
                        score += 150;
                   }
                     
                    break;
                    
                
        }
        //Para cada enemigo, verificamos si el laser se cruza con una nave jugador
 
        laserListIterator = enemyLaserList.listIterator();
        while (laserListIterator.hasNext()) {
            Laser laser = laserListIterator.next();
            if (playerShip.intersects(laser.boundingBox)){
                //contacto con la nave jugador 
                if  (playerShip.hitDetectAndDestroy(laser)){
                    explosionsList.add( 
                            new Explosion(explosionTexture,
                                    new Rectangle(playerShip.boundingBox),
                                    1.6f));

                    playerShip.lives --;
                    playerShip.shiel = 5;
                    explosionSound.play();
                    if(playerShip.lives < 0){
                         musicbg.stop(); 
                        mainGame.setScreen(new GameOverScreen(mainGame));
                    }
                };
                l
                aserListIterator.remove();
            }
        }
    }

    private void renderExplosions(float deltaTime){
        ListIterator<Explosion>explosionListIterat or = explosionsList.listIterator();
        while (explosionListIte rator.hasNext()){
            Explosion explosion = explosionList Iterator.next();
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
        // lasers jugador
        if (playerShip.canFireLaser()){
             Laser[]lasers = playerShi p.fireLaser();
            playerL aserList.addAll(Arrays.asList(lasers));
        }
        //lasers enemigos
        Li stIterator<EnemyShip>enemyShipListIterator = enemyShipList.listIterator();
        while (enemyShipListIte rator.hasNext()){
            EnemyShip enemyShip = enemyShipList Iterator.next();
            if(enemyShip.canFireLaser()){
                 Laser[]lasers = enemyShi p.fireLaser();
                enemyLa serList.addAll(Arrays.asList(lasers));
            }
        }
        //pintar lasers
        // borrar viejos lasers
        Li stIterator<Laser>iterator = playerLaserList.listIterator();
        while (iterator.has Next()){
            Laser laser = iterator .next();
            laser.draw(batch);
            laser.boundingBox.y += laser.movementSpeed*deltaTime;
            if (laser.boundingBox.y > WORLD_HEIGHT){  
                iterator.remove(); 
            }
        }
        iterator = enemyLaserList.listIterator();
        while (iterator.hasNext()){
            Laser laser = iterator .next();
            laser.draw(batch);
            laser.boundingBox.y -= laser.movementSpeed*deltaTime;
            if (laser.boundingBox.y + laser.boundingBo x .height< 0){
                iterator.remove();  
            }
        }
    }

    private void renderBackground(float deltaTime){
        backgroundOffSet[0] += deltaTime * backgro undMaxScrollingSpeed / 8;
        backgroundOffSet[1] += deltaTime * backgroundMaxScrollingSpeed / 4;
        backgroundOffSet[2] += deltaTime * backgroundMaxScrollingSpeed / 2;
        backgroundOffSet[3] += deltaTime * backgroundMaxScrollingSpeed ;
        for (int layer = 0 ; layer < backgroundOffSet.length ; layer +){
            if (backgroundffSet[layer] > WORLD_HEIGHT){ 
                backgroundOffSet[layer] = 0; 
            }
            batch.draw(background[layer],0, -backgroundOffSet[layer],
                    WORLD_WIDTH,backgroun dHeight);
        } 
    }

    @Override
    public void resize(int width, int height) {
        viewport.update(width,height,true);
        batch.setProjectionMat rix(cam era.combined);
    }

    @Override
    public void dispose() {
    }

    @Override
    public void show() {
        this.musicbg.setLooping(true);
        musicbg.play();
        this.musicbg.setVolume(0.5f);
    }
}
