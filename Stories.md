# Stories

* Playing deck consists of (total = 63):
    * Write Code (18)
    * Code Smaller (18)
    * Predict (18)
    * Can't Assert (2)
    * Code Bloat (3)
    * Refactor (4)

---

1. Start Game: creates deck, players fill their hand
    1. Results in: Playing deck initialized with cards
    1. Each of 2 players draw 5 cards
        * Player 1 has 5 cards
        * Player 2 has 5 cards
        * Deck has 53 cards remaining

1. Shuffle

    Two times shuffles happen:
    
    1. Initial deck (start of game)
    2. When draw deck has 0 cards, shuffle cards from Discard Pile
    
    Rule: When Draw Deck is empty, shuffle cards from Discard Pile into Draw Deck 

1. Discard pile

1. Re-shuffle deck from discard when deck is empty
