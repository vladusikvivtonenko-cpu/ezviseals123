package com.ezvisuals;

import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.text.Text;
import org.lwjgl.glfw.GLFW;

/** Drag the watermark, armor, potions and cooldowns anywhere. Esc / Right Shift to finish, R to reset. */
public class HudEditor extends Screen {
    private final Screen parent;
    private HudPos.Widget drag;
    private int offX, offY;

    public HudEditor(Screen parent) {
        super(Text.literal("HUD Editor"));
        this.parent = parent;
    }

    @Override
    public boolean shouldPause() { return false; }

    @Override
    public void renderBackground(DrawContext context, int mouseX, int mouseY, float delta) {
        // no vanilla blur
    }

    @Override
    public void close() {
        client.setScreen(parent);
    }

    @Override
    public void removed() {
        Config.saveMain();
    }

    @Override
    public void render(DrawContext c, int mx, int my, float delta) {
        Theme t = Theme.cur();
        c.fill(0, 0, width, height, 0x28000000);
        // center guide
        for (int y = 0; y < height; y += 6) c.fill(width / 2, y, width / 2 + 1, y + 3, 0x30FFFFFF);

        for (HudPos.Widget wd : HudPos.ALL) {
            Hud.drawWidget(wd, c, client, delta, true);
            int pad = 3;
            boolean hot = wd == drag || (drag == null && inside(wd, mx, my));
            int col = hot ? t.accent : Draw.alpha(t.accent, 0x70);
            Draw.outline(c, wd.px - pad, wd.py - pad, wd.w + pad * 2, wd.h + pad * 2, col);
            String label = wd.label + (isEnabled(wd) ? "" : " (off)");
            Draw.shadow(c, label, wd.px - pad, wd.py - pad - 10, hot ? 0xFFFFFFFF : 0xFFB4B4C4);
        }

        Draw.textC(c, "Drag the blocks where you want them", width / 2, height - 34, 0xFFFFFFFF);
        Draw.textC(c, "R = reset positions    Esc / Right Shift = done", width / 2, height - 22, Draw.SUB);
    }

    private static boolean isEnabled(HudPos.Widget wd) {
        if (wd == HudPos.WATERMARK) return Modules.WATERMARK.enabled;
        if (wd == HudPos.ARMOR) return Modules.ARMOR_HUD.enabled;
        if (wd == HudPos.POTIONS) return Modules.POTION_HUD.enabled;
        return Modules.COOLDOWNS.enabled;
    }

    private static boolean inside(HudPos.Widget wd, double mx, double my) {
        return mx >= wd.px - 3 && mx < wd.px + wd.w + 3 && my >= wd.py - 3 && my < wd.py + wd.h + 3;
    }

    @Override
    public boolean mouseClicked(double mx, double my, int button) {
        if (button == 0) {
            for (int i = HudPos.ALL.length - 1; i >= 0; i--) {
                HudPos.Widget wd = HudPos.ALL[i];
                if (inside(wd, mx, my)) {
                    drag = wd;
                    offX = (int) mx - wd.px;
                    offY = (int) my - wd.py;
                    return true;
                }
            }
        }
        return super.mouseClicked(mx, my, button);
    }

    @Override
    public boolean mouseDragged(double mx, double my, int button, double dx, double dy) {
        if (drag != null) {
            HudPos.set(drag, (int) mx - offX, (int) my - offY, width, height);
            return true;
        }
        return super.mouseDragged(mx, my, button, dx, dy);
    }

    @Override
    public boolean mouseReleased(double mx, double my, int button) {
        drag = null;
        return super.mouseReleased(mx, my, button);
    }

    @Override
    public boolean keyPressed(int key, int scan, int mods) {
        if (key == GLFW.GLFW_KEY_R) {
            HudPos.resetAll();
            return true;
        }
        if (key == GLFW.GLFW_KEY_ESCAPE || EzVisualsPremium.menuKey.matchesKey(key, scan)) {
            close();
            return true;
        }
        return super.keyPressed(key, scan, mods);
    }
}
