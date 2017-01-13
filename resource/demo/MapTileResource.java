
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
			g.setColor(Color.BLACK);
			g.fillRect(0, 0, 256, 256);
			g.translate(-(double)(x << 8), -(double)(y << 8));
			g.scale(1 << z, 1 << z);
			g.setColor(Color.RED);

			// scale to sidestep the fact that drawOval takes integer coordinates
			g.scale(0.01, 0.01);
			Random random = new Random(12345);
			for (int i=0; i<1000; i++) {
				int r = random.nextInt(100) + 5;
				g.drawOval(random.nextInt(25600), random.nextInt(25600), r, r);
			}

			// g.setFont(g.getFont().deriveFont(30.0f));
			// g.drawString("" + attributes.getParameters().get("x") + ", " + attributes.getParameters().get("y") + ", " + attributes.getParameters().get("z"), 30, 80);
		} finally {
			g.dispose();
		}
		return toImageData(image);
	}

}
