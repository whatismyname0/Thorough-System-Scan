package data.scripts;

import data.TSScan_Constants;
import data.ThoroughSystemScanPlugin;
import lunalib.lunaSettings.LunaSettings;
import lunalib.lunaSettings.LunaSettingsListener;

public class TSScan_LunaSettingsListener implements LunaSettingsListener
{
    @Override
    public void settingsChanged(String modID)
    {
        if (modID.equals(ThoroughSystemScanPlugin.modID))reload();
    }
    public static void reload()
    {
        TSScan_Constants.SENSOR_STRENGTH_NEEDED = LunaSettings.getFloat(ThoroughSystemScanPlugin.modID, "TSSSensorStrengthNeeded");
        TSScan_Constants.VOLATILE_MULT = LunaSettings.getFloat(ThoroughSystemScanPlugin.modID, "TSSVolatileMult");
        TSScan_Constants.VOLATILE_MULT_BLACK_HOLE = LunaSettings.getFloat(ThoroughSystemScanPlugin.modID,"TSSVolatileBlackHole");
        TSScan_Constants.VOLATILE_MULT_NEUTRON_STAR = LunaSettings.getFloat(ThoroughSystemScanPlugin.modID,"TSSVolatileNeutronStar");
        TSScan_Constants.VOLATILE_MULT_BLUE_SUPERGIANT = LunaSettings.getFloat(ThoroughSystemScanPlugin.modID,"TSSVolatileBlueSupergiant");
        TSScan_Constants.VOLATILE_MULT_RED_SUPERGIANT = LunaSettings.getFloat(ThoroughSystemScanPlugin.modID,"TSSVolatileRedSupergiant");
        TSScan_Constants.VOLATILE_MULT_ORANGE_GIANT = LunaSettings.getFloat(ThoroughSystemScanPlugin.modID,"TSSVolatileOrangeGiant");
        TSScan_Constants.VOLATILE_MULT_RED_GIANT = LunaSettings.getFloat(ThoroughSystemScanPlugin.modID,"TSSVolatileRedGiant");
        TSScan_Constants.VOLATILE_MULT_BLUE_GIANT = LunaSettings.getFloat(ThoroughSystemScanPlugin.modID,"TSSVolatileBlueGiant");
        TSScan_Constants.VOLATILE_MULT_YELLOW = LunaSettings.getFloat(ThoroughSystemScanPlugin.modID,"TSSVolatileYellow");
        TSScan_Constants.VOLATILE_MULT_ORANGE = LunaSettings.getFloat(ThoroughSystemScanPlugin.modID,"TSSVolatileOrange");
        TSScan_Constants.VOLATILE_MULT_WHITE_DWARF = LunaSettings.getFloat(ThoroughSystemScanPlugin.modID,"TSSVolatileWhiteDwarf");
        TSScan_Constants.VOLATILE_MULT_RED_DWARF = LunaSettings.getFloat(ThoroughSystemScanPlugin.modID,"TSSVolatileRedDwarf");
        TSScan_Constants.VOLATILE_MULT_BROWN_DWARF = LunaSettings.getFloat(ThoroughSystemScanPlugin.modID,"TSSVolatileBrownDwarf");
        TSScan_Constants.VOLATILE_MULT_GAS_GIANT = LunaSettings.getFloat(ThoroughSystemScanPlugin.modID,"TSSVolatileGasGiant");
        TSScan_Constants.VOLATILE_MULT_ICE_GIANT = LunaSettings.getFloat(ThoroughSystemScanPlugin.modID,"TSSVolatileIceGiant");
        TSScan_Constants.VOLATILE_MULT_PLANET = LunaSettings.getFloat(ThoroughSystemScanPlugin.modID,"TSSVolatilePlanet");
        TSScan_Constants.VOLATILE_MULT_ACCRETION_DISK = LunaSettings.getFloat(ThoroughSystemScanPlugin.modID,"TSSVolatileAccretionDisk");
        TSScan_Constants.VOLATILE_MULT_STABLE_LOCATION = LunaSettings.getFloat(ThoroughSystemScanPlugin.modID,"TSSVolatileStableLocation");
        TSScan_Constants.VOLATILE_MULT_GATE = LunaSettings.getFloat(ThoroughSystemScanPlugin.modID,"TSSVolatileGate");
        TSScan_Constants.VOLATILE_MULT_JUMP_POINT = LunaSettings.getFloat(ThoroughSystemScanPlugin.modID,"TSSVolatileJumpPoint");
        TSScan_Constants.VOLATILE_MULT_SALVAGEABLE = LunaSettings.getFloat(ThoroughSystemScanPlugin.modID,"TSSVolatileSalvageable");

        TSScan_Constants.CRLOSS_MULT = LunaSettings.getFloat(ThoroughSystemScanPlugin.modID, "TSSCRLossMult");
        TSScan_Constants.CRLOSS_MULT_FRIGATE = LunaSettings.getFloat(ThoroughSystemScanPlugin.modID, "TSSCRLossMultFrigate");
        TSScan_Constants.CRLOSS_MULT_DESTROYER = LunaSettings.getFloat(ThoroughSystemScanPlugin.modID, "TSSCRLossMultDestroyer");
        TSScan_Constants.CRLOSS_MULT_CRUISER = LunaSettings.getFloat(ThoroughSystemScanPlugin.modID, "TSSCRLossMultCruiser");
        TSScan_Constants.CRLOSS_MULT_CAPITAL = LunaSettings.getFloat(ThoroughSystemScanPlugin.modID, "TSSCRLossMultCapital");

        TSScan_Constants.MAP_SHOULD_DISPLAY = LunaSettings.getBoolean(ThoroughSystemScanPlugin.modID, "TSSMapDisplay");
    }
}
