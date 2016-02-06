import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;
import java.util.Random;

import javax.swing.JPanel;

@SuppressWarnings("serial")
public class BlockBreakerPanel extends JPanel implements KeyListener{

	ArrayList<Block> blocks = new ArrayList<Block>();
	ArrayList<Block> ball = new ArrayList<Block>();
	ArrayList<Block> powerup = new ArrayList<Block>();
	Block paddle;
	Animate animate;
	Thread thread;
	int size = 25;
	
	BlockBreakerPanel(){
		paddle = new Block(175, 400, 150, 25, "paddle.png");
		for (int i = 0; i<8 ; i++){
			blocks.add(new Block((i*60+2), i, 60, 25, "Blue.png" ));
		}
		for (int i = 0; i<8 ; i++){
			blocks.add(new Block((i*60+2), i+25, 60, 25, "Red.png" ));
		}
		for (int i = 0; i<8 ; i++){
			blocks.add(new Block((i*60+2), i+50, 60, 25, "Yellow.png" ));
		}
		for (int i = 0; i<8 ; i++){
			blocks.add(new Block((i*60+2), i+75, 60, 25, "Green.png" ));
		}
		Random random = new Random();
		blocks.get(random.nextInt(32)).powerup = true;
		blocks.get(random.nextInt(32)).powerup = true;
		blocks.get(random.nextInt(32)).powerup = true;
		blocks.get(random.nextInt(32)).powerup = true;
		blocks.get(random.nextInt(32)).powerup = true;
		ball.add(new Block(237, 370, 25, 25, "Ball.png"));
		
		addKeyListener(this);
		setFocusable(true);
		
	}
	public void paintComponent(Graphics g){
		super.paintComponent(g);
		for (Block b: blocks)
			b.draw(g, this);
		
		for (Block b: ball)
			b.draw(g, this);
		for (Block p: powerup)
			p.draw(g, this);
		
		paddle.draw(g,  this);
	}
	public void update() {
		for (Block p : powerup){
			p.y+=1;
			if (p.intersects(paddle) && !p.destroyed){
				p.destroyed = true; 
				ball.add(new Block(paddle.x, 400, 25, 25, "ball.png"));
			}
		}	
		for (Block ba : ball){
			if (ba.x>(getWidth()-size) && ba.dx>0 || ba.x < 0){
				ba.dx*=-1;
			}	
			if (ba.y < 0 || ba.intersects(paddle)) {
				ba.dy*=-1;
			}
			ba.x += ba.dx;
			ba.y += ba.dy;
			for (Block b : blocks){
				if ((ba.left.intersects(b.left) || ba.right.intersects(b.right))  && !b.destroyed){
					b.destroyed = true;
					b.dx*=-1;
					if (b.powerup){
						powerup.add(new Block (b.x, b.y, 25, 19, "extra.png"));
					}
				}
				else if (ba.intersects(b) && !b.destroyed){
					b.destroyed = true;
					ba.dy*=-1;
					if (b.powerup){
						powerup.add(new Block (b.x, b.y, 25, 19, "extra.png"));
					}
				}
			}
		}
		repaint();
	}
	@Override
	public void keyTyped(KeyEvent e) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void keyPressed(KeyEvent e) {
		if (e.getKeyCode() == KeyEvent.VK_ENTER){
			animate = new Animate(this);
			thread = new Thread(animate);
			thread.start();
		}
		if (e.getKeyCode() == KeyEvent.VK_LEFT && paddle.x >0){
			paddle.x -= 30;
		}
		if (e.getKeyCode() == KeyEvent.VK_RIGHT && paddle.x < (getWidth()-paddle.width)){
			paddle.x += 30;
		}	
	}
	@Override
	public void keyReleased(KeyEvent e) {
		// TODO Auto-generated method stub
		
	}
}
