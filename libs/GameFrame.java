package libs;

import java.awt.*;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;

/**
 * This is the game frame handler that manages the frame graphics
 * 
 * @author williamhooper $Id: GameFrame.java,v 1.2 2011/02/09 07:31:49 williamhooper Exp $
 * 
 *         Copyright 2008 William Hooper
 * 
 *         This library is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as
 *         published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version.
 * 
 *         This library is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty
 *         of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * 
 *         You should have received a copy of the GNU General Public License. If not, see <http://www.gnu.org/licenses/>.
 */

public class GameFrame extends Frame
{

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private BufferStrategy bufferStrategy;
    private Graphics currentGraphics;

    /**
     * Constructor
     * 
     * @param width
     * @param height
     */
    public GameFrame( int width, int height )
    {
        this( width, height, false );
    }

    /**
     * Constructor
     * 
     * @param width
     * @param height
     * @param cursor
     */
    public GameFrame( int width, int height, boolean cursor )
    {
        /**
         * Turn off auto repaint and decorations
         */
        setIgnoreRepaint( true );
        setUndecorated( true );

        /**
         * Get focus so events come to here
         */
        setFocusable( true );
        requestFocus();

        /**
         * Set the graphics frame width and height
         */
        setSize( new Dimension( width, height ) );

        /**
         * Get the size of the monitor screen
         */
        Toolkit toolkit = Toolkit.getDefaultToolkit();
        Dimension screenSize = toolkit.getScreenSize();

        /**
         * Calculate the frame location in center of screen and set the new frame location
         */
        int x = ( screenSize.width - getWidth() ) / 2;
        int y = ( screenSize.height - getHeight() ) / 2;
        setLocation( x, y );

        /**
         * Hide the cursor if requested
         */
        if ( !cursor )
        {
            hideCursor();
        }

        /**
         * Make the frame visible
         */
        setVisible( true );

        /**
         * Create a double buffer strategy
         */
        createBufferStrategy( 2 );
        bufferStrategy = getBufferStrategy();
    }

    /**
     * Creates an image compatible with the current display
     * 
     * @param w
     * @param h
     * @param transparancy
     * @return
     */
    public BufferedImage getCompatibleImage( int w, int h, int transparancy )
    {
        GraphicsConfiguration gc = getGraphicsConfiguration();
        return gc.createCompatibleImage( w, h, transparancy );
    }

    /**
     * Get the current graphics. The application must dispose of the graphics object.
     * 
     */
    public Graphics getCurrentGraphics()
    {
        currentGraphics = bufferStrategy.getDrawGraphics();
        return currentGraphics;
    }

    /**
     * Update the current graphics screen
     */
    public void updateGraphics()
    {
        /**
         * If the buffer got lost (graphics memory reallocated before we were done with it) then don't show.
         */
        if ( !bufferStrategy.contentsLost() )
        {
            bufferStrategy.show();
        }

        /**
         * Dispose of the graphics like we were asked to do
         */
        if ( currentGraphics != null )
        {
            currentGraphics.dispose();
        }

        /**
         * Make sure image is flushed to the screen
         */
        Toolkit.getDefaultToolkit().sync();

    }

    /**
     * Hide the cursor
     */
    private void hideCursor()
    {
        /**
         * Hide the mouse cursor
         */
        Image cursorImage = Toolkit.getDefaultToolkit().getImage( "xparent.gif" );
        Cursor blankCursor = Toolkit.getDefaultToolkit().createCustomCursor( cursorImage, new Point( 0, 0 ), "" );
        setCursor( blankCursor );
    }
}