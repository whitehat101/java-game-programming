package project4;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.font.FontRenderContext;
import java.awt.font.LineMetrics;

import libs.Sprite;

public class ScoreInputSprite implements Sprite
{
    // private String name;
    private char [ ] name;
    private int nameIdx;

    final private HighScore highscore;

    private boolean ignoreNextKey;
    private Rectangle popup;

    private boolean doneWithInput;

    public ScoreInputSprite( HighScore hs )
    {
        this.highscore = hs;
        name = new char [ 12 ];
        nameIdx = 0;
        doneWithInput = false;

        // Create the InputPopup
        popup = new Rectangle( 200, 200, 420, 100 );

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
        popup.height = ( int ) ( 5.5 * lineHeight );

        g2d.setColor( Color.BLACK );
        g2d.fill( popup );

        g2d.setColor( Color.WHITE );
        g2d.drawString( "Score: " + highscore.score, popup.x, popup.y + 1 * lineHeight );
        g2d.drawString( "Level: " + highscore.level, popup.x, popup.y + 2 * lineHeight );
        g2d.drawString( "Name: " + new String( name, 0, nameIdx ), popup.x, popup.y + 3 * lineHeight );
        g2d.drawString( "Date: " + highscore.date.toString(), popup.x, popup.y + 4 * lineHeight );
    }

    @Override
    public void keyboardAction( KeyEvent ke )
    {
        if ( doneWithInput )
            return;

        if ( ignoreNextKey )
        {
            ignoreNextKey = false;
            return;
        }
        // System.out.print( "ID: " + ke.getID() + "\nkeychar: " +
        // ke.getKeyChar() + "\nkeycode: " + ke.getKeyCode() + "\n\n" );

        if ( ke.getID() == KeyEvent.KEY_PRESSED )
        {

            switch ( ke.getKeyCode() )
            {
                // case KeyEvent.VK_DELETE:
                case KeyEvent.VK_BACK_SPACE:
                    if ( nameIdx > 0 )
                    {
                        name[ --nameIdx ] = 0;
                    }
                    ignoreNextKey = true;
                    break;
                case KeyEvent.VK_ENTER:
                    doneWithInput = true;
                    highscore.name = new String( name, 0, nameIdx );
                    ignoreNextKey = true;
                    break;
            }
        }
        // Backspace doesn't get sent over KEY_TYPED \=
        else if ( ke.getID() == KeyEvent.KEY_TYPED )
        {
            if ( nameIdx < name.length )
            {
                name[ nameIdx++ ] = ke.getKeyChar();
            }
        }
        // System.out.print( "name(" + nameIdx + "): " );
        // System.out.println( name );

    }

    @Override
    public void mouseAction( MouseEvent me )
    {
        // TODO Auto-generated method stub

    }

    public boolean isDone()
    {
        return doneWithInput;
    }

    public HighScore getHighScore()
    {
        // TODO Auto-generated method stub
        return highscore;
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

    @Override
    public void update()
    {
    }
}
