package dev.ted.tddgame.domain;

public class Workspace {
    private final WorkspaceId id;
    protected final HexTile currentHexTile;

    /**
     * Assign the Workspace ID from the Player's ID
     * (This is temporary: it should have its own ID)
     * @param playerId the "owner" of this workspace
     */
    public Workspace(PlayerId playerId) {
        id = new WorkspaceId(playerId.id());
        currentHexTile = HexTile.WHAT_SHOULD_IT_DO;
    }

    public WorkspaceId id() {
        return id;
    }

    public HexTile currentHexTile() {
        return currentHexTile;
    }
}
