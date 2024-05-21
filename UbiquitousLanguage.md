# Glossary (aka our Ubiquitous Language of Domain Terms in the game)

* Location: this is the player's hex tile location on the board, identified by a unique descriptor (hi Value Object!) 

* Member: this is our own storage of people who are eligible to become a player in a game, with:
  * a "nickname" that is used for addressing them in non-game situations
    * the "nickname" becomes the default name within the game (can be overridden inside the game or when joining the game)
  * an "authName" that is the Security Principal.getName(), which we will get from external AuthN service (e.g., Kinde, GitHub, Google, etc.)
    * we use this to identify and lookup the Member in our MemberStore repository

