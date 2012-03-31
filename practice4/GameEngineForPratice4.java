package practice4;

//Unzip and copy the framework classes below into the libs package
//Create a new package in your Eclipse workspace called practice4
//Create a class named Practice4 that extends GameEngine and contains a main()
//In your constructor method instantiate a GameFrame of size 800 x 600
//Implement code to manage key presses and mouse actions
//Draw a white background
//Using the Sprite class create sprite objects that uses Java 2D shapes to create several round sprites
//Add your sprites so they are rendered on the screen
//Move at least one sprite about the screen using a fixed velocity and bounce the sprites off the sides of the frame
//Move at least one sprite with the keyboard
//Move at least one sprite with the mouse and change it's color with a mouse click
//Execute your program to make sure it works, then jar up the code and submit.

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Vector;

import libs.GameEngine;
import libs.GameFrame;
import libs.Sprite;

public class GameEngineForPratice4 extends GameEngine
{
    GameFrame myGameFrame;
    List< Sprite > spriteList;
    LinkedList< Sprite > spriteAddList;// should probably be a queue
    LinkedList< Sprite > spriteDelList;
    GameEngineForPratice4 self;// For key listener

    public GameEngineForPratice4( )
    {
        // TODO Auto-generated constructor stub
        myGameFrame = new GameFrame( 800, 600, true );
        spriteList = new Vector< Sprite >();
        spriteAddList = new LinkedList< Sprite >();// should probably be a queue
        spriteDelList = new LinkedList< Sprite >();
        self = this;

        KeyAdapter basicKeyAdapter = new KeyAdapter()
        {
            public void keyPressed( KeyEvent ke )
            {
                // System.out.println( ke.getKeyCode() );
                switch ( ke.getKeyCode() )
                {
                    case KeyEvent.VK_J:
                        JME( self );
                        break;
                    case KeyEvent.VK_R:
                        RWBfields( self );
                        break;
                    case KeyEvent.VK_C:
                        removeAllSprites();
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
        };
        MouseAdapter basicMouseAdapter = new MouseAdapter()
        {
            public void mouseClicked( MouseEvent me )
            {
                Iterator< Sprite > it = spriteList.iterator();
                while ( it.hasNext() )
                {
                    it.next().mouseAction( me );
                }
            }
            public void mousePressed( MouseEvent me )
            {
                Iterator< Sprite > it = spriteList.iterator();
                while ( it.hasNext() )
                {
                    it.next().mouseAction( me );
                }
            }
            public void mouseReleased( MouseEvent me )
            {
                Iterator< Sprite > it = spriteList.iterator();
                while ( it.hasNext() )
                {
                    it.next().mouseAction( me );
                }
            }
            public void mouseDragged( MouseEvent me )
            {
                Iterator< Sprite > it = spriteList.iterator();
                while ( it.hasNext() )
                {
                    it.next().mouseAction( me );
                }
            }
        };
        myGameFrame.addKeyListener( basicKeyAdapter );
        myGameFrame.addMouseListener( basicMouseAdapter );
        myGameFrame.addMouseMotionListener( basicMouseAdapter );

    }

    @Override
    public void collisions()
    {
        // TODO Auto-generated method stub
//        System.out.println( "doh!" );
    }

    @Override
    public void update()
    {
        // Add new Sprites
        while ( !spriteAddList.isEmpty() )
        {
            spriteList.add( spriteAddList.removeFirst() );
        }
        // Remove old Sprites
        while ( !spriteDelList.isEmpty() )
        {
            spriteList.remove( spriteDelList.removeFirst() );
        }

        Iterator< Sprite > it = spriteList.iterator();

        while ( it.hasNext() )
        {
            // Collision?
            Sprite s = it.next();
            s.update();
            // Random Death!
            // To test the remove queue
            // if(Math.random()*1000 < 5)
            // spriteRemoveList.add( s );
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

        g2d.setColor( new Color( 15, 168, 250 ) );
        g2d.fillRect( 0, 0, myGameFrame.getWidth(), myGameFrame.getHeight() );

        // g2d.setColor(new Color(0xCA,0xFE,0xED));
        g2d.setColor( Color.DARK_GRAY );
        g2d.setFont( new Font( "Tahoma", Font.BOLD, 20 ) );

        g2d.drawString( "c Clear", 15, myGameFrame.getHeight() - 2 * 25 - 10 );
        g2d.drawString( "j JME Animation", 15, myGameFrame.getHeight() - 1 * 25 - 10 );
        g2d.drawString( "r RWB Animation", 15, myGameFrame.getHeight() - 0 * 25 - 10 );

        g2d.setFont( new Font( "Tahoma", Font.PLAIN, 12 ) );
        g2d.drawString( "esc/q Quit", myGameFrame.getWidth() - 150, myGameFrame.getHeight() - 1 * 15 - 10 );
        g2d.drawString( "Managing " + spriteList.size() + " Sprites.", myGameFrame.getWidth() - 150,
                myGameFrame.getHeight() - 0 * 15 - 10 );

        // *** SPRITE LAYER**
        Iterator< Sprite > it = spriteList.iterator();
        while ( it.hasNext() )
        {
            it.next().draw( g2d );
        }
        // *** UI/TOP LAYER**
        // None for now

    }

    public int getWidth()
    {
        return myGameFrame.getWidth();
    }

    public int getHeight()
    {
        return myGameFrame.getHeight();
    }

    public void addSprite( Sprite sprite )
    {
        spriteAddList.add( sprite );
    }

    public void removeAllSprites()
    {
        spriteDelList.addAll( spriteList );
    }

    /**
     * @param args
     */
    public static void main( String [ ] args )
    {
        // TODO Auto-generated method stub'
        GameEngineForPratice4 gefp4 = new GameEngineForPratice4();
        
        KeySprite ks = new KeySprite(gefp4, 60,60,50);
        ks.setColor( Color.PINK );
        gefp4.addSprite( ks );

        MouseSprite ms = new MouseSprite(gefp4, 100,100, 25);
        ms.setColor( Color.MAGENTA );
        gefp4.addSprite( ms );

        BounceSprite bs = new BounceSprite(gefp4, 100,100, 25);
        bs.setColor( Color.BLUE );
        gefp4.addSprite( bs );

        //JME( gefp4 );
        
        gefp4.start();
    }

    public static void JME( GameEngineForPratice4 gt )
    {
        int radius = 35;
        // Top of j
        for ( int i = 0; i < 4; i++ )
        {
            int theX = ( int ) ( Math.random() * ( gt.getWidth() - 2 * radius ) ) + radius;
            int theY = ( int ) ( Math.random() * ( gt.getHeight() - 2 * radius ) ) + radius;
            ASprite s = new ASprite( gt, theX, theY, radius );

            int tX = ( 2 + i ) * radius;
            int tY = ( 2 ) * radius;
            s.setTarget( tX, tY );
            s.setColor( Color.RED );
            gt.addSprite( s );
        }
        // Stem of j
        for ( int i = 0; i < 4; i++ )
        {
            int theX = ( int ) ( Math.random() * ( gt.getWidth() - 2 * radius ) ) + radius;
            int theY = ( int ) ( Math.random() * ( gt.getHeight() - 2 * radius ) ) + radius;
            ASprite s = new ASprite( gt, theX, theY, radius );

            int tX = ( 4 ) * radius;
            int tY = ( 3 + i ) * radius;
            s.setTarget( tX, tY );
            s.setColor( Color.RED );
            gt.addSprite( s );
        }
        // Base of j
        for ( int i = 0; i < 2; i++ )
        {
            int theX = ( int ) ( Math.random() * ( gt.getWidth() - 2 * radius ) ) + radius;
            int theY = ( int ) ( Math.random() * ( gt.getHeight() - 2 * radius ) ) + radius;
            ASprite s = new ASprite( gt, theX, theY, radius );

            int tX = ( 2 + i ) * radius;
            int tY = ( 6 ) * radius;
            s.setTarget( tX, tY );
            s.setColor( Color.RED );
            gt.addSprite( s );
        }

        // Legs of M
        for ( int i = 0; i < 10; i++ )
        {
            int theX = ( int ) ( Math.random() * ( gt.getWidth() - 2 * radius ) ) + radius;
            int theY = ( int ) ( Math.random() * ( gt.getHeight() - 2 * radius ) ) + radius;
            ASprite s = new ASprite( gt, theX, theY, radius );

            int tX = ( 7 + ( i / 5 ) * 4 ) * radius;
            int tY = ( 2 + i % 5 ) * radius;
            s.setTarget( tX, tY );
            s.setColor( Color.BLUE );
            gt.addSprite( s );
        }
        // v of M
        for ( int i = 0; i < 3; i++ )
        {
            int theX = ( int ) ( Math.random() * ( gt.getWidth() - 2 * radius ) ) + radius;
            int theY = ( int ) ( Math.random() * ( gt.getHeight() - 2 * radius ) ) + radius;
            ASprite s = new ASprite( gt, theX, theY, radius );

            int tX = ( 8 + i ) * radius;
            int tY = ( 3 + i % 2 ) * radius - ( i % 2 ) * radius / 2;
            s.setTarget( tX, tY );
            s.setColor( Color.BLUE );
            gt.addSprite( s );
        }

        // Side of E
        for ( int i = 0; i < 5; i++ )
        {
            int theX = ( int ) ( Math.random() * ( gt.getWidth() - 2 * radius ) ) + radius;
            int theY = ( int ) ( Math.random() * ( gt.getHeight() - 2 * radius ) ) + radius;
            ASprite s = new ASprite( gt, theX, theY, radius );

            int tX = ( 13 ) * radius;
            int tY = ( 2 + i ) * radius;
            s.setTarget( tX, tY );
            s.setColor( Color.WHITE );
            gt.addSprite( s );
        }
        // Arms of E
        for ( int i = 0; i < 8; i++ )
        {
            int theX = ( int ) ( Math.random() * ( gt.getWidth() - 2 * radius ) ) + radius;
            int theY = ( int ) ( Math.random() * ( gt.getHeight() - 2 * radius ) ) + radius;
            ASprite s = new ASprite( gt, theX, theY, radius );

            int tX = ( 14 + i / 3 ) * radius;
            int tY = ( 2 + i % 3 * 4 ) * radius;
            if ( tY == 10 * radius )
                tY = ( 2 + 2 ) * radius;
            s.setTarget( tX, tY );
            s.setColor( Color.WHITE );
            gt.addSprite( s );
        }
    }

    public static void RWBfields( GameEngineForPratice4 ge )
    {
        int radius = 36;
        for ( int i = 0; i < 228; i++ )
        {
            int theX = ( int ) ( Math.random() * ( ge.getWidth() - 2 * radius ) ) + radius;
            int theY = ( int ) ( Math.random() * ( ge.getHeight() - 2 * radius ) ) + radius;
            ASprite s = new ASprite( ge, theX, theY, radius );

            int tX = ( i % 19 ) * radius + 50;
            int tY = i / 19 * radius + 50;
            s.setTarget( tX, tY );

            switch ( i % 3 )
            {
                case 0:
                    s.setColor( Color.RED );
                    break;
                case 1:
                    s.setColor( Color.WHITE );
                    break;
                case 2:
                    s.setColor( Color.BLUE );
                    break;
            }

            ge.addSprite( s );
        }
    }

}
