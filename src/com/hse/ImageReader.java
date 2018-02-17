package com.hse;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import static com.hse.ExceptionHandler.PrintException;
/**
 * Created by titaninus on 06.02.18.
 */
public class ImageReader {

    private static float redweight = 0.2989f;
    private static  float greenweight = 0.5866f;
    private static float blueweight = 0.1145f;

    private boolean isFlippedHorisontal = false;
    private boolean isFlippedVertical = false;
    private boolean isRotating = false;
    private boolean isConcurrent = false;
    private float RotAngle = 0;

    public ImageReader(boolean isFlippedH, boolean isFlippedV, boolean isRot, boolean isConc, float Rotation) {
        RotAngle = Rotation;
        isFlippedHorisontal = isFlippedH;
        isFlippedVertical = isFlippedV;
        isConcurrent = isConc;
        isRotating = isRot;
    }

    private Image ReadFromDisk(String path) {
        try {
            Image Readed = new Image();
            BufferedImage RawImage;
            RawImage = ImageIO.read(new File(path));
            Readed.width = RawImage.getWidth();
            Readed.height = RawImage.getHeight();
            Readed.data = new ArrayList<>();
            for (int j = 0; j < Readed.height; ++j) {
                ArrayList<RGB> sub = new ArrayList<RGB>();
                for (int i = 0; i < Readed.width; ++i) {
                    sub.add(GetFormattedPixel(RawImage, i, j));
                }
                Readed.data.add(sub);
            }
            Readed.isImageReady = true;
            return Readed;
        }
        catch (IOException e) {
            PrintException("Couldn't read the file " + path, "Image");
            return null;
        }
    }

    private RGB GetFormattedPixel(BufferedImage _Image, int x, int y) {
        int rgb = _Image.getRGB(x, y);
        RGB pixel = new RGB();
        pixel.red = (rgb >> 16) & 0xFF;
        pixel.green = (rgb >> 8) & 0xFF;
        pixel.blue = rgb & 0xFF;
        pixel.grey = (int)(pixel.blue * blueweight + pixel.green * greenweight + pixel.red * redweight);
        return pixel;
    }

    public Image ConvertToAscii(String path) {
        Image Readed = ReadFromDisk(path);
        if (Readed == null) {
            PrintException("Applications Crashed", "Image");
            return null;
        }
        if (isConcurrent) {
            ImageAsyncTransformerManager manager = new ImageAsyncTransformerManager(4);
            if (isFlippedVertical) {
                manager.flipVertical(Readed);
            }
            if (isFlippedHorisontal) {
                manager.flipHorizontal(Readed);
            }
            if (isRotating) {
                manager.rotateImage(RotAngle, Readed);
            }
        } else {
            if (isFlippedVertical) {
                ImageTransformer.flipVertical(Readed);
            }
            if (isFlippedHorisontal) {
                ImageTransformer.flipHorizontal(Readed);
            }
            if (isRotating) {
                ImageTransformer.rotateImage(RotAngle, Readed);
            }
        }
        return Readed;
    }



}
