/*
 * DynamicEntity.java
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


import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;
import com.sfernandezledesma.physics.AABB;

import java.util.List;

public class DynamicEntity extends Entity {
    protected double velocityX = 0;
    protected double velocityY = 0;
    protected double accelerationX = 0;
    protected double accelerationY = 0;
    protected boolean isResolvingCollision = false;
    protected AABB newBox;

    public DynamicEntity(AABB box, Sprite sprite, Vector2 offsetsSprite) {
        super(box, sprite, offsetsSprite);
        newBox = new AABB(box);
    }

    @Override
    public void update(float delta) {
        velocityX += accelerationX * delta;
        velocityY += accelerationY * delta;
    }

    @Override
    public boolean onCollision(Entity entity, World world, float delta) {
        return entity.onCollisionWith(this, world, delta);
    }

    @Override
    public boolean onCollisionWith(DynamicEntity otherDynamicEntity, World world, float delta) {
        boolean ret = true;
        if (!otherDynamicEntity.isResolvingCollision) {
            otherDynamicEntity.moveAndCollide(world, delta); // Lets try to move the other entity, maybe we are not really colliding
            ret = newBox.overlapsWith(otherDynamicEntity.box); // We return true if we are still colliding
        }
        return ret;
    }

    public boolean moveAndCollide(World world, float delta) {
        //Gdx.app.log("COLLISION INFO", "Moving entity " + this.myID);
        setResolvingCollision(true);
        // First we try to move horizontally
        if(!moveAndCollideHorizontally(world, delta))
            return false;
        // Now we try moving vertically
        return moveAndCollideVertically(world, delta);
    }

    // Moves horizontally colliding with other entities. Returns false if out of the world.
    private boolean moveAndCollideHorizontally(World world, float delta) {
        boolean ret = true;
        if (velocityX != 0) {
            double dx = velocityX * delta;
            newBox.syncPositionWith(box);
            newBox.translateX(dx);
            double minimumCollidingLeftSideX = Double.MAX_VALUE;
            double maximumCollidingRightSideX = -Double.MAX_VALUE;
            boolean collidesWithSomething = false;
            List<Entity> entitiesToCollide = quadtree.getPossibleCollidingEntities(newBox);
            for (Entity otherEntity : entitiesToCollide) {
                if (otherEntity.equals(this))
                    continue;
                if (newBox.overlapsWith(otherEntity.getBox())) {
                    if (otherEntity.onCollision(this, world, delta)) {
                        collidesWithSomething = true;
                        minimumCollidingLeftSideX = Math.min(minimumCollidingLeftSideX, otherEntity.getBox().leftSideX());
                        maximumCollidingRightSideX = Math.max(maximumCollidingRightSideX, otherEntity.getBox().rightSideX());
                    }
                }
            }
            if (collidesWithSomething) { // We fix its position and keep the invariant
                //Gdx.app.log("COLLISION INFO", "Collided horizontally.");
                if (velocityX > 0) { // The entity was colliding to the other from the left
                    ret = setX(minimumCollidingLeftSideX - box.getWidth());
                } else { // The entity was colliding from the right
                    ret = setX(maximumCollidingRightSideX);
                }
                velocityX = 0;
            } else {
                ret = setX(newBox.getX());
            }
        }
        return ret;
    }

    // Moves vertically colliding with other entities. Returns false if out of the world.
    private boolean moveAndCollideVertically(World world, float delta) {
        boolean ret = true;
        if (velocityY != 0) {
            double dy = velocityY * delta;
            newBox.syncPositionWith(box);
            newBox.translateY(dy);
            double minimumCollidingBottomSideY = Double.MAX_VALUE;
            double maximumCollidingTopSideY = -Double.MAX_VALUE;
            boolean collidesWithSomething = false;
            List<Entity> entitiesToCollide = quadtree.getPossibleCollidingEntities(newBox);
            for (Entity otherEntity : entitiesToCollide) {
                if (otherEntity.equals(this))
                    continue;
                if (newBox.overlapsWith(otherEntity.getBox())) {
                    if (otherEntity.onCollision(this, world, delta)) {
                        collidesWithSomething = true;
                        minimumCollidingBottomSideY = Math.min(minimumCollidingBottomSideY, otherEntity.getBox().bottomSideY());
                        maximumCollidingTopSideY = Math.max(maximumCollidingTopSideY, otherEntity.getBox().topSideY());
                    }
                }
            }
            if (collidesWithSomething) { // We fix its position and keep the invariant
                //Gdx.app.log("COLLISION INFO", "Collided vertically.");
                if (velocityY > 0) { // The entity was colliding to the other from below
                    ret = setY(minimumCollidingBottomSideY - box.getHeight());
                } else { // The entity was colliding from above
                    ret = setY(maximumCollidingTopSideY);
                }
                velocityY = 0;
            } else {
                ret = setY(newBox.getY());
            }
        }
        return ret;
    }

    public void setVelocityX(double vx) {
        velocityX = vx;
    }

    public void setVelocityY(double vy) {
        velocityY = vy;
    }

    public void setAccelerationX(double ax) {
        accelerationX = ax;
    }

    public void setAccelerationY(double ay) {
        accelerationY = ay;
    }

    public boolean isResolvingCollision() {
        return isResolvingCollision;
    }

    public void setResolvingCollision(boolean resolvingCollision) {
        isResolvingCollision = resolvingCollision;
    }
}
