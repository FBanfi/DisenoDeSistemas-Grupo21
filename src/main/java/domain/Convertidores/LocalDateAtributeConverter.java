package domain.Convertidores;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;
import java.time.LocalDate;
import java.sql.Date;

//Porque el tipo de dato LocalDate no existe en la base de datos
// entonces lo necesito cambiar a tipo de dato Date
@Converter(autoApply = true)
public class LocalDateAtributeConverter implements AttributeConverter<LocalDate, Date> {

  @Override
  public Date convertToDatabaseColumn(LocalDate locDate) {
    return locDate == null ? null : Date.valueOf(locDate);
  }

  @Override
  public LocalDate convertToEntityAttribute(Date sqlDate) {
    return sqlDate == null ? null : sqlDate.toLocalDate();
  }

}
