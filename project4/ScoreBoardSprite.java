package project4;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.font.FontRenderContext;
import java.awt.font.LineMetrics;
import java.io.IOException;
import java.util.Collections;
import java.util.LinkedList;
import java.util.prefs.BackingStoreException;
import java.util.prefs.Preferences;

import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;

import libs.AudioSample;
import libs.AudioSample.AudioState;
import libs.GameEvent;
import libs.GameEvent.GameEventType;
import libs.GameEventDispatcher;
import libs.Sprite;

public class ScoreBoardSprite implements Sprite
{
    // private String name;
    LinkedList< HighScore > highScoreList;
    ScoreInputSprite popup;
    Preferences prefs;
    HighScore latestHighScore;
    private AudioSample highScoreSample;

    public ScoreBoardSprite( )
    {
        highScoreList = new LinkedList< HighScore >();

        prefs = Preferences.systemNodeForPackage( this.getClass() );

        HighScore hs;
        for ( int index = 1; index <= 10; index++ )
        {
            int level, score;
            String date, name;

            level = prefs.getInt( "SCORE" + index + "_LEVEL", -1 );
            score = prefs.getInt( "SCORE" + index + "_SCORE", -1 );
            if ( score == -1 || level == -1 )
                break;

            name = prefs.get( "SCORE" + index + "_NAME", new String() );
            date = prefs.get( "SCORE" + index + "_DATE", new String() );

            hs = new HighScore( level, score, date, name );
            highScoreList.add( hs );
        }

    }

    public void newScore( int level, int score )
    {
        // Create the InputPopup
        popup = new ScoreInputSprite( new HighScore( level, score ) );
    }

    @Override
    public void draw( Graphics g )
    {
        Graphics2D g2d = ( Graphics2D ) g;

        g2d.setColor( Color.RED );
        Font f = new Font( "Tahoma", Font.BOLD, 20 );
        g2d.setFont( f );

        LineMetrics metrics = f.getLineMetrics( "Metrics", new FontRenderContext( null, true, false ) );
        float lineHeight = metrics.getHeight();

        Rectangle scoreBox = new Rectangle( 50, 50, 700, 500 );

        g2d.setColor( Color.GRAY );
        g2d.fill( scoreBox );

        int lineCounter = 1;
        Point drawPoint = new Point();

        // Header
        g2d.setColor( Color.BLACK );
        drawPoint.y = ( int ) ( scoreBox.y + lineCounter * lineHeight );

        drawPoint.x = scoreBox.x + 1;
        g2d.drawString( "#", drawPoint.x, drawPoint.y );

        drawPoint.x += 25;
        g2d.drawString( "Score", drawPoint.x, drawPoint.y );

        drawPoint.x += 70;
        g2d.drawString( "Level", drawPoint.x, drawPoint.y );

        drawPoint.x += 70;
        g2d.drawString( "Name", drawPoint.x, drawPoint.y );

        drawPoint.x += 220;
        g2d.drawString( "Date", drawPoint.x, drawPoint.y );

        // Scores
        g2d.setFont( new Font( "Tahoma", Font.PLAIN, 20 ) );
        g2d.setColor( Color.WHITE );
        for ( HighScore hs : highScoreList )
        {
            lineCounter++;
            g2d.setColor( Color.WHITE );
            if ( hs.equals( latestHighScore ) )
                g2d.setColor( Color.RED );
            
            drawPoint.y = ( int ) ( scoreBox.y + lineCounter * lineHeight );

            drawPoint.x = scoreBox.x + 5;
            g2d.drawString( "" + ( lineCounter - 1 ), drawPoint.x, drawPoint.y );

            drawPoint.x += 25;
            g2d.drawString( "" + hs.score, drawPoint.x, drawPoint.y );

            drawPoint.x += 70;
            g2d.drawString( "" + hs.level, drawPoint.x, drawPoint.y );

            drawPoint.x += 70;
            g2d.drawString( hs.name, drawPoint.x, drawPoint.y );

            drawPoint.x += 220;
            g2d.drawString( hs.date.toString(), drawPoint.x, drawPoint.y );
        }

        // System.out.print( "printed " + lineCounter + " lines\n" );

        if ( popup != null )
            popup.draw( g );
    }

    @Override
    public void keyboardAction( KeyEvent ke )
    {
        if ( popup != null )
        {
            popup.keyboardAction( ke );
        }
        else
        {
            GameEvent ge = null;
            switch ( ke.getKeyCode() )
            {
                case KeyEvent.VK_Q:
                case KeyEvent.VK_ESCAPE:
                    ge = new GameEvent( this, GameEventType.Quit, null );
                    break;
                case KeyEvent.VK_S:
                    ge = new GameEvent( this, GameEventType.Start, null );
                    break;
            }
            if ( ge != null )
                GameEventDispatcher.getGameEventDispatcher().dispatchEvent( ge );
        }

    }

    @Override
    public void update()

    {
        if ( popup != null && popup.isDone() )
        {
            HighScore newScore = popup.getHighScore();
            popup = null;// Delete popup

            highScoreList.addFirst( newScore );
            latestHighScore = newScore;
            Collections.sort( highScoreList );


            // Update System Highscore
            int highScoreCountLimit = 10;
            if ( highScoreList.size() < 10 )
            {
                highScoreCountLimit = highScoreList.size();
            }

            for ( int index = 0; index < highScoreCountLimit; index++ )
            {
                prefs.putInt( "SCORE" + ( index + 1 ) + "_SCORE", highScoreList.get( index ).score );
                prefs.put( "SCORE" + ( index + 1 ) + "_NAME", highScoreList.get( index ).name );
                prefs.putInt( "SCORE" + ( index + 1 ) + "_LEVEL", highScoreList.get( index ).level );
                prefs.put( "SCORE" + ( index + 1 ) + "_DATE", highScoreList.get( index ).date.toString() );
                System.out.println( "Saving: SCORE" + ( index + 1 ) );
            }

            try
            {
                prefs.flush();
            }
            catch ( BackingStoreException e )
            {
                e.printStackTrace();
            }
            if ( highScoreSample == null )
            {
                try
                {
                    highScoreSample = new AudioSample( this, "sounds/can you believe it.wav" );
                }
                catch ( IOException e )
                {
                    e.printStackTrace();
                }
                catch ( UnsupportedAudioFileException e )
                {
                    e.printStackTrace();
                }
                catch ( LineUnavailableException e )
                {
                    e.printStackTrace();
                }
            }
            if ( highScoreSample.getState() == AudioState.DONE )
            {
                System.out.println( "sounds/hey stupid.wav: " + highScoreSample.getTime() );
                highScoreSample.play();
            }

        }

    }

    @Override
    public void mouseAction( MouseEvent me )
    {
        // TODO Auto-generated method stub

    }

    // Unused:
    @Override
    public Rectangle intersects( Rectangle boundingBox )
    {
        return null;
    }

    @Override
    public void checkCollision( Sprite obj )
    {
    }

}
