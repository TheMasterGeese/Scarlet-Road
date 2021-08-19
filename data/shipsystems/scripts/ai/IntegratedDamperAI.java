package data.shipsystems.scripts.ai;

import com.fs.starfarer.api.combat.*;
import com.fs.starfarer.api.util.IntervalUtil;
import org.lazywizard.lazylib.MathUtils;
import org.lwjgl.util.vector.Vector2f;

import java.util.List;

public class IntegratedDamperAI implements ShipSystemAIScript {

    private final IntervalUtil tracker = new IntervalUtil(0.5f, 1f);
    private ShipAPI ship;
    private CombatEngineAPI engine;
    private ShipwideAIFlags flags;
    private ShipSystemAPI system;
    private float sinceLast = 0f;

    public void init(ShipAPI ship, ShipSystemAPI system, ShipwideAIFlags flags, CombatEngineAPI engine) {
        this.ship = ship;
        this.flags = flags;
        this.engine = engine;
        this.system = system;
    }

    public void advance(float amount, Vector2f missileDangerDir, Vector2f collisionDangerDir, ShipAPI target) {
        tracker.advance(amount);

        sinceLast += amount;

        // logic should be similar to shields, you raise them in response to incoming damage, if damage is negligible don't bother.
        // if you're in range of enemy ships with beams, always account for those in incoming damage.

        // As flux level gets higher, become increasingly more likely to drop the damper field.
        // As your hull gets lower, become increasingly less likely to drop the damper field.

        if (tracker.intervalElapsed()) {

        }
    }
}
