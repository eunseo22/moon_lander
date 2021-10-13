package moon_lander;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.swing.JButton;

public class Game {

    
    private PlayerRocket playerRocket1, playerRocket2;
    private LandingArea landingArea;
    private BufferedImage backgroundImg;
    private BufferedImage redBorderImg;
    
    public static int rocketNum = 1;

    public Game()
    {
        Framework.gameState = Framework.GameState.GAME_CONTENT_LOADING;
        
        Thread threadForInitGame = new Thread() {
            @Override
            public void run(){
                Initialize();
                LoadContent();
                
                Framework.gameState = Framework.GameState.PLAYING;
            }
        };
        threadForInitGame.start();
    }
    
    
    private void Initialize()
    {
    	switch(Framework.playerCnt) {
    		case 1:
    			rocketNum = 1;
    			playerRocket1 = new PlayerRocket();
    		break;
    		case 2:
    			rocketNum = 1;
    			playerRocket1 = new PlayerRocket();
    			rocketNum = 2;
    			playerRocket2 = new PlayerRocket();
    		break;
    	}
        landingArea  = new LandingArea();
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
    	switch(Framework.playerCnt) {
			case 1:
				rocketNum = 1;
				playerRocket1.ResetPlayer();
			break;
			case 2:
				if(playerRocket2==null) {
					rocketNum = 1;
					playerRocket1.ResetPlayer();
					rocketNum = 2;
					playerRocket2 = new PlayerRocket();
				} else {
					rocketNum = 1;
					playerRocket1.ResetPlayer();
					rocketNum = 2;
					playerRocket2.ResetPlayer();
				}
			break;
		}
    }
    
    
    public void UpdateGame(long gameTime, Point mousePosition)
    {
    	switch(Framework.playerCnt) {
    		case 1:
    			// Move the rocket
    	        playerRocket1.Update();
    	        
    	        // Checks where the player rocket is. Is it still in the space or is it landed or crashed?
    	        // First we check bottom y coordinate of the rocket if is it near the landing area.
    	        if(playerRocket1.y + playerRocket1.rocketImgHeight - 10 > landingArea.y)
    	        {
    	            // Here we check if the rocket is over landing area.
    	            if((playerRocket1.x > landingArea.x) && (playerRocket1.x < landingArea.x + landingArea.landingAreaImgWidth - playerRocket1.rocketImgWidth))
    	            {
    	                // Here we check if the rocket speed isn't too high.
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
    		case 2:
    			// Move the rocket
    			rocketNum = 1;
				playerRocket1.Update();
				rocketNum = 2;
		        playerRocket2.Update();
		        
		        // Checks where the player rocket 1 is. Is it still in the space or is it landed or crashed?
		        // First we check bottom y coordinate of the rocket if is it near the landing area.
		        if(playerRocket1.y + playerRocket1.rocketImgHeight - 10 > landingArea.y)
		        {
		            // Here we check if the rocket is over landing area.
		            if((playerRocket1.x > landingArea.x) && (playerRocket1.x < landingArea.x + landingArea.landingAreaImgWidth - playerRocket1.rocketImgWidth))
		            {
		                // Here we check if the rocket speed isn't too high.
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
		            // Here we check if the rocket is over landing area.
		            if((playerRocket2.x > landingArea.x) && (playerRocket2.x < landingArea.x + landingArea.landingAreaImgWidth - playerRocket2.rocketImgWidth))
		            {
		                // Here we check if the rocket speed isn't too high.
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
		        } else if(playerRocket1.landed) {
		        	Framework.gameState = Framework.GameState.GAMEOVER;
		        } else if(playerRocket2.landed) {
		        	Framework.gameState = Framework.GameState.GAMEOVER;
		        }
    	}
    }
    
   
    public void Draw(Graphics2D g2d, Point mousePosition)
    {
        g2d.drawImage(backgroundImg, 0, 0, Framework.frameWidth, Framework.frameHeight, null);
        
        landingArea.Draw(g2d);
        
        switch(Framework.playerCnt) {
			case 1:
				rocketNum = 1;
				playerRocket1.Draw(g2d);
			break;
			case 2:
				rocketNum = 1;
				playerRocket1.Draw(g2d);
				rocketNum = 2;
				playerRocket2.Draw(g2d);
			break;
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
			break;
			case 2:
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


	public void InitializeE(int level) {
		// TODO Auto-generated method stub
		
	}
}
