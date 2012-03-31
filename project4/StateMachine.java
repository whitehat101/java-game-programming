package project4;

import java.awt.Color;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.io.IOException;
import java.util.LinkedList;

import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;

import libs.AudioSample;
import libs.GameEvent;
import libs.GameEvent.GameEventType;
import libs.GameEventListener;
import libs.Sprite;

public class StateMachine implements GameEventListener, Runnable, MouseMotionListener, MouseListener, KeyListener
{
    // Events
    private LinkedList< GameEvent > eventList;

    // Sprites
    private LinkedList< Sprite > spriteList;
    private PlayerShipSprite player;
    private InvaderFleetSprite invader;
    private ScoreSprite scoreSprite;
    private LivesSprite livesSprite;
    // private MissileMonitorSprite missileMonitorSprite;
    private ScoreBoardSprite highScoreSprite;

    // State

    private State previousState;
    private State presentState;
    private boolean pause;
    private int level;
    private AudioSample backgroundAudio;

    enum State
    {
        Splash, Playing, Pause, Menu, HighScores
    }

    public StateMachine( LinkedList< Sprite > spriteList )
    {
        this.spriteList = spriteList;
        eventList = new LinkedList< GameEvent >();


        setState( State.Splash );
        JME( new Rectangle( 0, 0, 800, 600 ), new Point( 90, 150 ) );
    }

    public void run()
    {
        manageEvents();
    }

    private void manageEvents()
    {
        GameEvent event;

        while ( true )
        {
            synchronized ( eventList )
            {
                if ( eventList.isEmpty() )
                {
                    try
                    {
                        eventList.wait();
                    }
                    catch ( InterruptedException exception )
                    {
                    }
                }
                event = eventList.removeFirst();
            }
            // System.out.println( event.getSource() + "->" + event.getType() +
            // ": " + event.getAttachment() );
            switch ( event.getType() )
            {
                case AddBack:
                    synchronized ( spriteList )
                    {
                        spriteList.add( ( Sprite ) event.getAttachment() );
                    }
                    break;

                case AddFront:
                    synchronized ( spriteList )
                    {
                        spriteList.addFirst( ( Sprite ) event.getAttachment() );
                    }
                    break;

                case Remove:
                    synchronized ( spriteList )
                    {
                        spriteList.remove( ( Sprite ) event.getAttachment() );
                    }
                    break;

                // case Life:
                // break;

                case Start:
                    switch(getState())
                    {
                        case Splash:
                        case Menu:
                        case HighScores:
                        case Pause:
                            synchronized ( spriteList )
                            {
                                spriteList.clear();
                            }
                            startGame();
                            setLevel( 1 );
                            setState( State.Playing );
                    }
                    break;

                case LevelComplete:
                    if ( getState() == State.Playing )
                    {
                        setLevel( ++level );
                    }
                    break;

                case End:
                    if ( getState() == State.Playing )
                    {
                        setHighScoresScreen();
                        highScoreSprite.newScore( level, scoreSprite.getScore() );
                    }
                    break;
                case Pause:
                    // Unnecessarily complex code..?
                    if ( getState() == State.Playing )
                    {
                        pause = true;
                        setState( State.Pause );
                    }
                    else if ( getState() == State.Pause )
                    {
                        pause = false;
                        setState( getPrevState() );
                    }
                    break;
                case Quit:
                    System.exit( 0 );
                    break;
                default:
                    // System.out.print( "Unhandled Event!: " );
                    // System.out.println( event.getSource() + "->" +
                    // event.getType() + ": " + event.getAttachment() );
            }
        }
    }

    private void setHighScoresScreen()
    {
        synchronized ( spriteList )
        {
            spriteList.clear();
            if ( highScoreSprite == null )
                highScoreSprite = new ScoreBoardSprite();
            spriteList.add( highScoreSprite );
            // spriteList.add( missileMonitorSprite );
        }
        setState( State.HighScores );
    }

