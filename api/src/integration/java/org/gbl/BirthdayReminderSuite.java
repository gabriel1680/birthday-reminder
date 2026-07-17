package org.gbl;

import org.gbl.common.service.json.GsonJsonServiceAdapter;
import org.junit.platform.suite.api.AfterSuite;
import org.junit.platform.suite.api.BeforeSuite;
import org.junit.platform.suite.api.IncludeClassNamePatterns;
import org.junit.platform.suite.api.SelectPackages;
import org.junit.platform.suite.api.Suite;
import org.junit.platform.suite.api.SuiteDisplayName;

@Suite
@SuiteDisplayName("Birthday Remainder API Integration Tests")
@SelectPackages("org.gbl")
@IncludeClassNamePatterns(".*_IT")
class BirthdayReminderSuite {

    private static BReminderAPI api;

    @BeforeSuite
    static void init() {
        api = new BReminderAPI(new GsonJsonServiceAdapter());
        api.start();
        api.awaitInitialization();
    }

    @AfterSuite
    static void tearDown() {
        api.stop();
    }
}
