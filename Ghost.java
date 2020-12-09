import java.awt.*;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

class Ghost extends Mover
{


    /* The pellet the ghost is on top of */

    private Image image1;
    private Image image2;
    /* The pellet the ghost was last on top of */
    private int lastPelletX;
    private int lastPelletY;

    /*Constructor places ghost and updates states*/
    public Ghost(int x, int y,String file)
    {
        super(x,y);
        setDirection('L');
        setPelletX(x/ getGridSize() -1);
        setPelletY(x/ getGridSize() -1);
        setLastPelletX(getPelletX());
        setLastPelletY(getPelletY());
        image1 = Toolkit.getDefaultToolkit().getImage(file);
        image2 = Toolkit.getDefaultToolkit().getImage(file.replace('0','1'));
    }

    /* update pellet status */
    public void updatePellet()
    {
        int tempX,tempY;
        tempX = getX()/ getGridSize() -1;
        tempY = getY()/ getGridSize() -1;
        if (tempX != getPelletX() || tempY != getPelletY())
        {
            setLastPelletX(getPelletX());
            setLastPelletY(getPelletY());
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
        char backwards=getBackwards();
        int newX=getX(),newY=getY();
        int lookX=getX(),lookY=getY();
        Set<Character> set = new HashSet<Character>();

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

            lookX=getX();
            lookY=getY();

            /* Randomly choose a direction */
            random = (int)(Math.random()*4) + 1;
            if (random == 1)
            {
                newDirection = 'L';
                lookX-= getIncrement();
            }
            else if (random == 2)
            {
                newDirection = 'R';
                lookX+= getGridSize();
            }
            else if (random == 3)
            {
                newDirection = 'U';
                lookY-= getIncrement();
            }
            else if (random == 4)
            {
                newDirection = 'D';
                lookY+= getGridSize();
            }
            if (newDirection != backwards)
            {
                set.add(new Character(newDirection));
            }
        }
        return newDirection;
    }

    public void drawGhost(Graphics g){
        if (getFrameCount()< 5){
            g.drawImage(image1,getX(),getY(),Color.BLACK,null);
        }else{
            g.drawImage(image2,getX(),getY(),Color.BLACK,null);
        }
        if (getFrameCount() >=10){
            setFrameCount(0);
        }else {
            setFrameCount(getFrameCount() + 1);
        }
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
    public boolean intersects(Player p){
        if(Math.abs(getX() - p.getX()) < 10&&Math.abs(getY() - p.getY()) < 10 ) return true;
        return false;
    }

    public int getLastPelletX() {
        return lastPelletX;
    }

    public void setLastPelletX(int lastPelletX) {
        this.lastPelletX = lastPelletX;
    }

    public int getLastPelletY() {
        return lastPelletY;
    }

    public void setLastPelletY(int lastPelletY) {
        this.lastPelletY = lastPelletY;
    }
}