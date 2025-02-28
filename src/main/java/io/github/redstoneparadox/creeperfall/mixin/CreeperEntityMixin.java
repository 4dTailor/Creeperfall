package io.github.redstoneparadox.creeperfall.mixin;

import io.github.redstoneparadox.creeperfall.models.CreeperModel;
import net.minecraft.entity.LivingEntity;
import net.minecraft.server.world.ServerWorld;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.mob.CreeperEntity;
import net.minecraft.world.World;

@Mixin(CreeperEntity.class)
public abstract class CreeperEntityMixin extends LivingEntity implements CreeperModel {
    @Shadow
    @Final
    private static TrackedData<Boolean> CHARGED;

    protected CreeperEntityMixin(EntityType<LivingEntity> type, World world) {
        super(type, world);
    }

    @Override
    public boolean damage(ServerWorld world, DamageSource source, float amount) {
        if (this.isCharged() && !(source.getSource() instanceof CreeperEntity)) {
            discharge();
            return super.damage(world, source, 0);
        }
        if (source.getSource() instanceof CreeperEntity) {
            return false;
        }
        return super.damage(world, source, amount);
    }

    @Override
    public boolean isCharged() {
        return this.dataTracker.get(CHARGED);
    }

    @Override
    public void charge() {
        this.dataTracker.set(CHARGED, true);
    }

    @Override
    public void discharge() {
        this.dataTracker.set(CHARGED, false);
    }
}