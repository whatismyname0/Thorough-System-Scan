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

    protected static Object getValue(String type, String id)
    {
        switch (type)
        {
            case "int":return LunaSettings.getInt(ThoroughSystemScanPlugin.modID,id);
            case "float":return LunaSettings.getFloat(ThoroughSystemScanPlugin.modID,id);
            case "double":return LunaSettings.getDouble(ThoroughSystemScanPlugin.modID,id);
            case "string":return LunaSettings.getString(ThoroughSystemScanPlugin.modID,id);
            case "boolean":return LunaSettings.getBoolean(ThoroughSystemScanPlugin.modID,id);
            case "color":return LunaSettings.getColor(ThoroughSystemScanPlugin.modID,id);
            default:return null;
        }
    }

    @SuppressWarnings("DataFlowIssue")
    public static void reload()
    {
        TSScan_Constants.SENSOR_STRENGTH_NEEDED = (Float)getValue("float", "TSSSensorStrengthNeeded");
        TSScan_Constants.VOLATILE_MULT = (Float)getValue("float", "TSSVolatileMult");
        TSScan_Constants.VOLATILE_MULT_BLACK_HOLE = (Float)getValue("float","TSSVolatileBlackHole");
        TSScan_Constants.VOLATILE_MULT_NEUTRON_STAR = (Float)getValue("float","TSSVolatileNeutronStar");
        TSScan_Constants.VOLATILE_MULT_BLUE_SUPERGIANT = (Float)getValue("float","TSSVolatileBlueSupergiant");
        TSScan_Constants.VOLATILE_MULT_RED_SUPERGIANT = (Float)getValue("float","TSSVolatileRedSupergiant");
        TSScan_Constants.VOLATILE_MULT_ORANGE_GIANT = (Float)getValue("float","TSSVolatileOrangeGiant");
        TSScan_Constants.VOLATILE_MULT_RED_GIANT = (Float)getValue("float","TSSVolatileRedGiant");
        TSScan_Constants.VOLATILE_MULT_BLUE_GIANT = (Float)getValue("float","TSSVolatileBlueGiant");
        TSScan_Constants.VOLATILE_MULT_YELLOW = (Float)getValue("float","TSSVolatileYellow");
        TSScan_Constants.VOLATILE_MULT_ORANGE = (Float)getValue("float","TSSVolatileOrange");
        TSScan_Constants.VOLATILE_MULT_WHITE_DWARF = (Float)getValue("float","TSSVolatileWhiteDwarf");
        TSScan_Constants.VOLATILE_MULT_RED_DWARF = (Float)getValue("float","TSSVolatileRedDwarf");
        TSScan_Constants.VOLATILE_MULT_BROWN_DWARF = (Float)getValue("float","TSSVolatileBrownDwarf");
        TSScan_Constants.VOLATILE_MULT_GAS_GIANT = (Float)getValue("float","TSSVolatileGasGiant");
        TSScan_Constants.VOLATILE_MULT_ICE_GIANT = (Float)getValue("float","TSSVolatileIceGiant");
        TSScan_Constants.VOLATILE_MULT_PLANET = (Float)getValue("float","TSSVolatilePlanet");
        TSScan_Constants.VOLATILE_MULT_ACCRETION_DISK = (Float)getValue("float","TSSVolatileAccretionDisk");
        TSScan_Constants.VOLATILE_MULT_STABLE_LOCATION = (Float)getValue("float","TSSVolatileStableLocation");
        TSScan_Constants.VOLATILE_MULT_GATE = (Float)getValue("float","TSSVolatileGate");
        TSScan_Constants.VOLATILE_MULT_JUMP_POINT = (Float)getValue("float","TSSVolatileJumpPoint");
        TSScan_Constants.VOLATILE_MULT_SALVAGEABLE = (Float)getValue("float","TSSVolatileSalvageable");

        TSScan_Constants.CR_LOSS_MULT = (Float)getValue("float", "TSSCRLossMult");
        TSScan_Constants.CR_LOSS_MULT_FRIGATE = (Float)getValue("float", "TSSCRLossMultFrigate");
        TSScan_Constants.CR_LOSS_MULT_DESTROYER = (Float)getValue("float", "TSSCRLossMultDestroyer");
        TSScan_Constants.CR_LOSS_MULT_CRUISER = (Float)getValue("float", "TSSCRLossMultCruiser");
        TSScan_Constants.CR_LOSS_MULT_CAPITAL = (Float)getValue("float", "TSSCRLossMultCapital");

        TSScan_Constants.MAP_SHOULD_DISPLAY = (Boolean)getValue("boolean", "TSSMapDisplay");
        TSScan_Constants.REPORT_SHOULD_DISPLAY = (Boolean)getValue("boolean", "TSSReportDisplay");
    }
}
