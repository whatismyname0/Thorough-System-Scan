package data;

import com.fs.starfarer.api.BaseModPlugin;
import com.fs.starfarer.api.Global;
import com.fs.starfarer.api.campaign.StarSystemAPI;
import com.fs.starfarer.api.impl.campaign.abilities.TSScan_SystemScaleSensorBurstAbility;
import data.scripts.TSScan_LunaSettingsListener;
import data.scripts.TSScan_SystemScanPointsManager;
import lunalib.lunaSettings.LunaSettings;

import java.util.List;

public class ThoroughSystemScanPlugin extends BaseModPlugin
{
    public static final String modID="thorough_system_scan";

    @Override
    public void onApplicationLoad()
    {
        if (Global.getSettings().getModManager().isModEnabled("LunaLib"))
        {
            TSScan_LunaSettingsListener.reload();
            LunaSettings.addSettingsListener(new TSScan_LunaSettingsListener());
        }
    }

    @Override
    public void onGameLoad(boolean newGame)
    {
        Global.getSector().getCharacterData().removeAbility(TSScan_SystemScaleSensorBurstAbility.SYSTEM_SCALE_SENSOR_BURST);
        Global.getSector().getCharacterData().addAbility(TSScan_SystemScaleSensorBurstAbility.SYSTEM_SCALE_SENSOR_BURST);
        List<StarSystemAPI> systems = Global.getSector().getStarSystems();
        for (StarSystemAPI system:systems)
            TSScan_SystemScanPointsManager.reload(system);
    }
}