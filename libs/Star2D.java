package libs;

import java.awt.*;
import java.awt.geom.*;

/**
 * A five pointed star. Looks best when filled. The Star class is not complete. It is missing the contains() methods, which currently
 * returns false.
 * 
 * @author williamhooper $Id: Star2D.java,v 1.3 2010/09/06 06:21:09 williamhooper Exp $
 * 
 *         Copyright 2008 William Hooper
 * 
 *         This library is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as
 *         published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version.
 * 
 *         This library is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty
 *         of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * 
 *         You should have received a copy of the GNU General Public License. If not, see
 *         <http://www.gnu.org/licenses/>.
 */
public class Star2D implements Shape
{

    /**
     * Star shape iterator
     * 
     * @author williamhooper
     * 
     */
    class StarIterator implements PathIterator
    {
        int done;
        Double dtheta = Math.PI * 0.8;
        double flatness;
        final int MAX_POINTS = 6;
        double cx, cy;
        Double theta = -Math.PI / 2;
        AffineTransform transform;

        /**
         * Constructor
         * 
         * @param transform
         * @param flatness
         */
        public StarIterator( AffineTransform transform, double flatness )
        {
            this.transform = transform;
            this.flatness = flatness;
            this.done = MAX_POINTS;

            cx = x + ( width / 2 );
            cy = y + ( height / 2 );
        }

        /*
         * (non-Javadoc)
         * 
         * @see java.awt.geom.PathIterator#currentSegment(double[])
         */
        public int currentSegment( double [ ] coords )
        {
            coords[ 0 ] = ( cx + width * Math.cos( theta ) / 2 );
            coords[ 1 ] = ( cy + height * Math.sin( theta ) / 2 );

            if ( transform != null )
                transform.transform( coords, 0, coords, 0, 1 );

            if ( done == MAX_POINTS )
            {
                return SEG_MOVETO;
            }
            else
            {
                return SEG_LINETO;
            }
        }

        /*
         * (non-Javadoc)
         * 
         * @see java.awt.geom.PathIterator#currentSegment(float[])
         */
        public int currentSegment( float [ ] coords )
        {
            coords[ 0 ] = ( float ) ( cx + ( width * Math.cos( theta ) ) / 2 );
            coords[ 1 ] = ( float ) ( cy + ( height * Math.sin( theta ) ) / 2 );

            if ( transform != null )
                transform.transform( coords, 0, coords, 0, 1 );

            if ( done == MAX_POINTS )
            {
                return SEG_MOVETO;
            }
            else
            {
                return SEG_LINETO;
            }
        }

        /*
         * (non-Javadoc)
         * 
         * @see java.awt.geom.PathIterator#getWindingRule()
         */
        public int getWindingRule()
        {
            return WIND_NON_ZERO;
        }

        /*
         * (non-Javadoc)
         * 
         * @see java.awt.geom.PathIterator#isDone()
         */
        public boolean isDone()
        {
            return ( done == 0 );
        }

        /*
         * (non-Javadoc)
         * 
         * @see java.awt.geom.PathIterator#next()
         */
        public void next()
        {
            if ( done == 0 )
                return;
            done--;
            theta += dtheta;
        }
    }

    public double height, width;
    public double x, y;

    /**
     * Constructor
     * 
     */
    public Star2D( )
    {
        super();
    }

    /**
     * Constructor
     * 
     * @param x
     * @param y
     * @param w
     * @param h
     */
    public Star2D( double x, double y, double w, double h )
    {
        super();
        this.x = x;
        this.y = y;
        this.width = w;
        this.height = h;
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.awt.Shape#contains(double, double)
     */
    public boolean contains( double x, double y )
    {
        return false;
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.awt.Shape#contains(double, double, double, double)
     */
    public boolean contains( double x, double y, double w, double h )
    {
        return false;
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.awt.Shape#contains(java.awt.geom.Point2D)
     */
    public boolean contains( Point2D p )
    {
        return false;
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.awt.Shape#contains(java.awt.geom.Rectangle2D)
     */
    public boolean contains( Rectangle2D r )
    {
        return false;
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.awt.Shape#getBounds()
     */
    public Rectangle getBounds()
    {
        return new Rectangle( ( int ) ( x ), ( int ) ( y ), ( int ) ( width ), ( int ) ( height ) );
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.awt.Shape#getBounds2D()
     */
    public Rectangle2D getBounds2D()
    {
        return new Rectangle2D.Double( x, y, width, height );
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.awt.Shape#getPathIterator(java.awt.geom.AffineTransform)
     */
    public PathIterator getPathIterator( AffineTransform at )
    {
        return new StarIterator( at, ( height * width ) / 500.0 );
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.awt.Shape#getPathIterator(java.awt.geom.AffineTransform, double)
     */
    public PathIterator getPathIterator( AffineTransform at, double flatness )
    {
        return new StarIterator( at, flatness );
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.awt.Shape#intersects(double, double, double, double)
     */
    public boolean intersects( double x, double y, double w, double h )
    {
        Shape approx = new Rectangle2D.Double( x, y, width, height );
        return approx.intersects( x, y, w, h );
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.awt.Shape#intersects(java.awt.geom.Rectangle2D)
     */
    public boolean intersects( Rectangle2D r )
    {
        return intersects( r.getX(), r.getY(), r.getWidth(), r.getHeight() );
    }
}