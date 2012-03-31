package project1;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;

import libs.Sprite;

public class BaseSprite implements Sprite
{
    final Color myColor = Color.WHITE;

    Rectangle boundingBox;//I prefer integer math for games like this. Who cares about fractional pixels?
    Rectangle[] bases;

    /* The for the bases the bounding box will describe the width and height
     * of the bases. There will be gaps in the bases, but the boundingBox 
     * contains all of the bases
     */
    public BaseSprite(Rectangle boundingBox, int baseWidth, int baseCount )
    {
        this.boundingBox = boundingBox;
        //The number of "free" pixels in the base area
        int freeSpace = boundingBox.width - baseWidth * baseCount;
        //The amount of padding on each side
        int padding = freeSpace/(baseCount+1);
        
        bases = new Rectangle[baseCount];
        
        int offset = 0;
        for(int baseNumber = 0; baseNumber < baseCount; baseNumber++)
        {
            offset += padding;
            bases[baseNumber] = new Rectangle(offset,0,baseWidth,boundingBox.height);
            offset += baseWidth;
        }
        
        System.out.print( "BaseSprite spawn @(" + boundingBox.toString() + ")\n" );
    }

    @Override
    public void checkCollision( Sprite obj )
    {
        // TODO Auto-generated method stub

    }

    @Override
    public void draw( Graphics g )
    {
        g.setColor( myColor );//Per specification, this is Color.WHITE
        g.drawRect( boundingBox.x, boundingBox.y, boundingBox.width, boundingBox.height );
        for(Rectangle base : bases){
            g.fillRect( boundingBox.x + base.x, boundingBox.y + base.y, base.width, base.height );
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
        //I'm a base! Raa! I stay here unless something hits me.
    }

}