    // ProducerLogo
    private void JME( Rectangle boundingBox, Point controlPoint )
    {
        // ASprite( Point start, int radius, Rectangle boundingBox, float speed )
        final Rectangle bounds = boundingBox;
        final int radius = 15;
        final float speed = 7.5f;

        // Top of j
        for ( int i = 0; i < 4; i++ )
        {
            int theX = ( int ) ( Math.random() * ( bounds.width - 2 * radius ) ) + bounds.x;
            int theY = ( int ) ( Math.random() * ( bounds.height - 2 * radius ) ) + bounds.y;
            ASprite s = new ASprite( new Point( theX, theY ), radius, bounds, speed );

            int tX = ( 2 + i ) * 2 * radius;
            int tY = ( 2 ) * 2 * radius;
            s.setTarget( new Point( tX + controlPoint.x, tY + controlPoint.y ) );
            s.setColor( Color.RED );
            gameEvent( new GameEvent( this, GameEventType.AddBack, s ) );
        }
        // Stem of j
        for ( int i = 0; i < 4; i++ )
        {
            int theX = ( int ) ( Math.random() * ( bounds.width - 2 * radius ) ) + bounds.x;
            int theY = ( int ) ( Math.random() * ( bounds.height - 2 * radius ) ) + bounds.y;
            Point start = new Point( theX, theY );
            ASprite s = new ASprite( start, radius, bounds, speed );

            int tX = ( 4 ) * 2 * radius;
            int tY = ( 3 + i ) * 2 * radius;
            s.setTarget( new Point( tX + controlPoint.x, tY + controlPoint.y ) );
            s.setColor( Color.RED );
            gameEvent( new GameEvent( this, GameEventType.AddBack, s ) );
        }
        // Base of j
        for ( int i = 0; i < 2; i++ )
        {
            int theX = ( int ) ( Math.random() * ( bounds.width - 2 * radius ) ) + bounds.x;
            int theY = ( int ) ( Math.random() * ( bounds.height - 2 * radius ) ) + bounds.y;
            Point start = new Point( theX, theY );
            ASprite s = new ASprite( start, radius, bounds, speed );

            int tX = ( 2 + i ) * 2 * radius;
            int tY = ( 6 ) * 2 * radius;
            s.setTarget( new Point( tX + controlPoint.x, tY + controlPoint.y ) );
            gameEvent( new GameEvent( this, GameEventType.AddBack, s ) );
        }

        // Legs of M
        for ( int i = 0; i < 10; i++ )
        {
            int theX = ( int ) ( Math.random() * ( bounds.width - 2 * radius ) ) + bounds.x;
            int theY = ( int ) ( Math.random() * ( bounds.height - 2 * radius ) ) + bounds.y;
            Point start = new Point( theX, theY );
            ASprite s = new ASprite( start, radius, bounds, speed );

            int tX = ( 7 + ( i / 5 ) * 4 ) * 2 * radius;
            int tY = ( 2 + i % 5 ) * 2 * radius;
            s.setTarget( new Point( tX + controlPoint.x, tY + controlPoint.y ) );
            s.setColor( Color.BLUE );
            gameEvent( new GameEvent( this, GameEventType.AddBack, s ) );
        }
        // v of M
        for ( int i = 0; i < 3; i++ )
        {
            int theX = ( int ) ( Math.random() * ( bounds.width - 2 * radius ) ) + bounds.x;
            int theY = ( int ) ( Math.random() * ( bounds.height - 2 * radius ) ) + bounds.y;
            Point start = new Point( theX, theY );
            ASprite s = new ASprite( start, radius, bounds, speed );

            int tX = ( 8 + i ) * 2 * radius;
            int tY = ( 3 + i % 2 ) * 2 * radius - ( i % 2 ) * 2 * radius / 2;
            s.setTarget( new Point( tX + controlPoint.x, tY + controlPoint.y ) );
            s.setColor( Color.BLUE );
            gameEvent( new GameEvent( this, GameEventType.AddBack, s ) );
        }

        // Side of E
        for ( int i = 0; i < 5; i++ )
        {
            int theX = ( int ) ( Math.random() * ( bounds.width - 2 * radius ) ) + bounds.x;
            int theY = ( int ) ( Math.random() * ( bounds.height - 2 * radius ) ) + bounds.y;
            Point start = new Point( theX, theY );
            ASprite s = new ASprite( start, radius, bounds, speed );

            int tX = ( 13 ) * 2 * radius;
            int tY = ( 2 + i ) * 2 * radius;
            s.setTarget( new Point( tX + controlPoint.x, tY + controlPoint.y ) );
            s.setColor( Color.WHITE );
            gameEvent( new GameEvent( this, GameEventType.AddBack, s ) );
        }
        // Arms of E
        for ( int i = 0; i < 8; i++ )
        {
            int theX = ( int ) ( Math.random() * ( bounds.width - 2 * radius ) ) + bounds.x;
            int theY = ( int ) ( Math.random() * ( bounds.height - 2 * radius ) ) + bounds.y;
            Point start = new Point( theX, theY );
            ASprite s = new ASprite( start, radius, bounds, speed );

            int tX = ( 14 + i / 3 ) * 2 * radius;
            int tY = ( 2 + i % 3 * 4 ) * 2 * radius;
            if ( tY == 10 * 2 * radius )
                tY = ( 2 + 2 ) * 2 * radius;
            s.setTarget( new Point( tX + controlPoint.x, tY + controlPoint.y ) );
            s.setColor( Color.WHITE );
            gameEvent( new GameEvent( this, GameEventType.AddBack, s ) );
        }

    }

