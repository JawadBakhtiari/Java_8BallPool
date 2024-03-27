import javafx.scene.Scene;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.shape.ArcType;

public class OtherBall extends Ball {

    public OtherBall(int x, int y, int radius, int vel_x, int vel_y, double mass, double friction, Color color) {
        super(x, y, radius, vel_x, vel_y, mass, friction, color);
    }

    public void update(Scene scene) {
        acc = acc.unit().mult(acceleration);

        // add acceleration to velocity
        vel = vel.add(acc);     // add acceleration to velocity
        vel = vel.mult((100 - friction)/100); // add friction to velocity

        // add Velocity to position // this x and y represents top left of the circle.
        if((vel.x < 0.01 && vel.x > -0.01) && (vel.y < 0.01 && vel.y > -0.01)) {
            vel.x = 0;
            vel.y = 0;
        }

        pos = pos.add(vel);
    }


    public void draw(GraphicsContext g2) {
        // Color of Circle
        g2.setFill(this.color);

        // Draw Circle
        g2.fillArc((int) this.pos.x - this.radius, (int) this.pos.y - this.radius, this.radius * 2, this.radius * 2, 0, 360, ArcType.ROUND);
    }
}
