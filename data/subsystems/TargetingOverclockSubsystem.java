package data.subsystems;

import com.fs.starfarer.api.combat.MutableShipStatsAPI;
import com.fs.starfarer.api.combat.WeaponAPI;
import data.scripts.subsystems.dl_BaseSubsystem;
import org.lazywizard.lazylib.MathUtils;
import org.lwjgl.util.vector.Vector2f;

import java.util.List;

public class TargetingOverclockSubsystem extends dl_BaseSubsystem {

    public static final String SUBSYSTEM_ID = "scarletroad_targetingoverclock";

    public TargetingOverclockSubsystem() {
        super(SUBSYSTEM_ID);
    }

    @Override
    public void apply(MutableShipStatsAPI stats, String id, SubsystemState state, float effectLevel) {

        stats.getBallisticWeaponRangeBonus().modifyFlat(id, 600f * effectLevel);
        stats.getFluxDissipation().modifyFlat(id, -stats.getFluxDissipation().getModifiedValue());
    }

    @Override
    public void unapply(MutableShipStatsAPI stats, String id) {
        stats.getBallisticWeaponRangeBonus().unmodify(id);
        stats.getFluxDissipation().unmodify(id);
    }

    @Override
    public String getStatusString() {
        return null; //use default text
    }

    @Override
    public String getInfoString() {
        if (isActive()) return "BALLISTIC RANGE INCREASED";
        return "OVERCLOCK READY";
    }

    @Override
    public String getFlavourString() {
        return "INCREASED BALLISTIC RANGE";
    }

    @Override
    public int getNumGuiBars() { //number of slots the subsystem uses in combat GUI
        return 1;
    }

    @Override
    public void aiInit() {
        //do nothing
    }

    @Override
    public void aiUpdate(float v) {

        if (ship == null || !ship.isAlive()) return;

        List weapons = ship.getAllWeapons();
        float maximumWeaponRange = 0f;
        float minimumWeaponRange = 99999999f;
        float maximumFluxThreshold = 0f;
        for (int i = 0; i < weapons.size(); i++) {
            WeaponAPI w = (WeaponAPI) (weapons.get(i));
            // The subsystem only affects ballistic weapons, we ignore all others.
            if (w.getType() != WeaponAPI.WeaponType.BALLISTIC) continue;

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
        if (ship.getShipTarget() == null) {
            if (isOn()) {
                activate();
            }
            return;
        }

        Vector2f ourPosition = ship.getLocation();
        Vector2f theirPosition = ship.getShipTarget().getLocation();
        float rangeToTarget = MathUtils.getDistance(ourPosition, theirPosition);

        if (isOn()) {
            // Evaluate whether it should be turned off.
            if (rangeToTarget > maximumWeaponRange // Turn it off if your target is still not within range, because the range increase isn't enough and is preventing flux dissipation
                    || rangeToTarget < minimumWeaponRange - 600f// Turn it off if your target is closer than your lowest current weapon range (minus the 600 bonus already applied)
                    || ship.getFluxLevel() > maximumFluxThreshold) // Turn it off if your flux prevents you from firing effectively.
            {
               activate();
            }
        } else {
            if (rangeToTarget < (maximumWeaponRange + 600f)
                    && rangeToTarget > maximumWeaponRange
                    && ship.getFluxLevel() < maximumFluxThreshold) {
                activate();
            }
        }
        return;

    }

}
