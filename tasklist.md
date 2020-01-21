# Tasks

## Refactoring
* [ ] Combine Hand and InPlay into a single Class (called ??? CardSet?) with two instances
    * [ ] Change ".count() < 5" to ".canAdd()" (or "hasRoom") when looping to add cards
* [ ] Move PlayerHandTest tests to HandTests and refactor ("object envy" code smell)


## Deployment

* [ ] Properly build with Maven that combines front- and back-end code into single deployable JAR
* [ ] Deploy onto Heroku
* [ ] Use CloudFlare to point tdd.cards to Heroku
* [ ] Login page with an access code?


## Display
~~* [ ] Center the cards within their row~~
* [X] Display user's name coming from back-end
* [X] No "Play" option for card in "In-Play" area
* [X] Prevent card selection for opponent-in-play cards
* [X] Keyboard actions for (D)iscard and (P)lay
* [ ] Fix layout problem if player name is too long (e.g., "FlavCreations")
* [ ] Move Playing Card IDs to lower-right and make smaller
* [ ] Clean up display of scoring (colors, etc.) and die image (make it look more like a die)
* [ ] Single click discard of In-Play area
* [ ] Auto-draw upon play card or discard card
* [ ] Ensure can't move card past the left/right edge 

### Requires WebSockets
* [ ] When "Run Test" card is drawn, show on both player's screen
    [X] 1. FE: POST to draw the "run test" card -> 204 (no content), with, well, no body content
    [X] 1a. BE: Only return 204
    [X] 2. BE: testResultsCardDeck.draw -> game.drawnTestResultsCard <-- also need the player ID for who drew
    [X] 3. BE: send WS message on /topic/testresultcard: { action = "TestResultsCardDrawn", cardId: 123, PlayerId who drew } 
    [X] 4. FE: (recv msg) show modal with that card
    [X] 5. FE: =button click= POST to discard "drawn test results card" -> 204
    [X] 6. BE: testResultsCardDeck.discard(the card)
    [X] 7. BE: game.drawnTestResultsCard = null
    [X] 8. BE: send WS message { action: "TestResultsCardDiscarded", playerId: id }
    9. FE: Upon receipt of WS discarded message, hide the modal  
    10. FE: if PlayerIdWhoDrew == Current Player, then show dismiss modal button
* [ ] Score & Risk tracking - propagate to other players via server
    * [X] Score
    * [ ] Risk-level
* [ ] Propagate card view order to server 

### Nice to Have
* [ ] Track player action count
* [ ] Drag-n-drop reordering of display of cards
* [ ] Animate new card appearing anywhere (esp. in hand)
* [ ] Animate card being moved from Hand to In-Play
* [ ] Animate play of Refactor card, which is discard-on-play
* [ ] Prevent multiple selection: only single select of cards
* [ ] show deck status with # of cards in discard pile and in draw pile

## User Actions (Vue)
* [X] Highlight card for action: (D)iscard or (P)lay
* [X] Run Test: Display modal Test Results
* [X] Increment/decrement Risk Level
* [X] Increment Passing Tests (Score)
* [X] Roll die
* [X] Draw card

## Game (Spring Boot)
* [X] Run Tests: card is select from Test Results pile
    * [X] Shuffle Test Results cards
* [X] Draw to full hand
* [X] Draw a single new card into hand
* [X] Shuffle Draw Pile
* [X] Discard and Play actions (per card)
    * [X] Click on a card
    * [X] Display choices available: Discard or Play 
    * [X] Handle Discard (only from Hand)
    * [X] Handle Play

## Add Player Support
* [X] "log in" to Game system: takes a player name, returns player number and a base url (e.g., /api/game/player/0)
* [X] Handle all player actions and direct to correct API endpoint (player ID parameter)


# Game Master View

This could also be used as a "watch the game" view either as "god" (view everything),
or only see cards in-play. 

## Required

* [X] View All Players cards: in-hand & in-play
* [X] Current (if any) Test Results Drawn Card
* [X] For both Playing Card and Test Results Decks
    * [X] Summary/Size of draw and discard piles


## Nice to Have
* [ ] Able to reset game back to starting point -- from Game Master View
* [ ] For both Playing Card and Test Results Decks
    * [ ] Draw and discard piles in detail
* [ ] Ability to arbitrarily move cards around between players and play areas
* [ ] Undo actions

----

# Deferred

## Display
* [ ] Highlight which player's turn
* [ ] Show empty outlines of where cards will be displayed


# Completed

## Vue-ify
* [X] Create Playing card component
    * [X] Templatize existing HTML/CSS
    * [X] Parameterize the card type to color & title background color:
          **Predict** card maps to `background = yellow` + `title = light-yellow`
* [X] Scoring (left-side) component

