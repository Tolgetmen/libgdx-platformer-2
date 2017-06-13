/*
 * GameSprite.java
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

package com.sfernandezledesma.graphics;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;


public class GameSprite extends Sprite {
    private float screenOffsetX = 0;
    private float screenOffsetY = 0;

    public GameSprite(Texture texture, int srcX, int srcY, int srcWidth, int srcHeight, float screenOffsetX, float screenOffsetY) {
        super(texture, srcX, srcY, srcWidth, srcHeight);
        this.screenOffsetX = screenOffsetX;
        this.screenOffsetY = screenOffsetY;
    }

    public GameSprite(GameSprite other) {
        this(other.getTexture(), other.getRegionX(), other.getRegionY(), other.getRegionWidth(), other.getRegionHeight(), other.screenOffsetX, other.screenOffsetY);
    }

    public float getScreenOffsetX() {
        return screenOffsetX;
    }

    public float getScreenOffsetY() {
        return screenOffsetY;
    }

    @Override
    public void setPosition(float x, float y) {
        super.setPosition(x - screenOffsetX, y - screenOffsetY);
    }
}
