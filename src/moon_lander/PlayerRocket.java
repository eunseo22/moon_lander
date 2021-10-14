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

import sprite.Sprite;


public class PlayerRocket extends Sprite{
	
    private Random random;
    public int x;
    public int y;
    public boolean landed;
    public boolean crashed;
    private int speedAccelerating;
    private int speedStopping;
    public int topLandingSpeed;
    private int speedX;
    public int speedY;
    
    private BufferedImage rocketImg;
    private BufferedImage rocketLandedImg;
    private BufferedImage rocketCrashedImg;
    private BufferedImage rocketFireImg;
    public int rocketImgWidth;
    public int rocketImgHeight;
    
    // 스코어 관리 위한 변수
    public boolean isWin = false;     
    public boolean isSurvives = false;
    public int AsteroidKill = 0;
    public int alienKill = 0;
    public int score = 0;
    // 
    
    public PlayerRocket()
    {
        Initialize();
        LoadContent();
        
        x = random.nextInt(Framework.frameWidth - rocketImgWidth);
        // 범위 조정해야 함.
    }
    
    public int getX() {
		return x;
	}
	public int getY() {
		return y;
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
        	URL rocketImgUrl;
        	URL rocketLandedImgUrl;
        	URL rocketCrashedImgUrl;
        	if(Framework.playerCnt == 1) {   // 1인용일 때는 그냥 회색 로켓
        		rocketImgUrl = this.getClass().getResource("/resources/images/rocket.png");
                rocketLandedImgUrl = this.getClass().getResource("/resources/images/rocket_landed.png");
                rocketCrashedImgUrl = this.getClass().getResource("/resources/images/rocket_crashed.png");
        	} else {
        		if(Game.rocketNum == 1) {    // 2인용, 1번째 로켓이 오렌지색.
        			rocketImgUrl = this.getClass().getResource("/resources/images/rocket_orange.png");
                    rocketLandedImgUrl = this.getClass().getResource("/resources/images/rocket_landed_orange.png");
                    rocketCrashedImgUrl = this.getClass().getResource("/resources/images/rocket_crashed_orange.png");
        		} else {
        			rocketImgUrl = this.getClass().getResource("/resources/images/rocket_purple.png");
                    rocketLandedImgUrl = this.getClass().getResource("/resources/images/rocket_landed_purple.png");
                    rocketCrashedImgUrl = this.getClass().getResource("/resources/images/rocket_crashed_purple.png");
        		}
        	}
        	rocketImg = ImageIO.read(rocketImgUrl);
            rocketImgWidth = rocketImg.getWidth();
            rocketImgHeight = rocketImg.getHeight();
            
            rocketLandedImg = ImageIO.read(rocketLandedImgUrl);
            
            rocketCrashedImg = ImageIO.read(rocketCrashedImgUrl);
            
            URL rocketFireImgUrl = this.getClass().getResource("/resources/images/rocket_fire.png");
            rocketFireImg = ImageIO.read(rocketFireImgUrl);
        }
        catch (IOException ex) {
            Logger.getLogger(PlayerRocket.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void ResetPlayer()
    {
        landed = false;
        crashed = false;
        
        LoadContent();
        
        x = random.nextInt(Framework.frameWidth - rocketImgWidth);
        y = 5+ 60*6 ;
        
        speedX = 0;
        speedY = 0;
    }
    
    
    public void Update()
    {
    	if(this.crashed) {
    		
    	} else if(this.landed) {
    		
    	} else {
    		if(Game.rocketNum == 2) {  // 2번째 로켓일 때.
                if(Canvas.keyboardKeyState(KeyEvent.VK_UP))
                    speedY -= speedAccelerating;
                else
                    speedY += speedStopping;
                
                if(Canvas.keyboardKeyState(KeyEvent.VK_LEFT))
                    speedX -= speedAccelerating;
                else if(speedX < 0)
                    speedX += speedStopping;
                
                if(Canvas.keyboardKeyState(KeyEvent.VK_RIGHT))
                    speedX += speedAccelerating;
                else if(speedX > 0)
                    speedX -= speedStopping;
                
                x += speedX;
                y += speedY;
                
        	} else {               // 1번째 로켓일 때
                if(Canvas.keyboardKeyState(KeyEvent.VK_W))
                    speedY -= speedAccelerating;
                else
                    speedY += speedStopping;
                
                if(Canvas.keyboardKeyState(KeyEvent.VK_A))
                    speedX -= speedAccelerating;
                else if(speedX < 0)
                    speedX += speedStopping;
                
                if(Canvas.keyboardKeyState(KeyEvent.VK_D))
                    speedX += speedAccelerating;
                else if(speedX > 0)
                    speedX -= speedStopping;
                
                x += speedX;
                y += speedY;
        	}
    		
    		if(x <= 2) x=2;
    		if( x >= Params.BOARD_WIDTH - 2*rocketImgWidth) {
    			
    			x= Params.BOARD_WIDTH - 2*rocketImgWidth;
    		}
    		
    		if(y <= 2) y=2;
    		if( y >= Params.BOARD_HEIGHT - 2*rocketImgHeight) {
    			
    			y= Params.BOARD_HEIGHT - 2*rocketImgHeight;
    		}
    		
			/*  // 이전 코드
			 * if(x > Framework.frameWidth - rocketImgWidth) { x = Framework.frameWidth -
			 * rocketImgWidth; } else if (x < 0) { x = 0; } if(y > Framework.frameHeight -
			 * rocketImgHeight) { y = Framework.frameHeight - rocketImgHeight; } else if (y
			 * < 0) { y = 0; }
			 */
    	}
    }
    
    public void Draw(Graphics2D g2d)
    {
        g2d.setColor(Color.white);
        if(Game.rocketNum == 1) {
        	g2d.drawString("Rocket 1 coordinates: "+ x +" : "+ y, 5, 15);
        } else {
        	g2d.drawString("Rocket 2 coordinates: "+ x +" : "+ y, Framework.frameWidth-180, 15);
        }
        
        if(landed)
        {
        	g2d.drawImage(rocketLandedImg, x, y, null);
        }
        else if(crashed)
        {
            g2d.drawImage(rocketCrashedImg, x, y + rocketImgHeight - rocketCrashedImg.getHeight(), null);
        }
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

	public void KeyReleased(KeyEvent e) {
		int key = e.getKeyCode();

        if (key == KeyEvent.VK_A) {
            x -= 2;
        }

        if (key == KeyEvent.VK_D) {
        	x += 2;
        }
        
        if (key == KeyEvent.VK_W) {
        	y -= 2;
        }
	}

	public void keyPressed(KeyEvent e) {
		int key = e.getKeyCode();
		
		if(key == KeyEvent.VK_A) {
			
			x += 0;
		}
		
		if(key == KeyEvent.VK_D) {
			
			x += 0;
		}
		if (key == KeyEvent.VK_W) {
        	y += 0;
        }
	}


	
    
}