package dev.kleinbox.cccbridge.common.create.display;

import com.simibubi.create.content.redstone.displayLink.DisplayLinkContext;
import com.simibubi.create.content.redstone.displayLink.target.DisplayBoardTarget;
import com.simibubi.create.content.redstone.displayLink.target.DisplayTargetStats;
import dan200.computercraft.core.terminal.Terminal;
import dev.kleinbox.cccbridge.common.assistance.CharsetManipulator;
import dev.kleinbox.cccbridge.common.minecraft.blockEntity.TargetBlockEntity;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.level.block.entity.BlockEntity;

import java.util.ArrayList;
import java.util.List;

public class TargetBlockDisplayTarget extends DisplayBoardTarget {

    @Override
    public void acceptFlapText(int offset, List<List<MutableComponent>> text, DisplayLinkContext context) {
        if (!(context.getTargetBlockEntity() instanceof TargetBlockEntity target))
            return;

        var peripheral = target.getPeripheral(null);
        if (peripheral == null)
            return;

        Terminal term = peripheral.term;
        if (term == null)
            return;

        List<String> source = new ArrayList<>();

        for (List<MutableComponent> line : text) {
            StringBuilder parts = new StringBuilder();
            for (MutableComponent sLine : line)
                parts.append(sLine.getString()).append(" ");

            source.add(CharsetManipulator.toCCTxt(parts.toString()));
        }

        synchronized (term) {
            for (int i = 0; i < source.size(); i++) {
                term.setCursorPos(0, offset + i);
                term.clearLine();
            }

            int y = offset;
            for (String line : source) {
                term.setCursorPos(0, y);
                y++;

                term.write(line);
            }
        }
    }

    @Override
    public DisplayTargetStats provideStats(DisplayLinkContext context) {
        BlockEntity block = context.getTargetBlockEntity();
        if (!(block instanceof TargetBlockEntity targetBlock))
            return new DisplayTargetStats(1, 1, this);

        var peripheral = targetBlock.getPeripheral(null);
        if (peripheral == null)
            return new DisplayTargetStats(1, 1, this);

        Terminal term = peripheral.term;
        if (term == null)
            return new DisplayTargetStats(1, 1, this);

        return new DisplayTargetStats(term.getHeight(), term.getWidth(), this);
    }
}
