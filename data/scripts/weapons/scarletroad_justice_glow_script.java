package data.scripts.weapons;

import com.fs.starfarer.api.Global;
import com.fs.starfarer.api.combat.CombatEngineAPI;
import com.fs.starfarer.api.combat.EveryFrameWeaponEffectPlugin;
import com.fs.starfarer.api.combat.ShipAPI;
import com.fs.starfarer.api.combat.WeaponAPI;
import org.lazywizard.lazylib.MathUtils;
import org.lwjgl.util.vector.Vector2f;

import java.awt.*;

public class scarletroad_justice_glow_script implements EveryFrameWeaponEffectPlugin {
    private static final float[] COLOR_NORMAL = {216f / 255f, 47f / 255f, 77f / 255f};
    private static final float[] COLOR_OVERDRIVE = {207f / 255f, 131f / 255f, 0f / 255f};
    private static final float[] COLOR_SYSTEM = {216f / 255f, 47f / 255f, 77f / 255f};
    private static final float MAX_JITTER_DISTANCE = 0.8f;
    private static final float MAX_OPACITY = 1f;
    private static final float TRIGGER_PERCENTAGE = 0.3f;

    @Override
    public void advance(float amount, CombatEngineAPI engine, WeaponAPI weapon) {
        if (Global.getCombatEngine().isPaused()) {
            return;
        }

        ShipAPI ship = weapon.getShip();
        if (ship == null) {
            return;
        }

        float currentBrightness;
        //Brightness based on flux under normal conditions
        if (ship.getSystem().isActive()) {
            currentBrightness = 1.0f;
        } else {
            currentBrightness = 0.0f;
        }

        //No glows on wrecks or in refit
        if (ship.isPiece() || !ship.isAlive() || ship.getOriginalOwner() == -1) {
            currentBrightness = 0f;
        }

        //Switches to the proper sprite
        if (currentBrightness > 0) {
            weapon.getAnimation().setFrame(1);
        } else {
            weapon.getAnimation().setFrame(0);
        }

        //Now, set the color to the one we want, and include opacity
        Color colorToUse = new Color(COLOR_NORMAL[0], COLOR_NORMAL[1], COLOR_NORMAL[2], currentBrightness * MAX_OPACITY);

        //And finally actually apply the color
        weapon.getSprite().setColor(colorToUse);

        //Jitter! Jitter based on our maximum jitter distance and our flux level
        if (currentBrightness > 0.8) {
            Vector2f randomOffset = MathUtils.getRandomPointInCircle(new Vector2f(weapon.getSprite().getWidth() / 2f, weapon.getSprite().getHeight() / 2f), MAX_JITTER_DISTANCE);
            weapon.getSprite().setCenter(randomOffset.x, randomOffset.y);
        }
    }
}