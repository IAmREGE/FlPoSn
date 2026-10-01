package rege.chemicalcompound.mod115.flposn.fluid;

import java.util.Optional;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.FluidBlock;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.CollisionEvent;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityCollisionHandler;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.fluid.FlowableFluid;
import net.minecraft.fluid.Fluid;
import net.minecraft.fluid.FluidState;
import net.minecraft.item.Item;
import net.minecraft.item.Items;
import net.minecraft.network.packet.s2c.play.ParticleS2CPacket;
import net.minecraft.particle.ParticleEffect;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.tag.FluidTags;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;
import net.minecraft.state.StateManager.Builder;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import net.minecraft.world.WorldAccess;
import net.minecraft.world.WorldView;
import net.minecraft.world.event.GameEvent;
import net.minecraft.world.rule.GameRules;
import org.jetbrains.annotations.Nullable;

import static net.minecraft.block.Blocks.AIR;
import static net.minecraft.block.Blocks.POWDER_SNOW;

public abstract class PowderSnowFluid extends FlowableFluid {
    public static final TagKey<Fluid> FLOW_LIKE_WATER = TagKey.of(RegistryKeys.FLUID, Identifier.of("flposn", "flow_like_water"));
    public static final TagKey<Fluid> IS_INFINITE = TagKey.of(RegistryKeys.FLUID, Identifier.of("flposn", "is_infinite"));

    @Override
    public Fluid getFlowing() {
        return Flowing.FLOWING_POWDER_SNOW;
    }

    @Override
    public Fluid getStill() {
        return Still.POWDER_SNOW;
    }

    @Override
    public Item getBucketItem() {
        return Items.POWDER_SNOW;
    }

    @Override
    protected @Nullable ParticleEffect getParticle() {
        return ParticleTypes.SNOWFLAKE;
    }

    @Override
    protected boolean isInfinite(final ServerWorld world) {
        return Registries.FLUID.getEntry(this.getStill()).isIn(IS_INFINITE);
    }

    @Override
    protected void beforeBreakingBlock(WorldAccess world, BlockPos pos, BlockState state) {
        BlockEntity blockEntity = state.hasBlockEntity() ? world.getBlockEntity(pos) : null;
        Block.dropStacks(state, world, pos, blockEntity);
    }

    @Override
    protected int getMaxFlowDistance(WorldView world) {
        return Registries.FLUID.getEntry(this.getStill()).isIn(FLOW_LIKE_WATER) ? 4 : 1;
    }

    @Override
    public float getHeight(final FluidState state) {
        return state.getLevel() / 8f;
    }

    @Override
    public BlockState toBlockState(FluidState state) {
        return POWDER_SNOW.getDefaultState().withIfExists(FluidBlock.LEVEL, getBlockStateLevel(state));
    }

    @Override
    public boolean matchesType(final Fluid fluid) {
        return fluid == Still.POWDER_SNOW || fluid == Flowing.FLOWING_POWDER_SNOW;
    }

    @Override
    protected int getLevelDecreasePerBlock(WorldView world) {
        return Registries.FLUID.getEntry(this.getStill()).isIn(FLOW_LIKE_WATER) ? 1 : 8;
    }

    @Override
    public int getTickRate(WorldView world) {
        return Registries.FLUID.getEntry(this.getStill()).isIn(FLOW_LIKE_WATER) ? 100 : Integer.MAX_VALUE;
    }

    @Override
    protected boolean canBeReplacedWith(FluidState state, final BlockView world, final BlockPos pos, Fluid fluid, Direction direction) {
        return fluid.isIn(FluidTags.WATER) || fluid.isIn(FluidTags.LAVA);
    }

    @Override
    protected void onEntityCollision(final World world, final BlockPos pos, final Entity entity, final EntityCollisionHandler handler) {
        super.onEntityCollision(world, pos, entity, handler);
        entity.slowMovement(world.getBlockState(pos), new Vec3d(0.9, 1.5, 0.9));
        if (world instanceof ServerWorld) {
            Vec3d knownMovement = entity.getMovement();
            boolean isMoving = knownMovement.getX() != 0. || knownMovement.getZ() != 0.;
            if (isMoving) {
                Random random = world.getRandom();
                if (random.nextBoolean()) {
                    ((ServerWorld)world).spawnParticles(ParticleTypes.SNOWFLAKE, entity.getX(), pos.getY() + 1, entity.getZ(), 1, 0., 0., 0., MathHelper.nextBetween(random, -1f, 1f) * 0.083333336F, 0.05f, MathHelper.nextBetween(random, -1f, 1f) * 0.083333336f, ParticleS2CPacket.RandomizationType.ALTERNATIVE);
                }
            }
        }
        BlockPos position = pos.toImmutable();
        handler.addPreCallback(CollisionEvent.EXTINGUISH, e -> {
            if (world instanceof ServerWorld) {
                if (e.isOnFire() && (((ServerWorld)world).getGameRules().getValue(GameRules.MOB_GRIEFING) || e instanceof PlayerEntity) && e.canModifyAt((ServerWorld)world, position)) {
                    BlockState blockState = world.getBlockState(position);
                    FluidState fluidState = blockState.getFluidState();
                    if (fluidState.is(this) && blockState == fluidState.getBlockState()) {
                        blockState.getBlock().spawnBreakParticles(world, position, blockState);
                        if (world.setBlockState(position, AIR.getDefaultState())) {
                            world.emitGameEvent(GameEvent.BLOCK_DESTROY, pos, GameEvent.Emitter.of(null, blockState));
                        }
                    }
                }
            }
        });
        handler.addEvent(CollisionEvent.FREEZE);
        handler.addEvent(CollisionEvent.EXTINGUISH);
    }

    @Override
    protected float getBlastResistance() {
        return 1f;
    }

    @Override
    public Optional<SoundEvent> getBucketFillSound() {
        return Optional.of(SoundEvents.ITEM_BUCKET_FILL_POWDER_SNOW);
    }

    public static class Flowing extends PowderSnowFluid {
        public static final Flowing FLOWING_POWDER_SNOW = Registry.register(Registries.FLUID, Identifier.of("minecraft", "flowing_powder_snow"), new Flowing());

        @Override
        protected void appendProperties(Builder<Fluid, FluidState> builder) {
            super.appendProperties(builder);
            builder.add(LEVEL);
        }

        @Override
        public int getLevel(FluidState state) {
            return state.get(LEVEL);
        }

        @Override
        public boolean isStill(FluidState state) {
            return false;
        }
    }

    public static class Still extends PowderSnowFluid {
        public static final Still POWDER_SNOW = Registry.register(Registries.FLUID, Identifier.of("minecraft", "powder_snow"), new Still());

        @Override
        public int getLevel(FluidState state) {
            return 8;
        }

        @Override
        public boolean isStill(FluidState state) {
            return true;
        }
    }
}
