package ui;

import java.awt.*;

public class GraphicInterface {
    public static void main() {
        System.out.println("Starting in GUI.");
        final SplashScreen splash = SplashScreen.getSplashScreen();
        if (splash == null) {
            System.out.println("SplashScreen.getSplashScreen() returned null");
            return;
        }
        Graphics2D g = splash.createGraphics();
        if (g == null) {
            System.out.println("g is null");
            return;
        }
        MainContainer.main();
    }
}
