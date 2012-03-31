package practice3;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.Transparency;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.awt.image.WritableRaster;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.imageio.ImageReader;

import libs.GameFrame;
import libs.Sprite;

public class BSprite implements Sprite
{
    char suit;
    String card;
    BufferedImage image;
    int baseX;
    int baseY;
    int myWidth;
    int myHeight;
    GameFrame gf;

    public BSprite(GameFrame GF )
    {
        suit = 's';
        card = "B";
        baseX = 100;
        baseY = 125;
        myWidth = 25;
        myHeight = 30;
        gf = GF;
        
        //image = gf.getCompatibleImage( 300, 300, Transparency.TRANSLUCENT );


        //BufferedImage bi = null;
        try {
            image = ImageIO.read(new File("Cards-s.png"));
        } catch (IOException ioe) {
        }

        
//        imageG2D.setFont(new Font("Times New Roman", Font.BOLD, 20));
//        imageG2D.setColor( Color.BLACK );
//        imageG2D.drawString( card, 0, 0 );
        
        //imageG2D.finalize();

     }

    @Override
    public void checkCollision( Sprite obj )
    {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void draw( Graphics g )
    {
        //System.out.println("i ran");
        //g.drawImage( image, 0, 0, null );
        g.setFont(new Font("Times New Roman", Font.BOLD, 20));
        g.setColor( Color.BLUE );
        g.fillOval( baseX, baseY, myWidth, myHeight );
        g.setColor( Color.BLACK );
        g.drawString( card, baseX + 7, baseY + 20  );    }

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
        if(Math.random()<0.4f)return;
        int TryY = ( int ) ( Math.random() * 20 - 10 );
        int TryX = ( int ) ( Math.random() * 20 - 10 );
        
        if(baseX + TryX < gf.getWidth() - myHeight && baseX + TryX > 0)
            baseX += TryX;
        if(baseY + TryY < gf.getHeight() - myWidth && baseY + TryY > 0)
            baseY += TryY;
    }

}
