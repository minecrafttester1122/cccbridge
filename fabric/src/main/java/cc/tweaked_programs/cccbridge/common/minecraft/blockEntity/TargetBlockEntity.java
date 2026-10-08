package cc.tweaked_programs.cccbridge.common.minecraft.blockEntity;

import cc.tweaked_programs.cccbridge.common.CCCRegistries;
import cc.tweaked_programs.cccbridge.common.computercraft.peripherals.TargetBlockPeripheral;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

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

    public @NotNull TargetBlockPeripheral getPeripheral(@Nullable Direction side) {
        if (peripheral == null) {
            int safeWidth = Math.max(1, width > 0 ? width : DEFAULT_WIDTH);
            int safeHeight = Math.max(1, height > 0 ? height : DEFAULT_HEIGHT);
            peripheral = new TargetBlockPeripheral(this);
        }

        return peripheral;
    }
}
