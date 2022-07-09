package com.iiplabs.nns.core.model;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

@Converter(autoApply = true)
public class NotificationStatusConverter implements AttributeConverter<NotificationStatus, Integer> {

  @Override
  public Integer convertToDatabaseColumn(NotificationStatus attribute) {
    Integer ret = null;
    if (attribute != null) {
      ret = attribute.getCode();
    }
    return ret;
  }

  @Override
  public NotificationStatus convertToEntityAttribute(Integer dbData) {
    NotificationStatus ret = null;
    if (dbData != null) {
      ret = NotificationStatus.findByCode(dbData);
    }
    return ret;
  }

}
