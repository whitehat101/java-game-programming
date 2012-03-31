package project4;

public class FleetStrategy
{

    private int invaderCount;
    private int fleetXSpeed;
    private int fleetYdrop;

    public FleetStrategy( int invaders, int Xspeed, int Ydrop )
    {
        invaderCount = invaders;
        fleetXSpeed = Xspeed;
        fleetYdrop = Ydrop;
    }

    public int getInvaderCount()
    {
        return invaderCount;
    }

    public int getXSpeed()
    {
        return fleetXSpeed;
    }

    public int getYDrop()
    {
        return fleetYdrop;
    }

}
