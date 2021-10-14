package sprite;

import javax.swing.ImageIcon;

public class Shot extends Sprite  {
	public Shot() {
		super();
	}
	
	
	public Shot(int x, int y) {
		initShot(x, y);
	}
	
	private void initShot(int x, int y) {
		
		String shotImg = "./resources/images/sprite/laser4.png";
		ImageIcon img = new ImageIcon(shotImg);
		setImage(img.getImage());
		
		int X_SPACE = 6;
		setX(x + X_SPACE);
		
		int Y_SPACE = 1;
		setY(y - Y_SPACE);
	}
}
