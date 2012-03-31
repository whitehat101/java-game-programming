package project4;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.io.IOException;

import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;

import libs.AudioSample;
import libs.AudioSample.AudioState;
import libs.GameEvent;
import libs.GameEvent.GameEventType;
import libs.GameEventDispatcher;
import libs.ImageUtil;
import libs.Sprite;

public class PlayerShipSprite implements Sprite
{
    final Color myColor = Color.WHITE;
    private Rectangle boundingBox;
    private Rectangle ship;
    private boolean fireOnUpdate;
    private long fireTimer;
    private GameEventDispatcher dispatcher;
    private final int CONCURRENT_SOUNDS = 2;
    private AudioSample [ ] audioSamples = new AudioSample [ CONCURRENT_SOUNDS ];
    private AudioSample deathSample;

    final private int maxSpeed = 10;
    final int shipWidth = 40;

    private int mouseX;
    private boolean moveRight;
    private boolean moveLeft;

    private boolean autoFIRE;
    private Image shipImage;
    private int dead;
    private boolean freshMouseX;
    private int respawnProtection;

    public PlayerShipSprite( Rectangle boundingBox )
    {
        this.boundingBox = boundingBox;

        reset();
        fireTimer = System.nanoTime();
        dispatcher = GameEventDispatcher.getGameEventDispatcher();

        try
        {
            shipImage = ImageUtil.createImage( this, "Trogdor_40.png" );
            // shipDeadImage = ImageUtil.createImage( this, "ship.png" );
        }
        catch ( IOException e )
        {
            System.out.println( "Failed to load ship sprites." );
        }

        System.out.print( "PlayerShipSprite spawn @(" + boundingBox + ")\n" );
    }

    public void reset()
    {
        dead = 0;
        respawnProtection = 0;
        mouseX = boundingBox.x + ( boundingBox.width - shipWidth ) / 2;
        ship = null;// GC hint
        ship = new Rectangle( mouseX, boundingBox.y, shipWidth, boundingBox.height );
        fireOnUpdate = false;
        autoFIRE = false;
        moveLeft = false;
        moveRight = false;
    }

