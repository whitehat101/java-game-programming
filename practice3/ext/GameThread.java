package practice3.ext;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.Vector;

import libs.GameFrame;
import libs.Sprite;

public class GameThread extends TimerTask
{
    //Foreground Sprites
    List<Sprite> spriteListFG;
    LinkedList<Sprite> spriteAddList;
    LinkedList<Sprite> spriteRemoveList;
    GameFrame gf;
    Graphics2D lastFrame, currentFrame;
    boolean simulationIsRunning;
    boolean threadIsRunning;
 

    
    Timer SimulationTimer;
    
    public GameThread( GameFrame GF )
    {
        spriteListFG = new Vector<Sprite>();
        spriteAddList = new LinkedList<Sprite>();
        spriteRemoveList = new LinkedList<Sprite>();
        gf = GF;
        simulationIsRunning = false;
    }
    
    //Naw, this is safe.
    //If this is called while the game is running, any Iterator in run()
    //will quickly abort itself, resulting in incomplete turns or renderings
    //Beware, or add code to safely add new sprites at the beginning of next run()
    public void addSprite(Sprite sprite)
    {
        spriteAddList.add( sprite );
    }
    public void removeAllSpritesSprite()
    {
        spriteRemoveList.addAll( spriteListFG );
    }

    @Override
    public void run()
    {
        long codeTimer = System.nanoTime();
       threadIsRunning = true;
       //Add new Sprites
       while(!spriteAddList.isEmpty()){
           spriteListFG.add( spriteAddList.removeFirst() );
       }
       //Remove old Sprites
       while(!spriteRemoveList.isEmpty()){
           spriteListFG.remove( spriteRemoveList.removeFirst() );
       }
       
       runGameTurn();
       
       //If we need to skip frames, exit here.
       if(System.currentTimeMillis() - scheduledExecutionTime() >= 1000/30){
           //1000/30 Å 33ms
           threadIsRunning = false;
           System.out.println("Skipping Frame.");
           return;
       }
       
       currentFrame = gf.getCurrentGraphics2D();
       gameRenderBackground();
       gameRenderFrame();       
       gf.updateGraphics();
       threadIsRunning = false;
       codeTimer = System.nanoTime() - codeTimer;
//       System.out.print( "Run "+codeTimer+"ns\n" );
       //System.out.print( "Sleep "+ ((long)(1e+9/30) - codeTimer)+"ns " );
       //System.out.print( "Theoretical max fps: "+ 1e+9/codeTimer + "\n");
//       System.out.println(System.currentTimeMillis() - scheduledExecutionTime() +
//               " period: "+ 1000/30 + "\n"
//               );
    }
    
    //Increments the action one turn/one frame
    private void runGameTurn()
    {
        Iterator<Sprite> it = spriteListFG.iterator();
        
        while(it.hasNext()){
            //Collision?
            Sprite s = it.next();
            s.update();
            //Random Death!
            //To test the remove queue
//            if(Math.random()*1000 < 5)
//                spriteRemoveList.add( s );
        }
        
    }
    private void gameRenderFrame()
    {
        Iterator<Sprite> it = spriteListFG.iterator();
        while(it.hasNext()){
            it.next().draw(currentFrame);
        }
    }
    
    private void gameRenderBackground()
    {
        Graphics2D g2d = currentFrame;
        //currentFrame.setColor(Color.DARK_GRAY);
        //currentFrame.setColor(getRGBColor(0xCA,0xFE,0xED));
   
        //General Setup
        // Enable antialiasing for text
        g2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING,
                             RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        // Enable antialiasing for shapes
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                             RenderingHints.VALUE_ANTIALIAS_ON);

        g2d.setColor(new Color(15,168,250));
        g2d.fillRect( 0, 0, gf.getWidth(), gf.getHeight() );
        
        //g2d.setColor(new Color(0xCA,0xFE,0xED));
        g2d.setColor(Color.DARK_GRAY);
        g2d.setFont(new Font("Tahoma", Font.BOLD, 20));
        
        g2d.drawString("c Clear",
                15, gf.getHeight()-2*25 -10);
        g2d.drawString("j JME Animation",
                15, gf.getHeight()-1*25 -10);
        g2d.drawString("r RWB Animation",
                15, gf.getHeight()-0*25-10 );

        g2d.setFont(new Font("Tahoma", Font.PLAIN, 12));
        g2d.drawString("esc/q Quit",
                gf.getWidth()-150, gf.getHeight()-1*15-10 );
        g2d.drawString("Managing "+spriteListFG.size()+" Sprites.",
                gf.getWidth()-150, gf.getHeight()-0*15-10 );
    }
    
    public void runAtFPS(double targetFPS)
    {
        if(SimulationTimer == null){
            SimulationTimer = new Timer("GameThread");
        }
        //TODO support runtime fps changes
        if(simulationIsRunning == true){
            return;//unsupported runtime fps changes
        }
        
        SimulationTimer.scheduleAtFixedRate(this, 0, (long) ( 1000/targetFPS ));
        
        simulationIsRunning = true;
        System.out.println("Stated at target FPS:" + targetFPS);
    }


    public void stopSimulation()
    {
        SimulationTimer.cancel();//Use this to end the Simulation
       
        while(threadIsRunning == true){
                System.out.print(".");
        }System.out.print("\n");
        
        
        simulationIsRunning = false;
        System.out.println("Stopped. Exit now.");
    }
    
}
