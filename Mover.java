import java.awt.*;
import java.util.HashMap;
import java.util.Map;

class Mover
{
    /* Framecount is used to count animation frames*/
    private int frameCount=0;

    private static final Map<Character,Character> backwards = Map.of('L','R', 'R', 'L','U','D','D','U');

    /* State contains the game map */
    private boolean[][] state;

    /* gridSize is the size of one square in the game.
       max is the height/width of the game.
       increment is the speed at which the object moves,
       1 increment per move() call */
    private int gridSize;
    private int max;
    private int increment;

    /* Direction ghost is heading */
    private char direction;

    private int pelletX;
    private int pelletY;
    /* Last ghost location*/
    private int lastX;
    private int lastY;

    /* Current ghost location */
    private int x;
    private int y;

    /* Generic constructor */
    public Mover(int x, int y)
    {
        setGridSize(20);
        setIncrement(4);
        setMax(400);
        setState(new boolean[20][20]);
        this.x = x;
        this.y = y;
        this.lastX = x;
        this.lastY = y;

        for(int i =0;i<20;i++)
        {
            for(int j=0;j<20;j++)
            {
                getState()[i][j] = false;
            }
        }
    }

    /* Updates the state information */
    public void updateState(boolean[][] state)
    {
        for(int i =0;i<20;i++)
        {
            for(int j=0;j<20;j++)
            {
                this.getState()[i][j] = state[i][j];
            }
        }
    }

    /* Determines if a set of coordinates is a valid destination.*/
    public boolean isValidDest(int x, int y)
    {
    /* The first statements check that the x and y are inbounds.  The last statement checks the map to
       see if it's a valid location */
        if ((((x)%20==0) || ((y)%20)==0) && 20<=x && x<400 && 20<= y && y<400 && getState()[x/20-1][y/20-1] )
        {
            return true;
        }
        return false;
    }

    public void overDraw(Graphics g){
        g.setColor(Color.BLACK);
        g.fillRect(getLastX(),getLastY(),20,20);
    }

    public char getBackwards(){
        return backwards.get(direction);
    }

    public int getLastX() {
        return lastX;
    }

    public void setLastX(int lastX) {
        this.lastX = lastX;
    }

    public int getLastY() {
        return lastY;
    }

    public void setLastY(int lastY) {
        this.lastY = lastY;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() { return y; }

    public void setY(int y) {
        this.y = y;
    }

    public char getDirection() {
        return direction;
    }

    public void setDirection(char direction) {
        this.direction = direction;
    }

    public int getFrameCount() {
        return frameCount;
    }

    public void setFrameCount(int frameCount) {
        this.frameCount = frameCount;
    }

    public boolean[][] getState() {
        return state;
    }

    public void setState(boolean[][] state) {
        this.state = state;
    }

    public int getGridSize() {
        return gridSize;
    }

    public void setGridSize(int gridSize) {
        this.gridSize = gridSize;
    }

    public int getMax() {
        return max;
    }

    public void setMax(int max) {
        this.max = max;
    }

    public int getIncrement() {
        return increment;
    }

    public void setIncrement(int increment) {
        this.increment = increment;
    }

    public int getPelletX() {return pelletX; }

    public void setPelletX(int pelletX) { this.pelletX = pelletX; }

    public int getPelletY() { return pelletY; }

    public void setPelletY(int pelletY) { this.pelletY = pelletY; }
}