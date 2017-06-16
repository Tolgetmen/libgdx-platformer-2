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


public abstract class GameSprite extends Sprite {
    public enum State {STANDING, RUNNING, JUMPING, CLIMBING, ATTACKING, DEAD}

    protected boolean facingRight = true;
    protected State state = State.STANDING;
    protected int srcX;
    protected int srcY;
    protected int srcWidth;
    protected int srcHeight;
    protected float screenOffsetX = 0;
    protected float screenOffsetY = 0;

    public GameSprite(Texture texture, int srcX, int srcY, int srcWidth, int srcHeight, float screenOffsetX, float screenOffsetY) {
        super(texture, srcX, srcY, srcWidth, srcHeight);
        this.screenOffsetX = screenOffsetX;
        this.screenOffsetY = screenOffsetY;
        this.srcX = srcX;
        this.srcY = srcY;
        this.srcWidth = srcWidth;
        this.srcHeight = srcHeight;
    }

    public void setState(State newState) {
        state = newState;
    }

    public void setFacingRight(boolean facingRight) {
        setFlip(!facingRight, false);
        this.facingRight = facingRight;
    }

    /*public GameSprite(GameSprite other) {
        this(other.getTexture(), other.getRegionX(), other.getRegionY(), other.getRegionWidth(), other.getRegionHeight(), other.screenOffsetX, other.screenOffsetY);
    }*/

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
