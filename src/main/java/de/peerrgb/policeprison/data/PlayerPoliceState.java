package de.peerrgb.policeprison.data;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;

public class PlayerPoliceState {
    private boolean restrained;
    private boolean jailed;
    private ResourceLocation anchorDimension;
    private BlockPos anchorPos;

    public boolean isRestrained() {
        return restrained;
    }

    public void setRestrained(boolean restrained) {
        this.restrained = restrained;
    }

    public boolean isJailed() {
        return jailed;
    }

    public void setJailed(boolean jailed) {
        this.jailed = jailed;
    }

    public ResourceLocation getAnchorDimension() {
        return anchorDimension;
    }

    public void setAnchorDimension(ResourceLocation anchorDimension) {
        this.anchorDimension = anchorDimension;
    }

    public BlockPos getAnchorPos() {
        return anchorPos;
    }

    public void setAnchorPos(BlockPos anchorPos) {
        this.anchorPos = anchorPos;
    }

    public CompoundTag save() {
        CompoundTag tag = new CompoundTag();
        tag.putBoolean("restrained", restrained);
        tag.putBoolean("jailed", jailed);
        if (anchorDimension != null) {
            tag.putString("anchorDimension", anchorDimension.toString());
        }
        if (anchorPos != null) {
            tag.putLong("anchorPos", anchorPos.asLong());
        }
        return tag;
    }

    public static PlayerPoliceState load(CompoundTag tag) {
        PlayerPoliceState state = new PlayerPoliceState();
        state.restrained = tag.getBoolean("restrained");
        state.jailed = tag.getBoolean("jailed");
        if (tag.contains("anchorDimension")) {
            state.anchorDimension = new ResourceLocation(tag.getString("anchorDimension"));
        }
        if (tag.contains("anchorPos")) {
            state.anchorPos = BlockPos.of(tag.getLong("anchorPos"));
        }
        return state;
    }
}
