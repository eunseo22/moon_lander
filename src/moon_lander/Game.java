package moon_lander;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JPanel;

import sprite.Alien;
import sprite.Shot;

public class Game extends JPanel {

    
    private PlayerRocket playerRocket1, playerRocket2;
    private LandingArea landingArea;
    private BufferedImage backgroundImg;
    private BufferedImage redBorderImg;
    
    public static int rocketNum = 1;

    // �� ������ ���� ����
    private List<Alien> aliens;
    public static List<Shot> shots;
	private Shot shot1, shot2; 
	
	private int direction = -1;
    private int whoWin;   // ������ �̱� ����� ���;� ��. ��Ƴ��Ѵ�? -> ������ ���� �� ����?
    					// �� ���� ��� ���̴��� ����غ��� ��.
    
    private String explImg = "./resources/images/sprite/explosion2.png";
    
    
    
    public Game()
    {
        Framework.gameState = Framework.GameState.GAME_CONTENT_LOADING;
        
        Thread threadForInitGame = new Thread() {
            @Override
            public void run(){
                Initialize();
                InitializeE(Framework.level, Framework.playerCnt);
                LoadContent();
                
                Framework.gameState = Framework.GameState.PLAYING;
            }
        };
        threadForInitGame.start();
    }
    
    
    private void Initialize()
    {
    	shots = new ArrayList<>();
    	shot1 = new Shot();		shot2 = new Shot();
    	shots.add(shot1);		shots.add(shot2);
    	
    	aliens = new ArrayList<>();    	
    	
    	rocketNum = 1;			
    	playerRocket1 = new PlayerRocket();
    	
    	if(Framework.playerCnt == 2) {
    		rocketNum = 2;			
    		playerRocket2 = new PlayerRocket();
    	}
        landingArea  = new LandingArea();
    }
    
