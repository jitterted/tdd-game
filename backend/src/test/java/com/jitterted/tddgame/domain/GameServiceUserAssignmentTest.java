package com.jitterted.tddgame.domain;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.*;

class GameServiceUserAssignmentTest {

  @Test
  public void withNewGameConnectUserResultsInPlayerAssignedToUser() throws Exception {
    GameService gameService = new TwoPlayerGameService(new PlayerFactory());
    User user = new User("new user");

    Player assignedPlayer = gameService.assignNextAvailablePlayerToUser(user);

    assertThat(assignedPlayer.assignedUser())
      .isEqualTo(user);
  }

  @Test
  public void assignUserIsAssignedToUnassignedPlayer() throws Exception {
    User assignedUser = new User("assigned user");
    Player playerWithUser = new Player(PlayerId.of(1));
    playerWithUser.assignUser(assignedUser);
    Player playerWithoutUser = new Player(PlayerId.of(2));
    PlayerFactory playerFactory = new PlayerFactory() {
      @Override
      public List<Player> createTwoPlayers() {
        return List.of(playerWithUser, playerWithoutUser);
      }
    };
    GameService gameService = new TwoPlayerGameService(playerFactory);

    User toBeAssigned = new User("to be assigned");

    Player assignedPlayer = gameService.assignNextAvailablePlayerToUser(toBeAssigned);

    assertThat(assignedPlayer)
      .isEqualTo(playerWithoutUser);
  }

  @Test
  public void attemptToAssignThreeUsersWithTwoPlayersThrowsException() throws Exception {
    GameService gameService = new TwoPlayerGameService(new PlayerFactory());
    User user = new User("user");

    gameService.assignNextAvailablePlayerToUser(user);
    gameService.assignNextAvailablePlayerToUser(user);

    assertThatThrownBy(() -> {
      gameService.assignNextAvailablePlayerToUser(user);
    }).isInstanceOf(IllegalStateException.class);

  }

}
