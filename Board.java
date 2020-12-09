/* Drew Schuster */
import java.awt.*;
import javax.swing.JPanel;
import java.lang.Math;
import java.util.*;
import java.io.*;


/* Both Player and Ghost inherit Mover.  Has generic functions relevant to both*/


/* This is the pacman object */


/*This board class contains the player, ghosts, pellets, and most of the game logic.*/
public class Board extends JPanel
{


  private final String pathg1 = "img/ghost10.jpg";
  private final String pathg2 = "img/ghost20.jpg";
  private final String pathg3 = "img/ghost30.jpg";
  private final String pathg4 = "img/ghost40.jpg";

  private Image titleScreenImage = Toolkit.getDefaultToolkit().getImage("img/titleScreen.jpg");
  private Image gameOverImage = Toolkit.getDefaultToolkit().getImage("img/gameOver.jpg");
  private Image winScreenImage = Toolkit.getDefaultToolkit().getImage("img/winScreen.jpg");

  /* Initialize the player and ghosts */
  private Player player = new Player(200,300);


  private ArrayList<Ghost> ghosts = new ArrayList<>();
  /* Timer is used for playing sound effects and animations */
  private long timer = System.currentTimeMillis();

  /* Dying is used to count frames in the dying animation.  If it's non-zero,
     pacman is in the process of dying */
  private int dying=0;
 
  /* Score information */
  private int currScore;
  private int highScore;

  /* if the high scores have been cleared, we have to update the top of the screen to reflect that */
  private boolean clearHighScores= false;

  private int numLives=2;

  /*Contains the game map, passed to player and ghosts */
  private boolean[][] state;

  /* Contains the state of all pellets*/
  private boolean[][] pellets;

  /* Game dimensions */
  private int gridSize;
  private int max;

  /* State flags*/
  private boolean stopped;
  private boolean titleScreen;
  private boolean winScreen = false;
  private boolean overScreen = false;
  private boolean demo = false;
  private int New;

  /* Used to call sound effects */
  private GameSounds sounds;

  private int lastPelletEatenX = 0;
  private int lastPelletEatenY=0;

  /* This is the font used for the menus */
  private Font font = new Font("Monospaced",Font.BOLD, 12);

  /* Constructor initializes state flags etc.*/
  public Board() 
  {
    ghosts.add(new Ghost(180,180,pathg1));
    ghosts.add(new Ghost(180,180,pathg2));
    ghosts.add(new Ghost(180,180,pathg3));
    ghosts.add(new Ghost(180,180,pathg4));

    initHighScores();
    sounds = new GameSounds();
    currScore=0;
    stopped=false;
    max=400;
    gridSize=20;
    New=0;
    titleScreen = true;
  }

  /* Reads the high scores file and saves it */
  public void initHighScores()
  {
    File file = new File("highScores.txt");
    Scanner sc;
    try
    {
        sc = new Scanner(file);
        highScore = sc.nextInt();
        sc.close();
    }
    catch(Exception e)
    {
    }
  }

  /* Writes the new high score to a file and sets flag to update it on screen */
  public void updateScore(int score)
  {
    PrintWriter out;
    try
    {
      out = new PrintWriter("highScores.txt");
      out.println(score);
      out.close();
    }
    catch(Exception e)
    {
    }
    highScore=score;
    clearHighScores=true;
  }

  /* Wipes the high scores file and sets flag to update it on screen */
  public void clearHighScores()
  {
    PrintWriter out;
    try
    {
      out = new PrintWriter("highScores.txt");
      out.println("0");
      out.close();
    }
    catch(Exception e)
    {
    }
    highScore=0;
    clearHighScores=true;
  }

  /* Reset occurs on a new game*/
  public void reset()
  {
    numLives=2;
    state = new boolean[20][20];
    pellets = new boolean[20][20];

    /* Clear state and pellets arrays */
    for(int i=0;i<20;i++)
    {
      for(int j=0;j<20;j++)
      {
        state[i][j]=true;
        pellets[i][j]=true;
      }
    }

    /* Handle the weird spots with no pellets*/
    for(int i = 5;i<14;i++)
    {
      for(int j = 5;j<12;j++)
      {
        pellets[i][j]=false;
      }
    }
    pellets[9][7] = false;
    pellets[8][8] = false;
    pellets[9][8] = false;
    pellets[10][8] = false;

  }


