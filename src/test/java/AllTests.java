import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

import service.MailServiceTest;
import validator.MailValidatorTest;

@RunWith(Suite.class)
@SuiteClasses({ MailServiceTest.class, MailValidatorTest.class })
public class AllTests {

}
