package project1;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.util.LinkedList;

import libs.Sprite;

public class InvaderFleetSprite implements Sprite
{
    final Color myColor = Color.RED;
    LinkedList<Sprite> spriteAddList, spriteDelList;

    Rectangle boundingBox;
    Rectangle fleetBox;
    LinkedList<InvaderSprite> invaders;
    
    int fleetXVelocity;

    /* The for the bases the bounding box will describe the width and height
     * of the bases. There will be gaps in the bases, but the boundingBox 
     * contains all of the bases
     */
    public InvaderFleetSprite(Rectangle boundingBox, int invaderCount, LinkedList<Sprite> spriteAddList,LinkedList<Sprite> spriteDelList)
    {
        this.boundingBox = boundingBox;
        this.spriteAddList = spriteAddList;
        this.spriteDelList = spriteDelList;
        fleetBox = new Rectangle( boundingBox.getLocation() );
        fleetXVelocity = 5;

        
        invaders = new LinkedList<InvaderSprite>();
        
        final int colCount = 5;
        for(int invaderNumber = 0; invaderNumber < invaderCount; invaderNumber++)
        {
            //new Point(invaderNumber%colCount*60, invaderNumber/colCount*60)
            InvaderSprite sprite = new InvaderSprite(invaderNumber%colCount, invaderNumber/colCount, new Dimension( 50, 50), spriteDelList);
            invaders.addFirst(sprite);
        }
        fleetBox.width = (colCount-1)*60 + 50;
        fleetBox.height = invaderCount/5*60 - 10;
        
        System.out.print( "InvaderSprite spawn @(" + boundingBox.toString() + ")\n" );
    }

    @Override
    public void checkCollision( Sprite obj )
    {
        if(obj instanceof MissleSprite){
            Rectangle result = obj.intersects( fleetBox );
            if(result != null){
                System.out.println("InvaderFleetSprite hit by MissleSprite, passing on to InvaderSprites");
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
        g.setColor( myColor );//Per specification, this is Color.WHITE
        g.drawRect( boundingBox.x, boundingBox.y, boundingBox.width, boundingBox.height );
        g.drawRect( fleetBox.x, fleetBox.y, fleetBox.width, fleetBox.height );
        for(InvaderSprite invader : invaders){
            invader.draw(g);
        }
    }

    @Override
    public Rectangle intersects( Rectangle boundingBox )
    {
        // TODO Auto-generated method stub
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
        
        fleetBox.x += fleetXVelocity;

        if(fleetBox.x + fleetBox.width > boundingBox.x + boundingBox.width || fleetBox.x < boundingBox.x)
        {
            fleetXVelocity *= -1;
            fleetBox.y += 20;
        }
        if(fleetBox.y + fleetBox.height > boundingBox.y + boundingBox.height){
            //out of bounds, the invaders win
            System.out.println("You Lose.");
            fleetBox.y = boundingBox.y + boundingBox.height-fleetBox.height;
            fleetXVelocity = 0;
        }
        
        if(fleetBox.x < boundingBox.x)
        {
            fleetBox.x = boundingBox.x;
        }
        if(fleetBox.x + fleetBox.width > boundingBox.x + boundingBox.width)
        {
            fleetBox.x = boundingBox.x + boundingBox.width - fleetBox.width;
        }
        
        Point invaderFleet = new Point(fleetBox.x,fleetBox.y);
        for(InvaderSprite invader : invaders){
            invader.setFleetPoint(invaderFleet);
            //invader.update();
        }

    }

}
