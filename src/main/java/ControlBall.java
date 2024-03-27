import javafx.scene.Scene;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.MouseButton;
import javafx.scene.paint.Color;
import javafx.scene.shape.ArcType;


public class ControlBall extends Ball{
    double acc_Cap;
    public boolean leftClickPressed;
    public int mouse_X;
    public int mouse_y;
    public boolean onWhite;

    public ControlBall(int x, int y, int radius, int vel_x, int vel_y, double mass, double friction, Color color) {
        super(x, y, radius, vel_x, vel_y, mass, friction, color);
        this.acc_Cap = 40/GameEngine.scale;
        this.leftClickPressed = false;
        this.mouse_X = 0;
        this.mouse_y = 0;
        this.onWhite = false;
    }

    @Override
    public void update(Scene scene) {
        scene.setOnMouseDragged(event -> {
            mouse_X = (int) event.getX();
            mouse_y = (int) event.getY();
        });


        scene.setOnMousePressed(event -> {
            if (event.getButton() == MouseButton.PRIMARY) {
                leftClickPressed = true;
            }
        });

        scene.setOnMouseReleased(event -> {
            if (event.getButton() == MouseButton.PRIMARY) {
                leftClickPressed = false;
            }
        });

        move();
    }

    public void move() {
        if(leftClickPressed && GameEngine.staticBall && onWhite==false) {  // check if left click is pressed, and the game is static and the white ball has not been previously pressed
            clickWhite();  // Check if user is pressing the white ball
        }

        if(leftClickPressed && GameEngine.staticBall && onWhite) { // Check if left click is pressed and the game is static and the white ball has not previously been pressed
            acc.x = (mouse_X - pos.x)/10;
            acc.y = (mouse_y - pos.y)/10;

            double radius = acc.mag();

            if(radius > acc_Cap) {
                if(acc.x >= 0 && acc.y >= 0) {  // bottom right
                    acc.x = acc_Cap * Math.cos(Math.atan(acc.y/acc.x));
                    acc.y = acc_Cap * Math.sin(Math.atan(acc.y/acc.x));
                }
                else if (acc.x < 0 && acc.y >= 0) { // bottom left
                    acc.x = -1 * acc_Cap * Math.cos(Math.atan(acc.y/acc.x));
                    acc.y = -1 * acc_Cap * Math.sin(Math.atan(acc.y/acc.x));
                }

                else if (acc.x < 0 && acc.y < 0) { // Top left
                    acc.x = -1 * acc_Cap * Math.cos(Math.atan(acc.y/acc.x));
                    acc.y = -1 * acc_Cap * Math.sin(Math.atan(acc.y/acc.x));
                }

                else if (acc.x > 0 && acc.y < 0) { // Top Right
                    acc.x = acc_Cap * Math.cos(Math.atan(acc.y/acc.x));
                    acc.y = acc_Cap * Math.sin(Math.atan(acc.y/acc.x));
                }

            }
        }

        if(!leftClickPressed && GameEngine.staticBall) {   // if left click is let go and game is static add the velocity
            acc.x = acc.x/2;
            acc.y = acc.y/2;

            vel = vel.add(acc);     // add acceleration to velocity
            acc.x = 0;
            acc.y = 0;
            onWhite = false;
        }

        //vel = vel.add(acc);     // add acceleration to velocity
        vel = vel.mult((100 - friction)/100); // add friction to velocity

        // add Velocity to position // this x and y represents top left of the circle.

        if((vel.x < 0.01 && vel.x > -0.01) && (vel.y < 0.01 && vel.y > -0.01)) {
            vel.x = 0;
            vel.y = 0;
        }


        pos = pos.add(vel);
    }

    public void clickWhite() {
        double distance_x = pos.x - mouse_X;
        double distance_y = pos.y - mouse_y;
        if((distance_x < 25 && distance_x > -25) && (distance_y < 25 && distance_y > -25)) {
            onWhite = true;
        }
        else {
            onWhite = false;
        }
    }

    @Override
    public void draw(GraphicsContext g2) {
        // Colour of ball
        g2.setFill(Color.WHITE);

        // Draw Circle (ball)
        g2.fillArc((int) this.pos.x - this.radius, (int) this.pos.y - this.radius, this.radius * 2, this.radius * 2, 0, 360, ArcType.ROUND);

        // Draw guiding line
        g2.setLineWidth(1);
        acc.drawVec(g2, this.pos, 10);


        Vector cueTip = new Vector(-acc.x, -acc.y).mult(5);
        double x1 = this.pos.x + cueTip.x;
        double y1 = this.pos.y + cueTip.y;
        Vector end = new Vector(x1, y1);

        // Draw Cue Stick
        g2.setLineWidth(7);
        cueTip.unit().drawVec(g2, end, 500/GameEngine.scale);
        g2.setLineWidth(1);
    }
}