  /* Function is called during drawing of the map.
     Whenever the a portion of the map is covered up with a barrier,
     the map and pellets arrays are updated accordingly to note
     that those are invalid locations to travel or put pellets
  */
  public void updateMap(int x,int y, int width, int height)
  {
    for (int i =x/gridSize; i<x/gridSize+width/gridSize;i++)
    {
      for (int j=y/gridSize;j<y/gridSize+height/gridSize;j++)
      {
        state[i-1][j-1]=false;
        pellets[i-1][j-1]=false;
      }
    }
  } 


  /* Draws the appropriate number of lives on the bottom left of the screen.
     Also draws the menu */
  public void drawLives(Graphics g)
  {
    g.setColor(Color.BLACK);

    /*Clear the bottom bar*/
    g.fillRect(0,max+5,600,gridSize);
    g.setColor(Color.YELLOW);
    for(int i = 0;i<numLives;i++)
    {
      /*Draw each life */
      g.fillOval(gridSize*(i+1),max+5,gridSize,gridSize);
    }
    /* Draw the menu items */
    g.setColor(Color.YELLOW);
    g.setFont(font);
    g.drawString("Reset",100,max+5+gridSize);
    g.drawString("Clear High Scores",180,max+5+gridSize);
    g.drawString("Exit",350,max+5+gridSize);
  }
  
  
  /*  This function draws the board.  The pacman board is really complicated and can only feasibly be done
      manually.  Whenever I draw a wall, I call updateMap to invalidate those coordinates.  This way the pacman
      and ghosts know that they can't traverse this area */ 
  public void drawBoard(Graphics g)
  {
        g.setColor(Color.BLACK);
        g.fillRect(0,0,600,600);
        g.setColor(Color.BLACK);
        g.fillRect(0,0,420,420);
        
        g.setColor(Color.BLACK);
        g.fillRect(0,0,20,600);
        g.fillRect(0,0,600,20);
        g.setColor(Color.WHITE);
        g.drawRect(19,19,382,382);
        g.setColor(Color.BLUE);

        g.fillRect(40,40,60,20);
          updateMap(40,40,60,20);
        g.fillRect(120,40,60,20);
          updateMap(120,40,60,20);
        g.fillRect(200,20,20,40);
          updateMap(200,20,20,40);
        g.fillRect(240,40,60,20);
          updateMap(240,40,60,20);
        g.fillRect(320,40,60,20);
          updateMap(320,40,60,20);
        g.fillRect(40,80,60,20);
          updateMap(40,80,60,20);
        g.fillRect(160,80,100,20);
          updateMap(160,80,100,20);
        g.fillRect(200,80,20,60);
          updateMap(200,80,20,60);
        g.fillRect(320,80,60,20);
          updateMap(320,80,60,20);

        g.fillRect(20,120,80,60);
          updateMap(20,120,80,60);
        g.fillRect(320,120,80,60);
          updateMap(320,120,80,60);
        g.fillRect(20,200,80,60);
          updateMap(20,200,80,60);
        g.fillRect(320,200,80,60);
          updateMap(320,200,80,60);

        g.fillRect(160,160,40,20);
          updateMap(160,160,40,20);
        g.fillRect(220,160,40,20);
          updateMap(220,160,40,20);
        g.fillRect(160,180,20,20);
          updateMap(160,180,20,20);
        g.fillRect(160,200,100,20);
          updateMap(160,200,100,20);
        g.fillRect(240,180,20,20);
        updateMap(240,180,20,20);
        g.setColor(Color.BLUE);


        g.fillRect(120,120,60,20);
          updateMap(120,120,60,20);
        g.fillRect(120,80,20,100);
          updateMap(120,80,20,100);
        g.fillRect(280,80,20,100);
          updateMap(280,80,20,100);
        g.fillRect(240,120,60,20);
          updateMap(240,120,60,20);

        g.fillRect(280,200,20,60);
          updateMap(280,200,20,60);
        g.fillRect(120,200,20,60);
          updateMap(120,200,20,60);
        g.fillRect(160,240,100,20);
          updateMap(160,240,100,20);
        g.fillRect(200,260,20,40);
          updateMap(200,260,20,40);

        g.fillRect(120,280,60,20);
          updateMap(120,280,60,20);
        g.fillRect(240,280,60,20);
          updateMap(240,280,60,20);

        g.fillRect(40,280,60,20);
          updateMap(40,280,60,20);
        g.fillRect(80,280,20,60);
          updateMap(80,280,20,60);
        g.fillRect(320,280,60,20);
          updateMap(320,280,60,20);
        g.fillRect(320,280,20,60);
          updateMap(320,280,20,60);

        g.fillRect(20,320,40,20);
          updateMap(20,320,40,20);
        g.fillRect(360,320,40,20);
          updateMap(360,320,40,20);
        g.fillRect(160,320,100,20);
          updateMap(160,320,100,20);
        g.fillRect(200,320,20,60);
          updateMap(200,320,20,60);

        g.fillRect(40,360,140,20);
          updateMap(40,360,140,20);
        g.fillRect(240,360,140,20);
          updateMap(240,360,140,20);
        g.fillRect(280,320,20,40);
          updateMap(280,320,20,60);
        g.fillRect(120,320,20,60);
          updateMap(120,320,20,60);
        drawLives(g);
  } 


