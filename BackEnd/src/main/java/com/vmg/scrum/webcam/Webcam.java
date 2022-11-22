package com.vmg.scrum.webcam;

import com.github.sarxos.webcam.WebcamEvent;
import com.github.sarxos.webcam.WebcamListener;
import com.github.sarxos.webcam.WebcamResolution;
import org.opencv.core.Core;

import javax.imageio.ImageIO;
import java.awt.*;
import java.io.File;
import java.io.IOException;

public class Webcam {
    public static void main(String[] args) throws IOException {
//        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
//        String imgFile =

        com.github.sarxos.webcam.Webcam webcam = com.github.sarxos.webcam.Webcam.getDefault();

        webcam.addWebcamListener(new WebcamListener() {
            @Override
            public void webcamOpen(WebcamEvent we) {
                System.out.println("Webcam Open");
            }

            @Override
            public void webcamClosed(WebcamEvent we) {
                System.out.println("Webcam closed");
            }

            @Override
            public void webcamDisposed(WebcamEvent we) {
                System.out.println("Webcam disposed");
            }

            @Override
            public void webcamImageObtained(WebcamEvent we) {
                System.out.println("Image Taken");
            }
        });

        for (Dimension supportedSize : webcam.getViewSizes()) {
            System.out.println(supportedSize.toString());
        }

//        webcam.setViewSize(new Dimension(640, 480));
        webcam.setViewSize(WebcamResolution.VGA.getSize());

        webcam.open();
        ImageIO.write(webcam.getImage(), "PNG", new File("firstCapture.png"));
        webcam.close();
    }
}
