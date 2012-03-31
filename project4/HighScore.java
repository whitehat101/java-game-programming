package project4;

import java.util.Date;

public class HighScore implements Comparable< HighScore >
{
    int score;
    int level;
    String date;
    String name;

    public HighScore( int level, int score )
    {
        this.level = level;
        this.score = score;
        date = new Date().toString();
    }

    public HighScore( int level, int score, String date, String name )
    {
        this.level = level;
        this.score = score;
        this.date = date;
        this.name = name;
    }

    @Override
    public int compareTo( HighScore o )
    {
        return o.score - score;
    }

}
