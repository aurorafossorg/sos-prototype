/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package engine;

import com.jme3.input.CameraInput;
import com.jme3.input.ChaseCamera;
import com.jme3.input.InputManager;
import com.jme3.input.KeyInput;
import com.jme3.input.MouseInput;
import com.jme3.input.controls.KeyTrigger;
import com.jme3.input.controls.MouseAxisTrigger;
import com.jme3.input.controls.MouseButtonTrigger;
import com.jme3.export.binary.BinaryLoader;
import com.jme3.math.FastMath;
import com.jme3.math.Vector3f;
import com.jme3.scene.Spatial;

/**
 *
 * @author ricardo
 */
public class Camera extends ChaseCamera {
    
    private final float VERTICAL_ANGLE = FastMath.DEG_TO_RAD * 30;
    private final float HORIZONTAL_ANGLE = -FastMath.HALF_PI;
    private final float DELTA_H_ANGLE = FastMath.QUARTER_PI;
   

    public Camera(com.jme3.renderer.Camera cam, Spatial target, InputManager inputManager) {
        super(cam, target);
        this.inputManager = inputManager;
        bootstrap();
    }
    
    private void bootstrap() {
        // Locks the vertical rotation of the camera to a 30 degree isometric perspective
        setMinVerticalRotation(VERTICAL_ANGLE);
        setMaxVerticalRotation(VERTICAL_ANGLE);
        
        // Rotates the camera horizontally
        setDefaultHorizontalRotation(HORIZONTAL_ANGLE);
        
        // Sets the camera's rotation speed
        setDragToRotate(false);
        
        // Sets "smoothness"
        setSmoothMotion(true);
        setRotationSensitivity(100);
        setRotationSpeed(1);
        
        // Registers custom inputs
        registerCustomInputs();
    }
    
    private void registerCustomInputs() {
        // Default mappings
        String[] inputs = {
            CameraInput.CHASECAM_TOGGLEROTATE,
            CameraInput.CHASECAM_DOWN,
            CameraInput.CHASECAM_UP,
            CameraInput.CHASECAM_MOVELEFT,
            CameraInput.CHASECAM_MOVERIGHT,
            CameraInput.CHASECAM_ZOOMIN,
            CameraInput.CHASECAM_ZOOMOUT
        };
        
        // Defines the inputs
        if (!invertYaxis) {
            this.inputManager.addMapping(CameraInput.CHASECAM_DOWN, new MouseAxisTrigger(MouseInput.AXIS_Y, true));
            this.inputManager.addMapping(CameraInput.CHASECAM_UP, new MouseAxisTrigger(MouseInput.AXIS_Y, false));
        } else {
            this.inputManager.addMapping(CameraInput.CHASECAM_DOWN, new MouseAxisTrigger(MouseInput.AXIS_Y, false));
            this.inputManager.addMapping(CameraInput.CHASECAM_UP, new MouseAxisTrigger(MouseInput.AXIS_Y, true));
        }
        this.inputManager.addMapping(CameraInput.CHASECAM_ZOOMIN, new MouseAxisTrigger(MouseInput.AXIS_WHEEL, false));
        this.inputManager.addMapping(CameraInput.CHASECAM_ZOOMOUT, new MouseAxisTrigger(MouseInput.AXIS_WHEEL, true));
        if (!invertXaxis) {
            this.inputManager.addMapping(CameraInput.CHASECAM_MOVELEFT, new KeyTrigger(KeyInput.KEY_E));
            this.inputManager.addMapping(CameraInput.CHASECAM_MOVERIGHT, new KeyTrigger(KeyInput.KEY_Q));
        } else {
            this.inputManager.addMapping(CameraInput.CHASECAM_MOVELEFT, new KeyTrigger(KeyInput.KEY_Q));
            this.inputManager.addMapping(CameraInput.CHASECAM_MOVERIGHT, new KeyTrigger(KeyInput.KEY_E));
        }
        this.inputManager.addMapping(CameraInput.CHASECAM_TOGGLEROTATE, new MouseButtonTrigger(MouseInput.BUTTON_MIDDLE));

        this.inputManager.addListener(this, inputs);
    }
    
