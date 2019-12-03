# Game API

/api/game?player=1

"game": {
    "hand": {"cards": 
              [
                {"title": "refactor", "id": 9},
                {"title": "predict", "id": 10}
              ]
            },
    "in_play": {"cards": []},
    "opponent_in_play": {"cards": []},
    "score": { "name": "Ted", 
               "passing_tests": 0,
               "risk_level": 0
             },
    "opponent_score": {"passing_tests": 0, "risk_level": 0}
}

## Game:

* 2 Players, each player has
    * Passing Tests (Score)
    * Risk Level
    * Cards in Hand
    * Cards in Play
* Playing Card Deck
* Test Results Deck

1. Start Game: creates deck, players fill their hand
    1. Results in: Playing deck initialized with cards
    1. Each of 2 players draw 5 cards
        * Player 1 has 5 cards
        * Player 2 has 5 cards
        * Deck has 53 cards remaining
