package com.jitterted.tddgame;

import com.jitterted.tddgame.domain.GameService;
import com.jitterted.tddgame.domain.PlayerFactory;
import com.jitterted.tddgame.domain.TwoPlayerGameService;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class TddGameApplication {

	public static void main(String[] args) {
		SpringApplication.run(TddGameApplication.class, args);
	}

	@Bean
  public GameService defaultTwoPlayerGameService() {
    return new TwoPlayerGameService(new PlayerFactory());
  }

}
