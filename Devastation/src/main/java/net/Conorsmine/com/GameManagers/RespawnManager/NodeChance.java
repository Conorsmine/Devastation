package net.Conorsmine.com.GameManagers.RespawnManager;

import org.bukkit.Material;

public class NodeChance {

    private final int chance, lowVal, highVal;
    private final Material mat;

    public NodeChance(int chance, int lowVal, Material mat) {
        this.chance = chance;
        this.lowVal = lowVal;
        this.highVal = lowVal + chance;
        this.mat = mat;
    }

    public int getChance() {
        return chance;
    }

    public int getLowVal() {
        return lowVal;
    }

    public int getHighVal() {
        return highVal;
    }

    public Material getMat() {
        return mat;
    }
}
