import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import javafx.scene.layout.VBox;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import java.awt.Font;
import java.io.*;
import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Point2D;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import java.lang.Object;
import javafx.scene.Node;
import javafx.scene.image.ImageView;
import javafx.scene.image.Image;
import java.net.URL;

/**
 * @author Eric Ruppert, Jakob Brüderlein, Killian Gerschütz
 * @version 0.9
 */
public class Main extends Application {

    private HashMap<KeyCode, Boolean> keys = new HashMap<KeyCode, Boolean>();

    private ArrayList<Node> platforms = new ArrayList<Node>();
    private ArrayList<Node> coins = new ArrayList<Node>();
    private ArrayList<Node> ends = new ArrayList<Node>();
    private ArrayList<Node> plats = new ArrayList<Node>();
    private ArrayList<Node> kills = new ArrayList<Node>();

    private Pane appRoot;
    @FXML
    private Pane gameRoot;
    @FXML
    private Pane uiRoot;

    @FXML
    private Node player;

    private Point2D playerVelocity = new Point2D(0, 0);
    private boolean canJump = true;

    private int levelWidth1;
    private int levelWidth2;

    private Config config;
    private int score2;
    @FXML
    private SimpleIntegerProperty score;
    @FXML
    private Text textScore;
    public int test;
    /** Die Methode checkt ob Knöpfe gedrückt wurden und bewegt den Spieler*/
    private void update() {
        if (isPressed(KeyCode.W) && player.getTranslateY() >= 5) {
            jumpPlayer();
        }

        if (isPressed(KeyCode.A) && player.getTranslateX() >= 5) {
            movePlayerX(-5);
        }

        if (isPressed(KeyCode.D) && player.getTranslateX() + config.getPlayerSize() <= levelWidth1 - 5) {
            movePlayerX(5);
        }

        if (playerVelocity.getY() < 10) {
            playerVelocity = playerVelocity.add(0, 1);
        }

        movePlayerY((int)playerVelocity.getY());
        /** Dieser Teil lässt die Coins beim einsammeln verschwinden und erhöht den Score*/
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

        for (Node kill : kills) {
            if (player.getBoundsInParent().intersects(kill.getBoundsInParent())) {
                kill.getProperties().put("alive", false);
                player.getProperties().put("alive", false);
            }
        }

        for (Iterator<Node> it = kills.iterator(); it.hasNext(); ) {
            Node kill = it.next();
            if (!(Boolean)kill.getProperties().get("alive")) {
                it.remove();
                gameRoot.getChildren().remove(kill);
                gameRoot.getChildren().remove(player);
                gameRoot.getChildren().clear();
            }
        }

    }
    private ImageView coin;
    /** Diese Methode Erstellt das End-Objekt*/
    private Node createEnd(int x, int y, int w, int h){
        Button button3 = new Button("Next");
        button3.setTranslateX(x-50);
        button3.setTranslateY(y);
        button3.getProperties().put("alive", true);
        button3.setPrefHeight(55);
        button3.setPrefWidth(150);
        button3.setOnAction(e -> button3.setText("Level Fertig!"));
        button3.setStyle("-fx-font: 18 arial;");
        gameRoot.getChildren().add(button3);
        return button3;
    }
    private ImageView kill;
    /** das kill objekt wird erstellt
     * 
     */  private Node createKill(int x, int y) throws java.io.FileNotFoundException{
        this.kill = new ImageView(new Image(getClass().getResourceAsStream("/assets/günterstanding2.png")));
        kill.setTranslateX(x);
        kill.setTranslateY(y-30);
        kill.prefHeight(50);
        kill.prefWidth(100);
        kill.getProperties().put("alive", true);
        kill.setVisible(true);
        gameRoot.getChildren().add(kill);
        return kill;
    }

    /** Die methode definiert die coins*/
    private Node createCoin(int x, int y) throws java.io.FileNotFoundException{
        this.coin = new ImageView(new Image(getClass().getResourceAsStream("/assets/coin.png")));
        coin.setTranslateX(x);
        coin.setTranslateY(y-30);
        coin.prefHeight(50);
        coin.prefWidth(100);
        coin.getProperties().put("alive", true);
        coin.setVisible(true);
        gameRoot.getChildren().add(coin);
        return coin;
    }

    /** Diese Methode bewegt den Spieler nach Links und Rechts*/
    private void movePlayerX(int value) {
        boolean movingRight = value > 0;

        for (int i = 0; i < Math.abs(value); i++) {
            for (Node platform : platforms) {
                if (player.getBoundsInParent().intersects(platform.getBoundsInParent())) {
                    if (movingRight) {
                        if (player.getTranslateX() + config.getPlayerSize() == platform.getTranslateX()) {
                            return;
                        }
                    }
                    else {
                        if (player.getTranslateX() == platform.getTranslateX() + config.getBlockSize()) {
                            return;
                        }
                    }
                }
            }
            player.setTranslateX(player.getTranslateX() + (movingRight ? 1 : -1));
        }
    }

    /**Die Methode lässt den Spieler springen*/
    private void movePlayerY(int value) {
        boolean movingDown = value > 0;

        for (int i = 0; i < Math.abs(value); i++) {
            for (Node platform : platforms) {
                if (player.getBoundsInParent().intersects(platform.getBoundsInParent())) {
                    if (movingDown) {
                        if (player.getTranslateY() + config.getPlayerSize() == platform.getTranslateY()) {
                            player.setTranslateY(player.getTranslateY() - 1);
                            canJump = true;
                            return;
                        }
                    }
                    else {
                        if (player.getTranslateY() == platform.getTranslateY() + config.getBlockSize()) {
                            return;
                        }
                    }
                }
            }
            player.setTranslateY(player.getTranslateY() + (movingDown ? 1 : -1));
        }
    }

