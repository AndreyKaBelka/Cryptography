package com.andreyka.crypto.api;

import com.google.common.base.Stopwatch;
import lombok.AllArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.extension.AfterAllCallback;
import org.junit.jupiter.api.extension.AfterTestExecutionCallback;
import org.junit.jupiter.api.extension.BeforeTestExecutionCallback;
import org.junit.jupiter.api.extension.ExtensionContext;

import java.util.HashMap;
import java.util.Map;
import java.util.StringJoiner;
import java.util.concurrent.TimeUnit;

@Slf4j
public class TimingExtension implements BeforeTestExecutionCallback, AfterTestExecutionCallback, AfterAllCallback {
    private final Stopwatch stopwatch = Stopwatch.createUnstarted();
    private final Map<String, Pair> timeOfTests = new HashMap<>();

    @Override
    public void afterTestExecution(ExtensionContext context) throws Exception {
        stopwatch.stop();

        String testName = context.getRequiredTestMethod().getName();
        long elapsed = stopwatch.elapsed(TimeUnit.NANOSECONDS);
        timeOfTests.compute(testName, (key, pair) -> {
            if (pair == null) {
                return new Pair(elapsed, 1);
            } else {
                pair.addTime(elapsed);
                pair.increaseTimes();
                return pair;
            }
        });
        stopwatch.reset();
    }

    @Override
    public void beforeTestExecution(ExtensionContext context) throws Exception {
        stopwatch.start();
    }

    @Override
    public void afterAll(ExtensionContext context) throws Exception {
        timeOfTests.entrySet().forEach(System.out::println);
    }

    @AllArgsConstructor
    private static class Pair {
        private long time;
        private int times;

        void addTime(long time) {
            this.time += time;
        }

        void increaseTimes() {
            this.times++;
        }

        @Override
        public String toString() {
            return new StringJoiner(", ", "(", ")")
                .add("time = " + time / times)
                .toString();
        }
    }
}
