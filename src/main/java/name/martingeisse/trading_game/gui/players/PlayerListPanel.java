package name.martingeisse.trading_game.gui.players;

import name.martingeisse.trading_game.game.GameLogicException;
import name.martingeisse.trading_game.game.player.Player;
import name.martingeisse.trading_game.game.player.PlayerRepository;
import name.martingeisse.trading_game.gui.util.PromptAjaxLink;
import name.martingeisse.trading_game.platform.util.JavascriptAssemblerUtil;
import name.martingeisse.trading_game.platform.wicket.AbstractPanel;
import name.martingeisse.trading_game.platform.wicket.MyWicketApplication;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.model.LoadableDetachableModel;
import org.apache.wicket.model.PropertyModel;

import java.util.List;

/**
 * Shows the list of players.
 */
public class PlayerListPanel extends AbstractPanel {

	public PlayerListPanel(String id) {
		super(id);
		LoadableDetachableModel<List<Player>> playerListModel = new LoadableDetachableModel<List<Player>>() {
			@Override
			protected List<Player> load() {
				return MyWicketApplication.get().getDependency(PlayerRepository.class).getAllPlayers();
			}
		};
		add(new ListView<Player>("players", playerListModel) {
			@Override
			protected void populateItem(ListItem<Player> item) {
				item.add(new Label("name", new PropertyModel<>(item.getModel(), "name")));
				item.add(new PromptAjaxLink("transferMoneyLink") {

					@Override
					protected String getPrompt() {
						return "Transfer how much?";
					}

					@Override
					protected void onClick(AjaxRequestTarget target, String promptInput) {

						// parse amount
						int amount;
						if (promptInput == null) {
							return;
						}
						promptInput = promptInput.trim();
						if (promptInput.isEmpty()) {
							return;
						}
						try {
							amount = Integer.parseInt(promptInput);
						} catch (NumberFormatException e) {
							target.appendJavaScript("alert('Invalid input');");
							return;
						}
						if (amount < 0) {
							target.appendJavaScript("alert('input is negative');");
							return;
						}
						if (amount == 0) {
							return;
						}

						// transfer money
						Player self = getPlayer();
						Player otherPlayer = item.getModelObject();
						try {
							self.transferMoneyTo(otherPlayer, amount);
						} catch (GameLogicException e) {
							target.appendJavaScript("alert('" + JavascriptAssemblerUtil.escapeStringLiteralSpecialCharacters(e.getMessage()) + "');");
						}

					}

				});
			}
		});
	}

}
