package dev.ted.tddgame.application.port;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.databind.DatabindContext;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.jsontype.impl.TypeIdResolverBase;
import dev.ted.tddgame.domain.ActionCard;
import dev.ted.tddgame.domain.TestResultsCard;

// NOTE: this will go into the outbound repository adapter when we create it
public class CardTypeResolver extends TypeIdResolverBase {
    @Override
    public String idFromValue(Object value) {
        return switch (value.getClass().getSimpleName()) {
            case "RegularCard", "TechNeglectCard" -> "ActionCard";
            case "TestResultsCard" -> "TestResultsCard";
            default ->
                    throw new IllegalArgumentException("Unknown type: " + value.getClass().getSimpleName());
        };
    }

    @Override
    public String idFromValueAndType(Object value, Class<?> suggestedType) {
        return idFromValue(value);
    }

    @Override
    public JavaType typeFromId(DatabindContext context, String id) {
        var clazz = switch (id) {
            case "ActionCard" -> ActionCard.class;
            case "TestResultsCard" -> TestResultsCard.class;
            default -> throw new IllegalArgumentException("Unknown type: " + id);
        };
        return context.constructType(clazz);
    }

    @Override
    public JsonTypeInfo.Id getMechanism() {
        return JsonTypeInfo.Id.NAME;
    }
}
