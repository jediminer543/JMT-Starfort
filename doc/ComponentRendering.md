# Component Rendering

This hopefully describes the component rendering pipeline such that people can understand how it works

## Rendering

This is currently written for the 2d renderer

If the 3d one exists and isn't documented, raise an issue

### Rendering operation

The 2d render works like this:

1. Sweep accross gridspace of world
2. Access block at gridspace - Concurrency issues result in this tile being skipped
3. Match each component to a rendering rule if one exists
4. Draw all the rendering rules in order of z-priority

### Render Rules

There are several standard render rules

#### Generic

Draws a static texture for a given component; applying the material colour to the texture

#### Masking

Draws a static texture for a given component; applying the material colour to the parts of the texture specified by the mask

#### Directional

Draws a static texture based upon the directionality of the component. Applies colour like generic.

## Shading

This assumes you are using the standard shaders.

### Shader Flags

Component shader has the following flags: 

* Colour - Should colour be added or should the default texture color be used
* Depth - Should the depth effect be added
* Mask - Should the application of color be masked

### Future

#### MultiColourMask

In the future there will hopefully be a Complex mask mode, which will shade based upon the mask colour

(I.e. if you split each of the 4 colour channel into 4 levels, and reserve the default states (entirely 0ed and entirely 1ed) then you have (4^4)-2 = 254 possible colours to apply to a given texture)

Currently the human shaders do this, but this represents the generalisation of that.

#### TextureCombine

Allow for the addition of material effects to blocks; i.e. adding a blobby pattern to cobblestone, or a specular point to metalic surfaces.