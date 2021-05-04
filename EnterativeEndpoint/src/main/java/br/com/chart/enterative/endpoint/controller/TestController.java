package br.com.chart.enterative.endpoint.controller;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @author William Leite
 */
@RestController
public class TestController {
    @GetMapping(path = "test", produces = "application/json")
    @CrossOrigin(origins="http://localhost:4200")
    public TestVO test() {
        TestVO result = new TestVO();
        result.setTest("hey you");
        return result;
    }
}
