import java.awt.Canvas;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;

import javax.swing.JFrame;

public class Game extends Canvas implements Runnable {
	
	
	public static JFrame frame;
	private Thread thread;
	private boolean isRunning = true;
	private final int WIDTH = 240;
	private final int HEIGHT = 160;
	private final int SCALE = 3;
	
	private BufferedImage image;
	
	private Spritesheet sheet; 
	private BufferedImage[] player;
	private int frames = 0;
	private int maxframes = 10;// tempo animar personagem < mais rapido 
	private int curAnimation = 0,maxAnimation = 3;
	
	public Game() {
		sheet = new Spritesheet("/spritesheet.png");
		player = new BufferedImage[4];
		player[0] = sheet.getSprite(0, 0, 16, 16);
		player[1] = sheet.getSprite(16,0,16,16);
		player[2] = sheet.getSprite(32,0,16,16);
		player[3] = sheet.getSprite(48,0,16,16);
		setPreferredSize(new Dimension(WIDTH*SCALE,HEIGHT*SCALE));
		initiFrame();
		image = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_RGB);
		
	}
	
	
	public void initiFrame() {
		frame = new JFrame("grafico");
		frame.add(this);
		frame.setResizable(false);
		frame.pack();
		frame.setLocationRelativeTo(null);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setVisible(true);
	}
	
	public synchronized void start() {
		thread = new Thread(this);
		isRunning = true;
		thread.start();
	}
	
	public synchronized void stop( ) {
		try {
			thread.join();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	
	public static void main(String args[]) {
		Game game = new Game();
		game.start();
		
	}
	
	
	public void tick() {
		frames ++ ;
		if (frames> maxframes) {
			frames = 0;
			curAnimation ++ ;
		if (curAnimation > maxAnimation) {
			curAnimation = 0;
		}
		
		}
			
	}
	
	public void render() {
		BufferStrategy bs = this.getBufferStrategy();
		if(bs == null ) {
			this.createBufferStrategy(3);
			return;
		}
		Graphics g = image.getGraphics();
		g.setColor(new Color(20,60,60));
		g.fillRect(0, 0, WIDTH, HEIGHT);
		
		//renderização do jogo
		Graphics2D g2 = (Graphics2D) g;
		g2.drawImage(player[curAnimation], 90, 90, null);
		

		g.dispose();
		g = bs.getDrawGraphics();
		g.drawImage(image, 0, 0, WIDTH*SCALE, HEIGHT*SCALE, null);
		bs.show();
		
	}
	
	
	public void run() {
		long lastTime = System.nanoTime();
		double amountOfTicks = 60.0;
		double ns = 1000000000 / amountOfTicks;
		double delta = 0;
		int frames = 0;
		double timer = System.currentTimeMillis();
		while (isRunning) {
			long now = System.nanoTime();
			delta+= (now - lastTime) / ns;
			lastTime = now;
			if (delta >= 1) {
				tick();
				render();
				frames++;
				delta--;
			}
			
			if (System.currentTimeMillis() - timer >= 1000) {
				System.out.println("FPS " + frames);
				frames = 0;
				timer+=1000;
			}
		}
		
		stop();
		
	}
	
	
	
	
} 


