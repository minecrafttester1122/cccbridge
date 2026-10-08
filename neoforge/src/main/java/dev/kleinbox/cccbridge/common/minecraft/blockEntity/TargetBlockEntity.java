package dev.kleinbox.cccbridge.common.minecraft.blockEntity;

import dev.kleinbox.cccbridge.common.CCCRegistries;
import dev.kleinbox.cccbridge.common.computercraft.peripherals.TargetBlockPeripheral;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jspecify.annotations.NonNull;

public class TargetBlockEntity extends BlockEntity implements PeripheralBlockEntity {
    private static final int DEFAULT_WIDTH = 32;
    private static final int DEFAULT_HEIGHT = 8;

    private TargetBlockPeripheral peripheral;
    private int width = DEFAULT_WIDTH;
    private int height = DEFAULT_HEIGHT;

    public TargetBlockEntity(BlockPos pos, BlockState state) {
        super((BlockEntityType<TargetBlockEntity>) CCCRegistries.TARGET_BLOCK_ENTITY.get(), pos, state);
        this.width = DEFAULT_WIDTH;
        this.height = DEFAULT_HEIGHT;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public void afterResize(int width, int height) {
        this.width = width;
        this.height = height;
        setChanged();
    }

    public @NotNull TargetBlockPeripheral getPeripheral(@Nullable Direction side) {
        if (peripheral == null) {
            int safeWidth = Math.max(1, width > 0 ? width : DEFAULT_WIDTH);
            int safeHeight = Math.max(1, height > 0 ? height : DEFAULT_HEIGHT);
            peripheral = new TargetBlockPeripheral(this, safeWidth, safeHeight);
        }

        return peripheral;
    }

    @Override
    protected void loadAdditional(CompoundTag tag, HolderLookup.@NonNull Provider registries) {
        width = tag.contains("width") ? tag.getInt("width") : DEFAULT_WIDTH;
        height = tag.contains("height") ? tag.getInt("height") : DEFAULT_HEIGHT;
        if (width < 1) width = DEFAULT_WIDTH;
        if (height < 1) height = DEFAULT_HEIGHT;
        super.loadAdditional(tag, registries);
    }

    @Override
    protected void saveAdditional(CompoundTag tag, HolderLookup.@NonNull Provider registries) {
        tag.putInt("width", width);
        tag.putInt("height", height);
        super.saveAdditional(tag, registries);
    }

    @Nullable
    @Override
    public Packet<ClientGamePacketListener> getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create(this);
    }

    @Override
    public @NotNull CompoundTag getUpdateTag(HolderLookup.@NotNull Provider registries) {
        return saveWithoutMetadata(registries);
    }

    @Override
    public void setChanged() {
        super.setChanged();

        if (level == null) return;

        BlockState state = getBlockState();
        level.sendBlockUpdated(worldPosition, state, state, Block.UPDATE_ALL);
    }
}
