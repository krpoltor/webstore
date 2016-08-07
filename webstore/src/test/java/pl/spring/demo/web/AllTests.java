package pl.spring.demo.web;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

import pl.spring.demo.web.controller.HomeControllerTest;
import pl.spring.demo.web.controller.ValidBookControllerTest;
import pl.spring.demo.web.rest.BookRestServiceTest;

@RunWith(Suite.class)
@SuiteClasses({ HomeControllerTest.class, ValidBookControllerTest.class, BookRestServiceTest.class })
public class AllTests {

}
