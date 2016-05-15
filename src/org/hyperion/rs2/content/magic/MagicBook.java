package org.hyperion.rs2.content.magic;

/**
 * Represents the different types of magic books.
 * @author Stephen Andrews
 */
public enum MagicBook {
    ANCIENT,
    MODERN;

    /**
     * Gets the interface id of the magic book.
     * @return The interface id.
     */
    public int getInterfaceId() {
        return this == ANCIENT ? MagicConstants.ANCIENT_BOOK : MagicConstants.MODERN_BOOK;
    }
}
