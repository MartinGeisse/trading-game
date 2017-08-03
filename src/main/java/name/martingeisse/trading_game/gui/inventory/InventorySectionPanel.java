package name.martingeisse.trading_game.gui.inventory;

import name.martingeisse.trading_game.common.util.UnexpectedExceptionException;
import name.martingeisse.trading_game.game.EntityProvider;
import name.martingeisse.trading_game.game.GameLogicException;
import name.martingeisse.trading_game.game.action.ActionQueue;
import name.martingeisse.trading_game.game.action.actions.EquipAction;
import name.martingeisse.trading_game.game.action.actions.LoadUnloadAction;
import name.martingeisse.trading_game.game.action.actions.UnequipAction;
import name.martingeisse.trading_game.game.equipment.PlayerShipEquipment;
import name.martingeisse.trading_game.game.equipment.PlayerShipEquipmentChangedEvent;
import name.martingeisse.trading_game.game.equipment.SlotInfo;
import name.martingeisse.trading_game.game.event.GameEvent;
import name.martingeisse.trading_game.game.event.GameEventBatch;
import name.martingeisse.trading_game.game.event.GameEventEmitter;
import name.martingeisse.trading_game.game.item.*;
import name.martingeisse.trading_game.game.jackson.JacksonService;
import name.martingeisse.trading_game.game.market.MarketOrder;
import name.martingeisse.trading_game.game.market.MarketOrderFactory;
import name.martingeisse.trading_game.game.market.MarketOrderRepository;
import name.martingeisse.trading_game.game.market.MarketOrderType;
import name.martingeisse.trading_game.game.player.Player;
import name.martingeisse.trading_game.game.space.Space;
import name.martingeisse.trading_game.game.space.SpaceObject;
import name.martingeisse.trading_game.game.space.SpaceStation;
import name.martingeisse.trading_game.gui.gamepage.GuiNavigationUtil;
import name.martingeisse.trading_game.gui.item.ItemIcons;
import name.martingeisse.trading_game.gui.websockets.GuiGameEventListener;
import name.martingeisse.trading_game.platform.wicket.AbstractPanel;
import name.martingeisse.trading_game.platform.wicket.MyWicketApplication;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.core.request.handler.IPartialPageRequestHandler;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.image.Image;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.LoadableDetachableModel;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Encapsulates the actual map view and the properties box.
 */
public class InventorySectionPanel extends AbstractPanel implements GuiGameEventListener {

	private final IModel<SpaceStation> itemLoadingSpaceStationModel = new LoadableDetachableModel<SpaceStation>() {
		@Override
		protected SpaceStation load() {
			return getPlayer().getSpaceStationForItemLoading();
		}
	};

