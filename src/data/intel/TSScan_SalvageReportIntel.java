package data.intel;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import java.awt.Color;

import com.fs.starfarer.api.Global;
import com.fs.starfarer.api.campaign.CampaignClockAPI;
import com.fs.starfarer.api.campaign.StarSystemAPI;
import com.fs.starfarer.api.impl.campaign.ids.Commodities;
import com.fs.starfarer.api.impl.campaign.ids.Items;
import com.fs.starfarer.api.impl.campaign.ids.Tags;
import com.fs.starfarer.api.impl.campaign.intel.BaseIntelPlugin;
import com.fs.starfarer.api.ui.SectorMapAPI;
import com.fs.starfarer.api.ui.TooltipMakerAPI;
import com.fs.starfarer.api.util.Misc;
import data.scripts.TSSCan_SalvageableValue;

public class TSScan_SalvageReportIntel extends BaseIntelPlugin {

    public enum SalvageValue {
        NONE("无"),
        LOW("较低"),
        MEDIUM("中等"),
        HIGH("较高"),
        EXTREME("巨大极其非常无穷超级多");
        private final String valueString;

        SalvageValue(String value) {
            this.valueString = value;
        }
        public String getValueString() {
            return valueString;
        }
    }


    protected StarSystemAPI system;
    protected long removalCheckTimestamp = 0;
    protected float daysUntilRemoveCheck = 1f;

    public TSScan_SalvageReportIntel(StarSystemAPI system)
    {
        this.system=system;
    }

    @Override
    public boolean shouldRemoveIntel() {
        if (!system.isCurrentLocation()) {
            float daysSince = Global.getSector().getClock().getElapsedDaysSince(removalCheckTimestamp);
            if (daysSince > daysUntilRemoveCheck) {
                SalvageValue value = TSSCan_SalvageableValue.getSystemSalvageableValue();
                if (value == SalvageValue.NONE) {
                    return true;
                }
                removalCheckTimestamp = Global.getSector().getClock().getTimestamp();
                daysUntilRemoveCheck = 3f + (float) Math.random() * 3f;
            }
        }
        return super.shouldRemoveIntel();
    }


    protected void addBulletPoints(TooltipMakerAPI info, ListInfoMode mode, boolean isUpdate, Color tc, float initPad) {
        Color h = Misc.getHighlightColor();

        SalvageValue value = TSSCan_SalvageableValue.getSystemSalvageableValue();
        Map<String,Float> itemCount = TSSCan_SalvageableValue.getItemAmount();

        bullet(info);

        if (mode != ListInfoMode.IN_DESC) {
            Color highlight = h;
            if (value == SalvageValue.NONE) highlight = tc;
            info.addPara("预期稀有物品价值: %s", initPad, tc, highlight, value.getValueString());
            initPad = 0f;
        }
        if (value != SalvageValue.NONE)
        {
            info.addPara("期望获得如下数量的稀有物品(也就是说更多更少都说不定哦):", initPad, tc);
            initPad = 0f;
        }
        if (itemCount.get(Commodities.ALPHA_CORE) > 0) {
            info.addPara("%s 个 %s 级AI核心", initPad, tc, h, "" + itemCount.get(Commodities.ALPHA_CORE).intValue(), "Alpha");
            initPad = 0f;
        }
        if (itemCount.get(Commodities.BETA_CORE) > 0) {
            info.addPara("%s 个 %s 级AI核心", initPad, tc, h, "" + itemCount.get(Commodities.BETA_CORE).intValue(), "Beta");
            initPad = 0f;
        }
        if (itemCount.get(Commodities.GAMMA_CORE) > 0) {
            info.addPara("%s 个 %s 级AI核心", initPad, tc, h, "" + itemCount.get(Commodities.GAMMA_CORE).intValue(), "Gamma");
            initPad = 0f;
        }
        if (itemCount.get(Commodities.BLUEPRINTS) > 0) {
            info.addPara("%s 个舰船、战机联队或武器蓝图", initPad, tc, h, "" + itemCount.get(Commodities.BLUEPRINTS).intValue());
            initPad = 0f;
        }
        if (itemCount.get(Items.MODSPEC) > 0) {
            info.addPara("%s 个舰船插件蓝图", initPad, tc, h, "" + itemCount.get(Items.MODSPEC).intValue());
            initPad = 0f;
        }
        if (itemCount.get("special_items") > 0) {
            info.addPara("%s 个其他特殊物品", initPad, tc, h, "" + itemCount.get("special_items").intValue());
        }

        unindent(info);
    }

