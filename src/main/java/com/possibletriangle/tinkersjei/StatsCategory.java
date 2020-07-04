package com.possibletriangle.tinkersjei;

import com.possibletriangle.tinkersjei.TinkersJEI;

import mezz.jei.api.IGuiHelper;
import mezz.jei.api.gui.IDrawable;
import mezz.jei.api.gui.IGuiItemStackGroup;
import mezz.jei.api.gui.IRecipeLayout;
import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.recipe.IRecipeCategory;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.TextComponentTranslation;
import slimeknights.tconstruct.TConstruct;

public class StatsCategory implements IRecipeCategory<StatsWrapper> {

	public static final String UUID = TinkersJEI.MODID + ":tool_stats";
	public static final int WIDTH  = 182, HEIGHT = 128, OFFSET_X = 6;

	IDrawable background, icon;

	public StatsCategory(IGuiHelper helper) {
		ResourceLocation icon_location = new ResourceLocation(TinkersJEI.MODID, "textures/icon.png");

		background = helper.createBlankDrawable(WIDTH, HEIGHT);
		icon = helper.createDrawable(icon_location, 0, 0, 16, 16, 16, 16);

	}

	@Override
	public IDrawable getIcon() {
		return icon;
	}

	@Override
	public String getUid() {
		return UUID;
	}

	@Override
	public String getTitle() {
		return new TextComponentTranslation("jei.category.tool_stats.name").getFormattedText();
	}

	@Override
	public String getModName() {
		return TConstruct.modName;
	}

	@Override
	public IDrawable getBackground() {
		return background;
	}

	private static final int PARTS = 0, REPRES = 1, FLUID = 2;

	@Override
	public void setRecipe(IRecipeLayout layout, StatsWrapper wrapper, IIngredients ingredients) {
		IGuiItemStackGroup stacks = layout.getItemStacks();

		if (wrapper.hasFluid()) {
			layout.getFluidStacks().init(FLUID, true, OFFSET_X + 22, 1);
			layout.getFluidStacks().set(FLUID, wrapper.getFluid());
		}

		ItemStack item = wrapper.material.getRepresentativeItem();

		if (item != null && !item.isEmpty()) {
			stacks.init(REPRES, true, OFFSET_X, 0);
			stacks.set(REPRES, item);
		}

		stacks.init(PARTS, true, background.getWidth() - 18 - OFFSET_X, 0);
		stacks.set(PARTS, wrapper.getParts());

	}

}
