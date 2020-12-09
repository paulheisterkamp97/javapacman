import java.util.HashSet;
import java.util.Set;

class Ghost extends Mover
{


    /* The pellet the ghost is on top of */


    /* The pellet the ghost was last on top of */
    int lastPelletX,lastPelletY;

    /*Constructor places ghost and updates states*/
    public Ghost(int x, int y)
    {
        super(x,y);
        setDirection('L');
        setPelletX(x/ getGridSize() -1);
        setPelletY(x/ getGridSize() -1);
        lastPelletX= getPelletX();
        lastPelletY= getPelletY();
    }

    /* update pellet status */
    public void updatePellet()
    {
        int tempX,tempY;
        tempX = getX()/ getGridSize() -1;
        tempY = getY()/ getGridSize() -1;
        if (tempX != getPelletX() || tempY != getPelletY())
        {
            lastPelletX = getPelletX();
            lastPelletY = getPelletY();
            setPelletX(tempX);
            setPelletY(tempY);
        }

    }

    /* Determines if the location is one where the ghost has to make a decision*/
    public boolean isChoiceDest()
    {
        if (  getX()% getGridSize() ==0&& getY()% getGridSize() ==0 )
        {
            return true;
        }
        return false;
    }

    /* Chooses a new direction randomly for the ghost to move */
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
        /* While we still haven't found a valid direction */
        while (newDirection == backwards || !isValidDest(lookX,lookY))
        {
            /* If we've tried every location, turn around and break the loop */
            if (set.size()==3)
            {
                newDirection=backwards;
                break;
            }

            newX=getX();
            newY=getY();
            lookX=getX();
            lookY=getY();

            /* Randomly choose a direction */
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

    /* Random move function for ghost */
    public void move()
    {
        setLastX(getX());
        setLastY(getY());

        /* If we can make a decision, pick a new direction randomly */
        if (isChoiceDest())
        {
            setDirection(newDirection());
        }

        /* If that direction is valid, move that way */
        switch(getDirection())
        {
            case 'L':
                if ( isValidDest(getX()- getIncrement(),getY()))
                    setX(getX()- getIncrement());
                break;
            case 'R':
                if ( isValidDest(getX()+ getGridSize(),getY()))
                    setX(getX()+ getIncrement());
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
    }
}