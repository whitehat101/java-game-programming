package practice4;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;

import libs.Sprite;

public class MouseSprite implements Sprite
{
    GameEngineForPratice4 ge;
    int baseX;
    int baseY;
    int radius;
    Color myColor;
    int flicker;
    Point mousePoint;

    int targetX;
    int targetY;
    float speed;

    public MouseSprite( GameEngineForPratice4 GE, int initX, int initY, int initRadius )
    {

        baseX = initX;
        baseY = initY;
        radius = initRadius;
        myColor = Color.RED;
        flicker = 0;
        speed = 5f;
        ge = GE;

        System.out.print( "MouseSprite spawn @(" + baseX + "," + baseY + ")\n" );
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
    }

    @Override
    public void mouseAction( MouseEvent me )
    {
        //System.out.println(me.getID() + me.toString());
        if (me.getID() == MouseEvent.MOUSE_DRAGGED)
        {
            //System.out.println("Drag");
            if(mousePoint != null){
                if ( baseY + (me.getY() - mousePoint.y) >= radius 
                        && baseY + (me.getY() - mousePoint.y) <= ge.getHeight() - radius )
                {
                    baseY += me.getY() - mousePoint.y;
                    mousePoint.y = me.getY();
                }
                if ( baseX + (me.getX() - mousePoint.x) >= radius 
                        && baseX + (me.getX() - mousePoint.x) <= ge.getWidth() - radius )
                {
                    baseX += me.getX() - mousePoint.x;
                    mousePoint.x = me.getX();
                }
//                mousePoint = me.getPoint();
            }   
        }
        else if (me.getID() == MouseEvent.MOUSE_PRESSED)
        {
            // radius^2  <= (x-targetX)^2 + (y-targetY)^2

            long xPart = ( long ) Math.pow( baseX - me.getX() , 2 );
            long yPart = ( long ) Math.pow( baseY - me.getY() , 2 );

//            System.out.println( "(" + xPart + ", " +
//                    yPart + ") >= " + Math.pow( radius, 2 ));

            if(Math.pow(radius,2) >= xPart + yPart)
            {
                mousePoint = me.getPoint();
                setColor(new Color((int)(Math.random()*255),
                        (int)(Math.random()*255),
                        (int)(Math.random()*255)
                        ));
            }
            else
            {
                System.out.println("miss" + me.getPoint().toString());
            }
            
        }
        else if (me.getID() == MouseEvent.MOUSE_RELEASED)
        {
            mousePoint = null;
            
        }
    }

    @Override
    public void update()
    {

        int TryX = ( int ) ( targetX );
        int TryY = ( int ) ( targetY );

        if ( baseX + TryX < ge.getWidth() - radius && baseX + TryX > 0 )
            baseX += TryX;
        if ( baseY + TryY < ge.getHeight() - radius && baseY + TryY > 0 )
            baseY += TryY;
    }

}
