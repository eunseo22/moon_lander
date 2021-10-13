package sprite;

import javax.swing.ImageIcon;

import sprite.Alien.Bomb;


public class Asteroid extends Sprite{
	private boolean destroyed;
	
	public Asteroid(int x, int y) {
		
		initAsteroid(x, y);
	}
	
	private void initAsteroid(int x, int y) {
		
		setDestroyed(true);
		this.x = x;
		this.y = y;
		
		String alienImg = "./resources/images/sprite/asteroid1.png";
		ImageIcon img = new ImageIcon(alienImg);
		
		setImage(img.getImage());
	}
	
	public void setDestroyed(boolean destroyed) {

        this.destroyed = destroyed;
    }
	
	public boolean isDestroyed() {

        return destroyed;
    }
	
	public void act(int direction) {
		
		this.x += direction;
	}
	// 이 함수 필요한가?
}