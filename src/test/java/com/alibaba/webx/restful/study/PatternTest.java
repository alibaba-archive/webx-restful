package com.alibaba.webx.restful.study;

import java.util.regex.Pattern;

import junit.framework.TestCase;

public class PatternTest extends TestCase {

    public void test_0() throws Exception {
        Pattern pattern = Pattern.compile("/helloworld(/.*)?");

        String path = "/helloworld/now";
        
        for (int i = 0; i < 10; ++i) {
            long startMillis = System.currentTimeMillis();
            perf1(pattern, path);
            long millis = System.currentTimeMillis() - startMillis;
            
            System.out.println(millis);
        }
    }

    private boolean perf(Pattern pattern, String path) {
        for (int i = 0; i < 1000 * 1000 * 10; ++i) {
//             pattern.matcher(path);
            boolean result = path.startsWith("/helloworld");
            if (!result) {
                return false;
            }
        }
        return true;
    }

    
    private boolean perf1(Pattern pattern, String path) {
        for (int i = 0; i < 1000 * 1000 * 10; ++i) {
//             pattern.matcher(path);
            boolean result = pattern.matcher(path) != null;
            if (!result) {
                return false;
            }
        }
        return true;
    }
}
