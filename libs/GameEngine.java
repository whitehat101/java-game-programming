package libs;

/**
 * Threaded game engine, with support for game events and collisions
 * 
 * @author williamhooper $Id: GameEngine.java,v 1.2 2011/02/09 07:13:53 williamhooper Exp $
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

public abstract class GameEngine implements Runnable
{
    /**
     * Number of frames that can be skipped in any one animation loop
     */
    private static int MAX_FRAME_SKIPS = 2;

    /**
     * Number of frames per second
     */
    private final int DEFAULT_FPS = 30;

    /**
     * Desired time per rendered frame
     */
    private final long framePeriod = ( 1000 / DEFAULT_FPS ) * 1000000L;

    /**
     * game thread object
     */
    private Thread gameThread;

    /**
     * Number of frames with a delay of 0 ms before the animation thread yields to other running threads.
     */
    private final int NO_DELAYS_PER_YIELD = 16;

    /**
     * Thread is running flag
     */
    private boolean running;

    /**
     * Check for collisions method
     */
    abstract public void collisions();

    /**
     * Update the game state
     */
    abstract public void update();

    /**
     * Actively paint the screen. Rather than call repaint(), we'll do the work ourselves. This allows us to have greater control over
     * the timing.
     */
    abstract public void draw();

    /**
     * Paint the picture onto the offscreen buffer
     */
    abstract public void render();

    /**
     * Return true if the thread is running
     * 
     * @return
     */
    public boolean isRunning()
    {
        return running;
    }

    /**
     * The run method is part of the Runnable interface and needs to be implemented. It goes through an update-render-draw loop to drive
     * the program forward.
     */
    public void run()
    {
        long beforeTime, afterTime, timeDiff, sleepTime;
        long overSleepTime = 0L;
        int noDelays = 0;
        long excess = 0L;

        running = true;

        while ( running )
        {
            beforeTime = System.nanoTime();

            /**
             * Check for collisions among the game objects
             */
            collisions();

            /**
             * Update all the game objects
             */
            update();

            /**
             * Render all the game objects onto the offscreen buffer
             */
            render();

            /**
             * Draw the offscreen buffer to the screen
             */
            draw();

            /**
             * Calculate how long we should sleep
             */
            afterTime = System.nanoTime();
            timeDiff = afterTime - beforeTime;
            sleepTime = ( framePeriod - timeDiff ) - overSleepTime;

            if ( sleepTime > 0 )
            {
                /**
                 * some time left in this cycle
                 */
                try
                {
                    Thread.sleep( sleepTime / 1000000L ); // nano -> ms
                    noDelays = 0; // reset noDelays when sleep occurs
                }
                catch ( InterruptedException ex )
                {
                }
            }
            else
            {
                /**
                 * sleepTime <= 0; the frame took longer than the period
                 */
                excess -= sleepTime; // store excess time value

                if ( ++noDelays >= NO_DELAYS_PER_YIELD )
                {
                    Thread.yield(); // give another thread a chance to run
                    noDelays = 0;
                }
            }

            /**
             * If frame animation is taking too long, update the game state without rendering it, to get the updates/sec nearer to the
             * required FPS.
             */
            int skips = 0;
            while ( ( excess > framePeriod ) && ( skips < MAX_FRAME_SKIPS ) )
            {
                excess -= framePeriod;

                /**
                 * Check for collisions among the game objects
                 */
                collisions();

                /**
                 * Update all the game objects
                 */
                update();
                skips++;
            }

        }

        /**
         * If we get to this point, it's because running was set to false. In that case, exit the thread.
         */
        return;
    }

    /**
     * Set the thread running state
     * 
     * @param running
     */
    public void setRunning( boolean running )
    {
        this.running = running;
    }

    /**
     * Get the animation thread going. Calls run() once the thread is created and running.
     */
    public void start()
    {
        if ( gameThread == null )
        {
            gameThread = new Thread( this );
            gameThread.start();
        }
    }
}
