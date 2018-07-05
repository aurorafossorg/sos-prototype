package sprite;

import com.jme3.asset.AssetManager;
import com.jme3.math.FastMath;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import com.jme3.scene.control.AbstractControl;
import com.jme3.scene.control.BillboardControl;
import com.jme3.texture.Texture;
import com.jme3.texture.Texture.MagFilter;
import com.jme3.texture.Texture.MinFilter;
import engine.audio.AudioFiles;
import engine.audio.AudioManager;

public abstract class BaseSprite extends AbstractControl {
    // Fields
    private final Texture[][] textures;
    private final int MAX_MAJOR, MAX_MINOR;
    private int currentMajor, currentMinor, idleMinor;
    private float cycleTime;
    private double timer;
    private boolean moving = false;

    // Constructors
    public BaseSprite(AssetManager assetManager, String rootPath, int major, int minor) {
        // First creates variables
        this.MAX_MAJOR = major;
        this.MAX_MINOR = minor;

        this.textures = new Texture[major][minor];

        // Then populates the array with the textures
        for(int i = 0; i < major; i++) {
            for(int j = 0; j < minor; j++) {
                String path = rootPath + (i + 1) + '-' + (j + 1) + ".png";
                Texture tex = assetManager.loadTexture(path);
                tex.setMagFilter(MagFilter.Nearest);
                tex.setMinFilter(MinFilter.NearestNearestMipMap);
                textures[i][j] = tex;
            }
        }
    }

    // Methods
    @Override
    protected void controlUpdate(float tpf) {
        if(moving) {
            timer += tpf;
            if(timer >= cycleTime) {
                timer = 0;
                
                AudioManager.playInstanceRandom(AudioFiles.GRASS_STEP1, AudioFiles.GRASS_STEP2, AudioFiles.GRASS_STEP3);
                
                currentMinor++;
                if(currentMinor > MAX_MINOR) {
                    currentMinor = 1;
                }
                
                bindTextureToGeom();
            }
        } else if(timer > 0) {
            timer = 0;

            currentMinor = idleMinor;

            bindTextureToGeom();
        }
    }
    
    @Override
    public void setSpatial(Spatial spat) {
        super.setSpatial(spat);
        
        // Add a billboard control
        BillboardControl bc = new BillboardControl();
        bc.setAlignment(BillboardControl.Alignment.AxialY);
        this.spatial.addControl(bc);
        
        currentMajor = 2;
        currentMinor = idleMinor;
        
        bindTextureToGeom();
    }

    public final void setIdleTexture(int minor) {
        idleMinor = minor;
    }
    
    public final void setCurrentMajor(int major) {
        currentMajor = (int)FastMath.clamp(major, 1, MAX_MAJOR);
        bindTextureToGeom();
    }
    
    public final void incrementMajor() {
        currentMajor++;
        
        if(currentMajor > MAX_MAJOR) {
            currentMajor = 1;
        }
        
        bindTextureToGeom();
    }
    
    public final void decrementMajor() {
        currentMajor--;
        
        if(currentMajor < 1) {
            currentMajor = MAX_MAJOR;
        }
        
        bindTextureToGeom();
    }
    
    public final void setCycleTime(float cycleTime) {
        this.cycleTime = cycleTime;
    }
    
    public final void setMoving(boolean moving) {
        this.moving = moving;
    }
    
    private void bindTextureToGeom() {
        try {
        ((Geometry)((Node)spatial).getChild("Geom")).getMaterial()
        .getTextureParam("ColorMap")
        .setTextureValue(textures[currentMajor - 1][currentMinor - 1]);
        } catch(NullPointerException npe) {
            
        }
    }
}
