package edu.ncsu.csc.CoffeeMaker.common;

import static org.junit.jupiter.api.Assertions.fail;

import org.junit.jupiter.api.Test;

import edu.ncsu.csc.CoffeeMaker.forms.CustomerForm;
import edu.ncsu.csc.CoffeeMaker.util.ValidationUtil;

class ValidationUtilTest {

    @Test
    public void testSatisfactionSurvey () {

        final CustomerForm customerForm = new CustomerForm();
        customerForm.setUsername( "username" );

        try {
            ValidationUtil.validate( customerForm );
        }

        catch ( final IllegalArgumentException e ) {
            fail( "This should have worked." );
        }
    }

}
