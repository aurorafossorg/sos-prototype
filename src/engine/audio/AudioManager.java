package engine.audio;

import com.jme3.app.SimpleApplication;
import com.jme3.asset.AssetManager;
import com.jme3.audio.AudioNode;
import com.jme3.math.FastMath;
import java.util.HashMap;

public class AudioManager {
    
    // Singleton
    private static AudioManager audioManager;
    
    // Audio
    private final AssetManager assetManager;
    private final HashMap<String, AudioNode> audioNodes = new HashMap<>();
    
    private AudioManager(SimpleApplication app) {
        this.assetManager = app.getAssetManager();
        /*// Pre-buffer the sound files
        // First the background audio
        AudioNode backgroundAudio = new AudioNode(assetManager, "Sounds/background.ogg", DataType.Stream);
        backgroundAudio.setPositional(false);
        backgroundAudio.setVolume(0.5f);
        backgroundAudio.setLooping(true);
        audioNodes.put(BACKGROUND, backgroundAudio);
        
        AudioNode menuThemeAudio = new AudioNode(assetManager, "Sounds/menuTheme.ogg", DataType.Stream);
        menuThemeAudio.setPositional(false);
        menuThemeAudio.setVolume(0.8f);
        menuThemeAudio.setLooping(true);
        audioNodes.put(MENU_THEME, menuThemeAudio);
        
        // Then the rotating sound effect
        AudioNode rotatingAudio = new AudioNode(assetManager, "Sounds/rotateLeft.wav", DataType.Buffer);
        rotatingAudio.setPitch(2);
        rotatingAudio.setVolume(1f);
        rotatingAudio.setLooping(false);
        rotatingAudio.setPositional(false);
        audioNodes.put(ROTATE_VIEW, rotatingAudio);
        
        // Then the 3 sound effects
        AudioNode[] stepsSounds = {new AudioNode(assetManager, "Sounds/step1.wav", DataType.Buffer),
                                new AudioNode(assetManager, "Sounds/step2.wav", DataType.Buffer),
                                new AudioNode(assetManager, "Sounds/step3.wav", DataType.Buffer)};
        
        for(int i = 0; i < 3; i++) {
            AudioNode stepSound = stepsSounds[i];
            
            stepSound.setPositional(false);
            stepSound.setLooping(false);
            stepSound.setVolume(15);
            
            switch(i) {
                case 0:
                    audioNodes.put(STEP1, stepSound);
                    break;
                    
                case 1:
                    audioNodes.put(STEP2, stepSound);
                    break;
                    
                case 2:
                    audioNodes.put(STEP3, stepSound);
                    break;
                    
                default:
                    break;
            }
        }*/
    }
    
    // Singleton methods
    public static void initialize(SimpleApplication app) {
        audioManager = new AudioManager(app);
    }
    
    public static void terminate() {
        unloadAll();
    }
    
    public static void load(AudioFiles audioFile) {
        AudioNode audioNode = new AudioNode(audioManager.assetManager, "Sounds/" + audioFile.path, audioFile.settings.dataType);
        audioNode.setVolume(audioFile.settings.volume);
        audioNode.setPitch(audioFile.settings.pitch);
        audioNode.setPositional(audioFile.settings.positional);
        audioNode.setLooping(audioFile.settings.looping);
        audioNode.setLocalTranslation(audioFile.settings.position);
        
        System.out.println("[Audio] Song \"" + audioFile.name + "\" (" + audioFile.path + ") was loaded into memory.");
        
        audioManager.audioNodes.put(audioFile.name, audioNode);
    }
    
    public static void unload(AudioFiles audioFile) {
        stop(audioFile);
        audioManager.audioNodes.remove(audioFile.name);
    }
    
    public static void unloadAll() {
        stopAll();
        audioManager.audioNodes.clear();
        
        System.out.println("[Audio] All songs were unloaded from memory.");
    }

    public static void play(AudioFiles audioFile) {
        audioManager.audioNodes.get(audioFile.name).play();
    }
    
    public static void playInstance(AudioFiles audioFile) {
        audioManager.audioNodes.get(audioFile.name).playInstance();
    }
    
    public static void playRandom(AudioFiles... audioFiles) {
        play(audioFiles[FastMath.nextRandomInt(0, audioFiles.length - 1)]);
    }
    
    public static void playInstanceRandom(AudioFiles... audioFiles) {
        playInstance(audioFiles[FastMath.nextRandomInt(0, audioFiles.length - 1)]);
    }
    
    public static void stop(AudioFiles audioFile) {
        audioManager.audioNodes.get(audioFile.name).stop();
    }
    
    public static void stopAll() {
        for(AudioNode an : audioManager.audioNodes.values()) {
            an.stop();
        }
    }
}
