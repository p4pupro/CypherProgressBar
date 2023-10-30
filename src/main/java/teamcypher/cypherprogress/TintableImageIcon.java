package teamcypher.cypherprogress;

import com.intellij.util.ui.UIUtil;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.awt.image.Raster;
import java.awt.image.WritableRaster;

public class TintableImageIcon extends ImageIcon {
    public final transient BufferedImage tintedImage;
    private final Color tintColor;
    private final Graphics2D cg2d;

    public TintableImageIcon(Image resource, int tintColor) {
        super(resource);

        this.tintedImage = UIUtil.createImage(null, this.getIconWidth(), this.getIconHeight(), BufferedImage.TYPE_INT_ARGB);
        this.cg2d = tintedImage.createGraphics();
        this.cg2d.setBackground(new Color(0x00_FFFFFF, true));
        this.tintColor = new Color(tintColor);
    }

    @Override
    public synchronized void paintIcon(Component c, Graphics g, int x, int y) {
        paintIcon(c, g, x, y, false);
    }

    public synchronized void paintIcon(Component c, Graphics g, int x, int y, boolean flipped) {
        // Clear graphics and get the image
        cg2d.clearRect(0, 0, this.getIconWidth(), this.getIconHeight());

        cg2d.setComposite(AlphaComposite.SrcOver);
        if (flipped)
            cg2d.drawImage(this.getImage(), this.getIconWidth(), 0, -this.getIconWidth(), this.getIconHeight(), c);
        else
            cg2d.drawImage(this.getImage(), 0, 0, c);

        cg2d.setComposite(BlendMulComposite.getInstance());
        cg2d.setColor(tintColor);
        cg2d.fillRect(0, 0, this.getIconWidth(), this.getIconHeight());

        if (this.getImageObserver() == null) {
            g.drawImage(tintedImage, x, y, c);
        } else {
            g.drawImage(tintedImage, x, y, this.getImageObserver());
        }
    }

}

class BlendMulComposite implements Composite {
    private static final BlendMulComposite INSTANCE = new BlendMulComposite();
    private static final CompositeContext CONTEXT = new BlendMulCompositeContext();

    public static BlendMulComposite getInstance() {
        return INSTANCE;
    }

    @Override
    public CompositeContext createContext(ColorModel srcColorModel, ColorModel dstColorModel, RenderingHints hints) {
        return CONTEXT;
    }

    // Referenced from org.jdesktop.swingx.graphics.BlendComposite.BlendingRgbContext
    private static class BlendMulCompositeContext implements CompositeContext {
        @Override
        public void dispose() {
        }

        @Override
        public void compose(Raster src, Raster dstIn, WritableRaster dstOut) {
            int width = Math.min(src.getWidth(), dstIn.getWidth());
            int height = Math.min(src.getHeight(), dstIn.getHeight());
            int[] result = new int[3];
            int[] srcPixel = new int[3];
            int[] dstPixel = new int[3];
            int[] srcPixels = new int[width];
            int[] dstPixels = new int[width];

            for (int y = 0; y < height; ++y) {
                src.getDataElements(0, y, width, 1, srcPixels);
                dstIn.getDataElements(0, y, width, 1, dstPixels);

                for (int x = 0; x < width; ++x) {
                    int pixel = srcPixels[x];
                    srcPixel[0] = pixel & 255;
                    srcPixel[1] = pixel >> 8 & 255;
                    srcPixel[2] = pixel >> 16 & 255;
                    pixel = dstPixels[x];
                    dstPixel[0] = pixel & 255;
                    dstPixel[1] = pixel >> 8 & 255;
                    dstPixel[2] = pixel >> 16 & 255;

                    result[0] = srcPixel[0] * dstPixel[0] >> 8;
                    result[1] = srcPixel[1] * dstPixel[1] >> 8;
                    result[2] = srcPixel[2] * dstPixel[2] >> 8;

                    dstPixels[x] = (dstPixels[x] & 0xFF_000000)
                            | (int) ((float) dstPixel[0] + (float) (result[0] - dstPixel[0])) & 255
                            | ((int) ((float) dstPixel[1] + (float) (result[1] - dstPixel[1])) & 255) << 8
                            | ((int) ((float) dstPixel[2] + (float) (result[2] - dstPixel[2])) & 255) << 16;
                }

                dstOut.setDataElements(0, y, width, 1, dstPixels);
            }
        }
    }
}
