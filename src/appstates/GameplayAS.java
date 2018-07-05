/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package appstates;

import com.jme3.app.Application;
import com.jme3.app.SimpleApplication;
import com.jme3.app.state.BaseAppState;
import com.jme3.app.state.ScreenshotAppState;
import com.jme3.asset.AssetManager;
import com.jme3.input.InputManager;
import com.jme3.input.KeyInput;
import com.jme3.input.controls.ActionListener;
import com.jme3.input.controls.AnalogListener;
import com.jme3.input.controls.KeyTrigger;
import com.jme3.math.ColorRGBA;
import com.jme3.math.FastMath;
import com.jme3.math.Quaternion;
import com.jme3.math.Vector3f;
import com.jme3.renderer.Camera;
import com.jme3.renderer.ViewPort;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import engine.DebugManager;
import engine.audio.AudioFiles;
import engine.audio.AudioManager;
import java.util.logging.Level;
import java.util.logging.Logger;


/** 
 * Appstate related to general gameplay. Moving, attacking, etc...
 * @author ricardo 
 */
public class GameplayAS extends BaseAppState {
    
    private static final Logger logger = Logger.getLogger(GameplayAS.class.getName());
    
    private SimpleApplication app;
    private Node rootNode;
    private AssetManager assetManager;
    private InputManager inputManager;
    private Camera cam;
    private ViewPort viewPort;
    
    private float frustumSize = 7;
    private final float speed = 0.025f;
    private engine.Camera chaseCam;
    private Node charNode;
    private final boolean invertX = true;
    private boolean sprinting = false;
    private sprite.Character charControl;
    
    private ScreenshotAppState screenshot;
    
    private boolean forward = false, backward = false, left = false, right = false;
    
    private Vector3f lookDir = new Vector3f(0, 0, 1);
    private final float angles[] = {0, FastMath.QUARTER_PI, 0};
    private final float anglesInvert[] = {0, -FastMath.QUARTER_PI, 0};
    private Quaternion roll45 = new Quaternion(angles);
    private Quaternion rollMinus45 = new Quaternion(anglesInvert);

    @Override
    protected void initialize(Application app) {
        DebugManager.setLoggerLevel(logger);
        
        logger.log(Level.INFO, "GameplayAS is being created.");
        this.app = (SimpleApplication)app;
        this.rootNode = this.app.getRootNode();
        this.assetManager = this.app.getAssetManager();
        this.inputManager = this.app.getInputManager();
        this.cam = this.app.getCamera();
        this.viewPort = this.app.getViewPort();
        
        //Node world = (Node)assetManager.loadModel("Models/test-stage.j3o");
        //Node world = (Node)assetManager.loadModel("Maps/Overworld/0-0/chunk.j3o");
        Node world = (Node)assetManager.loadModel("Maps/Overworld/0-0/Chunk-0-0.j3o");
        rootNode.attachChild(world);
        logger.log(Level.FINEST, "[1/7] Game world loaded.");
        
        AudioManager.load(AudioFiles.HAGROF_GROUNDS);
        AudioManager.load(AudioFiles.GRASS_STEP1);
        AudioManager.load(AudioFiles.GRASS_STEP2);
        AudioManager.load(AudioFiles.GRASS_STEP3);
        AudioManager.load(AudioFiles.ROTATING_VIEW);
        AudioManager.play(AudioFiles.HAGROF_GROUNDS);
        logger.log(Level.FINEST, "[2/7] Audio pre-loaded.");
        
        viewPort.setBackgroundColor(ColorRGBA.Cyan);
        //flyCam.setEnabled(false);
        logger.log(Level.FINEST, "[3/7] Viewport configured.");
        
        charNode = new Node("Character Node");
        rootNode.attachChild(charNode);
        //charNode.setLocalTranslation(0, 0, -10);
        
        charControl = new sprite.Character(assetManager, "Textures/Characters/Default/", 8, 4);
        
        Spatial character = assetManager.loadModel("Models/char.j3o");
        character.addControl(charControl);
        charNode.attachChild(character);
        logger.log(Level.FINEST, "[4/7] Character sprite loaded.");

        chaseCam = new engine.Camera(cam, charNode, inputManager);
        chaseCam.setInvertHorizontalAxis(invertX);
        logger.log(Level.FINEST, "[5/7] Chase camera created.");
        
        cam.setParallelProjection(true);
        float aspect = (float) cam.getWidth() / cam.getHeight();
        cam.setFrustum(-1000, 1000, -aspect * frustumSize, aspect * frustumSize, frustumSize, -frustumSize);
        logger.log(Level.FINEST, "[6/7] Ortographic camera set up.");
        
        inputManager.addListener(action, "RotLeft", "RotRight", "Frustum", "Forward", "Backwards", "Left", "Right", "Sprinting", "Menu", "Screenshot");
        inputManager.addListener(analog, "Size+", "Size-");
        inputManager.addMapping("Size+", new KeyTrigger(KeyInput.KEY_SUBTRACT));
        inputManager.addMapping("Size-", new KeyTrigger(KeyInput.KEY_ADD));
        inputManager.addMapping("RotLeft", new KeyTrigger(KeyInput.KEY_Q));
        inputManager.addMapping("RotRight", new KeyTrigger(KeyInput.KEY_E));
        inputManager.addMapping("Frustum", new KeyTrigger(KeyInput.KEY_F));
        inputManager.addMapping("Forward", new KeyTrigger(KeyInput.KEY_W));
        inputManager.addMapping("Sprinting", new KeyTrigger(KeyInput.KEY_LSHIFT));
        inputManager.addMapping("Backwards", new KeyTrigger(KeyInput.KEY_S));
        inputManager.addMapping("Left", new KeyTrigger(KeyInput.KEY_A));
        inputManager.addMapping("Right", new KeyTrigger(KeyInput.KEY_D));
        inputManager.addMapping("Menu", new KeyTrigger(KeyInput.KEY_ESCAPE));
        inputManager.addMapping("Screenshot", new KeyTrigger(KeyInput.KEY_SPACE));
        logger.log(Level.FINEST, "[7/7] Inputs registered.");
        
        screenshot = new ScreenshotAppState("Screenshots//");
        app.getStateManager().attach(screenshot);
        
        logger.log(Level.INFO, "GameplayAS successfully loaded.");
    }
    
