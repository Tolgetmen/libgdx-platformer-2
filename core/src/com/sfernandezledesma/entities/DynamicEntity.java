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


import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.sfernandezledesma.graphics.GameSprite;
import com.sfernandezledesma.physics.AABB;
import com.sfernandezledesma.world.World;

import java.util.List;

public class DynamicEntity extends Entity {
    protected double velocityX = 0;
    protected double velocityY = 0;
    protected double accelerationX = 0;
    protected double accelerationY = 0;
    protected boolean updating = false;
    protected AABB newBox;

    public DynamicEntity(AABB box, GameSprite gameSprite, boolean centerPosition, World world) {
        super(box, gameSprite, centerPosition, world);
        newBox = new AABB(box);
        world.addDynamicEntity(this);
    }

    @Override
    protected boolean resolveCollisionOf(Entity entity, float delta) {
        return entity.onCollisionWithDynamicEntity(this, delta);
    }

    @Override
    protected boolean onCollisionWithDynamicEntity(DynamicEntity otherDynamicEntity, float delta) {
        otherDynamicEntity.update(delta); // Lets try to move the other entity, maybe we are not really colliding
        return newBox.overlapsWith(otherDynamicEntity.box); // We return true if we are still colliding
    }

    @Override
    public void render(SpriteBatch batch)  {
        gameSprite.setPosition((float) getX(), (float) getY());
        gameSprite.draw(batch);
    }

    // This is called inside update, before moving the entity
    protected void updateBeforeMoving(float delta) {
        velocityX += accelerationX * delta;
        velocityY += accelerationY * delta;
    }

    // This is called inside update, after moving the entity
    protected void updateAfterMoving(boolean collidedHorizontally, boolean collidedVertically, float delta) {
    }

    // Invariant: All rigid entities are NOT colliding, we have to keep that invariant after each update
    public void update(float delta) {
        if (updating)
            return;
        setUpdating(true);
        // First we update velocities, handle input, etc
        updateBeforeMoving(delta);
        // Now we try to move horizontally
        boolean collidedHorizontally = moveAndCollideHorizontally(delta);
        // Now we try to move vertically
        boolean collidedVertically = moveAndCollideVertically(delta);
        // Finally we update the entity after it moved
        updateAfterMoving(collidedHorizontally, collidedVertically, delta);
    }

    // Moves horizontally colliding with other entities.
    private boolean moveAndCollideHorizontally(float delta) {
        double dx = velocityX * delta;
        newBox.syncPositionWith(box);
        newBox.translateX(dx);
        double minimumCollidingLeftSideX = Double.MAX_VALUE;
        double maximumCollidingRightSideX = -Double.MAX_VALUE;
        boolean collidesWithSomething = false;
        List<Entity> entitiesToCollide = quadtree.getPossibleCollidingEntities(newBox);
        //Gdx.app.log("DYNAMIC ENTITY", "Checking collision with " + entitiesToCollide.size() + " possible entities.");
        for (Entity otherEntity : entitiesToCollide) {
            if (otherEntity.equals(this))
                continue;
            if (newBox.overlapsWith(otherEntity.getBox())) {
                if (otherEntity.resolveCollisionOf(this, delta)) {
                    collidesWithSomething = true;
                    minimumCollidingLeftSideX = Math.min(minimumCollidingLeftSideX, otherEntity.getBox().leftSideX());
                    maximumCollidingRightSideX = Math.max(maximumCollidingRightSideX, otherEntity.getBox().rightSideX());
                }
            }
        }
        if (collidesWithSomething) { // We fix its position and keep the invariant
            //Gdx.app.log("COLLISION INFO", "Collided horizontally.");
            if (velocityX > 0) { // The entity was colliding to the other from the left
                setX(minimumCollidingLeftSideX - box.getWidth());
            } else { // The entity was colliding from the right
                setX(maximumCollidingRightSideX);
            }
            velocityX = 0;
        } else {
            setX(newBox.getX());
        }
        return collidesWithSomething;
    }

    // Moves vertically colliding with other entities.
    private boolean moveAndCollideVertically(float delta) {
        double dy = velocityY * delta;
        newBox.syncPositionWith(box);
        newBox.translateY(dy);
        double minimumCollidingBottomSideY = Double.MAX_VALUE;
        double maximumCollidingTopSideY = -Double.MAX_VALUE;
        boolean collidesWithSomething = false;
        List<Entity> entitiesToCollide = quadtree.getPossibleCollidingEntities(newBox);
        //Gdx.app.log("DYNAMIC ENTITY", "Checking collision with " + entitiesToCollide.size() + " possible entities.");
        for (Entity otherEntity : entitiesToCollide) {
            if (otherEntity.equals(this))
                continue;
            if (newBox.overlapsWith(otherEntity.getBox())) {
                if (otherEntity.resolveCollisionOf(this, delta)) {
                    collidesWithSomething = true;
                    minimumCollidingBottomSideY = Math.min(minimumCollidingBottomSideY, otherEntity.getBox().bottomSideY());
                    maximumCollidingTopSideY = Math.max(maximumCollidingTopSideY, otherEntity.getBox().topSideY());
                }
            }
        }
        if (collidesWithSomething) { // We fix its position and keep the invariant
            //Gdx.app.log("COLLISION INFO", "Collided vertically.");
            if (velocityY > 0) { // The entity was colliding to the other from below
                setY(minimumCollidingBottomSideY - box.getHeight());
            } else { // The entity was colliding from above
                setY(maximumCollidingTopSideY);
            }
            velocityY = 0;
        } else {
            setY(newBox.getY());
        }
        return collidesWithSomething;
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

    public boolean isUpdating() {
        return updating;
    }

    public void setUpdating(boolean updating) {
        this.updating = updating;
    }

}
