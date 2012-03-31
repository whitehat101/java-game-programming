package libs;

import java.io.IOException;
import java.net.URL;

import javax.sound.midi.InvalidMidiDataException;
import javax.sound.midi.MetaEventListener;
import javax.sound.midi.MetaMessage;
import javax.sound.midi.MidiChannel;
import javax.sound.midi.MidiSystem;
import javax.sound.midi.MidiUnavailableException;
import javax.sound.midi.Sequence;
import javax.sound.midi.Sequencer;
import javax.sound.midi.Synthesizer;
import javax.sound.sampled.UnsupportedAudioFileException;

/**
 * Midi clip class for playing sounds
 * 
 * @author williamhooper $Id: AudioMidi.java,v 1.1 2011/02/07 07:10:25 williamhooper Exp $
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
public class AudioMidi implements MetaEventListener
{

    public enum AudioState
    {
        PLAYING, LOOPING, STOPPED, DONE, CLOSED
    };

    public static final int MAX_VOLUME = 11;
    public static final int LOOP_CONTINUOUSLY = Sequencer.LOOP_CONTINUOUSLY;

    private static final int VOLUME_CONTROLLER = 7;
    private static final int PAN_CONTROLLER = 10;
    private static final int BALANCE_CONTROLLER = 8;

    private URL audioURL;
    private Sequence sequence;
    private Sequencer sequencer;
    private Synthesizer synthesizer;
    private AudioState audioState;
    private MidiChannel [ ] midiChannels;

    /**
     * Constructor
     * 
     * @param filename
     * @throws MidiUnavailableException
     * @throws IOException
     * @throws InvalidMidiDataException
     * @throws IOException
     * @throws LineUnavailableException
     * @throws UnsupportedAudioFileException
     */
    public AudioMidi( Object obj, String filename ) throws InvalidMidiDataException, IOException, MidiUnavailableException
    {
        java.net.URL audioURL = obj.getClass().getResource( filename );
        if ( audioURL != null )
        {
            this.audioURL = audioURL;
            load();
            audioState = AudioState.DONE;
        }
        else
        {
            throw new IOException( "Couldn't find file: " + filename );
        }
    }

    /**
     * Constructor
     * 
     * @param audioFile
     * @throws MidiUnavailableException
     * @throws IOException
     * @throws InvalidMidiDataException
     * @throws LineUnavailableException
     * @throws IOException
     * @throws UnsupportedAudioFileException
     */
    public AudioMidi( URL audioURL ) throws InvalidMidiDataException, IOException, MidiUnavailableException
    {
        this.audioURL = audioURL;
        load();
        audioState = AudioState.DONE;
    }

    /**
     * Return the state of the audio clip
     * 
     * @return
     */
    public synchronized AudioState getState()
    {
        return audioState;
    }

    /**
     * Get the length of the audio clip
     * 
     * @return
     */
    public synchronized long getTime()
    {
        if ( sequence == null )
            return 0L;

        return sequence.getMicrosecondLength();
    }

    /**
     * Loop the audio clip
     * 
     * @param loop
     */
    public synchronized void loop( int loop )
    {
        if ( sequence == null )
            return;

        // rewind
        sequencer.setTickPosition( 0L );

        // Start looping
        sequencer.setLoopCount( loop );
        sequencer.start();
        audioState = AudioState.LOOPING;
    }

    /**
     * Rewind the audio clip and play
     * 
     */
    public synchronized void play()
    {
        if ( sequence == null )
            return;

        // rewind
        sequencer.setTickPosition( 0L );

        // Start playing
        sequencer.start();
        audioState = AudioState.PLAYING;
    }

    /**
     * Set the balance from -1.0 for left, 0.0 for center and 1.0 for right
     * 
     * @param range
     */
    public synchronized void setBalance( double range )
    {
        int value = ( int ) ( ( range + 1.0 ) * 127.0 / 2.0 );
        for ( int i = 0; i < midiChannels.length; i++ )
        {
            midiChannels[ i ].controlChange( BALANCE_CONTROLLER, value );
        }
    }

    /**
     * Set the pan from -1.0 for left, 0.0 for center and 1.0 for right
     * 
     * 
     * @param range
     */
    public synchronized void setPan( double range )
    {
        int value = ( int ) ( ( range + 1.0 ) * 127.0 / 2.0 );
        System.out.println( "pan " + value );
        for ( int i = 0; i < midiChannels.length; i++ )
        {
            midiChannels[ i ].controlChange( PAN_CONTROLLER, value );
        }
    }

    /**
     * Set the volume from 0 (muted) to 11 (loud)
     * 
     * @param volume
     */
    public synchronized void setVolume( double volume )
    {
        int value = ( int ) ( ( volume / ( double ) 11.0 ) * 127 );

        System.out.println( "volume " + value );
        for ( int i = 0; i < midiChannels.length; i++ )
        {
            midiChannels[ i ].controlChange( VOLUME_CONTROLLER, value );
        }
    }

    /**
     * Start playing the audio clip from the current position
     * 
     */
    public synchronized void start()
    {
        if ( sequence == null )
            return;

        // Start playing
        sequencer.start();
        audioState = AudioState.PLAYING;
    }

    /**
     * Stop playing the audio clip
     * 
     */
    public synchronized void stop()
    {
        if ( sequence == null )
            return;

        // Stop playing
        sequencer.stop();
        audioState = AudioState.STOPPED;
    }

    /**
     * Private method to load audio file
     * 
     * @throws IOException
     * @throws InvalidMidiDataException
     * @throws MidiUnavailableException
     * 
     */
    private void load() throws InvalidMidiDataException, IOException, MidiUnavailableException
    {
        // Read the sequence from the file
        sequence = MidiSystem.getSequence( audioURL );

        // Gget a Sequencer to play sequences of MIDI events
        sequencer = MidiSystem.getSequencer();
        sequencer.open();
        sequencer.setSequence( sequence );

        // Get a Synthesizer for the Sequencer to send notes to
        synthesizer = MidiSystem.getSynthesizer();
        synthesizer.open();

        // fetch the midi channels
        midiChannels = synthesizer.getChannels();

        // you need to do a check here to see if the java sound bank was loaded
        if ( synthesizer.getDefaultSoundbank() == null )
        {
            // then you know that java sound is using the hardware soundbank
            sequencer.getTransmitter().setReceiver( MidiSystem.getReceiver() );
        }
        else
        {
            // link the sequencer to the synthesizer
            sequencer.getTransmitter().setReceiver( synthesizer.getReceiver() );
        }
    }

    /**
     * Close the audio clip.
     * 
     */
    public void close()
    {
        if ( sequence == null )
            return;

        sequencer.stop();
        audioState = AudioState.STOPPED;
        sequencer.close();

        synthesizer.close();
    }

    /**
     * Meta message listener that tells us when the sound in done playing
     * 
     */
    public void meta( MetaMessage event )
    {
        if ( event.getType() == 47 )
        {
            sequencer.stop();
            audioState = AudioState.CLOSED;
        }
    }

}
