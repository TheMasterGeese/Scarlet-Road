package data.missions.orbitalstationtest;

import com.fs.starfarer.api.combat.BaseEveryFrameCombatPlugin;
import com.fs.starfarer.api.combat.CombatEngineAPI;
import com.fs.starfarer.api.fleet.FleetGoal;
import com.fs.starfarer.api.fleet.FleetMemberType;
import com.fs.starfarer.api.mission.FleetSide;
import com.fs.starfarer.api.mission.MissionDefinitionAPI;
import com.fs.starfarer.api.mission.MissionDefinitionPlugin;

import java.util.ArrayList;
import java.util.List;

public class MissionDefinition implements MissionDefinitionPlugin {

	private final List ships = new ArrayList();

	public void defineMission(MissionDefinitionAPI api) {

        int maximumFP = 100;

        addShip("doom_Strike", 3);
        addShip("shade_Assault", 7);
        addShip("afflictor_Strike", 7);
        addShip("hyperion_Attack", 3);
        addShip("hyperion_Strike", 3);
        addShip("onslaught_Standard", 3);
        addShip("onslaught_Outdated", 3);
        addShip("onslaught_Elite", 1);
        addShip("astral_Elite", 3);
		addShip("astral_Strike", 3);
		addShip("astral_Attack", 3);
		addShip("paragon_Elite", 1);
		addShip("legion_Strike", 1);
		addShip("legion_Assault", 1);
		addShip("legion_Escort", 1);
		addShip("legion_FS", 1);
		addShip("odyssey_Balanced", 2);
		addShip("conquest_Elite", 3);
		addShip("eagle_Assault", 5);
		addShip("falcon_Attack", 5);
		addShip("venture_Balanced", 5);
		addShip("apogee_Balanced", 5);
		addShip("aurora_Balanced", 5);
		addShip("aurora_Balanced", 5);
		addShip("gryphon_FS", 7);
		addShip("gryphon_Standard", 7);
		addShip("mora_Assault", 3);
		addShip("mora_Strike", 3);
		addShip("mora_Support", 3);
		addShip("dominator_Assault", 5);
		addShip("dominator_Support", 5);
		addShip("medusa_Attack", 5);
		addShip("condor_Support", 15);
		addShip("condor_Strike", 15);
		addShip("condor_Attack", 15);
		addShip("enforcer_Assault", 15);
		addShip("enforcer_CS", 15);
		addShip("hammerhead_Balanced", 10);
		addShip("hammerhead_Elite", 5);
		addShip("drover_Strike", 10);
		addShip("sunder_CS", 10);
		addShip("gemini_Standard", 8);
		addShip("buffalo2_FS", 20);
		addShip("lasher_CS", 20);
		addShip("lasher_Standard", 20);
		addShip("hound_Standard", 15);
		addShip("tempest_Attack", 15);
		addShip("brawler_Assault", 15);
		addShip("wolf_CS", 2);
		addShip("hyperion_Strike", 1);
		addShip("vigilance_Standard", 10);
		addShip("vigilance_FS", 15);
		addShip("tempest_Attack", 2);
		addShip("brawler_Assault", 10);


		// Set up the fleets
		api.initFleet(FleetSide.PLAYER, "AHS", FleetGoal.ATTACK, false, 5);
		api.initFleet(FleetSide.ENEMY, "HSS", FleetGoal.ATTACK, true, 5);


		// Set a blurb for each fleet
        api.setFleetTagline(FleetSide.PLAYER, "Scarlet Orbital Station");
        api.setFleetTagline(FleetSide.ENEMY, "Invading Forces");

        // These show up as items in the bulleted list under
        // "Tactical Objectives" on the mission detail screen
        api.addBriefingItem("Watch the ensuing carnage");

        generateFleet(maximumFP, FleetSide.ENEMY, ships, api);

        // Set up the player's fleet
        api.addToFleet(FleetSide.PLAYER, "scarletroad_station_Standard", FleetMemberType.SHIP, "AHS Philar", true);
        //api.addToFleet(FleetSide.PLAYER, "onslaught_Standard", FleetMemberType.SHIP, "TTS Invincible", true, CrewXPLevel.ELITE);

        // Mark player flagship as essential
        api.defeatOnShipLoss("AHS Philar");

        // Set up the map.
        float width = 24000f;
        float height = 18000f;
        api.initMap(-width / 2f, width / 2f, -height / 2f, height / 2f);

		float minX = -width / 2;
		float minY = -height / 2;

		for (int i = 0; i < 15; i++) {
			float x = (float) Math.random() * width - width / 2;
			float y = (float) Math.random() * height - height / 2;
			float radius = 100f + (float) Math.random() * 900f;
			api.addNebula(x, y, radius);
		}
		api.setBackgroundSpriteName("graphics/backgrounds/hyperspace1.jpg");
		//api.setBackgroundSpriteName("graphics/backgrounds/background2.jpg");

		//system.setBackgroundTextureFilename("graphics/backgrounds/background2.jpg");
		//api.setBackgroundSpriteName();

		// Add an asteroid field going diagonally across the
		// battlefield, 2000 pixels wide, with a maximum of
		// 100 asteroids in it.
		// 20-70 is the range of asteroid speeds.
		api.addAsteroidField(0f, 0f, (float) Math.random() * 360f, width,
				20f, 70f, 100);


		api.addPlugin(new BaseEveryFrameCombatPlugin() {
			public void advance(float amount, List events) {
			}

			public void init(CombatEngineAPI engine) {
				engine.getContext().setStandoffRange(10000f);
			}
		});

	}

	private void addShip(String variant, int weight) {
		for (int i = 0; i < weight; i++) {
			ships.add(variant);
		}
	}

	private void generateFleet(int maxFP, FleetSide side, List ships, MissionDefinitionAPI api) {
		int currFP = 0;

		if (side == FleetSide.PLAYER) {
			String[] choices = {
					"onslaught_Elite",
					"astral_Strike",
					"paragon_Elite",
					"odyssey_Balanced",
					"legion_Strike",
					"legion_FS",
					"doom_Strike"
			};
			String flagship = choices[(int) (Math.random() * (float) choices.length)];
			api.addToFleet(side, flagship, FleetMemberType.SHIP, true);
			currFP += api.getFleetPointCost(flagship);
		}

		while (true) {
			int index = (int) (Math.random() * ships.size());
			String id = (String) ships.get(index);
			currFP += api.getFleetPointCost(id);
			if (currFP > maxFP) {
				return;
			}

			if (id.endsWith("_wing")) {
				api.addToFleet(side, id, FleetMemberType.FIGHTER_WING, false);
			} else {
				api.addToFleet(side, id, FleetMemberType.SHIP, false);
			}
		}
	}

}






