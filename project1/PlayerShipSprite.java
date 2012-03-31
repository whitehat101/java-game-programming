package project1;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.util.LinkedList;

import libs.Sprite;

public class PlayerShipSprite implements Sprite
{
    final Color myColor = Color.WHITE;
    Rectangle boundingBox;
    Rectangle ship;
    boolean fireOnUpdate;
    LinkedList<Sprite> spriteAddList, spriteDelList;

    public PlayerShipSprite( Rectangle boundingBox, LinkedList<Sprite> spriteAddList,LinkedList<Sprite> spriteDelList)
    {
        this.boundingBox = boundingBox;
        this.spriteAddList = spriteAddList;
        this.spriteDelList = spriteDelList;
        
        int shipWidth = 20;
        ship = new Rectangle(boundingBox.x + (boundingBox.width-shipWidth)/2,0,shipWidth,boundingBox.height);
        fireOnUpdate = false;
        
        System.out.print( "PlayerShipSprite spawn @(" + boundingBox + ")\n" );
    }

    @Override
    public void checkCollision( Sprite obj )
    {
        // TODO Auto-generated method stub

    }

    @Override
    public void draw( Graphics g )
    {
        g.setColor( myColor );
        g.drawRect( boundingBox.x, boundingBox.y, boundingBox.width, boundingBox.height );
        g.fillRect( boundingBox.x + ship.x, boundingBox.y + ship.y, ship.width, ship.height );

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
        if(me.getID() == MouseEvent.MOUSE_CLICKED){
            spriteAddList.add(
            new MissleSprite(new Point((int)ship.getCenterX()+boundingBox.x,(int)ship.getCenterY()+boundingBox.y), -10 , spriteDelList)
            );
        }
        
        if (me.getID() == MouseEvent.MOUSE_MOVED)
        {
            int adjustedX = me.getX() - ship.width/2;//Center the object on the curser
            //If in boundingBox
            if(adjustedX >= boundingBox.x && adjustedX <= boundingBox.x + boundingBox.width - ship.width)
            {
                ship.x = adjustedX - boundingBox.x;
            }
            //If left of boundingBox
            else if (adjustedX < boundingBox.x)
            {
                ship.x = 0;
            }
            //If right of boundingBox
            else if (adjustedX > boundingBox.x + boundingBox.width - ship.width)
            {
                ship.x = boundingBox.width - ship.width;
            }
        }
    }

    @Override
    public void update()
    {
    }

}
