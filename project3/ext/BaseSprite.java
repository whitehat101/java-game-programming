package project3.ext;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.io.IOException;

import libs.GameEvent;
import libs.GameEvent.GameEventType;
import libs.GameEventDispatcher;
import libs.ImageUtil;
import libs.Sprite;

public class BaseSprite implements Sprite
{
    private Rectangle boundingBox;
    private int health;
    private GameEventDispatcher dispatcher;
    private Image baseImage;

    /*
     * The for the bases the bounding box will describe the width and height of
     * the bases. There will be gaps in the bases, but the boundingBox contains
     * all of the bases
     */
    public BaseSprite( Rectangle boundingBox )
    {
        this.boundingBox = boundingBox;
        health = 8;
        dispatcher = GameEventDispatcher.getGameEventDispatcher();
        try
        {
            baseImage = ImageUtil.createImage( this, "base.png" );
        }
        catch ( IOException e )
        {
            e.printStackTrace();
        }

        System.out.print( "BaseSprite spawn @(" + boundingBox.toString() + ")\n" );
    }

    @Override
    public void checkCollision( Sprite obj )
    {
        if ( obj instanceof MissileSprite )
        {
            Rectangle result = obj.intersects( boundingBox );
            if ( result != null )
            {
                health--;
                // remove the missile
                dispatcher.dispatchEvent( new GameEvent( this, GameEventType.Remove, obj ) );
            }
        }
    }

    @Override
    public void draw( Graphics g )
    {
        g.drawImage( baseImage, boundingBox.x, boundingBox.y, null );
        // g.setColor( Color.YELLOW );
        // g.drawRect( boundingBox.x, boundingBox.y, boundingBox.width,
        // boundingBox.height );
        // g.fillRect( boundingBox.x, boundingBox.y, boundingBox.width,
        // boundingBox.height );

        Graphics2D g2d = ( Graphics2D ) g;
        g2d.setColor( Color.RED );
        g2d.setFont( new Font( "Tahoma", Font.BOLD, 20 ) );
        g2d.drawString( "" + health, ( int ) boundingBox.getCenterX() - 10, ( int ) boundingBox.getCenterY() );

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
    }

    @Override
    public void update()
    {
        if ( health <= 0 )
        {
            health = 0;
            dispatcher.dispatchEvent( new GameEvent( this, GameEventType.Remove, this ) );
        }

        // I'm a base! Raa! I stay here unless something hits me.
        // int RGBColor = 255 * health / 10;
        // myColor = new Color( RGBColor, RGBColor, RGBColor );
    }

}
