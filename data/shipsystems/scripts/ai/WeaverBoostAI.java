package data.shipsystems.scripts.ai;

import com.fs.starfarer.api.combat.*;
import com.fs.starfarer.api.combat.ShipwideAIFlags.AIFlags;
import com.fs.starfarer.api.util.IntervalUtil;
import com.fs.starfarer.combat.ai.AI;
import org.lwjgl.util.vector.Vector2f;

import java.util.Dictionary;
import java.util.HashMap;

import static org.lazywizard.lazylib.MathUtils.getDistance;

public class WeaverBoostAI implements ShipSystemAIScript {

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
            // used to ultimately make the decision whether to use the system or not;
            float systemPriority = 0.0f;

            // if the ship is not within 250 range of the target, return.
            if (getDistance(ship, ship.getShipTarget()) > 250) {
                return;
            }



            // maps specific AI flags to specific float values.
            // -1000 is assumed to mean "never use system while you have this flag under any circumstance"
            // 1000 is assumed to mean "always use system while you have this flag under any circumstance"
            HashMap<AIFlags, Float> flagPriorities = new HashMap<AIFlags, Float>();

                // presumably used by ships that want to turn quickly. Weaver Boost helps with that.
                flagPriorities.put(AIFlags.TURN_QUICKLY, 100f);

                // presumably used by ships that are pursuing others. Weaver Boost helps with that.
                flagPriorities.put(AIFlags.PURSUING, 100f);

                // opposite of the above
                flagPriorities.put(AIFlags.DO_NOT_PURSUE, -100f);

                // Weaver boost would help ships back off faster.
                flagPriorities.put(AIFlags.BACK_OFF, 100f);
                flagPriorities.put(AIFlags.BACK_OFF_MIN_RANGE, 100f);
                flagPriorities.put(AIFlags.BACKING_OFF, 100f);

                // the ship has already decided to cancel the system, so we'll flag it to do so.
                flagPriorities.put(AIFlags.OK_TO_CANCEL_SYSTEM_USE_TO_VENT, -1000f);
                // Weaver boost uses flux, so a flag not to use flux means we shouldn't use the system.
                flagPriorities.put(AIFlags.DO_NOT_USE_FLUX, -1000f);

                // spreading necessitates movement. If there is no need for further spreading, there should be less of a need for weaver boost.
                flagPriorities.put(AIFlags.FINISHED_SPREADING, -10f);

                // Weaver boost can help the ship move in
                flagPriorities.put(AIFlags.HARASS_MOVE_IN, 100f);

                // Attack run will require movement
                flagPriorities.put(AIFlags.IN_ATTACK_RUN, 100f);

                // Maintaining range does not require as much movement
                flagPriorities.put(AIFlags.MAINTAINING_STRIKE_RANGE, -10f);

                // Assuming this means some kind of maneuvering, weaver boost can help.
                flagPriorities.put(AIFlags.MANEUVER_TARGET, 100f);

                // Post-attack run means they're not on an attack run, and less likely to need to move.
                flagPriorities.put(AIFlags.POST_ATTACK_RUN, -10f);

                // Being at a waypoint reduces the need for movement.
                flagPriorities.put(AIFlags.REACHED_WAYPOINT, -10f);

                // Running can be helped by weaver boost.
                flagPriorities.put(AIFlags.RUN_QUICKLY, 100f);

                // standing off means no movement
                flagPriorities.put(AIFlags.STANDING_OFF_VS_SHIP_ON_MAP_BORDER, -100f);


                // needing help doesn't necessarily mean they should/should not use weaver boost.
                // flagPriorities.put(AIFlags.NEEDS_HELP, 0f);

                // If we knew the ship was trying to dodge fire this would be useful, but if it's taking it like a champ weaver boost won't help.
                // flagPriorities.put(AIFlags.HAS_INCOMING_DAMAGE, 0f);

                // Important, but the order to actually move should be on another flag.
                //flagPriorities.put(AIFlags.IN_CRITICAL_DPS_DANGER, 0f);


