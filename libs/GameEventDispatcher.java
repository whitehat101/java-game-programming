package libs;

import java.util.ArrayList;

/**
 * Game Event Dispatch
 * 
 * @author williamhooper $Id: GameEventDispatcher.java,v 1.2 2011/02/12 18:43:22 williamhooper Exp $
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
 * 
 */

public class GameEventDispatcher
{
    /**
     * List of game action listeners
     */
    private static ArrayList< GameEventListener > _gameEventListeners = new ArrayList< GameEventListener >();
    private static GameEventDispatcher _instance;

    /**
     * Private constructor
     * 
     */
    private GameEventDispatcher( )
    {
        /**
         * no code required
         */
    }

    /**
     * Get the game event dispatcher
     * 
     * @return
     */
    public static GameEventDispatcher getGameEventDispatcher()
    {
        if ( _instance == null )
            /**
             * it's ok, we can call this constructor
             */
            _instance = new GameEventDispatcher();
        return _instance;
    }

    @Override
    public Object clone() throws CloneNotSupportedException
    {
        throw new CloneNotSupportedException();
    }

    /**
     * Add a game action listener
     * 
     * @param gel
     */
    public synchronized void addGameEventListener( GameEventListener gel )
    {
        _gameEventListeners.add( gel );
    }

    /**
     * Dispatch a game action event
     * 
     * @param event
     * @param attachment
     */
    public synchronized void dispatchEvent( GameEvent event )
    {
        for ( GameEventListener listener : _gameEventListeners )
        {
            listener.gameEvent( event );
        }
    }
}
