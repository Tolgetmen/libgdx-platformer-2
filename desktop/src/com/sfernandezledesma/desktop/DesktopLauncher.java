package com.sfernandezledesma.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.sfernandezledesma.Platformer;

public class DesktopLauncher {
    public static void main(String[] arg) {
        LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
        config.width = Platformer.getWindowWidth();
        config.height = Platformer.getWindowHeight();
        config.title = Platformer.getTitle();
        new LwjglApplication(new Platformer(), config);
    }
}
