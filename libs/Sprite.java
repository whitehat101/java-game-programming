package libs;

import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;

/**
 * Sprite class to manage an animation sequence of multiple images
 * 
 * @author williamhooper $Id: Sprite.java,v 1.1 2011/02/07 07:10:25 williamhooper Exp $
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

public interface Sprite
{
    /**
     * Determine if the passed Sprite obj collided with this object.
     * 
     * @param obj
     */
    public abstract void checkCollision( Sprite obj );

    /**
     * Draw method
     * 
     * @param g
     */
    public abstract void draw( Graphics g );

    /**
     * Check to see if the shape intersects with our bounding boxes
     * 
     * @param boundingBox
     */
    public abstract Rectangle intersects( Rectangle boundingBox );

    /**
     * Receive a keyboard event. This method is typically overridden.
     * 
     * @param ke
     */
    public abstract void keyboardAction( KeyEvent ke );

    /**
     * Receive a mouse event.
     * 
     * @param me
     */
    public abstract void mouseAction( MouseEvent me );

    /**
     * Update the sprite's state.
     * 
     */
    public abstract void update();

}
