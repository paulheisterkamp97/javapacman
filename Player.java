import java.awt.*;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

class Player extends Mover
{
    private final HashMap<Character,Image> images = new HashMap<>();

    private char currDirection;
    private char desiredDirection;

    /* Keeps track of pellets eaten to determine end of game */
    private int pelletsEaten;


    /* Which pellet the pacman is on top of */


    /* teleport is true when travelling through the teleport tunnels*/
    private boolean teleport;

    /* Stopped is set when the pacman is not moving or has been killed */
    private boolean stopped = false;

    /* Constructor places pacman in initial location and orientation */
    public Player(int x, int y)
    {
        super(x,y);
        setTeleport(false);
        setPelletsEaten(0);
        setPelletX(x/ getGridSize() -1);
        setPelletY(y/ getGridSize() -1);
        setCurrDirection('L');
        setDesiredDirection('L');

        images.put('R',Toolkit.getDefaultToolkit().getImage("img/pacmanright.jpg"));
        images.put('L',Toolkit.getDefaultToolkit().getImage("img/pacmanleft.jpg"));
        images.put('U',Toolkit.getDefaultToolkit().getImage("img/pacmanup.jpg"));
        images.put('D',Toolkit.getDefaultToolkit().getImage("img/pacmandown.jpg"));
        images.put('F',Toolkit.getDefaultToolkit().getImage("img/pacman.jpg"));
    }





    /* The move function moves the pacman for one frame in non demo mode */
    public void move()
    {
        int gridSize=20;
        setLastX(getX());
        setLastY(getY());

        /* Try to turn in the direction input by the user */
        /*Can only turn if we're in center of a grid*/
        if (getX() %20==0 && getY()%20==0 ||
                /* Or if we're reversing*/
                (getDesiredDirection() =='L' && getCurrDirection() =='R')  ||
                (getDesiredDirection() =='R' && getCurrDirection() =='L')  ||
                (getDesiredDirection() =='U' && getCurrDirection() =='D')  ||
                (getDesiredDirection() =='D' && getCurrDirection() =='U')
        )
        {
            switch(getDesiredDirection())
            {
                case 'L':
                    if ( isValidDest(getX()- getIncrement(),getY()))
                        setX(getX()- getIncrement());
                    break;
                case 'R':
                    if ( isValidDest(getX()+gridSize,getY()))
                        setX(getX()+ getIncrement());
                    break;
                case 'U':
                    if ( isValidDest(getX(),getY()- getIncrement()))
                        setY(getY()- getIncrement());
                    break;
                case 'D':
                    if ( isValidDest(getX(),getY()+gridSize))
                        setY(getY()+ getIncrement());
                    break;
            }
        }
        /* If we haven't moved, then move in the direction the pacman was headed anyway */
        if (getLastX()==getX() && getLastY()==getY())
        {
            switch(getCurrDirection())
            {
                case 'L':
                    if ( isValidDest(getX()- getIncrement(),getY()))
                        setX(getX()- getIncrement());
                    else if (getY() == 9*gridSize && getX() < 2 * gridSize)
                    {
                        setX(getMax() - gridSize);
                        setTeleport(true);
                    }
                    break;
                case 'R':
                    if ( isValidDest(getX()+gridSize,getY()))
                        setX(getX()+ getIncrement());
                    else if (getY() == 9*gridSize && getX() > getMax() - gridSize*2)
                    {
                        setX(gridSize);
                        setTeleport(true);
                    }
                    break;
                case 'U':
                    if ( isValidDest(getX(),getY()- getIncrement()))
                        setY(getY()- getIncrement());
                    break;
                case 'D':
                    if ( isValidDest(getX(),getY()+gridSize))
                        setY(getY()+ getIncrement());
                    break;
            }
        }

        /* If we did change direction, update currDirection to reflect that */
        else
        {
            setCurrDirection(getDesiredDirection());
        }

        /* If we didn't move at all, set the stopped flag */
        if (getLastX() == getX() && getLastY()==getY())
            setStopped(true);

            /* Otherwise, clear the stopped flag and increment the frameCount for animation purposes*/
        else
        {
            setStopped(false);
            setFrameCount(getFrameCount() + 1);
        }
    }

    /* Update what pellet the pacman is on top of */
    public void updatePellet()
    {
        if (getX()% getGridSize() ==0 && getY()% getGridSize() == 0)
        {
            setPelletX(getX()/ getGridSize() -1);
            setPelletY(getY()/ getGridSize() -1);
        }
    }
    public void drawPlayer(Graphics g,boolean full){
        Image x = images.get(currDirection);
        if(full)x = images.get('F');
        g.drawImage(x,getX(),getY(),Color.BLACK,null);
    }

    public char getCurrDirection() {
        return currDirection;
    }

    public void setCurrDirection(char currDirection) {
        this.currDirection = currDirection;
    }

    public char getDesiredDirection() {
        return desiredDirection;
    }

    public void setDesiredDirection(char desiredDirection) {
        this.desiredDirection = desiredDirection;
    }

    public int getPelletsEaten() {
        return pelletsEaten;
    }

    public void setPelletsEaten(int pelletsEaten) {
        this.pelletsEaten = pelletsEaten;
    }

    public boolean isTeleport() {
        return teleport;
    }

    public void setTeleport(boolean teleport) {
        this.teleport = teleport;
    }

    public boolean isStopped() {
        return stopped;
    }

    public void setStopped(boolean stopped) {
        this.stopped = stopped;
    }
}

