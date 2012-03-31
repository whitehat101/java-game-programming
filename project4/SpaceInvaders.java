package project4;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.Toolkit;
import java.io.IOException;
import java.util.Iterator;
import java.util.LinkedList;

import libs.GameEngine;
import libs.GameEventDispatcher;
import libs.GameFrame;
import libs.ImageUtil;
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
public class SpaceInvaders extends GameEngine
{

    private GameFrame myGameFrame;
    private LinkedList< Sprite > spriteList;

    private GameEventDispatcher dispatcher;
    private StateMachine state;
    private Image backgroundImage;

    public SpaceInvaders( )
    {
        myGameFrame = new GameFrame( 800, 600, true );
        // Toolkit toolkit = Toolkit.getDefaultToolkit();
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        myGameFrame.setLocation( screenSize.width - myGameFrame.getWidth() - 16, 32 );
        spriteList = new LinkedList< Sprite >();

        state = new StateMachine( spriteList );

        // Register Mouse and Key Adapters
        myGameFrame.addKeyListener( state );
        myGameFrame.addMouseListener( state );
        myGameFrame.addMouseMotionListener( state );

        dispatcher = GameEventDispatcher.getGameEventDispatcher();
        dispatcher.addGameEventListener( state );

        try
        {
            backgroundImage = ImageUtil.createImage( this, "Background.png" );
            // shipDeadImage = ImageUtil.createImage( this, "ship.png" );
        }
        catch ( IOException e )
        {
            System.out.println( "Failed to load ship sprites." );
        }

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

        // g2d.setColor( Color.BLACK );// Per Specification
        // g2d.fillRect( 0, 0, myGameFrame.getWidth(), myGameFrame.getHeight()
        // );
        g.drawImage( backgroundImage, 0, 0, null );

        g2d.setColor( Color.LIGHT_GRAY );
        g2d.setFont( new Font( "Tahoma", Font.BOLD, 16 ) );

        final int lineHeight = 20;
        final int yOffset = 10;
        g2d.drawString( "s Start New Game", 15, myGameFrame.getHeight() - 2 * lineHeight - yOffset );
        g2d.drawString( "e End Game", 15, myGameFrame.getHeight() - 1 * lineHeight - yOffset );
        g2d.drawString( "p Pause Game", 15, myGameFrame.getHeight() - 0 * lineHeight - yOffset );


        g2d.setFont( new Font( "Tahoma", Font.PLAIN, 12 ) );
        g2d.drawString( "esc/q Quit", myGameFrame.getWidth() - 150, myGameFrame.getHeight() - 1 * 15 - 10 );
        if ( spriteList.size() > 1 )
            g2d.drawString( "Managing " + spriteList.size() + " Sprites.", myGameFrame.getWidth() - 150,
                myGameFrame.getHeight() - 0 * 15 - 10 );
        else
            g2d.drawString( "Managing " + spriteList.size() + " Sprite.", myGameFrame.getWidth() - 150,
                    myGameFrame.getHeight() - 0 * 15 - 10 );

        // *** SPRITE LAYER**
        synchronized(spriteList)
        {
            Iterator< Sprite > it = spriteList.iterator();
            while ( it.hasNext() )
            {
                it.next().draw( g2d );
            }
        }
    }

    @Override
    public void collisions()
    {
        synchronized ( spriteList )
        {
            for ( Sprite spriteA : spriteList )
            {
                if ( spriteA == null ){
                    spriteList.remove( spriteA );// Why would this happen??
                    continue;
                }
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
        if ( state.isPaused() )
            return;

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
        SpaceInvaders si = new SpaceInvaders();
        si.start();
        si.manageEvents();
    }


    public void manageEvents()
    {
        state.run();
    }

}
