package moon_lander;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;

/**
 * The space rocket with which player will have to land.
 * 
 * @author www.gametutorial.net
 */

public class PlayerRocket {
    
    /**
     * We use this to generate a random number for starting x coordinate of the rocket.
     */
    private Random random;
 
    /**
     * X coordinate of the rocket.
     */
    public int x;
    /**
     * Y coordinate of the rocket.
     */
    public int y;
    
    /**
     * Is rocket landed?
     */
    public boolean landed;
    
    /**
     * Has rocket crashed?
     */
    public boolean crashed;
        
    /**
     * Accelerating speed of the rocket.
     */
    private int speedAccelerating;
    /**
     * Stopping/Falling speed of the rocket. Falling speed because, the gravity pulls the rocket down to the moon.
     */
    private int speedStopping;
    
    /**
     * Maximum speed that rocket can have without having a crash when landing.
     */
    public int topLandingSpeed;
    
    /**
     * How fast and to which direction rocket is moving on x coordinate?
     */
    private int speedX;
    /**
     * How fast and to which direction rocket is moving on y coordinate?
     */
    public int speedY;
            
    /**
     * Image of the rocket in air.
     */
    private BufferedImage rocketImg;
    /**
     * Image of the rocket when landed.
     */
    private BufferedImage rocketLandedImg;
    /**
     * Image of the rocket when crashed.
     */
    private BufferedImage rocketCrashedImg;
    /**
     * Image of the rocket fire.
     */
    private BufferedImage rocketFireImg;
    
    /**
     * Width of rocket.
     */
    public int rocketImgWidth;
    /**
     * Height of rocket.
     */
    public int rocketImgHeight;
    
    
    public PlayerRocket()
    {
        Initialize();
        LoadContent();
        
        // Now that we have rocketImgWidth we set starting x coordinate.
        x = random.nextInt(Framework.frameWidth - rocketImgWidth);
    }
    
    
    private void Initialize()
    {
        random = new Random();
        
        ResetPlayer();
        
        speedAccelerating = 2;
        speedStopping = 1;
        
        topLandingSpeed = 5;
    }
    
    private void LoadContent()
    {
        try
        {
            URL rocketImgUrl = this.getClass().getResource("/resources/images/rocket.png");
            rocketImg = ImageIO.read(rocketImgUrl);
            rocketImgWidth = rocketImg.getWidth();
            rocketImgHeight = rocketImg.getHeight();
            
            URL rocketLandedImgUrl = this.getClass().getResource("/resources/images/rocket_landed.png");
            rocketLandedImg = ImageIO.read(rocketLandedImgUrl);
            
            URL rocketCrashedImgUrl = this.getClass().getResource("/resources/images/rocket_crashed.png");
            rocketCrashedImg = ImageIO.read(rocketCrashedImgUrl);
            
            URL rocketFireImgUrl = this.getClass().getResource("/resources/images/rocket_fire.png");
            rocketFireImg = ImageIO.read(rocketFireImgUrl);
        }
        catch (IOException ex) {
            Logger.getLogger(PlayerRocket.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    /**
     * Here we set up the rocket when we starting a new game.
     */
    public void ResetPlayer()
    {
        landed = false;
        crashed = false;
        
        x = random.nextInt(Framework.frameWidth - rocketImgWidth);
        y = 10;
        
        speedX = 0;
        speedY = 0;
    }
    
    
    /**
     * Here we move the rocket.
     */
    public void Update()
    {
    	if(this.crashed) {
    		
    	} else if(this.landed) {
    		
    	} else {
    		if(Game.rocketNum == 2) {
        		// Calculating speed for moving up or down.
                if(Canvas.keyboardKeyState(KeyEvent.VK_UP))
                    speedY -= speedAccelerating;
                else
                    speedY += speedStopping;
                
                // Calculating speed for moving or stopping to the left.
                if(Canvas.keyboardKeyState(KeyEvent.VK_LEFT))
                    speedX -= speedAccelerating;
                else if(speedX < 0)
                    speedX += speedStopping;
                
                // Calculating speed for moving or stopping to the right.
                if(Canvas.keyboardKeyState(KeyEvent.VK_RIGHT))
                    speedX += speedAccelerating;
                else if(speedX > 0)
                    speedX -= speedStopping;
                
                // Moves the rocket.
                x += speedX;
                y += speedY;
                
                
             // Edit the rocket's coordinates.
                if(x < 0) x = 0;
                else if(x > Framework.frameWidth - rocketImgWidth) x =  Framework.frameWidth-rocketImgWidth;
                
                if(y<0) y=0;
                
                
        	} else {
        		// Calculating speed for moving up or down.
                if(Canvas.keyboardKeyState(KeyEvent.VK_W))
                    speedY -= speedAccelerating;
                else
                    speedY += speedStopping;
                
                // Calculating speed for moving or stopping to the left.
                if(Canvas.keyboardKeyState(KeyEvent.VK_A))
                    speedX -= speedAccelerating;
                else if(speedX < 0)
                    speedX += speedStopping;
                
                // Calculating speed for moving or stopping to the right.
                if(Canvas.keyboardKeyState(KeyEvent.VK_D))
                    speedX += speedAccelerating;
                else if(speedX > 0)
                    speedX -= speedStopping;
                
                // Moves the rocket.
                x += speedX;
                y += speedY;
                
                
                // Edit the rocket's coordinates.
                if(x < 0) x = 0;
                else if(x > Framework.frameWidth - rocketImgWidth) x =  Framework.frameWidth-rocketImgWidth;
                
                if(y<0) y=0;
        	}
    	}
    }
    
    public void Draw(Graphics2D g2d)
    {
        g2d.setColor(Color.white);
        if(Game.rocketNum == 1) {
        	g2d.drawString("Rocket 1 coordinates: "+ x +" : "+ y, 5, 15);
        	g2d.drawString(Framework.frameHeight +" " + Framework.frameWidth, 5, 25);  // 
        } else {
        	g2d.drawString("Rocket 2 coordinates: "+ x +" : "+ y, Framework.frameWidth-180, 15);
        }
        
        // If the rocket is landed.
        if(landed)
        {
        	switch (Game.rocketNum) {
			case 1:
				g2d.drawImage(rocketLandedImg, x, y, null);
				break;
			case 2:
				g2d.drawImage(rocketLandedImg, x, y, null);
				break;
			}
            g2d.drawImage(rocketLandedImg, x, y, null);
        }
        // If the rocket is crashed.
        else if(crashed)
        {
            g2d.drawImage(rocketCrashedImg, x - rocketImgWidth, y + rocketImgHeight - rocketCrashedImg.getHeight(), null);
        }
        // If the rocket is still in the space.
        else
        {
            // If player hold down a W key we draw rocket fire.
            if(Canvas.keyboardKeyState(KeyEvent.VK_W) && Game.rocketNum == 1)
                g2d.drawImage(rocketFireImg, x + 12, y + 66, null);
            if(Canvas.keyboardKeyState(KeyEvent.VK_UP) && Game.rocketNum == 2)
                g2d.drawImage(rocketFireImg, x + 12, y + 66, null);
            g2d.drawImage(rocketImg, x, y, null);
        }
    }
    
}
