package graphics;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;

/**
 * Class to manage and draw tile sheets
 * 
 * @author williamhooper $Id: TileSheet.java,v 1.1 2011/02/07 07:10:25 williamhooper Exp $
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
public class TileSheet
{
    private BufferedImage image;
    private ArrayList< BufferedImage > imageList;
    private int tileWidth;
    private int tileHeight;

    /**
     * Constructor
     * 
     * @param image
     * @param width
     * @param height
     */
    public TileSheet( BufferedImage image, int width, int height )
    {
        tileWidth = width;
        tileHeight = height;

        this.image = image;
        initialize();
    }

    /**
     * Constructor
     * 
     * @param obj
     * @param file
     * @param width
     *            Width of a tile in the tileset
     * @param height
     *            Height of s tile in the tileset
     * @throws IOException
     */
    public TileSheet( Object obj, String file, int width, int height ) throws IOException
    {
        tileWidth = width;
        tileHeight = height;

        image = ImageUtil.toBufferedImage( ImageUtil.createImage( obj, file ) );
        initialize();
    }

    /**
     * Initialize the tile list
     */
    private void initialize()
    {
        int numX = image.getWidth() / tileWidth;
        int numY = image.getHeight() / tileHeight;

        imageList = new ArrayList< BufferedImage >();

        for ( int y = 0; y < numY; y++ )
        {
            for ( int x = 0; x < numX; x++ )
            {
                imageList.add( image.getSubimage( ( x * tileWidth ), ( y * tileHeight ), tileWidth, tileHeight ) );
            }
        }

    }

    /**
     * Get a tile image
     * 
     * @param index
     * @return
     */
    public BufferedImage getTile( int index )
    {
        return imageList.get( index );
    }

    /**
     * Return the number of tiles
     * 
     * @return
     */
    public int getNumTiles()
    {
        return imageList.size();
    }
}