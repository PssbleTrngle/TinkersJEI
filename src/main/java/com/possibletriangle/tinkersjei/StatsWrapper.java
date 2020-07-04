package com.possibletriangle.tinkersjei;

import java.awt.Color;
import java.util.*;

import com.possibletriangle.tinkersjei.TinkersJEI;

import mezz.jei.api.IGuiHelper;
import mezz.jei.api.gui.IDrawable;
import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.ingredients.VanillaTypes;
import mezz.jei.api.recipe.IRecipeWrapper;
import net.minecraft.client.Minecraft;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.fluids.FluidStack;
import slimeknights.tconstruct.TConstruct;
import slimeknights.tconstruct.library.TinkerRegistry;
import slimeknights.tconstruct.library.materials.IMaterialStats;
import slimeknights.tconstruct.library.materials.Material;
import slimeknights.tconstruct.library.smeltery.MeltingRecipe;
import slimeknights.tconstruct.library.tools.IToolPart;
import slimeknights.tconstruct.library.traits.ITrait;

public class StatsWrapper implements IRecipeWrapper {

	final Material material;
	private final IDrawable slot;

	public StatsWrapper(Material material, IGuiHelper helper) {
		this.material = material;

		slot = helper.getSlotDrawable();
	}

	@Override
	public void getIngredients(IIngredients ingredients) {

		ingredients.setInputs(VanillaTypes.ITEM, getRepresantives());
		ingredients.setOutputs(VanillaTypes.ITEM, getRepresantives());

		if (material.hasFluid()) {
			ingredients.setInputs(VanillaTypes.FLUID, Collections.singletonList(new FluidStack(material.getFluid(), 1)));
			ingredients.setOutputs(VanillaTypes.FLUID, Collections.singletonList(new FluidStack(material.getFluid(), 1)));
		}

	}

	public List<ItemStack> getRepresantives() {

		ArrayList<ItemStack> list = new ArrayList<ItemStack>();

		if (material.hasFluid()) {
			for (MeltingRecipe recipe : TinkerRegistry.getAllMeltingRecipies())
				if (material.getFluid().equals(recipe.output.getFluid()))
					list.addAll(recipe.input.getInputs());
		}
		if (material.getRepresentativeItem() != null && !material.getRepresentativeItem().isEmpty())
			list.add(material.getRepresentativeItem());
		list.addAll(getParts());

		return list;

	}

	public List<ItemStack> getParts() {

		ArrayList<ItemStack> list = new ArrayList<ItemStack>();

		for (IToolPart part : TinkerRegistry.getToolParts())
			if (part.canUseMaterial(material)) {
				ItemStack stack = part.getItemstackWithMaterial(material);
				if (!stack.equals(material.getShard()))
					list.add(part.getItemstackWithMaterial(material));
			}

		return list;

	}

	@Override
	public void drawInfo(Minecraft minecraft, int recipeWidth, int recipeHeight, int mouseX, int mouseY) {

		ItemStack item = material.getRepresentativeItem();
		if (item != null && !item.isEmpty())
			slot.draw(minecraft, recipeWidth - 18 - StatsCategory.OFFSET_X, 0);
		slot.draw(minecraft, StatsCategory.OFFSET_X, 0);

		if (material.hasFluid())
			slot.draw(minecraft, StatsCategory.OFFSET_X + 22 - 1, 0);

		{
			String s = material.getLocalizedName();
			int textWidth = minecraft.fontRenderer.getStringWidth(s);
			minecraft.fontRenderer.drawString(s, (recipeWidth - textWidth) / 2.0F, 4, material.materialTextColor, true);
		}

		int h = minecraft.fontRenderer.FONT_HEIGHT, offset = 22;

		if (!material.getAllTraits().isEmpty()) {

			ArrayList<String> traits = new ArrayList<>();
			for (ITrait trait : material.getAllTraits()) if(trait != null && trait.getLocalizedName() != null)
				traits.add(trait.getLocalizedName());

			HashMap<Integer, String> traitList = new HashMap<>();
			int y = 0;

			for(String trait : traits) if(trait != null) {
				if(traitList.size() == y) traitList.put(y, "");
				else trait = ", " + trait;
				int w = minecraft.fontRenderer.getStringWidth(traitList.get(0) + trait);
				if(w > recipeWidth) {
					y++;
					trait = trait.substring(2);
				}

				String s = traitList.get(y) == null ? "" : traitList.get(y);
				traitList.put(y, s + trait);
			}

			traitList.forEach((row, trait) -> {

				int w = minecraft.fontRenderer.getStringWidth(trait);
				minecraft.fontRenderer.drawString(trait, (recipeWidth - w) / 2.0F, 22 + h * row,
						material.materialTextColor, true);

			});

			offset += (h + 1) * traitList.size();

		}

		int maxLines = (recipeHeight - offset - 4) / h - 1;
		ArrayList<IMaterialStats> stats = new ArrayList<>(material.getAllStats());
		stats.removeIf(stat -> stat.getLocalizedInfo().isEmpty());
		IMaterialStats stat = stats.get((int) (System.currentTimeMillis() / 2000 % stats.size()));

		ArrayList<String> lines = new ArrayList<>();
		lines.add(TextFormatting.BOLD.toString() + stat.getLocalizedName() + ": ");
		lines.addAll(stat.getLocalizedInfo());

		for (int y = 0; y < Math.min(maxLines, lines.size()); y++) {
			String line = lines.get(y);

			int textWidth = minecraft.fontRenderer.getStringWidth(line);
			minecraft.fontRenderer.drawString(line, (recipeWidth - textWidth) / 2, (y % maxLines) * h + offset + 4,
					Color.GRAY.getRGB());

		}

	}

	public boolean hasFluid() {
		return material.hasFluid();
	}

	public FluidStack getFluid() {
		return new FluidStack(material.getFluid(), 1000);
	}

}
