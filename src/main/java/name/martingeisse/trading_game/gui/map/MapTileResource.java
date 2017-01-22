package name.martingeisse.trading_game.gui.map;

import com.google.common.collect.ImmutableList;
import name.martingeisse.trading_game.game.Game;
import name.martingeisse.trading_game.game.space.SpaceObject;
import name.martingeisse.trading_game.gui.wicket.MyWicketApplication;
import org.apache.wicket.request.resource.DynamicImageResource;
import org.apache.wicket.util.time.Duration;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.Random;

/**
 *
 */
public class MapTileResource extends DynamicImageResource {

	@Override
	protected void configureResponse(ResourceResponse response, Attributes attributes) {
		response.setCacheDuration(Duration.NONE);
	}

	@Override
	protected byte[] getImageData(Attributes attributes) {
		int x = attributes.getParameters().get("x").toInt(0);
		int y = attributes.getParameters().get("y").toInt(0);
		int z = attributes.getParameters().get("z").toInt(0);
		BufferedImage image = new BufferedImage(256, 256, BufferedImage.TYPE_INT_RGB);
		Graphics2D g = (Graphics2D)image.createGraphics();
		try {
			renderTile(x, y, z, g);
		} finally {
			g.dispose();
		}
		return toImageData(image);
	}

	private void renderTile(int x, int y, int z, Graphics2D g) {

		// fill background
		g.setColor(Color.BLACK);
		g.fillRect(0, 0, 256, 256);

		// draw tile grid
		g.setColor(Color.GRAY);
		g.drawRect(0, 0, 256, 256);
		g.drawString("" + x + ", " + y + ", " + z, 5, 15);

		// setup tile coordinates
		g.translate(-(double)(x << 8), -(double)(y << 8)); // translate to render the correct tile
		g.scale(1 << z, 1 << z); // apply zoom
		g.setColor(Color.RED);

		// scale to sidestep the fact that drawOval takes integer coordinates
		g.scale(0.01, 0.01);

		// draw spce objects
		g.setFont(g.getFont().deriveFont(30.0f));
		ImmutableList<SpaceObject> spaceObjects = MyWicketApplication.get().getDependency(Game.class).getSpace().getSpaceObjects();
		for (SpaceObject spaceObject : spaceObjects) {
			drawCircle(g, (int)spaceObject.getX(), (int)spaceObject.getY(), 30); // TODO int/long
			g.drawString("" + spaceObject.getX() + ", " + spaceObject.getY(), (int)spaceObject.getX() + 35, (int)spaceObject.getY());
		}

	}

	private static void drawCircle(Graphics2D g, int centerX, int centerY, int radius) {
		int diameter = radius << 1;
		g.drawOval(centerX - radius, centerY - radius, diameter, diameter);
	}

}
