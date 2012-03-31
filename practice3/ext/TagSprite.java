package practice3.ext;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;


import libs.GameFrame;
import libs.Sprite;

public class TagSprite implements Sprite
{
    TagFrame gf;
    int baseX;
    int baseY;
    int myRadius;
    int myID;
    Color myTeamColor;
    int flicker;
    
    
    int targetID;
    float speed;
   

    public TagSprite(TagFrame GF, Color myTeam, int initX, int initY, int radius )
    {

        baseX = initX;
        baseY = initY;
        myRadius = radius;
        myTeamColor = myTeam;
        flicker = 0;
        speed = 5f;
        gf = GF;
        
        System.out.print("TagSprite.spawn "+myTeamColor.toString()+"-Team @("+baseX+","+baseY+")\n" );
     }
    
    public void setTarget()//random selection
    {
        TagSprite target = gf.pickRandomTarget(myTeamColor);
        targetID = newTargetID;
    }

    public void setTeamColor(Color c){
        myTeamColor = c;
    }
    public Color getTeam(){
        return myTeamColor;
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
            //g.setColor( Color.RED );
            flicker--;
        } 
        g.setColor( myTeamColor );
        
        g.fillOval( baseX-myRadius, baseY-myRadius, myRadius*2, myRadius*2 );
        g.setColor( Color.BLACK );
        g.fillOval( baseX, baseY, 2, 2 );
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
        
       //System.out.print("("+baseX+","+baseY+") ");
//        xAdjust = (targetX - baseX)
//        yAdjust = (targetY - baseY);
//        double dist = Math.sqrt(xAdjust*xAdjust+yAdjust*yAdjust);
//        //System.out.print("Dist("+(int)dist+") ");
        //System.out.print("delta("+xAdjust+","+yAdjust+")");
          
//        xAdjust = xAdjust/dist*speed;
//        yAdjust = yAdjust/dist*speed;
//        //System.out.print("adj("+xAdjust+","+yAdjust+")\n");
        
        int TryX = ( int ) ( Math.random() * 10 - 5 + xAdjust);
        int TryY = ( int ) ( Math.random() * 10 - 5 + yAdjust);
        
        
        //Kind of collision detection...
        if(baseX + TryX < gf.getWidth() - myRadius && baseX + TryX > myRadius)
            baseX += TryX;
        if(baseY + TryY < gf.getHeight() - myRadius && baseY + TryY > myRadius)
            baseY += TryY;
    }

}
