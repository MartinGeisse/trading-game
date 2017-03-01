package name.martingeisse.trading_game.game.definition;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.google.common.collect.ImmutableList;
import com.google.inject.Singleton;
import name.martingeisse.trading_game.common.util.JacksonUtil;
import name.martingeisse.trading_game.common.util.UnexpectedExceptionException;
import name.martingeisse.trading_game.game.action.Action;
import name.martingeisse.trading_game.game.action.ActionSerializer;
import name.martingeisse.trading_game.game.crafting.CraftingRecipe;
import name.martingeisse.trading_game.game.crafting.FixedCraftingRecipe;
import name.martingeisse.trading_game.game.item.ImmutableItemStack;
import name.martingeisse.trading_game.game.item.ImmutableItemStacks;
import name.martingeisse.trading_game.game.item.ItemType;
import name.martingeisse.trading_game.game.item.ItemTypeSerializer;
import name.martingeisse.trading_game.game.skill.Skill;
import name.martingeisse.trading_game.game.skill.SkillByNameSerializer;
import name.martingeisse.trading_game.game.skill.SkillSerializer;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * This object defines how the game works on a game-logic level. It is injected into all parts that need such static
 * definitions.
 */
@Singleton
public final class GameDefinition implements ItemTypeSerializer, SkillSerializer, ActionSerializer {

	private final ImmutableList<ItemType> itemTypes;

	private final ImmutableList<Skill> skills;
	private final ItemType redPixelItemType;
	private final ItemType redPixelAssemblyItemType;

	private final SkillByNameSerializer skillByNameSerializer;

	/**
	 *
	 */
	public GameDefinition() {


		ItemType redPixelItemType = new ItemType("red pixel", "red_pixel.png", 10);
		ItemType redPixelAssemblyItemType = new ItemType("red pixel assembly", "red_pixel_assembly.png", 10);
		ItemType redPixelLineItemType = new ItemType("red pixel line", "no_icon.png", 10);
		ItemType redPixelGlueItemType = new ItemType("red pixel glue", "no_icon.png", 10);

		ItemType pixelAxeItemType = new ItemType("pixel axe", "no_icon.png", 10);
		ItemType pixelHoeItemType = new ItemType("pixel hoe", "no_icon.png", 10);
		ItemType pixelHammerItemType = new ItemType("pixel hammer", "no_icon.png", 10);
		ItemType pixelPickaxeItemType = new ItemType("pixel pickaxe", "no_icon.png", 10);

		ItemType logItemType = new ItemType("log", "no_icon.png", 10);

		List<ItemType> itemTypes = new ArrayList<>();
		itemTypes.add(redPixelItemType);
		itemTypes.add(redPixelAssemblyItemType);
		itemTypes.add(redPixelLineItemType);
		itemTypes.add(redPixelGlueItemType);
		itemTypes.add(pixelAxeItemType);
		itemTypes.add(pixelHoeItemType);
		itemTypes.add(pixelHammerItemType);
		itemTypes.add(pixelPickaxeItemType);
		itemTypes.add(logItemType);
		this.itemTypes = ImmutableList.copyOf(itemTypes);

		CraftingRecipe redPixelCraftingRecipe = new FixedCraftingRecipe(100, ImmutableItemStacks.EMPTY, redPixelItemType);
		CraftingRecipe redPixelAssemblyCraftingRecipe = new FixedCraftingRecipe(300, ImmutableItemStacks.from(redPixelItemType, 5), redPixelAssemblyItemType);
		CraftingRecipe redPixelLineCraftingRecipe = new FixedCraftingRecipe(300, ImmutableItemStacks.from(redPixelItemType, 10), redPixelLineItemType);
		CraftingRecipe redPixelGlueCraftingRecipe = new FixedCraftingRecipe(50, ImmutableItemStacks.from(redPixelItemType, 5), redPixelGlueItemType);

		CraftingRecipe fellTreeRecipe = new FixedCraftingRecipe(1000, ImmutableItemStacks.from(pixelAxeItemType, 1), ImmutableItemStacks.from(pixelAxeItemType, 1, logItemType, 3));

		ImmutableItemStacks pixelToolBillOfMaterials = new ImmutableItemStacks(ImmutableList.of(
			new ImmutableItemStack(redPixelLineItemType, 3),
			new ImmutableItemStack(redPixelAssemblyItemType, 5),
			new ImmutableItemStack(redPixelItemType, 10),
			new ImmutableItemStack(redPixelGlueItemType, 1)
		));
		CraftingRecipe pixelAxeCraftingRecipe = new FixedCraftingRecipe(1000, pixelToolBillOfMaterials, pixelAxeItemType);
		CraftingRecipe pixelHoeCraftingRecipe = new FixedCraftingRecipe(1000, pixelToolBillOfMaterials, pixelHoeItemType);
		CraftingRecipe pixelHammerCraftingRecipe = new FixedCraftingRecipe(1000, pixelToolBillOfMaterials, pixelHammerItemType);
		CraftingRecipe pixelPickaxeCraftingRecipe = new FixedCraftingRecipe(1000, pixelToolBillOfMaterials, pixelPickaxeItemType);

		// TODO
//		this.contextFreeActionDefinitions = ImmutableList.of(
//			new ContextFreeActionDefinition(redPixelCraftingRecipe),
//			new ContextFreeActionDefinition(redPixelAssemblyCraftingRecipe),
//			new ContextFreeActionDefinition(redPixelLineCraftingRecipe),
//			new ContextFreeActionDefinition(redPixelGlueCraftingRecipe),
//
//			new ContextFreeActionDefinition(pixelAxeCraftingRecipe),
//			new ContextFreeActionDefinition(pixelHoeCraftingRecipe),
//			new ContextFreeActionDefinition(pixelHammerCraftingRecipe),
//			new ContextFreeActionDefinition(pixelPickaxeCraftingRecipe),
//
//			new ContextFreeActionDefinition("Fell a tree", fellTreeRecipe)
//		);

		this.skills = ImmutableList.of(new Skill() {

			@Override
			public String getName() {
				return "being cool";
			}

			@Override
			public int getRequiredSecondsForLearning() {
				return 10_000;
			}

		});
		this.skillByNameSerializer = new SkillByNameSerializer(skills);

		this.redPixelItemType = redPixelItemType;
		this.redPixelAssemblyItemType = redPixelAssemblyItemType;
	}

