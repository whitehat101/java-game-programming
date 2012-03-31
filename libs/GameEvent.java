package libs;

/**
 * Gane action event class. Used to pass async messages back to the main
 * 
 * @author williamhooper $Id: GameEvent.java,v 1.2 2011/02/12 18:42:26 williamhooper Exp $
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

public class GameEvent
{

    public enum GameEventType
    {
        AddFront, AddBack, Remove, Score, Life, Start, Level, End, Quit, Pause, LevelComplete
    };

    private GameEventType type;
    private Object attachment;
    private Object source;

    /**
     * Constructor
     * 
     * @param source
     * @param type
     * @param attachment
     */
    public GameEvent( Object source, GameEventType type, Object attachment )
    {
        this.source = source;
        this.type = type;
        this.attachment = attachment;
    }

    /**
     * Get the event type
     * 
     * @return
     */
    public GameEventType getType()
    {
        return type;
    }

    /**
     * Get the event attachment
     * 
     * @return
     */
    public Object getAttachment()
    {
        return attachment;
    }

    public Object getSource()
    {
        return source;
    }
}
