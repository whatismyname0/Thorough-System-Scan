package data.scripts;


import com.fs.starfarer.api.Global;
import com.fs.starfarer.api.campaign.CampaignFleetAPI;
import com.fs.starfarer.api.campaign.CargoAPI;
import com.fs.starfarer.api.campaign.CargoStackAPI;
import com.fs.starfarer.api.campaign.SectorEntityToken;
import com.fs.starfarer.api.campaign.rules.MemoryAPI;
import com.fs.starfarer.api.combat.MutableStat;
import com.fs.starfarer.api.impl.campaign.ids.*;
import com.fs.starfarer.api.impl.campaign.procgen.SalvageEntityGenDataSpec;
import com.fs.starfarer.api.impl.campaign.rulecmd.salvage.SalvageEntity;
import com.fs.starfarer.api.util.Misc;
import org.apache.log4j.Logger;
import data.intel.TSScan_SalvageReportIntel;

import java.util.*;


public class TSSCan_SalvageableValue extends SalvageEntity
{
        private static final Logger log = Global.getLogger(TSSCan_SalvageableValue.class);

    final static Map<String,Float> itemAmount = new HashMap<>();
    static float totalSalvageableValue = 0f;
    static final int repeatTime = 4;

    static
    {
        itemAmount.put(Commodities.ALPHA_CORE,0f);
        itemAmount.put(Commodities.BETA_CORE,0f);
        itemAmount.put(Commodities.GAMMA_CORE,0f);
        itemAmount.put(Commodities.BLUEPRINTS,0f);
        itemAmount.put(Items.TAG_MODSPEC,0f);
        itemAmount.put(Items.TAG_COLONY_ITEM,0f);
        itemAmount.put("special_items",0f);
    }

    public static Map<String,Float> getItemAmount()
    {
        return itemAmount;
    }

    public static TSScan_SalvageReportIntel.SalvageValue getSystemSalvageableValue()
    {
        if (totalSalvageableValue <= 0f) {
            return TSScan_SalvageReportIntel.SalvageValue.NONE;
        } else if (totalSalvageableValue < 15) {
            return TSScan_SalvageReportIntel.SalvageValue.LOW;
        } else if (totalSalvageableValue < 40) {
            return TSScan_SalvageReportIntel.SalvageValue.MEDIUM;
        } else if (totalSalvageableValue < 100) {
            return TSScan_SalvageReportIntel.SalvageValue.HIGH;
        } else {
            return TSScan_SalvageReportIntel.SalvageValue.EXTREME;
        }
    }
    public static void reset()
    {
        totalSalvageableValue=0;
        itemAmount.put(Commodities.ALPHA_CORE,0f);
        itemAmount.put(Commodities.BETA_CORE,0f);
        itemAmount.put(Commodities.GAMMA_CORE,0f);
        itemAmount.put(Commodities.BLUEPRINTS,0f);
        itemAmount.put(Items.TAG_MODSPEC,0f);
        itemAmount.put(Items.TAG_COLONY_ITEM,0f);
        itemAmount.put("special_items",0f);
    }

