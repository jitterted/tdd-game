package dev.ted.tddgame.domain;

public class Workspace {
    private final WorkspaceId id;
    protected final HexTile currentHexTile;

    /**
     * Assign the Workspace ID from the Player's ID
     * @param player the player associated with this workspace
     */
    public Workspace(Player player) {
        id = new WorkspaceId(player.id().id());
        currentHexTile = HexTile.WHAT_SHOULD_IT_DO;
    }

    public WorkspaceId id() {
        return id;
    }

    public HexTile currentHexTile() {
        return currentHexTile;
    }
}
