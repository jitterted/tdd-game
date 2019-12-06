# Tasks

## Vue-ify
* [X] Create Playing card component
    * [X] Templatize existing HTML/CSS
    * [X] Parameterize the card type to color & title background color:
          **Predict** card maps to `background = yellow` + `title = light-yellow`
* [X] Scoring (left-side) component

## Display
~~* [ ] Center the cards within their row~~

## User Actions (Vue)
* [X] Highlight card for action: (D)iscard or (P)lay
* [X] Run Test: Display modal Test Results
* [X] Increment/decrement Risk Level
* [X] Increment Passing Tests (Score)
* [X] Roll die

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

# Deferred

## Display
* [ ] Highlight which player's turn
* [ ] Show empty outlines of where cards will be displayed 
      (this will help keep the display evenly divided even when no cards are in play)

## Game Master View

This could also be used as a "watch the game" view either as "god" (view everything),
or only see cards in-play. 

* [ ] View Deck: draw and discard piles
* [ ] View Test Results Deck
* [ ] View All Players' Hands
