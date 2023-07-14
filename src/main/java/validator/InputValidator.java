package validator;


import errorhandling.ErrorConst;
import errorhandling.ValidationException;
import io.micrometer.common.util.StringUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.lang.reflect.Field;

public class InputValidator implements Validator{


    @Override
    public void validate(Object objToValidate) {

        Class<?> concreteClass = objToValidate.getClass();
        Field[] fieldsToValidate = concreteClass.getDeclaredFields();
        for (Field field: fieldsToValidate) {
            try {
                Field fieldToCheck = concreteClass.getDeclaredField(field.getName());
                fieldToCheck.setAccessible(true);
                if (fieldToCheck.getType().equals(String.class)) {
                    String value = (String) fieldToCheck.get(objToValidate);
                    boolean isBlank = StringUtils.isBlank(value);
                    boolean isStartWithDigit = Character.isDigit(value.charAt(0));
                    if (isBlank){
                        throw new ValidationException(value + ":" + ErrorConst.NO_SUCH_FILED);
                    }
                    if (isStartWithDigit){
                        throw new ValidationException(value + ":" + ErrorConst.FIELD_START_WITH_DIGIT);
                    }
                }
            }
            catch (NoSuchFieldException e){
                throw new ValidationException(field.getName()+ ":" +ErrorConst.NO_SUCH_FILED);
            } catch (IllegalAccessException e) {
                throw new ValidationException(ErrorConst.FIELD_NOT_ACCESSIBLE);
            }
        }
    }
}
