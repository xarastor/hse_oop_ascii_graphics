package com.hse;

import java.awt.geom.AffineTransform;
import java.util.ArrayList;

/**
 * Created by titaninus on 17.02.18.
 */
public class ImageAsyncTransformerWorker
implements Runnable {
    float[] source;
    float[] destination;
    AffineTransform transform;
    public ImageAsyncTransformerWorker(AffineTransform tx, float[] src) {
        source = src;
        transform = tx;
    }

    public void run() {
        Transform(transform);
    }

    private void Transform(AffineTransform tx) {

        ArrayList<ArrayList<RGB>> _data = new ArrayList<>();
        destination = new float[source.length];
        tx.transform(source, 0, destination, 0, source.length / 2);
    }

}
