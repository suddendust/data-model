package org.hypertrace.core.datamodel.shared;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import javax.annotation.Nonnull;
import org.apache.avro.specific.SpecificRecord;
import org.apache.avro.specific.SpecificRecordBuilderBase;
import org.apache.commons.lang3.tuple.Pair;

/**
 * Utility class to instantiate a new avro builder in a fast and efficient way. Default Avro
 * implementation has a perf issue due to uncached reflection based implementation. For more
 * details, https://issues.apache.org/jira/browse/AVRO-3048
 *
 * <p>This avro perf bug is fixed in unreleased avro version (0.11.x). We can get rid of this
 * utility class once we migrate to Avro 0.11.x or above
 */
@SuppressWarnings({"rawtypes", "unchecked"})
public class AvroBuilderCache {
  private static final Map<
          Class<SpecificRecordBuilderBase>, Pair<Method, SpecificRecordBuilderBase>>
      AVRO_BUILDER_CACHE = new ConcurrentHashMap<>();

  public static <T extends SpecificRecordBuilderBase> T fastNewBuilder(
      Class<T> specificAvroBuilderClass) {
    try {
      Pair<Method, SpecificRecordBuilderBase> pair =
          AVRO_BUILDER_CACHE.computeIfAbsent(
              (Class<SpecificRecordBuilderBase>) specificAvroBuilderClass,
              AvroBuilderCache::newBuilderFor);
      return (T) pair.getLeft().invoke(null, pair.getRight());
    } catch (Exception e) {
      throw new RuntimeException(
          "Unable to instantiate new builder for: " + specificAvroBuilderClass.getName(), e);
    }
  }

  private static Pair<Method, SpecificRecordBuilderBase> newBuilderFor(
      @Nonnull Class<SpecificRecordBuilderBase> specificAvroBuilderClass) {
    try {
      Class<SpecificRecord> specificAvroClass =
          (Class<SpecificRecord>) specificAvroBuilderClass.getEnclosingClass();
      Method newBuilderMethod = findBuilderMethod(specificAvroClass);

      final Constructor declaredConstructor = specificAvroBuilderClass.getDeclaredConstructor();
      declaredConstructor.setAccessible(true);
      SpecificRecordBuilderBase builderModel =
          (SpecificRecordBuilderBase) declaredConstructor.newInstance();

      return Pair.of(newBuilderMethod, builderModel);
    } catch (Exception e) {
      throw new RuntimeException(
          "Unable to instantiate the model builder: " + specificAvroBuilderClass.getName(), e);
    }
  }

  private static Method findBuilderMethod(Class specificAvroClass) {
    return Arrays.stream(specificAvroClass.getDeclaredMethods())
        .filter(method -> method.getName().equals("newBuilder"))
        .filter(method -> method.getParameterTypes().length == 1)
        .filter(
            method ->
                SpecificRecordBuilderBase.class.isAssignableFrom(method.getParameterTypes()[0]))
        .findFirst()
        .orElseThrow(IllegalArgumentException::new);
  }
}
