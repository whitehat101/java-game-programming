package practice3.ext;

import java.awt.Color;
import java.util.HashMap;
import java.util.Set;
import java.util.Vector;

import libs.Sprite;

public class TagFrame
{
    Vector< TagSprite > team0players;
    Vector< TagSprite > team1players;

    public void addTeam0Player( TagSprite s )
    {
        team0players.add( s );
    }

    public void addTeam1Player( TagSprite s )
    {
        team1players.add( s );
    }

    public TagSprite pickRandomTarget( Color myTeamColor )
    {
        Set< Color > keys = players.keySet();
        keys.remove( myTeamColor );
        Color targetTeam = ( Color ) keys.toArray()[ ( int ) ( Math.random() * keys.size() ) ];
        Vector< TagSprite > targets = players.get( targetTeam );
        TagSprite target = ( TagSprite ) targets.get( ( int ) ( Math.random() * targets.size() ) );

        return target;
    }

}
