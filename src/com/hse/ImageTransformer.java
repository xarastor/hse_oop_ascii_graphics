package com.hse;

import java.awt.geom.AffineTransform;
import java.util.ArrayList;

/**
 * Created by titaninus on 06.02.18.
 */
public class ImageTransformer {


    public static void rotateImage(double angle, Image image) {
        double angle_ = Math.toRadians(angle);
        AffineTransform tx = new AffineTransform();
        tx.rotate(angle_, (image.width - 1) / 2, (image.height - 1) / 2);
        Transform(tx, image);
    }

    public static void flipHorizontal(Image image) {
        AffineTransform tx = AffineTransform.getScaleInstance(-1, 1);
        tx.translate(-(image.width - 1), 0);
        Transform(tx, image);
    }

    public static void flipVertical(Image image) {
        AffineTransform tx = AffineTransform.getScaleInstance(1, -1);
        tx.translate(0, -(image.height - 1));
        Transform(tx, image);
    }

    private static int round (float x, float b) {
        if (x < 0)
            x = 0;
        if (x > b)
            x = b;
        return (int) x;
    }

    private static void Transform(AffineTransform tx, Image image) {

        ArrayList<ArrayList<RGB>> _data = new ArrayList<>();
        float[] src = new float[image.width * image.height * 2];
        float[] dst = new float[image.width * image.height * 2];
        for (int j = 0; j < image.height; ++j) {
            for (int i = 0; i < image.width; ++i) {
                src[(image.width * j + i) * 2] = i;
                src[(image.width * j + i) * 2 + 1] = j;
            }
        }
        tx.transform(src, 0, dst, 0, image.width * image.height);
        for (int j = 0; j < image.height; ++j) {
            ArrayList<RGB> sub = new ArrayList<>();
            for (int i = 0; i < image.width; ++i) {
                int x = round(dst[(image.width * j + i) * 2], image.width - 1);
                int y = round(dst[(image.width * j + i) * 2 + 1], image.height - 1);
                sub.add(image.data.get(y).get(x));
            }
            _data.add(sub);
        }
        image.data = _data;
    }

}
