package de.peerrgb.policeprison.client;

public final class ClientState {
    private static boolean restrained;
    private static boolean jailed;

    private ClientState() {
    }

    public static void set(boolean newRestrained, boolean newJailed) {
        restrained = newRestrained;
        jailed = newJailed;
    }

    public static boolean isRestrained() {
        return restrained;
    }

    public static boolean isJailed() {
        return jailed;
    }
}