	public InventorySectionPanel(String id) {
		super(id);
		setOutputMarkupId(true);
		IModel<List<PlayerBelongingsService.InventoryEntry>> inventoryEntryListModel = new LoadableDetachableModel<List<PlayerBelongingsService.InventoryEntry>>() {
			@Override
			protected List<PlayerBelongingsService.InventoryEntry> load() {
				List<PlayerBelongingsService.InventoryEntry> list = MyWicketApplication.get().getDependency(PlayerBelongingsService.class).getBelongingsForPlayerId(getPlayer().getId());
				long playerShipInventoryId = getPlayer().getShip().getInventoryId();
				Collections.sort(list, (x, y) -> {
					long inventoryId1 = x.getInventoryId();
					long inventoryId2 = y.getInventoryId();
					if (inventoryId1 == inventoryId2) {
						return 0;
					}
					if (inventoryId1 == playerShipInventoryId) {
						return -1;
					}
					if (inventoryId2 == playerShipInventoryId) {
						return 1;
					}
					return Long.compare(inventoryId1, inventoryId2);
				});
				return list;
			}
		};
		add(new ListView<PlayerBelongingsService.InventoryEntry>("inventories", inventoryEntryListModel) {
			@Override
			protected void populateItem(ListItem<PlayerBelongingsService.InventoryEntry> inventoryEntryItem) {
				long inventoryId = inventoryEntryItem.getModelObject().getInventoryId();
				boolean isPlayerShipInventory = (inventoryId == getPlayer().getInventory().getId());
				String title = MyWicketApplication.get().getDependency(InventoryNameService.class).getNameForInventoryId(inventoryId);
				inventoryEntryItem.add(new Label("inventoryTitle", title));
				SpaceObject location = MyWicketApplication.get().getDependency(Space.class).getInventoryLocationOrNull(inventoryId);
				List<MarketOrder> buyOrders;
				if (location == null) {
					buyOrders = new ArrayList<>();
				} else {
					buyOrders = MyWicketApplication.get().getDependency(MarketOrderRepository.class).getMarketOrdersByLocation(location, MarketOrderType.BUY, true);
				}
				inventoryEntryItem.add(new ListView<ImmutableItemStack>("itemStacks", inventoryEntryItem.getModelObject().getItemStacks().getStacks()) {
					@Override
					protected void populateItem(ListItem<ImmutableItemStack> itemStackItem) {
						itemStackItem.add(new Label("size", itemStackItem.getModelObject().getSize()));
						itemStackItem.add(new Label("itemType", itemStackItem.getModelObject().getItemType().getName()));
						itemStackItem.add(new Image("icon", ItemIcons.get(itemStackItem.getModelObject().getItemType())));
						itemStackItem.add(new AjaxLink<Void>("loadLink") {

							@Override
							protected void onConfigure() {
								super.onConfigure();
								SpaceStation spaceStation = itemLoadingSpaceStationModel.getObject();
								setVisible(spaceStation != null && spaceStation.getInventoryId() == inventoryEntryItem.getModelObject().getInventoryId());
							}

							@Override
							public void onClick(AjaxRequestTarget target) {
								SpaceStation spaceStation = itemLoadingSpaceStationModel.getObject();
								Player player = getPlayer();
								ImmutableItemStack itemsToLoad = new ImmutableItemStack(itemStackItem.getModelObject().getItemType(), itemStackItem.getModelObject().getSize());
								ActionQueue actionQueue = player.getActionQueue();
								actionQueue.cancelCurrentAction();
								actionQueue.cancelAllPendingActions();
								GameEventEmitter gameEventEmitter = MyWicketApplication.get().getDependency(GameEventEmitter.class);
								actionQueue.scheduleAction(new LoadUnloadAction(player, spaceStation, LoadUnloadAction.Type.LOAD, itemsToLoad, itemStackItem.getIndex(), gameEventEmitter));
							}
						});
						itemStackItem.add(new AjaxLink<Void>("unloadLink") {

							@Override
							protected void onConfigure() {
								super.onConfigure();
								setVisible(isPlayerShipInventory && itemLoadingSpaceStationModel.getObject() != null);
							}

							@Override
							public void onClick(AjaxRequestTarget target) {
								SpaceStation spaceStation = itemLoadingSpaceStationModel.getObject();
								if (spaceStation == null) {
									return;
								}
								Player player = getPlayer();
								ImmutableItemStack itemsToLoad = new ImmutableItemStack(itemStackItem.getModelObject().getItemType(), itemStackItem.getModelObject().getSize());
								ActionQueue actionQueue = player.getActionQueue();
								actionQueue.cancelCurrentAction();
								actionQueue.cancelAllPendingActions();
								GameEventEmitter gameEventEmitter = MyWicketApplication.get().getDependency(GameEventEmitter.class);
								actionQueue.scheduleAction(new LoadUnloadAction(player, spaceStation, LoadUnloadAction.Type.UNLOAD, itemsToLoad, itemStackItem.getIndex(), gameEventEmitter));
							}

						});
						itemStackItem.add(new AjaxLink<Void>("equipLink") {

							@Override
							protected void onConfigure() {
								super.onConfigure();
								setVisible(isPlayerShipInventory && itemStackItem.getModelObject().getItemType().getPlayerShipEquipmentSlotType() != null);
							}

							@Override
							public void onClick(AjaxRequestTarget target) {
								Player player = getPlayer();
								ActionQueue actionQueue = player.getActionQueue();
								// TODO should add this action as the next one but keep other actions
								actionQueue.cancelCurrentAction();
								actionQueue.cancelAllPendingActions();
								actionQueue.scheduleAction(new EquipAction(player, itemStackItem.getModelObject().getItemType()));
							}

						});
						itemStackItem.add(new AjaxLink<Void>("transferOwnershipLink") {
							@Override
							public void onClick(AjaxRequestTarget target) {
								// TODO do not allow to transfer ownership of items in the player's ship!
								GuiNavigationUtil.setPanel(this, id -> {
									return new TransferOwnershipPlayerListPanel(id, inventoryId, itemStackItem.getModelObject());
								}, target);
							}
						}.setVisible(!inventoryEntryItem.getModelObject().isPlayerExclusive()));

						boolean foundBuyOrder = false;
						long foundUnitPrice = 0;
						int foundQuantity = 0;
						ItemType currentItemType = itemStackItem.getModelObject().getItemType();
						String serializedCurrentItemType = MyWicketApplication.get().getDependency(JacksonService.class).serialize(currentItemType);
						for (MarketOrder buyOrder : buyOrders) {
							if (!buyOrder.getSerializedItemType().equals(serializedCurrentItemType)) {
								continue;
							}
							if (!foundBuyOrder || buyOrder.getUnitPrice() > foundUnitPrice) {
								foundUnitPrice = buyOrder.getUnitPrice();
								foundQuantity = buyOrder.getQuantity() == null ? Integer.MAX_VALUE : buyOrder.getQuantity();
							} else if (buyOrder.getUnitPrice() == foundUnitPrice) {
								foundQuantity += buyOrder.getQuantity();
							} else {
								continue;
							}
							foundBuyOrder = true;
							if (foundQuantity >= itemStackItem.getModelObject().getSize()) {
								foundQuantity = itemStackItem.getModelObject().getSize();
								break;
							}
						}
						int finalFoundQuantity = foundQuantity;
						long finalFoundUnitPrice = foundUnitPrice;
						AjaxLink sellLink = new AjaxLink<Void>("sellLink") {
							@Override
							public void onClick(AjaxRequestTarget target) {
								try {
									MyWicketApplication.get().getDependency(MarketOrderFactory.class).createMarketOrder(
										getPlayer(), location, MarketOrderType.SELL, currentItemType, finalFoundQuantity, finalFoundUnitPrice);
								} catch (GameLogicException e) {
									throw new UnexpectedExceptionException(e);
								}
							}
						};
						sellLink.add(new Label("quantity", foundQuantity));
						sellLink.add(new Label("unitPrice", foundUnitPrice));
						sellLink.setVisible(!inventoryEntryItem.getModelObject().isPlayerExclusive() && foundBuyOrder);
						itemStackItem.add(sellLink);
					}
				});
			}
		});
		IModel<List<SlotInfo>> equipmentSlotListModel = new LoadableDetachableModel<List<SlotInfo>>() {
			@Override
			protected List<SlotInfo> load() {
				EntityProvider entityProvider = MyWicketApplication.get().getDependency(EntityProvider.class);
				PlayerShipEquipment equipment = entityProvider.getPlayerShipEquipment(getPlayer().getShip().getId());
				return equipment.getAllSlots();
			}
		};
		add(new ListView<SlotInfo>("equipmentSlots", equipmentSlotListModel) {
			@Override
			protected void populateItem(ListItem<SlotInfo> equipmentSlotItem) {
				equipmentSlotItem.add(new Label("slotType", equipmentSlotItem.getModelObject().getPlayerShipEquipmentSlotType().name().toLowerCase()));
				equipmentSlotItem.add(new Label("itemType", "" + equipmentSlotItem.getModelObject().getItemType()));
				equipmentSlotItem.add(new Image("icon", ItemIcons.get(equipmentSlotItem.getModelObject().getItemType())));
				equipmentSlotItem.add(new AjaxLink<Void>("unequipLink") {
					@Override
					public void onClick(AjaxRequestTarget target) {
						Player player = getPlayer();
						ActionQueue actionQueue = player.getActionQueue();
						// TODO should add this action as the next one but keep other actions
						actionQueue.cancelCurrentAction();
						actionQueue.cancelAllPendingActions();
						actionQueue.scheduleAction(new UnequipAction(player, equipmentSlotItem.getModelObject().getPlayerShipEquipmentSlotType()));
					}
				});
			}
		});

	}

	@Override
	public void receiveGameEventBatch(IPartialPageRequestHandler partialPageRequestHandler, GameEventBatch eventBatch) {
		for (GameEvent e : eventBatch.getEvents()) {
			if (e instanceof InventoryChangedEvent || e instanceof PlayerShipEquipmentChangedEvent) {
				// TODO filter by inventories that actually contain items owned by the player / equipped by the player
				partialPageRequestHandler.add(this);
				return;
			}
		}
	}

	@Override
	protected void detachModel() {
		super.detachModel();
		itemLoadingSpaceStationModel.detach();
	}

}
