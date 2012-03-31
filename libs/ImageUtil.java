package libs;

import java.awt.Graphics;
import java.awt.GraphicsConfiguration;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.Image;
import java.awt.Transparency;
import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.awt.image.PixelGrabber;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;

/**
 * Class to load an image from the file system
 * 
 * @author williamhooper $Id: ImageUtil.java,v 1.1 2011/02/07 07:10:25 williamhooper Exp $
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

public class ImageUtil
{
    /**
     * $Id: ImageUtil.java,v 1.1 2011/02/07 07:10:25 williamhooper Exp $
     */

    /**
     * Returns an ImageIcon, or null if the path was invalid.
     * 
     * @throws IOException
     * 
     */
    public static Image createImage( Object obj, String path ) throws IOException
    {
        java.net.URL imgURL = obj.getClass().getResource( path );
        if ( imgURL != null )
        {
            return ImageIO.read( imgURL );
        }
        else
        {
            throw new IOException( "Couldn't find file: " + path );
        }
    }

    /**
     * Convert an image into a buffered, compatible image
     * 
     * @param image
     * @return
     */
    public static BufferedImage toBufferedImage( Image image )
    {
        if ( image instanceof BufferedImage )
        {
            return ( BufferedImage ) image;
        }

        /**
         * This code ensures that all the pixels in the image are loaded
         */
        image = new ImageIcon( image ).getImage();

        /**
         * Determine if the image has transparent pixels; for this method's implementation, see e661 Determining If an Image Has
         * Transparent Pixels
         */
        boolean hasAlpha = hasAlpha( image );

        /**
         * Create a buffered image with a format that's compatible with the screen
         */
        BufferedImage bimage = null;
        GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();

        /**
         * Determine the type of transparency of the new buffered image
         */
        int transparency = Transparency.OPAQUE;
        if ( hasAlpha )
        {
            transparency = Transparency.BITMASK;
        }

        /**
         * Create the buffered image
         */
        GraphicsDevice gs = ge.getDefaultScreenDevice();
        GraphicsConfiguration gc = gs.getDefaultConfiguration();
        bimage = gc.createCompatibleImage( image.getWidth( null ), image.getHeight( null ), transparency );

        if ( bimage == null )
        {
            /**
             * Create a buffered image using the default color model
             */
            int type = BufferedImage.TYPE_INT_RGB;
            if ( hasAlpha )
            {
                type = BufferedImage.TYPE_INT_ARGB;
            }
            bimage = new BufferedImage( image.getWidth( null ), image.getHeight( null ), type );
        }

        /**
         * Get the graphics context
         */
        Graphics g = bimage.createGraphics();

        /**
         * Paint the image onto the buffered image
         */
        g.drawImage( image, 0, 0, null );
        g.dispose();

        return bimage;
    }

    /**
     * Convert an portion of an image into a buffered, compatible image
     * 
     * @param image
     * @param sx1
     * @param sy1
     * @param sx2
     * @param sy2
     * @return
     */
    public static BufferedImage toBufferedImage( Image image, int sx1, int sy1, int sx2, int sy2 )
    {
        int width = ( sx2 - sx1 );
        int height = ( sy2 - sy1 );

        /**
         * This code ensures that all the pixels in the image are loaded
         */
        image = new ImageIcon( image ).getImage();

        /**
         * Determine if the image has transparent pixels; for this method's implementation, see e661 Determining If an Image Has
         * Transparent Pixels
         */
        boolean hasAlpha = hasAlpha( image );

        /**
         * Create a buffered image with a format that's compatible with the screen
         */
        BufferedImage bimage = null;
        GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();

        /**
         * Determine the type of transparency of the new buffered image
         */
        int transparency = Transparency.OPAQUE;
        if ( hasAlpha )
        {
            transparency = Transparency.BITMASK;
        }

        /**
         * Create the buffered image
         */
        GraphicsDevice gs = ge.getDefaultScreenDevice();
        GraphicsConfiguration gc = gs.getDefaultConfiguration();
        bimage = gc.createCompatibleImage( width, height, transparency );

        if ( bimage == null )
        {
            /**
             * Create a buffered image using the default color model
             */
            int type = BufferedImage.TYPE_INT_RGB;
            if ( hasAlpha )
            {
                type = BufferedImage.TYPE_INT_ARGB;
            }
            bimage = new BufferedImage( image.getWidth( null ), image.getHeight( null ), type );
        }

        /**
         * Get the graphics context
         */
        Graphics g = bimage.createGraphics();

        /**
         * Paint the image onto the buffered image
         */
        g.drawImage( image, 0, 0, width, height, sx1, sy1, sx2, sy2, null );
        g.dispose();

        return bimage;
    }

    /**
     * Return true if the image has an alph channel
     * 
     * @param image
     * @return
     */
    public static boolean hasAlpha( Image image )
    {
        // If buffered image, the color model is readily available
        if ( image instanceof BufferedImage )
        {
            BufferedImage bimage = ( BufferedImage ) image;
            return bimage.getColorModel().hasAlpha();
        }

        // Use a pixel grabber to retrieve the image's color model;
        // grabbing a single pixel is usually sufficient
        PixelGrabber pg = new PixelGrabber( image, 0, 0, 1, 1, false );
        try
        {
            pg.grabPixels();
        }
        catch ( InterruptedException e )
        {
        }

        // Get the image's color model
        ColorModel cm = pg.getColorModel();
        return cm.hasAlpha();
    }
}