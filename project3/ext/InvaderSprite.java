package project3.ext;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;

import libs.GameEvent;
import libs.GameEvent.GameEventType;
import libs.GameEventDispatcher;
import libs.Sprite;

public class InvaderSprite implements Sprite
{
    Rectangle boundingBox;
    private int row;
    private int col;
    private float padding;
//    private Color color;
    private int dead;// The remaining number of frames
    private GameEventDispatcher dispatcher;
    private long fireTimer;
    private MissileSprite myMissle;
    private InvaderSheet invaderSheet;
    private BufferedImage bufferedFrame;


    public InvaderSprite( int col, int row, Dimension widthheight, InvaderSheet t )
    {
        padding = 2.2f;
//        color = Color.WHITE;
        dead = -1;
        dispatcher = GameEventDispatcher.getGameEventDispatcher();
        this.row = row;
        this.col = col;
        boundingBox = new Rectangle( widthheight );

        invaderSheet = t;

    }

    @Override
    public void checkCollision( Sprite obj )
    {
        // This signals the death animation, collisions should not occur on dead
        // invaders
        if ( dead > 0 )
            return;

        if(obj instanceof MissileSprite){
            if ( ( ( MissileSprite ) obj ).getParent() instanceof InvaderSprite )
                return;

            // Rectangle rectangle = new Rectangle(drawPoint.x, drawPoint.y,
            // widthheight.width, widthheight.height);
            Rectangle result = obj.intersects( boundingBox );
            if(result != null){
                // System.out.println("InvaderSprite hit by MissleSprite");
                // remove the missile
                dispatcher.dispatchEvent( new GameEvent( this, GameEventType.Remove, obj ) );
                dispatcher.dispatchEvent( new GameEvent( this, GameEventType.Score, ( Integer ) 10 ) );
                // Notify FleetSprite of death in 5 updates, after the death
                // animation has animated
                dead = 5;
            }
        }

        // If a crash
        if ( obj instanceof BaseSprite || obj instanceof PlayerShipSprite )
        {
            Rectangle result = obj.intersects( boundingBox );
            if ( result != null )
            {
                // dispatcher.dispatchEvent( new GameEvent( this,
                // GameEventType.End, new Integer( 1 ) ) );
                dispatcher.dispatchEvent( new GameEvent( this, GameEventType.End, null ) );
            }
        }
    }

    @Override
    public void draw( Graphics g )
    {
//        g.setColor(color);
//        g.fillRect( boundingBox.x, boundingBox.y, boundingBox.width, boundingBox.height );
        g.drawImage( bufferedFrame, boundingBox.x, boundingBox.y, null );
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
        final long fireRate = ( long ) 2500;// 2.5 Seconds, in milliseconds
        if ( System.currentTimeMillis() - fireTimer > fireRate )
        {
            if ( Math.random() > .6f )
            {
                myMissle = new MissileSprite( this, new Point( ( int ) boundingBox.getCenterX(),
                        ( int ) boundingBox.getCenterY() ),
                        10 );
                dispatcher.dispatchEvent( new GameEvent( this, GameEventType.AddBack, myMissle ) );
            }
            fireTimer = System.currentTimeMillis();
        }

        int frame;
        if ( dead != -1 )
        {
            dead--;
            frame = invaderSheet.EXPLOSION_FRAME + ( invaderSheet.ROW_LENGTH * ( row % 3 ) );
        }
        else
        {
            frame = ( boundingBox.x / 64 % 2 ) + ( invaderSheet.ROW_LENGTH * ( row % 3 ) );
        }
        bufferedFrame = invaderSheet.getTile( frame );
    }

    public void setFleetPoint( Point invaderFleet )
    {
        
//        this.invaderFleet = invaderFleet;
        boundingBox.x = ( int ) ( invaderFleet.x + col * boundingBox.width * padding );
        boundingBox.y = ( int ) ( invaderFleet.y + row * boundingBox.height * padding );
    }
    
    public Rectangle getBox()
    {
        return ( Rectangle ) boundingBox.clone();
    }

    public boolean isDead()
    {
        return dead == 0;
    }

}
