package data.hullmods;

import com.fs.starfarer.api.combat.BaseHullMod;
import com.fs.starfarer.api.combat.MutableShipStatsAPI;
import com.fs.starfarer.api.combat.ShieldAPI.ShieldType;
import com.fs.starfarer.api.combat.ShipAPI;
import com.fs.starfarer.api.combat.ShipAPI.HullSize;
import data.scripts.util.MagicIncompatibleHullmods;

import java.util.HashSet;
import java.util.Set;

public class ScarletEngineering extends BaseHullMod {


    private static final Set<String> BLOCKED_HULLMODS = new HashSet<>(1);

    private final float ARMOR_MULT = 1.1f;
    private final float EMP_DAMAGE_TAKEN_MULT = 0.5f;
    private final float MAINTENANCE_COST_REDUCTION = 0.25f;

    static {
        BLOCKED_HULLMODS.add("advancedshieldemitter");
        BLOCKED_HULLMODS.add("extendedshieldemitter");
        BLOCKED_HULLMODS.add("frontemitter");
        BLOCKED_HULLMODS.add("shieldshunt");
        BLOCKED_HULLMODS.add("frontshield");
        BLOCKED_HULLMODS.add("hardenedshieldemitter");
        BLOCKED_HULLMODS.add("adaptiveshields");
    }

    @Override
    public void applyEffectsBeforeShipCreation(HullSize hullSize, MutableShipStatsAPI stats, String id) {

        stats.getArmorBonus().modifyMult(id, ARMOR_MULT);
        stats.getEmpDamageTakenMult().modifyMult(id, 1 - EMP_DAMAGE_TAKEN_MULT);
        stats.getSuppliesPerMonth().modifyMult(id, 1 - MAINTENANCE_COST_REDUCTION);

    }

    public void applyEffectsAfterShipCreation(ShipAPI ship, String id) {
        for (String tmp : BLOCKED_HULLMODS) {
            if (ship.getVariant().getHullMods().contains(tmp)) {
                MagicIncompatibleHullmods.removeHullmodWithWarning(ship.getVariant(),tmp,"scarletroad_scarletengineering");
            }
        }

        // This hullmod is meant to apply to non-Scarlet Road ships. Scarlet Road ships may use the Phase type, which is why they use the Scarlet Engineering Hullmod instead
        if ((ship.getShield() != null &&
                (ship.getShield().getType() == ShieldType.FRONT ||
                        ship.getShield().getType() == ShieldType.OMNI)))
        {
            ship.setShield(ShieldType.NONE, 0f, 1f, 1f);
        }

    }


    public String getDescriptionParam(int index, HullSize hullSize) {
        if (index == 0) {
            return "" + Math.round((ARMOR_MULT - 1f) * 100f) + "%";
        }
        if (index == 1) {
            return "" + Math.round((EMP_DAMAGE_TAKEN_MULT) * 100f) + "%";
        }
        if (index == 2) {
            return "" + Math.round((MAINTENANCE_COST_REDUCTION) * 100f) + "%";
        }

        return null;
    }

    public boolean isApplicableToShip(ShipAPI ship) {
        return ship != null;
    }

    public String getUnapplicableReason(ShipAPI ship) {
        return "Ship is null";
    }
}