package project1;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.util.LinkedList;

import libs.Sprite;

public class MissleSprite implements Sprite
{
    final Color myColor = Color.WHITE;
    protected int velocity;
    Rectangle missle;
    LinkedList<Sprite> spriteDelList;//To auto-delete if off screen


    public MissleSprite(Point MissleSpawn, int velocity, LinkedList<Sprite> spriteDelList)
    {
        this.velocity = velocity;
        this.spriteDelList = spriteDelList;
        
        int missleWidth = 8;
        int missleHeight = 42;//Douglas Adams
        missle = new Rectangle(MissleSpawn.x - missleWidth/2 ,MissleSpawn.y, missleWidth, missleHeight);
        
        System.out.print( "MissleSprite spawn @(" + MissleSpawn.toString() + ")\n" );
    }

    @Override
    public void checkCollision( Sprite obj )
    {
        // TODO Auto-generated method stub

    }

    @Override
    public void draw( Graphics g )
    {
        g.setColor( myColor );
        g.fillRect( missle.x, missle.y, missle.width, missle.height );
    }

    @Override
    public Rectangle intersects( Rectangle boundingBox )
    {
        return boundingBox.intersects( missle ) ? boundingBox.intersection( missle ) : null;
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
        missle.y += velocity;
        if(missle.y < 0){
            spriteDelList.add( this );
            //System.out.println("MissleSprite death");
        }
    }

}
