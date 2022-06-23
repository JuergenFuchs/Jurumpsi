package com.badlogic.drop.desktop;

import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration;
import com.badlogic.drop.Drop;

public class DesktopLauncher {
   public static void main (String[] arg) {
      Lwjgl3ApplicationConfiguration config = new Lwjgl3ApplicationConfiguration();
      config.setTitle("Drop");
      config.setWindowedMode(800, 480);
      config.useVsync(true);
      config.setForegroundFPS(60);
      new Lwjgl3Application(new Drop(), config);
   }
}