    public void InitializeE(int level, int playerNum) {
    	int ii = 0, jj = 0;        // ������ ���� ��Ÿ���� ���� ������ �޸���. // ii*jj ����
    	if(level == 1) {
    		ii = 4; 	jj = 5;
    	} else if(level == 2) {
    		ii = 3; 	jj = 7;
    	} else if(level == 3) {
    		ii = 4; 	jj = 7;
    	}
    	
    	if(playerNum == 2) {
    		jj*=2;
    	}
		 for(int i=0; i<ii; i++) {
	        	for(int j=0; j<jj; j++) {
	        		int getRandomValue = ThreadLocalRandom.current().nextInt(150, 600) + 150;
	        		Alien alien = new Alien(getRandomValue + 60*j,  5 + 60*i);
	        		aliens.add(alien);
	        	}
	       } 
	}
    
    
    private void LoadContent()
    {
        try
        {
            URL backgroundImgUrl = this.getClass().getResource("/resources/images/background.jpg");
            backgroundImg = ImageIO.read(backgroundImgUrl);
            
            URL redBorderImgUrl = this.getClass().getResource("/resources/images/red_border.png");
            redBorderImg = ImageIO.read(redBorderImgUrl);
            
            File musicPath = new File("./resources/sounds/Cool_Space_Music.wav");
			if(musicPath.exists()) {
				AudioInputStream audioInput = AudioSystem.getAudioInputStream(musicPath);
				Clip clip = AudioSystem.getClip();
				clip.open(audioInput);
				clip.start();
				clip.loop(Clip.LOOP_CONTINUOUSLY);
			} else {
				System.out.println("Cannot find the Audio File");
			}
        }
        catch (IOException ex) {
            Logger.getLogger(Game.class.getName()).log(Level.SEVERE, null, ex);
        }
        catch (Exception ex) {
        	ex.printStackTrace();
        }
    }
    
    
    public void RestartGame()
    {
    	rocketNum = 1;			
    	playerRocket1 = new PlayerRocket();
    	
    	if(Framework.playerCnt == 2) {
    		rocketNum = 2;
    		playerRocket1.ResetPlayer();
    		
    		if(playerRocket2 == null) {    			
    			playerRocket2 = new PlayerRocket();    			
    		} else {    			
    			playerRocket2.ResetPlayer();
    		}  
    	}
    }
    
    
    public void UpdateGame(long gameTime, Point mousePosition)
    {
    	switch(Framework.playerCnt) {
    		case 1:  // 1�ο�
    	        playerRocket1.Update();
    	        
    	        InteractionOfEP(Framework.level);
    	        
    	        if(playerRocket1.y + playerRocket1.rocketImgHeight - 10 > landingArea.y)
    	        {
    	            if((playerRocket1.x > landingArea.x) && (playerRocket1.x < landingArea.x + landingArea.landingAreaImgWidth - playerRocket1.rocketImgWidth))
    	            {
    	                if(playerRocket1.speedY <= playerRocket1.topLandingSpeed)
    	                    playerRocket1.landed = true;
    	                else
    	                    playerRocket1.crashed = true;
    	            }
    	            else
    	                playerRocket1.crashed = true;
    	                
    	            Framework.gameState = Framework.GameState.GAMEOVER;
    	        }
    	    break;
    		case 2:  // 2�ο�
    			rocketNum = 1;
				playerRocket1.Update();
				rocketNum = 2;
		        playerRocket2.Update();
		        
		        if(playerRocket1.y + playerRocket1.rocketImgHeight - 10 > landingArea.y)
		        {
		            if((playerRocket1.x > landingArea.x) && (playerRocket1.x < landingArea.x + landingArea.landingAreaImgWidth - playerRocket1.rocketImgWidth))
		            {
		                if(playerRocket1.speedY <= playerRocket1.topLandingSpeed)
		                    playerRocket1.landed = true;
		                else
		                    playerRocket1.crashed = true;
		            }
		            else
		                playerRocket1.crashed = true;
		        }
		        
		        // Checks where the player rocket 2 is.
		        if(playerRocket2.y + playerRocket2.rocketImgHeight - 10 > landingArea.y)
		        {
		            if((playerRocket2.x > landingArea.x) && (playerRocket2.x < landingArea.x + landingArea.landingAreaImgWidth - playerRocket2.rocketImgWidth))
		            {
		                if(playerRocket2.speedY <= playerRocket2.topLandingSpeed)
		                    playerRocket2.landed = true;
		                else
		                    playerRocket2.crashed = true;
		            }
		            else
		                playerRocket2.crashed = true;
		        }
		        
		        if(playerRocket1.crashed && playerRocket2.crashed) {		        	
		        	Framework.gameState = Framework.GameState.GAMEOVER;
		        	
		        } else if(playerRocket1.landed && playerRocket2.crashed) {	
		        	playerRocket1.isSurvives=true;  
		        	Framework.gameState = Framework.GameState.GAMEOVER;
		        	
		        } else if(playerRocket2.landed && playerRocket1.crashed) {
		        	playerRocket2.isSurvives=true;
		        	Framework.gameState = Framework.GameState.GAMEOVER;
		        	
		        } else if(playerRocket1.landed && playerRocket2.landed) {
		        	playerRocket1.isSurvives=true; 
		        	playerRocket2.isSurvives=true;
		        	Framework.gameState = Framework.GameState.GAMEOVER;
		        }
		        // ���� ���..
		        InteractionOfEP(Framework.level);
			       // ��� ����Ʈ: 1p 2p ������..���� �Լ� �ϳ����� �ұ�, �ƴϸ� �Լ� �ϳ��� �� �����?
    	}
    }
    

    
    private void InteractionOfEP(int level) {
    	if(Framework.playerCnt==1) { // 1�ο�
    		for(int i=0; i<1; i++) {
        		if (shots.get(i) != null && shots.get(i).isVisible()) {    		// -> �÷��̾ alien�� ���� �׿��� ���� ��Ȳ
            		int shotX = shots.get(i).getX();
            		int shotY = shots.get(i).getY();    		
            		for (Alien alien : aliens) {    			
            			int alienX = alien.getX();
            			int alienY = alien.getY();    			
            			if(alien.isVisible() && shots.get(i).isVisible()) {
            				if (shotX >= (alienX) && shotX <= (alienX + Params.ALIEN_WIDTH)
            					&& shotY >= (alienY) && shotY <= (alienY + Params.ALIEN_HEIGHT)) {    					
            					ImageIcon img = new ImageIcon(explImg);
            					alien.setImage(img.getImage());
            					alien.setDying(true);
            					playerRocket1.alienKill++;
            					shots.get(i).die();
            		}}}
            		
            		int y = shots.get(i).getY();	y-=4;
            		if (y<0) { shots.get(i).die(); } 
            		else { shots.get(i).setY(y); }
            		
            	} // end if(shot1.isVisible()) 
        	} // ��� ���� ��� ��. 1�ο��̶�. shots.get(i)�� �����ϱ� ���ؼ���.
			
			for (Alien alien : aliens) {    // -> alien�� x�� check
				if(alien == null) continue;
	    		int x = alien.getX();    		
	    		if (x >= Params.BOARD_WIDTH - Params.BORDER_RIGHT && direction != -1) {    			
	                direction = -1;
	                Iterator<Alien> i1 = aliens.iterator();
	                while (i1.hasNext()) {
	                    Alien a2 = i1.next();
	                    a2.setY(a2.getY() + Params.GO_DOWN);
	                }
	            }
	    		
	    		if (x <= Params.BORDER_LEFT && direction != 1) {    			
	    			direction = 1;    			
	    			Iterator<Alien> i2 = aliens.iterator();    			
	    			while (i2.hasNext()) {    				
	    				Alien a = i2.next();
	    				a.setY(a.getY() + Params.GO_DOWN);
	    			}
	    		}
	    	} // end for (Alien alien : aliens)
	    	
	    	Iterator<Alien> it = aliens.iterator();   // -> alien�� y�� check    	
	    	while (it.hasNext()) {    		
	    		Alien alien = it.next();    		
	    		if (alien.isVisible()) {    			
	    			int y = alien.getY();    			
	    			if (y > Params.GROUND - Params.ALIEN_HEIGHT) {    	
	    				Framework.gameState = Framework.GameState.GAMEOVER;
	    			}    			
	    			alien.act(direction);
	    		}
	    	} // end while (it.hasNext())
	    	
	    	Random generator = new Random();    	
	    	for(Alien alien : aliens) {    		 // -> bomb ��ġ ������    	
	    		int shot = generator.nextInt(15);
	    		Alien.Bomb bomb = alien.getBomb();    		
	    		if (shot == Params.CHANCE && alien.isVisible() && bomb.isDestroyed()) {    // alien ����ְ� bomb�� ����ִٸ�..?			
	    			bomb.setDestroyed(false); 
	    			bomb.setX(alien.getX());   // bomb ��ġ ������
	    			bomb.setY(alien.getY());    			
	    		}
	    		
	    		int bombX = bomb.getX();
	    		int bombY = bomb.getY();
	    		int playerX = playerRocket1.getX();
	    		int playerY = playerRocket1.getY();
	    		
	    		if (playerRocket1.isVisible() && !bomb.isDestroyed()) {    // �÷��̾ �㿡 �¾� �׾��� ���..			
	    			if(bombX >=(playerX) && bombX <= (playerX + playerRocket1.rocketImgWidth)
	    				&& bombY >= (playerY) && bombY <= (playerY + playerRocket1.rocketImgHeight)) {
	    				ImageIcon img = new ImageIcon(explImg);
	    				playerRocket1.setImage(img.getImage());
	    				playerRocket1.setDying(true);
	    				bomb.setDestroyed(true);    				
	    		} }
	    		
	    		if (!bomb.isDestroyed()) {    			
	    			bomb.setY(bomb.getY() + 1);    			
	    			if (bomb.getY() >= Params.GROUND - Params.BOMB_HEIGHT) {    				
	    				bomb.setDestroyed(true);
	    		} }
	    	} // end for(Alien alien : aliens) 	
    		
    		
    	} else if(Framework.playerCnt==2) {  // 2�ο�...
    		for(int i=0; i<2; i++) {
        		if (shots.get(i) != null && shots.get(i).isVisible()) {    		// -> �÷��̾ alien�� ���� �׿��� ���� ��Ȳ
            		int shotX = shots.get(i).getX();
            		int shotY = shots.get(i).getY();    		
            		for (Alien alien : aliens) {    			
            			int alienX = alien.getX();
            			int alienY = alien.getY();    			
            			if(alien.isVisible() && shots.get(i).isVisible()) {
            				if (shotX >= (alienX) && shotX <= (alienX + Params.ALIEN_WIDTH)
            					&& shotY >= (alienY) && shotY <= (alienY + Params.ALIEN_HEIGHT)) {    					
            					ImageIcon img = new ImageIcon(explImg);
            					alien.setImage(img.getImage());
            					alien.setDying(true);
            					playerRocket1.alienKill++;
            					shots.get(i).die();
            		}}}
            		
            		int y = shots.get(i).getY();	y-=4;
            		if (y<0) { shots.get(i).die(); } 
            		else { shots.get(i).setY(y); }
            		
            	} // end if(shot1.isVisible()) 
        	}
    		
    		for (Alien alien : aliens) {    // -> alien�� x�� check
    			if(alien == null) continue;
	    		int x = alien.getX();    		
	    		if (x >= Params.BOARD_WIDTH - Params.BORDER_RIGHT && direction != -1) {    			
	                direction = -1;
	                Iterator<Alien> i1 = aliens.iterator();
	                while (i1.hasNext()) {
	                    Alien a2 = i1.next();
	                    a2.setY(a2.getY() + Params.GO_DOWN);
	                }
	            }
	    		
	    		if (x <= Params.BORDER_LEFT && direction != 1) {    			
	    			direction = 1;    			
	    			Iterator<Alien> i2 = aliens.iterator();    			
	    			while (i2.hasNext()) {    				
	    				Alien a = i2.next();
	    				a.setY(a.getY() + Params.GO_DOWN);
	    			}
	    		}
	    	} // end for (Alien alien : aliens)
	    	
	    	Iterator<Alien> it = aliens.iterator();   // -> alien�� y�� check    	
	    	while (it.hasNext()) {    		
	    		Alien alien = it.next();    		
	    		if (alien.isVisible()) {    			
	    			int y = alien.getY();    			
	    			if (y > Params.GROUND - Params.ALIEN_HEIGHT) {    	
	    				Framework.gameState = Framework.GameState.GAMEOVER;
	    			}    			
	    			alien.act(direction);
	    		}
	    	} // end while (it.hasNext())	
	    		
		    	
	    	
	    	
	    	Random generator = new Random();    	
	    	for(Alien alien : aliens) {    		 // -> bomb ��ġ ������    	
	    		int shot = generator.nextInt(15);
	    		Alien.Bomb bomb = alien.getBomb();    		
	    		if (shot == Params.CHANCE && alien.isVisible() && bomb.isDestroyed()) {    // alien ����ְ� bomb�� ����ִٸ�..?			
	    			bomb.setDestroyed(false); 
	    			bomb.setX(alien.getX());   // bomb ��ġ ������
	    			bomb.setY(alien.getY());    			
	    		}
	    		
	    		int bombX = bomb.getX();
	    		int bombY = bomb.getY();
	    		int playerX = playerRocket1.getX();
	    		int playerY = playerRocket1.getY();
	    		
	    		if (playerRocket1.isVisible() && !bomb.isDestroyed()) {    // �÷��̾ �㿡 �¾� �׾��� ���..			
	    			if(bombX >=(playerX) && bombX <= (playerX + playerRocket1.rocketImgWidth)
	    				&& bombY >= (playerY) && bombY <= (playerY + playerRocket1.rocketImgHeight)) {
	    				ImageIcon img = new ImageIcon(explImg);
	    				playerRocket1.setImage(img.getImage());
	    				playerRocket1.setDying(true);
	    				bomb.setDestroyed(true);    				
	    		} }
	    		
	    		if (playerRocket2.isVisible() && !bomb.isDestroyed()) {    // �÷��̾ �㿡 �¾� �׾��� ���..			
	    			if(bombX >=(playerX) && bombX <= (playerX + playerRocket2.rocketImgWidth)
	    				&& bombY >= (playerY) && bombY <= (playerY + playerRocket2.rocketImgHeight)) {
	    				ImageIcon img = new ImageIcon(explImg);
	    				playerRocket2.setImage(img.getImage());
	    				playerRocket2.setDying(true);
	    				bomb.setDestroyed(true);    				
	    		} }
	    		
	    		if (!bomb.isDestroyed()) {    			
	    			bomb.setY(bomb.getY() + 1);    			
	    			if (bomb.getY() >= Params.GROUND - Params.BOMB_HEIGHT) {    				
	    				bomb.setDestroyed(true);
	    		} }
	    	} // end for(Alien alien : aliens) 
	    	
	    	
    	}	
	}

