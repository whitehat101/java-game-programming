package project4;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.io.IOException;

import libs.GameEvent;
import libs.GameEvent.GameEventType;
import libs.GameEventDispatcher;
import libs.ImageUtil;
import libs.Sprite;

public class MissileSprite implements Sprite
{
    final Rectangle boundingBox;// We should not change this
    private Rectangle missile;
    final private Color myColor = Color.WHITE;
    private int velocity;
    private Sprite parent;
    private GameEventDispatcher dispatcher;
    static private Image missileImage;

    public MissileSprite( Sprite parent, Point MissileSpawn, Rectangle boundingBox, int velocity )
    {
        this.boundingBox = boundingBox;
        this.parent = parent;
        this.velocity = velocity;
        
        //TODO: draw a arrow/cross shaped missile
        final int missileWidth = 10;
        final int missileHeight = 21;// Douglas Adams
        missile = new Rectangle( MissileSpawn.x - missileWidth / 2, MissileSpawn.y, missileWidth, missileHeight );
        dispatcher = GameEventDispatcher.getGameEventDispatcher();
        

        if ( missileImage == null )
        {
            try
            {
                missileImage = ImageUtil.createImage( this, "path2999.png" );
                // shipDeadImage = ImageUtil.createImage( this, "ship.png" );
            }
            catch ( IOException e )
            {
                System.out.println( "Failed to load ship sprites." );
            }
        }

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
                dispatcher.dispatchEvent( new GameEvent( this, GameEventType.Score, ( Integer ) 15 ) );
                dispatcher.dispatchEvent( new GameEvent( this, GameEventType.Remove, obj ) );
                dispatcher.dispatchEvent( new GameEvent( this, GameEventType.Remove, this ) );
            }
        }

    }

    @Override
    public void draw( Graphics g )
    {
        g.setColor( myColor );
        // g.fillRect( missile.x, missile.y, missile.width, missile.height );
        g.drawImage( missileImage, missile.x, missile.y, null );
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
