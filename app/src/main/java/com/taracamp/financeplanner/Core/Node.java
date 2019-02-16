package com.taracamp.financeplanner.Core;

public interface Node {
    boolean save(Object object); // save or update object in database.
    boolean remove(Object object); // remove object from database.
}
