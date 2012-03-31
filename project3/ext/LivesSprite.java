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

public class LivesSprite implements Sprite, GameEventListener
{
    private GameEventDispatcher dispatcher;
    private volatile int lives;

    public LivesSprite( )
    {
        lives = 3;
        dispatcher = GameEventDispatcher.getGameEventDispatcher();
        dispatcher.addGameEventListener( this );
    }

    @Override
    public void checkCollision( Sprite obj )
    {
    }

    @Override
    public void draw( Graphics g )
    {
        Graphics2D g2d = ( Graphics2D ) g;
        g2d.setColor( Color.RED );
        g2d.setFont( new Font( "Tahoma", Font.BOLD, 20 ) );
        g2d.drawString( "lives: " + lives, 16, 36 );

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
        if ( ge.getType() == GameEventType.Life )
        {
            lives--;
            if ( lives < 0 )
            {
                // dispatcher.dispatchEvent( new GameEvent( this,
                // GameEventType.End, 0 ) );
                dispatcher.dispatchEvent( new GameEvent( this, GameEventType.End, null ) );
            }
        }
    }

    public void setLives( int l )
    {
        lives = l;
    }
}