	/**
	 * Getter method.
	 *
	 * @return the skills
	 */
	public ImmutableList<Skill> getSkills() {
		return skills;
	}

	/**
	 * Getter method.
	 *
	 * @return the redPixelItemType
	 */
	public ItemType getRedPixelItemType() {
		return redPixelItemType;
	}

	/**
	 * Getter method.
	 *
	 * @return the redPixelAssemblyItemType
	 */
	public ItemType getRedPixelAssemblyItemType() {
		return redPixelAssemblyItemType;
	}

	@Override
	public String serializeItemType(ItemType itemType) {
		return itemType.getName();
	}

	@Override
	public ItemType deserializeItemType(String serializedItemType) {
		for (ItemType itemType : itemTypes) {
			if (itemType.getName().equals(serializedItemType)) {
				return itemType;
			}
		}
		throw new RuntimeException("cannot deserialize item type: " + serializedItemType);
	}

	@Override
	public String serializeSkill(Skill skill) {
		return skillByNameSerializer.serializeSkill(skill);
	}

	@Override
	public Skill deserializeSkill(String serializedSkill) {
		return skillByNameSerializer.deserializeSkill(serializedSkill);
	}

	@Override
	public String serializeAction(Action action) {
		try {
			return JacksonUtil.objectMapper.writeValueAsString(action);
		} catch (JsonProcessingException e) {
			throw new UnexpectedExceptionException(e);
		}
	}

	@Override
	public Action deserializeAction(String serializedAction) {
		try {
			return JacksonUtil.objectMapper.readValue(serializedAction, Action.class);
		} catch (IOException e) {
			throw new UnexpectedExceptionException(e);
		}
	}

}
