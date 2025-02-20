package cz.jaro.homework;

import org.junit.platform.suite.api.SelectPackages;
import org.junit.platform.suite.api.Suite;
import org.junit.platform.suite.api.SuiteDisplayName;

@Suite
@SuiteDisplayName("All Tests Suite")
@SelectPackages("cz.jaro.homework")
public class TestSuite {
}
