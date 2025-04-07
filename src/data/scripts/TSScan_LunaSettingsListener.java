package data.scripts;

import com.fs.starfarer.api.Global;
import com.fs.starfarer.api.impl.campaign.abilities.TSScan_SystemScaleSensorBurstAbility;
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
        TSScan_SystemScaleSensorBurstAbility.SENSOR_STRENGTH_NEEDED = LunaSettings.getFloat(ThoroughSystemScanPlugin.modID, "TSSSensorStrengthNeeded");
        TSScan_SystemScaleSensorBurstAbility.VOLATILE_MULT = LunaSettings.getFloat(ThoroughSystemScanPlugin.modID, "TSSVolatileMult");
        TSScan_SystemScaleSensorBurstAbility.VOLATILE_MULT_BLACK_HOLE = LunaSettings.getFloat(ThoroughSystemScanPlugin.modID,"TSSVolatileBlackHole");
        TSScan_SystemScaleSensorBurstAbility.VOLATILE_MULT_NEUTRON_STAR = LunaSettings.getFloat(ThoroughSystemScanPlugin.modID,"TSSVolatileNeutronStar");
        TSScan_SystemScaleSensorBurstAbility.VOLATILE_MULT_BLUE_SUPERGIANT = LunaSettings.getFloat(ThoroughSystemScanPlugin.modID,"TSSVolatileBlueSupergiant");
        TSScan_SystemScaleSensorBurstAbility.VOLATILE_MULT_RED_SUPERGIANT = LunaSettings.getFloat(ThoroughSystemScanPlugin.modID,"TSSVolatileRedSupergiant");
        TSScan_SystemScaleSensorBurstAbility.VOLATILE_MULT_ORANGE_GIANT = LunaSettings.getFloat(ThoroughSystemScanPlugin.modID,"TSSVolatileOrangeGiant");
        TSScan_SystemScaleSensorBurstAbility.VOLATILE_MULT_RED_GIANT = LunaSettings.getFloat(ThoroughSystemScanPlugin.modID,"TSSVolatileRedGiant");
        TSScan_SystemScaleSensorBurstAbility.VOLATILE_MULT_BLUE_GIANT = LunaSettings.getFloat(ThoroughSystemScanPlugin.modID,"TSSVolatileBlueGiant");
        TSScan_SystemScaleSensorBurstAbility.VOLATILE_MULT_YELLOW = LunaSettings.getFloat(ThoroughSystemScanPlugin.modID,"TSSVolatileYellow");
        TSScan_SystemScaleSensorBurstAbility.VOLATILE_MULT_ORANGE = LunaSettings.getFloat(ThoroughSystemScanPlugin.modID,"TSSVolatileOrange");
        TSScan_SystemScaleSensorBurstAbility.VOLATILE_MULT_WHITE_DWARF = LunaSettings.getFloat(ThoroughSystemScanPlugin.modID,"TSSVolatileWhiteDwarf");
        TSScan_SystemScaleSensorBurstAbility.VOLATILE_MULT_RED_DWARF = LunaSettings.getFloat(ThoroughSystemScanPlugin.modID,"TSSVolatileRedDwarf");
        TSScan_SystemScaleSensorBurstAbility.VOLATILE_MULT_BROWN_DWARF = LunaSettings.getFloat(ThoroughSystemScanPlugin.modID,"TSSVolatileBrownDwarf");
        TSScan_SystemScaleSensorBurstAbility.VOLATILE_MULT_GAS_GIANT = LunaSettings.getFloat(ThoroughSystemScanPlugin.modID,"TSSVolatileGasGiant");
        TSScan_SystemScaleSensorBurstAbility.VOLATILE_MULT_ICE_GIANT = LunaSettings.getFloat(ThoroughSystemScanPlugin.modID,"TSSVolatileIceGiant");
        TSScan_SystemScaleSensorBurstAbility.VOLATILE_MULT_PLANET = LunaSettings.getFloat(ThoroughSystemScanPlugin.modID,"TSSVolatilePlanet");
        TSScan_SystemScaleSensorBurstAbility.VOLATILE_MULT_ACCRETION_DISK = LunaSettings.getFloat(ThoroughSystemScanPlugin.modID,"TSSVolatileAccretionDisk");
        TSScan_SystemScaleSensorBurstAbility.VOLATILE_MULT_STABLE_LOCATION = LunaSettings.getFloat(ThoroughSystemScanPlugin.modID,"TSSVolatileStableLocation");
        TSScan_SystemScaleSensorBurstAbility.VOLATILE_MULT_GATE = LunaSettings.getFloat(ThoroughSystemScanPlugin.modID,"TSSVolatileGate");
        TSScan_SystemScaleSensorBurstAbility.VOLATILE_MULT_JUMP_POINT = LunaSettings.getFloat(ThoroughSystemScanPlugin.modID,"TSSVolatileJumpPoint");
        TSScan_SystemScaleSensorBurstAbility.VOLATILE_MULT_SALVAGEABLE = LunaSettings.getFloat(ThoroughSystemScanPlugin.modID,"TSSVolatileSalvageable");

        TSScan_CRLoss.CRLOSS_MULT = LunaSettings.getFloat(ThoroughSystemScanPlugin.modID, "TSSCRLossMult");
        TSScan_CRLoss.CRLOSS_MULT_FRIGATE = LunaSettings.getFloat(ThoroughSystemScanPlugin.modID, "TSSCRLossMultFrigate");
        TSScan_CRLoss.CRLOSS_MULT_DESTROYER = LunaSettings.getFloat(ThoroughSystemScanPlugin.modID, "TSSCRLossMultDestroyer");
        TSScan_CRLoss.CRLOSS_MULT_CRUISER = LunaSettings.getFloat(ThoroughSystemScanPlugin.modID, "TSSCRLossMultCruiser");
        TSScan_CRLoss.CRLOSS_MULT_CAPITAL = LunaSettings.getFloat(ThoroughSystemScanPlugin.modID, "TSSCRLossMultCapital");
    }
}
