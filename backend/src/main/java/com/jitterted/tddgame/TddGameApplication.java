package com.jitterted.tddgame;

import com.jitterted.tddgame.domain.GameService;
import com.jitterted.tddgame.domain.PlayerFactory;
import com.jitterted.tddgame.domain.TwoPlayerGameService;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.core.task.TaskExecutor;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

@SpringBootApplication
public class TddGameApplication {

	public static void main(String[] args) {
		SpringApplication.run(TddGameApplication.class, args);
	}

	@Bean
  public GameService defaultTwoPlayerGameService() {
    return new TwoPlayerGameService(new PlayerFactory());
  }

  @Bean
  @Primary
  public TaskExecutor threadPoolTaskExecutor() {
    ThreadPoolTaskExecutor threadPoolTaskExecutor = new ThreadPoolTaskExecutor();
    threadPoolTaskExecutor.afterPropertiesSet();
    return threadPoolTaskExecutor;
  }

}
