package project4;

import java.awt.image.BufferedImage;
import java.io.IOException;

import libs.TileSheet;

public class InvaderSheet extends TileSheet
{
    final int START_ANI_FRAME = 0;
    final int STOP_ANI_FRAME = 1;
    final int EXPLOSION_FRAME = 2;
    final int ROW_LENGTH = 3;

    public InvaderSheet( BufferedImage image, int width, int height )
    {
        super( image, width, height );
        // TODO Auto-generated constructor stub
    }

    public InvaderSheet( Object obj, String file, int width, int height ) throws IOException
    {
        super( obj, file, width, height );
        // TODO Auto-generated constructor stub
    }

}
