package com.example.testmod.block.pedestal;

import com.example.testmod.TestMod;
import com.example.testmod.registries.BlockRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.Connection;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.world.Containers;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

import javax.annotation.Nonnull;

public class PedestalTile extends BlockEntity {
    private static final String NBT_HELD_ITEM = "heldItem";

    //    private final ItemStackHandler itemHandler = new ItemStackHandler(4) {
//        @Override
//        protected void onContentsChanged(int slot) {
//            updateMenuSlots();
//            setChanged();
//        }
//    };
//
//    private final LazyOptional<IItemHandler> lazyItemHandler = LazyOptional.of(() -> itemHandler);
    private ItemStack heldItem = ItemStack.EMPTY;

    public PedestalTile(BlockPos pWorldPosition, BlockState pBlockState) {
        super(BlockRegistry.PEDESTAL_TILE.get(), pWorldPosition, pBlockState);
    }

    public ItemStack getHeldItem() {
        return heldItem;
    }

    public void setHeldItem(ItemStack newItem) {
        heldItem = newItem;
        setChanged();

    }

//    @Override
//    public void onLoad() {
//        super.onLoad();
//        lazyItemHandler = LazyOptional.of(() -> itemHandler);
//
//    }


    public void drops() {
        SimpleContainer simpleContainer = new SimpleContainer(heldItem);

        Containers.dropContents(this.level, this.worldPosition, simpleContainer);
    }

    @Override
    public void load(CompoundTag nbt) {
        super.load(nbt);
        TestMod.LOGGER.debug("Loading Pedestal NBT");
        readNBT(nbt);

    }

    @Override
    protected void saveAdditional(@Nonnull CompoundTag tag) {
        //TestMod.LOGGER.debug("saveAdditional tag:{}", tag);
        //tag.put("inventory", itemHandler.serializeNBT());
        writeNBT(tag);
    }

    @Override
    public CompoundTag getUpdateTag() {
        CompoundTag tag = new CompoundTag();
        //tag.put("inventory", itemHandler.serializeNBT());
        writeNBT(tag);
        //TestMod.LOGGER.debug("getUpdateTag tag:{}", tag);
        return tag;
    }

    @Override
    public ClientboundBlockEntityDataPacket getUpdatePacket() {
        //var packet = ClientboundBlockEntityDataPacket.create(this);
        //TestMod.LOGGER.debug("getUpdatePacket: packet.getTag:{}", packet.getTag());
        CompoundTag nbt = writeNBT(new CompoundTag());
        return ClientboundBlockEntityDataPacket.create(this, (block) -> nbt);
    }

    @Override
    public void onDataPacket(Connection net, ClientboundBlockEntityDataPacket pkt) {
        //TestMod.LOGGER.debug("onDataPacket: pkt.getTag:{}", pkt.getTag());
        handleUpdateTag(pkt.getTag());
        level.sendBlockUpdated(worldPosition, getBlockState(), getBlockState(), Block.UPDATE_ALL);
    }

    @Override
    public void handleUpdateTag(CompoundTag tag) {
        //TestMod.LOGGER.debug("handleUpdateTag: tag:{}", tag);
        if (tag != null) {
            load(tag);
        }
    }

    //    @Nonnull
//    @Override
//    public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap, @javax.annotation.Nullable Direction side) {
//        if (cap == ForgeCapabilities.ITEM_HANDLER) {
//            return lazyItemHandler.cast();
//        }
//
//        return super.getCapability(cap, side);
//    }

    private CompoundTag writeNBT(CompoundTag nbt) {
        nbt.put(NBT_HELD_ITEM, heldItem.serializeNBT());
        //TestMod.LOGGER.debug("getUpdateTag tag:{}", tag);
        return nbt;
    }

    private CompoundTag readNBT(CompoundTag nbt) {
        if (nbt.contains(NBT_HELD_ITEM)) {
            //itemHandler.deserializeNBT(nbt.getCompound("inventory"));
            TestMod.LOGGER.debug("Pedestal NBT contains held item ({})", nbt.getCompound(NBT_HELD_ITEM));

            //heldItem.deserializeNBT(nbt.getCompound(NBT_HELD_ITEM));
            heldItem = ItemStack.of(nbt.getCompound(NBT_HELD_ITEM));
            TestMod.LOGGER.debug("Held Item: {}", heldItem);

        }
        return nbt;
    }
}
