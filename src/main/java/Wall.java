import javafx.scene.canvas.GraphicsContext;

public class Wall {
    Vector start;
    Vector end;

    public Wall(double x1, double y1, double x2, double y2) {
        this.start = new Vector(x1, y1);
        this.end = new Vector(x2, y2);
    }

    public Vector wallUnit() {
        return this.end.sub(this.start).unit();
    }

    public void draw(GraphicsContext g2) {
        g2.setLineWidth(5/GameEngine.scale);

        g2.strokeLine( (int) this.start.x,  (int) this.start.y, (int) this.end.x, (int) this.end.y);

        g2.setLineWidth(1);
    }
}