    @Override
    public void createSmallDescription(TooltipMakerAPI info, float width, float height) {
        Color h = Misc.getHighlightColor();
        Color tc = Misc.getTextColor();
        float opad = 10f;
        float imageWidth = width/2f;

        SalvageValue value = TSSCan_SalvageableValue.getSystemSalvageableValue();

        switch (value)
        {
            case NONE:
            {
                info.addImage(Global.getSettings().getSpriteName("intel", "TSSNoneValue"), imageWidth, opad);
                break;
            }
            case LOW:
            {
                info.addImage(Global.getSettings().getSpriteName("intel", "TSSLowValue"), imageWidth, opad);
                break;
            }
            case MEDIUM:
            {
                info.addImage(Global.getSettings().getSpriteName("intel", "TSSMediumValue"), imageWidth, opad);
                break;
            }
            case HIGH:
            {
                info.addImage(Global.getSettings().getSpriteName("intel", "TSSHighValue"), imageWidth, opad);
                break;
            }
            case EXTREME:
            {
                info.addImage(Global.getSettings().getSpriteName("intel", "TSSHighValue"), imageWidth, opad);
            }
        }

        if (value == SalvageValue.NONE) {
            info.addPara("在 " + system.getNameWithLowercaseTypeShort() + "星系中没有探测到稀有物品哦.", opad);
        }
        else {
            info.addPara("好欸！在 " +
            system.getNameWithLowercaseTypeShort() + "星系中探测到稀有物品. 预计总共有 %s 的价值.",
            opad, h, value.getValueString());
        }
        switch (value)
        {
            case NONE:
            {
                info.addPara("真是遗憾，下一个星系一定会有不错的收获吧.", opad);
                break;
            }
            case LOW:
            {
                info.addPara("姑且也算很不错呢，希望超空间今天也平静一点.", opad);
                break;
            }
            case MEDIUM:
            {
                info.addPara("好东西真不少，这趟没白来呢.", opad);
                break;
            }
            case HIGH:
            {
                info.addPara("我们来对地方了！呀！探测报告太多了！", opad);
                break;
            }
            case EXTREME:
            {
                info.addPara("这简直是...真不敢相信！", opad);
            }
        }

        addBulletPoints(info, ListInfoMode.IN_DESC);


        addLogTimestamp(info, tc, opad);

        addDeleteButton(info, width);
    }

    @Override
    public String getIcon() {
        SalvageValue value = TSSCan_SalvageableValue.getSystemSalvageableValue();
        if (value == SalvageValue.NONE) {
            return Global.getSettings().getSpriteName("intel", "TSSNoneValue");
        } else if (value == SalvageValue.LOW) {
            return Global.getSettings().getSpriteName("intel", "TSSLowValue");
        } else if (value == SalvageValue.MEDIUM) {
            return Global.getSettings().getSpriteName("intel", "TSSMediumValue");
        } else {
            return Global.getSettings().getSpriteName("intel", "TSSHighValue");
        }
    }

    @Override
    public Set<String> getIntelTags(SectorMapAPI map) {
        Set<String> tags = super.getIntelTags(map);
        //tags.add(Tags.INTEL_FLEET_LOG);
        SalvageValue value = TSSCan_SalvageableValue.getSystemSalvageableValue();
        if (value != SalvageValue.NONE) {
            tags.add(Tags.INTEL_EXPLORATION);
        }

        tags.add("Salvage");
        return tags;
    }


    public String getName() {
        return "广域传感器扫描结果 - " + system.getBaseName();
    }


    @Override
    public String getCommMessageSound() {
        return super.getCommMessageSound();
        //return "ui_discovered_entity";
    }

