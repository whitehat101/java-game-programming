package project2;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;

import libs.GameEvent;
import libs.GameEvent.GameEventType;
import libs.GameEventDispatcher;
import libs.Sprite;

public class BaseSprite implements Sprite
{
    Color myColor = Color.WHITE;
    Rectangle boundingBox;// I prefer integer math for games like this. Who
                          // cares about fractional pixels?
    int health;
    int blink;
    private GameEventDispatcher dispatcher;

    /*
     * The for the bases the bounding box will describe the width and height of
     * the bases. There will be gaps in the bases, but the boundingBox contains
     * all of the bases
     */
    public BaseSprite( Rectangle boundingBox )
    {
        this.boundingBox = boundingBox;
        health = 10;
        blink = 0;
        dispatcher = GameEventDispatcher.getGameEventDispatcher();
        System.out.print( "BaseSprite spawn @(" + boundingBox.toString() + ")\n" );
    }

    @Override
    public void checkCollision( Sprite obj )
    {
        if ( obj instanceof MissleSprite )
        {
            Rectangle result = obj.intersects( boundingBox );
            if ( result != null )
            {
                // System.out.println( "Base hit by MissleSprite" );
                // color = Color.YELLOW;

                health--;
                // remove the missile
                dispatcher.dispatchEvent( new GameEvent( this, GameEventType.Remove, obj ) );
            }
        }
    }

    @Override
    public void draw( Graphics g )
    {
        g.setColor( myColor );// Per specification, this is Color.WHITE
        // g.drawRect( boundingBox.x, boundingBox.y, boundingBox.width,
        // boundingBox.height );
        g.fillRect( boundingBox.x, boundingBox.y, boundingBox.width, boundingBox.height );
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
        int RGBColor = 255 * health / 10;
        myColor = new Color( RGBColor, RGBColor, RGBColor );// All these new
                                                            // colors... bad?
    }

}
