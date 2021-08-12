:warning: Do not rely on anything in this guide. This is *very* early stuff! We do not know if this shader pack
system will be staying. It is purely for experimentation right now, and the pack format could change drastically without notice.

### Creating a shader pack

Shader packs are pretty similar to how Minecraft does resource packs, except you can't replace assets between 
shader packs. Each shader pack is _only_ allowed to provide assets in their own namespace.

To get started, simply create a folder for your pack in the `shaderpacks` folder. This is your workspace directory.

#### Shader pack manifest file

You need to create a manifest file so that Sodium knows how to read your shader pack. To do so, create the
file `shaderpack.json` in your workspace directory.

```json
{
    "name": "example"
}
```

... That's it. For now, at least. Probably. If this guide sounds uncertain to you, that's because it is.

### Creating a render pass

Every block in the world is assigned to a render pass, and each pass is progressively rendered during a frame to
create a composite image. Render passes tell the renderer which shader to use and how to configure it (if any
specialization is desired.)

To create a render pass, add a new file to your `passes` folder with the following information:

```json
{
  "name": "example:fancy",
  "layer": "minecraft:solid",
  "shader": {
    "source": "sodium:shaders/block_layer_generic"
  }
}
```

Right now, a render pass must extend from an existing vanilla render layer, but this will change in the future. The
`name` must be a valid identifier for your pack. The shader program can be changed to one you have created (see below),
but Sodium provides a generic shader with some knobs for specialization.

```json
{
    "name": "example:fancy",
    "layer": "minecraft:solid",
    "shader": {
        "source": "sodium:shaders/block_layer_generic",
        "constants": {
            "COLOR_MULTIPLIER": "vec4(0.5, 0.7, 0.2, 1.0)"
        }
    }
}
```

### Using a render pass

Creating a render pass is only one part of the problem. You also need to specify the blocks that should render on that
pass. Right now, this is a bit of a kludge, but can be done through modifying the `maps/block_map.json` file.

```json
{
  "blocks": {
    "minecraft:oak_leaves": "example:fancy",
    "minecraft:spruce_leaves": "example:fancy",
    "minecraft:birch_leaves": "example:fancy",
    "minecraft:jungle_leaves": "example:fancy"
  }
}
```

Each block name is assigned to a render pass name, which is the name you specified in the render pass just
a moment ago.

### Creating a shader program

In order to start adding shaders, you need to specify two pieces of information: A program description and the
associated shader files. This guide will not cover how to actually write a shader file (as it's any other GLSL shader).

To create a program description, add a JSON file within your `shaders` folder with the desired name of your shader
program.

```json
{
  "shaders": {
    "vertex": "sodium:shaders/block_layer_generic",
    "fragment": "sodium:shaders/block_layer_generic"
  },
  "uniforms": [
    { "name": "u_FogColor", "type": "vec4", "binding": "minecraft:fog_color" },
    { "name": "u_FogStart", "type": "float", "binding": "minecraft:fog_start" },
    { "name": "u_FogEnd", "type": "float", "binding": "minecraft:fog_end" },

    { "name": "u_ProjectionMatrix", "type": "mat4", "binding": "builtin:projection_matrix" },
    { "name": "u_ModelViewMatrix", "type": "mat4", "binding": "builtin:model_view_matrix" },

    { "name": "u_ModelScale", "type": "float", "binding": "sodium:position_scale" },
    { "name": "u_TextureScale", "type": "float", "binding": "sodium:texture_scale" },

    { "name": "ubo_DrawParameters", "type": "buffer", "binding": "sodium:chunk_draw_params" }
  ],
  "samplers": [
    { "name": "u_BlockTex", "type": "sampler2D", "texture": "minecraft:block_tex" },
    { "name": "u_LightTex", "type": "sampler2D", "texture": "minecraft:light_tex" }
  ]
}
```

The vertex and fragment shader sources should be replaced to point to your own files. You probably do not want to remove
any of the default uniforms/samplers, but you can add uniforms such as the world render time.

```json
{ "name": "u_WorldTime", "type": "float", "binding": "sodium:world_time" }
```

Shader sources referenced from the program description are expected to end with `.vsh` and `.fsh` for vertex and fragment
shaders respectively.

### Reloading shaders

You can reload all shader packs with F3+A, which is also the hotkey that reloads the renderer. If there are any errors
loading the shader pack, the game will just crash. This is the bleeding edge of development, after all.

### Publishing a resource pack

Simply create a ZIP file out of the contents of your workspace directory (not including the folder itself). These ZIPs
can then be placed right back into the `shaderpacks` folder, where they will be automatically loaded.