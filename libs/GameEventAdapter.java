package libs;

/**
 * An abstract adapter class for receiving game action events. The methods in this class are empty. This class exists as convenience for
 * creating listener objects.
 * 
 * @author williamhooper $Id: GameEventAdapter.java,v 1.1 2011/02/07 07:10:25 williamhooper Exp $
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

public class GameEventAdapter implements GameEventListener
{
    /**
     * Invoked when a game action event occurs
     */
    public void gameEvent( GameEvent ge )
    {

    }
}
