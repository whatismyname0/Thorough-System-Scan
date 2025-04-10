package com.fs.starfarer.api.impl.campaign.terrain;

import com.fs.starfarer.api.Global;
import com.fs.starfarer.api.campaign.PlanetAPI;
import com.fs.starfarer.api.campaign.SectorEntityToken;
import org.lwjgl.util.vector.Vector2f;

import java.awt.*;

import static data.scripts.TSScan_SystemScanPointsManager.getScanLocation;

public class TSScan_LagrangePointAreaPlugin extends BaseRingTerrain
{

    //    public static int segmentCount = 48;

    public static class LagrangePointAreaParams extends RingParams {
        Vector2f location;
        boolean L4orL5;
        public LagrangePointAreaParams(float maxRadius, PlanetAPI planet, String name, Vector2f location, boolean L4orL5) {
            super(maxRadius, maxRadius/2f, planet, name);
            this.location=location;
            this.L4orL5=L4orL5;
        }
    }

    public LagrangePointAreaParams params;
    public boolean updated=false;

    @Override
    public void advance(float amount)
    {
        if (Global.getSector().getClock().getDay()%5==0)
        {
            if (!updated)
            {
                updated=true;
                params.location=getScanLocation(entity.getStarSystem(), params.L4orL5);
                entity.setLocation(params.location.x,params.location.y);
            }
        }
        else updated=false;
        super.advance(amount);
    }

    @Override
    protected boolean shouldCheckFleetsToApplyEffect() {
        return false;
    }

    public void init(String terrainId, SectorEntityToken entity, Object param) {
        super.init(terrainId, entity, param);
        params = (LagrangePointAreaParams) param;
        name = params.name;
        if (name == null) {
            name = "星系广域扫描区域";
        }
        entity.setLocation(params.location.x,params.location.y);
    }

    private transient RingRenderer rr;

    public void renderOnMap(float factor, float alphaMult) {
        if (params == null) return;
        if (rr == null) {
            rr = new RingRenderer("systemMap", "map_lagrange_point_area");
        }
        Color color = Global.getSettings().getColor("lagrangePointAreaMapColor");
        rr.render(entity.getLocation(),
                0,
                params.middleRadius*2f,
                color,
                false, factor, alphaMult/2f);
    }

//    public void RoundAreaBorder(Vector2f center, float radius)
//    {
//        float angle = (float)Math.PI*2f/(float)segmentCount;
//
//        GL11.glBegin(GL11.GL_LINES);
//        Misc.setColor(Color.lightGray,.3f);
//
//        for (int i=0;i<segmentCount;i++)
//        {
//            GL11.glVertex2f(center.x + radius * (float) Math.cos(angle * (float) i), center.y + radius * (float) Math.sin(angle * (float) i));
//            GL11.glVertex2f(center.x + radius * (float) Math.cos(angle * (float) (i+1)), center.y + radius * (float) Math.sin(angle * (float) (i+1)));
//        }
//
//        GL11.glEnd();
//    }
//
//    public void RoundAreaFilling(Vector2f center, float radius)
//    {
//        float angle = (float)Math.PI*2f/(float)segmentCount;
//
//        GL11.glBegin(GL11.GL_TRIANGLES);
//        Misc.setColor(new Color(16,16,16),.05f);
//
//        for (int i=0;i<segmentCount;i++)
//        {
//            GL11.glVertex2f(center.x+radius*(float)Math.cos(angle*(float)i),center.y+radius*(float)Math.sin(angle*(float)i));
//            GL11.glVertex2f(center.x,center.y);
//            GL11.glVertex2f(center.x+radius*(float)Math.cos(angle*(float)(i+1)),center.y+radius*(float)Math.sin(angle*(float)(i+1)));
//        }
//        GL11.glEnd();
//    }

//    public void render(CampaignEngineLayers layer, ViewportAPI viewport) {
//        RoundAreaBorder(entity.getLocation(),params.bandWidthInEngine);
//        RoundAreaFilling(entity.getLocation(),params.bandWidthInEngine);
//
//        super.render(layer, viewport);
//    }

    public String getNameForTooltip() {
        return "星系广域扫描区域";
        //return params.name;
    }

    public float getProximitySoundFactor() {
        return 0;
    }
}
