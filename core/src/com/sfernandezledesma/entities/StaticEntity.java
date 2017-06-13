/*
 * StaticEntity.java
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

public class StaticEntity extends Entity {

    public StaticEntity(AABB box, GameSprite gameSprite, boolean centerPosition) {
        super(box, gameSprite, centerPosition);
        this.gameSprite.setPosition((float) getX(), (float) getY());
    }

    @Override
    protected boolean resolveCollisionOf(Entity entity, World world, float delta) {
        return entity.onCollisionWithStaticEntity(this, world, delta);
    }

    @Override
    public void render(SpriteBatch batch)  {
        gameSprite.draw(batch);
    }
}
