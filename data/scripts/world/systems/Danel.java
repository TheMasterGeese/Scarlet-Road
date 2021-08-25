package data.scripts.world.systems;

import java.awt.Color;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import com.fs.starfarer.api.Global;
import com.fs.starfarer.api.campaign.*;
import com.fs.starfarer.api.campaign.econ.EconomyAPI;
import com.fs.starfarer.api.campaign.econ.MarketAPI;
import com.fs.starfarer.api.impl.campaign.ids.*;
import com.fs.starfarer.api.impl.campaign.procgen.NebulaEditor;
import com.fs.starfarer.api.impl.campaign.procgen.PlanetConditionGenerator;
import com.fs.starfarer.api.impl.campaign.procgen.StarAge;
import com.fs.starfarer.api.impl.campaign.terrain.AsteroidFieldTerrainPlugin;
import com.fs.starfarer.api.impl.campaign.terrain.HyperspaceTerrainPlugin;
import com.fs.starfarer.api.util.Misc;

import org.lazywizard.lazylib.MathUtils;

public class Danel {
    public void generate(SectorAPI sector) {

        StarSystemAPI system = sector.createStarSystem("Danel");
        system.getLocation().set(-39000, 39000);

        system.setBackgroundTextureFilename("graphics/backgrounds/background5.jpg");

        // create the star and generate the hyperspace anchor for this system
        PlanetAPI danelStar = system.initStar("Danel", // unique id for this star
                "star_red_giant", // id in planets.json
                1100f, // radius (in pixels at default zoom)
                450); // corona radius, from star edge
        system.setLightColor(new Color(239, 155, 128)); // light color in entire system, affects all entities

        SectorEntityToken danelAF1 = system.addTerrain(Terrain.ASTEROID_FIELD,
                new AsteroidFieldTerrainPlugin.AsteroidFieldParams(
                        200f, // min radius
                        300f, // max radius
                        8, // min asteroid count
                        16, // max asteroid count
                        4f, // min asteroid radius
                        16f, // max asteroid radius
                        "Asteroids Field")); // null for default name
        danelAF1.setCircularOrbit(danelStar, 130, 800, 240);

        //add first stable loc
        SectorEntityToken stableLoc1 = system.addCustomEntity("danel_stableloc_1", "Stable Location", "stable_location", "scarletroad");
        stableLoc1.setCircularOrbit(danelStar, MathUtils.getRandomNumberInRange(0f, 360f), 1600, 520);

        //asteroid belt1 ring
        system.addAsteroidBelt(danelStar, 1000, 2400, 800, 250, 400, Terrain.ASTEROID_BELT, "Inner Band");
        system.addRingBand(danelStar, "misc", "rings_asteroids0", 256f, 3, Color.gray, 256f, 2400 - 200, 250f);
        system.addRingBand(danelStar, "misc", "rings_asteroids0", 256f, 0, Color.gray, 256f, 2400, 350f);
        system.addRingBand(danelStar, "misc", "rings_asteroids0", 256f, 2, Color.gray, 256f, 2400 + 200, 400f);

        // Hye-Steel: Useful world way far out, ruins, decivilized
        PlanetAPI hye_steel = system.addPlanet("hye_steel",
                danelStar,
                "Hye-Steel",
                "frozen",
                360 * (float) Math.random(),
                190f,
                3200,
                3421f);
        hye_steel.setCustomDescriptionId("scarletroad_danel_hye_steel"); //reference descriptions.csv
        hye_steel.getMarket().addCondition(Conditions.RUINS_WIDESPREAD);
        hye_steel.getMarket().addCondition(Conditions.VERY_COLD);
        hye_steel.getMarket().addCondition(Conditions.DECIVILIZED);
        hye_steel.getMarket().addCondition(Conditions.DARK);
        hye_steel.getMarket().addCondition(Conditions.ORE_ULTRARICH);
        hye_steel.getMarket().addCondition(Conditions.RARE_ORE_MODERATE);

        // Danel Gamma: Dead World
        PlanetAPI danelGamma = system.addPlanet("danel_gamma",
                danelStar,
                "Danel Gamma",
                "barren-bombarded",
                360f * (float) Math.random(),
                320f,
                4800,
                1421f);
        danelGamma.setCustomDescriptionId("scarletroad_danel_danelGamma"); //reference descriptions.csv
        PlanetConditionGenerator.generateConditionsForPlanet(danelGamma, StarAge.AVERAGE);

        // Danel Prime: Terran homeworld
        PlanetAPI danelPrime = system.addPlanet("danel_prime",
                danelStar,
                "Danel Prime",
                "terran",
                360f * (float) Math.random(),
                220f,
                6400,
                320f);

        danelPrime.setCustomDescriptionId("scarletroad_danel_danelprime"); //reference descriptions.csv

        MarketAPI danelPrime_market = addMarketplace("scarletroad", danelPrime, null,
                "Danel Prime",
                6,
                Arrays.asList(
                        Conditions.POPULATION_6,
                        Conditions.ORE_RICH,
                        Conditions.RARE_ORE_ABUNDANT,
                        Conditions.FARMLAND_BOUNTIFUL,
                        Conditions.HABITABLE,
                        Conditions.ORGANIZED_CRIME,
                        Conditions.TERRAN,
                        Conditions.REGIONAL_CAPITAL,
                        Conditions.STEALTH_MINEFIELDS,
                        Conditions.AI_CORE_ADMIN
                ),
                Arrays.asList(
                        Submarkets.GENERIC_MILITARY,
                        Submarkets.SUBMARKET_OPEN,
                        Submarkets.SUBMARKET_STORAGE,
                        Submarkets.SUBMARKET_BLACK
                ),
                Arrays.asList(
                        Industries.POPULATION,
                        Industries.MEGAPORT,
                        Industries.MINING,
                        Industries.ORBITALSTATION,
                        Industries.HEAVYBATTERIES,
                        Industries.HIGHCOMMAND,
                        Industries.WAYSTATION
                ),
                0.18f,
                true,
                true);

        danelPrime_market.addIndustry(Industries.ORBITALWORKS, Collections.singletonList(Items.PRISTINE_NANOFORGE)); //couldn't find another way to add w/ forge!
        danelPrime_market.getIndustry(Industries.HIGHCOMMAND).setAICoreId(Commodities.ALPHA_CORE);
        danelPrime_market.getIndustry(Industries.ORBITALSTATION).setAICoreId(Commodities.ALPHA_CORE);
        danelPrime_market.getIndustry(Industries.MEGAPORT).setAICoreId(Commodities.ALPHA_CORE);
        danelPrime_market.getIndustry(Industries.ORBITALWORKS).setAICoreId(Commodities.ALPHA_CORE);
        danelPrime_market.getIndustry(Industries.POPULATION).setAICoreId(Commodities.BETA_CORE);
        danelPrime_market.getIndustry(Industries.WAYSTATION).setAICoreId(Commodities.GAMMA_CORE);

        system.autogenerateHyperspaceJumpPoints(true, true);

        HyperspaceTerrainPlugin plugin = (HyperspaceTerrainPlugin) Misc.getHyperspaceTerrain().getPlugin();
        NebulaEditor editor = new NebulaEditor(plugin);
        float minRadius = plugin.getTileSize() * 2f;

        float radius = system.getMaxRadiusInHyperspace();
        editor.clearArc(system.getLocation().x, system.getLocation().y, 0, radius + minRadius, 0, 360f);
        editor.clearArc(system.getLocation().x, system.getLocation().y, 0, radius + minRadius, 0, 360f, 0.25f);

    }

