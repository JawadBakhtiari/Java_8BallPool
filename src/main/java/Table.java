import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.shape.ArcType;

public class Table {
    private static final int REFERENCE_TABLE_WIDTH = 1280;
    private static final int SCREEN_MARGIN = 200;
    public Color color;
    public int width;
    public int height;
    
    public Table(Color color, int tableWidth, int tableHeight) {
        this.color = color;
        this.width = tableWidth;
        this.height = tableHeight;
        GameEngine.scale = REFERENCE_TABLE_WIDTH / tableWidth;
        Main.screenWidth = tableWidth + SCREEN_MARGIN;
        Main.screenHeight = tableHeight + SCREEN_MARGIN;
        buildPockets();
    }

    public void buildPockets() {
        Vector centerScreen = new Vector(Main.screenWidth / 2.0,Main.screenHeight / 2.0);
        Vector halfTableSize = new Vector(width / 2.0,height / 2.0);
        GameEngine.pocTopLeft = new Vector(centerScreen.x - halfTableSize.x, centerScreen.y - halfTableSize.y);
        GameEngine.pocTopRight = new Vector(centerScreen.x + halfTableSize.x, centerScreen.y - halfTableSize.y);
        GameEngine.pocBotLeft = new Vector(centerScreen.x - halfTableSize.x, centerScreen.y + halfTableSize.y);
        GameEngine.pocBotRight = new Vector(centerScreen.x + halfTableSize.x, centerScreen.y + halfTableSize.y);
    }
    
    public void drawTable(GraphicsContext gc) {
        // Draw Outer Frame
        int outerFrameSize = 60/GameEngine.scale;
        gc.setFill(Color.BLACK);
        gc.fillRect((int) GameEngine.pocTopLeft.x - outerFrameSize, (int) GameEngine.pocTopLeft.y - outerFrameSize, width + 2*(outerFrameSize),height + 2*(outerFrameSize));

        // Draw Inner Frame
        int innerFrameSize = 50/GameEngine.scale;
        gc.setFill(Color.DARKGRAY);
        gc.fillRect((int) GameEngine.pocTopLeft.x - innerFrameSize, (int) GameEngine.pocTopLeft.y - innerFrameSize, width + 2*(innerFrameSize),height + 2*(innerFrameSize));

        // Draw table canvas
        gc.setFill(this.color);
        gc.fillRect((int) GameEngine.pocTopLeft.x, (int) GameEngine.pocTopLeft.y, width, height);

        // Draw Circle Pockets
        int pocketSize = 50/GameEngine.scale;
        gc.setFill(Color.BLACK);
        gc.fillArc((int) GameEngine.pocTopLeft.x - pocketSize, (int) GameEngine.pocTopLeft.y - pocketSize, 2 * pocketSize, 2 * pocketSize, 0,270, ArcType.ROUND);   // Top left
        gc.fillArc((int) GameEngine.pocTopRight.x - pocketSize, (int) GameEngine.pocTopRight.y - pocketSize, 2 * pocketSize, 2 * pocketSize, -90,270, ArcType.ROUND); // Top Right
        gc.fillArc((int) GameEngine.pocBotRight.x - pocketSize, (int) GameEngine.pocBotRight.y - pocketSize, 2 * pocketSize, 2 * pocketSize, 180,270, ArcType.ROUND); // Bot Right
        gc.fillArc((int) GameEngine.pocBotLeft.x - pocketSize, (int) GameEngine.pocBotLeft.y - pocketSize, 2 * pocketSize, 2 * pocketSize, 90,270, ArcType.ROUND);  // Bot Left
        gc.fillArc((int) GameEngine.pocTopRight.x - width/2 - pocketSize, (int) GameEngine.pocTopLeft.y - pocketSize, 2 * pocketSize, 2 * pocketSize, 0,180, ArcType.ROUND); // Top middle
        gc.fillArc((int) GameEngine.pocTopRight.x - width/2 - pocketSize, (int) GameEngine.pocBotLeft.y - pocketSize, 2 * pocketSize, 2 * pocketSize, 180,180, ArcType.ROUND); // Bot middle
    }
    
}
