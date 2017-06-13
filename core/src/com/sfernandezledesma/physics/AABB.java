/*
 * AABB.java
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


public class AABB {
    private double x;
    private double y;
    private double width;
    private double height;

    public AABB(AABB other) {
        x = other.getX();
        y = other.getY();
        width = other.getWidth();
        height = other.getHeight();
    }

    public AABB(double x, double y, double width, double height) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
    }

    public void syncPositionWith(AABB other) {
        x = other.x;
        y = other.y;
    }

    public void setPosition(double x, double y) {
        this.x = x;
        this.y = y;
    }

    public void translateX(double dx) {
        x += dx;
    }

    public void translateY(double dy) {
        y += dy;
    }

    public void setX(double x) {
        this.x = x;
    }

    public void setY(double y) {
        this.y = y;
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public double getWidth() {
        return width;
    }

    public double getHeight() {
        return height;
    }

    public double leftSideX() {
        return x;
    }

    public double rightSideX() {
        return x + width;
    }

    public double topSideY() {
        return y + height;
    }

    public double bottomSideY() {
        return y;
    }

    public double centerX() {
        return x + width / 2;
    }

    public double centerY() {
        return y + height / 2;
    }

    public boolean overlapsWith(AABB otherAABB) {
        if (rightSideX() <= otherAABB.leftSideX())
            return false;
        if (leftSideX() >= otherAABB.rightSideX())
            return false;
        if (topSideY() <= otherAABB.bottomSideY())
            return false;
        if (bottomSideY() >= otherAABB.topSideY())
            return false;

        return true;
    }

    public boolean insideOf(AABB otherAABB) {
        return (topSideY() <= otherAABB.topSideY() && bottomSideY() >= otherAABB.bottomSideY() && leftSideX() >= otherAABB.leftSideX() && rightSideX() <= otherAABB.rightSideX());
    }
}
