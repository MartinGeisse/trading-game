package name.martingeisse.trading_game.gui.inventory;

import name.martingeisse.trading_game.game.event.GameEvent;
import name.martingeisse.trading_game.game.event.GameEventBatch;
import name.martingeisse.trading_game.game.item.ImmutableItemStack;
import name.martingeisse.trading_game.game.item.InventoryChangedEvent;
import name.martingeisse.trading_game.game.item.InventoryNameService;
import name.martingeisse.trading_game.game.item.PlayerBelongingsService;
import name.martingeisse.trading_game.gui.gamepage.MainMenuTabbedPanel;
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

import java.util.Collections;
import java.util.List;

/**
 * Encapsulates the actual map view and the properties box.
 */
public class InventorySectionPanel extends AbstractPanel implements GuiGameEventListener {

	public InventorySectionPanel(String id) {
		super(id);
		setOutputMarkupId(true);
		IModel<List<PlayerBelongingsService.InventoryEntry>> model = new LoadableDetachableModel<List<PlayerBelongingsService.InventoryEntry>>() {
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
		add(new ListView<PlayerBelongingsService.InventoryEntry>("inventories", model) {
			@Override
			protected void populateItem(ListItem<PlayerBelongingsService.InventoryEntry> item) {
				long inventoryId = item.getModelObject().getInventoryId();
				String title = MyWicketApplication.get().getDependency(InventoryNameService.class).getNameForInventoryId(inventoryId);
				item.add(new Label("inventoryTitle", title));
				item.add(new ListView<ImmutableItemStack>("itemStacks", item.getModelObject().getItemStacks().getStacks()) {
					@Override
					protected void populateItem(ListItem<ImmutableItemStack> item) {
						item.add(new Label("size", item.getModelObject().getSize()));
						item.add(new Label("itemType", item.getModelObject().getItemType().getName()));
						item.add(new Image("icon", ItemIcons.get(item.getModelObject().getItemType())));
						item.add(new AjaxLink<Void>("transferOwnershipLink") {
							@Override
							public void onClick(AjaxRequestTarget target) {
								// TODO do not allow to transfer ownership of items in the player's ship!
								MainMenuTabbedPanel.replaceTabPanel(this, id -> {
									return new TransferOwnershipPlayerListPanel(id, inventoryId, item.getModelObject());
								}, target);
							}
						});
					}
				});
			}
		});
	}

	@Override
	public void receiveGameEventBatch(IPartialPageRequestHandler partialPageRequestHandler, GameEventBatch eventBatch) {
		for (GameEvent e : eventBatch.getEvents()) {
			if (e instanceof InventoryChangedEvent) {
				// TODO filter by inventories that actually contain items owned by the player
				partialPageRequestHandler.add(this);
				return;
			}
		}
	}

}
