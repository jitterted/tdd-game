# Stories

* [ ] Start Hex Board implementation
    * [ ] Game start: both players are on the first hex, able to query where a player is
    * [ ] When event for EXIT condition for current hex is received, move player to next hex 
    * [ ]

* [ ] Turn tracker
    * [ ] Once THREE (3) cards are PLAYED or DISCARDED, that player's turn is over, except...
    * [ ] ...except for TECH DEBT card situations


* [X] Discard from Play area: cards go to Deck Discard Pile
* [X] Multiple players can play
    * [X] Identify myself with my Name (get a player ID)
    * [X] Display game from player's POV
        * [X] My cards AND Opponent's In-Play
    * [X] Actions happen with player's ID
    * [X] Playing attack card ("Code Bloat", "Can't Assert That") goes to OPPONENT'S In-Play area
* [X] Immediate use card: Refactor

* [X] Run Tests Results card: show modal of card from Results deck
    * [X] TestResultsCard
    * [X] TestResultsCardFactory
    * [X] TestResultsDeck
    * [X] Controller draws card from TestResultsDeck, returns it, then discards it to the TestResultsDeck
    * [X] Test Result Card Vue component to display the card based on its title
    

~~* [ ] Move to RSocket for more real-time interaction~~


# Completed

* Playing deck consists of (total = 63):
    * Write Code (18)
    * Code Smaller (18)
    * Predict (18)
    * Can't Assert (2)
    * Code Bloat (3)
    * Refactor (4)

---

[X] 1. Shuffle:

        Two times shuffles happen:
        
        1. Initial deck (start of game)
        2. When draw deck has 0 cards, shuffle cards from Discard Pile
        
        Rule: When Draw Deck is empty, shuffle cards from Discard Pile into Draw Deck 

[X] 1. Discard pile

[X] 1. Re-shuffle deck from discard when deck is empty
