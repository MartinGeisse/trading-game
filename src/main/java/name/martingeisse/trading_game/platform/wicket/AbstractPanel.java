package name.martingeisse.trading_game.platform.wicket;

import name.martingeisse.trading_game.game.definition.GameDefinition;
import name.martingeisse.trading_game.game.player.Player;
import name.martingeisse.trading_game.game.space.Space;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.AbstractReadOnlyModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.PropertyModel;

/**
 *
 */
public class AbstractPanel extends Panel {

	public AbstractPanel(String id) {
		super(id);
	}

	public AbstractPanel(String id, IModel<?> model) {
		super(id, model);
	}

	public final Space getSpace() {
		return MyWicketApplication.get().getDependency(Space.class);
	}

	public final GameDefinition getGameDefinition() {
		return MyWicketApplication.get().getDependency(GameDefinition.class);
	}

	public final IModel<GameDefinition> gameDefinitionModel() {
		return new AbstractReadOnlyModel<GameDefinition>() {
			@Override
			public GameDefinition getObject() {
				return getGameDefinition();
			}
		};
	}

	public final <T> IModel<T> gameDefinitionModel(String propertyPath) {
		return new PropertyModel<T>(gameDefinitionModel(), propertyPath);
	}

	public final Player getPlayer() {
		return MyWicketSession.get().getPlayer();
	}

}
