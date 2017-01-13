
import org.apache.wicket.request.resource.DynamicImageResource;
import org.apache.wicket.util.time.Duration;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;

/**
 *
 */
public class ComputedTileResource extends DynamicImageResource {

	public static final int ITERATIONS = 100;

	public static final int DIVERGENCE_INDEX_DIVISOR = 10;

	public static final int BOUNDED_COLOR = 0xff000000;

	public static final int[] DIVERGENCE_COLORS = {
		0xffffffff,
		0xffffff00,
		0xffff0000,
		0xff0000ff,
	};

	public static final int RENDERING_TILE_SIZE = 1024;

	public static final int OUTPUT_TILE_SIZE = 256;

	@Override
	protected void configureResponse(ResourceResponse response, Attributes attributes) {
		response.setCacheDuration(Duration.NONE);
	}

	@Override
	protected byte[] getImageData(Attributes attributes) {
		int x = attributes.getParameters().get("x").toInt(0);
		int y = attributes.getParameters().get("y").toInt(0);
		int z = attributes.getParameters().get("z").toInt(0);
		BufferedImage image = new BufferedImage(RENDERING_TILE_SIZE, RENDERING_TILE_SIZE, BufferedImage.TYPE_INT_RGB);
		render(x, y, z, image);
		if (OUTPUT_TILE_SIZE != RENDERING_TILE_SIZE) {
			BufferedImage oldImage = image;
			image = new BufferedImage(OUTPUT_TILE_SIZE, OUTPUT_TILE_SIZE, BufferedImage.TYPE_INT_RGB);
			Graphics2D g = image.createGraphics();
			try {
				g.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
				g.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
				g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
				double factor = ((double)OUTPUT_TILE_SIZE) / ((double)RENDERING_TILE_SIZE);
				g.drawRenderedImage(oldImage, AffineTransform.getScaleInstance(factor, factor));
			} finally {
				g.dispose();
			}
		}
		return toImageData(image);
	}

	private void render(int tileX, int tileY, int zoom, BufferedImage image) {
		double scale = 4.0 * Math.pow(2.0, -zoom);
		double r0 = scale * tileX - 2.0;
		double i0 = scale * tileY - 2.0;
		double delta = scale / RENDERING_TILE_SIZE;
		for (int x = 0; x < RENDERING_TILE_SIZE; x++) {
			double r = r0 + x * delta;
			for (int y = 0; y < RENDERING_TILE_SIZE; y++) {
				int divergence = compute(r, i0 + y * delta);
				int color;
				if (divergence == 0) {
					color = BOUNDED_COLOR;
				} else {
					int index = divergence * DIVERGENCE_COLORS.length / DIVERGENCE_INDEX_DIVISOR;
					if (index >= DIVERGENCE_COLORS.length) {
						index = DIVERGENCE_COLORS.length - 1;
					}
					color = DIVERGENCE_COLORS[index];
				}
				image.setRGB(x, y, color);
			}
		}
	}

	private int compute(double cr, double ci) {
		int divergence = ITERATIONS;
		double r = 0, i = 0;
		while (divergence > 0) {
			double r2 = r * r;
			double i2 = i * i;
			double ri = r * i;
			if (r2 + i2 > 4.0) {
				// diverged
				break;
			}
			r = r2 - i2 + cr;
			i = 2 * ri + ci;
			divergence--;
		}
		return divergence;
	}

}
