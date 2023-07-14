package validator;

import org.springframework.http.ResponseEntity;

public interface Validator {
    public void validate(Object obj);

}
