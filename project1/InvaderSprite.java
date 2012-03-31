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

public class InvaderSprite implements Sprite
{
    Rectangle boundingBox;
    Point drawPoint;
    Dimension widthheight;
    private int row;
    private int col;
    private float padding;
    private Color color;
    private LinkedList<Sprite> spriteDelList;


    public InvaderSprite( int row, int col, Dimension widthheight, LinkedList<Sprite> spriteDelList)
    {
        padding = 1.2f;
        color = Color.RED;
        this.row = row;
        this.col = col;
        this.widthheight = widthheight;
        this.spriteDelList = spriteDelList;
        drawPoint = new Point();
    }

    @Override
    public void checkCollision( Sprite obj )
    {
        if (color == Color.YELLOW)return;
        
        if(obj instanceof MissleSprite){
            Rectangle rectangle = new Rectangle(drawPoint.x, drawPoint.y, widthheight.width, widthheight.height);
            Rectangle result = obj.intersects( rectangle );
            if(result != null){
                System.out.println("InvaderSprite hit by MissleSprite");
                color = Color.YELLOW;
                spriteDelList.add( obj );
            }
        }
    }

    @Override
    public void draw( Graphics g )
    {
        g.setColor(color);
        g.fillRect(drawPoint.x, drawPoint.y, widthheight.width, widthheight.height  );

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
        // TODO Auto-generated method stub

    }

    @Override
    public void mouseAction( MouseEvent me )
    {
        // TODO Auto-generated method stub

    }

    @Override
    public void update()
    {
        // TODO Auto-generated method stub

    }

    public void setFleetPoint( Point invaderFleet )
    {
        
//        this.invaderFleet = invaderFleet;
        drawPoint.x = ( int ) ( invaderFleet.x + row*widthheight.width*padding );
        drawPoint.y = ( int ) ( invaderFleet.y + col*widthheight.height*padding );
    }
    
    public Point getLocation()
    {
        return new Point(drawPoint.x,drawPoint.y);
    }

}
