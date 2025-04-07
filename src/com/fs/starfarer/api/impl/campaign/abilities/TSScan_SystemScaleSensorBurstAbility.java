package com.fs.starfarer.api.impl.campaign.abilities;

import com.fs.starfarer.api.Global;
import com.fs.starfarer.api.campaign.*;
import com.fs.starfarer.api.combat.ShipAPI;
import com.fs.starfarer.api.fleet.FleetMemberAPI;
import com.fs.starfarer.api.impl.campaign.ids.*;
import com.fs.starfarer.api.impl.campaign.terrain.AsteroidBeltTerrainPlugin;
import com.fs.starfarer.api.impl.campaign.terrain.BaseRingTerrain;
import com.fs.starfarer.api.ui.TooltipMakerAPI;
import com.fs.starfarer.api.util.Misc;
import com.fs.starfarer.campaign.CampaignTerrain;
import data.scripts.TSScan_CRLoss;
import data.scripts.TSScan_EntityDiscover;
import data.scripts.TSScan_SystemScanPointsManager;
import org.lwjgl.util.vector.Vector2f;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class TSScan_SystemScaleSensorBurstAbility extends BaseDurationAbility {

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

	public static final String SYSTEM_SCALE_SENSOR_BURST = "system_scale_sensor_burst";

	private static TSScan_EntityDiscover nowDiscovery=null;

	private static LocationAPI initialLocation;

	@Override
	protected void activateImpl() {
		if (entity.isInCurrentLocation()) {
			if (!Global.getSettings().isDevMode())
			{
				initialLocation=getFleet().getContainingLocation();
				TSScan_CRLoss.CRLoss(false,null);
				getFleet().getCargo().removeCommodity(Commodities.VOLATILES,(int)computeVolatileCost());
			}
			Global.getSector().addPing(entity, SYSTEM_SCALE_SENSOR_BURST);
		}
	}

	@Override
	protected void applyEffect(float amount, float level) {

		if (getFleet().getContainingLocation()!=initialLocation)
		{
			deactivateImpl();
			return;
		}
		CampaignFleetAPI fleet = getFleet();
		if (fleet == null) return;

		fleet.getStats().getSensorRangeMod().modifyFlat(getModId(), ((int)(9000f*Math.random())%(10000-getFleet().getSensorRangeMod().computeEffective(getFleet().getSensorStrength()))), "广域传感器扫描");
		fleet.getStats().getDetectedRangeMod().modifyFlat(getModId(), 30000f, "广域传感器扫描");
		if (level>=.8f&&nowDiscovery==null)nowDiscovery=new TSScan_EntityDiscover(fleet.getStarSystem());

		fleet.goSlowOneFrame();
	}

	@Override
	protected void deactivateImpl() {
		cleanupImpl();
	}

	@Override
	protected void cleanupImpl() {
		CampaignFleetAPI fleet = getFleet();
		if (fleet == null) return;
		if (nowDiscovery!=null)nowDiscovery.recoverEntities();
		nowDiscovery=null;
		fleet.getStats().getSensorRangeMod().unmodify(getModId());
		fleet.getStats().getDetectedRangeMod().unmodify(getModId());
	}

	@Override
	public void createTooltip(TooltipMakerAPI tooltip, boolean expanded) {
		Color gray = Misc.getGrayColor();
		Color highlight = Misc.getHighlightColor();
		Color alarm = Color.red;

		tooltip.addTitle(spec.getName());

		float pad = 10f;
		tooltip.addPara("超载舰队传感器阵列以获得几乎全星系范围内的信息", pad);
		tooltip.addPara("将传感器探测范围暂时扩展至全星系,并且极大增加你舰队的被探测范围.当该能力启用时舰队只能 %s** .",
				pad, highlight,"缓慢移动");
		tooltip.addPara("受引力场影响,若星系除中心天体外有其他天体,只能在最高轨道天体的L4或L5点附近进行广域传感器扫描.",
				pad);
		if (getFleet().getSensorRangeMod().computeEffective(getFleet().getSensorStrength())<SENSOR_STRENGTH_NEEDED)
		{
			tooltip.addPara("警告:你的舰队传感器强度必须达到1000才能有效进行广域传感器扫描!", alarm, pad);
			addIncompatibleToTooltip(tooltip, expanded);
			return;
		}
		if (getFleet().isInHyperspace())
			tooltip.addPara("警告:无法在超空间启用广域传感器扫描!",alarm,pad);
		else {
			tooltip.addPara("扫描当前星系需要 %s 单位挥发物",pad,highlight,""+(int)computeVolatileCost());
			if ((int) computeVolatileCost() > getFleet().getCargo().getCommodityQuantity(Commodities.VOLATILES))
				tooltip.addPara("警告:你的挥发物储量不足!", alarm, pad);
			else if (!isInScanLocation())
				tooltip.addPara("前往扫描位置以开始广域传感器扫描", Color.orange, pad);
			else if (!isUsable())
				tooltip.addPara("当前位置可进行广域传感器扫描", Color.yellow, pad);
			else  tooltip.addPara("当前可进行广域传感器扫描", Color.green, pad);
		}
		TSScan_CRLoss.CRLoss(true,tooltip);
		if (!getNonReadyShips().isEmpty()&&!Global.getSettings().isDevMode())
		{
			tooltip.addPara("严重警告:以下舰船战备过低,超载传感器可能造成人员及舰船损失:",alarm,pad);
			List<FleetMemberAPI> members=getNonReadyShips();
			for (FleetMemberAPI member:members)
			{
				tooltip.addPara("   %s %s  "+member.getShipName()+", "+member.getHullSpec().getHullNameWithDashClass(),pad,highlight,""+(int)(member.getRepairTracker().getCR()*100)+"%",""+(int)(TSScan_CRLoss.calculateCRLoss(member))+"%");
			}
		}
		tooltip.addPara("*2000 单位 = 1 地图网格边长", gray, pad);
		tooltip.addPara("**当舰队最大加速为舰队中最慢舰船速度的一半时,舰队被视作缓慢移动", gray, 0f);
		tooltip.addPara("***护卫舰下降 %s,驱逐舰下降 %s,巡洋舰下降 %s,主力舰下降 %s", 0f, gray,gray,""+(int)(TSScan_CRLoss.baseCRDamage*TSScan_CRLoss.lossMultOfSize(ShipAPI.HullSize.FRIGATE))+"%",""+(int)(TSScan_CRLoss.baseCRDamage*TSScan_CRLoss.lossMultOfSize(ShipAPI.HullSize.DESTROYER))+"%",""+(int)(TSScan_CRLoss.baseCRDamage*TSScan_CRLoss.lossMultOfSize(ShipAPI.HullSize.CRUISER))+"%",""+(int)(TSScan_CRLoss.baseCRDamage*TSScan_CRLoss.lossMultOfSize(ShipAPI.HullSize.CAPITAL_SHIP))+"%");

		addIncompatibleToTooltip(tooltip, expanded);

	}


	@Override
	public Color getCooldownColor() {
		if (showAlarm()) {
			Color color = Misc.getNegativeHighlightColor();
			return Misc.scaleAlpha(color, Global.getSector().getCampaignUI().getSharedFader().getBrightness() * 0.5f);
		}
		return super.getCooldownColor();
	}

	@Override
	public boolean isCooldownRenderingAdditive() {
		return showAlarm();
	}
	@Override
	public boolean isUsable() {
		return super.isUsable() &&
				getFleet() != null &&
				!getFleet().isInHyperspace()&&
				(
						isInScanLocation()&&
								getFleet().getSensorRangeMod().computeEffective(getFleet().getSensorStrength())>=SENSOR_STRENGTH_NEEDED&&
								computeVolatileCost() <= getFleet().getCargo().getCommodityQuantity(Commodities.VOLATILES)
				)||
				Global.getSettings().isDevMode();
	}
	protected boolean showAlarm() {
		return !getNonReadyShips().isEmpty() && !isOnCooldown() && !isActiveOrInProgress() && isUsable();
	}
	protected List<FleetMemberAPI> getNonReadyShips() {
		List<FleetMemberAPI> result = new ArrayList<>();
		if (Global.getSettings().isDevMode())return result;
		CampaignFleetAPI fleet=getFleet();
		if (fleet == null) return result;
		List<FleetMemberAPI> members=TSScan_CRLoss.getSensorMembers();
		for (FleetMemberAPI member : members)
			if (member.getRepairTracker().getCR()*100f < -TSScan_CRLoss.calculateCRLoss(member))
				result.add(member);
		return result;
	}
	protected float computeVolatileCost()
	{
		if (Global.getSettings().isDevMode()||getFleet().isInHyperspace())return 0;
		double cost=0;
		List<SectorEntityToken> entities=getFleet().getStarSystem().getAllEntities();
		for (SectorEntityToken entity:entities)
		{
			if (entity instanceof AsteroidBeltTerrainPlugin)cost+=1;
			else if (entity instanceof PlanetAPI)
			{
				if (entity.hasTag(Tags.STAR)||entity.hasTag(Tags.GAS_GIANT))
				{
					cost+=switch (((PlanetAPI) entity).getSpec().getPlanetType())
					{
						case StarTypes.BLACK_HOLE->VOLATILE_MULT_BLACK_HOLE;
						case StarTypes.NEUTRON_STAR->VOLATILE_MULT_NEUTRON_STAR;
						case StarTypes.BLUE_SUPERGIANT->VOLATILE_MULT_BLUE_SUPERGIANT;
						case StarTypes.RED_SUPERGIANT->VOLATILE_MULT_RED_SUPERGIANT;
						case StarTypes.ORANGE_GIANT->VOLATILE_MULT_ORANGE_GIANT;
						case StarTypes.RED_GIANT->VOLATILE_MULT_RED_GIANT;
						case StarTypes.BLUE_GIANT->VOLATILE_MULT_BLUE_GIANT;
						case StarTypes.YELLOW->VOLATILE_MULT_YELLOW;
						case StarTypes.ORANGE->VOLATILE_MULT_ORANGE;
						case StarTypes.WHITE_DWARF->VOLATILE_MULT_WHITE_DWARF;
						case StarTypes.RED_DWARF->VOLATILE_MULT_RED_DWARF;
						case StarTypes.BROWN_DWARF->VOLATILE_MULT_BROWN_DWARF;
						case StarTypes.GAS_GIANT->VOLATILE_MULT_GAS_GIANT;
						case StarTypes.ICE_GIANT->VOLATILE_MULT_ICE_GIANT;
						default->0;
					};
				}
				else if (entity.hasTag(Tags.PLANET))cost+=VOLATILE_MULT_PLANET;
			}
			else if (entity.hasTag(Tags.ACCRETION_DISK))cost+=VOLATILE_MULT_ACCRETION_DISK;
			else if (entity.hasTag(Tags.STABLE_LOCATION))cost+=VOLATILE_MULT_STABLE_LOCATION;
			else if (entity.hasTag(Tags.GATE))cost+=VOLATILE_MULT_GATE;
			else if (entity.hasTag(Tags.JUMP_POINT))cost+=VOLATILE_MULT_JUMP_POINT;
			else if (entity.hasTag(Tags.SALVAGEABLE))cost+=VOLATILE_MULT_SALVAGEABLE;
		}
		return (float)(cost*VOLATILE_MULT);
	}

	public boolean isInScanLocation()
	{
		if (getFleet().isInHyperspace())return false;
		if (TSScan_SystemScanPointsManager.IgnoredSystems.contains(getFleet().getContainingLocation().getId()))return true;

		List<SectorEntityToken> ScanPoints=TSScan_SystemScanPointsManager.ScanPointsOfSystems.get(getFleet().getContainingLocation().getId());

		if (ScanPoints==null||ScanPoints.isEmpty())
		{
			TSScan_SystemScanPointsManager.reload(getFleet().getStarSystem());
			return isInScanLocation();
		}

		for (SectorEntityToken ScanPoint:ScanPoints)
			if (((CampaignTerrain)ScanPoint).getPlugin().containsEntity(getFleet()))
				return true;
		return false;
	}
}





