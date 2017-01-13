
import org.apache.wicket.request.resource.DynamicImageResource;

import java.awt.*;
import java.awt.image.BufferedImage;

/**
 *
 */
public class ExperimentPlugin extends Plugin {

	@Override
	protected void configure() {
		extend(WicketInitParticipant.class, application -> {
			application.mountPage("/ol", OpenLayersPage.class);
			application.getSharedResources().add("MapTile", new MapTileResource());
			application.getSharedResources().add("ComputedTile", new ComputedTileResource());
		});
	}

}