                // not applicable
                // flagPriorities.put(AIFlags.AUTO_BEAM_FIRING_AT_PHASE_SHIP, 0f);
                // flagPriorities.put(AIFlags.AUTO_FIRING_AT_PHASE_SHIP, 0f);
                // flagPriorities.put(AIFlags.CARRIER_FIGHTER_TARGET, 0f);
                // flagPriorities.put(AIFlags.DO_NOT_AUTOFIRE_NON_ESSENTIAL_GROUPS, 0f);
                // flagPriorities.put(AIFlags.DO_NOT_BACK_OFF, 0f);
                // flagPriorities.put(AIFlags.DO_NOT_USE_SHIELDS, 0f);
                // flagPriorities.put(AIFlags.DO_NOT_VENT, 0f);
                // flagPriorities.put(AIFlags.KEEP_SHIELDS_ON, 0f);
                // flagPriorities.put(AIFlags.PHASE_ATTACK_RUN, 0f);
                // flagPriorities.put(AIFlags.PHASE_ATTACK_RUN_FROM_BEHIND_DIST_CRITICAL, 0f);
                // flagPriorities.put(AIFlags.PHASE_ATTACK_RUN_IN_GOOD_SPOT, 0f);
                // flagPriorities.put(AIFlags.PHASE_ATTACK_RUN_TIMEOUT, 0f);
                // flagPriorities.put(AIFlags.PREFER_LEFT_BROADSIDE, 0f);
                // flagPriorities.put(AIFlags.PREFER_RIGHT_BROADSIDE, 0f);
                // flagPriorities.put(AIFlags.SAFE_FROM_DANGER_TIME, 0f);
                // flagPriorities.put(AIFlags.SAFE_VENT, 0f);
                // flagPriorities.put(AIFlags.WING_NEAR_ENEMY, 0f);
                // flagPriorities.put(AIFlags.WING_SHOULD_GET_SOME_DISTANCE, 0f);
                // flagPriorities.put(AIFlags.WING_WAS_NEAR_ENEMY, 0f);

                // no idea what these do
                // flagPriorities.put(AIFlags.AVOIDING_BORDER, 0f);
                // flagPriorities.put(AIFlags.BIGGEST_THREAT, 0f);
                // flagPriorities.put(AIFlags.DELAY_STRIKE_FIRE, 0f);
                // flagPriorities.put(AIFlags.DRONE_MOTHERSHIP, 0f);
                // flagPriorities.put(AIFlags.HARASS_MOVE_IN_COOLDOWN, 0f);
                // flagPriorities.put(AIFlags.MOVEMENT_DEST, 0f);
                // flagPriorities.put(AIFlags.SYSTEM_TARGET_COORDS, 0f);
                // flagPriorities.put(AIFlags.WANTED_TO_SLOW_DOWN, 0f);


            // system should be used more often when on lower flux and less often when on higher flux.
            float fluxLevel = ship.getFluxLevel();
            float fluxPriority = 0;
            if (fluxLevel < 0.1) {
                // There's no flux-related reason to not use weaver drive.
                fluxPriority = 0;
            } else if (fluxLevel >= 0.1 && fluxLevel < 0.75) {
                // don't use weaver drive unless there's an important reason to do so.
                fluxPriority = fluxLevel * -100;
            } else if (fluxLevel >= 0.75 && fluxLevel < 0.9) {
                // Prefer not to use weaver drive above 75% flux unless there's several critical reasons to do so.
                fluxPriority = fluxLevel * -200;
            } else if (fluxLevel >= 0.9) {
                // Don't be using Weaver boost at all about 90% flux
                fluxPriority = -1000;
            }

            systemPriority += fluxPriority;

            for (AIFlags flag : flagPriorities.keySet()) {
                if (flags.hasFlag(flag)) {
                    systemPriority += flagPriorities.get(flag);
                }
            }

            if (system.isOn() && systemPriority < 0) {
                // The weaver drive is a right-click system that is being treated like phase cloak atm.
                ship.getPhaseCloak().deactivate();
            } else if (!system.isOn() && systemPriority > 0) {
                ship.setPhased(true);
            }
        }
    }
}
