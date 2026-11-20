package com.ezvisuals;

/** Positions of the movable HUD widgets. Stored as screen fractions so they survive any GUI scale / resolution. */
public final class HudPos {
    private HudPos() {}

    public static final int LEFT = 0, CENTER = 1, RIGHT = 2;

    public static final class Widget {
        public final String id, label;
        public final int anchor;
        public final double defFx, defFy;
        public double fx, fy;
        /** Size and top-left corner from the last time the widget was drawn. */
        public int w, h, px, py;

        Widget(String id, String label, int anchor, double fx, double fy) {
            this.id = id; this.label = label; this.anchor = anchor;
            this.defFx = fx; this.defFy = fy;
            this.fx = fx; this.fy = fy;
        }

        public void reset() { fx = defFx; fy = defFy; }
    }

    public static final Widget WATERMARK = new Widget("watermark", "Watermark", CENTER, 0.5, 0.016);
    public static final Widget ARMOR = new Widget("armor", "Armor", LEFT, 0.012, 0.30);
    public static final Widget POTIONS = new Widget("potions", "Potions", LEFT, 0.012, 0.64);
    public static final Widget COOLDOWNS = new Widget("cooldowns", "Cooldowns", RIGHT, 0.988, 0.42);
    public static final Widget[] ALL = {WATERMARK, ARMOR, POTIONS, COOLDOWNS};

    private static int clamp(int v, int lo, int hi) { return Math.max(lo, Math.min(Math.max(lo, hi), v)); }

    public static int x(Widget wd, int sw) {
        double base = wd.fx * sw;
        int x = (int) Math.round(wd.anchor == LEFT ? base : wd.anchor == CENTER ? base - wd.w / 2.0 : base - wd.w);
        return clamp(x, 2, sw - wd.w - 2);
    }

    public static int y(Widget wd, int sh) {
        return clamp((int) Math.round(wd.fy * sh), 2, sh - wd.h - 2);
    }

    /** Moves the widget so its top-left corner is at x, y (screen pixels). Snaps to the screen center. */
    public static void set(Widget wd, int x, int y, int sw, int sh) {
        x = clamp(x, 2, sw - wd.w - 2);
        y = clamp(y, 2, sh - wd.h - 2);
        double ax = wd.anchor == LEFT ? x : wd.anchor == CENTER ? x + wd.w / 2.0 : x + wd.w;
        if (Math.abs(x + wd.w / 2.0 - sw / 2.0) < 4) ax = wd.anchor == LEFT ? sw / 2.0 - wd.w / 2.0
                : wd.anchor == CENTER ? sw / 2.0 : sw / 2.0 + wd.w / 2.0;
        wd.fx = ax / sw;
        wd.fy = y / (double) sh;
    }

    public static void resetAll() {
        for (Widget w : ALL) w.reset();
    }
}
