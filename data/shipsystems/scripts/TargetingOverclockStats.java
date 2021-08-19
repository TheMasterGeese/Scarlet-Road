package data.shipsystems.scripts;

import com.fs.starfarer.api.combat.MutableShipStatsAPI;
import com.fs.starfarer.api.impl.combat.BaseShipSystemScript;

public class TargetingOverclockStats extends BaseShipSystemScript {


    public void apply(MutableShipStatsAPI stats, String id, State state, float effectLevel) {

        stats.getBallisticWeaponRangeBonus().modifyFlat(id, 600f * effectLevel);
    }

    public void unapply(MutableShipStatsAPI stats, String id) {
        stats.getBallisticWeaponRangeBonus().unmodify(id);
    }

    public StatusData getStatusData(int index, State state, float effectLevel) {
        if (index == 0) {
            return new StatusData("Ballistic Range + " + 600, false);
        }
        return null;
    }
}
