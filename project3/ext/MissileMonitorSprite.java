package project3.ext;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;

import libs.GameEvent;
import libs.GameEvent.GameEventType;
import libs.GameEventDispatcher;
import libs.GameEventListener;
import libs.Sprite;

public class MissileMonitorSprite implements Sprite, GameEventListener
{
    private GameEventDispatcher dispatcher;
    private volatile long shotsFired;
    private volatile long shotsHit;

    public MissileMonitorSprite( )
    {
        shotsFired = 0;
        shotsHit = 0;
        dispatcher = GameEventDispatcher.getGameEventDispatcher();
        dispatcher.addGameEventListener( this );
    }

    @Override
    public void draw( Graphics g )
    {
        Graphics2D g2d = ( Graphics2D ) g;
        g2d.setColor( Color.RED );
        g2d.setFont( new Font( "Tahoma", Font.PLAIN, 20 ) );
        g2d.drawString( "Shots: " + shotsFired, 16, 64 );
        g2d.drawString( "Hits: " + shotsHit, 16, 88 );
        g2d.drawString( "Accuracy: " + ( ( float ) 100 * shotsHit / shotsFired ) + "%", 16, 112 );
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
    }

    @Override
    public void gameEvent( GameEvent ge )
    {
        if ( ge.getSource() instanceof PlayerShipSprite && ge.getType() == GameEventType.AddFront
                && ge.getAttachment() instanceof MissileSprite )
        {
            shotsFired++;
        }
        else if ( ge.getSource() instanceof InvaderSprite && ge.getType() == GameEventType.Remove
                && ge.getAttachment() instanceof MissileSprite )
        {
            shotsHit++;
        }
        else if ( ge.getSource() instanceof MissileSprite && ge.getAttachment() instanceof MissileSprite
                && !ge.getSource().equals( ge.getAttachment() ) )
        {
            shotsHit++;
        }

    }

    @Override
    public void checkCollision( Sprite obj )
    {
    }

}
