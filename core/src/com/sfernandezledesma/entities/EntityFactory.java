/*
 * EntityFactory.java
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


import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Texture;
import com.sfernandezledesma.graphics.GameAnimatedSprite;
import com.sfernandezledesma.graphics.GameFixedSprite;
import com.sfernandezledesma.physics.AABB;
import com.sfernandezledesma.world.World;

public class EntityFactory {
    public enum EntityName {HERO, GROUND, WALL, LADDER, ONEWAY}

    private World world;
    private Texture tileset;

    public EntityFactory(World world) {
        this.world = world;
        tileset = world.getAssetManager().get("simples_pimples.png", Texture.class);
    }

    public void createEntityInWorld(EntityName name, double x, double y) {
        switch (name) {
            case HERO:
                new Hero(new AABB(x, y, 10, 16), new GameAnimatedSprite(tileset, 416, 16, 96, 16, 3, 0), true, world);
                break;
            case GROUND:
                new StaticEntity(new AABB(x, y, 16, 16), new GameFixedSprite(tileset, 16, 96, 16, 16, 0, 0), true, world);
                break;
            case ONEWAY:
                new OneWayPlatform(new AABB(x, y, 16, 16), new GameFixedSprite(tileset, 32, 768, 16, 16, 0, 0), true, world);
                break;
            case LADDER:
                new Ladder(new AABB(x, y, 8, 16), new GameFixedSprite(tileset, 0, 192, 16, 16, 4, 0), true, world);
                break;
        }
    }

    public World getWorld() {
        return world;
    }

    public void setWorld(World world) {
        this.world = world;
    }
}
