package de.peerrgb.policeprison.data;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.saveddata.SavedData;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class PoliceSavedData extends SavedData {
    private static final String DATA_NAME = "policeprison_state";
    private final Map<UUID, PlayerPoliceState> playerStates = new HashMap<>();
    private ResourceLocation jailDimension;
    private BlockPos jailPos;

    public static PoliceSavedData get(ServerLevel level) {
        return level.getServer().overworld().getDataStorage().computeIfAbsent(PoliceSavedData::load, PoliceSavedData::new, DATA_NAME);
    }

    public PlayerPoliceState getOrCreate(UUID playerId) {
        return playerStates.computeIfAbsent(playerId, ignored -> new PlayerPoliceState());
    }

    public PlayerPoliceState get(UUID playerId) {
        return playerStates.get(playerId);
    }

    public void remove(UUID playerId) {
        playerStates.remove(playerId);
        setDirty();
    }

    public Map<UUID, PlayerPoliceState> getAll() {
        return playerStates;
    }

    public ResourceLocation getJailDimension() {
        return jailDimension;
    }

    public BlockPos getJailPos() {
        return jailPos;
    }

    public void setJailPosition(ResourceLocation dimension, BlockPos pos) {
        this.jailDimension = dimension;
        this.jailPos = pos;
        setDirty();
    }

    @Override
    public CompoundTag save(CompoundTag tag) {
        ListTag list = new ListTag();
        for (Map.Entry<UUID, PlayerPoliceState> entry : playerStates.entrySet()) {
            CompoundTag entryTag = new CompoundTag();
            entryTag.putUUID("player", entry.getKey());
            entryTag.put("state", entry.getValue().save());
            list.add(entryTag);
        }
        tag.put("states", list);

        if (jailDimension != null) {
            tag.putString("jailDimension", jailDimension.toString());
        }
        if (jailPos != null) {
            tag.putLong("jailPos", jailPos.asLong());
        }

        return tag;
    }

    public static PoliceSavedData load(CompoundTag tag) {
        PoliceSavedData data = new PoliceSavedData();
        ListTag list = tag.getList("states", Tag.TAG_COMPOUND);
        for (Tag raw : list) {
            CompoundTag entryTag = (CompoundTag) raw;
            UUID player = entryTag.getUUID("player");
            PlayerPoliceState state = PlayerPoliceState.load(entryTag.getCompound("state"));
            data.playerStates.put(player, state);
        }

        if (tag.contains("jailDimension")) {
            data.jailDimension = new ResourceLocation(tag.getString("jailDimension"));
        }
        if (tag.contains("jailPos")) {
            data.jailPos = BlockPos.of(tag.getLong("jailPos"));
        }
        return data;
    }
}
