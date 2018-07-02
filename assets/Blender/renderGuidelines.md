# Render Guidelines

1. Center the view on the object in question. *(press `.` basically)*
1. Rotate to an isometric view *(Numpad 1 > 3x 6 > 2x 8)* for an ortographic render **OR** to any desired angle for a perspective render.
1. Make a plane big enough to fit **AND** set it's color to **#B8E72C**.
1. Add a camera, if not already, and set it's view to current view. *(Ctrl + Alt + Numpad 0)*
1. Add a sun, if not already, and set it's rotation to around 45º on the X and Z axis. *(facing the object)*
1. Change to *Cycles Render*.
1. Set the object to be rendered a **Diffuse BSDF** with the *Color* variable to a **Image Texture**.
    - The **Image Texture**'s *Interpolation* should be **Closest** for the *pixel-art* look.
1. The light should have an **Emission** node with a *Strength* value equal to the desired effect *(bright or loomy)*.
1. Render must be at **100%** scale, *1920 x 1080* resolution. Naming convention: *ConceptArtRender-[nameOfObject]-[ortho/perspective]-[X]*, where X is the number.
