/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package appstates;

import com.jme3.app.Application;
import com.jme3.app.SimpleApplication;
import com.jme3.app.state.AppStateManager;
import com.jme3.app.state.BaseAppState;
import com.jme3.input.InputManager;
import engine.audio.AudioFiles;
import engine.audio.AudioManager;
import engine.ui.EventListener;
import engine.ui.EventType;
import engine.ui.UIManager;

/**
 *  
 *  This AppState represents the initial state, were there is a main menu.
 *
 * @author ricardo 
 */
public class MainMenuAS extends BaseAppState implements EventListener {
    
    private SimpleApplication app;
    private AppStateManager stateManager;
    private InputManager inputManager;

    @Override
    protected void initialize(Application app) {
        System.out.println("MainMenuAS is being created.");
        this.app = (SimpleApplication)app;
        this.stateManager = this.app.getStateManager();
        this.inputManager = this.app.getInputManager();
        
        inputManager.setCursorVisible(true);
        
        UIManager.getInstance().registerEventListener(this);
        UIManager.loadUI("Interface/mainMenu.xml", "mainMenu");
        
        AudioManager.load(AudioFiles.MENU_THEME);
        AudioManager.play(AudioFiles.MENU_THEME);
    }

    @Override
    protected void cleanup(Application app) {
        System.out.println("MainMenuAS is being deleted.");
        inputManager.setCursorVisible(false);
        UIManager.getInstance().removeEventListener(this);
        
        AudioManager.unloadAll();
        
        UIManager.unloadUI();
    }
    
    @Override
    protected void onEnable() {
        
    }

    @Override
    protected void onDisable() {

    }
    
    @Override
    public void update(float tpf) {

    }

    @Override
    public void onEvent(String elementName, EventType et) {
        if(elementName.equals("play") && et == EventType.ButtonPressed) {
            stateManager.detach(this);
            stateManager.attach(new GameplayAS());
        } else if(elementName.equals("options") && et == EventType.ButtonPressed) {
            // TODO: Implement options menu
        } else if(elementName.equals("exit") && et == EventType.ButtonPressed) {
            app.stop();
        }
    }
}