  /* Draws the pellets on the screen */
  public void drawPellets(Graphics g)
  {
        g.setColor(Color.YELLOW);
        for (int i=1;i<20;i++)
        {
          for (int j=1;j<20;j++)
          {
            if ( pellets[i-1][j-1])
            g.fillOval(i*20+8,j*20+8,4,4);
          }
        }
  }

  /* Draws one individual pellet.  Used to redraw pellets that ghosts have run over */
  public void fillPellet(int x, int y, Graphics g)
  {
    g.setColor(Color.YELLOW);
    g.fillOval(x*20+28,y*20+28,4,4);
  }

  /* This is the main function that draws one entire frame of the game */
  public void paint(Graphics g)
  {
    /* If we're playing the dying animation, don't update the entire screen.
       Just kill the pacman*/ 
    if (dying > 0)
    {
      dyingAnimation(g);
      return;
    }

    /* If this is the title screen, draw the title screen and return */
    if (titleScreen)
    {
      drawTitleScreen(g);
      return;
    } 

    /* If this is the win screen, draw the win screen and return */
    else if (winScreen)
    {
      drawWinScreen(g);
      return;
    }

    /* If this is the game over screen, draw the game over screen and return */
    else if (overScreen)
    {
      drawOverScreen(g);
      return;
    }

    /* If need to update the high scores, redraw the top menu bar */
    if (clearHighScores)
    {
      g.setColor(Color.BLACK);
      g.fillRect(0,0,600,18);
      g.setColor(Color.YELLOW);
      g.setFont(font);
      clearHighScores= false;
      if (demo)
        g.drawString("DEMO MODE PRESS ANY KEY TO START A GAME\t High Score: "+highScore,20,10);
      else
        g.drawString("Score: "+(currScore)+"\t High Score: "+highScore,20,10);
    }
   
    /* oops is set to true when pacman has lost a life */ 
    boolean oops=false;
    
    /* Game initialization */
    if (New==1)
    {
      reset();
      player = new Player(200,300);
      ghosts.set(0,new Ghost(180,180,pathg1));
      ghosts.set(1,new Ghost(200,180,pathg2));
      ghosts.set(2,new Ghost(220,180,pathg3));
      ghosts.set(3,new Ghost(220,180,pathg4));

      currScore = 0;
      drawBoard(g);
      drawPellets(g);
      drawLives(g);
      /* Send the game map to player and all ghosts */
      player.updateState(state);
      /* Don't let the player go in the ghost box*/
      player.getState()[9][7]=false;
      for (Ghost gx: ghosts) {
        gx.updateState(state);
      }

   
      /* Draw the top menu bar*/
      g.setColor(Color.YELLOW);
      g.setFont(font);
      if (demo)
        g.drawString("DEMO MODE PRESS ANY KEY TO START A GAME\t High Score: "+highScore,20,10);
      else
        g.drawString("Score: "+(currScore)+"\t High Score: "+highScore,20,10);
      New++;
    }
    /* Second frame of new game */
    else if (New == 2)
    {
      New++;
    }
    /* Third frame of new game */
    else if (New == 3)
    {
      New++;
      /* Play the newGame sound effect */
      sounds.newGame();
      timer = System.currentTimeMillis();
      return;
    }
    /* Fourth frame of new game */
    else if (New == 4)
    {
      /* Stay in this state until the sound effect is over */
      long currTime = System.currentTimeMillis();
      if (currTime - timer >= 5000)
      {
        New=0;
      }
      else
        return;
    }
    
    /* Drawing optimization */
    g.copyArea(player.getX()-20,player.getY()-20,80,80,0,0);
    for (Ghost gx:ghosts) {
      g.copyArea(gx.getX()-20,gx.getY()-20,80,80,0,0);
    }

    for (Ghost gx:ghosts) {
      if(gx.intersects(player))oops=true;
    }


    /* Kill the pacman */
    if (oops && !stopped)
    {
      /* 4 frames of death*/
      dying=4;
      
      /* Play death sound effect */
      sounds.death();
      /* Stop any pacman eating sounds */
      sounds.nomNomStop();

      /*Decrement lives, update screen to reflect that.  And set appropriate flags and timers */
      numLives--;
      stopped=true;
      drawLives(g);
      timer = System.currentTimeMillis();
    }

    /* Delete the players and ghosts */
    g.setColor(Color.BLACK);
    g.fillRect(player.getLastX(),player.getLastY(),20,20);
    for (Ghost gx:ghosts) {
      g.fillRect(gx.getLastX(),gx.getLastY(),20,20);
    }

    /* Eat pellets */
    if ( pellets[player.getPelletX()][player.getPelletY()] && New!=2 && New !=3)
    {
      lastPelletEatenX = player.getPelletX();
      lastPelletEatenY = player.getPelletY();

      /* Play eating sound */
      sounds.nomNom();
      
      /* Increment pellets eaten value to track for end game */
      player.setPelletsEaten(player.getPelletsEaten() + 1);

      /* Delete the pellet*/
      pellets[player.getPelletX()][player.getPelletY()]=false;

      /* Increment the score */
      currScore += 50;

      /* Update the screen to reflect the new score */
      g.setColor(Color.BLACK);
      g.fillRect(0,0,600,20);
      g.setColor(Color.YELLOW);
      g.setFont(font);
      if (demo)
        g.drawString("DEMO MODE PRESS ANY KEY TO START A GAME\t High Score: "+highScore,20,10);
      else
        g.drawString("Score: "+(currScore)+"\t High Score: "+highScore,20,10);

      /* If this was the last pellet */
      if (player.getPelletsEaten() == 173)
      {
        /*Demo mode can't get a high score */
        if (!demo)
        {
          if (currScore > highScore)
          {
            updateScore(currScore);
          }
          winScreen = true;
        }
        else
        {
          titleScreen = true;
        }
        return;
      }
    }

    /* If we moved to a location without pellets, stop the sounds */
    else if ( (player.getPelletX() != lastPelletEatenX || player.getPelletY() != lastPelletEatenY ) || player.isStopped())
    {
      /* Stop any pacman eating sounds */
      sounds.nomNomStop();
    }


    /* Replace pellets that have been run over by ghosts */
    for (Ghost gx:ghosts) {
      if ( pellets[gx.getLastPelletX()][gx.getLastPelletY()])
        fillPellet(gx.getLastPelletX(), gx.getLastPelletY(),g);
    }


    /*Draw the ghosts */
    for (Ghost gx:ghosts) {
      gx.drawGhost(g);
    }

    /* Draw the pacman */
    player.drawPlayer(g);


    /* Draw the border around the game in case it was overwritten by ghost movement or something */
    g.setColor(Color.WHITE);
    g.drawRect(19,19,382,382);

  }

