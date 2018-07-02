package engine.ui;

import com.jme3.app.SimpleApplication;
import com.jme3.asset.AssetManager;
import com.jme3.audio.AudioRenderer;
import com.jme3.input.InputManager;
import com.jme3.niftygui.NiftyJmeDisplay;
import com.jme3.renderer.ViewPort;
import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.screen.Screen;
import de.lessvoid.nifty.screen.ScreenController;
import engine.DebugManager;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * A singleton used to control UI elements.
 * @author ricardo
 */
public class UIManager implements ScreenController {
    // Variables
    // Essentials
    private final SimpleApplication app;
    private final AssetManager assetManager;
    private final InputManager inputManager;
    private final AudioRenderer audioRenderer;
    private final ViewPort guiViewPort;
    private static final Logger logger = Logger.getLogger(UIManager.class.getName());
    
    // EventListener list
    private final ArrayList<EventListener> eventListeners;
    
    // Singleton
    public static UIManager uiManager;
    
    // Nifty GUI
    private final NiftyJmeDisplay niftyJME;
    private final Nifty nifty;
    private String xmlPath;
    private String currentScreen;
    
    private UIManager(SimpleApplication app) {
        DebugManager.setLoggerLevel(logger, DebugManager.LEVEL);
        
        // Gathers essential variables
        this.app = app;
        
        this.assetManager = this.app.getAssetManager();
        this.inputManager = this.app.getInputManager();
        this.audioRenderer = this.app.getAudioRenderer();
        this.guiViewPort = this.app.getGuiViewPort();
        
        this.eventListeners = new ArrayList<>();
        
        // Starts up Nifty GUI
        this.niftyJME = new NiftyJmeDisplay(assetManager, inputManager, audioRenderer, guiViewPort);
        this.nifty = niftyJME.getNifty();
        this.nifty.registerScreenController(UIManager.this);
        this.guiViewPort.addProcessor(niftyJME);
        
        //this.nifty.registerStyle(new StyleType(new Attributes("nifty-default-styles.xml")));
        //this.nifty.registerControlDefintion(new ControlDefinitionType(new Attributes("nifty-default-controls.xml")));
        
        logger.log(Level.FINE, "[UI] UI successfully initialized.");
    }
    
    private void cleanup() {
        logger.log(Level.FINEST, "[UI] UI successfully cleaned-up.");
    }
    
    public void registerEventListener(EventListener listener) {
        eventListeners.add(listener);
        logger.log(Level.FINEST, "[UI] Added new EventListener. Current array size: [{0}]", eventListeners.size());
    }
    
    public void removeEventListener(EventListener listener) {
        eventListeners.remove(listener);
        logger.log(Level.FINEST, "[UI] Removed an EventListener. Current array size: [{0}]", eventListeners.size());
    }
    
    public void onEvent(String elementName) {
        logger.log(Level.FINER, "[UI] onEvent called. elementName = \"{0}\"", elementName);
        for(EventListener e : eventListeners) {
            e.onEvent(elementName, EventType.ButtonPressed);
        }
    }
    
    @Override
    public void bind(Nifty nifty, Screen screen) {
        logger.log(Level.FINEST, "[UI] Nifty was bound to screen \"{0}\"", screen.getScreenId());
    }

    @Override
    public void onStartScreen() {
        logger.log(Level.FINEST, "[UI] Nifty screen was started.");
    }

    @Override
    public void onEndScreen() {
        logger.log(Level.FINEST, "[UI] Nifty screen was ended.");
    }
    
    // Singleton methods
    public static void initialize(SimpleApplication app) {
        uiManager = new UIManager(app);
    }
    
    public static void terminate() {
        uiManager.cleanup();
        uiManager = null;
    }
    
    public static UIManager getInstance() {
        return uiManager;
    }
    
    public static void loadUI(String xmlPath, String screen) {
        Nifty nifty = uiManager.nifty;
        uiManager.xmlPath = xmlPath;
        uiManager.currentScreen = screen;
        
        try {
            if(DebugManager.isDebugModeOn()) {
                nifty.validateXml(xmlPath);
                nifty.fromXml(xmlPath, screen);
                logger.log(Level.FINE, "XML file \"{0}\" loaded successfully on screen \"{1}\"",
                        new Object[]{xmlPath, screen});
            }
        } catch(Exception e) {
            logger.log(Level.SEVERE, "XML file did not pass validation! Message: {0}\n{1}",
                    new Object[]{e.getMessage(), e.getStackTrace()});
        }
    }
    
    public static void unloadUI() {
        Nifty nifty = uiManager.nifty;
        
        nifty.removeScreen(uiManager.currentScreen);
        
        logger.log(Level.FINE, "XML file \"{0}\" and screen \"{1}\" unloaded successfully.",
                new Object[]{uiManager.xmlPath, uiManager.currentScreen});
        
        uiManager.xmlPath = "";
        uiManager.currentScreen = "";
    }
    
    public static String getXMLPath() {
        return uiManager.xmlPath;
    }
    
    public static String getCurrentScreen() {
        return uiManager.currentScreen;
    }
}