    @Override
    public void onAnalog(String name, float value, float tpf) {
        if(name.equals(CameraInput.CHASECAM_UP)) {
            //vRotateCamera(value);
        } else if (name.equals(CameraInput.CHASECAM_DOWN)) {
            //vRotateCamera(-value);
        }
    }
    
    @Override
    public void onAction(String name, boolean pressed, float tpf) {
        if (dragToRotate) {
            if (name.equals(CameraInput.CHASECAM_TOGGLEROTATE) && enabled) {
                if (pressed) {
                    canRotate = true;
                    if (hideCursorOnRotate) {
                        this.inputManager.setCursorVisible(false);
                    }
                } else {
                    canRotate = false;
                    if (hideCursorOnRotate) {
                        this.inputManager.setCursorVisible(true);
                    }
                }
            }
        }
        
        if(name.equals(CameraInput.CHASECAM_MOVELEFT) && pressed) {
            rotateCamera(-DELTA_H_ANGLE);
        } else if(name.equals(CameraInput.CHASECAM_MOVERIGHT) && pressed) {
            rotateCamera(DELTA_H_ANGLE);
        }
    }
    
    @Override
    public void setInvertHorizontalAxis(boolean invertXaxis) {
        this.invertXaxis = invertXaxis;
        inputManager.deleteMapping(CameraInput.CHASECAM_MOVELEFT);
        inputManager.deleteMapping(CameraInput.CHASECAM_MOVERIGHT);
        if (!invertXaxis) {
            inputManager.addMapping(CameraInput.CHASECAM_MOVELEFT, new KeyTrigger(KeyInput.KEY_E));
            inputManager.addMapping(CameraInput.CHASECAM_MOVERIGHT, new KeyTrigger(KeyInput.KEY_Q));
        } else {
            inputManager.addMapping(CameraInput.CHASECAM_MOVELEFT, new KeyTrigger(KeyInput.KEY_Q));
            inputManager.addMapping(CameraInput.CHASECAM_MOVERIGHT, new KeyTrigger(KeyInput.KEY_E));
        }
        inputManager.addListener(this, CameraInput.CHASECAM_MOVELEFT, CameraInput.CHASECAM_MOVERIGHT);
    }
    
