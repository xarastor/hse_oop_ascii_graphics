package com.hse;

import com.hse.Image;

import javax.print.DocFlavor;

import static com.hse.ExceptionHandler.PrintException;


public class Main {
    static boolean isColored;
    static boolean isDoubled;
    static boolean ToConsole;
    static boolean isFilled;
    static boolean isFlippedHorisontal;
    static boolean isFlippedVertical;
    static boolean isSpaced;
    static boolean isRotating;
    static boolean isConcurrent;
    static float RotAngle;
    private static String Input;
    private static String Output;

    private static void PrintHelp() {
        System.out.println("Usage: java -jar jpegToAscii.jar <inputFile> [<outputFile>]\n" +
                "[-f, --filled  - fill background]\n" +
                "[-s, --spaced  - all chars turns to space (for colored background mode)]\n" +
                "[-n, --console - all output will be force written in console]\n" +
                "[-c, --colored - allow writing colors in images]\n" +
                "[-h, --help    - show this help]\n" +
                "[-d, --no-double    - off double char mode]\n" +
                "[-fh, --flip-horisontally - flip image horisontally]\n" +
                "[-fv, --flip-vertically - flip image vertically]\n" +
                "[-r <angle>, --rotate <angle> - rotate image by angle\n" +
                "[-t, --threads - use async transformations\n" +
                "if no outputFile specified, output will be written in console\n");
    }

    private static boolean  parseArguments(String[] args) {
        if (args.length < 1) {
            PrintException("Couldn't find path in args", "Main");
            PrintHelp();
            return false;
        }
        Input = args[0];
        for (int i = 1; i < args.length; ++i) {
            if (args[i].contains("-")) {
                switch (args[i]) {
                    case "-f":
                    case "--filled": {
                        isFilled = true;
                        break;
                    }
                    case "-n":
                    case "--console": {
                        ToConsole = true;
                        break;
                    }
                    case "-s":
                    case "--spaced": {
                        isSpaced = true;
                        break;
                    }
                    case "-c":
                    case "--colored": {
                        isColored = true;
                        break;
                    }
                    case "-d":
                    case "--no-double": {
                        isDoubled = false;
                        break;
                    }
                    case "-fh":
                    case "--flip-horisontally": {
                        isFlippedHorisontal = true;
                        break;
                    }
                    case "-fv":
                    case "--flip-vertically": {
                        isFlippedVertical = true;
                        break;
                    }
                    case "-t":
                    case "--threads": {
                        isConcurrent = true;
                        break;
                    }
                    case "-r":
                    case "--rotate": {
                        i++;
                        if (i == args.length) {
                            PrintException("Please specify angle to argument -r/--rotate. Application stopped", "Main");
                            PrintHelp();
                            return false;
                        } else {
                            RotAngle = Float.parseFloat(args[i]);
                            isRotating = true;
                        }
                        break;
                    }
                    case "-h":
                    case "--help": {
                        PrintHelp();
                        break;
                    }
                    default: {
                        PrintException("Unrecognized argument " + args[i] + ". Application stopped", "Main");
                        PrintHelp();
                        return false;
                    }
                }
            } else {
                Output = args[i];
            }
        }
        if (Output == "" || Output == null) {
            ToConsole = true;
        }
        return true;
    }

    public static void main(String[] args) {
        if (!parseArguments(args)) {
            return;
        }
        ImageReader IR = new ImageReader(isFlippedHorisontal, isFlippedVertical, isRotating, isConcurrent, RotAngle);
        ImageWriter IW = new ImageWriter(ToConsole, isFilled, isSpaced, isColored, isDoubled);
        Image image;
        image = IR.ConvertToAscii(Input);
        IW.PrintImage(Output, image);

    }
}
