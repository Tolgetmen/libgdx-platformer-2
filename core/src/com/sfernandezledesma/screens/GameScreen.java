/*
 * GameScreen.java
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

import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.sfernandezledesma.Platformer;


public abstract class GameScreen implements Screen {
    protected Platformer game;
    protected SpriteBatch batch;
    protected OrthographicCamera camera;
    protected Viewport viewport;

    GameScreen(Platformer game) {
        this.game = game;
        this.batch = game.getBatch();
    }

    @Override
    public void resize(int width, int height) {
        viewport.update(width, height);
    }
}
