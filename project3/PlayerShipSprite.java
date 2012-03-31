package project3;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.io.IOException;

import libs.GameEvent;
import libs.GameEvent.GameEventType;
import libs.GameEventDispatcher;
import libs.ImageUtil;
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
    final int shipWidth = 120;
    private boolean autoFIRE;
    private Image shipImage;
    private Image shipDeadImage;
    private int dead;

    public PlayerShipSprite( Rectangle boundingBox )
    {
        this.boundingBox = boundingBox;

        reset();
        fireTimer = System.nanoTime();
        dispatcher = GameEventDispatcher.getGameEventDispatcher();

        try
        {
            shipImage = ImageUtil.createImage( this, "ship.png" );
            shipDeadImage = ImageUtil.createImage( this, "ship.png" );
        }
        catch ( IOException e )
        {
            System.out.println( "Failed to load ship sprites." );
        }
        
        System.out.print( "PlayerShipSprite spawn @(" + boundingBox + ")\n" );
    }

    public void reset()
    {
        dead = 0;
        mouseX = boundingBox.x + ( boundingBox.width - shipWidth ) / 2;
        ship = null;
        ship = new Rectangle( mouseX, boundingBox.y, shipWidth, boundingBox.height );
        fireOnUpdate = false;
        autoFIRE = false;
    }

    @Override
    public void checkCollision( Sprite obj )
    {
        if ( dead > 0 )
            return;

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
                dispatcher.dispatchEvent( new GameEvent( this, GameEventType.Life, new Integer( -1 ) ) );
                dead = 30;
            }
        }

    }

    @Override
    public void draw( Graphics g )
    {
        // g.setColor( myColor );
        // g.drawRect( boundingBox.x, boundingBox.y, boundingBox.width,
        // boundingBox.height );

        if ( dead > 0 )
        {
            g.drawImage( shipDeadImage, ship.x, ship.y, null );
            g.fillRect( ship.x, ship.y, ship.width, ship.height );
        }
        else
        {
            g.drawImage( shipImage, ship.x, ship.y, null );
        }
        // g.fillRect( ship.x, ship.y, ship.width, ship.height );

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

        if ( me.getID() == MouseEvent.MOUSE_PRESSED )
        {
            autoFIRE = true;
        }
        if ( me.getID() == MouseEvent.MOUSE_RELEASED )
        {
            autoFIRE = false;
        }


        if ( me.getID() == MouseEvent.MOUSE_MOVED || me.getID() == MouseEvent.MOUSE_DRAGGED )
        {
            mouseX = me.getX();// Center the object on the mouse
        }

        if ( me.getID() == MouseEvent.MOUSE_CLICKED )
        {
            fireOnUpdate = true;
        }

    }

    @Override
    public void update()
    {
        if ( dead > 0 )
        {
            dead--;
            return;
        }

        GameEventDispatcher dispatcher;
        final long fireRate = 500000000l;
        if ( fireOnUpdate || autoFIRE )
        {
            if ( System.nanoTime() - fireTimer > fireRate )
            {
                dispatcher = GameEventDispatcher.getGameEventDispatcher();
                MissleSprite ms = new MissleSprite( this, new Point( ( int ) ship.getCenterX(), ( int ) ship.getMinY() ),
                        -10 );
                dispatcher.dispatchEvent( new GameEvent( this, GameEventType.AddFront, ms ) );
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
