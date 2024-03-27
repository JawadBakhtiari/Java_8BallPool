import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.shape.ArcType;

public class Table {
    public Color color;
    public int width;
    public int height;
    
    public Table(Color color, int tableWidth, int tableHeight) {
        this.color = color;
        this.width = tableWidth;
        this.height = tableHeight;
        GameEngine.scale = 1280 / tableWidth;
        Main.screenWidth = tableWidth + 200;
        Main.screenHeight = tableHeight + 200;
        buildPockets();
    }

    public void buildPockets() {
        GameEngine.poc_TopLeft = new Vector(Main.screenWidth/2 - width/2, Main.screenHeight/2 - height/2);
        GameEngine.poc_TopRight = new Vector(Main.screenWidth/2 + width/2, Main.screenHeight/2 - height/2);
        GameEngine.poc_BotLeft = new Vector(Main.screenWidth/2 - width/2, Main.screenHeight/2 + height/2);
        GameEngine.poc_BotRight = new Vector(Main.screenWidth/2 + width/2, Main.screenHeight/2 + height/2);
    }
    
    public void drawTable(GraphicsContext gc) {
        // Draw Outer Frame
        int outerFrameSize = 60/GameEngine.scale;
        gc.setFill(Color.BLACK);
        gc.fillRect((int) GameEngine.poc_TopLeft.x - outerFrameSize, (int) GameEngine.poc_TopLeft.y - outerFrameSize, width + 2*(outerFrameSize),height + 2*(outerFrameSize));

        // Draw Inner Frame
        int innerFrameSize = 50/GameEngine.scale;
        gc.setFill(Color.DARKGRAY);
        gc.fillRect((int) GameEngine.poc_TopLeft.x - innerFrameSize, (int) GameEngine.poc_TopLeft.y - innerFrameSize, width + 2*(innerFrameSize),height + 2*(innerFrameSize));

        // Draw table canvas
        gc.setFill(this.color);
        gc.fillRect((int) GameEngine.poc_TopLeft.x, (int) GameEngine.poc_TopLeft.y, width, height);

        // Draw Circle Pockets
        int pocketSize = 50/GameEngine.scale;
        gc.setFill(Color.BLACK);
        gc.fillArc((int) GameEngine.poc_TopLeft.x - pocketSize, (int) GameEngine.poc_TopLeft.y - pocketSize, 2 * pocketSize, 2 * pocketSize, 0,270, ArcType.ROUND);   // Top left
        gc.fillArc((int) GameEngine.poc_TopRight.x - pocketSize, (int) GameEngine.poc_TopRight.y - pocketSize, 2 * pocketSize, 2 * pocketSize, -90,270, ArcType.ROUND); // Top Right
        gc.fillArc((int) GameEngine.poc_BotRight.x - pocketSize, (int) GameEngine.poc_BotRight.y - pocketSize, 2 * pocketSize, 2 * pocketSize, 180,270, ArcType.ROUND); // Bot Right
        gc.fillArc((int) GameEngine.poc_BotLeft.x - pocketSize, (int) GameEngine.poc_BotLeft.y - pocketSize, 2 * pocketSize, 2 * pocketSize, 90,270, ArcType.ROUND);  // Bot Left
        gc.fillArc((int) GameEngine.poc_TopRight.x - width/2 - pocketSize, (int) GameEngine.poc_TopLeft.y - pocketSize, 2 * pocketSize, 2 * pocketSize, 0,180, ArcType.ROUND); // Top middle
        gc.fillArc((int) GameEngine.poc_TopRight.x - width/2 - pocketSize, (int) GameEngine.poc_BotLeft.y - pocketSize, 2 * pocketSize, 2 * pocketSize, 180,180, ArcType.ROUND); // Bot middle
    }
    
}
