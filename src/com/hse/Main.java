package com.hse;

import com.hse.Image;

import javax.print.DocFlavor;

import static com.hse.ExceptionHandler.PrintException;

public class Main {

    public static void PrintHelp() {
        System.out.println("Usage: java -jar jpegToAscii.jar <inputFile> [<outputFile>]\n" +
                "[-f, --filled  - fill background]\n" +
                "[-s, --spaced  - all chars turns to space (for colored background mode)]\n" +
                "[-n, --console - all output will be force written in console]\n" +
                "[-c, --colored - allow writing colors in images]\n" +
                "[-h, --help    - show this help]\n" +
                "[-d, --no-double    - off double char mode]\n" +
                "if no outputFile specified, output will be written in console\n");
    }

    public static void main(String[] args) {
        Image image = new Image();
        String Input = "";
        String Output = "";
        boolean ToConsole = false;
        boolean isFilled = false;
        boolean isSpaced = false;
        boolean isColored = false;
        boolean isDoubled = true;
        if (args.length < 1) {
            PrintException("Couldn't find path in args", "Main");
            PrintHelp();
            return;
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
                    case "--no-double ": {
                        isDoubled = true;
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
                        return;
                    }
                }
            } else {
                Output = args[i];
            }
        }
        if (Output == "") {
            ToConsole = true;
        }
        image.ConvertToAscii(Input);
        if (ToConsole) {
            if (isColored)
                image.PrintColoredImageToFile(System.out, isFilled, isSpaced, isDoubled);
            else
                image.PrintImageToFile(System.out, isDoubled);
        } else {
            if (isColored)
                image.PrintColoredImageToFile(Output, isFilled, isSpaced, isDoubled);
            else
                image.PrintImageToFile(Output, isDoubled);
        }
    }
}
