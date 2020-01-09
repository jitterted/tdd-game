
package com.jitterted.tddgame.adapter.vue.masterview;

import com.jitterted.tddgame.adapter.vue.CardsView;
import lombok.Data;

@Data
public class PlayerWorldView {

  private final String name;
  private final CardsView hand;
  private final CardsView inPlay;

}
