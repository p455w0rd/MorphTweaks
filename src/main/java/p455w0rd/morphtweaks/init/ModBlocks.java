package p455w0rd.morphtweaks.init;

import java.util.Arrays;

import net.minecraft.block.Block;
import net.minecraft.util.NonNullList;
import p455w0rd.morphtweaks.api.IModelHolder;
import p455w0rd.morphtweaks.blocks.BlockEnchanter;
import p455w0rd.morphtweaks.blocks.BlockFakeAir;
import p455w0rd.morphtweaks.blocks.BlockNewTFPortal;
import p455w0rd.morphtweaks.blocks.BlockVoidifier;

/**
 * @author p455w0rd
 *
 */
public class ModBlocks {

	private static final NonNullList<Block> BLOCK_LIST = NonNullList.<Block>create();
	public static final Block NEW_TF_PORTAL = new BlockNewTFPortal();
	public static final Block BLOCK_FAKE_AIR = new BlockFakeAir();
	public static final Block VOIDIFIER = new BlockVoidifier();
	public static final Block ENCHANTER = new BlockEnchanter();

	public static void registerModels() {
		for (Block block : getList()) {
			if (block instanceof IModelHolder) {
				((IModelHolder) block).initModel();
			}
		}
	}

	public static NonNullList<Block> getList() {
		if (BLOCK_LIST.isEmpty()) {
			BLOCK_LIST.addAll(Arrays.asList(NEW_TF_PORTAL, BLOCK_FAKE_AIR, VOIDIFIER, ENCHANTER));
		}
		return BLOCK_LIST;
	}

}
