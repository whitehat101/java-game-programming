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

public class MissleSprite implements Sprite
{
    final Color myColor = Color.WHITE;
    protected int velocity;
    Rectangle missle;
    Sprite parent;
    private GameEventDispatcher dispatcher;


    public MissleSprite( Sprite parent, Point MissleSpawn, int velocity )
    {
        this.parent = parent;
        this.velocity = velocity;
        
        int missleWidth = 8;
        int missleHeight = 42;//Douglas Adams
        missle = new Rectangle(MissleSpawn.x - missleWidth/2 ,MissleSpawn.y, missleWidth, missleHeight);
        dispatcher = GameEventDispatcher.getGameEventDispatcher();
        
        // System.out.print( "MissleSprite spawn @(" + MissleSpawn.toString() +
        // ")\n" );
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
        g.fillRect( missle.x, missle.y, missle.width, missle.height );
    }

    @Override
    public Rectangle intersects( Rectangle boundingBox )
    {
        return boundingBox.intersects( missle ) ? boundingBox.intersection( missle ) : null;
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
        missle.y += velocity;
        if ( missle.y < 0 || missle.y > 600 )
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
