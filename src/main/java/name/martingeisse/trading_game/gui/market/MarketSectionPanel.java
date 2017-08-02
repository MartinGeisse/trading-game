package name.martingeisse.trading_game.gui.market;

import name.martingeisse.trading_game.common.util.UnexpectedExceptionException;
import name.martingeisse.trading_game.game.GameLogicException;
import name.martingeisse.trading_game.game.event.GameEventBatch;
import name.martingeisse.trading_game.game.market.MarketOrder;
import name.martingeisse.trading_game.game.market.MarketOrderFactory;
import name.martingeisse.trading_game.game.market.MarketOrderRepository;
import name.martingeisse.trading_game.game.market.MarketOrderType;
import name.martingeisse.trading_game.game.player.Player;
import name.martingeisse.trading_game.gui.gamepage.GuiNavigationLink;
import name.martingeisse.trading_game.gui.inventory.InventorySectionPanel;
import name.martingeisse.trading_game.gui.websockets.GuiGameEventListener;
import name.martingeisse.trading_game.platform.wicket.AbstractPanel;
import name.martingeisse.trading_game.platform.wicket.MyWicketApplication;
import name.martingeisse.trading_game.platform.wicket.page.AbstractPage;
import org.apache.wicket.core.request.handler.IPartialPageRequestHandler;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.LoadableDetachableModel;
import org.apache.wicket.model.PropertyModel;

import java.util.List;

/**
 * Encapsulates the actual map view and the properties box.
 */
public class MarketSectionPanel extends AbstractPanel implements GuiGameEventListener {

	public MarketSectionPanel(String id) {
		super(id);
		setOutputMarkupId(true);
		add(new ShopSellOrderListView("shopSellOrders", new MarketOrderListModel(MarketOrderType.SELL, true)));
		add(new MarketOrderListView("sellOrders", new MarketOrderListModel(MarketOrderType.SELL, false)));
		add(new MarketOrderListView("buyOrders", new MarketOrderListModel(MarketOrderType.BUY, false)));
		add(new GuiNavigationLink<Void>("inventoryLink") {
			@Override
			protected WebMarkupContainer getPanel(String panelId) {
				return new InventorySectionPanel(panelId);
			}
		});
	}

	@Override
	public void receiveGameEventBatch(IPartialPageRequestHandler partialPageRequestHandler, GameEventBatch eventBatch) {
		// TODO
	}

	public class MarketOrderListModel extends LoadableDetachableModel<List<MarketOrder>> {

		private final MarketOrderType type;
		private final Boolean global;

		public MarketOrderListModel(MarketOrderType type, Boolean global) {
			this.type = type;
			this.global = global;
		}

		@Override
		protected List<MarketOrder> load() {
			return MyWicketApplication.get().getDependency(MarketOrderRepository.class).getMarketOrders(type, global);
		}

	}

	public static class ShopSellOrderListView extends ListView<MarketOrder> {

		public ShopSellOrderListView(String id, IModel<? extends List<MarketOrder>> model) {
			super(id, model);
		}

		@Override
		protected void populateItem(ListItem<MarketOrder> item) {
			MarketOrder marketOrder = item.getModelObject();
			Link<Void> link = new Link<Void>("link") {
				@Override
				public void onClick() {
					MarketOrderFactory marketOrderFactory = MyWicketApplication.get().getDependency(MarketOrderFactory.class);
					Player player = ((AbstractPage)getPage()).getPlayer();
					try {
						marketOrderFactory.createMatchingMarketOrderForGlobal(player, player.getShip(), marketOrder, 1);
					} catch (GameLogicException e) {
						throw new UnexpectedExceptionException(e);
					}
				}
			};
			item.add(link);
			link.add(new Label("itemType", new PropertyModel<>(marketOrder, "itemType")));
			link.add(new Label("unitPrice", new PropertyModel<>(marketOrder, "unitPrice")));
		}

	}

	public static class MarketOrderListView extends ListView<MarketOrder> {

		public MarketOrderListView(String id, IModel<? extends List<MarketOrder>> model) {
			super(id, model);
		}

		@Override
		protected void populateItem(ListItem<MarketOrder> item) {
			MarketOrder marketOrder = item.getModelObject();
			Link<Void> link = new Link<Void>("link") {
				@Override
				public void onClick() {
					MarketOrderFactory marketOrderFactory = MyWicketApplication.get().getDependency(MarketOrderFactory.class);
					Player player = ((AbstractPage)getPage()).getPlayer();
					try {
						marketOrderFactory.createMatchingMarketOrderForLocal(player, marketOrder);
					} catch (GameLogicException e) {
						throw new UnexpectedExceptionException(e);
					}
				}
			};
			item.add(link);
			link.add(new Label("quantity", new PropertyModel<>(marketOrder, "quantity")));
			link.add(new Label("itemType", new PropertyModel<>(marketOrder, "itemType")));
			link.add(new Label("unitPrice", new PropertyModel<>(marketOrder, "unitPrice")));
			link.add(new Label("location", new PropertyModel<>(marketOrder, "location")));
			link.add(new Label("principal", new PropertyModel<>(marketOrder, "principal")));
		}

	}

}
