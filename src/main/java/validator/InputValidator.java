package validator;
/**
 * This class implements the Validator interface and perform very basic validation on user input, using reflection.
 */

import errorhandling.ErrorConst;
import errorhandling.ValidationException;
import io.micrometer.common.util.StringUtils;
import java.lang.reflect.Field;

public class InputValidator implements Validator{


    /**
     * Fetch the concrete class and the fields of the tested object.
     * Currently checking only String fields. Perform the following basics checks:
     * 1. if no value (e.g empty field)
     * 2. if the value start with digit.
     * @param objToValidate
     */
    @Override
    public void validate(Object objToValidate) {

        if (objToValidate == null){
            throw new ValidationException(ErrorConst.OBJECT_IS_NULL);
        }
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
                        throw new ValidationException(value + ":" + ErrorConst.EMPTY_FIELD_VALUE);
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