    protected static void estimateEntitySalvageValue(SectorEntityToken entity)
    {
        if (!entity.hasTag(Tags.SALVAGEABLE)&&!entity.hasTag(Tags.PLANET))return;
        if (entity.hasTag(Tags.NOT_RANDOM_MISSION_TARGET))return;
        if (entity.hasTag(Tags.FADING_OUT_AND_EXPIRING))return;
        if (entity.hasTag(Tags.EXPIRES))return;


        int salvageableValue=0;

        Map<String,Integer> tempItemAccount = new HashMap<>();
        tempItemAccount.put(Commodities.ALPHA_CORE,0);
        tempItemAccount.put(Commodities.BETA_CORE,0);
        tempItemAccount.put(Commodities.GAMMA_CORE,0);
        tempItemAccount.put(Commodities.BLUEPRINTS,0);
        tempItemAccount.put(Items.TAG_MODSPEC,0);
        tempItemAccount.put(Items.TAG_COLONY_ITEM,0);
        tempItemAccount.put("special_items",0);
        
        for (int i=0;i<repeatTime;i++)
        {
            List<CargoStackAPI> cargos = genCargoFromDrop(entity,i).getStacksCopy();

            int index=0;
            for (CargoStackAPI cargo:cargos) {
                String commodityId = cargo.getCommodityId();

                if (Global.getSettings().isDevMode()) {
                    index++;
                    log.info(index);
                    if (cargo.getDisplayName() != null) log.info(cargo.getDisplayName());
                    else log.info("null display name");
                    if (commodityId != null) log.info(commodityId);
                    else log.info("null id");
                    if (cargo.isSpecialStack()) {
                        log.info(cargo.getSpecialDataIfSpecial().getId());
                        log.info(cargo.getSpecialDataIfSpecial().getData());
                    }
                }

                if (cargo.isSpecialStack()) {
                    if (cargo.getSpecialDataIfSpecial().getId().equals("modspec")) {
                        salvageableValue += 2;
                        tempItemAccount.put(Items.TAG_MODSPEC, tempItemAccount.get(Items.TAG_MODSPEC) + 1);
                    }
                    else if (cargo.getSpecialItemSpecIfSpecial().hasTag(Items.TAG_BLUEPRINT_PACKAGE)) {
                        salvageableValue += 10;
                        tempItemAccount.put(Commodities.BLUEPRINTS, tempItemAccount.get(Commodities.BLUEPRINTS) + 1);
                    }
                    else if (cargo.getSpecialItemSpecIfSpecial().hasTag(Items.TAG_SINGLE_BP)) {
                        salvageableValue += 3;
                        tempItemAccount.put(Commodities.BLUEPRINTS, tempItemAccount.get(Commodities.BLUEPRINTS) + 1);
                    }
                    else if (cargo.getSpecialItemSpecIfSpecial().hasTag(Items.TAG_COLONY_ITEM)) {
                        salvageableValue += 20;
                        tempItemAccount.put(Items.TAG_COLONY_ITEM, tempItemAccount.get(Items.TAG_COLONY_ITEM) + 1);
                    }
                    else
                    {
                        salvageableValue += 3;
                        tempItemAccount.put("special_items", tempItemAccount.get("special_items") + 1);
                    }
                }
                if (commodityId == null) continue;

                switch (commodityId)
                {
                    case Commodities.ALPHA_CORE:
                    {
                        salvageableValue += 40;
                        tempItemAccount.put(commodityId, tempItemAccount.get(commodityId) + 1);
                        break;
                    }
                    case Commodities.BETA_CORE:
                    {
                        salvageableValue += 10;
                        tempItemAccount.put(commodityId, tempItemAccount.get(commodityId) + 1);
                        break;
                    }
                    case Commodities.GAMMA_CORE:
                    {
                        salvageableValue += 5;
                        tempItemAccount.put(commodityId, tempItemAccount.get(commodityId) + 1);
                    }
                }
            }
        }
        
        totalSalvageableValue+=(float)salvageableValue/(float)repeatTime;

        itemAmount.put(Commodities.ALPHA_CORE,itemAmount.get(Commodities.ALPHA_CORE)+(float)tempItemAccount.get(Commodities.ALPHA_CORE)/(float)repeatTime);
        itemAmount.put(Commodities.BETA_CORE,itemAmount.get(Commodities.BETA_CORE)+(float)tempItemAccount.get(Commodities.BETA_CORE)/(float)repeatTime);
        itemAmount.put(Commodities.GAMMA_CORE,itemAmount.get(Commodities.GAMMA_CORE)+(float)tempItemAccount.get(Commodities.GAMMA_CORE)/(float)repeatTime);
        itemAmount.put(Commodities.BLUEPRINTS,itemAmount.get(Commodities.BLUEPRINTS)+(float)tempItemAccount.get(Commodities.BLUEPRINTS)/(float)repeatTime);
        itemAmount.put(Items.TAG_MODSPEC,itemAmount.get(Items.TAG_MODSPEC)+(float)tempItemAccount.get(Items.TAG_MODSPEC)/(float)repeatTime);
        itemAmount.put(Items.TAG_COLONY_ITEM,itemAmount.get(Items.TAG_COLONY_ITEM)+(float)tempItemAccount.get(Items.TAG_COLONY_ITEM)/(float)repeatTime);
        itemAmount.put("special_items",itemAmount.get("special_items")+(float)tempItemAccount.get("special_items")/(float)repeatTime);
    }

