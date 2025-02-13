package dev.ted.tddgame.application.port;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.databind.annotation.JsonTypeIdResolver;

@JsonTypeInfo(
        use = JsonTypeInfo.Id.CUSTOM,
        include = JsonTypeInfo.As.PROPERTY
)
@JsonTypeIdResolver(CardTypeResolver.class)
interface CardMixIn {}
