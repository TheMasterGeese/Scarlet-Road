package data.shipsystems.scripts;

import com.fs.starfarer.api.Global;
import com.fs.starfarer.api.combat.MutableShipStatsAPI;
import com.fs.starfarer.api.combat.ShipAPI;
import com.fs.starfarer.api.combat.ShipSystemAPI;
import com.fs.starfarer.api.impl.combat.BaseShipSystemScript;
import com.fs.starfarer.api.plugins.ShipSystemStatsScript;

import java.util.HashMap;
import java.util.Map;

public class IntegratedDamperStats extends BaseShipSystemScript {

    public static final float INCOMING_DAMAGE_CAPITAL = 0.5f;
    private static final Map mag = new HashMap();

    static {
        mag.put(ShipAPI.HullSize.FIGHTER, 0.5f);
        mag.put(ShipAPI.HullSize.FRIGATE, 0.5f);
        mag.put(ShipAPI.HullSize.DESTROYER, 0.5f);
        mag.put(ShipAPI.HullSize.CRUISER, 0.5f);
        mag.put(ShipAPI.HullSize.CAPITAL_SHIP, 0.5f);
    }

    protected Object STATUSKEY1 = new Object();

    public void apply(MutableShipStatsAPI stats, String id, ShipSystemStatsScript.State state, float effectLevel) {
        effectLevel = 1f;

        float mult = (Float) mag.get(ShipAPI.HullSize.CRUISER);
        if (stats.getVariant() != null) {
            mult = (Float) mag.get(stats.getVariant().getHullSize());
        }
        stats.getHullDamageTakenMult().modifyMult(id, 1f - (1f - mult) * effectLevel);
        stats.getArmorDamageTakenMult().modifyMult(id, 1f - (1f - mult) * effectLevel);
        stats.getEmpDamageTakenMult().modifyMult(id, 1f - (1f - mult) * effectLevel);

        ShipAPI ship = null;
        boolean player = false;
        if (stats.getEntity() instanceof ShipAPI) {
            ship = (ShipAPI) stats.getEntity();
            player = ship == Global.getCombatEngine().getPlayerShip();
        }
        if (player) {
            ShipSystemAPI system = ship.getSystem();
            if (system != null) {
                float percent = (1f - mult) * effectLevel * 100;
                Global.getCombatEngine().maintainStatusForPlayerShip(STATUSKEY1,
                        system.getSpecAPI().getIconSpriteName(), "Integrated Damper Field",
                        Math.round(percent) + "% less damage taken", false);
            }
        }
    }

    public void unapply(MutableShipStatsAPI stats, String id) {
        stats.getHullDamageTakenMult().unmodify(id);
        stats.getArmorDamageTakenMult().unmodify(id);
        stats.getEmpDamageTakenMult().unmodify(id);
    }
}
