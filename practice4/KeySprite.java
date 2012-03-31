package practice4;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;

import libs.Sprite;

public class KeySprite implements Sprite
{
    GameEngineForPratice4 ge;
    int baseX;
    int baseY;
    int radius;
    Color myColor;
    int flicker;

    int xDirection;
    int yDirection;
    float speed;

    public KeySprite( GameEngineForPratice4 GE, int initX, int initY, int initRadius )
    {

        baseX = initX;
        baseY = initY;
        radius = initRadius;
        myColor = Color.RED;
        flicker = 0;
        speed = 5f;
        ge = GE;

        System.out.print( "KeySprite spawn @(" + baseX + "," + baseY + ")\n" );
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
        if ( flicker > 0 )
        {
            g.setColor( Color.YELLOW );
            flicker--;
        }
        else
        {
            g.setColor( myColor );
        }
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
            case KeyEvent.VK_RIGHT:
                xDirection = 10;
                break;
            case KeyEvent.VK_LEFT:
                xDirection = -10;
                break;
            case KeyEvent.VK_UP:
                yDirection = -10;
                break;
            case KeyEvent.VK_DOWN:
                yDirection = 10;
                break;
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
        int TryX = ( int ) ( xDirection );
        if ( baseX + TryX >= radius && baseX + TryX <= ge.getWidth() - radius)
            baseX += TryX;
        xDirection = 0;

        int TryY = ( int ) ( yDirection );
        if ( baseY + TryY >= radius && baseY + TryY <= ge.getHeight() - radius )
            baseY += TryY;
        yDirection = 0;
    }

}
