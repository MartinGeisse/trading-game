package name.martingeisse.trading_game.gui.manufacturing;

import name.martingeisse.trading_game.game.action.actions.ManufacturingAction;
import name.martingeisse.trading_game.game.definition.GameDefinition;
import name.martingeisse.trading_game.game.item.ImmutableItemStack;
import name.martingeisse.trading_game.game.manufacturing.Blueprint;
import name.martingeisse.trading_game.platform.wicket.AbstractPanel;
import name.martingeisse.trading_game.platform.wicket.MyWicketApplication;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.model.AbstractReadOnlyModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.PropertyModel;

import java.util.List;

/**
 *
 */
public class ManufacturingSectionPanel extends AbstractPanel {

	public ManufacturingSectionPanel(String id) {
		super(id);
		setOutputMarkupId(true);
		IModel<List<Blueprint>> blueprintsModel = new AbstractReadOnlyModel<List<Blueprint>>() {
			@Override
			public List<Blueprint> getObject() {
				return MyWicketApplication.get().getDependency(GameDefinition.class).getAllBlueprints();
			}
		};
		add(new ListView<Blueprint>("blueprints", blueprintsModel) {
			@Override
			protected void populateItem(ListItem<Blueprint> blueprintItem) {
				IModel<Blueprint> blueprintModel = blueprintItem.getModel();
				IModel<Integer> totalTimeModel = new AbstractReadOnlyModel<Integer>() {
					@Override
					public Integer getObject() {
						return new ManufacturingAction(getPlayer(), blueprintModel.getObject()).getTotalTime();
					}
				};
				blueprintItem.add(new Label("name", new PropertyModel<>(blueprintModel, "name")));
				blueprintItem.add(new Label("totalTime", totalTimeModel));
				blueprintItem.add(new ListView<ImmutableItemStack>("billOfMaterials", new PropertyModel<>(blueprintModel, "billOfMaterials.stacks")) {
					@Override
					protected void populateItem(ListItem<ImmutableItemStack> stackItem) {
						stackItem.add(new Label("quantity", new PropertyModel<>(stackItem.getModel(), "size")));
						stackItem.add(new Label("itemType", new PropertyModel<>(stackItem.getModel(), "itemType")));
					}
				});
				blueprintItem.add(new ListView<ImmutableItemStack>("yield", new PropertyModel<>(blueprintModel, "yield.stacks")) {
					@Override
					protected void populateItem(ListItem<ImmutableItemStack> stackItem) {
						stackItem.add(new Label("quantity", new PropertyModel<>(stackItem.getModel(), "size")));
						stackItem.add(new Label("itemType", new PropertyModel<>(stackItem.getModel(), "itemType")));
					}
				});
			}
		});
	}

}
