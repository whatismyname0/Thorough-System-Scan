package com.fs.starfarer.api.impl.campaign.terrain;

import com.fs.starfarer.api.Global;
import com.fs.starfarer.api.campaign.OrbitAPI;
import com.fs.starfarer.api.campaign.PlanetAPI;
import com.fs.starfarer.api.campaign.SectorEntityToken;
import com.fs.starfarer.campaign.CampaignClock;
import com.fs.starfarer.campaign.CampaignTerrain;
import data.TSScan_Constants;
import org.lwjgl.util.vector.Vector2f;

import java.awt.*;

public class TSScan_LagrangePointAreaPlugin extends BaseRingTerrain
{

    public static int segmentCount = 48;

    public static class LagrangePointAreaParams extends RingParams {
        Vector2f location;
        boolean L4orL5;
        OrbitAPI orbit;

        public LagrangePointAreaParams(float maxRadius, PlanetAPI planet, String name, Vector2f location, boolean L4orL5) {
            super(maxRadius, maxRadius/2f, planet, name);
            this.location=location;
            this.L4orL5=L4orL5;
            this.orbit=planet.getOrbit().makeCopy();
            if (!L4orL5)
                this.orbit.advance(this.orbit.getOrbitalPeriod()/6f*CampaignClock.SECONDS_PER_GAME_DAY);
            else
                this.orbit.advance(-this.orbit.getOrbitalPeriod()/6f*CampaignClock.SECONDS_PER_GAME_DAY);
        }
    }

    public LagrangePointAreaParams params;

    @Override
    protected boolean shouldCheckFleetsToApplyEffect() {
        return false;
    }

    @Override
    public void init(String terrainId, SectorEntityToken entity, Object param) {
        super.init(terrainId, entity, param);
        params = (LagrangePointAreaParams) param;
        name = params.name;
        if (name == null) {
            name = "星系广域扫描区域";
        }
        entity.setLocation(params.location.x,params.location.y);
        entity.setOrbit(params.orbit.makeCopy());
        ((CampaignTerrain)entity).setRadius(params.bandWidthInEngine);
    }

    public void delete()
    {
        entity.getContainingLocation().removeEntity(entity);
        entity=null;
        params=null;
    }


    private transient RingRenderer rr;

    @Override
    public void renderOnMap(float factor, float alphaMult) {
        if (params == null) return;
        if (!TSScan_Constants.MAP_SHOULD_DISPLAY)
        {
            rr = null;
            return;
        }
        if (rr == null) {
            rr = new RingRenderer("systemMap", "map_lagrange_point_area");
        }
        Color color = Global.getSettings().getColor("lagrangePointAreaMapColor");
        rr.render(entity.getLocation(),
                0,
                params.bandWidthInEngine,
                color,
                false, factor, alphaMult/1.5f);
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

//    @Override
//    public void render(CampaignEngineLayers layer, ViewportAPI v) {
//        super.render(layer, v);
//
//        RoundAreaBorder(entity.getLocation(),params.bandWidthInEngine);
//        RoundAreaFilling(entity.getLocation(),params.bandWidthInEngine);
//    }

    @Override
    public String getNameForTooltip() {
        return "星系广域扫描区域";
        //return params.name;
    }

    @Override
    public float getProximitySoundFactor() {
        return 0;
    }
}
