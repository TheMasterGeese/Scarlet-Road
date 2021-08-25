package data.scripts.world;

import com.fs.starfarer.api.campaign.FactionAPI;
import com.fs.starfarer.api.campaign.SectorAPI;
import com.fs.starfarer.api.impl.campaign.ids.Factions;
import data.scripts.world.systems.Danel;

public class ScarletRoadGen {
    public void generate(SectorAPI sector) {
        new Danel().generate(sector);
        initFactionRelationships(sector);
    }

    private void initFactionRelationships(SectorAPI sector) {
        FactionAPI hegemony = sector.getFaction(Factions.HEGEMONY);
        FactionAPI tritachyon = sector.getFaction(Factions.TRITACHYON);
        FactionAPI pirates = sector.getFaction(Factions.PIRATES);
        FactionAPI independent = sector.getFaction(Factions.INDEPENDENT);
        FactionAPI kol = sector.getFaction(Factions.KOL);
        FactionAPI church = sector.getFaction(Factions.LUDDIC_CHURCH);
        FactionAPI path = sector.getFaction(Factions.LUDDIC_PATH);
        FactionAPI player = sector.getFaction(Factions.PLAYER);
        FactionAPI diktat = sector.getFaction(Factions.DIKTAT);
        FactionAPI league = sector.getFaction(Factions.PERSEAN);
        FactionAPI scarlet = sector.getFaction("scarletroad");

        player.setRelationship(scarlet.getId(), 0);

        scarlet.setRelationship(hegemony.getId(), -0.75f);
        scarlet.setRelationship(pirates.getId(), -0.6f);
        scarlet.setRelationship(diktat.getId(), -0.6f);

        scarlet.setRelationship(tritachyon.getId(), -0.75f);

        scarlet.setRelationship(independent.getId(), -0.3f);
        scarlet.setRelationship(league.getId(), 0.5f);

        church.setRelationship(scarlet.getId(), 0.5f);
        path.setRelationship(scarlet.getId(), 0f);
        kol.setRelationship(scarlet.getId(), 0.5f);
    }
}