  private void drawOverScreen(Graphics g) {
    g.setColor(Color.BLACK);
    g.fillRect(0,0,600,600);
    g.drawImage(gameOverImage,0,0,Color.BLACK,null);
    New = 1;
    /* Stop any pacman eating sounds */
    sounds.nomNomStop();
    return;
  }

  private void drawWinScreen(Graphics g) {
    g.setColor(Color.BLACK);
    g.fillRect(0,0,600,600);
    g.drawImage(winScreenImage,0,0,Color.BLACK,null);
    New = 1;
    /* Stop any pacman eating sounds */
    sounds.nomNomStop();
    return;
  }

  private void drawTitleScreen(Graphics g) {
    g.setColor(Color.BLACK);
    g.fillRect(0,0,600,600);
    g.drawImage(titleScreenImage,0,0,Color.BLACK,null);

    /* Stop any pacman eating sounds */
    sounds.nomNomStop();
    New = 1;
    return;
  }

  private void dyingAnimation(Graphics g) {
    /* Stop any pacman eating sounds */
    sounds.nomNomStop();

    /* Draw the pacman */
    player.drawPlayer(g);
    g.setColor(Color.BLACK);

    /* Kill the pacman */
    if (dying == 4)
      g.fillRect(player.getX(),player.getY(),20,7);
    else if ( dying == 3)
      g.fillRect(player.getX(),player.getY(),20,14);
    else if ( dying == 2)
      g.fillRect(player.getX(),player.getY(),20,20);
    else if ( dying == 1)
    {
      g.fillRect(player.getX(),player.getY(),20,20);
    }

      /* Take .1 seconds on each frame of death, and then take 2 seconds
         for the final frame to allow for the sound effect to end */
    long currTime = System.currentTimeMillis();
    long temp;
    if (dying != 1)
      temp = 100;
    else
      temp = 2000;
    /* If it's time to draw a new death frame... */
    if (currTime - timer >= temp)
    {
      dying--;
      timer = currTime;
      /* If this was the last death frame...*/
      if (dying == 0)
      {
        if (numLives==-1)
        {
          /* Demo mode has infinite lives, just give it more lives*/
          if (demo)
            numLives=2;
          else
          {
          /* Game over for player.  If relevant, update high score.  Set gameOver flag*/
            if (currScore > highScore)
            {
              updateScore(currScore);
            }
            overScreen=true;
          }
        }
      }
    }
    return;
  }


