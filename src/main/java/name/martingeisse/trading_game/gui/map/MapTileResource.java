package name.martingeisse.trading_game.gui.map;

import com.google.common.collect.ImmutableList;
import name.martingeisse.trading_game.game.Game;
import name.martingeisse.trading_game.game.space.Asteroid;
import name.martingeisse.trading_game.game.space.Planet;
import name.martingeisse.trading_game.game.space.SpaceObject;
import name.martingeisse.trading_game.game.space.SpaceStation;
import name.martingeisse.trading_game.gui.wicket.MyWicketApplication;
import org.apache.wicket.request.resource.DynamicImageResource;
import org.apache.wicket.util.time.Duration;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.WritableRaster;

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
		if (z < 5) {
			BufferedImage image = new BufferedImage(256, 256, BufferedImage.TYPE_BYTE_GRAY);
			renderHeatMapTile(x, y, z, image);
			return toImageData(image);
		} else {
			BufferedImage image = new BufferedImage(256, 256, BufferedImage.TYPE_INT_RGB);
			Graphics2D g = image.createGraphics();
			try {
				renderDetailTile(x, y, z, g);
			} finally {
				g.dispose();
			}
			return toImageData(image);
		}
	}

	private void renderHeatMapTile(int tileX, int tileY, int zoomLevel, BufferedImage image) {
		WritableRaster raster = image.getRaster();

		// fill background
		for (int x=0; x<256; x++) {
			for (int y=0; y<256; y++) {
				raster.setSample(x, y, 0, 0);
			}
		}

		// draw tile grid TODO remove
		Graphics2D g = image.createGraphics();
		try {
			g.setColor(Color.DARK_GRAY);
			g.drawRect(0, 0, 256, 256);
		} finally {
			g.dispose();
		}

		//
		// note: the sequence .scale(a) .translate(b) .drawAt(c) results in the coordinate (c + b) * a, so .scale()
		// also scales the translation amount for a subsequent .translate()
		//

		// add space objects to the heat map
		int shift = zoomLevel + 8; // TODO wrong, should be (8 - shiftLevel), but hits the same problem: pixels != latLng != gameCoords
									// -- must be solved first
		ImmutableList<SpaceObject> spaceObjects = MyWicketApplication.get().getDependency(Game.class).getSpace().getSpaceObjects();
		for (SpaceObject spaceObject : spaceObjects) {
			// long lx = spaceObject.getX() - (tileX << shift); // TODO see above
			// long ly = spaceObject.getY() - (tileY << shift); // TODO see above
			long lx = (spaceObject.getX() << zoomLevel) / 1000 - (tileX << 8);
			long ly = (spaceObject.getY() << zoomLevel) / 1000 - (tileY << 8);
			if (lx >= 0 && lx < 256 && ly >= 0 && ly < 256) {
				int x = (int)lx;
				int y = (int)ly;
				int value = raster.getSample(x, y, 0);
				raster.setSample(x, y, 0, value == 255 ? value : (value + 85));
			}
		}

	}

	private void renderDetailTile(int x, int y, int z, Graphics2D g) {

		// fill background
		g.setColor(Color.BLACK);
		g.fillRect(0, 0, 256, 256);

		// draw tile grid
		g.setColor(Color.DARK_GRAY);
		g.drawRect(0, 0, 256, 256);
		g.drawString("" + x + ", " + y + ", " + z, 5, 15);

		// setup tile coordinates
		g.translate(-(double) (x << 8), -(double) (y << 8)); // translate to render the correct tile
		g.scale(1 << z, 1 << z); // apply zoom

		// TODO remove
		g.scale(0.001, 0.001);

		// draw space objects
		g.setFont(g.getFont().deriveFont(30.0f));
		ImmutableList<SpaceObject> spaceObjects = MyWicketApplication.get().getDependency(Game.class).getSpace().getSpaceObjects();
		for (SpaceObject spaceObject : spaceObjects) {
			draw(spaceObject, g);
			// shows coordinates:
			g.drawString("" + spaceObject.getX() + ", " + spaceObject.getY(), (int) spaceObject.getX() + 35, (int) spaceObject.getY());
		}

	}

	private static void draw(SpaceObject spaceObject, Graphics2D g) {
		int x = (int) spaceObject.getX();
		int y = (int) spaceObject.getY();
		if (spaceObject instanceof Asteroid) {
			g.setColor(Color.GRAY);
			drawCircle(g, x, y, 20);
		} else if (spaceObject instanceof Planet) {
			g.setColor(Color.GRAY);
			drawCircle(g, x, y, 50);
		} else if (spaceObject instanceof SpaceStation) {
			g.setColor(Color.BLUE);
			drawBox(g, x, y, 5);
		} else {
			g.setColor(Color.RED);
			g.drawString("?", x - 5, y - 5);
		}
		// drawCircle(g, (int)spaceObject.getX(), (int)spaceObject.getY(), 30); // TODO int/long
	}

	private static void drawCircle(Graphics2D g, int centerX, int centerY, int radius) {
		int diameter = radius << 1;
		g.fillOval(centerX - radius, centerY - radius, diameter, diameter);
	}

	private static void drawBox(Graphics2D g, int centerX, int centerY, int radius) {
		int diameter = radius << 1;
		g.fillRect(centerX - radius, centerY - radius, diameter, diameter);
	}

}
