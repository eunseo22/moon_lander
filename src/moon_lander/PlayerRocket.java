package moon_lander;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.awt.image.ImageObserver;
import java.io.IOException;
import java.net.URL;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;

import sprite.Shot;
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
    
    // ���ھ� ���� ���� ����
    public boolean isWin = false;     
    public boolean isSurvives = false;
    public int alienKill = 0;
    public int score = 0;
    // 
    
    
    
    public PlayerRocket()
    {
        Initialize();
        LoadContent();
        
        x = random.nextInt(Framework.frameWidth - rocketImgWidth);
       
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
        	if(Framework.playerCnt == 1) {   // 1�ο��� ���� �׳� ȸ�� ����
        		rocketImgUrl = this.getClass().getResource("/resources/images/rocket.png");
                rocketLandedImgUrl = this.getClass().getResource("/resources/images/rocket_landed.png");
                rocketCrashedImgUrl = this.getClass().getResource("/resources/images/rocket_crashed.png");
        	} else {
        		if(Game.rocketNum == 1) {    // 2�ο�, 1��° ������ ��������.
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
    		if(Game.rocketNum == 2) {  // 2��° ������ ��.
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
                
                
                // ���� ����!
                if(Canvas.keyboardKeyState(KeyEvent.VK_OPEN_BRACKET)) {
                	Shot shot = Game.shots.get(1);
                	if(!shot.isVisible()) {
                		shot = new Shot(x,y);
                		Game.shots.set(1, shot);
                	}
                }
                
                
        	} else {               // 1��° ������ ��
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
                
                // ���� ���� 
                if(Canvas.keyboardKeyState(KeyEvent.VK_SPACE)) {
                	Shot shot = Game.shots.get(0);
                	if(!shot.isVisible()) {
                		shot = new Shot(x,y);
                		Game.shots.set(0, shot);
                	}
                }
        	}
    		
    		if(x <= 2) x=2;
    		if( x >= Params.BOARD_WIDTH - 2*rocketImgWidth) {
    			
    			x= Params.BOARD_WIDTH - 2*rocketImgWidth;
    		}
    		
    		if(y <= 2) y=2;
    		if( y >= Params.BOARD_HEIGHT - 2*rocketImgHeight) {
    			
    			y= Params.BOARD_HEIGHT - 2*rocketImgHeight;
    		}
    		
			/*  // ���� �ڵ�
			 * if(x > Framework.frameWidth - rocketImgWidth) { x = Framework.frameWidth -
			 * rocketImgWidth; } else if (x < 0) { x = 0; } if(y > Framework.frameHeight -
			 * rocketImgHeight) { y = Framework.frameHeight - rocketImgHeight; } else if (y
			 * < 0) { y = 0; }
			 */
    	}
    }
    
    private void drawShot(Graphics2D g2d) {
    	if(Game.shots == null) return;
    	for(Shot shot : Game.shots) {
    		if (shot.isVisible()) {
               // g2d.drawImage(shot.getImage(), shot.getX(), shot.getY(), (ImageObserver) this);
                g2d.drawImage(shot.getImage(), shot.getX(), shot.getY(), null);
            }
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
        
        // ������ ���� �˻�
        if(isDying()) {
        	
        	g2d.drawImage(this.getImage(), x, y, null);
        }
        if(landed)
        {
        	g2d.drawImage(rocketLandedImg, x, y, null);
        }
        else if(crashed)
        {
            g2d.drawImage(rocketCrashedImg, x, y + rocketImgHeight - rocketCrashedImg.getHeight(), null);
        }
        else // ���⼭ ���ݵ� ��ο� ������.
        {
        	drawShot(g2d);
        	
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

	public void keyPressed(KeyEvent e) {
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


	
    
}