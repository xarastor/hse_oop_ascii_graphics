package com.hse;

import java.awt.geom.AffineTransform;
import java.util.ArrayList;
import java.util.Arrays;

/**
 * Created by titaninus on 17.02.18.
 */
public class ImageAsyncTransformerManager {

    ArrayList<ImageAsyncTransformerWorker> workers;
    ArrayList<Thread> threads;
    int workersCount = 1;
    public ImageAsyncTransformerManager(int workerCount) {
        workersCount = workerCount;
    }

    public void rotateImage(double angle, Image image) {
        double angle_ = Math.toRadians(angle);
        AffineTransform tx = new AffineTransform();
        tx.rotate(angle_, (image.width - 1) / 2, (image.height - 1) / 2);
        Transform(tx, image);
    }

    public void flipHorizontal(Image image) {
        AffineTransform tx = AffineTransform.getScaleInstance(-1, 1);
        tx.translate(-(image.width - 1), 0);
        Transform(tx, image);
    }

    public void flipVertical(Image image) {
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

    public void Transform(AffineTransform tx, Image im) {
        int slice = (im.height * im.width * 2) / workersCount;
        workers = new ArrayList<>();
        threads = new ArrayList<>();
        ArrayList<ArrayList<RGB>> _data = new ArrayList<>();
        float[] src = new float[im.width * im.height * 2];
        float[] dst = new float[im.width * im.height * 2];
        for (int j = 0; j < im.height; ++j) {
            for (int i = 0; i < im.width; ++i) {
                src[(im.width * j + i) * 2] = i;
                src[(im.width * j + i) * 2 + 1] = j;
            }
        }
        for (int i = 0; i < workersCount; ++i) {
            int from = i * slice;
            int to = Math.min((i + 1) * slice, im.height * im.width * 2);
            ImageAsyncTransformerWorker worker = new ImageAsyncTransformerWorker(tx, Arrays.copyOfRange(src, from, to));
            workers.add(worker);
            Thread thread = new Thread(worker);
            threads.add(thread);
            thread.start();
        }
        for (int i = 0; i < workersCount; ++i) {
            try {
                threads.get(i).join();
            } catch (InterruptedException e) {
            }
        }
        for (int i = 0; i < workersCount; ++i) {
            int from = i * slice;
            int to = Math.min((i + 1) * slice, im.height * im.width * 2);
            System.arraycopy(workers.get(i).destination, 0, dst, from, to - from);
        }
        for (int j = 0; j < im.height; ++j) {
            ArrayList<RGB> sub = new ArrayList<>();
            for (int i = 0; i < im.width; ++i) {
                int x = round(dst[(im.width * j + i) * 2], im.width - 1);
                int y = round(dst[(im.width * j + i) * 2 + 1], im.height - 1);
                sub.add(im.data.get(y).get(x));
            }
            _data.add(sub);
        }
        im.data = _data;
    }

}