    public static CargoAPI genCargoFromDrop(SectorEntityToken entity,int randomParameter) {
        MemoryAPI memory = entity.getMemoryWithoutUpdate();
        long seed = memory.getLong(MemFlags.SALVAGE_SEED);
        Random random = Misc.getRandom(seed, randomParameter);

        List<SalvageEntityGenDataSpec.DropData> dropValue = new ArrayList<>(entity.getDropValue());
        List<SalvageEntityGenDataSpec.DropData> dropRandom = new ArrayList<>(entity.getDropRandom());
        SalvageEntityGenDataSpec spec = (SalvageEntityGenDataSpec) Global.getSettings().getSpec(
                SalvageEntityGenDataSpec.class, entity.getCustomEntityType(), true);

        if (spec != null) {
            dropValue.addAll(spec.getDropValue());
            dropRandom.addAll(spec.getDropRandom());
        }

        CampaignFleetAPI fleet = Global.getSector().getPlayerFleet();
        TSSCan_SalvageableValue temp = new TSSCan_SalvageableValue();
        temp.cargo=fleet.getCargo();
        temp.entity=entity;

        return SalvageEntity.generateSalvage(random, temp.getValueRecoveryStat(true).getModifiedValue(), fleet.getStats().getDynamic().getValue(Stats.SALVAGE_VALUE_MULT_FLEET_INCLUDES_RARE), Global.getSettings().getFloat("easySalvageMult"), 1f, dropValue, dropRandom);
    }

    protected MutableStat getValueRecoveryStat(boolean withSkillMultForRares) {
        MutableStat valueRecovery = new MutableStat(1f);
        int i = 0;

        float machineryContrib = 0.75f;
        valueRecovery.modifyPercent("base", -100f);
        if (machineryContrib < 1f) {
            valueRecovery.modifyPercent("base_positive", (int) Math.round(100f - 100f * machineryContrib), "Base effectiveness");
        }
        //valueRecovery.modifyPercent("base", -75f);

        CampaignFleetAPI fleet = Global.getSector().getPlayerFleet();
        boolean modified = false;
        if (withSkillMultForRares) {
            for (MutableStat.StatMod mod : fleet.getStats().getDynamic().getStat(Stats.SALVAGE_VALUE_MULT_FLEET_INCLUDES_RARE).getFlatMods().values()) {
                modified = true;
                valueRecovery.modifyPercentAlways("" + i++, (int) Math.round(mod.value * 100f), mod.desc);
            }
        }

        {
            for (MutableStat.StatMod mod : fleet.getStats().getDynamic().getStat(Stats.SALVAGE_VALUE_MULT_FLEET_NOT_RARE).getFlatMods().values()) {
                modified = true;
                valueRecovery.modifyPercentAlways("" + i++, (int) Math.round(mod.value * 100f), mod.desc);
            }
        }
        if (!modified) {
            valueRecovery.modifyPercentAlways("" + i++, (int) Math.round(0f), "Salvaging skill");
        }

        float fleetSalvageShips = getPlayerShipsSalvageModUncapped();
        valueRecovery.modifyPercentAlways("" + i++, (int) Math.round(fleetSalvageShips * 100f), "Fleetwide salvaging capability");

        return valueRecovery;
    }
}
