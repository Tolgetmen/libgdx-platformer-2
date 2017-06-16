/*
 * GameAnimatedSprite.java
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


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.Array;

public class GameAnimatedSprite extends GameSprite {
    protected enum Frame {FRAME_STANDING, FRAME_RUNNING_1, FRAME_RUNNING_2, FRAME_CLIMBING, FRAME_ATTACKING, FRAME_DEAD}
    private Animation<TextureRegion> runningAnimation;
    private Animation<TextureRegion> climbingAnimation;
    private float stateTimer = 0;

    public GameAnimatedSprite(Texture texture, int srcX, int srcY, int srcWidth, int srcHeight, float screenOffsetX, float screenOffsetY) {
        super(texture, srcX, srcY, srcWidth, srcHeight, screenOffsetX, screenOffsetY);
        setBounds(0, 0, 16, 16);
        TextureRegion textureRegion = new TextureRegion(getTexture());
        Array<TextureRegion> frames = new Array<TextureRegion>();
        Frame frame = Frame.FRAME_RUNNING_1;
        textureRegion.setRegion(srcX + 16 * frame.ordinal(), srcY, 16, 16);
        frames.add(new TextureRegion(textureRegion));
        frame = Frame.FRAME_RUNNING_2;
        textureRegion.setRegion(srcX + 16 * frame.ordinal(), srcY, 16, 16);
        frames.add(new TextureRegion(textureRegion));
        runningAnimation = new Animation<TextureRegion>(0.1f, frames);

        frames = new Array<TextureRegion>();
        frame = Frame.FRAME_CLIMBING;
        textureRegion.setRegion(srcX + 16 * frame.ordinal(), srcY, 16, 16);
        frames.add(new TextureRegion(textureRegion));
        frame = Frame.FRAME_RUNNING_2;
        textureRegion.setRegion(srcX + 16 * frame.ordinal(), srcY, 16, 16);
        frames.add(new TextureRegion(textureRegion));
        climbingAnimation = new Animation<TextureRegion>(0.1f, frames);

        setFrame(Frame.FRAME_STANDING);
    }

    @Override
    public void draw(Batch batch) {
        stateTimer += Gdx.graphics.getDeltaTime();
        TextureRegion region;
        switch (state) {
            default:
            case STANDING:
                setFrame(Frame.FRAME_STANDING);
                break;
            case RUNNING:
                setRegion(runningAnimation.getKeyFrame(stateTimer, true));
                break;
            case JUMPING:
                setFrame(Frame.FRAME_RUNNING_1);
                break;
            case CLIMBING:
                setRegion(climbingAnimation.getKeyFrame(stateTimer, true));
                break;
        }
        setFacingRight(facingRight);
        super.draw(batch);
    }

    private void setFrame(Frame frame) {
        setRegion(srcX + 16 * frame.ordinal(), srcY, 16, 16);
    }
}
