package data.scripts;

import com.fs.starfarer.api.campaign.PlanetAPI;
import com.fs.starfarer.api.campaign.SectorEntityToken;
import com.fs.starfarer.api.campaign.StarSystemAPI;
import com.fs.starfarer.api.impl.campaign.ids.Tags;
import com.fs.starfarer.api.impl.campaign.terrain.TSScan_LagrangePointAreaPlugin;
import com.fs.starfarer.campaign.CampaignTerrain;
import data.TSScan_Constants;
import org.lwjgl.util.vector.Vector2f;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TSScan_SystemScanPointsManager
{
    public static List<String> IgnoredSystems=new ArrayList<>();
    public static Map<String,List<SectorEntityToken>> ScanPointsOfSystems=new HashMap<>();

    public static void reload(StarSystemAPI system)
    {
        SectorEntityToken entity=getEntityOfScanLocation(system);
        List<SectorEntityToken> terrains=system.getEntitiesWithTag(Tags.TERRAIN);

        for (SectorEntityToken terrain:terrains)
        {
            if (!(terrain instanceof CampaignTerrain))continue;
            CampaignTerrain terrain1 = (CampaignTerrain) terrain;
            if (!terrain1.getType().equals(TSScan_Constants.LAGRANGE_POINT_AREA))continue;
            ((TSScan_LagrangePointAreaPlugin)terrain1.getPlugin()).delete();
        }

        if (entity==null)
        {
            IgnoredSystems.add(system.getId());
            return;
        }

        List<SectorEntityToken> ScanPoints = new ArrayList<>();
        ScanPoints.add(system.addTerrain(
            TSScan_Constants.LAGRANGE_POINT_AREA,
            new TSScan_LagrangePointAreaPlugin.LagrangePointAreaParams(
                entity.getCircularOrbitRadius()/8f,
                (PlanetAPI)entity,
                "第四拉格朗日点",
                getScanLocation(system,false),
                false
            )
        ));
        ScanPoints.add(system.addTerrain(
            TSScan_Constants.LAGRANGE_POINT_AREA,
            new TSScan_LagrangePointAreaPlugin.LagrangePointAreaParams(
                entity.getCircularOrbitRadius()/8f,
                (PlanetAPI)entity,
                "第五拉格朗日点",
                getScanLocation(system,true),
                true
            )
        ));

        ScanPointsOfSystems.remove(system.getId());
        ScanPointsOfSystems.put(system.getId(),ScanPoints);
    }

    public static void unload()
    {
        IgnoredSystems = new ArrayList<>();
        ScanPointsOfSystems = new HashMap<>();
    }

    public static SectorEntityToken getEntityOfScanLocation(StarSystemAPI system)
    {
        List<SectorEntityToken> stars = system.getEntitiesWithTag(Tags.STAR);
        List<SectorEntityToken> gas_giants = system.getEntitiesWithTag(Tags.GAS_GIANT);
        List<SectorEntityToken> planets = system.getEntitiesWithTag(Tags.PLANET);
        planets.addAll(stars);
        planets.addAll(gas_giants);
        if (planets.isEmpty())return null;
        float maxRadius=0f;
        SectorEntityToken candidate=null;
        for (SectorEntityToken planet:planets)
            if (planet.getCircularOrbitRadius()>maxRadius&&planet.getOrbitFocus()==system.getCenter())
            {
                maxRadius=planet.getCircularOrbitRadius();
                candidate=planet;
            }
        return candidate;
    }
    public static Vector2f getScanLocation(StarSystemAPI location, boolean L4orL5)
    {
        SectorEntityToken entity = getEntityOfScanLocation(location);
        if (entity == null)return null;
        float angle = (entity.getCircularOrbitAngle()+60f*(L4orL5?1:-1))*(float)Math.PI/180f;
        float radius = entity.getCircularOrbitRadius();
        return new Vector2f((float)Math.cos(angle)*radius,(float)Math.sin(angle)*radius);
    }
}
