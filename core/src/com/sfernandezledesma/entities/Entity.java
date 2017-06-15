/*
 * Entity.java
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
import com.sfernandezledesma.physics.CollisionQuadtree;
import com.sfernandezledesma.world.World;

public abstract class Entity {
    private static int next_id = 1;
    protected int myID;
    protected AABB box;
    protected GameSprite gameSprite;
    protected CollisionQuadtree quadtree = null;
    protected boolean toBeDestroyed = false;
    protected World world = null;

    public Entity(AABB box, GameSprite gameSprite, boolean centerPosition, World world) {
        assignId();
        this.box = box;
        this.gameSprite = gameSprite; // For now lets assume all entities have a sprite
        if (centerPosition) {
            this.box.translateX(gameSprite.getScreenOffsetX());
            this.box.translateY(gameSprite.getScreenOffsetY());
        }
        this.world = world;
        //Gdx.app.log("ENTITY INFO", "Created entity " + myID);
    }

    protected void assignId() {
        myID = next_id;
        next_id = next_id + 1;
    }

    public AABB getBox() {
        return box;
    }

    public boolean setX(double x) {
        box.setX(x);
        return quadtree.update(this);
    }

    public boolean setY(double y) {
        box.setY(y);
        return quadtree.update(this);
    }

    public double getX() {
        return box.getX();
    }

    public double getY() {
        return box.getY();
    }

    public boolean translateX(double dx) {
        box.translateX(dx);
        return quadtree.update(this);
    }

    public boolean translateY(double dy) {
        box.translateY(dy);
        return quadtree.update(this);
    }

    protected abstract boolean resolveCollisionOf(Entity entity, float delta);
    protected boolean onCollisionWithDynamicEntity(DynamicEntity otherDynamicEntity, float delta) { return true; }
    protected boolean onCollisionWithStaticEntity(StaticEntity otherStaticEntity, float delta) { return true; }
    protected boolean onCollisionWithHero(Hero hero, float delta) { return true; }
    protected boolean onCollisionWithOneWayPlatform(OneWayPlatform oneWayPlatform, float delta) { return false; }
    protected boolean onCollisionWithLadder(Ladder ladder, float delta) { return false; }

    public abstract void render(SpriteBatch batch);

    public CollisionQuadtree getQuadtree() {
        return quadtree;
    }

    public void setQuadtree(CollisionQuadtree quadtree) {
        this.quadtree = quadtree;
    }

    public boolean isToBeDestroyed() {
        return toBeDestroyed;
    }

    public void setToBeDestroyed(boolean toBeDestroyed) {
        this.toBeDestroyed = toBeDestroyed;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Entity entity = (Entity) o;

        return myID == entity.myID;

    }

    @Override
    public int hashCode() {
        return myID;
    }

    public int getID() {
        return myID;
    }
}
