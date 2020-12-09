import java.util.HashSet;
import java.util.Set;

class Player extends Mover
{
    /* Direction is used in demoMode, currDirection and desiredDirection are used in non demoMode*/

    char currDirection;
    char desiredDirection;

    /* Keeps track of pellets eaten to determine end of game */
    int pelletsEaten;


    /* Which pellet the pacman is on top of */


    /* teleport is true when travelling through the teleport tunnels*/
    boolean teleport;

    /* Stopped is set when the pacman is not moving or has been killed */
    boolean stopped = false;

    /* Constructor places pacman in initial location and orientation */
    public Player(int x, int y)
    {
        super(x,y);
        teleport=false;
        pelletsEaten=0;
        setPelletX(x/ getGridSize() -1);
        setPelletY(y/ getGridSize() -1);
        currDirection='L';
        desiredDirection='L';

    }


    /* This function is used for demoMode.  It is copied from the Ghost class.  See that for comments */
    public char newDirection()
    {
        int random;
        char backwards='U';
        int newX=getX(),newY=getY();
        int lookX=getX(),lookY=getY();
        Set<Character> set = new HashSet<Character>();
        switch(getDirection())
        {
            case 'L':
                backwards='R';
                break;
            case 'R':
                backwards='L';
                break;
            case 'U':
                backwards='D';
                break;
            case 'D':
                backwards='U';
                break;
        }
        char newDirection = backwards;
        while (newDirection == backwards || !isValidDest(lookX,lookY))
        {
            if (set.size()==3)
            {
                newDirection=backwards;
                break;
            }
            newX=getX();
            newY=getY();
            lookX=getX();
            lookY=getY();
            random = (int)(Math.random()*4) + 1;
            if (random == 1)
            {
                newDirection = 'L';
                newX-= getIncrement();
                lookX-= getIncrement();
            }
            else if (random == 2)
            {
                newDirection = 'R';
                newX+= getIncrement();
                lookX+= getGridSize();
            }
            else if (random == 3)
            {
                newDirection = 'U';
                newY-= getIncrement();
                lookY-= getIncrement();
            }
            else if (random == 4)
            {
                newDirection = 'D';
                newY+= getIncrement();
                lookY+= getGridSize();
            }
            if (newDirection != backwards)
            {
                set.add(new Character(newDirection));
            }
        }
        return newDirection;
    }

    /* This function is used for demoMode.  It is copied from the Ghost class.  See that for comments */
    public boolean isChoiceDest()
    {
        if (  getX()% getGridSize() ==0&& getY()% getGridSize() ==0 )
        {
            return true;
        }
        return false;
    }

    /* This function is used for demoMode.  It is copied from the Ghost class.  See that for comments */
    public void demoMove()
    {
        setLastX(getX());
        setLastY(getY());
        if (isChoiceDest())
        {
            setDirection(newDirection());
        }
        switch(getDirection())
        {
            case 'L':
                if ( isValidDest(getX()- getIncrement(),getY()))
                {
                    setX(getX()- getIncrement());
                }
                else if (getY() == 9* getGridSize() && getX() < 2 * getGridSize())
                {
                    setX(getMax() - getGridSize() *1);
                    teleport = true;
                }
                break;
            case 'R':
                if ( isValidDest(getX()+ getGridSize(),getY()))
                {
                    setX(getX()+ getIncrement());
                }
                else if (getY() == 9* getGridSize() && getX() > getMax() - getGridSize() *2)
                {
                    setX(1* getGridSize());
                    teleport=true;
                }
                break;
            case 'U':
                if ( isValidDest(getX(),getY()- getIncrement()))
                    setY(getY()- getIncrement());
                break;
            case 'D':
                if ( isValidDest(getX(),getY()+ getGridSize()))
                    setY(getY()+ getIncrement());
                break;
        }
        currDirection = getDirection();
        setFrameCount(getFrameCount() + 1);
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
                (desiredDirection=='L' && currDirection=='R')  ||
                (desiredDirection=='R' && currDirection=='L')  ||
                (desiredDirection=='U' && currDirection=='D')  ||
                (desiredDirection=='D' && currDirection=='U')
        )
        {
            switch(desiredDirection)
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
            switch(currDirection)
            {
                case 'L':
                    if ( isValidDest(getX()- getIncrement(),getY()))
                        setX(getX()- getIncrement());
                    else if (getY() == 9*gridSize && getX() < 2 * gridSize)
                    {
                        setX(getMax() - gridSize);
                        teleport = true;
                    }
                    break;
                case 'R':
                    if ( isValidDest(getX()+gridSize,getY()))
                        setX(getX()+ getIncrement());
                    else if (getY() == 9*gridSize && getX() > getMax() - gridSize*2)
                    {
                        setX(gridSize);
                        teleport=true;
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
            currDirection=desiredDirection;
        }

        /* If we didn't move at all, set the stopped flag */
        if (getLastX() == getX() && getLastY()==getY())
            stopped=true;

            /* Otherwise, clear the stopped flag and increment the frameCount for animation purposes*/
        else
        {
            stopped=false;
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
}

