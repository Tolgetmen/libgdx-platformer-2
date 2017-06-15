/*
 * LoadingScreen.java
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

package com.sfernandezledesma.screens;


import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Texture;
import com.sfernandezledesma.Platformer;

public class LoadingScreen extends GameScreen {
    private AssetManager assetManager;

    public LoadingScreen(Platformer game) {
        super(game);
        assetManager = game.getAssetManager();
        assetManager.load("simples_pimples.png", Texture.class);
    }

    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {
        if (assetManager.update())
            game.setScreen(new PlayingScreen(game));
    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {

    }
}
