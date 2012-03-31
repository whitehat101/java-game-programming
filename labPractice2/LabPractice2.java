package labPractice2;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Frame;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.Toolkit;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.geom.RoundRectangle2D;

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
            @Override
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
        // Enable antialiasing for text
        g2d.setRenderingHint( RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON );
        // Enable antialiasing for shapes
        g2d.setRenderingHint( RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON );

        g2d.setStroke( new BasicStroke( 5.0f, // Line width
                BasicStroke.CAP_ROUND, // End-cap style
                BasicStroke.JOIN_ROUND ) ); // Vertex join style

        // Paint BG
        g2d.setColor( Color.getHSBColor( 0, 0, .8f ) );
        g2d.fillRect( 0, 0, 800, 600 );
        
        
        g2d.setFont( new Font( "Times New Roman", Font.BOLD, 12 ) );
        g2d.setColor( Color.white );
        g2d.drawString( "Jeremy Ebler", 0, 10 );
        g2d.setColor( Color.black );
        
        // segments
        RoundRectangle2D roundedRectangle = new RoundRectangle2D.Float( 50, 50, 4, 20, 5, 5 );
        g2d.fill( roundedRectangle );

        int width = 16, height = 16;

        g2d.drawLine( 0, 0, 16, 0 );
        g2d.drawLine( 0, 0, 0, 16 );

        /*
        Shape circle = new Ellipse2D.Float( 100.0f, 100.0f, // Upper-left corner
                300.0f, 300.0f ); // Width and height
        g2d.setStroke( new BasicStroke( 10.0f ) ); // Set line width
        g2d.draw( circle ); // Now draw it
        */

        //arrow
        int x = 0, y = 500;
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
