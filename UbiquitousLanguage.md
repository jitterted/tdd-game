# Glossary (aka our Ubiquitous Language of Domain Terms in the game)

* Location: this is the player's hex tile location on the board, identified by a unique descriptor (hi Value Object!) 

* Member: this is our own storage of people who are eligible to become a player in a game, with:
  * a "nickname" that is used for addressing them in non-game situations
    * the "nickname" becomes the default name within the game (can be overridden inside the game or when joining the game)
  * an "authName" that is the Security Principal.getName(), which we will get from external AuthN service (e.g., Kinde, GitHub, Google, etc.)
    * we use this to identify and lookup the Member in our MemberStore repository


Joining vs. Connecting vs. Observing

* **Joining** a game means that you have "signed up" for a game, but are not "connected" (this is a "domain" point of view)

* **Connecting** a game means that you have connected to the back-end via a WebSocket (nice to have: verify that you actually joined the game and should be allowed to be here!) and are _active_
  
* **Observing** a game means that you are not a **player** in the game, but can see the game by _connecting_ to it

**REMINDER:** goal of this implementation is NOT to enforce any rules (who can do what, when), but to provide a way for folks who are using a video conferencing tool (Zoom, etc.) to communicate "out of band" to deal with rules, who's turn it is, who left, etc.
