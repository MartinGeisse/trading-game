package name.martingeisse.trading_game.gui.map;

import name.martingeisse.trading_game.game.action.Action;
import name.martingeisse.trading_game.game.action.ActionQueue;
import name.martingeisse.trading_game.game.space.GeometryUtil;
import name.martingeisse.trading_game.game.space.SpaceObject;
import name.martingeisse.trading_game.platform.wicket.AbstractPanel;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.PropertyModel;

import java.util.List;

/**
 *
 */
public class SpaceObjectDetailPanel extends AbstractPanel {

	public SpaceObjectDetailPanel(String id, IModel<SpaceObject> model) {
		super(id, model);
		add(new Label("name", new PropertyModel<>(model, "name")));
		add(new Label("type", new PropertyModel<>(model, "class.simpleName")));
		add(new Label("x", new PropertyModel<>(model, "x")));
		add(new Label("y", new PropertyModel<>(model, "y")));
		add(new Label("distance", new PropertyModel<>(this, "distance")));
		add(new ListView<Action>("actions", new PropertyModel<>(this, "actions")) {
			@Override
			protected void populateItem(ListItem<Action> actionItem) {
				AjaxLink<?> link = new AjaxLink<Void>("link") {
					@Override
					public void onClick(AjaxRequestTarget target) {
						ActionQueue actionQueue = getPlayer().getActionQueue();
						actionQueue.cancelCurrentAction();
						actionQueue.cancelAllPendingActions();
						actionQueue.scheduleAction(actionItem.getModelObject());
					}
				};
				link.add(new Label("name", actionItem.getModelObject().getName()));
				actionItem.add(link);
			}

		});
	}

	public IModel<SpaceObject> getModel() {
		return (IModel<SpaceObject>) getDefaultModel();
	}

	public SpaceObject getSpaceObject() {
		return (SpaceObject) getDefaultModelObject();
	}

	public Double getDistance() {
		return GeometryUtil.getDistance(getPlayer().getShip(), getSpaceObject());
	}

	public List<Action> getActions() {
		return getSpaceObject().getActionsFor(getPlayer());
	}

}
