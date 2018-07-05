package engine.audio;

public enum AudioFiles {
    MENU_THEME("Menu Theme", "menuTheme.ogg", new AudioSettings(AudioSettings.BACKGROUND).volume(0.3f)),
    HAGROF_GROUNDS("Hagrof Grounds", "background.ogg", AudioSettings.BACKGROUND),
    ROTATING_VIEW("Rotating View", "rotateLeft.wav", new AudioSettings(AudioSettings.SFX).pitch(2)),
    GRASS_STEP1("Grass Step 1", "step1.wav", new AudioSettings(AudioSettings.SFX).volume(100)),
    GRASS_STEP2("Grass Step 2", "step2.wav", new AudioSettings(AudioSettings.SFX).volume(100)),
    GRASS_STEP3("Grass Step 3", "step3.wav", new AudioSettings(AudioSettings.SFX).volume(100));
    
    private AudioFiles(String name, String path, AudioSettings settings) {
        this.name = name;
        this.path = path;
        this.settings = settings;
    }
    
    public String name, path;
    public AudioSettings settings;
}
