package project1;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.util.Iterator;
import java.util.LinkedList;

import libs.GameEngine;
import libs.GameFrame;
import libs.Sprite;

/*
 * Game Specification:
 Aliens are invading! Create a new package in your workspace named project1. 
 Create your main game engine class and create the following sprites:
 * ship sprite
 * fortress sprite (that's the object you hid behind)
 * ship missile sprite
 * alien sprite
 * alien missile sprite
 Make the frame 800x600 with a black background.
 For each of your sprites draw white Rectangle2D shapes.
 Create two rows of five aliens and create three fortresses. 
 Arrange your sprites on the screen to match the starting positions of the game. 
 Move your space ship left and right using the mouse movement.
 */
public class SpaceInvaders1 extends GameEngine implements KeyListener, MouseMotionListener, MouseListener
{

    private GameFrame myGameFrame;
    private LinkedList< Sprite > spriteList;

    private LinkedList< Sprite > spriteAddList;
    private LinkedList< Sprite > spriteDelList;
    // private SpaceInvaders1 self;//Still needed?
    
    private PlayerShipSprite player;
    private BaseSprite base;
    private InvaderFleetSprite invader;
    //private Vector<MissleSprite> playerMissles, ememyMissles;

    public SpaceInvaders1( )
    {
        myGameFrame = new GameFrame( 800, 600, true );
        spriteList = new LinkedList< Sprite >();
        spriteAddList = new LinkedList< Sprite >();// should probably be a queue
        spriteDelList = new LinkedList< Sprite >();

        // Register Mouse and Key Adapters
        myGameFrame.addKeyListener( this );
        myGameFrame.addMouseListener( this );
        myGameFrame.addMouseMotionListener( this );

        // This draws all the bases ^_^
        base = new BaseSprite( new Rectangle( 20, 480, 760, 50 ), 100, 3 );

        // Ship is padded 10px above and below the bases and the frame
        player = new PlayerShipSprite( new Rectangle( 20, 540, 760, 50 ),spriteAddList,spriteDelList );
        
        // Ship is padded 10px above and below the bases and the frame
        invader = new InvaderFleetSprite( new Rectangle( 20, 20, 760, 460 ), 15, spriteAddList,spriteDelList );

        spriteList.add( player );
        spriteList.add( base );
        spriteList.add( invader );
    }

    @Override
    public void draw()
    {
        myGameFrame.updateGraphics();
    }

    @Override
    public void render()
    {
        // *** BACKGROUND LAYER**
        Graphics g = myGameFrame.getCurrentGraphics();
        Graphics2D g2d = ( Graphics2D ) g;
        // General Setup
        // Enable antialiasing for text
        g2d.setRenderingHint( RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON );
        // Enable antialiasing for shapes
        g2d.setRenderingHint( RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON );

        g2d.setColor( Color.BLACK );// Per Specification
        g2d.fillRect( 0, 0, myGameFrame.getWidth(), myGameFrame.getHeight() );

        g2d.setColor( Color.DARK_GRAY );
        // g2d.setFont( new Font( "Tahoma", Font.BOLD, 20 ) );
        //
        // g2d.drawString( "c Clear", 15, myGameFrame.getHeight() - 2 * 25 - 10
        // );
        // g2d.drawString( "j JME Animation", 15, myGameFrame.getHeight() - 1 *
        // 25 - 10 );
        // g2d.drawString( "r RWB Animation", 15, myGameFrame.getHeight() - 0 *
        // 25 - 10 );

        g2d.setFont( new Font( "Tahoma", Font.PLAIN, 12 ) );
        g2d.drawString( "esc/q Quit", myGameFrame.getWidth() - 150, myGameFrame.getHeight() - 1 * 15 - 10 );
        g2d.drawString( "Managing " + spriteList.size() + " Sprites.", myGameFrame.getWidth() - 150,
                myGameFrame.getHeight() - 0 * 15 - 10 );

        // *** SPRITE LAYER**
        synchronized(spriteList)
        {
            while(!spriteAddList.isEmpty())
            {
                spriteList.addFirst(spriteAddList.removeFirst());
            }
            while(!spriteDelList.isEmpty())
            {
                Sprite s = spriteDelList.removeFirst();
                spriteList.remove(s);
                //hint to gc?
            }
            
            
            Iterator< Sprite > it = spriteList.iterator();
            while ( it.hasNext() )
            {
                it.next().draw( g2d );
            }
            
            
        }
        // *** UI/TOP LAYER**
        // None for now
    }

    @Override
    public void collisions()
    {
        synchronized ( spriteList )
        {
            for ( Sprite spriteA : spriteList )
            {
                for ( Sprite spriteB : spriteList )
                {
                    if ( !spriteA.equals( spriteB ) )
                        spriteA.checkCollision( spriteB );
                }
            }
        }
    }

    @Override
    public void update()
    {
        synchronized ( spriteList )
        {
            for ( Sprite sprite : spriteList )
            {
                sprite.update();
            }
        }
    }

    /**
     * @param args
     */
    public static void main( String [ ] args )
    {
        SpaceInvaders1 si1 = new SpaceInvaders1();
        si1.start();

    }

    @Override
    public void mouseClicked( MouseEvent arg0 )
    {
        player.mouseAction( arg0 );
    }

    @Override
    public void mouseEntered( MouseEvent arg0 )
    {
        // TODO Auto-generated method stub

    }

    @Override
    public void mouseExited( MouseEvent arg0 )
    {
        // TODO Auto-generated method stub

    }

    @Override
    public void mousePressed( MouseEvent arg0 )
    {
        // TODO Auto-generated method stub

    }

    @Override
    public void mouseReleased( MouseEvent me )
    {
        Iterator< Sprite > it = spriteList.iterator();
        while ( it.hasNext() )
        {
            it.next().mouseAction( me );
        }
    }

    @Override
    public void mouseDragged( MouseEvent arg0 )
    {
        // TODO Auto-generated method stub

    }

    @Override
    public void mouseMoved( MouseEvent me )
    {
        player.mouseAction( me );
//        Iterator< Sprite > it = spriteList.iterator();
//        while ( it.hasNext() )
//        {
//            it.next().mouseAction( me );
//        }
    }

    @Override
    public void keyPressed( KeyEvent ke )
    {
        switch ( ke.getKeyCode() )
        {
            case KeyEvent.VK_J:
                // JME( self );
                break;
            case KeyEvent.VK_R:
                // RWBfields( self );
                break;
            case KeyEvent.VK_C:
                // removeAllSprites();
                break;

            // I have esc bound to speech recognition on my setup.
            // For whatever reason, the OS grabs some keyCodes and
            // doesn't share with java
            // In short, Q or Esc Quits
            case KeyEvent.VK_Q:
            case KeyEvent.VK_ESCAPE:
                // gt.stopSimulation();// How polite am I?
                System.exit( 0 );
            default:
                Iterator< Sprite > it = spriteList.iterator();
                while ( it.hasNext() )
                {
                    it.next().keyboardAction( ke );
                }

        }
    }

    @Override
    public void keyReleased( KeyEvent arg0 )
    {
        // TODO Auto-generated method stub

    }

    @Override
    public void keyTyped( KeyEvent ke )
    {
    }
}
