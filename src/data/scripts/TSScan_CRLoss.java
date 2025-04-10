package data.scripts;

import com.fs.starfarer.api.Global;
import com.fs.starfarer.api.combat.ShipAPI;
import com.fs.starfarer.api.fleet.FleetMemberAPI;
import com.fs.starfarer.api.ui.TooltipMakerAPI;
import com.fs.starfarer.api.util.Misc;
import data.TSScan_Constants;

import java.awt.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class TSScan_CRLoss {
    public static final float baseCRDamage = 20f;
    public static float CRLOSS_MULT_DEFAULT = .625f;

    public static List<FleetMemberAPI> getSensorMembers()
    {
        List<FleetMemberAPI> ships=new ArrayList<>();
        int numProfileShips = Global.getSettings().getInt("maxSensorShips");
        List<FleetMemberAPI> members = Global.getSector().getPlayerFleet().getFleetData().getMembersListCopy();
        Collections.sort(members, new Comparator<FleetMemberAPI>() {
            @Override
            public int compare(FleetMemberAPI o1, FleetMemberAPI o2) {
                return o2.getStats().getSensorStrength().getModifiedInt()-o1.getStats().getSensorStrength().getModifiedInt();
            }
        });
        for (FleetMemberAPI member:members)
        {
            numProfileShips--;
            ships.add(member);
            if (numProfileShips<=0)break;
        }
        return ships;
    }
    public static void CRLoss(boolean isPrintingInfo, TooltipMakerAPI tooltip)
    {
        Color highlight = Misc.getHighlightColor();

        float pad = 10f;

        int numProfileShips = Global.getSettings().getInt("maxSensorShips");
        float suppliesToRepair=0;
        if (isPrintingInfo)
        {
            tooltip.addPara("超载舰队传感器阵列会导致舰队内传感器强度最大的 %s 艘舰船战备值下降***",pad,highlight,""+numProfileShips);
            tooltip.addPara("会受到影响的舰船如下:",pad);
        }
        List<FleetMemberAPI> members= getSensorMembers();
        for (FleetMemberAPI member:members)
        {
            float CRLoss=calculateCRLoss(member);
            suppliesToRepair+=calculateSuppliesToRepair(member);
            if (!isPrintingInfo)member.getRepairTracker().applyCREvent(CRLoss*.01f, "systemscalesensorburst", "广域传感器扫描");
            else tooltip.addPara("  从 %s 降为 %s  "+member.getShipName()+", "+member.getHullSpec().getHullNameWithDashClass(),pad,highlight,""+(int)(member.getRepairTracker().getCR()*100f)+"%",""+Math.max((int)(member.getRepairTracker().getCR()*100f+CRLoss),0)+"%");
        }
        if (isPrintingInfo)tooltip.addPara("共需要 %s 补给恢复所有损失的战备值",pad,highlight,""+(int)-suppliesToRepair);
    }

    public static float lossMultOfSize(ShipAPI.HullSize size)
    {
        float mult= TSScan_Constants.CRLOSS_MULT;
        switch (size) {
            case CAPITAL_SHIP:return mult * TSScan_Constants.CRLOSS_MULT_CAPITAL;
            case CRUISER:return mult * TSScan_Constants.CRLOSS_MULT_CRUISER;
            case DESTROYER:return mult * TSScan_Constants.CRLOSS_MULT_DESTROYER;
            case FRIGATE:return mult * TSScan_Constants.CRLOSS_MULT_FRIGATE;
            default:return mult * CRLOSS_MULT_DEFAULT;
        }
    }

    public static float lossMultOfShip(FleetMemberAPI member)
    {
        return lossMultOfSize(member.getHullSpec().getHullSize());
    }

    public static float calculateCRLoss(FleetMemberAPI member)
    {
        return -baseCRDamage*lossMultOfShip(member);
    }
    public static float calculateSuppliesToRepair(FleetMemberAPI member)
    {
        return calculateCRLoss(member)*member.getStats().getSuppliesToRecover().getModifiedValue()/member.getStats().getCRPerDeploymentPercent().computeEffective(member.getVariant().getHullSpec().getCRToDeploy());
    }
}
