import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import javafx.util.Duration;

import java.io.InputStream;

public class Main extends Application {
    public static int screenWidth = 1280;   // 48 * 16 = 768 pixels
    public static int screenHeight = 720;  // 48 * 12 = 576 pixels
    public Thread gameThread;
    public GraphicsContext gc;
    public Scene scene;
    Stage primaryStage;
    GameEngine gameEngine;

    // GameStatus
    public static int score;

    @Override
    public void start(Stage primaryStage) throws Exception {
        // IMPORT JSON STUFF

        InputStream in = getClass().getResourceAsStream("/config.json");
        gameEngine = new GameEngine(this, in);

        this.primaryStage = primaryStage;

        primaryStage.setResizable(false);
        primaryStage.setTitle("8 Ball pool");

        Canvas canvas = new Canvas(screenWidth, screenHeight);  // FullScreen
        this.gc = canvas.getGraphicsContext2D(); // Graphics object draws game
        Timeline tl = new Timeline(new KeyFrame(Duration.millis(10), event -> run(gc)));
        tl.setCycleCount(Timeline.INDEFINITE);


        Scene scene = new Scene(new StackPane(canvas));
        this.scene = scene;
        scene.setFill(Color.WHITE);
        primaryStage.setScene(scene);

        primaryStage.show();

        primaryStage.setOnCloseRequest(new EventHandler<WindowEvent>() {
            @Override
            public void handle(WindowEvent event) {
                gameThread = null;
                Platform.exit();
            }
        });

        //startGameThread();
        tl.play();
    }

    //@Override
    public void run(GraphicsContext gc) {
        gameEngine.update();

        gameEngine.draw(gc);
    }
}

