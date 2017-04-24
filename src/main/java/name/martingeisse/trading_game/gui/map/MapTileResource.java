package name.martingeisse.trading_game.gui.map;

import com.google.common.collect.ImmutableList;
import name.martingeisse.trading_game.game.space.*;
import name.martingeisse.trading_game.platform.wicket.MyWicketApplication;
import org.apache.commons.lang3.tuple.Pair;
import org.apache.wicket.WicketRuntimeException;
import org.apache.wicket.request.resource.DynamicImageResource;
import org.apache.wicket.util.time.Duration;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.awt.image.WritableRaster;
import java.io.IOException;

/**
 *
 */
public class MapTileResource extends DynamicImageResource {

	private static final boolean DRAW_GRID = false;

	private static final ThreadLocal<Pair<BufferedImage, Graphics2D>> imageAndGraphicsPerThread = new ThreadLocal<>();

	@Override
	protected void configureResponse(ResourceResponse response, Attributes attributes) {
		response.setCacheDuration(Duration.NONE);
	}

	@Override
	protected byte[] getImageData(Attributes attributes) {

		// evaluate querystring parameters
		int x = attributes.getParameters().get("x").toInt(0);
		int y = attributes.getParameters().get("y").toInt(0);
		int z = attributes.getParameters().get("z").toInt(0);

		// prepare drawing resources
		BufferedImage image;
		Graphics2D graphics;
		{
			Pair<BufferedImage, Graphics2D> pair = imageAndGraphicsPerThread.get();
			if (pair == null) {
				image = new BufferedImage(256, 256, BufferedImage.TYPE_INT_RGB);
				graphics = image.createGraphics();
				imageAndGraphicsPerThread.set(Pair.of(image, graphics));
			} else {
				image = pair.getLeft();
				graphics = pair.getRight();
			}
		}

		// draw the image
		if (z <= MapCoordinates.HEAT_MAP_ZOOM_THRESHOLD) {
			renderHeatMapTile(x, y, z, image);
		} else {
			renderDetailTile(x, y, z, graphics);
		}

		// serialize the image to the output file format
		byte[] result = toImageData(image);

		return result;
	}

	private void renderHeatMapTile(int tileX, int tileY, int zoomLevel, BufferedImage image) {
		WritableRaster raster = image.getRaster();

		// fill background
		for (int x = 0; x < 256; x++) {
			for (int y = 0; y < 256; y++) {
				raster.setSample(x, y, 0, 0);
				raster.setSample(x, y, 1, 0);
				raster.setSample(x, y, 2, 0);
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
		// ImmutableList<StaticSpaceObject> spaceObjects = MyWicketApplication.get().getDependency(Space.class).getStaticSpaceObjects();
		ImmutableList<StaticSpaceObject> spaceObjects = getRelevantStaticSpaceObjects(tileX, tileY, zoomLevel);
		for (SpaceObject spaceObject : spaceObjects) {
			// long lx = spaceObject.getX() - (tileX << shift); // TODO see above
			// long ly = spaceObject.getY() - (tileY << shift); // TODO see above

			// double dx = (spaceObject.getX() << zoomLevel) / 1000 - (tileX << 8);
			// double dy = (spaceObject.getY() << zoomLevel) / 1000 - (tileY << 8);

			double zoomFactor = (1L << zoomLevel);
			double dx = MapCoordinates.convertXToLongitude(spaceObject.getX()) * zoomFactor - (tileX << 8);
			double dy = MapCoordinates.convertYToLatitude(spaceObject.getY()) * zoomFactor - (tileY << 8);

			if (dx >= 0 && dx < 256 && dy >= 0 && dy < 256) {
				int x = (int) dx;
				int y = (int) dy;
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
		ImmutableList<StaticSpaceObject> spaceObjects = getRelevantStaticSpaceObjects(x, y, z);
		g.setFont(g.getFont().deriveFont((float) (MapCoordinates.convertGameDistanceToMapDistance(5000))));
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
			g.drawString("?", (int) x - 5, (int) y - 5);
		}
	}

	private static void drawCircle(Graphics2D g, double centerX, double centerY, double radius) {
		double diameter = 2 * radius;
		g.fill(new Ellipse2D.Double(centerX - radius, centerY - radius, diameter, diameter));
	}

	private static void drawBox(Graphics2D g, double centerX, double centerY, double radius) {
		double diameter = 2 * radius;
		g.fill(new Rectangle2D.Double(centerX - radius, centerY - radius, diameter, diameter));
	}

	private static ImmutableList<StaticSpaceObject> getRelevantStaticSpaceObjects(int x, int y, int z) {
		double zoomFactor = Math.pow(2.0, 8 - z);
		long x1 = MapCoordinates.convertLongitudeToX(x * zoomFactor);
		long y1 = MapCoordinates.convertLatitudeToY(y * zoomFactor);
		long x2 = MapCoordinates.convertLongitudeToX((x + 1) * zoomFactor);
		long y2 = MapCoordinates.convertLatitudeToY((y + 1) * zoomFactor);
		long minX = Math.min(x1, x2);
		long maxX = Math.max(x1, x2);
		long minY = Math.min(y1, y2);
		long maxY = Math.max(y1, y2);
		return MyWicketApplication.get().getDependency(Space.class).getStaticSpaceObjects(minX, minY, maxX, maxY);
	}

	@Override
	protected byte[] toImageData(BufferedImage image) {
		try {
			ByteArrayImageOutputStream imageOutputStream = new ByteArrayImageOutputStream(1024);
			ImageIO.write(image, getFormat(), imageOutputStream);
			imageOutputStream.close();
			return imageOutputStream.toByteArray();
		} catch (IOException e) {
			throw new WicketRuntimeException("Unable to convert dynamic image to stream", e);
		}
	}

}