    @Override
    public void checkCollision( Sprite obj )
    {
        if ( dead > 0 )
            return;

        if ( obj instanceof MissileSprite )
        {
            if ( ( ( MissileSprite ) obj ).getParent() instanceof PlayerShipSprite )
                return;

            // Rectangle rectangle = new Rectangle(drawPoint.x, drawPoint.y,
            // widthheight.width, widthheight.height);
            Rectangle result = obj.intersects( ship );
            if ( result != null )
            {
                // System.out.println("InvaderSprite hit by MissleSprite");
                // remove the missile
                dispatcher.dispatchEvent( new GameEvent( this, GameEventType.Remove, obj ) );
                if ( respawnProtection <= 0 )
                {
                    dispatcher.dispatchEvent( new GameEvent( this, GameEventType.Life, new Integer( -1 ) ) );
                    dispatcher.dispatchEvent( new GameEvent( this, GameEventType.Score, new Integer( -50 ) ) );
                    dead = 30;
                    respawnProtection = 45;

                    if ( deathSample == null )
                    {
                        try
                        {
                            deathSample = new AudioSample( this, "sounds/hey stupid.wav" );
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
                    if ( deathSample.getState() == AudioState.DONE )
                    {
                        double pan = 2.0 * ( ship.x - boundingBox.x - boundingBox.width / 2 ) / boundingBox.width;
                        deathSample.setPan( pan );
                        System.out.println( "sounds/hey stupid.wav: " + deathSample.getTime() + " " + pan );
                        deathSample.play();
                    }

                }
            }
        }

    }

    @Override
    public void draw( Graphics g )
    {
        // g.setColor( myColor );
        // g.drawRect( boundingBox.x, boundingBox.y, boundingBox.width,
        // boundingBox.height );

        if ( dead > 0 )
        {
            // g.drawImage( shipDeadImage, ship.x, ship.y, null );
            g.setColor( Color.YELLOW );
            g.fillRect( ship.x, ship.y, ship.width, ship.height );
            return;
        }

        // Draw normal ship
        // g.setColor( Color.BLUE );
        // g.fillRect( ship.x, ship.y, ship.width, ship.height );
        g.drawImage( shipImage, ship.x, ship.y, null );

        if ( respawnProtection > 0 )
        {
            g.setColor( Color.RED );
            g.drawRect( ship.x - 1, ship.y - 1, ship.width + 2, ship.height + 2 );
        }

    }

    @Override
    public Rectangle intersects( Rectangle boundingBox )
    {
        return boundingBox.intersects( this.boundingBox ) ? boundingBox.intersection( this.boundingBox ) : null;
    }

    @Override
    public void keyboardAction( KeyEvent ke )
    {
        switch ( ke.getKeyCode() )
        {
            case KeyEvent.VK_SPACE:
            case KeyEvent.VK_UP:
            case KeyEvent.VK_W:
                fireOnUpdate = true;
                if ( ke.getID() == KeyEvent.KEY_PRESSED )
                {
                    autoFIRE = true;
                }
                else if ( ke.getID() == KeyEvent.KEY_RELEASED )
                {
                    autoFIRE = false;
                }
                break;
            case KeyEvent.VK_RIGHT:
            case KeyEvent.VK_D:
                if ( ke.getID() == KeyEvent.KEY_PRESSED )
                {
                    moveRight = true;
                }
                else if ( ke.getID() == KeyEvent.KEY_RELEASED )
                {
                    moveRight = false;
                }
                break;
            case KeyEvent.VK_LEFT:
            case KeyEvent.VK_A:
                if ( ke.getID() == KeyEvent.KEY_PRESSED )
                {
                    moveLeft = true;
                }
                else if ( ke.getID() == KeyEvent.KEY_RELEASED )
                {
                    moveLeft = false;
                }
                break;
        }
    }

    @Override
    public void mouseAction( MouseEvent me )
    {

        if ( me.getID() == MouseEvent.MOUSE_PRESSED )
        {
            autoFIRE = true;
        }
        if ( me.getID() == MouseEvent.MOUSE_RELEASED )
        {
            autoFIRE = false;
        }

        if ( me.getID() == MouseEvent.MOUSE_MOVED || me.getID() == MouseEvent.MOUSE_DRAGGED )
        {
            freshMouseX = true;
            mouseX = me.getX() - ship.width / 2;// Center the object on
                                                // the mouse
        }

        if ( me.getID() == MouseEvent.MOUSE_CLICKED )
        {
            fireOnUpdate = true;
        }

    }

    @Override
    public void update()
    {
        // It sucks to be dead
        if ( dead > 0 )
        {
            dead--;
            return;
        }

        if ( respawnProtection > 0 )
            respawnProtection--;

        GameEventDispatcher dispatcher;
        final long fireRate = 500000000l;
        if ( fireOnUpdate || autoFIRE )
        {
            if ( System.nanoTime() - fireTimer > fireRate )
            {
                dispatcher = GameEventDispatcher.getGameEventDispatcher();
                // MissileSprite ms = new MissileSprite( this, new Point( ( int
                // ) ship.getCenterX(), ( int ) ship.getMinY() ), -10 );
                MissileSprite ms = new MissileSprite( this, new Point( ship.x + 12, ( int ) ship.getMinY() ), boundingBox, -12 );
                dispatcher.dispatchEvent( new GameEvent( this, GameEventType.AddFront, ms ) );
                fireTimer = System.nanoTime();

                // Plays a sound if there is a slot open
                // Else does not start a new sound
                for ( int idx = 0; idx < CONCURRENT_SOUNDS; idx++ )
                {
                    if ( audioSamples[ idx ] == null )
                    {
                        try
                        {
                            audioSamples[ idx ] = new AudioSample( this, "sounds/typewriter_click.wav" );
                            // audioSamples[ idx ].play();
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
                    if ( audioSamples[ idx ].getState() == AudioState.DONE )
                    {
                        double pan = 2.0 * ( ship.x - boundingBox.x - boundingBox.width / 2 ) / boundingBox.width;
                        audioSamples[ idx ].setPan( pan );
                        System.out.println( "sounds/paper_tearing.wav: " + audioSamples[ idx ].getTime() + " " + pan );
                        audioSamples[ idx ].play();
                        break;
                    }
                }

            }
            fireOnUpdate = false;
        }

        // Movement
        if ( moveLeft || freshMouseX && mouseX < ship.x )
        {
            if ( freshMouseX && ship.x - mouseX < maxSpeed )
            {
                freshMouseX = false;
                ship.x = mouseX;
            }
            else
            {
                ship.x -= maxSpeed;
            }
        }
        else if ( moveRight || freshMouseX && mouseX > ship.x )
        {
            if ( freshMouseX && mouseX - ship.x < maxSpeed )
            {
                freshMouseX = false;
                ship.x = mouseX;
            }
            else
            {
                ship.x += maxSpeed;
            }
        }

        // Constrain to bound

        // CheckLeft
        if ( boundingBox.x > ship.x )
        {
            ship.x = boundingBox.x;
        }
        // CheckRight
        else if ( boundingBox.x + boundingBox.width - ship.width < ship.x )
        {
            ship.x = boundingBox.x + boundingBox.width - ship.width;
        }
    }
}
