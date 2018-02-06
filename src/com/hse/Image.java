package com.hse;

import javax.imageio.ImageIO;
import javax.print.DocFlavor;
import java.awt.*;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.io.*;
import java.awt.geom.AffineTransform;
import java.awt.geom.*;
import java.util.ArrayList;

import static com.hse.ExceptionHandler.PrintException;
import static com.hse.Options.*;

class RGB {
    int red;
    int green;
    int blue;
    int grey;
}



public class Image {
    public static final String ANSI_RESET = "\u001B[0m";
    public static final int ANSI_BLACK = 38;
    public static final int ANSI_RED = 31;
    public static final int ANSI_GREEN = 32;
    public static final int ANSI_YELLOW = 33;
    public static final int ANSI_BLUE = 34;
    public static final int ANSI_PURPLE = 35;
    public static final int ANSI_CYAN = 36;
    public static final int ANSI_GREY = 37;
    public static final int ANSI_WHITE = 30;

    ArrayList<ArrayList<RGB>> data;
    int width;
    int height;
    private boolean isImageReady = false;

    public boolean ReadFromDisk(String path) {
        try {
            BufferedImage _Image;
            _Image = ImageIO.read(new File(path));
            width = _Image.getWidth();
            height = _Image.getHeight();
            data = new ArrayList<>();
            for (int j = 0; j < height; ++j) {
                ArrayList<RGB> sub = new ArrayList<RGB>();
                for (int i = 0; i < width; ++i) {
                    sub.add(GetFormattedPixel(_Image, i, j));
                }
                data.add(sub);
            }
            isImageReady = true;
            return true;
        }
        catch (IOException e) {
            PrintException("Couldn't read the file " + path, "Image");
            isImageReady = false;
            return false;
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

    public boolean PrintImageToConsole() {
        if (!isImageReady) {
            PrintException("Image not ready ", "Image");
            return false;
        }
        for (int j = 0; j < height; ++j) {
            for (int i = 0; i < width; ++i) {
                RGB current = data.get(i).get(j);
                int index = (int)(current.grey * (ascii_palette.length() - 1) / 255f);
                System.out.print(ascii_palette.charAt(index));
                System.out.print(ascii_palette.charAt(index));
            }
            System.out.print('\n');
        }
        return true;
    }

    public boolean PrintImageToFile(String path) {
        if (!isImageReady) {
            PrintException("Image not ready ", "Image");
            return false;
        }
        PrintStream writer = null;

        try {
            writer = new PrintStream(new FileOutputStream(path));
            PrintImageToFile(writer);
            try {writer.close();} catch (Exception ex_) {/*ignore*/}
        } catch (IOException ex) {
            PrintException("Couldn't write into file " + path, "Image");
            try {writer.close();} catch (Exception ex_) {/*ignore*/}
            return false;
        }

        return true;
    }

    public boolean PrintImageToFile(PrintStream writer) {
        if (!isImageReady) {
            PrintException("Image not ready ", "Image");
            return false;
        }

            for (int j = 0; j < height; ++j) {
                String str = "";
                for (int i = 0; i < width; ++i) {
                    int x = i;
                    int y = j;
                    RGB current = data.get(j).get(i);
                    int index = (int)(current.grey * (ascii_palette.length() - 1) / 255f);
                    str = str + ascii_palette.charAt(index) + (isDoubled ? ascii_palette.charAt(index) : "");
                }
                str = str + '\n';
                writer.print(str);
            }

        return true;
    }

    public boolean PrintColoredImageToFile(String path) {
        if (!isImageReady) {
            PrintException("Image not ready ", "Image");
            return false;
        }
        try {
            PrintStream writer = new PrintStream( new FileOutputStream(new File(path)));
            return PrintColoredImageToFile(writer);
        } catch (IOException ex) {
            PrintException("Couldn't write into given file ", "Image");
            return false;
        }
    }

    public boolean PrintColoredImageToFile(File F, boolean fill, boolean isSpace, boolean isDoubled) {
        if (!isImageReady) {
            PrintException("Image not ready ", "Image");
            return false;
        }
        try {
            PrintStream writer = new PrintStream(new PrintStream(
                new FileOutputStream(F)));
            PrintColoredImageToFile(writer);
        } catch (IOException ex) {
            PrintException("Couldn't write into given file ", "Image");
            return false;
        }

        return true;
    }

    public boolean PrintColoredImageToFile(PrintStream F) {
        if (!isImageReady) {
            PrintException("Image not ready ", "Image");
            return false;
        }

            for (int j = 0; j < height; ++j) {
                String str = "";
                for (int i = 0; i < width; ++i) {
                    int x = i;
                    int y = j;
                    RGB current = data.get(j).get(i);
                    int index = (int)(current.grey * (ascii_palette.length() - 1) / 255f);

                    float t = 0.01f * 255; // threshold
                    int colr = ANSI_BLACK;
                    int R = current.red;
                    int G = current.green;
                    int B = current.blue;
                    int Y = current.grey;
                    char ch = ascii_palette.charAt(index);
                    if ( R-t>G && R-t>B )                 colr = ANSI_RED; // red
                    else if (R + G + B < t)               colr = ANSI_BLACK;
                    else if ( G-t>R && G-t>B )            colr = ANSI_GREEN; // green
                    else if ( R-t>B && G-t>B && R+G>i )   colr = ANSI_YELLOW; // yellow
                    else if ( B-t>R && B-t>G )            colr = ANSI_BLUE; // blue
                    else if ( R-t>G && B-t>G && R+B>i )   colr = ANSI_PURPLE; // magenta
                    else if ( G-t>R && B-t>R && B+G>i )   colr = ANSI_CYAN; // cyan
                    else if ( R+G+B>=3.0f*Y )             colr = ANSI_WHITE; // white
                    //else              colr = ANSI_WHITE; // white
                    if (isSpaced)
                        ch = ' ';
                    if (isFilled)
                        colr += 10;
                    String formatted = String.format("\u001B[%dm", colr);
                    str = str + formatted + ch + (isDoubled ? ch : "") + ANSI_RESET;
                }
                //str = str + '\n';
                F.println(str);
            }



        return true;
    }

    private void rotateImage(double angle) {
        double angle_ = Math.toRadians(angle);
        AffineTransform tx = new AffineTransform();
        tx.rotate(angle_, (width - 1) / 2, (height - 1) / 2);
        Transform(tx);
    }

    public int round (float x, float b) {
        if (x < 0)
            x = 0;
        if (x > b)
            x = b;
        return (int) x;
    }

    private void Transform(AffineTransform tx) {

        ArrayList<ArrayList<RGB>> _data = new ArrayList<>();
        float[] src = new float[width * height * 2];
        float[] dst = new float[width * height * 2];
        for (int j = 0; j < height; ++j) {
            for (int i = 0; i < width; ++i) {
                src[(width * j + i) * 2] = i;
                src[(width * j + i) * 2 + 1] = j;
            }
        }
        tx.transform(src, 0, dst, 0, width * height);
        for (int j = 0; j < height; ++j) {
            ArrayList<RGB> sub = new ArrayList<>();
            for (int i = 0; i < width; ++i) {
                sub.add(data.get(round(dst[(width * j + i) * 2 + 1], height - 1))
                        .get(round(dst[(width * j + i) * 2], width - 1)));
            }
            _data.add(sub);
        }
        data = _data;
    }

    public boolean ConvertToAscii(String path) {
        if (!ReadFromDisk(path)) {
            PrintException("Applications Crashed", "Image");
            return false;
        }
        AffineTransform tx;
        AffineTransformOp op;
        if (isFlippedVertical) {
            tx = AffineTransform.getScaleInstance(1, -1);
            tx.translate(0, -(height - 1));
            Transform(tx);
        }


// Flip the image horizontally
        if (isFlippedHorisontal) {
            tx = AffineTransform.getScaleInstance(-1, 1);
            tx.translate(-(width - 1), 0);
            Transform(tx);
        }

        if (isRotating) {
            rotateImage(RotAngle);
        }

        return true;
    }


}
