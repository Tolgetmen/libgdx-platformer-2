/*
 * Platformer.java
 * Copyright 2017 Sebastian Fernandez Ledesma
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.sfernandezledesma;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.sfernandezledesma.screens.LoadingScreen;
import com.sfernandezledesma.screens.PlayingScreen;


public class Platformer extends Game {
    private static final float MAX_DELTA = 1f / 30f;
    private static final int WINDOW_WIDTH = 1024;
    private static final int WINDOW_HEIGHT = 576;
    private static final int VIEWPORT_WIDTH = WINDOW_WIDTH / 2;
    private static final int VIEWPORT_HEIGHT = WINDOW_HEIGHT / 2;
    private static final String TITLE = "Yet Another Platformer!";
    private SpriteBatch batch;
    private AssetManager assetManager;

    @Override
    public void create() {
        batch = new SpriteBatch();
        assetManager = new AssetManager();
        setScreen(new LoadingScreen(this));
    }

    @Override
    public void render() {
        super.render(); // Delegates the rendering to the active screen.
    }

    @Override
    public void dispose() {
        batch.dispose();
        assetManager.dispose();
    }

    public SpriteBatch getBatch() {
        return batch;
    }

    public static int getViewportWidth() {
        return VIEWPORT_WIDTH;
    }

    public static int getViewportHeight() {
        return VIEWPORT_HEIGHT;
    }

    public static int getWindowWidth() {
        return WINDOW_WIDTH;
    }

    public static int getWindowHeight() {
        return WINDOW_HEIGHT;
    }

    public static String getTitle() {
        return TITLE;
    }

    public static float getMaxDelta() {
        return MAX_DELTA;
    }

    public AssetManager getAssetManager() {
        return assetManager;
    }
}
