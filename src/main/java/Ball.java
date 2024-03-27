import javafx.scene.Scene;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

public abstract class Ball {
    Main gp;

    // Ball properties
    public Color color;
    public int radius;

    // Vector components
    public Vector pos;
    public Vector initial_pos;
    public Vector vel;
    public Vector acc;

    // Ball physics
    public int acceleration;
    public double mass;
    public double inv_m;
    public double elasticity;
    public double friction;

    public boolean pocketed;

    public Ball(int x, int y, int radius, int vel_x, int vel_y, double mass, double friction, Color color) {
        this.color = color;
        this.pos = new Vector(x + GameEngine.poc_TopLeft.x,y + GameEngine.poc_TopLeft.y);
        setBallPos((int) this.pos.x, (int) this.pos.y, radius);
        this.vel = new Vector(vel_x,vel_y);
        this.radius = radius;
        this.mass = mass;
        this.friction = friction;   // Friction is from 0 = no friction to 100 which means the ball won't even move.

        setDefaultValues();
    }

    public void setBallPos(int x, int y, int radius) {
        if(x <= GameEngine.poc_TopLeft.x)
            pos.x = GameEngine.poc_TopLeft.x + radius + 1;
        else if (x >= GameEngine.poc_TopRight.x)
            pos.x = GameEngine.poc_TopRight.x - radius - 1;
        else {
            pos.x = x;
        }
        if(y<=GameEngine.poc_TopLeft.y)
            pos.y = GameEngine.poc_TopLeft.y + radius + 1;
        else if(y>=GameEngine.poc_BotLeft.y)
            pos.y = GameEngine.poc_BotLeft.y - radius - 1;
        else {
            pos.y = y;
        }
        initial_pos = new Vector(pos.x, pos.y);
    }

    public void setDefaultValues() {
        // Set players default position
        this.acc = new Vector(0,0);
        this.acceleration = 20;
        this.elasticity = 1.001;        // how hard the balls bounce off each other after contact
        if (this.mass == 0) {
            this.inv_m = 0;
        } else {
            this.inv_m = 1 / this.mass;
        }

        this.pocketed = false;
    }

    abstract void update(Scene scene);

    abstract public void draw(GraphicsContext gc);

    public void reset() {   // Reset a ball position to initial position.
        this.pos = new Vector(initial_pos.x, initial_pos.y);
        vel.x = 0;
        vel.y = 0;
        this.pocketed = false;
    }

}