    private final ActionListener action = new ActionListener() {
        @Override
        public void onAction(String name, boolean pressed, float tpf) {
            if(name.equals("RotLeft") && pressed) {
                lookDir = invertX ? roll45.mult(lookDir) : rollMinus45.mult(lookDir);
                charControl.incrementMajor();
                AudioManager.playInstance(AudioFiles.ROTATING_VIEW);
            } else if(name.equals("RotRight") && pressed) {
                lookDir = invertX ? rollMinus45.mult(lookDir) : roll45.mult(lookDir);
                charControl.decrementMajor();
                AudioManager.playInstance(AudioFiles.ROTATING_VIEW);
            }
            
            if(name.equals("Frustum") && !pressed) {
                System.out.println("Frustum bottom: " + cam.getFrustumBottom());
                System.out.println("Frustum top: " + cam.getFrustumTop());
                System.out.println("Frustum left: " + cam.getFrustumLeft());
                System.out.println("Frustum right: " + cam.getFrustumRight());
                System.out.println("Frustum near: " + cam.getFrustumNear());
                System.out.println("Frustum far: " + cam.getFrustumFar());
                System.out.println("Frustum size: " + frustumSize);
            }
            
            if(name.equals("Forward")) {
                forward = pressed;
            } else if(name.equals("Backwards")) {
                backward = pressed;
            }
            
            if(name.equals("Left")) {
                left = pressed;
            } else if(name.equals("Right")) {
                right = pressed;
            }
            
            if(name.equals("Sprinting")) {
                sprinting = pressed;
                logger.log(Level.FINEST, "Sprinting... {0}", sprinting);
            }
            
            if(name.equals("Menu") && pressed) {
                System.out.println("Called escape!");
                app.getStateManager().detach(GameplayAS.this);
                app.getStateManager().attach(new MainMenuAS());
            }
            
            if(name.equals("Screenshot") && pressed) {
                screenshot.takeScreenshot();
                logger.log(Level.INFO, "Screenshot taken at {0} frustum size.", frustumSize);
            }
        }
    };
    
    private final AnalogListener analog = new AnalogListener() {
        @Override
        public void onAnalog(String name, float value, float tpf) {
            // Instead of moving closer/farther to object, we zoom in/out.
            if(name.equals("Size+")) {
                frustumSize += 3f * tpf;
                float aspect = (float) cam.getWidth() / cam.getHeight();
                cam.setFrustum(-1000, 1000, -aspect * frustumSize, aspect * frustumSize, frustumSize, -frustumSize);
            } else if(name.equals("Size-")) {
                frustumSize -= 3f * tpf;
                float aspect = (float) cam.getWidth() / cam.getHeight();
                cam.setFrustum(-1000, 1000, -aspect * frustumSize, aspect * frustumSize, frustumSize, -frustumSize);
            }
        }
    };

    @Override
    protected void cleanup(Application app) {
        logger.log(Level.FINE, "GameplayAS is being deleted.");
        
        cam.setParallelProjection(false);
        cam.setLocation(new Vector3f(0, 0, 10));
        cam.setRotation(new Quaternion(0, 1, 0, 0));
        cam.lookAt(Vector3f.ZERO, Vector3f.UNIT_Y);
        
        inputManager.deleteMapping("Menu");
        AudioManager.unloadAll();
        rootNode.detachAllChildren();
    }

    @Override
    protected void onEnable() {

    }

    @Override
    protected void onDisable() {

    }

    @Override
    public void update(float tpf) {
        Vector3f forwardDir = new Vector3f(0, 0, 0);
        
        if(forward) {
            forwardDir.addLocal(lookDir.normalize().mult(sprinting ? speed * 1.5f : speed));
        } else if(backward) {
            forwardDir.addLocal(lookDir.negate().normalize().mult(sprinting ? speed * 1.5f : speed));
        }
        
        if(left) {
            forwardDir.addLocal(lookDir.cross(Vector3f.UNIT_Y).negate().normalize().mult(sprinting ? speed * 1.5f : speed));
        } else if(right) {
            forwardDir.addLocal(lookDir.cross(Vector3f.UNIT_Y).normalize().mult(sprinting ? speed * 1.5f : speed));
        }
        
        charNode.move(forwardDir);
        charControl.setMoving(forward || backward || left || right);
        charControl.setCycleTime(sprinting ? 0.2f : 0.25f);
        
        if(forward && left) {
            charControl.setCurrentMajor(1);
        } else if (forward && right) {
            charControl.setCurrentMajor(3);
        } else if(backward && left) {
            charControl.setCurrentMajor(7);
        } else if(backward && right) {
            charControl.setCurrentMajor(5);
        } else if(forward && !(left || right)) {
            charControl.setCurrentMajor(2);
        } else if(left && !(forward && backward)) {
            charControl.setCurrentMajor(8);
        } else if(backward && !(left || right)) {
            charControl.setCurrentMajor(6);
        } else if(right && !(forward || backward)) {
            charControl.setCurrentMajor(4);
        }
    }
}