    private void startGame()
    {
        // Ship is padded 10px above and below the bases and the frame
        if ( player == null )
            player = new PlayerShipSprite( new Rectangle( 20, 550, 760, 40 ) );
        if ( scoreSprite == null )
            scoreSprite = new ScoreSprite();
        if ( livesSprite == null )
            livesSprite = new LivesSprite();

        // missileMonitorSprite = new MissileMonitorSprite();

        player.reset();
        scoreSprite.reset();
        livesSprite.setLives( 3 );

        pause = false;
    }

    private void setLevel( int level )
    {
        this.level = level;
        synchronized ( spriteList )
        {
            spriteList.clear();

            // This draws all the bases ^_^
            Rectangle boundingBox = new Rectangle( 20, 480, 760, 50 );
            int baseWidth = 100;
            int baseCount = 3;
            int freeSpace = boundingBox.width - baseWidth * baseCount;
            // The amount of padding on each side
            int padding = freeSpace / ( baseCount + 1 );
            int offset = 0;
            Rectangle r;
            for ( int baseNumber = 0; baseNumber < baseCount; baseNumber++ )
            {
                offset += padding;
                r = new Rectangle( boundingBox.x + offset, boundingBox.y, baseWidth, boundingBox.height );
                spriteList.add( new BaseSprite( r ) );
                offset += baseWidth;
            }


            FleetStrategy strategy = new FleetStrategy( 25, 9, 20 );
            switch ( level )
            {
                case 1:
                    strategy = new FleetStrategy( 10, 5, 20 );
                    break;
                case 2:
                    strategy = new FleetStrategy( 15, 5, 20 );
                    break;
                case 3:
                    strategy = new FleetStrategy( 20, 5, 20 );
                    break;
                case 4:
                    strategy = new FleetStrategy( 25, 5, 20 );
                    break;

                case 5:
                    strategy = new FleetStrategy( 20, 13, 16 );
                    break;

                case 6:
                    strategy = new FleetStrategy( 20, 7, 22 );
                    break;
                case 7:
                    strategy = new FleetStrategy( 20, 7, 24 );
                    break;
                case 8:
                    strategy = new FleetStrategy( 20, 7, 26 );
                    break;

                case 9:
                    strategy = new FleetStrategy( 20, 9, 22 );
                    break;
                case 10:
                case 11:
                    strategy = new FleetStrategy( 25, 8, 22 );
                    break;
                case 12:
                case 13:
                    strategy = new FleetStrategy( 25, 9, 22 );
                    break;
                case 14:
                case 15:
                    strategy = new FleetStrategy( 25, 11, 22 );
                    break;

                default:
                    strategy = new FleetStrategy( 25, 14, 20 );
                    break;
            }

            invader = new InvaderFleetSprite( new Rectangle( 20, 20, 760, 460 ), strategy );

            spriteList.add( player );
            spriteList.add( invader );

            // UI
            spriteList.add( scoreSprite );
            spriteList.add( livesSprite );
            // spriteList.add( missileMonitorSprite );
        }
    }

    public boolean isPaused()
    {
        return pause;
    }

