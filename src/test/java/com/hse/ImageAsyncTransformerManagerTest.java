package com.hse;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;

public class ImageAsyncTransformerManagerTest extends Assert {
    private Image input;

    @Before
    public void initInput() {
        input = new Image();
        input.width = 2;
        input.height = 2;
        input.data = new ArrayList<>();
        ArrayList<RGB> line1 = new ArrayList<>();
        line1.add(new RGB(1, 0, 0));
        line1.add(new RGB(2, 0, 0));
        ArrayList<RGB> line2 = new ArrayList<>();
        line2.add(new RGB(3, 0, 0));
        line2.add(new RGB(4, 0, 0));
        input.data.add(line1);
        input.data.add(line2);
        input.isImageReady = true;
    }

    @Test
    public void testRotate() {
        Image expected = new Image();
        expected.width = 2;
        expected.height = 2;
        expected.data = new ArrayList<>();
        ArrayList<RGB> expectedLine1 = new ArrayList<>();
        expectedLine1.add(new RGB(3, 0, 0));
        expectedLine1.add(new RGB(1, 0, 0));
        ArrayList<RGB> expectedLine2 = new ArrayList<>();
        expectedLine2.add(new RGB(4, 0, 0));
        expectedLine2.add(new RGB(2, 0, 0));
        expected.data.add(expectedLine1);
        expected.data.add(expectedLine2);
        expected.isImageReady = true;

        ImageAsyncTransformerManager imageAsyncTransformerManager = new ImageAsyncTransformerManager(4);
        imageAsyncTransformerManager.rotateImage(90, input);
        comparePixels(expected, input);
    }

    @Test
    public void testComplexRotate() {
        Image expected = new Image();
        expected.width = 2;
        expected.height = 2;
        expected.data = new ArrayList<>();
        ArrayList<RGB> expectedLine1 = new ArrayList<>();
        expectedLine1.add(new RGB(1, 0, 0));
        expectedLine1.add(new RGB(2, 0, 0));
        ArrayList<RGB> expectedLine2 = new ArrayList<>();
        expectedLine2.add(new RGB(3, 0, 0));
        expectedLine2.add(new RGB(4, 0, 0));
        expected.data.add(expectedLine1);
        expected.data.add(expectedLine2);
        expected.isImageReady = true;

        ImageAsyncTransformerManager imageAsyncTransformerManager = new ImageAsyncTransformerManager(4);
        imageAsyncTransformerManager.rotateImage(270, input);
        imageAsyncTransformerManager.rotateImage(90, input);
        comparePixels(expected, input);
    }

    @Test
    public void testFlipHorizontalWithOneWorker() {
        Image expected = new Image();
        expected.width = 2;
        expected.height = 2;
        expected.data = new ArrayList<>();
        ArrayList<RGB> expectedLine1 = new ArrayList<>();
        expectedLine1.add(new RGB(2, 0, 0));
        expectedLine1.add(new RGB(1, 0, 0));
        ArrayList<RGB> expectedLine2 = new ArrayList<>();
        expectedLine2.add(new RGB(4, 0, 0));
        expectedLine2.add(new RGB(3, 0, 0));
        expected.data.add(expectedLine1);
        expected.data.add(expectedLine2);
        expected.isImageReady = true;

        ImageAsyncTransformerManager imageAsyncTransformerManager = new ImageAsyncTransformerManager(1);
        imageAsyncTransformerManager.flipHorizontal(input);
        comparePixels(expected, input);
    }

    @Test
    public void testFlipHorizontalWithThreWorkers() {
        Image expected = new Image();
        expected.width = 2;
        expected.height = 2;
        expected.data = new ArrayList<>();
        ArrayList<RGB> expectedLine1 = new ArrayList<>();
        expectedLine1.add(new RGB(2, 0, 0));
        expectedLine1.add(new RGB(1, 0, 0));
        ArrayList<RGB> expectedLine2 = new ArrayList<>();
        expectedLine2.add(new RGB(4, 0, 0));
        expectedLine2.add(new RGB(3, 0, 0));
        expected.data.add(expectedLine1);
        expected.data.add(expectedLine2);
        expected.isImageReady = true;

        ImageAsyncTransformerManager imageAsyncTransformerManager = new ImageAsyncTransformerManager(3);
        imageAsyncTransformerManager.flipHorizontal(input);
        comparePixels(expected, input);
    }

    @Test
    public void testFlipVerticalWithOneWorker() {
        Image expected = new Image();
        expected.width = 2;
        expected.height = 2;
        expected.data = new ArrayList<>();
        ArrayList<RGB> expectedLine1 = new ArrayList<>();
        expectedLine1.add(new RGB(3, 0, 0));
        expectedLine1.add(new RGB(4, 0, 0));
        ArrayList<RGB> expectedLine2 = new ArrayList<>();
        expectedLine2.add(new RGB(1, 0, 0));
        expectedLine2.add(new RGB(2, 0, 0));
        expected.data.add(expectedLine1);
        expected.data.add(expectedLine2);
        expected.isImageReady = true;

        ImageAsyncTransformerManager imageAsyncTransformerManager = new ImageAsyncTransformerManager(1);
        imageAsyncTransformerManager.flipVertical(input);
        comparePixels(expected, input);
    }

    @Test
    public void testFlipVerticalWithThreeWorkers() {
        Image expected = new Image();
        expected.width = 2;
        expected.height = 2;
        expected.data = new ArrayList<>();
        ArrayList<RGB> expectedLine1 = new ArrayList<>();
        expectedLine1.add(new RGB(3, 0, 0));
        expectedLine1.add(new RGB(4, 0, 0));
        ArrayList<RGB> expectedLine2 = new ArrayList<>();
        expectedLine2.add(new RGB(1, 0, 0));
        expectedLine2.add(new RGB(2, 0, 0));
        expected.data.add(expectedLine1);
        expected.data.add(expectedLine2);
        expected.isImageReady = true;

        ImageAsyncTransformerManager imageAsyncTransformerManager = new ImageAsyncTransformerManager(3);
        imageAsyncTransformerManager.flipVertical(input);
        comparePixels(expected, input);
    }

    @Test
    public void testFlipVerticalWithNegativeWorkersNumber() {
        Image expected = new Image();
        expected.width = 2;
        expected.height = 2;
        expected.data = new ArrayList<>();
        ArrayList<RGB> expectedLine1 = new ArrayList<>();
        expectedLine1.add(new RGB(3, 0, 0));
        expectedLine1.add(new RGB(4, 0, 0));
        ArrayList<RGB> expectedLine2 = new ArrayList<>();
        expectedLine2.add(new RGB(1, 0, 0));
        expectedLine2.add(new RGB(2, 0, 0));
        expected.data.add(expectedLine1);
        expected.data.add(expectedLine2);
        expected.isImageReady = true;

        ImageAsyncTransformerManager imageAsyncTransformerManager = new ImageAsyncTransformerManager(-3);
        imageAsyncTransformerManager.flipVertical(input);
        comparePixels(expected, input);
    }

    private void comparePixels(Image expected, Image actual) {
        for (int i = 0; i < expected.height; i++) {
            for (int j = 0; j < expected.width; j++) {
                RGB actualPixel = actual.data.get(i).get(j);
                RGB expectedPixel = expected.data.get(i).get(j);
                Assert.assertEquals(expectedPixel.red, actualPixel.red);
                Assert.assertEquals(expectedPixel.blue, actualPixel.blue);
                Assert.assertEquals(expectedPixel.green, actualPixel.green);
            }
        }
    }
}
