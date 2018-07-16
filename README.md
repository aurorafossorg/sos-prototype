# SOS Concept
Shadows of Shaema (SOS) Concept

## What is this?

This is a small project to test a concept/idea for [Shadows of Shaema](http://github.com/shaema). The concept being mainly the blending of 3D and 2D mechanic.

It's a very early concept, and is currently WIP. The push to GitLab was for easier code sharing and access.

### Graphical Concept

The idea for the visual art style for the game is the following:

- 8 view (octogonal) camera
- Isometric perspective
- Pixel-art style
- 2D and 3D objects

The concept is to have a merge between 3D and 2D. The addition of the isometric perspective adds a "touch" to the art style. However, this mechanic is still very experimental, and "restrictive", so I'm not sure if it should be added.

The graphic concept is a lot similar to Paper Mario:

![Screenshot from Paper Mario](https://vignette.wikia.nocookie.net/mario/images/c/c9/Mario_In_Koopa_Village_%28Paper_Mario%29.png/revision/latest?cb=20130108193918)

Notice in this screenshot that the "characters" are 2D and look like the're drawings on paper (hence the name, Paper Mario). The buildings and the floor, however, are completely 3D. The trees merge the 2 types: the trunk is 3D and the leaves are 2D images. Although they probably did this due to limitations of the system back then, we want to explicity create objects like that.

This is the closest image I can found that describes exactly the effect I want to propose to SOS. However, I want to blend in the octogonal camera too to add another layer of "perspective" for the game.

### Gameplay Concept

Here I'm not thinking into any new idea I didn't see used before. The core gameplay aspect is already described in the [GDD](https://gitlab.com/aurorafossorg/p/sos/docs/blob/master/md/gamedoc.md).

## Compiling

The project runs under [jMonkeyEngine](http://jmonkeyengine.org/). You need JDK (>1.8) and Ant to compile. Just run this command:

`ant [build.xml]`

And the project is built under `dist/`.

**WARNING** - The project currently won't run because it's lacking some assets. These we're omitted due to possible copyrigth infringements. I'm on the process of getting open-source and free assets so people can run this project.
