package engine.audio;

public enum AudioFiles {
    MENU_THEME("Menu Theme", "menuTheme.ogg", AudioSettings.BACKGROUND),
    HAGROF_GROUNDS("Hagrof Grounds", "background.ogg", AudioSettings.BACKGROUND),
    ROTATING_VIEW("Rotating View", "rotateLeft.wav", AudioSettings.SFX),
    GRASS_STEP1("Grass Step 1", "step1.wav", AudioSettings.SFX),
    GRASS_STEP2("Grass Step 2", "step2.wav", AudioSettings.SFX),
    GRASS_STEP3("Grass Step 3", "step3.wav", AudioSettings.SFX);
    
    private AudioFiles(String name, String path, AudioSettings settings) {
        this.name = name;
        this.path = path;
        this.settings = settings;
    }
    
    public String name, path;
    public AudioSettings settings;
}
