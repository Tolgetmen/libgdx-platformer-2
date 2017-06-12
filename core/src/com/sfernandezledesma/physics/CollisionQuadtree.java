/*
 * CollisionQuadtree.java
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

package com.sfernandezledesma.physics;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.sfernandezledesma.entities.Entity;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

public class CollisionQuadtree {
    public static final int MAX_ENTITIES = 8;
    public static final int MAX_LEVELS = 10;
    private int level;
    private Set<Entity> entities;
    private AABB bounds;
    private CollisionQuadtree parent = null;
    private CollisionQuadtree topLeftTree = null;
    private CollisionQuadtree topRightTree = null;
    private CollisionQuadtree bottomLeftTree = null;
    private CollisionQuadtree bottomRightTree = null;

    public CollisionQuadtree(int level, AABB bounds, CollisionQuadtree parent) {
        this.level = level;
        entities = new HashSet<Entity>(MAX_ENTITIES);
        this.bounds = new AABB(bounds);
        this.parent = parent;
    }

    public void clear() {
        getEntities().clear();
        if (getTopLeftTree() != null) {
            getTopLeftTree().clear();
            getTopRightTree().clear();
            getBottomLeftTree().clear();
            getBottomRightTree().clear();
        }
    }

    private void split() {
        double halfWidth = getBounds().getWidth() / 2;
        double halfHeight = getBounds().getHeight() / 2;
        double centerX = getBounds().leftSideX() + halfWidth;
        double centerY = getBounds().bottomSideY() + halfHeight;
        topLeftTree = new CollisionQuadtree(getLevel() + 1, new AABB(getBounds().leftSideX(), centerY, halfWidth, halfHeight), this);
        topRightTree = new CollisionQuadtree(getLevel() + 1, new AABB(centerX, centerY, halfWidth, halfHeight), this);
        bottomLeftTree = new CollisionQuadtree(getLevel() + 1, new AABB(getBounds().leftSideX(), getBounds().bottomSideY(), halfWidth, halfHeight), this);
        bottomRightTree = new CollisionQuadtree(getLevel() + 1, new AABB(centerX, getBounds().bottomSideY(), halfWidth, halfHeight), this);
    }

    public boolean update(Entity e) {
        if(!remove(e)) {
            Gdx.app.log("QUADTREE ERROR", "Tried to remove something that isn't there");
            return false;
        }
        return add(e);
    }

    public boolean add(Entity e) {
        if (!e.getBox().insideOf(bounds)) {
            if (parent == null) {
                Gdx.app.log("QUADTREE ERROR", "Entity cannot be added to the quadtree, it is out of the world!");
                e.setToBeDestroyed(true);
                return false;
            } else {
                return parent.add(e);
            }
        } else {
            if (!tryAddingToChildren(e)) { // We always try to push the entity to the lowest node in the tree
                // We have to add it in this node, and now we must check if we reached our limit
                entities.add(e); // Even if we were "full", we had to add the entity
                e.setQuadtree(this);
                if (entities.size() > CollisionQuadtree.MAX_ENTITIES && level < CollisionQuadtree.MAX_LEVELS && !hasChildren()) {
                    split();
                    Iterator<Entity> it = entities.iterator();
                    while (it.hasNext()) {
                        Entity entityToAddToChild = it.next();
                        if (tryAddingToChildren(entityToAddToChild)) {
                            it.remove();
                        }
                    }
                }
            }
            return true;
        }
    }

    // For now only removes the entity if it's present in the node, maybe it's enough
    public boolean remove(Entity e) {
        return entities.remove(e);
    }

    private boolean tryAddingToChildren(Entity e) {
        if (!hasChildren()) {
            return false;
        }
        AABB box = e.getBox();
        if (box.insideOf(topLeftTree.getBounds())) {
            topLeftTree.add(e);
            return true;
        } else if (box.insideOf(topRightTree.getBounds())) {
            topRightTree.add(e);
            return true;
        } else if (box.insideOf(bottomLeftTree.getBounds())) {
            bottomLeftTree.add(e);
            return true;
        } else if (box.insideOf(bottomRightTree.getBounds())) {
            bottomRightTree.add(e);
            return true;
        } else
            return false;
    }

    public ArrayList<Entity> getPossibleCollidingEntities(AABB box) {
        CollisionQuadtree root = this;
        while (root.getParent() != null)
            root = root.getParent();
        return root.collectPossibleCollidingEntities(box, new ArrayList<Entity>());
    }

    // First call should be on the root node
    private ArrayList<Entity> collectPossibleCollidingEntities(AABB box, ArrayList<Entity> list) {
        list.addAll(entities);
        if (hasChildren()) {
            if (box.overlapsWith(topLeftTree.getBounds()))
                topLeftTree.collectPossibleCollidingEntities(box, list);
            if (box.overlapsWith(topRightTree.getBounds()))
                topRightTree.collectPossibleCollidingEntities(box, list);
            if (box.overlapsWith(bottomLeftTree.getBounds()))
                bottomLeftTree.collectPossibleCollidingEntities(box, list);
            if (box.overlapsWith(bottomRightTree.getBounds()))
                bottomRightTree.collectPossibleCollidingEntities(box, list);
        }
        return list;
    }

    public boolean hasChildren() {
        return topLeftTree != null;
    }

    public int getLevel() {
        return level;
    }

    public Set<Entity> getEntities() {
        return entities;
    }

    public AABB getBounds() {
        return bounds;
    }

    public CollisionQuadtree getParent() {
        return parent;
    }

    public CollisionQuadtree getTopLeftTree() {
        return topLeftTree;
    }

    public CollisionQuadtree getTopRightTree() {
        return topRightTree;
    }

    public CollisionQuadtree getBottomLeftTree() {
        return bottomLeftTree;
    }

    public CollisionQuadtree getBottomRightTree() {
        return bottomRightTree;
    }

    public void debugRender(ShapeRenderer shapeRenderer) {
        shapeRenderer.rect((float)bounds.getX(), (float)bounds.getY(), (float)bounds.getWidth(), (float)bounds.getHeight());
        if (hasChildren()) {
            topLeftTree.debugRender(shapeRenderer);
            topRightTree.debugRender(shapeRenderer);
            bottomLeftTree.debugRender(shapeRenderer);
            bottomRightTree.debugRender(shapeRenderer);
        }
    }

    public int getTotalEntities() {
        if (!hasChildren())
            return entities.size();
        else
            return entities.size() + topLeftTree.getTotalEntities() + topRightTree.getTotalEntities() + bottomLeftTree.getTotalEntities() + bottomRightTree.getTotalEntities();
    }
}
