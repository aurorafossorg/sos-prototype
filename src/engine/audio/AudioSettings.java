package engine.audio;

import com.jme3.audio.AudioData.DataType;
import com.jme3.math.Vector3f;

public class AudioSettings {
    
    public static final AudioSettings BACKGROUND = new AudioSettings().volume(0.8f).looping(true).dataType(DataType.Stream);
    public static final AudioSettings SFX = new AudioSettings().volume(15).dataType(DataType.Buffer);
    
    protected float volume = 1.0f, pitch = 1.0f;
    protected boolean looping = false, positional = false;
    protected Vector3f position = Vector3f.ZERO;
    protected DataType dataType = DataType.Buffer;
    
    public AudioSettings() {}
    
    public AudioSettings(AudioSettings clone) {
        this.volume = clone.volume;
        this.pitch = clone.pitch;
        this.looping = clone.looping;
        this.positional = clone.positional;
        this.position = clone.position;
        this.dataType = clone.dataType;
    }
    
    public AudioSettings volume(float volume) {
        this.volume = volume;
        return this;
    }
    
    public AudioSettings pitch(float pitch) {
        this.pitch = pitch;
        return this;
    }
    
    public AudioSettings looping(boolean looping) {
        this.looping = looping;
        return this;
    }
    
    public AudioSettings positional(boolean positional) {
        this.positional = positional;
        return this;
    }
    
    public AudioSettings position(Vector3f position) {
        this.position = position;
        return this;
    }
    
    public AudioSettings dataType(DataType dataType) {
        this.dataType = dataType;
        return this;
    }
}