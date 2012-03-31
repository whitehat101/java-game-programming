package project3.ext;

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

public class MissileSprite implements Sprite
{
    final private Color myColor = Color.WHITE;
    private int velocity;
    private Rectangle missile;
    private Sprite parent;
    private GameEventDispatcher dispatcher;


    public MissileSprite( Sprite parent, Point MissileSpawn, int velocity )
    {
        this.parent = parent;
        this.velocity = velocity;
        
        final int missileWidth = 2;
        final int missileHeight = 42 / 2;// Douglas Adams
        missile = new Rectangle( MissileSpawn.x - missileWidth / 2, MissileSpawn.y, missileWidth, missileHeight );
        dispatcher = GameEventDispatcher.getGameEventDispatcher();
        
        // System.out.print( "MissileSprite spawn @(" + MissileSpawn.toString()
        // + ")\n" );
    }

    @Override
    public void checkCollision( Sprite obj )
    {
        if ( obj instanceof MissileSprite )
        {
            Rectangle result = obj.intersects( missile );
            if ( result != null )
            {
                // Rare missile-missile collision
                dispatcher.dispatchEvent( new GameEvent( this, GameEventType.Score, ( Integer ) 1 ) );
                dispatcher.dispatchEvent( new GameEvent( this, GameEventType.Remove, obj ) );
                dispatcher.dispatchEvent( new GameEvent( this, GameEventType.Remove, this ) );
            }
        }

    }

    @Override
    public void draw( Graphics g )
    {
        g.setColor( myColor );
        g.fillRect( missile.x, missile.y, missile.width, missile.height );
    }

    @Override
    public Rectangle intersects( Rectangle boundingBox )
    {
        return boundingBox.intersects( missile ) ? boundingBox.intersection( missile ) : null;
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
        missile.y += velocity;
        if ( missile.y < 0 || missile.y > 600 )
        {
            dispatcher.dispatchEvent( new GameEvent( this, GameEventType.Remove, this ) );
            //System.out.println("MissleSprite death");
        }
    }

    public Sprite getParent()
    {
        return parent;
    }

}
