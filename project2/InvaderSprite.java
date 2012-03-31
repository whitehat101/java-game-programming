package project2;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;

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
    private Color color;
    private boolean dead;
    private GameEventDispatcher dispatcher;
    private long fireTimer;
    private MissleSprite myMissle;


    public InvaderSprite( int row, int col, Dimension widthheight )
    {
        padding = 1.2f;
        color = Color.WHITE;
        dead = false;
        dispatcher = GameEventDispatcher.getGameEventDispatcher();
        this.row = row;
        this.col = col;
        boundingBox = new Rectangle( widthheight );
    }

    @Override
    public void checkCollision( Sprite obj )
    {
        if(obj instanceof MissleSprite){
            if ( ( ( MissleSprite ) obj ).getParent() instanceof InvaderSprite )
                return;

            // Rectangle rectangle = new Rectangle(drawPoint.x, drawPoint.y,
            // widthheight.width, widthheight.height);
            Rectangle result = obj.intersects( boundingBox );
            if(result != null){
                // System.out.println("InvaderSprite hit by MissleSprite");
                // remove the missile
                dispatcher.dispatchEvent( new GameEvent( this, GameEventType.Remove, obj ) );
                // Notify FleetSprite of death
                dead = true;
            }
        }
        if ( obj instanceof BaseSprite || obj instanceof PlayerShipSprite )
        {
            Rectangle result = obj.intersects( boundingBox );
            if ( result != null )
            {
                // dispatcher.dispatchEvent( new GameEvent( this,
                // GameEventType.End, new Integer( 1 ) ) );
                dispatcher.dispatchEvent( new GameEvent( this, GameEventType.Start, new Integer( 1 ) ) );
            }
        }
    }

    @Override
    public void draw( Graphics g )
    {
        g.setColor(color);
        g.fillRect( boundingBox.x, boundingBox.y, boundingBox.width, boundingBox.height );

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

    }

    @Override
    public void mouseAction( MouseEvent me )
    {
        // TODO Auto-generated method stub

    }

    @Override
    public void update()
    {
        final long fireRate = ( long ) 1E4;// 10 Seconds, in milliseconds
        if ( System.currentTimeMillis() - fireTimer > fireRate )
        {
            if ( Math.random() > .9f )
            {
                myMissle = new MissleSprite( this, new Point( ( int ) boundingBox.getCenterX(),
                        ( int ) boundingBox.getCenterY() ),
                        10 );
                dispatcher.dispatchEvent( new GameEvent( this, GameEventType.AddBack, myMissle ) );
            }
            fireTimer = System.currentTimeMillis();
        }

    }

    public void setFleetPoint( Point invaderFleet )
    {
        
//        this.invaderFleet = invaderFleet;
        boundingBox.x = ( int ) ( invaderFleet.x + row * boundingBox.width * padding );
        boundingBox.y = ( int ) ( invaderFleet.y + col * boundingBox.height * padding );
    }
    
    public Rectangle getBox()
    {
        return ( Rectangle ) boundingBox.clone();
    }

    public boolean isDead()
    {
        // TODO Auto-generated method stub
        return dead;
    }

}
