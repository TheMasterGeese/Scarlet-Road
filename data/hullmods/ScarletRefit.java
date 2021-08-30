package data.hullmods;

import com.fs.starfarer.api.combat.BaseHullMod;
import com.fs.starfarer.api.combat.MutableShipStatsAPI;
import com.fs.starfarer.api.combat.ShieldAPI.ShieldType;
import com.fs.starfarer.api.combat.ShipAPI;
import com.fs.starfarer.api.combat.ShipAPI.HullSize;

import java.util.HashSet;
import java.util.Set;

public class ScarletRefit extends BaseHullMod {

    private static final Set<String> BLOCKED_HULLMODS = new HashSet<>(1);

    public static final float SHIELD_ARC = 90f;
    public static final float SPEED_MULT = 0.8f;

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

        float fluxDissipation = stats.getFluxDissipation().getBaseValue();
        float shieldUpkeepCost = stats.getShieldUpkeepMult().getBaseValue();
        stats.getFluxDissipation().modifyFlat(id, (int) fluxDissipation * -shieldUpkeepCost);
        stats.getArmorBonus().modifyMult(id, ARMOR_MULT);
        stats.getEmpDamageTakenMult().modifyMult(id, 1 - EMP_DAMAGE_TAKEN_MULT);
        stats.getSuppliesPerMonth().modifyMult(id, 1 - MAINTENANCE_COST_REDUCTION);

    }

    public void applyEffectsAfterShipCreation(ShipAPI ship, String id) {
        float upkeepCost = 0;
        // This hullmod is meant to apply to non-Scarlet Road ships. Scarlet Road ships may use the Phase type, which is why they use the Scarlet Engineering Hullmod instead
        if ((ship.getShield() != null &&
                (ship.getShield().getType() == ShieldType.FRONT ||
                        ship.getShield().getType() == ShieldType.OMNI))) {
            upkeepCost = ship.getShield().getUpkeep();
        } else if (ship.getPhaseCloak() != null) {
            upkeepCost = ship.getPhaseCloak().getFluxPerSecond();
        }
        ship.getMutableStats().getFluxDissipation().modifyFlat(id, -upkeepCost);
        ship.setShield(ShieldType.NONE, 0f, 1f, 1f);
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
        for (String tmp : BLOCKED_HULLMODS) {
            if (ship.getVariant().getHullMods().contains(tmp)) {
                return false;
            }
        }
        return !ship.getVariant().getHullMods().contains("scarletroad_scarletengineering");
    }

    public String getUnapplicableReason(ShipAPI ship) {
        for (String tmp : BLOCKED_HULLMODS) {
            if (ship.getVariant().getHullMods().contains(tmp)) {
                return "Ship has incompatible shield hullmod.";
            }
        }
        if (ship.getVariant().getHullMods().contains("scarletroad_scarletengineering")) {
            return "Ship already has the Scarlet Engineering Hullmod";
        }
        return "Ship is null";
    }
}