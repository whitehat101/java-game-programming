package labPractice1;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Frame;
import java.awt.Graphics2D;
import java.awt.Toolkit;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class LabPractice1 {
	/**
	 * @param args
	 */
	public static void main(String[] args) {
	    Frame f = new Frame();
	    f.setUndecorated( true );
	    f.setSize(800,600);
	    
	    Toolkit tlkt = Toolkit.getDefaultToolkit();
	    Dimension screen = tlkt.getScreenSize();
	    
	    //Center the screen
	    f.setLocation((screen.width - f.getWidth())/2,
	            (screen.height - f.getHeight())/2);

	    //Input
        KeyAdapter l = new KeyAdapter()
        {
            public void keyPressed(KeyEvent ke)
            {
                System.out.println(ke.getKeyCode());
                switch(ke.getKeyCode()){
                    case KeyEvent.VK_Q:
                    case KeyEvent.VK_ESCAPE:
                        System.exit( 0 );                    
                }
            }
        };
        f.addKeyListener(l);

	    
	    f.setFocusable( true );
	    f.setIgnoreRepaint(true);//Will be handled by the game engine

	    
        f.setVisible(true);
        f.requestFocus();//I'm a needy application! Look at me!
        
        Graphics2D g2d = (Graphics2D) f.getGraphics();
        g2d.setFont(new Font("Times New Roman", Font.BOLD, 24));
        g2d.setColor( Color.black );
        g2d.drawString( "Let me out!", 5, 29 );
	    
        System.out.println("Hello Professor William Hooper.");
        System.out.println("I hope you are having a pleasant day.");
	}
}
