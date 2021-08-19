package data.shipsystems.scripts.ai;

import com.fs.starfarer.api.combat.*;
import com.fs.starfarer.api.combat.WeaponAPI.WeaponType;
import com.fs.starfarer.api.util.IntervalUtil;
import org.lazywizard.lazylib.MathUtils;
import org.lwjgl.util.vector.Vector2f;

import java.util.List;


/**
 * Sample ship system AI.
 * <p>
 * THIS CODE IS NOT ACTUALLY USED, AND IS PROVIDED AS AN EXAMPLE ONLY.
 * <p>
 * To enable it, uncomment the relevant lines in fastmissileracks.system.
 *
 * @author Alex Mosolov
 * <p>
 * Copyright 2012 Fractal Softworks, LLC
 */
public class TargetingOverclockAI implements ShipSystemAIScript {

    private final IntervalUtil tracker = new IntervalUtil(0.5f, 1f);
    private ShipAPI ship;
    private CombatEngineAPI engine;
    private ShipwideAIFlags flags;
    private ShipSystemAPI system;
    private float sinceLast = 0f;

    public void init(ShipAPI ship, ShipSystemAPI system, ShipwideAIFlags flags, CombatEngineAPI engine) {
        this.ship = ship;
        this.flags = flags;
        this.engine = engine;
        this.system = system;
    }

    @SuppressWarnings("unchecked")
    public void advance(float amount, Vector2f missileDangerDir, Vector2f collisionDangerDir, ShipAPI target) {
        tracker.advance(amount);

        sinceLast += amount;

        if (tracker.intervalElapsed()) {
            List weapons = ship.getAllWeapons();
            float maximumWeaponRange = 0f;
            float minimumWeaponRange = 99999999f;
            float maximumFluxThreshold = 0f;
            for (int i = 0; i < weapons.size(); i++) {
                WeaponAPI w = (WeaponAPI) (weapons.get(i));
                // The subsystem only affects ballistic weapons, we ignore all others.
                if (w.getType() != WeaponType.BALLISTIC) continue;

                float weaponRange = w.getRange();

                if (weaponRange > maximumWeaponRange) {
                    maximumWeaponRange = weaponRange;
                    maximumFluxThreshold = ship.getMaxFlux() - w.getFluxCostToFire();
                }

                if (weaponRange < minimumWeaponRange) {
                    minimumWeaponRange = weaponRange;
                }

            }

            // if you have no target, the subsystem shouldn't be on
            if (target == null) {
                if (system != null && system.isOn()) {
                    system.deactivate();
                }
                return;
            }

            Vector2f ourPosition = ship.getLocation();
            Vector2f theirPosition = target.getLocation();
            float rangeToTarget = MathUtils.getDistance(ourPosition, theirPosition);

            if (system.isOn()) {
                // Evaluate whether it should be turned off.
                if (rangeToTarget > maximumWeaponRange // Turn it off if your target is still not within range, because the range increase isn't enough and is preventing flux dissipation
                        || rangeToTarget < minimumWeaponRange - 600f// Turn it off if your target is closer than your lowest current weapon range (minus the 600 bonus already applied)
                        || ship.getFluxLevel() > maximumFluxThreshold) // Turn it off if your flux prevents you from firing effectively.
                {
                    system.deactivate();
                }
            } else {
                if (rangeToTarget < (maximumWeaponRange + 600f)
                        && rangeToTarget > maximumWeaponRange
                        && ship.getFluxLevel() < maximumFluxThreshold) {
                    ship.useSystem();
                }
            }
            return;
        }
    }
}
