package name.martingeisse.trading_game.game.definition;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import name.martingeisse.trading_game.game.equipment.PlayerShipEquipmentSlotType;
import name.martingeisse.trading_game.game.item.ItemType;
import name.martingeisse.trading_game.game.player.PlayerAttributeKey;
import name.martingeisse.trading_game.game.skill.SimpleSkill;
import name.martingeisse.trading_game.game.skill.Skill;

import java.util.ArrayList;
import java.util.List;

/**
 * This object defines how the game works on a game-logic level. It is injected into all parts that need such static
 * definitions.
 */
@Singleton
public final class GameDefinition {

	private final ImmutableList<ItemType> itemTypes;
	private final ImmutableList<ItemType> oreItemTypes;

	private final ImmutableList<Skill> skills;

	/**
	 *
	 */
	@Inject
	public GameDefinition() {

		ItemType ironOre = new ItemType("iron ore", "no_icon.png", 10);
		ItemType copperOre = new ItemType("copper ore", "no_icon.png", 10);
		ItemType aluminiumOre = new ItemType("aluminium ore", "no_icon.png", 10);
		ItemType silverOre = new ItemType("silver ore", "no_icon.png", 10);
		ItemType titaniumOre = new ItemType("titanium ore", "no_icon.png", 10);
		ItemType tinOre = new ItemType("tin ore", "no_icon.png", 10);
		this.oreItemTypes = ImmutableList.of(ironOre, copperOre, aluminiumOre, silverOre, titaniumOre, tinOre);

		ItemType level2Engine = new ItemType("level 2 engines", "no_icon.png", 10, PlayerShipEquipmentSlotType.ENGINE, ImmutableMap.of(PlayerAttributeKey.SHIP_MOVEMENT_SPEED, 10_000));
		ItemType level3Engine = new ItemType("level 3 engines", "no_icon.png", 10, PlayerShipEquipmentSlotType.ENGINE, ImmutableMap.of(PlayerAttributeKey.SHIP_MOVEMENT_SPEED, 20_000));
		ItemType level4Engine = new ItemType("level 4 engines", "no_icon.png", 10, PlayerShipEquipmentSlotType.ENGINE, ImmutableMap.of(PlayerAttributeKey.SHIP_MOVEMENT_SPEED, 30_000));
		ItemType level2MiningGear = new ItemType("level 2 mining gear", "no_icon.png", 10, PlayerShipEquipmentSlotType.MINING_GEAR, ImmutableMap.of(PlayerAttributeKey.MINING_SPEED, 10_000));
		ItemType level3MiningGear = new ItemType("level 3 mining gear", "no_icon.png", 10, PlayerShipEquipmentSlotType.MINING_GEAR, ImmutableMap.of(PlayerAttributeKey.MINING_SPEED, 20_000));
		ItemType level4MiningGear = new ItemType("level 4 mining gear", "no_icon.png", 10, PlayerShipEquipmentSlotType.MINING_GEAR, ImmutableMap.of(PlayerAttributeKey.MINING_SPEED, 30_000));
		ItemType level2CargoHold = new ItemType("level 2 cargo hold", "no_icon.png", 10, PlayerShipEquipmentSlotType.CARGO_HOLD, ImmutableMap.of(PlayerAttributeKey.MAXIMUM_CARGO_MASS, 5_000));
		ItemType level3CargoHold = new ItemType("level 3 cargo hold", "no_icon.png", 10, PlayerShipEquipmentSlotType.CARGO_HOLD, ImmutableMap.of(PlayerAttributeKey.MAXIMUM_CARGO_MASS, 10_000));
		ItemType level4CargoHold = new ItemType("level 4 cargo hold", "no_icon.png", 10, PlayerShipEquipmentSlotType.CARGO_HOLD, ImmutableMap.of(PlayerAttributeKey.MAXIMUM_CARGO_MASS, 20_000));

		List<ItemType> itemTypes = new ArrayList<>();
		itemTypes.add(ironOre);
		itemTypes.add(copperOre);
		itemTypes.add(aluminiumOre);
		itemTypes.add(silverOre);
		itemTypes.add(titaniumOre);
		itemTypes.add(tinOre);
		itemTypes.add(level2Engine);
		itemTypes.add(level3Engine);
		itemTypes.add(level4Engine);
		itemTypes.add(level2MiningGear);
		itemTypes.add(level3MiningGear);
		itemTypes.add(level4MiningGear);
		itemTypes.add(level2CargoHold);
		itemTypes.add(level3CargoHold);
		itemTypes.add(level4CargoHold);
		this.itemTypes = ImmutableList.copyOf(itemTypes);

		/* TODO
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
		*/

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

		this.skills = ImmutableList.of(
				new SimpleSkill("Navigation", 10_000),
				new SimpleSkill("Manufacturing", 10_000),
				new SimpleSkill("Industry", 10_000),
				new SimpleSkill("Science", 10_000),
				new SimpleSkill("Supply Chain Management", 10_000),
				new SimpleSkill("Mining", 10_000),
				new SimpleSkill("Industry", 10_000),
				new SimpleSkill("Jury-rigging", 10_000),
				new SimpleSkill("Electronics", 10_000)
		);
	}

	public ImmutableList<Skill> getSkills() {
		return skills;
	}

	public ImmutableList<ItemType> getOreItemTypes() {
		return oreItemTypes;
	}

	public ItemType getItemTypeByName(String itemTypeName) {
		for (ItemType itemType : itemTypes) {
			if (itemType.getName().equals(itemTypeName)) {
				return itemType;
			}
		}
		return null;
	}

	public Skill getSkillByName(String skillName) {
		for (Skill skill : skills) {
			if (skill.getName().equals(skillName)) {
				return skill;
			}
		}
		return null;
	}

}
