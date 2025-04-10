package data.scripts;

import com.fs.starfarer.api.campaign.CampaignFleetAPI;
import com.fs.starfarer.api.campaign.SectorEntityToken;
import com.fs.starfarer.api.campaign.StarSystemAPI;
import data.TSScan_constants;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class TSScan_EntityDiscover {

    private Map<String,Float> entitiesPair=new HashMap<>();

    private List<SectorEntityToken> entities;

    public TSScan_EntityDiscover(StarSystemAPI location)
    {
        entities=location.getAllEntities();
        for (SectorEntityToken entity:entities)
            if (entity.getSensorProfile()!=0.0F)
            {
                entitiesPair.put(entity.getId(),entity.getSensorProfile());

                if (entity instanceof CampaignFleetAPI)((CampaignFleetAPI)entity).setForceNoSensorProfileUpdate(true);
                entity.setSensorProfile(null);
            }
    }
    public void recoverEntities()
    {
        for (SectorEntityToken entity:entities)
        {
            if (entitiesPair.get(entity.getId())==null)continue;
            if (!entity.isAlive())continue;
            if (!(entity instanceof CampaignFleetAPI))continue;
            CampaignFleetAPI fleet=(CampaignFleetAPI)entity;
            fleet.getStats().getSensorProfileMod().unmodify(TSScan_constants.SYSTEM_SCALE_SENSOR_BURST);
            fleet.setDetectionRangeDetailsOverrideMult(entitiesPair.get(fleet.getId()));
            fleet.setForceNoSensorProfileUpdate(false);
        }
    }
}
