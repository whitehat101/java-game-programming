package project3.ext;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;

import libs.Sprite;

public class ASprite implements Sprite
{
    private Rectangle boundingBox;
    private Rectangle sprite;
    
    private Color myColor;
    private int flicker;
    
    private Point target;
    private float speed;
   

    public ASprite( Point start, int radius, Rectangle boundingBox, float speed )
    {
        sprite = new Rectangle( start );
        sprite.width = 2 * radius;
        sprite.height = 2 * radius;
        this.boundingBox = boundingBox;
        this.speed = speed;

        myColor = Color.RED;// default
        flicker = 0;
        
        System.out.print( "ASprite spawn @(" + sprite.x + "," + sprite.y + ")\n" );
     }
    
    public void setTarget( Point target )
    {
        // System.out.println( "\tTargeting: " + target.toString() );
        this.target = target;
    }

    public void setColor( Color c )
    {
        myColor = c;
    }

    @Override
    public void checkCollision( Sprite obj )
    {
    }

    @Override
    public void draw( Graphics g )
    {
        if(flicker > 0){
            g.setColor( Color.YELLOW );
            flicker--;
        } else {
            g.setColor( myColor );
        }
        g.fillOval( sprite.x, sprite.y, sprite.width, sprite.height );
    }

    @Override
    public Rectangle intersects( Rectangle boundingBox )
    {
        return null;
    }

    @Override
    public void keyboardAction( KeyEvent ke )
    {
    }

    @Override
    public void mouseAction( MouseEvent me )
    {
    }

    @Override
    public void update()
    {
        if ( Math.random() * 100 < 2.5f )
        {
            flicker = ( int ) Math.round( Math.random() * 3 ) + 2;
        }
        
        double xAdjust = 0.0;
        double yAdjust = 0.0;
        
        // Randomly don't move
        if ( Math.random() < 0.1f )
            return;
        
        if ( target != null )
        {
            // System.out.print("("+baseX+","+baseY+") ");
            xAdjust = ( target.x - sprite.x );
            yAdjust = ( target.y - sprite.y );
            double dist = Math.sqrt( xAdjust * xAdjust + yAdjust * yAdjust );
            // System.out.print("Dist("+(int)dist+") ");
            // System.out.print("delta("+xAdjust+","+yAdjust+")");

            xAdjust = xAdjust / dist * speed;
            yAdjust = yAdjust / dist * speed;
            // System.out.print("adj("+xAdjust+","+yAdjust+")\n");
        }
        
        //Movement
        sprite.x += ( int ) ( Math.random() * speed * 2 - speed + xAdjust );
        sprite.y += ( int ) ( Math.random() * speed * 2 - speed + yAdjust );
        
        //Keep in field
        if ( sprite.x < boundingBox.x )
        {
            sprite.x = boundingBox.x;
        }
        else if ( sprite.x > boundingBox.x + boundingBox.width - sprite.width )
        {
            sprite.x = boundingBox.x + boundingBox.width - sprite.width;
        }

        if ( sprite.y < boundingBox.y )
        {
            sprite.y = boundingBox.y;
        }
        else if ( sprite.y > boundingBox.y + boundingBox.height - sprite.height )
        {
            sprite.y = boundingBox.y - sprite.height;
        }

    }

}
