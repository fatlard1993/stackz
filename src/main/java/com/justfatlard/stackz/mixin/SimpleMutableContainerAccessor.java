package com.justfatlard.stackz.mixin;

import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.SimpleMutableContainer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

import java.util.List;

/**
 * The backing list a bundle's mutable view holds.
 *
 * <p>It lives on this superclass rather than on {@code BundleContents$Mutable},
 * which is where a shadow declared on the subclass looks for it and does not
 * find it. That failure is not a compile error: the mixin applies at class load,
 * so the first code path to make a bundle mutable takes the whole game down.
 */
@Mixin(SimpleMutableContainer.class)
public interface SimpleMutableContainerAccessor {
	@Accessor("items")
	List<ItemStack> stackz$items();
}
