package moon_lander;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.swing.JButton;
import javax.swing.JFrame;

public class Framework extends Canvas {
    
	private static final long serialVersionUID = 1L;
	public static int frameWidth;
    public static int frameHeight;
    public static final long secInNanosec = 1000000000L;
    public static final long milisecInNanosec = 1000000L;
    private final int GAME_FPS = 16;
    private final long GAME_UPDATE_PERIOD = secInNanosec / GAME_FPS;
    
    public static enum GameState{STARTING, VISUALIZING, GAME_CONTENT_LOADING, MAIN_MENU, OPTIONS, PLAYING, GAMEOVER, DESTROYED}
    
    public static GameState gameState;
    
    private long gameTime;
    private long lastTime;
    
    private Game game;
    
    
    private BufferedImage moonLanderMenuImg;
    
    // used for mode selection.
    // JButton name : Level + playerCnt.
    private JButton Easy1, Normal1, Hard1, Easy2, Normal2, Hard2;
    
    // used for selecting number of rocket.
    // playerCnt 0: initializing, playerCnt 1: 1 person, playerCnt 2: 2 person.
    public static int playerCnt = 0;
    
    // used for setting level of game.
    // level 1: Easy, level 2: Normal, level 3: Hard.
    public static int level = 1;
    
    public Framework ()
    {
        super();
        
        gameState = GameState.VISUALIZING;
        
        Thread gameThread = new Thread() {
            @Override
            public void run(){
                GameLoop();
            }
        };
        gameThread.start();
    }
        
  
    private void Initialize()
    {
        
    }
    
