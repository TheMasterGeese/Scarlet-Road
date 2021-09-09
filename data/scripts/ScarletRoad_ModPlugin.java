package data.scripts;

import com.fs.starfarer.api.BaseModPlugin;
import com.fs.starfarer.api.Global;

import data.scripts.util.dl_SpecLoadingUtils;
import data.scripts.util.dl_SubsystemUtils;
import data.scripts.world.ScarletRoadGen;
import data.subsystems.TargetingOverclockSubsystem;
import org.json.JSONException;
import java.io.IOException;

public class ScarletRoad_ModPlugin extends BaseModPlugin {


    private static void initScarletRoad() {
        new ScarletRoadGen().generate(Global.getSector());
    }

    @Override
    public void onApplicationLoad() {
        try {
            dl_SpecLoadingUtils.loadSubsystemData();
        } catch (JSONException e) {
            Global.getLogger(this.getClass()).error("Scarlet Road could not load its subsystem data");
            Global.getLogger(this.getClass()).error(e.getMessage());
        } catch (IOException e) {
            Global.getLogger(this.getClass()).error("Scarlet Road could not load its subsystem data");
            Global.getLogger(this.getClass()).error(e.getMessage());
        }

        this.applySubsystems();
        //throw new ClassCastException("Hello!");
    }

    private void applySubsystems() {
        dl_SubsystemUtils.addSubsystemToShipHull("scarletroad_dedication", TargetingOverclockSubsystem.class);
        //dl_SubsystemUtils.addSubsystemToShipHull("hammerhead", mymod_EpicDroneSubsystem.class);
    }
    @Override
    public void onNewGame() {
        Global.getLogger(this.getClass()).info("Hooray ScarletRoad plugin in a jar is loaded!");
        initScarletRoad();
    }

}