    /**
     * Shorthand function for adding a market -- this is derived from tahlan mod
     */
    public static MarketAPI addMarketplace(String factionID, SectorEntityToken primaryEntity, List<SectorEntityToken> connectedEntities, String name,
                                           int popSize, List<String> marketConditions, List<String> submarkets, List<String> industries, float tariff,
                                           boolean isFreePort, boolean floatyJunk) {
        EconomyAPI globalEconomy = Global.getSector().getEconomy();
        String planetID = primaryEntity.getId();
        String marketID = planetID + "_market"; //IMPORTANT this is a naming convention for markets. didn't want to have to pass in another variable :D

        MarketAPI newMarket = Global.getFactory().createMarket(marketID, name, popSize);
        newMarket.setFactionId(factionID);
        newMarket.setPrimaryEntity(primaryEntity);
        //newMarket.getTariff().modifyFlat("generator", tariff);
        newMarket.getTariff().setBaseValue(tariff);

        //Add submarkets, if any
        if (null != submarkets) {
            for (String market : submarkets) {
                newMarket.addSubmarket(market);
            }
        }

        //Add conditions
        for (String condition : marketConditions) {
            newMarket.addCondition(condition);
        }

        //Add industries
        for (String industry : industries) {
            newMarket.addIndustry(industry);
        }

        //Set free port
        newMarket.setFreePort(isFreePort);

        //Add connected entities, if any
        if (null != connectedEntities) {
            for (SectorEntityToken entity : connectedEntities) {
                newMarket.getConnectedEntities().add(entity);
            }
        }

        //set market in global, factions, and assign market, also submarkets
        globalEconomy.addMarket(newMarket, floatyJunk);
        primaryEntity.setMarket(newMarket);
        primaryEntity.setFaction(factionID);

        if (null != connectedEntities) {
            for (SectorEntityToken entity : connectedEntities) {
                entity.setMarket(newMarket);
                entity.setFaction(factionID);
            }
        }

        //Finally, return the newly-generated market
        return newMarket;
    }
}