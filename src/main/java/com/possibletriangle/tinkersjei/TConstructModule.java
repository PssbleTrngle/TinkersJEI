package com.possibletriangle.tinkersjei;

import java.util.ArrayList;

import mezz.jei.api.IGuiHelper;
import mezz.jei.api.IModPlugin;
import mezz.jei.api.IModRegistry;
import mezz.jei.api.JEIPlugin;
import mezz.jei.api.recipe.IRecipeCategoryRegistration;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.Loader;
import slimeknights.tconstruct.library.TinkerRegistry;
import slimeknights.tconstruct.library.events.TinkerRegisterEvent;
import slimeknights.tconstruct.library.materials.Material;
import slimeknights.tconstruct.tools.TinkerMaterials;
import slimeknights.tconstruct.tools.TinkerTools;

@JEIPlugin
public class TConstructModule implements IModPlugin {

	@Override
	public void register(IModRegistry registry) {

		if (Loader.isModLoaded("tconstruct")) {
			registry.addRecipes(recipes(registry.getJeiHelpers().getGuiHelper()), StatsCategory.UUID);
			registry.addRecipeCatalyst(new ItemStack(TinkerTools.toolForge, 1), StatsCategory.UUID);
			registry.addRecipeCatalyst(new ItemStack(TinkerTools.toolTables, 1, 3), StatsCategory.UUID);

		}

	}

	private ArrayList<StatsWrapper> recipes(IGuiHelper helper) {
		ArrayList<StatsWrapper> list = new ArrayList<StatsWrapper>();

		for (Material material : TinkerRegistry.getAllMaterials())
			if (!material.isHidden() && material.hasItems() && !material.getAllStats().isEmpty())
				list.add(new StatsWrapper(material, helper));

		return list;
	}

	@Override
	public void registerCategories(IRecipeCategoryRegistration registry) {

		if (Loader.isModLoaded("tconstruct"))
			registry.addRecipeCategories(new StatsCategory(registry.getJeiHelpers().getGuiHelper()));

	}

}
