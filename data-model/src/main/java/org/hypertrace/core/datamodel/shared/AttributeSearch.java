package org.hypertrace.core.datamodel.shared;

import java.util.Optional;
import org.hypertrace.core.datamodel.Attributes;

public class AttributeSearch {

  public static Optional<String> searchForAttribute(Attributes attributes, String key) {
    if (attributes != null && attributes.getAttributeMap() != null) {
      var attributeMap = attributes.getAttributeMap();
      if (attributeMap.containsKey(key)) {
        return Optional.of(attributeMap.get(key).getValue());
      }
    }
    return Optional.empty();
  }

  public static Optional<String> searchForAttributeIgnoreKeyCase(
      Attributes attributes, String key) {
    if (attributes != null && attributes.getAttributeMap() != null) {
      Optional<String> attributeKey =
          attributes.getAttributeMap().keySet().stream().filter(key::equalsIgnoreCase).findFirst();

      if (attributeKey.isPresent()) {
        return Optional.of(attributes.getAttributeMap().get(attributeKey.get()).getValue());
      }
    }
    return Optional.empty();
  }
}
