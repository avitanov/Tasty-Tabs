package finki.db.tasty_tabs.aspect.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Marks a method that requires the employee to be on an active shift.
 * The aspect associated with this annotation will verify the employee's status
 * before allowing the method to execute.
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface CheckOnDuty {
    /**
     * Specifies the name of the method parameter that holds the employee's ID.
     * Defaults to "employeeId". This parameter must be of type Long.
     * @return The parameter name for the employee ID.
     */
    String employeeIdParamName() default "employeeId";
}