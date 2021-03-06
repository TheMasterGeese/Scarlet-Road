package data.hullmods;

import com.fs.starfarer.api.combat.BaseHullMod;
import com.fs.starfarer.api.combat.MutableShipStatsAPI;
import com.fs.starfarer.api.combat.ShipAPI;

public class ScarletSpecializationWind extends BaseHullMod {

    private final float TOP_SPEED_MULT = 0.8f;
    private final float FLUX_CAPACITY_MULT = 0.8f;
    private final float WEAPON_RANGE_MULT = 0.8f;
    private final float WEAPON_PROJECTILE_SPEED_MULT = 0.7f;
    private final float ARMOR_MULT = 1.3f;
    private final float SYSTEM_COOLDOWN_MULT = 0.7f;


    @Override
    public void applyEffectsBeforeShipCreation(ShipAPI.HullSize hullSize, MutableShipStatsAPI stats, String id) {
        stats.getMaxSpeed().modifyMult(id, TOP_SPEED_MULT);
        stats.getFluxCapacity().modifyMult(id, FLUX_CAPACITY_MULT);
        stats.getBallisticWeaponRangeBonus().modifyMult(id, WEAPON_RANGE_MULT);
        stats.getMissileWeaponRangeBonus().modifyMult(id, WEAPON_RANGE_MULT);
        stats.getEnergyWeaponRangeBonus().modifyMult(id, WEAPON_RANGE_MULT);
        stats.getProjectileSpeedMult().modifyMult(id, WEAPON_PROJECTILE_SPEED_MULT);
        stats.getArmorBonus().modifyMult(id, ARMOR_MULT);
        stats.getSystemCooldownBonus().modifyMult(id, SYSTEM_COOLDOWN_MULT);

    }

    public void applyEffectsAfterShipCreation(ShipAPI ship, String id) {

    }


    public String getDescriptionParam(int index, ShipAPI.HullSize hullSize) {
        if (index == 0) {
            return "" + Math.round((ARMOR_MULT - 1f) * 100f) + "%";
        }
        if (index == 1) {
            return "" + Math.round((1f - SYSTEM_COOLDOWN_MULT) * 100f) + "%";
        }
        if (index == 2) {
            return "" + Math.round((WEAPON_RANGE_MULT - 1f) * 100f) + "%";
        }
        if (index == 3) {
            return "" + Math.round((WEAPON_PROJECTILE_SPEED_MULT - 1f) * 100f) + "%";
        }
        if (index == 4) {
            return "" + Math.round((TOP_SPEED_MULT - 1f) * 100f) + "%";
        }
        if (index == 5) {
            return "" + Math.round((FLUX_CAPACITY_MULT - 1f) * 100f) + "%";
        }


        return null;
    }

    public boolean isApplicableToShip(ShipAPI ship) {
        return ship != null
                && ship.getVariant().hasHullMod("scarletroad_scarletengineering")
                && !ship.getVariant().hasHullMod("scarletroad_spec_hail")
                && !ship.getVariant().hasHullMod("scarletroad_spec_lightning");
    }

    public String getUnapplicableReason(ShipAPI ship) {
        if (ship == null) {
            return "Ship is null";
        } else if (!ship.getVariant().hasHullMod("scarletroad_scarletengineering")) {
            return "The Scarlet Engineering hullmod is required.";
        } else if (ship.getVariant().hasHullMod("scarletroad_spec_lightning") ||
                ship.getVariant().hasHullMod("scarletroad_spec_hail")) {
            return "This Ship already has a Scarlet Road Specialization.";
        } else {
            return "Unknown Reason";
        }
    }
}