    /** Diese methode checkt ob der Spieler gesprungen ist, und lässt ihn nicht nochmal springen*/
    private void jumpPlayer() {
        if (canJump) {
            playerVelocity = playerVelocity.add(0, -30);
            canJump = false;
        }
    }

    /** Diese Methode erstellt die Blöcke*/
    private Node createEntity(int x, int y, int w, int h, Color color) {
        Rectangle entity = new Rectangle(w, h);
        entity.setTranslateX(x);
        entity.setTranslateY(y);
        entity.setFill(color);
        entity.getProperties().put("alive", true);

        gameRoot.getChildren().add(entity);
        return entity;
    }

    private boolean isPressed(KeyCode key) {
        return keys.getOrDefault(key, false);
    }

    /** Das Array Für das Level wird eingelesen und in die Welt übersetzt
     */
    @Override
    public void init() throws Exception {
        config = Config.getInstance();

        FXMLLoader loader = new FXMLLoader(getClass().getResource("game.fxml"));
        loader.setController(this);
        appRoot = loader.load();

        textScore.textProperty().bind(score.asString());
        levelWidth1 = LevelData.Level1[0].length() * config.getBlockSize();
        int leveldata1= LevelData.Level1.length;
        int leveldata2= LevelData.Level2.length;
        if(test == 1){
            leveldata1 = leveldata2;
        }
        for (int i = 0; i < leveldata2; i++) {
            String line = LevelData.Level1[i];
            String line2 = LevelData.Level2[i];
            if (test == 1){
                line = line2;
            }
            for (int j = 0; j < line.length(); j++) {
                switch (line.charAt(j)) {
                    case '0':
                        break;
                    case '1':
                        Node platform = createEntity(j*config.getBlockSize(), i*config.getBlockSize(), config.getBlockSize(), config.getBlockSize(), Color.BROWN);
                        platforms.add(platform);
                        break;
                    case '2':
                        Node coin = createCoin(j*config.getBlockSize(), i*config.getBlockSize());
                        coins.add(coin);
                        break;
                    case '3':
                        Node end = createEnd(j*config.getBlockSize(), i*config.getBlockSize(), config.getBlockSize(), config.getBlockSize());
                        ends.add(end);
                        break;
                    case '4':
                        Node kill = createKill(j*config.getBlockSize(), i*config.getBlockSize());
                        kills.add(kill);
                        break;
                }
            }
        }
        /**Der Spieler wird erstellt 
         */
        player.translateXProperty().addListener((obs, old, newValue) -> {
                int offset = newValue.intValue();

                if (offset > config.getAppWidth() / 2 && offset < levelWidth1 - config.getAppWidth() / 2) {
                    gameRoot.setLayoutX(-(offset - config.getAppWidth() / 2));
                }
            });
    }

    /**
     * Methode start
     *Diese Methode erstellt alle Grafischen Elemente
     */
    @Override
    public void start(Stage primaryStage) throws Exception {
        Pane layout = new Pane();
        Scene scene = new Scene(appRoot);
        Scene scene1 = new Scene(layout);
        FileInputStream input = new FileInputStream("hintergrund.png");
        Image image = new Image(input);
        ImageView hintergrund = new ImageView(image);
        primaryStage.setFullScreen(true);
        Button button = new Button("Start Game");
        button.setPrefHeight(80);
        button.setPrefWidth(400);
        button.setLayoutX(780);
        button.setLayoutY(400);
        button.setStyle("-fx-font: 36 arial;");
        button.setOnAction(e -> primaryStage.setScene(scene));
        Button button1 = new Button("Exit Game");
        button1.setPrefHeight(80);
        button1.setPrefWidth(400);
        button1.setLayoutX(780);
        button1.setLayoutY(600);
        button1.setStyle("-fx-font: 36 arial;");
        button1.setOnAction(e -> primaryStage.hide());
        Button button2 = new Button("Main Menu");
        button2.setPrefHeight(40);
        button2.setPrefWidth(300);
        button2.setStyle("-fx-font: 36 arial;");
        button2.setOnAction(e -> primaryStage.setScene(scene1));
        Label label1 = new Label("Jurumpsi");
        label1.setPrefHeight(80);
        label1.setPrefWidth(200);
        label1.setLayoutX(900);
        label1.setLayoutY(150);
        label1.setMaxSize(500, 300);
        label1.setStyle("-fx-font: 36 arial;");
        scene.setOnKeyPressed(event -> keys.put(event.getCode(), true));
        scene.setOnKeyReleased(event -> keys.put(event.getCode(), false));
        primaryStage.setTitle("Jurumpsi");
        primaryStage.setScene(scene1);
        primaryStage.show();
        appRoot.getChildren().addAll(button2);
        layout.getChildren().addAll(hintergrund, button, button1, label1);
        /** Ein neuer Timer welcher die methode Update aufruft wird erstellt*/
        AnimationTimer timer = new AnimationTimer() {
                @Override
                public void handle(long now) {
                    update();
                }
            };
        timer.start();
    }

    /** Started die Gesamte Anwendung
     * 
     */
    public static void Main(String[] args) {
        launch(args);
    }
}