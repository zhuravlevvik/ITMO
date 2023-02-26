package ru.itmo.wp.form.validator;

import com.google.common.base.Strings;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import ru.itmo.wp.form.CommentCredentials;

@Component
public class CommentCredentialsValidator implements Validator {

    public boolean supports(Class<?> clazz) {
        return CommentCredentials.class.equals(clazz);
    }

    public void validate(Object target, Errors errors) {
        if (!errors.hasErrors()) {
            CommentCredentials postCommentForm = (CommentCredentials) target;
            if (Strings.isNullOrEmpty(postCommentForm.getText().trim())) {
                errors.rejectValue("text", "text.invalid-text", "Text should be meaningful");
            }
        }
    }
}
