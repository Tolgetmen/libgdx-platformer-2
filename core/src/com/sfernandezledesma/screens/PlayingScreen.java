/*
 * PlayingScreen.java
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

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.FPSLogger;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.sfernandezledesma.Platformer;
import com.sfernandezledesma.entities.DynamicEntity;
import com.sfernandezledesma.entities.EntityFactory;
import com.sfernandezledesma.entities.Hero;
import com.sfernandezledesma.entities.Ladder;
import com.sfernandezledesma.entities.OneWayPlatform;
import com.sfernandezledesma.entities.StaticEntity;
import com.sfernandezledesma.graphics.GameFixedSprite;
import com.sfernandezledesma.world.World;
import com.sfernandezledesma.graphics.GameSprite;
import com.sfernandezledesma.physics.AABB;


public class PlayingScreen extends GameScreen {
    private FPSLogger fpsLogger = new FPSLogger();
    private ShapeRenderer shapeRenderer = new ShapeRenderer();

    private Texture texture;
    private GameSprite wallSprite;

    private World world;
    private boolean paused = true;

    private double vx = 0;
    private double vy = 0;

    public PlayingScreen(Platformer game) {
        super(game);
        world = new World(512, 512, game.getAssetManager());
        texture = game.getAssetManager().get("simples_pimples.png", Texture.class);
        wallSprite = new GameFixedSprite(texture, 16, 96, 16, 16, 0, 0);
        this.vx = -10;
        this.vy = vx;

        testPlayground();

        paused = false;
    }

    private void testPlayground() {
        EntityFactory entityFactory = new EntityFactory(world);
        entityFactory.createEntityInWorld(EntityFactory.EntityName.HERO, 128, 350);
        for (int i = 0; i < 512; i += 16) {
            entityFactory.createEntityInWorld(EntityFactory.EntityName.GROUND, i, 0);
        }
        for (int i = 0; i < 192; i += 16) {
            entityFactory.createEntityInWorld(EntityFactory.EntityName.GROUND, i, 48);
        }
        for (int i = 272; i < 512; i += 16) {
            entityFactory.createEntityInWorld(EntityFactory.EntityName.GROUND, i, 48);
        }
        for (int i = 128; i < 240; i += 16) {
            entityFactory.createEntityInWorld(EntityFactory.EntityName.GROUND, i, 96);
        }
        for (int i = 64; i < 112; i += 16) {
            entityFactory.createEntityInWorld(EntityFactory.EntityName.LADDER, 112, i);
        }
        for (int i = 0; i < 196; i += 16) {
            entityFactory.createEntityInWorld(EntityFactory.EntityName.ONEWAY, i, 128);
        }
        DynamicEntity movingWall1 = new DynamicEntity(new AABB(272, 272, wallSprite.getWidth(), wallSprite.getHeight()), wallSprite, true, world);
        movingWall1.setVelocityX(vx);
        movingWall1.setVelocityY(vy);
        DynamicEntity movingWall2 = new DynamicEntity(new AABB(288, 272, wallSprite.getWidth(), wallSprite.getHeight()), wallSprite, true, world);
        movingWall2.setVelocityX(vx);
        movingWall2.setVelocityY(vy);
    }

    private void testMovingWalls() {
        for (int i = 8; i < 32; i++) {
            for (int j = 24; j > 5; j--) {
                DynamicEntity movingWall = new DynamicEntity(new AABB(16 * i, 16 * j, 16, 16), wallSprite, true, world);
                movingWall.setVelocityY(vy);
                movingWall.setVelocityX(vx);
            }
        }
    }

    @Override
    public void show() {
        paused = false;
    }

    @Override
    public void render(float delta) {
        if (paused)
            return;
        delta = Math.min(delta, Platformer.getMaxDelta());
        world.update(delta);

        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        batch.setProjectionMatrix(camera.combined);
        batch.begin();
        world.render(batch);
        batch.end();

        //fpsLogger.log();
        //Gdx.app.log("QUADTREE INFO", "Total entities: " + world.getQuadtree().getTotalEntities());
        shapeRenderer.setProjectionMatrix(camera.combined);
        shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
        world.getQuadtree().debugRender(shapeRenderer);
        shapeRenderer.end();
    }

    @Override
    public void pause() {
        paused = true;
    }

    @Override
    public void resume() {
        paused = false;
    }

    @Override
    public void hide() {
        paused = true;
    }

    @Override
    public void dispose() {
        texture.dispose();
    }
}
