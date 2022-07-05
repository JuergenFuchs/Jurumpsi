
import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Point2D;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.*;


public class Main extends Application {

    //maps keycode to boolean - keycode is the javafx enumeration
    private HashMap<KeyCode, Boolean> keys = new HashMap<KeyCode, Boolean>();
    private ArrayList<Node> platforms = new ArrayList<Node>();
    private ArrayList<Node> coins = new ArrayList<Node>();
    private Pane appRoot = new Pane();
    private Pane gameRoot = new Pane();
    private Pane uiRoot = new Pane();

    private Node player;
    private Point2D playerVelocity = new Point2D(0, 0);
    private boolean canJump = true;
    private int levelWidth;
    //Initiert die Levelgeneration
    private void initContent1(){
        Rectangle bg = new Rectangle(1280, 720);
        levelWidth = LevelData.Level1[0].length() * 60;

        for (int i=0; i< LevelData.Level1.length; i++){
            String line = LevelData.Level1[i];
            for (int j=0; j <line.length();j++){
                switch (line.charAt(j)){
                    case '0':
                        break;
                    case '1':
                        Node platform = createEntity(j*60, i *60, 60, 60, Color.GREEN);
                        platforms.add(platform);
                        break;
                    case '2':
                        Node end = createEntity(j*60, i *60, 60, 60, Color.YELLOW);
                        coins.add(end);
                        break;
                }
            }
        }
        player = createEntity(0, 600, 40, 40, Color.BLUE);
        player.translateXProperty().addListener((obs, old, newValue) -> {
            int offset = newValue.intValue();
            if (offset > 640 && offset < levelWidth-640){
                gameRoot.setLayoutX(-(offset-640));
            }
        });
        appRoot.getChildren().addAll(bg, gameRoot, uiRoot);
    }
     private void initContent2(){
        Rectangle bg = new Rectangle(1280, 720);
        levelWidth = LevelData.Level2[0].length() * 60;

        for (int i=0; i< LevelData.Level2.length; i++){
            String line = LevelData.Level2[i];
            for (int j=0; j <line.length();j++){
                switch (line.charAt(j)){
                    case '0':
                        break;
                    case '1':
                        Node platform = createEntity(j*60, i *60, 60, 60, Color.GREEN);
                        platforms.add(platform);
                        break;
                    case '2':
                        Node coin = createEntity(j*config.getBlockSize(), i*config.getBlockSize(), config.getBlockSize(), config.getBlockSize(), Color.GOLD);
                        coins.add(coin);
                        break;
                        break;
                    }
            }
        }
        player = createEntity(0, 600, 40, 40, Color.BLUE);
        player.translateXProperty().addListener((obs, old, newValue) -> {
            int offset = newValue.intValue();
            if (offset > 640 && offset < levelWidth-640){
                gameRoot.setLayoutX(-(offset-640));
            }
        });
        appRoot.getChildren().addAll(bg, gameRoot, uiRoot);
    }
        private void update(){
        if (isPressed(KeyCode.SPACE) && player.getTranslateY() >= 5){
            jumpPlayer();
        }
        if (isPressed(KeyCode.A) && player.getTranslateX() >=5){
            movePlayerX(-5);
        }
        if (isPressed(KeyCode.D) && player.getTranslateX() + 40 <=levelWidth-5){
            movePlayerX(5);
        }
        if (playerVelocity.getY() < 10){
            playerVelocity = playerVelocity.add(0, 1);
        }
        movePlayerY((int)playerVelocity.getY());
         for (Node coin : coins) {
            if (player.getBoundsInParent().intersects(coin.getBoundsInParent())) {
                coin.getProperties().put("alive", false);
                score.set(score.get() + 100);
            }
        }

        for (Iterator<Node> it = coins.iterator(); it.hasNext(); ) {
            Node coin = it.next();
            if (!(Boolean)coin.getProperties().get("alive")) {
                it.remove();
                gameRoot.getChildren().remove(coin);
            }
        }
    }
        


    private void movePlayerX(int value){
    boolean movingRight = value > 0;
    for (int i=0; i < Math.abs(value);i++){
        for (Node platform : platforms){
            if(player.getBoundsInParent().intersects(platform.getBoundsInParent())){
                if(movingRight){
                    if (player.getTranslateX() + 40 == platform.getTranslateX()){
                        return;
                    }
                }else {
                    if (player.getTranslateX() == platform.getTranslateX() + 60) {
                        return;
                    }
                }
            }
        }
        player.setTranslateX(player.getTranslateX() + (movingRight ? 1 : -1));
        }
    }
    private void movePlayerY(int value){
        boolean movingDown = value > 0;
        for (int i=0; i < Math.abs(value);i++){
            for (Node platform : platforms){
                if(player.getBoundsInParent().intersects(platform.getBoundsInParent())){
                    if(movingDown){
                        if (player.getTranslateY() + 40 == platform.getTranslateY()){
                            canJump = true;
                            return;
                        }
                    }else {
                        if (player.getTranslateY() == platform.getTranslateY() + 60) {
                            return;
                        }
                    }
                }
            }
            player.setTranslateY(player.getTranslateY() + (movingDown ? 1 : -1));
        }
    }
    //Setzt die Geschwindigkeit des Spielercharakters für das Springen
    private void jumpPlayer(){
    if(canJump){
        playerVelocity = playerVelocity.add(0, -30);
        canJump = false;
        }
    }
    //Die Methode ist für das Erstellen von Blöcken, dem charakter usw. zuständig
    private Node createEntity(int x, int y, int w, int h, Color color){
        Rectangle entity = new Rectangle(w, h);
        entity.setTranslateX(x);
        entity.setTranslateY(y);
        entity.setFill(color);
        gameRoot.getChildren().add(entity);
        return entity;

    }
    private boolean isPressed(KeyCode key){
    return keys.getOrDefault(key, false);
    }
    //Startet über JavaFX die Grafik für das Level
    @Override
    public void start(Stage primaryStage) throws Exception{
        initContent1();
        Scene scene = new Scene(appRoot);
        scene.setOnKeyPressed(event -> keys.put(event.getCode(), true));
        scene.setOnKeyReleased(event -> keys.put(event.getCode(), false));
        primaryStage.setTitle("Jurumpsi");
        primaryStage.setScene(scene);
        primaryStage.show();

        AnimationTimer timer = new AnimationTimer() {
            @Override
            public void handle(long now) {
                update();
            }
        };
        timer.start();
    }
    public void checkCollisions(){
        for (Node end : ends){
        if(player.intersects(end.getBoundsInParent())){
            System.out.println("a");
        }
    }
    }
    public void level2s(Stage primaryStage){
        initContent2();
        Scene scene = new Scene(appRoot);
        scene.setOnKeyPressed(event -> keys.put(event.getCode(), true));
        scene.setOnKeyReleased(event -> keys.put(event.getCode(), false));
        primaryStage.setTitle("Jurumpsi");
        primaryStage.setScene(scene);
        primaryStage.show();
        checkCollisions();
        AnimationTimer timer = new AnimationTimer() {
            @Override
            public void handle(long now) {
                update();
            }
        };
    }

    //Started die gesamte Anwendung
    
    public static void main(String[] args) {

        launch(args);
    }
}
