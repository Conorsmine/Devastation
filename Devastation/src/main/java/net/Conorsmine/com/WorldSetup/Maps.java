package net.Conorsmine.com.WorldSetup;

public enum Maps {

    DEVASTATION("Cinder");
//    BLOOM("Bloom");

    private final String mapFolder;
    Maps(String mapFolder) {
        this.mapFolder = mapFolder;
    }

    public String getMapFolder() {
        return mapFolder;
    }
}