    @Override
    protected void updateCamera(float tpf) {
        if (enabled) {
        targetLocation.set(target.getWorldTranslation()).addLocal(lookAtOffset);
        
        if (smoothMotion) {

            //computation of target direction
            targetDir.set(targetLocation).subtractLocal(prevPos);
            float dist = targetDir.length();

            //Low pass filtering on the target postition to avoid shaking when physics are enabled.
            if (offsetDistance < dist) {
                //target moves, start chasing.
                chasing = true;
                //target moves, start trailing if it has to.
                if (trailingEnabled) {
                    trailing = true;
                }
                //target moves...
                targetMoves = true;
            } else {
                //if target was moving, we compute a slight offset in rotation to avoid a rought stop of the cam
                //We do not if the player is rotationg the cam
                if (targetMoves && !canRotate) {
                    if (targetRotation - rotation > trailingRotationInertia) {
                        targetRotation = rotation + trailingRotationInertia;
                    } else if (targetRotation - rotation < -trailingRotationInertia) {
                        targetRotation = rotation - trailingRotationInertia;
                    }
                }
                //Target stops
                targetMoves = false;
            }

            //the user is rotating the cam by dragging the mouse
            if (canRotate) {
                //reseting the trailing lerp factor
                trailingLerpFactor = 0;
                //stop trailing user has the control
                trailing = false;
            }


            if (trailingEnabled && trailing) {
                if (targetMoves) {
                    //computation if the inverted direction of the target
                    Vector3f a = targetDir.negate().normalizeLocal();
                    //the x unit vector
                    Vector3f b = Vector3f.UNIT_X;
                    //2d is good enough
                    a.y = 0;
                    //computation of the rotation angle between the x axis and the trail
                    if (targetDir.z > 0) {
                        targetRotation = FastMath.TWO_PI - FastMath.acos(a.dot(b));
                    } else {
                        targetRotation = FastMath.acos(a.dot(b));
                    }
                    if (targetRotation - rotation > FastMath.PI || targetRotation - rotation < -FastMath.PI) {
                        targetRotation -= FastMath.TWO_PI;
                    }

                    //if there is an important change in the direction while trailing reset of the lerp factor to avoid jumpy movements
                    if (targetRotation != previousTargetRotation && FastMath.abs(targetRotation - previousTargetRotation) > FastMath.PI / 8) {
                        trailingLerpFactor = 0;
                    }
                    previousTargetRotation = targetRotation;
                }
                //computing lerp factor
                trailingLerpFactor = Math.min(trailingLerpFactor + tpf * tpf * trailingSensitivity, 1);
                //computing rotation by linear interpolation
                rotation = FastMath.interpolateLinear(trailingLerpFactor, rotation, targetRotation);

                //if the rotation is near the target rotation we're good, that's over
                if (targetRotation + 0.01f >= rotation && targetRotation - 0.01f <= rotation) {
                    trailing = false;
                    trailingLerpFactor = 0;
                }
            }

            //linear interpolation of the distance while chasing
            if (chasing) {
                distance = temp.set(targetLocation).subtractLocal(cam.getLocation()).length();
                distanceLerpFactor = Math.min(distanceLerpFactor + (tpf * tpf * chasingSensitivity * 0.05f), 1);
                distance = FastMath.interpolateLinear(distanceLerpFactor, distance, targetDistance);
                if (targetDistance + 0.01f >= distance && targetDistance - 0.01f <= distance) {
                    distanceLerpFactor = 0;
                    chasing = false;
                }
            }

            //linear interpolation of the distance while zooming
            if (zooming) {
                distanceLerpFactor = Math.min(distanceLerpFactor + (tpf * tpf * zoomSensitivity), 1);
                distance = FastMath.interpolateLinear(distanceLerpFactor, distance, targetDistance);
                if (targetDistance + 0.1f >= distance && targetDistance - 0.1f <= distance) {
                    zooming = false;
                    distanceLerpFactor = 0;
                }
            }

            //linear interpolation of the rotation while rotating horizontally
            if (rotating) {
                rotationLerpFactor = Math.min(rotationLerpFactor + tpf * tpf * rotationSensitivity, 1);
                rotation = FastMath.interpolateLinear(rotationLerpFactor, rotation, targetRotation);
                if (targetRotation + 0.00001f >= rotation && targetRotation - 0.00001f <= rotation) {
                    rotating = false;
                    rotationLerpFactor = 0;
                }
            }

            //linear interpolation of the rotation while rotating vertically
            if (vRotating) {
                vRotationLerpFactor = Math.min(vRotationLerpFactor + tpf * tpf * rotationSensitivity, 1);
                vRotation = FastMath.interpolateLinear(vRotationLerpFactor, vRotation, targetVRotation);
                if (targetVRotation + 0.01f >= vRotation && targetVRotation - 0.01f <= vRotation) {
                    vRotating = false;
                    vRotationLerpFactor = 0;
                }
            }
            //computing the position
            computePosition();
            //setting the position at last
            cam.setLocation(pos.addLocal(lookAtOffset));
        } else {
            //easy no smooth motion
            vRotation = targetVRotation;
            rotation = targetRotation;
            distance = targetDistance;
            computePosition();
            cam.setLocation(pos.addLocal(lookAtOffset));
        }
        //keeping track on the previous position of the target
        prevPos.set(targetLocation);

        //the cam looks at the target
        cam.lookAt(targetLocation, initialUpVec);

        }
    }
}
