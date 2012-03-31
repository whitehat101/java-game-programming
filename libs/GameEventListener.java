package libs;

import java.util.EventListener;

/**
 * Game action event listener
 * 
 * @author williamhooper $Id: GameEventListener.java,v 1.1 2011/02/07 07:10:26 williamhooper Exp $
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

public interface GameEventListener extends EventListener
{
    /**
     * Receive a game action event
     * 
     * @param event
     * @param attachment
     */
    public void gameEvent( GameEvent ge );

}
