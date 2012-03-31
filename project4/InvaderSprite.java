package project4;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;

import libs.AudioSample;
import libs.AudioSample.AudioState;
import libs.GameEvent;
import libs.GameEvent.GameEventType;
import libs.GameEventDispatcher;
import libs.Sprite;

public class InvaderSprite implements Sprite
{
    final Rectangle boundingBox;// We should not change this
    Rectangle invader;
    private int row;
    private int col;
    private float padding;
//    private Color color;
    private int dead;// The remaining number of frames
    private GameEventDispatcher dispatcher;
    private long fireTimer;
    private MissileSprite myMissle;
    private InvaderSheet invaderSheet;
    private BufferedImage bufferedFrame;
    static private AudioSample deathSample;

    private final static int CONCURRENT_SOUNDS = 8;
    static private AudioSample [ ] audioSamples = new AudioSample [ CONCURRENT_SOUNDS ];


    public InvaderSprite( int col, int row, Dimension widthheight, Rectangle boundingBox, InvaderSheet t )
    {
        this.boundingBox = boundingBox;
        padding = 2.2f;
//        color = Color.WHITE;
        dead = -1;
        dispatcher = GameEventDispatcher.getGameEventDispatcher();
        this.row = row;
        this.col = col;
        invader = new Rectangle( widthheight );

        invaderSheet = t;

    }

    @Override
    public void checkCollision( Sprite obj )
    {
        // This signals the death animation, collisions should not occur on dead
        // invaders
        if ( dead > 0 )
            return;

        if(obj instanceof MissileSprite){
            if ( ( ( MissileSprite ) obj ).getParent() instanceof InvaderSprite )
                return;

            // Rectangle rectangle = new Rectangle(drawPoint.x, drawPoint.y,
            // widthheight.width, widthheight.height);
            Rectangle result = obj.intersects( invader );
            if(result != null){
                // System.out.println("InvaderSprite hit by MissleSprite");
                // remove the missile
                dispatcher.dispatchEvent( new GameEvent( this, GameEventType.Remove, obj ) );
                dispatcher.dispatchEvent( new GameEvent( this, GameEventType.Score, ( Integer ) 10 ) );
                // Notify FleetSprite of death in 5 updates, after the death
                // animation has animated
                dead = 5;
                if ( deathSample == null )
                {
                    try
                    {
                        deathSample = new AudioSample( this, "sounds/Startled female.wav" );
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
                    double pan = 2.0 * ( invader.x - boundingBox.x - boundingBox.width / 2 ) / boundingBox.width;
                    deathSample.setPan( pan );
                    System.out.println( "sounds/Startled female.wav: " + deathSample.getTime() + " " + pan );
                    deathSample.play();
                }

            }
        }

        // If a crash
        if ( obj instanceof BaseSprite || obj instanceof PlayerShipSprite )
        {
            Rectangle result = obj.intersects( invader );
            if ( result != null )
            {
                // dispatcher.dispatchEvent( new GameEvent( this,
                // GameEventType.End, new Integer( 1 ) ) );
                dispatcher.dispatchEvent( new GameEvent( this, GameEventType.End, null ) );
            }
        }
    }

    @Override
    public void draw( Graphics g )
    {
//        g.setColor(color);
//        g.fillRect( boundingBox.x, boundingBox.y, boundingBox.width, boundingBox.height );
        g.drawImage( bufferedFrame, invader.x, invader.y, null );
    }

    @Override
    public Rectangle intersects( Rectangle boundingBox )
    {
        return null;
    }

    @Override
    public void keyboardAction( KeyEvent ke )
    {
    }

    @Override
    public void mouseAction( MouseEvent me )
    {
    }

    @Override
    public void update()
    {
        final long fireRate = ( long ) 2500;// 2.5 Seconds, in milliseconds
        if ( System.currentTimeMillis() - fireTimer > fireRate )
        {
            if ( Math.random() > .6f )
            {
                myMissle = new MissileSprite( this, new Point( ( int ) invader.getCenterX(), ( int ) invader.getCenterY() ),
                        boundingBox, 10 );
                dispatcher.dispatchEvent( new GameEvent( this, GameEventType.AddBack, myMissle ) );
                
                // Plays a sound if there is a slot open
                // Else does not start a new sound
                for ( int idx = 0; idx < CONCURRENT_SOUNDS; idx++ )
                {
                    if ( audioSamples[ idx ] == null )
                    {
                        try
                        {
                            audioSamples[ idx ] = new AudioSample( this, "sounds/phasesr2.wav" );
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
                        double pan = 2.0 * ( invader.x - boundingBox.x - boundingBox.width / 2 ) / boundingBox.width;
                        audioSamples[ idx ].setPan( pan );
                        System.out.println( "sounds/phasesr2.wav : " + audioSamples[ idx ].getTime() + " " + pan );
                        audioSamples[ idx ].play();
                        break;
                    }
                }

            }
            fireTimer = System.currentTimeMillis();
        }

        int frame;
        if ( dead != -1 )
        {
            dead--;
            frame = invaderSheet.EXPLOSION_FRAME + ( invaderSheet.ROW_LENGTH * ( row % 3 ) );
        }
        else
        {
            frame = ( invader.x / 64 % 2 ) + ( invaderSheet.ROW_LENGTH * ( row % 3 ) );
        }
        bufferedFrame = invaderSheet.getTile( frame );
    }

    public void setFleetPoint( Point invaderFleet )
    {
        
//        this.invaderFleet = invaderFleet;
        invader.x = ( int ) ( invaderFleet.x + col * invader.width * padding );
        invader.y = ( int ) ( invaderFleet.y + row * invader.height * padding );
    }
    
    public Rectangle getBox()
    {
        return ( Rectangle ) invader.clone();
    }

    public boolean isDead()
    {
        return dead == 0;
    }

}