	private void drawAliens(Graphics g) {

        for (Alien alien : aliens) {

            if (alien.isVisible()) {
                g.drawImage(alien.getImage(), alien.getX(), alien.getY(), this);
            }

            if (alien.isDying()) {
                alien.die();
            }
        }
    }

    private void drawPlayer(Graphics g) {

        if (playerRocket1.isVisible()) {

            g.drawImage(playerRocket1.getImage(), playerRocket1.getX(), playerRocket1.getY(), this);
        }

        if (playerRocket1.isDying()) {

            playerRocket1.die();
            // �̹��� �ٲ���!
            
            
            if(rocketNum==1) Framework.gameState = Framework.GameState.GAMEOVER;
        }
        
        if(rocketNum==2) {
        	if (playerRocket2.isVisible()) {

                g.drawImage(playerRocket2.getImage(), playerRocket2.getX(), playerRocket2.getY(), this);
            }

            if (playerRocket2.isDying()) {

                playerRocket2.die();
                // �̹��� �ٲ���!
            }
            
            if(playerRocket1.isDying() && playerRocket2.isDying()) {
            	Framework.gameState = Framework.GameState.GAMEOVER;
            }
        }
    }
	
    


    private void drawBombing(Graphics g) {

        for (Alien a : aliens) {

            Alien.Bomb b = a.getBomb();

            if (!b.isDestroyed()) {

                g.drawImage(b.getImage(), b.getX(), b.getY(), this);
            }
        }
    }
    
    
    
