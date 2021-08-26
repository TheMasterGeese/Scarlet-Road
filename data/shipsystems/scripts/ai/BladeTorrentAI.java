package data.shipsystems.scripts.ai;

import com.fs.starfarer.api.combat.*;
import com.fs.starfarer.api.util.IntervalUtil;
import org.lwjgl.util.vector.Vector2f;

import static org.lazywizard.lazylib.MathUtils.clampAngle;
import static org.lazywizard.lazylib.MathUtils.getDistance;

public class BladeTorrentAI implements ShipSystemAIScript {

    private final IntervalUtil tracker = new IntervalUtil(0.5f, 1f);
    private ShipAPI ship;
    private CombatEngineAPI engine;
    private ShipwideAIFlags flags;
    private ShipSystemAPI system;
    private float sinceLast = 0f;


    @Override
    public void init(ShipAPI ship, ShipSystemAPI system, ShipwideAIFlags flags, CombatEngineAPI engine) {
        this.ship = ship;
        this.flags = flags;
        this.engine = engine;
        this.system = system;
    }

    @Override
    public void advance(float amount, Vector2f missileDangerDir, Vector2f collisionDangerDir, ShipAPI target) {
        tracker.advance(amount);

        sinceLast += amount;

        if (tracker.intervalElapsed()) {
            boolean highDamagePotential = false;
            boolean missilesOrStrikeCraftInRange = false;

            if (ship == null) {
                return;
            }
            if (getDistance(ship, ship.getShipTarget()) > 250 ||
                    ship.getSystem().isCoolingDown()) {
                return;
            }


            if (highDamagePotential(target) ){ //|| missileOrStrikeCraftInRange()) {
                ship.useSystem();
            }
        }
    }

    /**
     * Method to evaluate whether the amount of damage Blade Torrent can deal is "worthwhile".
     * <p>
     * There are two rules this is evaluated by:
     * <p>
     * -If at least one blade thrower can deal damage unmitigated by armor or shields, it is worthwhile.
     * -If the total damage that can be dealt is theoretically enough to overload the ship, it is worthwhile.
     *
     * @param target The target of the ship with the Blade Torrent subsystem.
     * @return true if the amount of damage Blade Torrent can deal is "worthwhile", false otherwise.
     */
    private boolean highDamagePotential(ShipAPI target) {

        // the ship's current amount of available flux. Blade torrent generates high flux, and the amount of available
        // flux can limit the amount of blade throwers that can fire.
        int availableFlux = (int) (ship.getMaxFlux() - ship.getCurrFlux());

        // This tracks how much flux is expected to be generated from blade throwers that will end up hitting enemy
        // shields,
        float totalFluxDamagePotential = 0f;


        // iterate over all the blade throwers
        for (WeaponAPI weapon : ship.getAllWeapons()) {
            if (weapon.getId().equals("scarletroad_bladethrower")) {

                // If you don't have the flux to fire this blade thrower, break, since you won't have the flux to fire
                // the other ones either.
                if (weapon.getFluxCostToFire() > availableFlux) {
                    break;
                } else {
                    boolean isShieldBlocking = false;

                    // if the target has phase cloak or no shield, damage to shields is not applicable.
                    if (target.getPhaseCloak() == null && target.getShield() != null) {
                        // is the shield currently in a position to block the projectiles from the blade thrower?
                        isShieldBlocking = target.getShield().isWithinArc(weapon.getLocation());
                        if (isShieldBlocking) {
                            // If so, damage dealt by the blade thrower will be converted to flux, and will contribute
                            // towards the amount of flux damage this could deal.
                            totalFluxDamagePotential += weapon.getDerivedStats().getBurstDamage() * 0.25f
                                    * target.getShield().getFluxPerPointOfDamage();
                        }
                    }

                    // is there a minimal amount of armor on the part of the ship the blade thrower is going to fire on?
                    boolean isArmorBlocking = target.getAverageArmorInSlice(
                            (clampAngle(ship.getFacing() + 180f)), 60f
                    ) > 100;

                    // If at least one blade thrower can deal damage unmitigated by armor or shields, it is worthwhile.
                    if (!isShieldBlocking && !isArmorBlocking) {
                        return true;
                    }
                    // we assume we will fire this weapon, so we now have less available flux to fire the others.
                    availableFlux -= weapon.getFluxCostToFire();
                }
            }
        }
        // If the total damage that can be dealt is theoretically enough to overload the ship, it is worthwhile.
        return target.getCurrFlux() + totalFluxDamagePotential > target.getMaxFlux();
    }
}