    private void LoadContent()
    {
        try
        {
            URL moonLanderMenuImgUrl = this.getClass().getResource("/resources/images/menu.jpg");
            moonLanderMenuImg = ImageIO.read(moonLanderMenuImgUrl);
        }
        catch (IOException ex) {
            Logger.getLogger(Framework.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    
    private void GameLoop()
    {
        long visualizingTime = 0, lastVisualizingTime = System.nanoTime();
        
       long beginTime, timeTaken, timeLeft;
        
        while(true)
        {
            beginTime = System.nanoTime();
            
            switch (gameState)
            {
                case PLAYING:
                    gameTime += System.nanoTime() - lastTime;                    
                    game.UpdateGame(gameTime, mousePosition());                    
                    lastTime = System.nanoTime();
                    
                    
                break;
                case GAMEOVER:
                    //...
                break;
                case MAIN_MENU:
                    //...
                break;
                case OPTIONS:
                    //...
                break;
                case GAME_CONTENT_LOADING:
                    //...
                break;
                case STARTING:
                    Initialize();
                    LoadContent();

                    gameState = GameState.MAIN_MENU;
                break;
                case VISUALIZING:
                     if(this.getWidth() > 1 && visualizingTime > secInNanosec)
                    {
                        frameWidth = this.getWidth();
                        frameHeight = this.getHeight();

                        gameState = GameState.STARTING;
                    }
                    else
                    {
                        visualizingTime += System.nanoTime() - lastVisualizingTime;
                        lastVisualizingTime = System.nanoTime();
                    }
                break;
			default:
				break;
            }
            
            // Repaint the screen.
            repaint();
            
             timeTaken = System.nanoTime() - beginTime;
            timeLeft = (GAME_UPDATE_PERIOD - timeTaken) / milisecInNanosec; // In milliseconds
            if (timeLeft < 10) 
                timeLeft = 10; //set a minimum
            try {
                 //Provides the necessary delay and also yields control so that other thread can do work.
                 Thread.sleep(timeLeft);
            } catch (InterruptedException ex) { }
        }
    }
    
    
    public void DrawButtonForLevel() {
    	// locate level/mode button.
           Easy1 = new JButton("1인 easy");
           Easy1.setBounds(frameWidth / 2 - 110, frameHeight / 2 + 30, 95, 20);	 Easy1.addMouseListener(this);
           
           Normal1 = new JButton("1인 normal");
           Normal1.setBounds(frameWidth / 2 - 110, frameHeight / 2 + 55, 95, 20);	 Normal1.addMouseListener(this);
           
           Hard1 = new JButton("1인 hard");
           Hard1.setBounds(frameWidth / 2 - 110, frameHeight / 2 + 80, 95, 20);	 Hard1.addMouseListener(this);
           
           Easy2 = new JButton("2인 easy");
           Easy2.setBounds(frameWidth / 2 - 5, frameHeight / 2 + 30, 95, 20);		 Easy2.addMouseListener(this);
           
           Normal2 = new JButton("2인 normal");
           Normal2.setBounds(frameWidth / 2 - 5, frameHeight / 2 + 55, 95, 20);	 Normal2.addMouseListener(this);
           
           Hard2 = new JButton("2인 hard");
           Hard2.setBounds(frameWidth / 2 - 5, frameHeight / 2 + 80, 95, 20);		 Hard2.addMouseListener(this);
    }
    
    @Override
    public void Draw(Graphics2D g2d)
    {
    	DrawButtonForLevel();
    	
    	
        switch (gameState)
        {
            case PLAYING:
                game.Draw(g2d, mousePosition());
            break;
            case GAMEOVER:
                game.DrawGameOver(g2d, mousePosition(), gameTime);
                this.add(Easy1);
                this.add(Normal1);
                this.add(Hard1);
                this.add(Easy2);
                this.add(Normal2);
                this.add(Hard2);
            break;
            case MAIN_MENU:
                g2d.drawImage(moonLanderMenuImg, 0, 0, frameWidth, frameHeight, null);
                g2d.setColor(Color.white);
                g2d.drawString("Click the button to start the game you want.", frameWidth / 2 - 117, frameHeight / 2 + 20);
                this.add(Easy1);
                this.add(Normal1);
                this.add(Hard1);
                this.add(Easy2);
                this.add(Normal2);
                this.add(Hard2);
                g2d.drawString("WWW.GAMETUTORIAL.NET", 7, frameHeight - 5);
            break;
            case OPTIONS:
                //...
            break;
            case GAME_CONTENT_LOADING:
                g2d.setColor(Color.white);
                g2d.drawString("GAME is LOADING", frameWidth / 2 - 50, frameHeight / 2);
            break;
		default:
			break;
        }
    }
    
    private void newGame()
    {
        gameTime = 0;
        lastTime = System.nanoTime();
        
        game = new Game();
    }
    
   
    private void restartGame()
    {
        gameTime = 0;
        lastTime = System.nanoTime();
        
        game.RestartGame();
        
        gameState = GameState.PLAYING;
    }
    
    /**
     * Returns the position of the mouse pointer in game frame/window.
     * If mouse position is null than this method return 0,0 coordinate.
     * 
     * @return Point of mouse coordinates.
     */
    private Point mousePosition()
    {
        try
        {
            Point mp = this.getMousePosition();
            
            if(mp != null)
                return this.getMousePosition();
            else
                return new Point(0, 0);
        }
        catch (Exception e)
        {
            return new Point(0, 0);
        }
    }
    
    /**
     * This method is called when keyboard key is released.
     * 
     * @param e KeyEvent
     */
    @Override
    public void keyReleasedFramework(KeyEvent e)
    {
        
    }
    
    /**
     * This method is called when mouse button is clicked.
     * 
     * @param e MouseEvent
     */
    @Override
    public void mouseClicked(MouseEvent e)
    {
    	JButton temp = (JButton)e.getSource();
            
    	if(temp.getText() == "1인 easy") {
        	playerCnt = 1;
        	level = 1;
        } else if(temp.getText() == "1인 normal") {
        	playerCnt = 1;
        	level = 2;
        } else if(temp.getText() == "1인 hard") {
    		playerCnt = 1;
        	level = 3;
    	} else if(temp.getText() == "2인 easy") {
    		playerCnt = 2;
    		level = 1;
    	} else if(temp.getText() == "2인 normal") {
    		playerCnt = 2;
    		level = 2;
    	} else if(temp.getText() == "2인 hard") {
    		playerCnt = 2;
    		level = 3;
    	}
    	switch(gameState) {
	    	case MAIN_MENU:
	    		newGame();
	    	break;
	    	case GAMEOVER:
	    		restartGame();
	    	break;
		default:
			break;
	    }
        this.removeAll();
    }
}
