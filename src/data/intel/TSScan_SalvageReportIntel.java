package data.intel;

import java.util.Map;
import java.util.Set;

import java.awt.Color;

import com.fs.starfarer.api.Global;
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

    protected boolean interruptedScan;
    protected StarSystemAPI system;
    protected long removalCheckTimestamp = 0;
    protected float daysUntilRemoveCheck = 1f;

    public TSScan_SalvageReportIntel(StarSystemAPI system, boolean interruptedScan)
    {
        this.system=system;
        this.interruptedScan=interruptedScan;
    }

    @Override
    public boolean shouldRemoveIntel() {
        if (system==null)return true;

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
        if (system==null)return;

        Color h = Misc.getHighlightColor();

        SalvageValue value = TSSCan_SalvageableValue.getSystemSalvageableValue();
        Map<String,Float> itemCount = TSSCan_SalvageableValue.getItemAmount();

        bullet(info);

        if (interruptedScan)
        {
            info.addPara("扫描强度还没达到峰值就被打断了...没有得到什么有价值的信息", initPad, tc);
            return;
        }

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
            info.addPara("%s 个 %s 级AI核心", initPad, tc, h, String.format("%.1f",itemCount.get(Commodities.ALPHA_CORE)), "Alpha");
            initPad = 0f;
        }
        if (itemCount.get(Commodities.BETA_CORE) > 0) {
            info.addPara("%s 个 %s 级AI核心", initPad, tc, h, String.format("%.1f",itemCount.get(Commodities.BETA_CORE)), "Beta");
            initPad = 0f;
        }
        if (itemCount.get(Commodities.GAMMA_CORE) > 0) {
            info.addPara("%s 个 %s 级AI核心", initPad, tc, h, String.format("%.1f",itemCount.get(Commodities.GAMMA_CORE)), "Gamma");
            initPad = 0f;
        }
        if (itemCount.get(Commodities.BLUEPRINTS) > 0) {
            info.addPara("%s 个舰船、战机联队或武器蓝图", initPad, tc, h, String.format("%.1f",itemCount.get(Commodities.BLUEPRINTS)));
            initPad = 0f;
        }
        if (itemCount.get(Items.TAG_MODSPEC) > 0) {
            info.addPara("%s 个舰船插件蓝图", initPad, tc, h, String.format("%.1f",itemCount.get(Items.TAG_MODSPEC)));
            initPad = 0f;
        }
        if (itemCount.get(Items.TAG_COLONY_ITEM) > 0) {
            info.addPara("%s 个殖民地工业用特殊物品", initPad, tc, h, String.format("%.1f",itemCount.get(Items.TAG_COLONY_ITEM)));
            initPad = 0f;
        }
        if (itemCount.get("special_items") > 0) {
            info.addPara("%s 个其他特殊物品", initPad, tc, h, String.format("%.1f",itemCount.get("special_items")));
        }

        unindent(info);
    }

    @Override
    public void createSmallDescription(TooltipMakerAPI info, float width, float height) {
        if (system==null)return;

        Color h = Misc.getHighlightColor();
        Color tc = Misc.getTextColor();
        float opad = 10f;
        float imageWidth = width/2f;

        SalvageValue value = TSSCan_SalvageableValue.getSystemSalvageableValue();

        if (interruptedScan)
            info.addImage(Global.getSettings().getSpriteName("intel", "TSSInterrupted"), imageWidth, opad);
        else
        {
            switch (value) {
                case NONE: {
                    info.addImage(Global.getSettings().getSpriteName("intel", "TSSNoneValue"), imageWidth, opad);
                    break;
                }
                case LOW: {
                    info.addImage(Global.getSettings().getSpriteName("intel", "TSSLowValue"), imageWidth, opad);
                    break;
                }
                case MEDIUM: {
                    info.addImage(Global.getSettings().getSpriteName("intel", "TSSMediumValue"), imageWidth, opad);
                    break;
                }
                case HIGH: {
                    info.addImage(Global.getSettings().getSpriteName("intel", "TSSHighValue"), imageWidth, opad);
                    break;
                }
                case EXTREME: {
                    info.addImage(Global.getSettings().getSpriteName("intel", "TSSHighValue"), imageWidth, opad);
                }
            }
            if (value == SalvageValue.NONE) {
                info.addPara("在 " + system.getNameWithLowercaseTypeShort() + "星系中没有探测到稀有物品哦.", opad);
            }
            else {
                info.addPara("好诶！在 " +
                system.getNameWithLowercaseTypeShort() + "星系中探测到稀有物品. 预计总共有 %s 的价值.",
                opad, h, value.getValueString());
            }
            switch (value) {
                case NONE: {
                    info.addPara("真是遗憾，下一个星系一定会有不错的收获吧.", opad);
                    break;
                }
                case LOW: {
                    info.addPara("姑且也算很不错呢，希望超空间今天也平静一点.", opad);
                    break;
                }
                case MEDIUM: {
                    info.addPara("好东西真不少，这趟没白来呢.", opad);
                    break;
                }
                case HIGH: {
                    info.addPara("我们来对地方了！呀！探测报告太多了！", opad);
                    break;
                }
                case EXTREME: {
                    info.addPara("这简直是...真不敢相信！", opad);
                }
            }
        }

        addBulletPoints(info, ListInfoMode.IN_DESC);


        addLogTimestamp(info, tc, opad);

        addDeleteButton(info, width);
    }

    @Override
    public String getIcon() {
        SalvageValue value = TSSCan_SalvageableValue.getSystemSalvageableValue();
        if (interruptedScan)return Global.getSettings().getSpriteName("intel", "TSSInterrupted");
        if (value == SalvageValue.NONE)return Global.getSettings().getSpriteName("intel", "TSSNoneValue");
        if (value == SalvageValue.LOW)return Global.getSettings().getSpriteName("intel", "TSSLowValue");
        if (value == SalvageValue.MEDIUM)return Global.getSettings().getSpriteName("intel", "TSSMediumValue");
        return Global.getSettings().getSpriteName("intel", "TSSHighValue");
    }

    @Override
    public Set<String> getIntelTags(SectorMapAPI map) {
        Set<String> tags = super.getIntelTags(map);
        //tags.add(Tags.INTEL_FLEET_LOG);
        SalvageValue value = TSSCan_SalvageableValue.getSystemSalvageableValue();
        if (value != SalvageValue.NONE) {
            tags.add(Tags.INTEL_EXPLORATION);
        }

        tags.add(Tags.INTEL_SALVAGE);
        return tags;
    }


    public String getName() {
        if (system==null)return "广域传感器扫描结果 - 未知";
        return "广域传感器扫描结果 - " + system.getBaseName();
    }


    @Override
    public String getCommMessageSound() {
        return super.getCommMessageSound();
        //return "ui_discovered_entity";
    }

    public String getSortString() {
        return getSortStringNewestFirst();
    }
}