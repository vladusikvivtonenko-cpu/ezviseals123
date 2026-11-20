# EzVisuals (Fabric 1.21.4)
GitHub -> Actions -> Build -> Artifacts -> ezvisuals-jar. Jar goes to .minecraft/mods with Fabric API.
Menu: Right Shift. Tab / Shift+Tab switch tabs inside the menu.

## Chat commands (typed in normal chat, never sent to the server)
  .cfg save <name>     save all settings as a config
  .cfg load <name>     load a config
  .cfg delete <name>   delete a config
  .cfg list            list configs          .cfg reset   reset everything to defaults
  .hud                 move watermark / armor / potions / cooldowns (also: menu -> HUD -> Open editor)
Configs are in .minecraft/config/ezvisuals/configs/<name>.json

## HUD
Watermark is a centered pill (Ezvisuals | ping | FPS). Armor, Potions, Cooldowns and the watermark can be dragged.
Panels behind HUD blocks are off by default (module "HUD Panels" turns them on).

Your own files, in .minecraft/config/ezvisuals/ :
  mascot.png            -> anime girl in the menu sidebar
  pictures/1.png..4.png -> Custom Pictures tab
  scripts/*.lua         -> Lua scripts (hot reload). API.md is created there automatically.
