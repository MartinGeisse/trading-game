package name.martingeisse.trading_game.gui.map;

import com.google.common.collect.ImmutableList;
import name.martingeisse.trading_game.game.Game;
import name.martingeisse.trading_game.game.generate.SpectrumNoise;
import name.martingeisse.trading_game.game.space.Asteroid;
import name.martingeisse.trading_game.game.space.Planet;
import name.martingeisse.trading_game.game.space.SpaceObject;
import name.martingeisse.trading_game.game.space.SpaceStation;
import name.martingeisse.trading_game.game.space.*;
import name.martingeisse.trading_game.platform.wicket.MyWicketApplication;
import org.apache.wicket.request.resource.DynamicImageResource;
import org.apache.wicket.util.time.Duration;

import java.awt.*;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.awt.image.WritableRaster;

/**
 *
 */
public class MapTileResource extends DynamicImageResource {

	private static final boolean DRAW_GRID = false;

	private final SpectrumNoise noise = new SpectrumNoise(4, 2.0);

	@Override
	protected void configureResponse(ResourceResponse response, Attributes attributes) {
		response.setCacheDuration(Duration.NONE);
	}

	@Override
	protected byte[] getImageData(Attributes attributes) {
		int x = attributes.getParameters().get("x").toInt(0);
		int y = attributes.getParameters().get("y").toInt(0);
		int z = attributes.getParameters().get("z").toInt(0);
		if (z <= MapCoordinates.HEAT_MAP_ZOOM_THRESHOLD) {
			BufferedImage image = new BufferedImage(256, 256, BufferedImage.TYPE_BYTE_GRAY);
			renderHeatMapTile(x, y, z, image);
			// renderNoiseMapTile(x, y, z, image);
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

	private void renderNoiseMapTile(int tileX, int tileY, int zoomLevel, BufferedImage image) {
		double zoomFactor = Math.pow(0.5, zoomLevel);
		WritableRaster raster = image.getRaster();
		for (int x=0; x<256; x++) {
			for (int y=0; y<256; y++) {
				double x2 = zoomFactor * (x + 256 * tileX) / 256.0;
				double y2 = zoomFactor * (y + 256 * tileY) / 256.0;
				double value = noise.get(x2, y2);
				raster.setSample(x, y, 0, 128 + (int)(value * 128));
			}
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

		// draw tile grid
		if (DRAW_GRID) {
			Graphics2D g = image.createGraphics();
			try {
				g.setColor(Color.DARK_GRAY);
				g.drawRect(0, 0, 256, 256);
			} finally {
				g.dispose();
			}
		}

		//
		// note: the sequence .scale(a) .translate(b) .drawAt(c) results in the coordinate (c + b) * a, so .scale()
		// also scales the translation amount for a subsequent .translate()
		//

		// add space objects to the heat map
		int shift = zoomLevel + 8; // TODO wrong, should be (8 - shiftLevel), but hits the same problem: pixels != latLng != gameCoords
									// -- must be solved first
		ImmutableList<StaticSpaceObject> spaceObjects = MyWicketApplication.get().getDependency(Game.class).getSpace().getStaticSpaceObjects();
		for (SpaceObject spaceObject : spaceObjects) {
			// long lx = spaceObject.getX() - (tileX << shift); // TODO see above
			// long ly = spaceObject.getY() - (tileY << shift); // TODO see above

			// double dx = (spaceObject.getX() << zoomLevel) / 1000 - (tileX << 8);
			// double dy = (spaceObject.getY() << zoomLevel) / 1000 - (tileY << 8);

			double zoomFactor = (1L << zoomLevel);
			double dx = MapCoordinates.convertXToLongitude(spaceObject.getX()) * zoomFactor - (tileX << 8);
			double dy = MapCoordinates.convertYToLatitude(spaceObject.getY()) * zoomFactor - (tileY << 8);

			if (dx >= 0 && dx < 256 && dy >= 0 && dy < 256) {
				int x = (int)dx;
				int y = (int)dy;
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
		if (DRAW_GRID) {
			g.setColor(Color.DARK_GRAY);
			g.drawRect(0, 0, 256, 256);
			g.drawString("" + x + ", " + y + ", " + z, 5, 15);
		}

		// setup tile coordinates
		g.translate(-(double) (x << 8), -(double) (y << 8)); // translate to render the correct tile
		g.scale(1 << z, 1 << z); // apply zoom

		// draw space objects
		g.setFont(g.getFont().deriveFont((float)(MapCoordinates.convertGameDistanceToMapDistance(5000))));
		ImmutableList<StaticSpaceObject> spaceObjects = MyWicketApplication.get().getDependency(Game.class).getSpace().getStaticSpaceObjects();
		for (SpaceObject spaceObject : spaceObjects) {
			draw(spaceObject, g);
		}

	}

	private static void draw(SpaceObject spaceObject, Graphics2D g) {
		double x = MapCoordinates.convertXToLongitude(spaceObject.getX());
		double y = MapCoordinates.convertYToLatitude(spaceObject.getY());
		// shows coordinates:
		// g.drawString("" + spaceObject.getX() + ", " + spaceObject.getY(), (float)(x + MapCoordinates.convertGameDistanceToMapDistance(5000)), (float)y);
		if (spaceObject instanceof Asteroid) {
			g.setColor(Color.GRAY);
			drawCircle(g, x, y, MapCoordinates.convertGameDistanceToMapDistance(2000));
		} else if (spaceObject instanceof Planet) {
			g.setColor(Color.GRAY);
			drawCircle(g, x, y, MapCoordinates.convertGameDistanceToMapDistance(5000));
		} else if (spaceObject instanceof SpaceStation) {
			g.setColor(Color.BLUE);
			drawBox(g, x, y, MapCoordinates.convertGameDistanceToMapDistance(500));
		} else {
			g.setColor(Color.RED);
			g.drawString("?", (int)x - 5, (int)y - 5);
		}
		// drawCircle(g, (int)spaceObject.getX(), (int)spaceObject.getY(), 30); // TODO int/long
	}

	private static void drawCircle(Graphics2D g, double centerX, double centerY, double radius) {
		double diameter = 2 * radius;
		g.fill(new Ellipse2D.Double(centerX - radius, centerY - radius, diameter, diameter));
	}

	private static void drawBox(Graphics2D g, double centerX, double centerY, double radius) {
		double diameter = 2 * radius;
		g.fill(new Rectangle2D.Double(centerX - radius, centerY - radius, diameter, diameter));
	}

}
