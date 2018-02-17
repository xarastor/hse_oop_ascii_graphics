package com.hse;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;

import static com.hse.ExceptionHandler.PrintException;

/**
 * Created by titaninus on 06.02.18.
 */
public class ImageWriter {

    static String ascii_palette = "   ...',;:clodxkO0KXNWM";
    private boolean ToConsole = false;
    private boolean isFilled = false;
    private boolean isSpaced = false;
    private boolean isColored = false;
    private boolean isDoubled = true;
    private  static final String ANSI_RESET = "\u001B[0m";
    private  static final int ANSI_BLACK = 38;
    private  static final int ANSI_RED = 31;
    private  static final int ANSI_GREEN = 32;
    private  static final int ANSI_YELLOW = 33;
    private  static final int ANSI_BLUE = 34;
    private  static final int ANSI_PURPLE = 35;
    private  static final int ANSI_CYAN = 36;
    private  static final int ANSI_GREY = 37;
    private  static final int ANSI_WHITE = 30;

    public ImageWriter(boolean ToCon, boolean isFill, boolean isSpace, boolean isColor, boolean isDouble) {
        ToConsole = ToCon;
        isFilled = isFill;
        isSpaced = isSpace;
        isColored = isColor;
        isDoubled = isDouble;
    }

    private boolean PrintImageToConsole(Image image) {
        if (!image.isImageReady) {
            PrintException("Image not ready ", "Image");
            return false;
        }
        for (int j = 0; j < image.height; ++j) {
            for (int i = 0; i < image.width; ++i) {
                RGB current = image.data.get(i).get(j);
                int index = (int)(current.grey * (ascii_palette.length() - 1) / 255f);
                System.out.print(ascii_palette.charAt(index));
                System.out.print(ascii_palette.charAt(index));
            }
            System.out.print('\n');
        }
        return true;
    }

    private boolean PrintImageToFile(String path, Image image) {
        if (!image.isImageReady) {
            PrintException("Image not ready ", "Image");
            return false;
        }
        PrintStream writer = null;

        try {
            writer = new PrintStream(new FileOutputStream(path));
            PrintImageToFile(writer, image);
            try {writer.close();} catch (Exception ex_) {/*ignore*/}
        } catch (IOException ex) {
            PrintException("Couldn't write into file " + path, "Image");
            try {writer.close();} catch (Exception ex_) {/*ignore*/}
            return false;
        }

        return true;
    }

    private boolean PrintImageToFile(PrintStream writer, Image image) {
        if (!image.isImageReady) {
            PrintException("Image not ready ", "Image");
            return false;
        }

        for (int j = 0; j < image.height; ++j) {
            String str = "";
            for (int i = 0; i < image.width; ++i) {
                int x = i;
                int y = j;
                RGB current = image.data.get(j).get(i);
                int index = (int)(current.grey * (ascii_palette.length() - 1) / 255f);
                str = str + ascii_palette.charAt(index) + (isDoubled ? ascii_palette.charAt(index) : "");
            }
            str = str + '\n';
            writer.print(str);
        }

        return true;
    }

    private boolean PrintColoredImageToFile(String path, Image image) {
        if (!image.isImageReady) {
            PrintException("Image not ready ", "Image");
            return false;
        }
        try {
            PrintStream writer = new PrintStream( new FileOutputStream(new File(path)));
            return PrintColoredImageToFile(writer, image);
        } catch (IOException ex) {
            PrintException("Couldn't write into given file ", "Image");
            return false;
        }
    }

    private boolean PrintColoredImageToFile(File F, Image image) {
        if (!image.isImageReady) {
            PrintException("Image not ready ", "Image");
            return false;
        }
        try {
            PrintStream writer = new PrintStream(new PrintStream(
                    new FileOutputStream(F)));
            PrintColoredImageToFile(writer, image);
        } catch (IOException ex) {
            PrintException("Couldn't write into given file ", "Image");
            return false;
        }

        return true;
    }

    private boolean PrintColoredImageToFile(PrintStream F, Image image) {
        if (!image.isImageReady) {
            PrintException("Image not ready ", "Image");
            return false;
        }

        for (int j = 0; j < image.height; ++j) {
            String str = "";
            for (int i = 0; i < image.width; ++i) {
                int x = i;
                int y = j;
                RGB current = image.data.get(j).get(i);
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

    public boolean PrintImage(String Output, Image image) {
        if (ToConsole) {
            if (isColored)
                PrintColoredImageToFile(System.out, image);
            else
                PrintImageToFile(System.out, image);
        } else {
            if (isColored)
                PrintColoredImageToFile(Output, image);
            else
                PrintImageToFile(Output, image);
        }
        return true;
    }

}
