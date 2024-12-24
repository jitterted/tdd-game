/*
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with
 * the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on
 * an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations under the License.
 *
 * Copyright 2012-2024 the original author or authors.
 */
package dev.ted.tddgame.domain;

import org.assertj.core.api.Condition;
import org.assertj.core.error.BasicErrorMessageFactory;
import org.assertj.core.error.ErrorMessageFactory;

/**
 * Creates an error message indicating that an assertion that verifies elements of a group satisfies exactly n times a
 * {@code Condition} A group of elements can be a collection, an array.<br>
 *
 * @author Nicolas François
 * @author Mikhail Mazursky
 * @author Joel Costigliola
 */
public class EventsShouldHaveExactly extends BasicErrorMessageFactory {

  /**
   * Creates a new <code>{@link EventsShouldHaveExactly}</code>.
   * @param actual the actual value in the failed assertion.
   * @param expectedCount exactly expectedCount the condition should be verify.
   * @param condition the {@code Condition}.
   * @return the created {@code ErrorMessageFactory}.
   */
  public static ErrorMessageFactory eventsShouldHaveExactly(Object actual, int expectedCount, int actualCount, Condition<?> condition) {
    return new EventsShouldHaveExactly(actual, expectedCount, actualCount, condition);
  }

  private EventsShouldHaveExactly(Object actual, int expectedCount, int actualCount, Condition<?> condition) {
    super("%nFound %s occurrences where %s, but expected %s in:%n  %s",
          actualCount, condition, expectedCount, actual);
  }
}
