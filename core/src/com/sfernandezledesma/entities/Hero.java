/*
 * Hero.java
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

package com.sfernandezledesma.entities;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.sfernandezledesma.graphics.GameSprite;
import com.sfernandezledesma.physics.AABB;
import com.sfernandezledesma.world.World;

public class Hero extends DynamicEntity {
    private double horizontalVelocity = 100;
    private double verticalVelocity = 100;
    private double gravityAccel = 400;
    private double jumpVelocity = 200;
    private boolean isTouchingDown = false;
    private boolean stepDown = false;
    private boolean climbingLadder = false;
    private boolean onLadder = false;

    public Hero(AABB box, GameSprite gameSprite, boolean centerPosition) {
        super(box, gameSprite, centerPosition);
        setAccelerationY(-gravityAccel);
    }

    @Override
    protected boolean onCollisionWithStaticEntity(StaticEntity otherStaticEntity, World world, float delta) {
        updateTouchingDown(otherStaticEntity.box);
        return true;
    }

    @Override
    protected boolean onCollisionWithDynamicEntity(DynamicEntity otherDynamicEntity, World world, float delta) {
        updateTouchingDown(otherDynamicEntity.box);
        return super.onCollisionWithDynamicEntity(otherDynamicEntity, world, delta);
    }

    @Override
    protected boolean onCollisionWithOneWayPlatform(OneWayPlatform oneWayPlatform, World world, float delta) {
        if (velocityY <= 0 && box.bottomSideY() >= oneWayPlatform.getBox().topSideY()) {
            isTouchingDown = !stepDown;
            return isTouchingDown;
        } else {
            return false;
        }
    }

    @Override
    protected boolean onCollisionWithLadder(Ladder ladder, World world, float delta) {
        boolean ret;
        if (climbingLadder)
            ret = false;
        else if ((velocityY <= 0 && box.bottomSideY() >= ladder.getBox().topSideY()) && !onLadder) { // Evaluating !onLadder works because we always move horizontally first, so we will only collide from above with the top section of the ladder
            isTouchingDown = true;
            ret = true;
        } else
            ret = false;
        onLadder = true;
        return ret;
    }

    private boolean updateTouchingDown(AABB otherBox) {
        if (velocityY <= 0 && box.bottomSideY() >= otherBox.topSideY()) {
            isTouchingDown = true;
        } else if (velocityY > 0) {
            isTouchingDown = false;
        }
        return isTouchingDown;
    }

    @Override
    protected boolean resolveCollisionOf(Entity entity, World world, float delta) {
        return entity.onCollisionWithHero(this, world, delta);
    }

    private void handleInput() {
        setVelocityX(0);
        if (Gdx.input.isKeyPressed(Input.Keys.LEFT))
            setVelocityX(-horizontalVelocity);
        if (Gdx.input.isKeyPressed(Input.Keys.RIGHT))
            setVelocityX(horizontalVelocity);
        if (Gdx.input.isKeyJustPressed(Input.Keys.Z) && (isTouchingDown || climbingLadder)) {
            setVelocityY(jumpVelocity);
            climbingLadder = false;
        }

        if (climbingLadder)
            setVelocityY(0);

        if (Gdx.input.isKeyPressed(Input.Keys.UP) && onLadder) {
            setVelocityY(verticalVelocity);
            climbingLadder = true;
        }
        if (Gdx.input.isKeyPressed(Input.Keys.DOWN)) {
            if (onLadder) {
                setVelocityY(-verticalVelocity);
                climbingLadder = true;
            }
            stepDown = true;
        }
    }

    @Override
    protected void updateBeforeMoving(float delta) {
        handleInput();
        if (!climbingLadder) {
            super.updateBeforeMoving(delta);
        }
        onLadder = false;
    }

    @Override
    protected void updateAfterMoving(boolean collidedHorizontally, boolean collidedVertically, float delta) {
        stepDown = false;
        if (!collidedVertically) {
            isTouchingDown = false;
        }
        if (!onLadder) {
            climbingLadder = false;
        }
    }
}
