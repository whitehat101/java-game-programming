package practice3.ext;

import java.awt.Color;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

import libs.GameFrame;

public class Practice3
{

    /**
     * @param args
     */
    public static void main( String [ ] args )
    {
        final GameFrame gf = new GameFrame( 800, 600, true );
        final GameThread gt = new GameThread( gf );

        gt.runAtFPS( 30.0f );

        // Populate the Gamefield
        // RWBfields(gf, gt);
        // JME(gf,gt);

        // Input this doesn't feel like it should belong here...
        KeyAdapter basicKeyAdapter = new KeyAdapter()
        {
            public void keyPressed( KeyEvent ke )
            {
                System.out.println( ke.getKeyCode() );
                switch ( ke.getKeyCode() )
                {
                    case KeyEvent.VK_F:
                        gt.run();// step one frame
                        break;
                    case KeyEvent.VK_J:
                        JME( gf, gt );
                        break;
                    case KeyEvent.VK_R:
                        RWBfields( gf, gt );
                        break;
                    case KeyEvent.VK_T:
                        tagSim( gf, gt, 10, new Color [ ] { Color.RED, Color.BLUE } );
                        break;
                    case KeyEvent.VK_C:
                        gt.removeAllSpritesSprite();
                        break;

                    // I have esc bound to speech recognition on my setup.
                    // For whatever reason, the OS grabs some keyCodes and
                    // doesn't share with java
                    // In short, Q or Esc Quits
                    case KeyEvent.VK_Q:
                    case KeyEvent.VK_ESCAPE:
                        gt.stopSimulation();// How polite am I?
                        System.exit( 0 );
                }
            }
        };
        gf.addKeyListener( basicKeyAdapter );
        // MouseMotionListener mouse;
        // gf.addMouseMotionListener( mouse );

        System.out.println( "Done With setup, running simulation." );
        // while(true){gt.run();}
    }

    public static void RWBfields( GameFrame gf, GameThread gt )
    {
        int radius = 36;
        for ( int i = 0; i < 228; i++ )
        {
            int theX = ( int ) ( Math.random() * ( gf.getWidth() - 2 * radius ) ) + radius;
            int theY = ( int ) ( Math.random() * ( gf.getHeight() - 2 * radius ) ) + radius;
            ASprite s = new ASprite( gf, theX, theY, radius );

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

            gt.addSprite( s );
        }
    }

    public static void JME( GameFrame gf, GameThread gt )
    {
        int radius = 35;
        // Top of j
        for ( int i = 0; i < 4; i++ )
        {
            int theX = ( int ) ( Math.random() * ( gf.getWidth() - 2 * radius ) ) + radius;
            int theY = ( int ) ( Math.random() * ( gf.getHeight() - 2 * radius ) ) + radius;
            ASprite s = new ASprite( gf, theX, theY, radius );

            int tX = ( 2 + i ) * radius;
            int tY = ( 2 ) * radius;
            s.setTarget( tX, tY );
            s.setColor( Color.RED );
            gt.addSprite( s );
        }
        // Stem of j
        for ( int i = 0; i < 4; i++ )
        {
            int theX = ( int ) ( Math.random() * ( gf.getWidth() - 2 * radius ) ) + radius;
            int theY = ( int ) ( Math.random() * ( gf.getHeight() - 2 * radius ) ) + radius;
            ASprite s = new ASprite( gf, theX, theY, radius );

            int tX = ( 4 ) * radius;
            int tY = ( 3 + i ) * radius;
            s.setTarget( tX, tY );
            s.setColor( Color.RED );
            gt.addSprite( s );
        }
        // Base of j
        for ( int i = 0; i < 2; i++ )
        {
            int theX = ( int ) ( Math.random() * ( gf.getWidth() - 2 * radius ) ) + radius;
            int theY = ( int ) ( Math.random() * ( gf.getHeight() - 2 * radius ) ) + radius;
            ASprite s = new ASprite( gf, theX, theY, radius );

            int tX = ( 2 + i ) * radius;
            int tY = ( 6 ) * radius;
            s.setTarget( tX, tY );
            s.setColor( Color.RED );
            gt.addSprite( s );
        }

        // Legs of M
        for ( int i = 0; i < 10; i++ )
        {
            int theX = ( int ) ( Math.random() * ( gf.getWidth() - 2 * radius ) ) + radius;
            int theY = ( int ) ( Math.random() * ( gf.getHeight() - 2 * radius ) ) + radius;
            ASprite s = new ASprite( gf, theX, theY, radius );

            int tX = ( 7 + ( i / 5 ) * 4 ) * radius;
            int tY = ( 2 + i % 5 ) * radius;
            s.setTarget( tX, tY );
            s.setColor( Color.BLUE );
            gt.addSprite( s );
        }
        // v of M
        for ( int i = 0; i < 3; i++ )
        {
            int theX = ( int ) ( Math.random() * ( gf.getWidth() - 2 * radius ) ) + radius;
            int theY = ( int ) ( Math.random() * ( gf.getHeight() - 2 * radius ) ) + radius;
            ASprite s = new ASprite( gf, theX, theY, radius );

            int tX = ( 8 + i ) * radius;
            int tY = ( 3 + i % 2 ) * radius - ( i % 2 ) * radius / 2;
            s.setTarget( tX, tY );
            s.setColor( Color.BLUE );
            gt.addSprite( s );
        }

        // Side of E
        for ( int i = 0; i < 5; i++ )
        {
            int theX = ( int ) ( Math.random() * ( gf.getWidth() - 2 * radius ) ) + radius;
            int theY = ( int ) ( Math.random() * ( gf.getHeight() - 2 * radius ) ) + radius;
            ASprite s = new ASprite( gf, theX, theY, radius );

            int tX = ( 13 ) * radius;
            int tY = ( 2 + i ) * radius;
            s.setTarget( tX, tY );
            s.setColor( Color.WHITE );
            gt.addSprite( s );
        }
        // Arms of E
        for ( int i = 0; i < 8; i++ )
        {
            int theX = ( int ) ( Math.random() * ( gf.getWidth() - 2 * radius ) ) + radius;
            int theY = ( int ) ( Math.random() * ( gf.getHeight() - 2 * radius ) ) + radius;
            ASprite s = new ASprite( gf, theX, theY, radius );

            int tX = ( 14 + i / 3 ) * radius;
            int tY = ( 2 + i % 3 * 4 ) * radius;
            if ( tY == 10 * radius )
                tY = ( 2 + 2 ) * radius;
            s.setTarget( tX, tY );
            s.setColor( Color.WHITE );
            gt.addSprite( s );
        }
    }

    public static void tagSim( GameFrame gf, GameThread gt, int teamSize, Color [ ] teams )
    {
        int radius = 50;

        int theX;
        int theY;
        TagSprite s;

        int teamCount = teams.length;

        for ( int teamNumber = 0; teamNumber < teamCount; teamNumber++ )
        {
            Color teamColor = teams[ teamNumber ];

            for ( int id = 0; id < teamSize; id++ )
            {
                theX = ( int ) ( Math.random() * ( gf.getWidth() / teamCount - 2 * radius ) ) + radius + teamNumber
                        * gf.getWidth() / ( teamNumber + 1 );
                theY = ( int ) ( Math.random() * ( gf.getHeight() - 2 * radius ) ) + radius;

                // s = new TagSprite(gf,teamColor,theX, theY, radius);
                // gt.addSprite( s );
            }
        }
    }

}
