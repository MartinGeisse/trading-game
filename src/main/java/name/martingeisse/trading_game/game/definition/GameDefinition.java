package name.martingeisse.trading_game.game.definition;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import name.martingeisse.trading_game.game.equipment.PlayerShipEquipmentSlotType;
import name.martingeisse.trading_game.game.item.ItemType;
import name.martingeisse.trading_game.game.manufacturing.Blueprint;
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
	private final ImmutableList<ImmutableList<ItemType>> upgradeItemTypes;

	private final ImmutableList<Skill> skills;

	private final ImmutableList<Blueprint> blueprints;

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

		List<ItemType> upgradeItemTypes = new ArrayList<>();
		List<ImmutableList<ItemType>> upgradeItemTypesByLevel = new ArrayList<>();
		int factor = 1;
		for (int level = 2; level < 10; level++) {
			List<ItemType> upgradesForThisLevel = new ArrayList<>();
			upgradesForThisLevel.add(new ItemType("level " + level + " engines", "no_icon.png", level * 10,
					PlayerShipEquipmentSlotType.ENGINE, ImmutableMap.of(PlayerAttributeKey.SHIP_MOVEMENT_SPEED,
					3 * factor))); // TODO player is still moving fast!
			upgradesForThisLevel.add(new ItemType("level " + level + " mining gear", "no_icon.png", level * 10,
					PlayerShipEquipmentSlotType.MINING_GEAR, ImmutableMap.of(PlayerAttributeKey.MINING_SPEED,
					10_000 * factor)));
			upgradesForThisLevel.add(new ItemType("level " + level + " cargo hold", "no_icon.png", level * 10,
					PlayerShipEquipmentSlotType.CARGO_HOLD, ImmutableMap.of(PlayerAttributeKey.MAXIMUM_CARGO_MASS,
					5_000 * factor)));
			upgradeItemTypes.addAll(upgradesForThisLevel);
			upgradeItemTypesByLevel.add(ImmutableList.copyOf(upgradesForThisLevel));
			factor = factor * 3 / 2;
		}
		this.upgradeItemTypes = ImmutableList.copyOf(upgradeItemTypesByLevel);

		List<ItemType> itemTypes = new ArrayList<>();
		itemTypes.add(ironOre);
		itemTypes.add(copperOre);
		itemTypes.add(aluminiumOre);
		itemTypes.add(silverOre);
		itemTypes.add(titaniumOre);
		itemTypes.add(tinOre);
		itemTypes.addAll(upgradeItemTypes);
		this.itemTypes = ImmutableList.copyOf(itemTypes);

		/* TODO
		Blueprint redPixelBlueprint = new FixedBlueprint(100, ImmutableItemStacks.EMPTY, redPixelItemType);
		Blueprint redPixelAssemblyBlueprint = new FixedBlueprint(300, ImmutableItemStacks.from(redPixelItemType, 5), redPixelAssemblyItemType);
		Blueprint redPixelLineBlueprint = new FixedBlueprint(300, ImmutableItemStacks.from(redPixelItemType, 10), redPixelLineItemType);
		Blueprint redPixelGlueBlueprint = new FixedBlueprint(50, ImmutableItemStacks.from(redPixelItemType, 5), redPixelGlueItemType);
		Blueprint fellTreeBlueprint = new FixedBlueprint(1000, ImmutableItemStacks.from(pixelAxeItemType, 1), ImmutableItemStacks.from(pixelAxeItemType, 1, logItemType, 3));
		ImmutableItemStacks pixelToolBillOfMaterials = new ImmutableItemStacks(ImmutableList.of(
				new ImmutableItemStack(redPixelLineItemType, 3),
				new ImmutableItemStack(redPixelAssemblyItemType, 5),
				new ImmutableItemStack(redPixelItemType, 10),
				new ImmutableItemStack(redPixelGlueItemType, 1)
		));
		Blueprint pixelAxeBlueprint = new FixedBlueprint(1000, pixelToolBillOfMaterials, pixelAxeItemType);
		Blueprint pixelHoeBlueprint = new FixedBlueprint(1000, pixelToolBillOfMaterials, pixelHoeItemType);
		Blueprint pixelHammerBlueprint = new FixedBlueprint(1000, pixelToolBillOfMaterials, pixelHammerItemType);
		Blueprint pixelPickaxeBlueprint = new FixedBlueprint(1000, pixelToolBillOfMaterials, pixelPickaxeItemType);
		*/

		// TODO
//		this.contextFreeActionDefinitions = ImmutableList.of(
//			new ContextFreeActionDefinition(redPixelBlueprint),
//			new ContextFreeActionDefinition(redPixelAssemblyBlueprint),
//			new ContextFreeActionDefinition(redPixelLineBlueprint),
//			new ContextFreeActionDefinition(redPixelGlueBlueprint),
//
//			new ContextFreeActionDefinition(pixelAxeBlueprint),
//			new ContextFreeActionDefinition(pixelHoeBlueprint),
//			new ContextFreeActionDefinition(pixelHammerBlueprint),
//			new ContextFreeActionDefinition(pixelPickaxeBlueprint),
//
//			new ContextFreeActionDefinition("Fell a tree", fellTreeBlueprint)
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

		this.blueprints = ImmutableList.of();
	}

	public ImmutableList<Skill> getSkills() {
		return skills;
	}

	public ImmutableList<ItemType> getOreItemTypes() {
		return oreItemTypes;
	}

	public ImmutableList<ImmutableList<ItemType>> getUpgradeItemTypes() {
		return upgradeItemTypes;
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

	public ImmutableList<Blueprint> getAllBlueprints() {
		return blueprints;
	}

}
