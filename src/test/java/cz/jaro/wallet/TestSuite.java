package cz.jaro.wallet;

import org.junit.platform.suite.api.SelectPackages;
import org.junit.platform.suite.api.Suite;
import org.junit.platform.suite.api.SuiteDisplayName;

@Suite
@SuiteDisplayName("All Tests Suite")
@SelectPackages("cz.jaro.transaction")
public class TestSuite {
}
