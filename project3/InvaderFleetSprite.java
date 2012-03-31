package project3;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.util.LinkedList;

import libs.GameEvent;
import libs.GameEvent.GameEventType;
import libs.GameEventDispatcher;
import libs.Sprite;

public class InvaderFleetSprite implements Sprite
{
    private Rectangle boundingBox;// Where the invaders may move
    private Point controlPoint;// The relative point the invaders draw from
    private Rectangle fleetBox;// The collision box for invaders
    private LinkedList< InvaderSprite > invaders;
    
    private int fleetXVelocity;
    final private int fleetXspeed = 5;

    private GameEventDispatcher dispatcher;

    /* The for the bases the bounding box will describe the width and height
     * of the bases. There will be gaps in the bases, but the boundingBox 
     * contains all of the bases
     */
    public InvaderFleetSprite( Rectangle boundingBox, int invaderCount )
    {
        this.boundingBox = boundingBox;
        fleetBox = new Rectangle( boundingBox.getLocation() );
        controlPoint = boundingBox.getLocation();
        
        fleetXVelocity = fleetXspeed;
        dispatcher = GameEventDispatcher.getGameEventDispatcher();
        
        invaders = new LinkedList<InvaderSprite>();
        
        InvaderSheet invaderSheet;
        final int colCount = 5;
        try
        {
            invaderSheet = new InvaderSheet( this, "invaders.png", 32, 32 );

            for ( int invaderNumber = 0; invaderNumber < invaderCount; invaderNumber++ )
            {
                InvaderSprite sprite = new InvaderSprite( invaderNumber % colCount, invaderNumber / colCount, new Dimension(
                        32, 32 ), invaderSheet );
                invaders.addFirst( sprite );
            }
        }
        catch ( IOException e )
        {
            e.printStackTrace();
        }

        fleetBox.width = (colCount-1)*60 + 50;
        fleetBox.height = invaderCount/5*60 - 10;
        
        System.out.print( "InvaderSprite spawn @(" + boundingBox.toString() + ")\n" );
    }

    @Override
    public void checkCollision( Sprite obj )
    {
        if ( obj instanceof MissleSprite || obj instanceof BaseSprite || obj instanceof PlayerShipSprite )
        {
            Rectangle result = obj.intersects( fleetBox );
            if(result != null){
                // System.out.println("InvaderFleetSprite hit by MissleSprite, passing on to InvaderSprites");
                for(InvaderSprite invaderSprite : invaders){
                    invaderSprite.checkCollision( obj );
                }
                //Pass on to invaders...
                //spriteDelList.add( obj );
            }
        }
    }

    @Override
    public void draw( Graphics g )
    {
        // g.setColor( Color.WHITE );
        // g.drawRect( boundingBox.x, boundingBox.y, boundingBox.width,
        // boundingBox.height );

        // Draw invaders
        for ( InvaderSprite invader : invaders )
        {
            invader.draw( g );
        }

        // g.setColor( Color.RED );
        // g.drawRect( fleetBox.x, fleetBox.y, fleetBox.width, fleetBox.height
        // );
        //
        // g.setColor( Color.BLUE );
        // g.fillOval( controlPoint.x, controlPoint.y, 5, 5 );

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
        // fleetBox.x = 0;
        // fleetBox.y = 0;
        // fleetBox.width = 0;
        // fleetBox.height = 0;

        int fleetMinX = 1000, fleetMaxX = 0;
        int fleetMinY = 1000, fleetMaxY = 0;
        Rectangle tmp;

        // Point invaderFleet = fleetBox.getLocation();
        InvaderSprite invaderToRemove = null;

        // System.out.println();System.out.println();
        for(InvaderSprite invader : invaders){
            // Check for dead 'vaders
            if ( invader.isDead() )
            {
                invaderToRemove = invader;// To not violate concurrentness... I
                                          // should just step through the
                                          // list... it *IS* a linked list.
            }
            //Rebuild the fleet box
            else
            {               
                tmp = invader.getBox();
                if (fleetMinX > tmp.x)fleetMinX = tmp.x;
                if (fleetMinY > tmp.y)fleetMinY = tmp.y;
                
                if (fleetMaxX < tmp.x + tmp.width)fleetMaxX = tmp.x + tmp.width;
                if (fleetMaxY < tmp.y + tmp.height)fleetMaxY =  tmp.y + tmp.height;
            }
        }
        fleetBox.x = fleetMinX;
        fleetBox.y = fleetMinY;
        fleetBox.width = fleetMaxX - fleetMinX;
        fleetBox.height = fleetMaxY - fleetMinY;
        // System.out.println( "\n" + fleetBox.toString() );

        
        
        if ( invaderToRemove != null )
        {
            invaders.remove( invaderToRemove );

            // Remove Globally/Score?
            // dispatcher.dispatchEvent( new GameEvent( this,
            // GameEventType.Remove, invaderToRemove ) );
        }

        // Player Wins
        if ( invaders.isEmpty() )
        {
            dispatcher.dispatchEvent( new GameEvent( this, GameEventType.LevelComplete, null ) );
        }

        // Fleet Movement
        fleetBox.x += fleetXVelocity;
        controlPoint.x += fleetXVelocity;

        if ( fleetBox.x + fleetBox.width > boundingBox.x + boundingBox.width || fleetBox.x < boundingBox.x )
        {
            if ( fleetBox.x < boundingBox.x )
            {
                fleetBox.x = boundingBox.x;
            }
            if ( fleetBox.x > boundingBox.x + boundingBox.width - fleetBox.width )
            {
                fleetBox.x = boundingBox.x + boundingBox.width - fleetBox.width;
            }

            // TODO: This code might be firing twice at the very beginning
            // System.out.println( "reversing direction\n" + fleetBox.toString() );

            fleetXVelocity *= -1;
            // fleetBox.y += 20;
            controlPoint.y += 10;

        }
        if ( controlPoint.y > boundingBox.y + boundingBox.height )
        {
            // out of bounds, the invaders win
            System.out.println( "You Lose." );
            fleetBox.y = boundingBox.y + boundingBox.height - fleetBox.height;
            fleetXVelocity = 0;
            dispatcher.dispatchEvent( new GameEvent( this, GameEventType.Start, new Integer( 1 ) ) );
        }


        // Update their positions
        for ( InvaderSprite invader : invaders )
        {
            invader.setFleetPoint( controlPoint );// invaderFleet
            invader.update();
        }

    }
}