    public String getSortString() {
        return "zzz"+String.format("%1$20s", "" + getPlayerVisibleTimestamp()).replace(' ', '0');
    }


    public void addLogTimestamp(TooltipMakerAPI info, Color tc, float opad) {//From vanilla 0.98a. Used for 0.97a compatibility.

//		if (!getTagsForSort().contains(Tags.INTEL_FLEET_LOG) && !getTagsForSort().contains(Tags.INTEL_EXPLORATION)) {

//		{
//			float days = getDaysSincePlayerVisible();
//			if (days >= 1) {
//				addDays(info, "Log entry added ", "ago.", days, tc, opad);
//			} else {
//				info.addPara("Log entry added less than a day ago.", opad);
//			}
//		}

        Color h = Misc.getHighlightColor();

        long ts = Global.getSector().getClock().getTimestamp();
        if (timestamp != null) ts = timestamp;
        CampaignClockAPI clock = Global.getSector().getClock().createClock(ts);

//		String dateStr = "Logged on c." + clock.getCycle() + ", " + clock.getShortMonthString() + " " + clock.getDay();
//		//tc = Misc.getGrayColor();
//		info.addPara(dateStr, opad, tc, h, "c." + clock.getCycle(), "" + clock.getDay());

        long msPerMin = 60L * 1000L;
        long msPerHour = msPerMin * 60L;
        long msPerDay = msPerHour * 24L;
        //long msPerWeek = msPerDay * 7L;
        long msPerMonth = msPerDay * 30L;
        long msPerCycle = msPerDay * 365L;

        long diff = Global.getSector().getClock().getTimestamp() - ts;


        String agoStr = "";
        List<String> highlights = new ArrayList<>();
//		highlights.add("c." + clock.getCycle());
//		highlights.add("" + clock.getDay());
        if (diff < msPerHour && false) {
            long minutes = diff / msPerMin;
            agoStr = "" + minutes + " " + (minutes == 1 ? "minute" : "minutes");
        } else if (diff < msPerDay && false) {
            long hours = diff / msPerHour;
            agoStr = "" + hours + " " + (hours == 1 ? "hour" : "hours");
            long rem = diff - hours * msPerHour;
            long minutes = rem / msPerMin;
            agoStr += " " + minutes + " " + (minutes == 1 ? "minute" : "minutes");
        } else if (diff < msPerMonth) {
            long days = diff / msPerDay;
            agoStr = "" + days + " " + (days == 1 ? "day" : "days");
            highlights.add("" + days);
//			long rem = diff - days * msPerDay;
//			long hours = rem / msPerHour;
//			agoStr += " " + hours + " " + (hours == 1 ? "hour" : "hours");
        } else if (diff < msPerCycle) {
            long months = diff / msPerMonth;
            agoStr = "" + months + " " + (months == 1 ? "month" : "months");
            long rem = diff - months * msPerMonth;
            long days = rem / msPerDay;
            agoStr += " and " + days + " " + (days == 1 ? "day" : "days");
            highlights.add("" + months);
            highlights.add("" + days);
        } else {
            long cycles = diff / msPerCycle;
            agoStr = "" + cycles + " " + (cycles == 1 ? "cycle" : "cycles");
            long rem = diff - cycles * msPerCycle;
            long months = rem / msPerMonth;
            agoStr += " and " + months + " " + (months == 1 ? "month" : "months");
            highlights.add("" + cycles);
            highlights.add("" + months);
        }


        //String dateStr = "Logged on c." + clock.getCycle() + ", " + clock.getShortMonthString() + " " + clock.getDay();
        //tc = Misc.getGrayColor();
        //info.addPara(dateStr + ", " + agoStr + " ago.", opad, tc, h, highlights.toArray(new String[0]));
        long days = diff / msPerDay;
        if (days >= 1) {
            info.addPara("Log entry added " + agoStr + " ago.", opad, tc, h, highlights.toArray(new String[0]));
        } else {
            info.addPara("Log entry added less than a day ago.", opad);
        }

        //ago = Label.create(agoStr, Misc.getGrayColor());
    }
}