  public Player getPlayer() {
    return player;
  }





  public int getDying() {
    return dying;
  }


  public boolean isStopped() {
    return stopped;
  }

  public void setStopped(boolean stopped) {
    this.stopped = stopped;
  }

  public boolean isTitleScreen() {
    return titleScreen;
  }

  public void setTitleScreen(boolean titleScreen) {
    this.titleScreen = titleScreen;
  }

  public boolean isWinScreen() {
    return winScreen;
  }

  public void setWinScreen(boolean winScreen) {
    this.winScreen = winScreen;
  }

  public boolean isOverScreen() {
    return overScreen;
  }

  public void setOverScreen(boolean overScreen) {
    this.overScreen = overScreen;
  }

  public boolean isDemo() {
    return demo;
  }

  public void setDemo(boolean demo) {
    this.demo = demo;
  }

  public int getNew() {
    return New;
  }

  public void setNew(int aNew) {
    New = aNew;
  }

  public GameSounds getSounds() {
    return sounds;
  }

  public void setSounds(GameSounds sounds) {
    this.sounds = sounds;
  }

  public ArrayList<Ghost> getGhosts() {
    return ghosts;
  }

  public void setGhosts(ArrayList<Ghost> ghosts) {
    this.ghosts = ghosts;
  }


  @Override
  public Font getFont() {
    return font;
  }

  @Override
  public void setFont(Font font) {
    this.font = font;
  }
}
