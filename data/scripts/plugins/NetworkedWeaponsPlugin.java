package data.scripts.plugins;

import com.fs.starfarer.api.combat.*;
import com.fs.starfarer.api.input.InputEventAPI;
import com.fs.starfarer.api.util.IntervalUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class NetworkedWeaponsPlugin extends BaseEveryFrameCombatPlugin {

    private CombatEngineAPI engine;
    private IntervalUtil interval = new IntervalUtil(0.5f, 1f);

    public NetworkedWeaponsPlugin() {

    }

    public void init(CombatEngineAPI engine) {
        this.engine = engine;
    }
    public void advance(float amount, List<InputEventAPI> events) {
        //iterate over each ship
        for (ShipAPI ship : engine.getShips()) {
            HashMap<String, ArrayList<String>> shipWeaponIDs = new HashMap<String, ArrayList<String>>();
            //iterate over all weapons
            for (WeaponAPI weapon : ship.getAllWeapons()) {
                //if the weapon has the "networked" tag
                    //if it's not disabled
                if (weapon.getSpec().getTags().contains("networked") && !weapon.isDisabled()) {
                    if (!shipWeaponIDs.containsKey(weapon.getSpec().getWeaponId())) {
                        shipWeaponIDs.put(weapon.getSpec().getWeaponId(), new ArrayList<String>());
                    } else {
                        //save the weapon to a hashset, the keys are the ids of the weapon type, the values are arrays of the ids of copies of that weapon on the ship.
                        //possibly optimize this later by only checking the value when a weapon goes offline/comes online.
                        shipWeaponIDs.get(weapon.getSpec().getWeaponId()).add(weapon.getId());
                    }
                }
            }
            //iterate over the hashset, for each weaponid, apply the networking effect to all ship weapons, intensity based on the size of the value array.
            for
        }



                    //lookup the effect it has on networking (how?)
                    //if the weapon is not malfunctioning/offline




    }

    public void renderInUICoords(ViewportAPI viewport) {
    }

    public void renderInWorldCoords(ViewportAPI viewport) {
    }

    public void processInputPreCoreControls(float amount, List<InputEventAPI> events) {
    }
}
