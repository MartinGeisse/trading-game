package name.martingeisse.trading_game.gui.inventory;

import com.google.common.collect.ImmutableList;
import name.martingeisse.trading_game.game.action.Action;
import name.martingeisse.trading_game.game.action.ActionQueue;
import name.martingeisse.trading_game.game.event.GameEvent;
import name.martingeisse.trading_game.game.event.GameEventBatch;
import name.martingeisse.trading_game.game.item.*;
import name.martingeisse.trading_game.game.space.*;
import name.martingeisse.trading_game.gui.gamepage.MainMenuTabbedPanel;
import name.martingeisse.trading_game.gui.item.ItemIcons;
import name.martingeisse.trading_game.gui.map.MapCoordinates;
import name.martingeisse.trading_game.gui.map.leaflet.D3;
import name.martingeisse.trading_game.gui.map.leaflet.Leaflet;
import name.martingeisse.trading_game.gui.map.leaflet.LeafletD3SvgOverlay;
import name.martingeisse.trading_game.gui.websockets.GuiGameEventListener;
import name.martingeisse.trading_game.platform.postgres.PostgresConnection;
import name.martingeisse.trading_game.platform.postgres.PostgresService;
import name.martingeisse.trading_game.platform.wicket.AbstractPanel;
import name.martingeisse.trading_game.platform.wicket.MyWicketApplication;
import name.martingeisse.trading_game.postgres_entities.QSpaceObjectBaseDataRow;
import org.apache.wicket.Component;
import org.apache.wicket.ajax.AbstractDefaultAjaxBehavior;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.attributes.CallbackParameter;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.core.request.handler.IPartialPageRequestHandler;
import org.apache.wicket.markup.ComponentTag;
import org.apache.wicket.markup.head.IHeaderResponse;
import org.apache.wicket.markup.head.JavaScriptHeaderItem;
import org.apache.wicket.markup.head.OnDomReadyHeaderItem;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.image.Image;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.LoadableDetachableModel;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.request.IRequestParameters;
import org.apache.wicket.request.Url;
import org.apache.wicket.request.resource.ResourceReference;
import org.apache.wicket.request.resource.SharedResourceReference;

import java.util.Collections;
import java.util.Comparator;
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

				// TODO this should be cleaner
				String title;
				try (PostgresConnection connection = MyWicketApplication.get().getDependency(PostgresService.class).newConnection()) {
					QSpaceObjectBaseDataRow qr = QSpaceObjectBaseDataRow.SpaceObjectBaseData;
					title = connection.query().select(qr.name).from(qr).where(qr.inventoryId.eq(inventoryId)).fetchFirst();
				}

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
								MainMenuTabbedPanel.replaceTabPanel(this, id -> {
									return new TransferOwnershipPlayerListPanel(id, item.getModelObject());
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
