package name.martingeisse.trading_game.gui.account;

import name.martingeisse.trading_game.common.util.RemainingTimeFormatter;
import name.martingeisse.trading_game.peripherals.payment.folding_at_home.FoldingAtHomeApiService;
import name.martingeisse.trading_game.peripherals.payment.folding_at_home.FoldingAtHomeConstants;
import name.martingeisse.trading_game.peripherals.payment.folding_at_home.TeamScore;
import name.martingeisse.trading_game.platform.wicket.AbstractPanel;
import name.martingeisse.trading_game.platform.wicket.MyWicketApplication;
import name.martingeisse.wicket.helpers.AbstractLongLoadingContainer;
import name.martingeisse.wicket.helpers.InlineLongLoadingContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.model.AbstractReadOnlyModel;
import org.apache.wicket.model.PropertyModel;

/**
 *
 */
public class AccountPanel extends AbstractPanel {

	public AccountPanel(String id) {
		super(id);
		add(new Label("remainingPlaytime", new AbstractReadOnlyModel<String>() {
			@Override
			public String getObject() {
				return RemainingTimeFormatter.format(getPlayer().getRemainingPlayTime());
			}
		}));
		add(new Label("spentFoldingCredits", new PropertyModel<>(getPlayer(), "spentFoldingCredits")));
		add(new Label("foldingUsername", new AbstractReadOnlyModel<String>() {
			@Override
			public String getObject() {
				return FoldingAtHomeConstants.NAME_PREFIX + getPlayer().getFoldingUserHash();
			}
		}));
		AbstractLongLoadingContainer gameTotalContainer = new InlineLongLoadingContainer("gameTotalContainer");
		gameTotalContainer.add(new Label("gameTotal", new AbstractReadOnlyModel<String>() {
			@Override
			public String getObject() {
				TeamScore teamScore = MyWicketApplication.get().getDependency(FoldingAtHomeApiService.class).getTeamScore();
				return teamScore.getCredits() + " credits in " + teamScore.getWorkUnits() + " WUs (rank " + teamScore.getRank() + ")";
			}
		}));
		add(gameTotalContainer);
	}

}
