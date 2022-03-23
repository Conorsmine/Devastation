package net.Conorsmine.com.GameManagers.RespawnManager;

import net.Conorsmine.com.WorldSetup.WorldManager;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.entity.EntityDeathEvent;

import java.util.ArrayList;

public class ResourceEntity extends ResourceManager {

    private final Location loc;
    private final EntityType entityType;
    private Entity entity;

    public ResourceEntity(Location loc, EntityType entity) {
        this.loc = loc;
        this.entityType = entity;
    }

    @Override
    void spawn() {
        loc.setWorld(WorldManager.getCurrentWorld());
        entity = WorldManager.getCurrentWorld().spawnEntity(loc, entityType);
    }

    public static void onDeath(EntityDeathEvent ev) {
        LivingEntity dead = ev.getEntity();
        for (ResourceManager respawnable : new ArrayList<>(ResourceManager.getRespawnList())) {
            if (!(respawnable instanceof ResourceEntity)) continue;
            ResourceEntity ent = (ResourceEntity) respawnable;
            if (dead == ent.entity) {
                ResourceManager.getRespawnList().remove(ent);

                ResourceEntity respawn = new ResourceEntity(ent.loc, ent.entityType);
                ResourceManager.addBroken(respawn);
            }
        }
    }
}
