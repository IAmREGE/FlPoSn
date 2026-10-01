package rege.chemicalcompound.mod115.flposn.block;

import net.minecraft.block.BlockRenderType;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.EntityShapeContext;
import net.minecraft.block.FluidBlock;
import net.minecraft.block.ShapeContext;
import net.minecraft.entity.Entity;
import net.minecraft.entity.FallingBlockEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.pathing.NavigationType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.fluid.FlowableFluid;
import net.minecraft.fluid.FluidState;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.sound.SoundEvent;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import net.minecraft.world.WorldView;
import rege.chemicalcompound.mod115.flposn.fluid.PowderSnowFluid;

import static net.minecraft.block.PowderSnowBlock.canWalkOnPowderSnow;
import static net.minecraft.util.shape.VoxelShapes.cuboid;
import static net.minecraft.util.shape.VoxelShapes.empty;

public class PowderSnowBlock extends FluidBlock {
    private static final VoxelShape[] FALLING_SHAPES = new VoxelShape[]{
        cuboid(0., 0., 0., 1., 0.025f, 1.),
        cuboid(0., 0., 0., 1., 0.15f, 1.),
        cuboid(0., 0., 0., 1., 0.275f, 1.),
        cuboid(0., 0., 0., 1., 0.4f, 1.),
        cuboid(0., 0., 0., 1., 0.525f, 1.),
        cuboid(0., 0., 0., 1., 0.65f, 1.),
        cuboid(0., 0., 0., 1., 0.775f, 1.),
        cuboid(0., 0., 0., 1., 0.9f, 1.)
    };
    private static final VoxelShape[] FILLED_SHAPES = new VoxelShape[]{
        cuboid(0., 0., 0., 1., 0.125, 1.),
        cuboid(0., 0., 0., 1., 0.25, 1.),
        cuboid(0., 0., 0., 1., 0.375, 1.),
        cuboid(0., 0., 0., 1., 0.5, 1.),
        cuboid(0., 0., 0., 1., 0.625, 1.),
        cuboid(0., 0., 0., 1., 0.75, 1.),
        cuboid(0., 0., 0., 1., 0.875, 1.),
        cuboid(0., 0., 0., 1., 1., 1.)
    };

    public PowderSnowBlock(FlowableFluid fluid, Settings settings) {
        super(fluid, settings);
    }

    @Override
    protected boolean isSideInvisible(final BlockState state, final BlockState stateFrom, final Direction direction) {
        FluidState fromFluidState = stateFrom.getFluidState();
        if (fromFluidState.getFluid() instanceof PowderSnowFluid) {
            switch (direction) {
                case DOWN: return fromFluidState.getLevel() >= 8;
                case UP: {
                    final int L = state.get(LEVEL);
                    return L == 0 || L > 7;
                }
                default: {
                    final int L1 = state.get(LEVEL);
                    final int L2 = fromFluidState.getBlockState().get(LEVEL);
                    return L2 == 0 || L2 > 7 || (L1 < 8 && L2 <= L1);
                }
            }
        }
        return false;
    }

    @Override
    protected VoxelShape getOutlineShape(final BlockState state, final BlockView world, final BlockPos pos, final ShapeContext context) {
        if (!(context instanceof EntityShapeContext)) {
            return empty();
        }
        Entity entity = ((EntityShapeContext)context).getEntity();
        if (!(entity instanceof LivingEntity)) {
            return empty();
        }
        ItemStack stack = ((LivingEntity)entity).getMainHandStack();
        if (stack == null || stack.isEmpty() || !stack.is(this.fluid.getBucketItem())) {
            return empty();
        }
        final int L = state.get(LEVEL).intValue();
        return FILLED_SHAPES[(L < 8) ? 7 - L : 7];
    }

    @Override
    protected VoxelShape getCullingShape(final BlockState state) {
        return empty();
    }

    public void onLandedUpon(final World world, final BlockState state, final BlockPos pos, final Entity entity, final double fallDistance) {
        if (!(fallDistance < 4.) && entity instanceof LivingEntity) {
            LivingEntity.FallSounds entityFallsounds = ((LivingEntity)entity).getFallSounds();
            SoundEvent fallSound = fallDistance < 7. ? entityFallsounds.small() : entityFallsounds.big();
            entity.playSound(fallSound, 1f, 1f);
        }
    }

    @Override
    protected VoxelShape getCollisionShape(final BlockState state, final BlockView level, final BlockPos pos, final ShapeContext context) {
        Entity entity;
        if (context instanceof EntityShapeContext &&
            (entity = ((EntityShapeContext)context).getEntity()) != null) {
            if (entity.fallDistance > 2.5f) {
                final int L = state.get(LEVEL).intValue();
                return FALLING_SHAPES[(L < 8) ? 7 - L : 7];
            }
            final int L = state.get(LEVEL).intValue();
            VoxelShape shape = FILLED_SHAPES[(L < 8) ? 7 - L : 7];
            if (entity instanceof FallingBlockEntity || canWalkOnPowderSnow(entity) && context.isAbove(shape, pos, false) && !context.isDescending()) {
                return shape;
            }
        }
        return empty();
    }

    @Override
    protected VoxelShape getCameraCollisionShape(final BlockState state, final BlockView world, final BlockPos pos, final ShapeContext context) {
        return empty();
    }

    @Override
    public BlockState onBreak(
        final World world, final BlockPos pos, final BlockState state,
        final PlayerEntity player
    ) {
        BlockState result = super.onBreak(world, pos, state, player);
        if (player.shouldSkipBlockDrops()) {
            world.setBlockState(pos, Blocks.AIR.getDefaultState());
        }
        return result;
    }

    @Override
    protected boolean canPathfindThrough(final BlockState state, final NavigationType type) {
        return true;
    }

    @Override
    protected BlockRenderType getRenderType(final BlockState state) {
        return BlockRenderType.MODEL;
    }

    @Override
    protected ItemStack getPickStack(final WorldView world, final BlockPos pos, final BlockState state, final boolean includeData) {
        return new ItemStack(Items.POWDER_SNOW);
    }
}
