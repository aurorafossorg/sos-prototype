/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sprite;

import com.jme3.asset.AssetManager;
import com.jme3.renderer.RenderManager;
import com.jme3.renderer.ViewPort;

public class Character extends BaseSprite {
    
    // Constructors
    public Character(AssetManager assetManager, String rootPath, int major, int minor) {
        super(assetManager, rootPath, major, minor);
        
        // Custom settings
        setIdleTexture(1);
        setCurrentMajor(1);
        setCycleTime(0.25f);
    }

    // Methods
    @Override
    protected void controlUpdate(float tpf) {
        super.controlUpdate(tpf);
    }

    @Override
    protected void controlRender(RenderManager rm, ViewPort vp) {
        
    }
}
