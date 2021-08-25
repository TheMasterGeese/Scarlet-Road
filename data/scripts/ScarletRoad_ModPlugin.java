package data.scripts;

import com.fs.starfarer.api.BaseModPlugin;
import com.fs.starfarer.api.Global;
import data.scripts.world.ScarletRoadGen;


public class ScarletRoad_ModPlugin extends BaseModPlugin {

    public static boolean hasGraphicsLib;

    private static void initScarletRoad() {
        new ScarletRoadGen().generate(Global.getSector());
    }

    @Override
    public void onApplicationLoad() {
        //throw new ClassCastException("Hello!");
    }

    @Override
    public void onNewGame() {
        Global.getLogger(this.getClass()).info("Hooray ScarletRoad plugin in a jar is loaded!");
        initScarletRoad();
    }

}
