package data;

import com.fs.starfarer.api.Global;

public class TSScan_Constants {
    public static final String SYSTEM_SCALE_SENSOR_BURST = "system_scale_sensor_burst";
    public final static String LAGRANGE_POINT_AREA = "lagrange_point_area";


    public static float SENSOR_STRENGTH_NEEDED = Global.getSettings().getFloat("TSSSensorStrengthNeeded");

    public static float VOLATILE_MULT = Global.getSettings().getFloat("TSSVolatileMult");
    public static float VOLATILE_MULT_BLACK_HOLE = Global.getSettings().getFloat("TSSVolatileBlackHole");
    public static float VOLATILE_MULT_NEUTRON_STAR = Global.getSettings().getFloat("TSSVolatileNeutronStar");
    public static float VOLATILE_MULT_BLUE_SUPERGIANT = Global.getSettings().getFloat("TSSVolatileBlueSupergiant");
    public static float VOLATILE_MULT_RED_SUPERGIANT = Global.getSettings().getFloat("TSSVolatileRedSupergiant");
    public static float VOLATILE_MULT_ORANGE_GIANT = Global.getSettings().getFloat("TSSVolatileOrangeGiant");
    public static float VOLATILE_MULT_RED_GIANT = Global.getSettings().getFloat("TSSVolatileRedGiant");
    public static float VOLATILE_MULT_BLUE_GIANT = Global.getSettings().getFloat("TSSVolatileBlueGiant");
    public static float VOLATILE_MULT_YELLOW = Global.getSettings().getFloat("TSSVolatileYellow");
    public static float VOLATILE_MULT_ORANGE = Global.getSettings().getFloat("TSSVolatileOrange");
    public static float VOLATILE_MULT_WHITE_DWARF = Global.getSettings().getFloat("TSSVolatileWhiteDwarf");
    public static float VOLATILE_MULT_RED_DWARF = Global.getSettings().getFloat("TSSVolatileRedDwarf");
    public static float VOLATILE_MULT_BROWN_DWARF = Global.getSettings().getFloat("TSSVolatileBrownDwarf");
    public static float VOLATILE_MULT_GAS_GIANT = Global.getSettings().getFloat("TSSVolatileGasGiant");
    public static float VOLATILE_MULT_ICE_GIANT = Global.getSettings().getFloat("TSSVolatileIceGiant");
    public static float VOLATILE_MULT_PLANET = Global.getSettings().getFloat("TSSVolatilePlanet");
    public static float VOLATILE_MULT_ACCRETION_DISK = Global.getSettings().getFloat("TSSVolatileAccretionDisk");
    public static float VOLATILE_MULT_STABLE_LOCATION = Global.getSettings().getFloat("TSSVolatileStableLocation");
    public static float VOLATILE_MULT_GATE = Global.getSettings().getFloat("TSSVolatileGate");
    public static float VOLATILE_MULT_JUMP_POINT = Global.getSettings().getFloat("TSSVolatileJumpPoint");
    public static float VOLATILE_MULT_SALVAGEABLE = Global.getSettings().getFloat("TSSVolatileSalvageable");

    public static float CRLOSS_MULT = Global.getSettings().getFloat("TSSCRLossMult");
    public static float CRLOSS_MULT_FRIGATE = Global.getSettings().getFloat("TSSCRLossMultFrigate");
    public static float CRLOSS_MULT_DESTROYER = Global.getSettings().getFloat("TSSCRLossMultDestroyer");
    public static float CRLOSS_MULT_CRUISER = Global.getSettings().getFloat("TSSCRLossMultCruiser");
    public static float CRLOSS_MULT_CAPITAL = Global.getSettings().getFloat("TSSCRLossMultCapital");

    public static boolean MAP_SHOULD_DISPLAY = Global.getSettings().getBoolean("TSSMapDisplay");
}
