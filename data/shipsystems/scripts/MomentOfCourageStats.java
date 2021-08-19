package data.shipsystems.scripts;

import com.fs.starfarer.api.combat.MutableShipStatsAPI;
import com.fs.starfarer.api.impl.combat.BaseShipSystemScript;

public class MomentOfCourageStats extends BaseShipSystemScript {

    private final float multiplier = 3.0f;

    public void apply(MutableShipStatsAPI stats, String id, State state, float effectLevel) {
        stats.getBallisticWeaponDamageMult().modifyMult(id, multiplier);
        stats.getBallisticWeaponFluxCostMod().modifyMult(id, multiplier);

        stats.getEnergyWeaponDamageMult().modifyMult(id, multiplier);
        stats.getEnergyWeaponFluxCostMod().modifyMult(id, multiplier);

        stats.getMissileWeaponDamageMult().modifyMult(id, multiplier);
        stats.getMissileWeaponFluxCostMod().modifyMult(id, multiplier);

    }

    public void unapply(MutableShipStatsAPI stats, String id) {
        stats.getBallisticWeaponDamageMult().unmodify(id);
        stats.getBallisticWeaponFluxCostMod().unmodify(id);

        stats.getEnergyWeaponDamageMult().unmodify(id);
        stats.getEnergyWeaponFluxCostMod().unmodify(id);

        stats.getMissileWeaponDamageMult().unmodify(id);
        stats.getMissileWeaponFluxCostMod().unmodify(id);

    }

    public StatusData getStatusData(int index, State state, float effectLevel) {
        if (index == 0) {
            return new StatusData("Weapon Damage + " + ((multiplier - 1f) * 100f) + "%", false);
        }
        if (index == 1) {
            return new StatusData("Weapon Flux Cost + " + ((multiplier - 1f) * 100f) + "%", false);
        }
        return null;
    }
}
