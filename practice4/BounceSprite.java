package practice4;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;

import libs.Sprite;

public class BounceSprite implements Sprite
{
    GameEngineForPratice4 ge;
    int baseX;
    int baseY;
    int radius;
    Color myColor;
    int flicker;

    int xBounce;
    int yBounce;
    float speed;

    public BounceSprite( GameEngineForPratice4 GE, int initX, int initY, int initRadius )
    {

        baseX = initX;
        baseY = initY;
        radius = initRadius;
        myColor = Color.RED;
        flicker = 0;
        speed = 25f;
        ge = GE;
        
        double angle = Math.random()*Math.PI*2;
        xBounce = ( int ) ( Math.cos(angle)*speed );
        yBounce = ( int ) ( Math.sin(angle)*speed );

        System.out.print( "BounceSprite spawn @(" + baseX + "," + baseY + ") "+angle+"\n" );
    }

    public void setColor( Color c )
    {
        myColor = c;
    }

    @Override
    public void checkCollision( Sprite obj )
    {
        // TODO Auto-generated method stub

    }

    @Override
    public void draw( Graphics g )
    {
        g.fillOval( baseX-radius, baseY-radius, radius*2, radius*2 );
    }

    @Override
    public Rectangle intersects( Rectangle boundingBox )
    {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void keyboardAction( KeyEvent ke )
    {
        // TODO Auto-generated method stub
        switch ( ke.getKeyCode() )
        {
            case KeyEvent.VK_BRACELEFT:
                speed += 0.25f;
            case KeyEvent.VK_BRACERIGHT:
                speed -= 0.25f;
        }

    }

    @Override
    public void mouseAction( MouseEvent me )
    {
        // TODO Auto-generated method stub

    }

    @Override
    public void update()
    {
      //XPart
        if ( baseX + xBounce <= radius )
        {
            baseX = radius;
            xBounce *= -1;
        }
        else if ( baseX + xBounce >= ge.getWidth() - radius )
        {
            baseX = ge.getWidth() - radius;
            xBounce *= -1;
        }
        else
        {
            baseX += xBounce;
        }

      //yPart
        if ( baseY + yBounce <= radius )
        {
            baseY = radius;
            yBounce *= -1;
        }
        else if ( baseY + yBounce >= ge.getHeight() - radius )
        {
            baseY = ge.getHeight() - radius;
            yBounce *= -1;
        }
        else
        {
            baseY += yBounce;
        }
    }

}
