package mygame;

import appstates.MainMenuAS;
import com.jme3.app.SimpleApplication;
import com.jme3.renderer.RenderManager;
import engine.DebugManager;
import engine.audio.AudioManager;
import engine.ui.UIManager;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * This is the Main Class of your Game. You should only do initialization here.
 * Move your Logic into AppStates or Controls
 * @author normenhansen
 */
public class Main extends SimpleApplication {
    
    private static final Logger logger = Logger.getLogger(Main.class.getName());

    public static void main(String[] args) {
        DebugManager.setLoggerLevel(logger);
        DebugManager.setLoggerLevel(Logger.getLogger(""), Level.SEVERE);
        Main app = new Main();
        app.start();
    }
    
    @Override
    public void simpleInitApp() {
        UIManager.initialize(this);
        AudioManager.initialize(this);
        
        flyCam.setEnabled(false);
        inputManager.deleteMapping(SimpleApplication.INPUT_MAPPING_EXIT);

        //stateManager.attach(new GameplayAS());
        stateManager.attach(new MainMenuAS());
    }

    @Override
    public void simpleUpdate(float tpf) {

    }

    @Override
    public void simpleRender(RenderManager rm) {

    }
}