package project4;

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

public class ScoreSprite implements Sprite, GameEventListener
{
    private GameEventDispatcher dispatcher;
    private volatile int score;

    public ScoreSprite( )
    {
        score = 0;
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
        g2d.drawString( "Score: " + score, 16, 16 );

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
        if ( ge.getType() == GameEventType.Score )
        {
            score += ( ( Integer ) ge.getAttachment() ).intValue();
        }

    }

    public void reset()
    {
        score = 0;
    }

    public int getScore()
    {
        return score;
    }
}
