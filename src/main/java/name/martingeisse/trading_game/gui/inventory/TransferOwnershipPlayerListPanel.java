package name.martingeisse.trading_game.gui.inventory;

import name.martingeisse.trading_game.common.util.UnexpectedExceptionException;
import name.martingeisse.trading_game.game.item.ImmutableItemStack;
import name.martingeisse.trading_game.game.item.Inventory;
import name.martingeisse.trading_game.game.item.InventoryRepository;
import name.martingeisse.trading_game.game.item.NotEnoughItemsException;
import name.martingeisse.trading_game.game.player.Player;
import name.martingeisse.trading_game.game.player.PlayerRepository;
import name.martingeisse.trading_game.gui.gamepage.MainMenuTabbedPanel;
import name.martingeisse.trading_game.gui.gamepage.TabPanelReplacementLink;
import name.martingeisse.trading_game.platform.wicket.AbstractPanel;
import name.martingeisse.trading_game.platform.wicket.MyWicketApplication;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.link.AbstractLink;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.LoadableDetachableModel;
import org.apache.wicket.model.PropertyModel;

import java.util.List;

/**
 *
 */
public class TransferOwnershipPlayerListPanel extends AbstractPanel {

	private final long inventoryId;
	private final ImmutableItemStack items;

	public TransferOwnershipPlayerListPanel(String id, long inventoryId, ImmutableItemStack items) {
		super(id);
		this.inventoryId = inventoryId;
		this.items = items;
		add(new TabPanelReplacementLink<Void>("backLink") {
			@Override
			protected Panel getPanel(String panelId) {
				return new InventorySectionPanel(panelId);
			}
		});
		IModel<List<Player>> playerListModel = new LoadableDetachableModel<List<Player>>() {
			@Override
			protected List<Player> load() {
				return MyWicketApplication.get().getDependency(PlayerRepository.class).getAllPlayers();
			}
		};
		add(new ListView<Player>("players", playerListModel) {
			@Override
			protected void populateItem(ListItem<Player> item) {
				AbstractLink link = new AjaxLink<Void>("link") {
					@Override
					public void onClick(AjaxRequestTarget target) {
						Inventory inventory = MyWicketApplication.get().getDependency(InventoryRepository.class).getInventory(inventoryId);
						try {
							inventory.remove(getPlayer().getId(), items.getItemType(), items.getSize());
						} catch (NotEnoughItemsException e) {
							throw new UnexpectedExceptionException(e);
						}
						inventory.add(item.getModelObject().getId(), items);
						MainMenuTabbedPanel.replaceTabPanel(this, InventorySectionPanel::new, target);
					}
				};
				item.add(link);
				link.add(new Label("name", new PropertyModel<>(item.getModel(), "name")));
			}
		});
	}


}
