package name.martingeisse.trading_game.gui.market;

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
import name.martingeisse.trading_game.gui.inventory.TransferOwnershipPlayerListPanel;
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
import org.apache.wicket.model.PropertyModel;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Encapsulates the actual map view and the properties box.
 */
public class MarketSectionPanel extends AbstractPanel implements GuiGameEventListener {

	public MarketSectionPanel(String id) {
		super(id);
		setOutputMarkupId(true);
		add(new MarketOrderListView("sellOrders", new MarketOrderListModel(MarketOrderType.SELL)));
		add(new MarketOrderListView("buyOrders", new MarketOrderListModel(MarketOrderType.BUY)));
	}

	@Override
	public void receiveGameEventBatch(IPartialPageRequestHandler partialPageRequestHandler, GameEventBatch eventBatch) {
		// TODO
	}

	public class MarketOrderListModel extends LoadableDetachableModel<List<MarketOrder>> {

		private final MarketOrderType type;

		public MarketOrderListModel(MarketOrderType type) {
			this.type = type;
		}

		@Override
		protected List<MarketOrder> load() {
			return MyWicketApplication.get().getDependency(MarketOrderRepository.class).getMarketOrders(type);
		}

	}

	public static class MarketOrderListView extends ListView<MarketOrder> {

		public MarketOrderListView(String id, IModel<? extends List<MarketOrder>> model) {
			super(id, model);
		}

		@Override
		protected void populateItem(ListItem<MarketOrder> item) {
			MarketOrder marketOrder = item.getModelObject();
			item.add(new Label("quantity", new PropertyModel<>(marketOrder, "quantity")));
			item.add(new Label("itemType", new PropertyModel<>(marketOrder, "itemType")));
			item.add(new Label("unitPrice", new PropertyModel<>(marketOrder, "unitPrice")));
			item.add(new Label("location", new PropertyModel<>(marketOrder, "location")));
			item.add(new Label("principal", new PropertyModel<>(marketOrder, "principal")));
		}

	}

}
