package data.scripts;

import com.fs.starfarer.api.campaign.PlanetAPI;
import com.fs.starfarer.api.campaign.SectorEntityToken;
import com.fs.starfarer.api.campaign.StarSystemAPI;
import com.fs.starfarer.api.impl.campaign.ids.Factions;
import com.fs.starfarer.api.impl.campaign.ids.Tags;
import com.fs.starfarer.api.impl.campaign.terrain.TSScan_LagrangePointAreaPlugin;
import org.lwjgl.util.vector.Vector2f;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TSScan_SystemScanPointsManager
{
    public static List<StarSystemAPI> IgnoredSystems=new ArrayList<>();
    public static Map<String,List<TSScan_LagrangePointAreaPlugin>> ScanPointsOfSystems=new HashMap<>();

    public static void reload(StarSystemAPI system)
    {
        SectorEntityToken entity=getEntityOfScanLocation(system);
        if (entity==null)
        {
            IgnoredSystems.add(system);
            return;
        }

        List<TSScan_LagrangePointAreaPlugin> ScanPoints = new ArrayList<>();
        ScanPoints.add((TSScan_LagrangePointAreaPlugin)system.addTerrain(
            TSScan_LagrangePointAreaPlugin.LAGRANGE_POINT_AREA,
            new TSScan_LagrangePointAreaPlugin.LagrangePointAreaParams(
                entity.getCircularOrbitRadius()/8f,
                (PlanetAPI)entity,
                "第四拉格朗日点",getScanLocation(system,false)
            )
        ));
        ScanPoints.add((TSScan_LagrangePointAreaPlugin)system.addTerrain(
            TSScan_LagrangePointAreaPlugin.LAGRANGE_POINT_AREA,
            new TSScan_LagrangePointAreaPlugin.LagrangePointAreaParams(
                entity.getCircularOrbitRadius()/8f,
                (PlanetAPI)entity,
                "第五拉格朗日点",getScanLocation(system,true)
            )
        ));

        ScanPointsOfSystems.put(system.getId(),ScanPoints);
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
        float angle = (entity.getCircularOrbitAngle()+60f*(L4orL5?1:-1))*(float)Math.PI/180f;
        float radius = entity.getCircularOrbitRadius();
        return new Vector2f((float)Math.cos(angle)*radius,(float)Math.sin(angle)*radius);
    }
}
