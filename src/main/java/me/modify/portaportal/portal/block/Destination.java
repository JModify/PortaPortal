package me.modify.portaportal.portal.block;

public enum Destination {

    SPAWN (0),
    BED (1),
    HOME (2),
    CUSTOM (3);

    private int index;

    Destination (int index) {
        this.index = index;
    }

    public int getIndex () {
        return index;
    }

    public Destination getByIndex (int index) {
        for (Destination destination : values()) {
            if (destination.getIndex() == index) {
                return destination;
            }
        }
        return SPAWN;
    }

    public Destination next() {
        int nextIndex = index++;
        if (nextIndex >= values().length) {
            nextIndex = 0;
        }

        return getByIndex(nextIndex);
    }
}
