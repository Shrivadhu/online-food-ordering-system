package com.foodorder.pattern.prototype;

/**
 * PROTOTYPE PATTERN (Creational)
 * Defines the cloning contract for objects that support prototype-based creation.
 * Allows creating new objects by copying an existing object (prototype)
 * instead of creating from scratch.
 */
public interface Cloneable<T> {
    T cloneObject();
}
