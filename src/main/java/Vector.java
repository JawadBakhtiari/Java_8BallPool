import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

public class Vector {
    double x;
    double y;

    public Vector(double x, double y) {
        this.x = x;
        this.y = y;
    }

    public Vector add(Vector v) {   // add velocity to P1 and get P2
        return new Vector(this.x + v.x, this.y + v.y);
    }

    public Vector sub(Vector v) {   // Subtract P2 from P1 and get velocity.
        return new Vector(this.x - v.x, this.y - v.y);
    }

    public double mag() {           // Pythagorous theorm to find magnitude of vector
        return Math.sqrt(Math.pow(this.x, 2) + Math.pow(this.y, 2));
    }

    public Vector mult(double n) {
        return new Vector(this.x * n, this.y * n);
    }

    public Vector normal() {    // get perpendicular unit vector version of the same vector
        return new Vector(-this.y, this.x).unit();
    }

    public Vector unit() {  // returns unit Vector version of the vector (what the x and y comp would be if the magnitude of the vector is 1.
        if(mag()==0)
            return new Vector(0,0);
        return new Vector(this.x / this.mag() , this.y / this.mag());
    }

    public static double dot(Vector v1, Vector v2) {
        return v1.x * v2.x + v1.y * v2.y;
    }

    public void drawVec(GraphicsContext g2, Vector vector, int size) {
        g2.strokeLine(vector.x, vector.y, (int) (vector.x + this.x * size), (int) (vector.y + this.y * size));
    }

}
