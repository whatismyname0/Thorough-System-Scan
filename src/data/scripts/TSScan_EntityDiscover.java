package data.scripts;

import com.fs.starfarer.api.Global;
import com.fs.starfarer.api.campaign.CampaignFleetAPI;
import com.fs.starfarer.api.campaign.SectorEntityToken;
import com.fs.starfarer.api.campaign.StarSystemAPI;
import data.TSScan_Constants;

import java.util.*;


public class TSScan_EntityDiscover {


    private final Map<String,Float> entitiesPair = new HashMap<>();

    private final List<SectorEntityToken> entities;

    public TSScan_EntityDiscover(StarSystemAPI location)
    {
        if (TSScan_Constants.REPORT_SHOULD_DISPLAY)
            TSSCan_SalvageableValue.reset();

        entities=location.getAllEntities();

        for (SectorEntityToken entity:entities)
        {
            if (TSScan_Constants.REPORT_SHOULD_DISPLAY)
                TSSCan_SalvageableValue.estimateEntitySalvageValue(entity);

            if (entity.getSensorProfile() != 0F && entity != Global.getSector().getPlayerFleet())
            {
                entitiesPair.put(entity.getId(), entity.getSensorProfile());

                if (entity instanceof CampaignFleetAPI) ((CampaignFleetAPI) entity).setForceNoSensorProfileUpdate(true);
                entity.setSensorProfile(null);
            }
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
            fleet.getStats().getSensorProfileMod().unmodify(TSScan_Constants.SYSTEM_SCALE_SENSOR_BURST);
            fleet.setDetectionRangeDetailsOverrideMult(entitiesPair.get(fleet.getId()));
            fleet.setForceNoSensorProfileUpdate(false);
        }
    }
}