    @Override
    public void keyPressed( KeyEvent ke )
    {
        // GameEventDispatcher dispatcher = GameEventDispatcher.getGameEventDispatcher();
        // gameEvent() works with a little less overhead for functions in the
        // StateMachine

        // State-dependant grabs
        switch ( getState() )
        {
            case Splash:
                switch ( ke.getKeyCode() )
                {
                    case KeyEvent.VK_S:
                        gameEvent( new GameEvent( this, GameEventType.Start, null ) );
                        break;
                    case KeyEvent.VK_H:
                        setHighScoresScreen();
                        break;
                    case KeyEvent.VK_Q:
                    case KeyEvent.VK_ESCAPE:
                        gameEvent( new GameEvent( this, GameEventType.Quit, null ) );
                        break;
                }
                break;
            case HighScores:
                highScoreSprite.keyboardAction( ke );
                break;
            case Menu:
            case Pause:
                switch ( ke.getKeyCode() )
                {
                    case KeyEvent.VK_S:
                        gameEvent( new GameEvent( this, GameEventType.Start, null ) );
                        break;
                    case KeyEvent.VK_P:
                        gameEvent( new GameEvent( this, GameEventType.Pause, null ) );
                        break;
                    case KeyEvent.VK_E:
                        gameEvent( new GameEvent( this, GameEventType.End, null ) );
                        break;
                    case KeyEvent.VK_Q:
                    case KeyEvent.VK_ESCAPE:
                        gameEvent( new GameEvent( this, GameEventType.Quit, null ) );
                        break;

                }
                break;
            case Playing:
                switch ( ke.getKeyCode() )
                {
                    case KeyEvent.VK_P:
                        gameEvent( new GameEvent( this, GameEventType.Pause, null ) );
                        break;
                    case KeyEvent.VK_E:
                        gameEvent( new GameEvent( this, GameEventType.End, null ) );
                        break;
                    default:
                        // Forward to player
                        player.keyboardAction( ke );
                        break;
                    case KeyEvent.VK_Q:
                    case KeyEvent.VK_ESCAPE:
                        gameEvent( new GameEvent( this, GameEventType.Quit, null ) );
                        break;

                }
                break;
        }

        // Global Grabs
        switch ( ke.getKeyCode() )
        {
            // I have esc bound to speech recognition on my setup.
            // For whatever reason, the OS grabs some keyCodes and
            // doesn't share with java
            // In short, Q or Esc Quits
            // case KeyEvent.VK_Q:
            case KeyEvent.VK_ESCAPE:
                gameEvent( new GameEvent( this, GameEventType.Quit, null ) );
                break;
        }
    }

    @Override
    public void keyReleased( KeyEvent ke )
    {
        // State-dependant grabs
        switch ( getState() )
        {
            case Playing:
                switch ( ke.getKeyCode() )
                {
                    default:
                        // Forward to player
                        player.keyboardAction( ke );
                }
        }
    }

    @Override
    public void keyTyped( KeyEvent ke )
    {
        if ( getState() == State.HighScores )
        {
            synchronized ( spriteList )
            {
                for ( Sprite s : spriteList )
                {
                    s.keyboardAction( ke );
                }
            }
        }
    }


    @Override
    public void mouseEntered( MouseEvent arg0 )
    {
    }

    @Override
    public void mouseExited( MouseEvent arg0 )
    {
    }

    @Override
    public void mouseClicked( MouseEvent arg0 )
    {
        if ( getState() == State.Playing )
        {
            player.mouseAction( arg0 );
        }
    }

    @Override
    public void mousePressed( MouseEvent arg0 )
    {
        if ( presentState == State.Playing )
        {
            player.mouseAction( arg0 );
        }
    }

    @Override
    public void mouseReleased( MouseEvent arg0 )
    {
        if ( presentState == State.Playing )
        {
            player.mouseAction( arg0 );
        }
    }

    @Override
    public void mouseDragged( MouseEvent arg0 )
    {
        if ( presentState == State.Playing )
        {
            player.mouseAction( arg0 );
        }
    }

    @Override
    public void mouseMoved( MouseEvent arg0 )
    {
        if ( presentState == State.Playing )
        {
            player.mouseAction( arg0 );
        }
    }

    @Override
    public void gameEvent( GameEvent ge )
    {
        synchronized ( eventList )
        {
            eventList.add( ge );
            eventList.notify();
        }
    }

    private void setState( State newState )
    {
        previousState = presentState;
        presentState = newState;
        System.out.println( "OldState: " + previousState + " NewState: " + presentState );
        

        // if(backgroundAudio !=null){
        // backgroundAudio.close();
        // }

        if ( backgroundAudio != null )
        switch ( previousState )
        {
            case Splash:
                    // backgroundAudio.close();
                    backgroundAudio = null;
                break;
        }

        String audioToLoad = null;
        switch(presentState){
            case Splash:
                audioToLoad = new String( "sounds/startup_hs.wav" );
                break;
        }

        if ( audioToLoad != null )
        {
            try
            {
                backgroundAudio = new AudioSample( this, audioToLoad );
                System.out.println( audioToLoad + ": " + backgroundAudio.getTime() );
                backgroundAudio.play();
            }
            catch ( IOException e )
            {
                e.printStackTrace();
            }
            catch ( UnsupportedAudioFileException e )
            {
                e.printStackTrace();
            }
            catch ( LineUnavailableException e )
            {
                e.printStackTrace();
            }

        }
    }

    private State getState()
    {
        return presentState;
    }

    private State getPrevState()
    {
        return previousState;
    }

}
