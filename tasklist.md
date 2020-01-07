# Tasks

## Refactoring
* [ ] Combine Hand and InPlay into a single Class (called ??? CardSet?) with two instances
    * [ ] Change ".count() < 5" to ".canAdd()" (or "hasRoom") when looping to add cards
* [ ] Move PlayerHandTest tests to HandTests and refactor ("object envy" code smell)

## BUGS
* [X] When cards are moved, the cards don't visibly change order until the data is refreshed
      Somewhere we're missing the propagation of changes to the ordering array  

## Display
~~* [ ] Center the cards within their row~~
* [X] Display user's name coming from back-end
* [X] No "Play" option for card in "In-Play" area
* [ ] Prevent card selection for opponent-in-play cards

### Nice to Have
* [ ] Keyboard actions for (D)iscard and (P)lay
* [ ] Drag-n-drop reordering of display of cards
* [ ] Animate new card appearing anywhere (esp. in hand)
* [ ] Prevent multiple selection: only single select of cards
* [ ] show deck status with # of cards in discard pile and in draw pile
* [ ] Animate card being moved from Hand to In-Play

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

* [ ] View Deck: draw and discard piles in detail
* [ ] View Test Results Deck
* [ ] View All Players' Hands

----

# Deferred

## Display
* [ ] Highlight which player's turn
* [ ] Show empty outlines of where cards will be displayed 
      (this will help keep the display evenly divided even when no cards are in play)


# Completed

## Vue-ify
* [X] Create Playing card component
    * [X] Templatize existing HTML/CSS
    * [X] Parameterize the card type to color & title background color:
          **Predict** card maps to `background = yellow` + `title = light-yellow`
* [X] Scoring (left-side) component

