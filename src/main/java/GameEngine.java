import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class GameEngine {
    private Main gp;
    public static int scale;
    public static boolean staticBall = true;       // if false it means at least one ball is still moving.
    private List<Ball> ballContainer = new ArrayList<>();    // Stores all the balls
    private Table table;
    private List<Wall> wallContainer = new ArrayList<>();    // Stores all the balls
    private long startTime = System.currentTimeMillis();
    private long currentTime;
    private boolean gameWon = false;
    private String timer = "0";

    // Corners of table
    public static Vector poc_TopLeft;
    public static Vector poc_TopRight;
    public static Vector poc_BotLeft;
    public static Vector poc_BotRight;


    public GameEngine(Main gp, InputStream configStream) throws Exception {
        this.gp = gp;
        buildTableAndBall(configStream);
        buildWalls();
    }

    public void buildTableAndBall(InputStream stream) throws Exception {
        JSONObject obj = (JSONObject) new JSONParser().parse(new InputStreamReader(stream));

        // getting Table object info from JSON
        JSONObject tableJSON = (JSONObject) obj.get("Table");

        String colorString = (String) tableJSON.get("colour");      // find a way of converting a string color to actual color

        Color actualTableColor = Color.valueOf(colorString);

        double friction = (double) tableJSON.get("friction");

        JSONObject tableSize = (JSONObject) tableJSON.get("size");
        long tableWidth = (long) tableSize.get("x");
        long tableHeight = (long) tableSize.get("y");

        // Creating table
        this.table = new Table(actualTableColor, (int) tableWidth, (int) tableHeight);

        // Getting Ball info from JSON
        JSONObject ballJSON = (JSONObject) obj.get("Balls");
        JSONArray balls = (JSONArray) ballJSON.get("ball");

        for(int i = 0; i < balls.size(); i++) {
            JSONObject singleBall = (JSONObject) balls.get(i);

            String ballColor = (String) singleBall.get("colour");

            Color actualBallColour = Color.valueOf(ballColor);

            JSONObject ballPos = (JSONObject) singleBall.get("position");

            double posX = (double) ballPos.get("x");
            double posY = (double) ballPos.get("y");

            JSONObject ballVel = (JSONObject) singleBall.get("velocity");
            double velX = (double) ballVel.get("x");
            double velY = (double) ballVel.get("y");

            double mass = (double) singleBall.get("mass");

            Ball ball;
            if(actualBallColour == Color.WHITE) {
                ball = new ControlBall((int) posX, (int) posY, 20/scale, (int) velX, (int) velY, mass, friction, actualBallColour);
            }
            else {
                ball = new OtherBall((int) posX, (int) posY, 20/scale, (int) velX, (int) velY, mass, friction, actualBallColour);
            }

            ballContainer.add(ball);
        }

    }

    public void whiteBallReset() {    // When white ball goes in
        for(int i = 0; i < ballContainer.size(); i++) {
            ballContainer.get(i).reset();
        }
        Main.score = 0;
        startTime = System.currentTimeMillis();
    }

    public void gameWon() {
        System.out.println("win and bye");
        gameWon = true;
    }

    public void buildWalls() {
        int pocketSize = 50/scale;

        wallContainer.add(new Wall(poc_TopLeft.x + pocketSize, poc_TopLeft.y, Main.screenWidth / 2 - pocketSize, poc_TopRight.y)); // Top Left
        wallContainer.add(new Wall(Main.screenWidth / 2 + pocketSize, poc_TopLeft.y, poc_TopRight.x - pocketSize, poc_TopRight.y)); // Top Right
        wallContainer.add(new Wall(poc_TopRight.x, poc_TopRight.y + pocketSize, poc_BotRight.x, poc_BotRight.y - pocketSize)); // Right Edge
        wallContainer.add(new Wall(poc_TopLeft.x, poc_TopLeft.y + pocketSize, poc_BotLeft.x, poc_BotLeft.y - pocketSize)); // left Edge
        wallContainer.add(new Wall(poc_TopLeft.x + pocketSize, poc_BotLeft.y, Main.screenWidth / 2 - pocketSize, poc_BotRight.y)); // bot Left
        wallContainer.add(new Wall(Main.screenWidth / 2 + pocketSize, poc_BotLeft.y, poc_TopRight.x - pocketSize, poc_BotRight.y)); // bot Right
    }



    public void update() {  // Updates information of entities
        // Game status
        if(gameWon==false) {
            currentTime = System.currentTimeMillis() - startTime;
        }

        // Iterate through the balls
        staticBall = true;
        for(int i=0; i < ballContainer.size() ;i++) {
            // confirm if ball is moving
            if(ballContainer.get(i).vel.x!=0 || ballContainer.get(i).vel.y!=0)
                staticBall = false;
            if(ballContainer.get(i).pocketed == true)
                staticBall = true;
            // update each ball information
            ballContainer.get(i).update(gp.scene);
            if(coll_det_outside(ballContainer.get(i)) && ballContainer.get(i).pocketed == false) {
                ballContainer.get(i).pocketed = true;

                if(ballContainer.get(i).color==Color.WHITE) {   // White ball has gone in
                    whiteBallReset();
                }

                else {  // Other ball gone in
                    Main.score += 1;
                    if(Main.score == ballContainer.size() - 1)
                        gameWon();
                }
            }


            // Iterate through walls, to check for ball and wall collision
            for(int k=0;k<wallContainer.size();k++) {
                if(coll_det_bw(ballContainer.get(i), wallContainer.get(k))) {
                    pen_res_bw(ballContainer.get(i), wallContainer.get(k));
                    coll_res_bw(ballContainer.get(i), wallContainer.get(k));
                }
            }

            // Ball Collision Response (Check this ball with all other balls to make sure it's not colliding inside).
            for(int j=i+1;j<ballContainer.size();j++) {
                if(coll_det_bb(ballContainer.get(i), ballContainer.get(j))) {
                    pen_res_bb(ballContainer.get(i),ballContainer.get(j));
                    coll_res_bb(ballContainer.get(i), ballContainer.get(j));
                }
            }

        }

    }

    public void draw(GraphicsContext gc) { // This graphics class is what we use to draw on the screen, think of it as as your pencil/paintbrush
        if(gameWon) {
            gc.clearRect(0, 0, Main.screenWidth, Main.screenHeight);

            table.drawTable(gc);

            // Iterate through the walls
            for (int i = 0; i < wallContainer.size(); i++) {
                wallContainer.get(i).draw(gc);
            }

            String time = String.valueOf(currentTime);
            if(currentTime>1000) {
                timer = time.substring(0, time.length() - 3);
            }
            gc.fillText("Score: " + String.valueOf(Main.score), poc_TopRight.x - 75, poc_TopLeft.y - 75);
            gc.fillText("Time Elapsed: " + timer + " seconds", poc_TopLeft.x + 75, poc_TopLeft.y - 75);
            gc.fillText("win and bye", poc_TopLeft.x + table.width/2 - 30, poc_TopLeft.y - 75);
        }

        else {

            gc.clearRect(0, 0, Main.screenWidth, Main.screenHeight);

            table.drawTable(gc);

            // Iterate through the balls
            for (int i = 0; i < ballContainer.size(); i++) {
                if (ballContainer.get(i).pocketed)
                    continue;
                ballContainer.get(i).draw(gc);
            }

            // Iterate through the walls
            for (int i = 0; i < wallContainer.size(); i++) {
                wallContainer.get(i).draw(gc);
            }

            String time = String.valueOf(currentTime);
            if(currentTime>1000) {
                timer = time.substring(0, time.length() - 3);
            }

            gc.fillText("Score: " + String.valueOf(Main.score), poc_TopRight.x - 75, poc_TopLeft.y - 75);
            gc.fillText("Time Elapsed: " + timer + " seconds", poc_TopLeft.x + 75, poc_TopLeft.y - 75);
        }
    }

    public Vector closestPointBW(Ball b1, Wall w1) {
        Vector ballToWallStart = w1.start.sub(b1.pos);
        if(Vector.dot(w1.wallUnit(), ballToWallStart) > 0) {
            return w1.start;
        }

        Vector wallEndToBall = b1.pos.sub(w1.end);
        if(Vector.dot(w1.wallUnit(), wallEndToBall) > 0) {
            return  w1.end;
        }

        double closestDist = Vector.dot(w1.wallUnit(), ballToWallStart);
        Vector closestVect = w1.wallUnit().mult(closestDist);
        return  w1.start.sub(closestVect);
    }

    public boolean coll_det_bb(Ball b1, Ball b2) {  // Detects collision between two balls
        if(b1.radius + b2.radius >= b2.pos.sub(b1.pos).mag()) {
            return true;
        }
        return false;
    }

    public boolean coll_det_bw(Ball b1, Wall w1) {
        Vector ballToClosest = closestPointBW(b1, w1).sub(b1.pos);
        if (ballToClosest.mag() <= b1.radius) {
            return true;
        }
        return false;
    }

    public boolean coll_det_outside(Ball b1) {
        if(b1.pos.x < poc_TopLeft.x || b1.pos.x > poc_TopRight.x || b1.pos.y < poc_TopRight.y || b1.pos.y > poc_BotRight.y) {
            return true;
        }
        return false;
    }

    public void pen_res_bb(Ball b1, Ball b2) {
        Vector dist = b1.pos.sub(b2.pos);
        double pen_depth = b1.radius + b2.radius - dist.mag();
        Vector pen_res = dist.unit().mult(pen_depth/ (b1.inv_m + b2.inv_m));
        b1.pos = b1.pos.add(pen_res.mult(b1.inv_m));
        b2.pos = b2.pos.add(pen_res.mult(-b2.inv_m));
    }

    public void pen_res_bw(Ball b1, Wall w1) {
        Vector penVect = b1.pos.sub(closestPointBW(b1, w1));
        b1.pos = b1.pos.add(penVect.unit().mult(b1.radius-penVect.mag()));
    }

    public void coll_res_bw(Ball b1, Wall w1) {
        Vector normal = b1.pos.sub(closestPointBW(b1, w1)).unit();
        double sepVel = Vector.dot(b1.vel, normal);
        double new_sepVel = -sepVel * b1.elasticity;
        double vsep_diff = sepVel - new_sepVel;
        b1.vel = b1.vel.add(normal.mult(-vsep_diff));
    }

    public void coll_res_bb(Ball b1, Ball b2) {
        Vector normal = b1.pos.sub(b2.pos).unit();
        Vector relVel = b1.vel.sub(b2.vel);
        double sepVel = Vector.dot(relVel, normal);
        double new_sepVel = -sepVel * Math.min(b1.elasticity, b2.elasticity);

        double vsep_diff = new_sepVel - sepVel;
        double impulse = vsep_diff / (b1.inv_m + b2.inv_m);
        Vector impulseVec = normal.mult(impulse);

        b1.vel = b1.vel.add(impulseVec.mult(b1.inv_m));
        b2.vel = b2.vel.add(impulseVec.mult(-b2.inv_m));
    }
}