package practice4;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;

import libs.Sprite;

public class ASprite implements Sprite
{
    GameEngineForPratice4 ge;
    int baseX;
    int baseY;
    int myWidth;
    int myHeight;
    Color myColor;
    int flicker;
    
    
    boolean haveTarget;
    int targetX;
    int targetY;
    float speed;
   

    public ASprite(GameEngineForPratice4 GE,int initX, int initY, int radius )
    {

        baseX = initX;
        baseY = initY;
        myWidth = radius;
        myHeight = radius;
        myColor = Color.RED;
        flicker = 0;
        speed = 5f;
        haveTarget = false;
        ge = GE;
        
        System.out.print("ASprite spawn @("+baseX+","+baseY+")\n" );
     }
    
    public void setTarget(int xT, int yT)
    {
        targetX = xT;
        targetY = yT;
        haveTarget = true;
    }
    public void setColor(Color c){
        myColor = c;
    }

    @Override
    public void checkCollision( Sprite obj )
    {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void draw( Graphics g )
    {
        if(flicker > 0){
            g.setColor( Color.YELLOW );
            flicker--;
        } else {
            g.setColor( myColor );
        }
        g.fillOval( baseX, baseY, myWidth, myHeight );
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
        if(Math.random()*100<2.5f){
            flicker = (int)Math.round(Math.random()*3) + 2;
        }
        
        double xAdjust = 0.0;
        double yAdjust = 0.0;
        
        if(Math.random()<0.1f)return;
        
        if(haveTarget == true){
          //System.out.print("("+baseX+","+baseY+") ");
          xAdjust = (targetX - baseX);
          yAdjust = (targetY - baseY);
          double dist = Math.sqrt(xAdjust*xAdjust+yAdjust*yAdjust);
          //System.out.print("Dist("+(int)dist+") ");
          //System.out.print("delta("+xAdjust+","+yAdjust+")");
          
          xAdjust = xAdjust/dist*speed;
          yAdjust = yAdjust/dist*speed;
          //System.out.print("adj("+xAdjust+","+yAdjust+")\n");
        }
        
        
        
        int TryX = ( int ) ( Math.random() * 10 - 5 + xAdjust);
        int TryY = ( int ) ( Math.random() * 10 - 5 + yAdjust);
        
        if(baseX + TryX < ge.getWidth() - myHeight && baseX + TryX > 0)
            baseX += TryX;
        if(baseY + TryY < ge.getHeight() - myWidth && baseY + TryY > 0)
            baseY += TryY;
    }

}
