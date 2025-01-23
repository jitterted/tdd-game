package dev.ted.tddgame.domain;

public class Workspace {
    private final WorkspaceId id;

    /**
     * Assign the Workspace ID from the Player's ID
     * @param player the player associated with this workspace
     */
    public Workspace(Player player) {
        id = new WorkspaceId(player.id().id());
    }

    public WorkspaceId id() {
        return id;
    }
}
