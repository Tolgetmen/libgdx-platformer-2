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
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.sfernandezledesma.Platformer;
import com.sfernandezledesma.entities.DynamicEntity;
import com.sfernandezledesma.entities.Hero;
import com.sfernandezledesma.entities.Ladder;
import com.sfernandezledesma.entities.OneWayPlatform;
import com.sfernandezledesma.entities.StaticEntity;
import com.sfernandezledesma.entities.World;
import com.sfernandezledesma.graphics.GameSprite;
import com.sfernandezledesma.physics.AABB;


public class PlayingScreen extends GameScreen {
    private FPSLogger fpsLogger = new FPSLogger();
    private ShapeRenderer shapeRenderer = new ShapeRenderer();

    private Texture texture;
    private GameSprite heroSprite;
    private GameSprite wallSprite;
    private GameSprite oneWayPlatformSprite;
    private GameSprite ladderSprite;

    private Hero hero;
    private World world;
    private boolean paused = true;

    private double vx = 0;
    private double vy = 0;

    public PlayingScreen(Platformer game) {
        super(game);
        world = new World(512, 512);
        texture = new Texture("simples_pimples.png");
        camera = new OrthographicCamera();
        viewport = new FitViewport(Platformer.getWindowWidth() / 2, Platformer.getWindowHeight() / 2, camera);
        camera.setToOrtho(false, viewport.getWorldWidth(), viewport.getWorldHeight());
        heroSprite = new GameSprite(texture, 416, 16, 16, 16, 3, 0);
        wallSprite = new GameSprite(texture, 16, 96, 16, 16, 0, 0);
        oneWayPlatformSprite = new GameSprite(texture, 32, 768, 16, 16, 0, 0);
        ladderSprite = new GameSprite(texture, 0, 192, 16, 16, 1, 0);
        this.vx = -10;
        this.vy = vx;

        testPlayground();

        paused = false;
    }

    private void testPlayground() {
        for (int i = 0; i < 512; i += 16) {
            world.addStaticEntity(new StaticEntity(new AABB(i, 0, wallSprite.getWidth(), wallSprite.getHeight()), wallSprite, true));
        }
        for (int i = 0; i < 192; i += 16) {
            world.addStaticEntity(new StaticEntity(new AABB(i, 48, wallSprite.getWidth(), wallSprite.getHeight()), wallSprite, true));
        }
        for (int i = 272; i < 512; i += 16) {
            world.addStaticEntity(new StaticEntity(new AABB(i, 48, wallSprite.getWidth(), wallSprite.getHeight()), wallSprite, true));
        }
        for (int i = 128; i < 240; i += 16) {
            world.addStaticEntity(new StaticEntity(new AABB(i, 96, wallSprite.getWidth(), wallSprite.getHeight()), wallSprite, true));
        }
        for (int i = 64; i < 112; i += 16) {
            world.addStaticEntity(new Ladder(new AABB(112, i, ladderSprite.getWidth(), ladderSprite.getHeight()), ladderSprite, true));
        }
        hero = new Hero(new AABB(128, 350, 10, 16), heroSprite, true);
        DynamicEntity movingWall1 = new DynamicEntity(new AABB(272, 272, wallSprite.getWidth(), wallSprite.getHeight()), wallSprite, true);
        movingWall1.setVelocityX(vx);
        movingWall1.setVelocityY(vy);
        DynamicEntity movingWall2 = new DynamicEntity(new AABB(288, 272, wallSprite.getWidth(), wallSprite.getHeight()), wallSprite, true);
        movingWall2.setVelocityX(vx);
        movingWall2.setVelocityY(vy);
        for (int i = 0; i < 196; i += oneWayPlatformSprite.getWidth())
            world.addStaticEntity(new OneWayPlatform(new AABB(i, 128, oneWayPlatformSprite.getWidth(), oneWayPlatformSprite.getHeight()), oneWayPlatformSprite, true));
        world.addDynamicEntity(hero);
        world.addDynamicEntity(movingWall2);
        world.addDynamicEntity(movingWall1);
    }

    private void testMovingWalls() {
        AABB box = new AABB(0, 0, wallSprite.getWidth(), wallSprite.getHeight());
        for (int i = 8; i < 32; i++) {
            for (int j = 24; j > 5; j--) {
                box.setPosition(i * box.getWidth(), j * box.getHeight());
                DynamicEntity movingWall = new DynamicEntity(box, wallSprite, true);
                movingWall.setVelocityY(vy);
                movingWall.setVelocityX(vx);
                world.addDynamicEntity(movingWall);
            }
        }
    }

    @Override
    public void show() {
        paused = false;
    }

    @Override
    public void render(float delta) {
        //fpsLogger.log();
        //Gdx.app.log("QUADTREE INFO", "Total entities: " + world.getQuadtree().getTotalEntities());
        delta = Math.min(delta, Platformer.MAX_DELTA);
        if (!paused) world.update(delta);

        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        batch.setProjectionMatrix(camera.combined);
        batch.begin();
        world.render(batch);
        batch.end();

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