    public void Draw(Graphics2D g2d, Point mousePosition)
    {
        g2d.drawImage(backgroundImg, 0, 0, Framework.frameWidth, Framework.frameHeight, null);
        
        landingArea.Draw(g2d);
        
        playerRocket1.Draw(g2d);
        if(Framework.playerCnt == 2) {
        	rocketNum=2;
        	playerRocket2.Draw(g2d);
        }
        
        
        drawPlayer(g2d);
    	drawBombing(g2d);
    	
        if(Framework.level != 1) {
        	drawAliens(g2d);
        } 
        
    }
    
    
    public void DrawGameOver(Graphics2D g2d, Point mousePosition, long gameTime)
    {
        Draw(g2d, mousePosition);
        
        g2d.drawString("Click the button to restart.", Framework.frameWidth / 2 - 80, Framework.frameHeight / 3 + 70);
        
        switch(Framework.playerCnt) {
			case 1:
				if(playerRocket1.landed)
		        {
		            g2d.drawString("You have successfully landed!", Framework.frameWidth / 2 - 100, Framework.frameHeight / 3);
		            g2d.drawString("You have landed in " + gameTime / Framework.secInNanosec + " seconds.", Framework.frameWidth / 2 - 100, Framework.frameHeight / 3 + 20);
		        }
		        else
		        {
		            g2d.setColor(Color.red);
		            g2d.drawString("You have crashed the rocket!", Framework.frameWidth / 2 - 90, Framework.frameHeight / 3);
		            g2d.drawImage(redBorderImg, 0, 0, Framework.frameWidth, Framework.frameHeight, null);
		        }
				// �÷��̾�1�� ������ �����
			break;
			case 2:
				// �÷��̾�1�� 2�� ������ �����
				// ���� �̰���� ǥ������.
				if(playerRocket1.landed)	
		        {
		            g2d.drawString("Rocket1 has successfully landed!", Framework.frameWidth / 2 - 100, Framework.frameHeight / 3);
		            g2d.drawString("You have landed in " + gameTime / Framework.secInNanosec + " seconds.", Framework.frameWidth / 2 - 95, Framework.frameHeight / 3 + 20);
		        }
				else if(playerRocket2.landed) {
					g2d.drawString("Rocket2 has successfully landed!", Framework.frameWidth / 2 - 100, Framework.frameHeight / 3);
		            g2d.drawString("You have landed in " + gameTime / Framework.secInNanosec + " seconds.", Framework.frameWidth / 2 - 95, Framework.frameHeight / 3 + 20);
				}
		        else if(playerRocket1.crashed && playerRocket2.crashed)
		        {
		            g2d.setColor(Color.red);
		            g2d.drawString("You have crashed the rocket!", Framework.frameWidth / 2 - 90, Framework.frameHeight / 3);
		            g2d.drawImage(redBorderImg, 0, 0, Framework.frameWidth, Framework.frameHeight, null);
		        }
		        else
		        {
		        	g2d.drawString("Game over", Framework.frameWidth / 2 - 50, Framework.frameHeight / 3);
		        }
			break;
		}
    }
    

}
