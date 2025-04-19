package data;

import com.fs.starfarer.api.BaseModPlugin;
import com.fs.starfarer.api.Global;
import com.fs.starfarer.api.campaign.StarSystemAPI;
import data.scripts.TSScan_LunaSettingsListener;
import data.scripts.TSScan_SystemScanPointsManager;
import lunalib.lunaSettings.LunaSettings;
import org.lazywizard.lazylib.ModUtils;

import java.util.List;

public class ThoroughSystemScanPlugin extends BaseModPlugin
{
    public static final String modID="thorough_system_scan";

    @Override
    public void onApplicationLoad()
    {
        if (!Global.getSettings().getModManager().isModEnabled("lw_lazylib"))
            throw new RuntimeException("Thorough System Scan: LazyLib is required!");

        if (!ModUtils.isModEnabled("lunalib"))
            throw new RuntimeException("Thorough System Scan: LunaLib is required!");

        TSScan_LunaSettingsListener.reload();
        LunaSettings.addSettingsListener(new TSScan_LunaSettingsListener());
    }

    @Override
    public void onGameLoad(boolean newGame)
    {

        Global.getSector().getCharacterData().removeAbility(TSScan_Constants.SYSTEM_SCALE_SENSOR_BURST);
        Global.getSector().getCharacterData().addAbility(TSScan_Constants.SYSTEM_SCALE_SENSOR_BURST);
        List<StarSystemAPI> systems = Global.getSector().getStarSystems();
        for (StarSystemAPI system:systems)
            TSScan_SystemScanPointsManager.reload(system);
    }
}