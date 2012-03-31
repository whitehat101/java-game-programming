package labPractice2;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Frame;
import java.awt.Graphics2D;
import java.awt.Toolkit;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

import libs.Star2D;

public class LabPractice2 {
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
            public void keyPressed(KeyEvent key)
            {
                //System.out.println(ke.getKeyCode());
                switch(key.getKeyCode()){
                    case KeyEvent.VK_Q:
                    case KeyEvent.VK_ESCAPE:
                        System.exit( 0 );  
                        break;
                }
            }
        };
        f.addKeyListener(l);

	    
	    f.setFocusable( true );
	    f.setIgnoreRepaint(true);//Will be handled by the game engine

	    
        f.setVisible(true);
        f.requestFocus();//I'm a needy application! Look at me!
        
        Graphics2D g2d = (Graphics2D) f.getGraphics();
        g2d.setColor( Color.getHSBColor( 0, 0, .4f ) );
        g2d.fillRect( 0, 0, 800, 600 );
        
        
        g2d.setFont(new Font("Times New Roman", Font.BOLD, 20));
        g2d.setColor( Color.white );
        g2d.drawString( "Hello World!", 0, 275 );
        
        
        g2d.setColor( Color.MAGENTA );
        g2d.fill3DRect( 5, 5, 19, 44, truew );

        int x,y,height,width;

        
        // x = y^2 - 2*y
        x = 150;
        y = 100;
        width = 200;
        height = 200;
        for(int t = -16; t < 12; t++){
            
            System.out.println((float)(12 - t)/32);
            g2d.setColor(Color.getHSBColor(
                    .42f + 3.1415926535f,
                    (float)(12 - t)/28,
                    (float)(12 - t)/28
                 ));
            g2d.fillOval( x + t*t - 2*t, y + 3*t, width - 9*t, height - 9*t);
        }
        
        // x = y^2 - 2*y
        x = 75;
        y = 400;
        width = 200;
        height = 200;
        for(int t = -12; t < 12; t++){
            
            System.out.println((float)(12 - t)/32);
            /*g2d.setColor(Color.getHSBColor(
                    .42f + 3.1415926535f,
                    (float)(12 - t)/24,
                    (float)(12 - t)/24
                 ));*/
            g2d.setColor( Color.ORANGE );
            g2d.fillRect( x + t*t - 2*t, y + 3*t, width - 5*t - t*t, height - 5*t - t*t); 
        }
        
        //arrow
        x=0;
        y=280;
        g2d.setColor(Color.getHSBColor( .7f, .8f, .9f ) );
        g2d.fillPolygon(
                new int[] {x, x+125, x+125, x+200, x+125, x+125, x  },
                new int[] {y,     y,  y-50,  y+50, y+150, y+100, y+100 },
                7 );
        
        //Star
        g2d.setColor( Color.YELLOW );
        g2d.fill(new Star2D(x+200-25,y+50-25,50,50));

	}
}
