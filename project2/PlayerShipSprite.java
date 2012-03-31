package project2;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;

import libs.GameEvent;
import libs.GameEvent.GameEventType;
import libs.GameEventDispatcher;
import libs.Sprite;

public class PlayerShipSprite implements Sprite
{
    final Color myColor = Color.WHITE;
    private Rectangle boundingBox;
    private Rectangle ship;
    private boolean fireOnUpdate;
    private long fireTimer;
    private GameEventDispatcher dispatcher;
    private int mouseX;
    final private int maxSpeed = 10;

    public PlayerShipSprite( Rectangle boundingBox )
    {
        this.boundingBox = boundingBox;
        
        fireTimer = System.nanoTime();

        int shipWidth = 20;
        ship = new Rectangle( boundingBox.x + ( boundingBox.width - shipWidth ) / 2, boundingBox.y, shipWidth,
                boundingBox.height );
        dispatcher = GameEventDispatcher.getGameEventDispatcher();
        fireOnUpdate = false;
        
        System.out.print( "PlayerShipSprite spawn @(" + boundingBox + ")\n" );
    }

    @Override
    public void checkCollision( Sprite obj )
    {
        if ( obj instanceof MissleSprite )
        {
            if ( ( ( MissleSprite ) obj ).getParent() instanceof PlayerShipSprite )
                return;

            // Rectangle rectangle = new Rectangle(drawPoint.x, drawPoint.y,
            // widthheight.width, widthheight.height);
            Rectangle result = obj.intersects( ship );
            if ( result != null )
            {
                // System.out.println("InvaderSprite hit by MissleSprite");
                // remove the missile
                dispatcher.dispatchEvent( new GameEvent( this, GameEventType.Remove, obj ) );
                dispatcher.dispatchEvent( new GameEvent( this, GameEventType.Remove, this ) );
                dispatcher.dispatchEvent( new GameEvent( this, GameEventType.Start, new Integer( 1 ) ) );
            }
        }

    }

    @Override
    public void draw( Graphics g )
    {
        g.setColor( myColor );
        g.drawRect( boundingBox.x, boundingBox.y, boundingBox.width, boundingBox.height );
        g.fillRect( ship.x, ship.y, ship.width, ship.height );

    }

    @Override
    public Rectangle intersects( Rectangle boundingBox )
    {
        return boundingBox.intersects( this.boundingBox ) ? boundingBox.intersection( this.boundingBox ) : null;
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
            fireOnUpdate = true;
        }
        
        if (me.getID() == MouseEvent.MOUSE_MOVED)
        {
            mouseX = me.getX();// Center the object on the mouse
        }
    }

    @Override
    public void update()
    {
        GameEventDispatcher dispatcher;
        final long fireRate = 500000000l;
        if ( fireOnUpdate )
        {
            if ( System.nanoTime() - fireTimer > fireRate )
            {
                dispatcher = GameEventDispatcher.getGameEventDispatcher();
                MissleSprite ms = new MissleSprite( this, new Point( ( int ) ship.getCenterX(), ( int ) ship.getCenterY() ),
                        -10 );
                dispatcher.dispatchEvent( new GameEvent( this, GameEventType.AddBack, ms ) );
                fireTimer = System.nanoTime();
            }
            fireOnUpdate = false;
        }



        // If in boundingBox
        if ( mouseX >= boundingBox.x && mouseX <= boundingBox.x + boundingBox.width - ship.width )
        {
            // ship.x -= ship.width / 2;

            // Cursor is right of mouse
            if ( ship.x < mouseX && ship.x + maxSpeed < mouseX )
            {
                ship.x += maxSpeed;
            }
            // Cursor is left of mouse
            else if ( ship.x > mouseX && ship.x - maxSpeed > mouseX )
            {
                ship.x -= maxSpeed;
            }
            else
            {
                ship.x = mouseX;
            }
        }
        // If left of boundingBox
        else if ( boundingBox.x > mouseX )
        {
            // Cursor is left of mouse
            if ( ship.x - maxSpeed > boundingBox.x )
            {
                ship.x -= maxSpeed;
            }
            else
            {
                ship.x = boundingBox.x;
            }

        }
        // If right of boundingBox
        else if ( mouseX > boundingBox.x + boundingBox.width - ship.width )
        {
            // Cursor is right of mouse
            if ( ship.x + maxSpeed < boundingBox.x + boundingBox.width - ship.width )
            {
                ship.x += maxSpeed;
            }
            else
            {
                ship.x = boundingBox.x + boundingBox.width - ship.width;
            }
        }


    }

}
