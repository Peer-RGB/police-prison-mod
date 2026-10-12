package de.peerrgb.policeprison.config;

import net.minecraftforge.common.ForgeConfigSpec;

public final class ModConfig {
    public static final ForgeConfigSpec SPEC;
    public static final ForgeConfigSpec.BooleanValue LOCK_RESTRAINED_MOVEMENT;
    public static final ForgeConfigSpec.DoubleValue RESTRAIN_RADIUS;

    static {
        ForgeConfigSpec.Builder builder = new ForgeConfigSpec.Builder();
        builder.push("restraints");
        LOCK_RESTRAINED_MOVEMENT = builder
                .comment("If true, restrained players are held near the restraint anchor position.")
                .define("lockRestrainedMovement", true);
        RESTRAIN_RADIUS = builder
                .comment("Allowed movement radius (blocks) around restraint anchor.")
                .defineInRange("restrainRadius", 1.5D, 0.0D, 16.0D);
        builder.pop();
        SPEC = builder.build();
    }

    private ModConfig() {
    }
}
