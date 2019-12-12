# Tasks

## Refactoring
* [ ] Move PlayerHandTest tests to HandTests and refactor ("object envy" code smell)

## Vue-ify
* [X] Create Playing card component
    * [X] Templatize existing HTML/CSS
    * [X] Parameterize the card type to color & title background color:
          **Predict** card maps to `background = yellow` + `title = light-yellow`
* [X] Scoring (left-side) component

## Display
~~* [ ] Center the cards within their row~~
* [ ] Nice-to-have: show deck status with # of cards in discard pile and in draw pile

## User Actions (Vue)
* [X] Highlight card for action: (D)iscard or (P)lay
* [X] Run Test: Display modal Test Results
* [X] Increment/decrement Risk Level
* [X] Increment Passing Tests (Score)
* [X] Roll die
* [ ] Draw card ** IN PROGRESS

## Game (Spring Boot)
* [X] Run Tests: card is select from Test Results pile
    * [X] Shuffle Test Results cards
* [X] Draw to full hand
* [X] Shuffle Draw Pile
* [ ] Discard and Play actions (per card)
    * [X] Click on a card
    * [X] Display choices available: Discard or Play 
    * [X] Handle Discard (only from Hand)
    * [ ] Handle Play

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

