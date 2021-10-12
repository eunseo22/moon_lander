package Sprite;

import javax.swing.ImageIcon;

public class Enemy extends Sprite {
	
	private Bomb bomb;
	
	public Enemy(int x, int y) {
		
		initEnemy(x, y);
	}
	
	private void initEnemy(int x, int y) {
		
		this.x = x;
		this.y = y;
		
		bomb = new Bomb(x,y);
		
		String EnemyImg = "./src/resources/images/Sprite/asteroid.png";
		ImageIcon img = new ImageIcon(EnemyImg);
		
		setImage(img.getImage());
	}
	
	public void act(int direction) {		
		this.x += direction;
	}
	
	public Bomb getBomb() {
		return bomb;
	}
	
	public class Bomb extends Sprite {		
		private boolean destroyed;
		
		public Bomb(int x, int y) {			
			initBomb(x, y);
		}
		
		private void initBomb(int x, int y) {			
			setDestroyed(true);
			
			this.x = x;
			this.y = y;
			
			String bombImg = "./src/resources/images/Sprite/bomb.png";
			ImageIcon img = new ImageIcon(bombImg);
			setImage(img.getImage());
		}
		
        public void setDestroyed(boolean destroyed) {
            this.destroyed = destroyed;
        }

        public boolean isDestroyed() {
            return destroyed;
        }
		
	}
}
