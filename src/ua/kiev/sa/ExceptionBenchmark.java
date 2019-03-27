package ua.kiev.sa;

import org.openjdk.jmh.annotations.*;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.RunnerException;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;

import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;

/*
Benchmark                                                            Mode  Cnt  Score    Error  Units
        ExceptionBenchmark.customExceptionWithStackTracePrinting     avgt   20  1,534 ±  0,239  ms/op
        ExceptionBenchmark.customExceptionWithStackTraceProcessing   avgt   20  0,002 ±  0,001  ms/op
        ExceptionBenchmark.custonExceptionWithoutStackTrace          avgt   20  0,001 ±  0,001  ms/op
        ExceptionBenchmark.runtimeExceptionWithStackTracePrinting    avgt   20  1,323 ±  0,244  ms/op
        ExceptionBenchmark.runtimeExceptionWithStackTraceProcessing  avgt   20  0,002 ±  0,001  ms/op
*/
@State(Scope.Benchmark)
@OutputTimeUnit(TimeUnit.MILLISECONDS)
@Warmup(iterations = 5)
@BenchmarkMode(Mode.AverageTime)
public class ExceptionBenchmark {

    public static void main(String[] args) throws RunnerException {

        Options opt = new OptionsBuilder()
                .include(ExceptionBenchmark.class.getSimpleName())
                .forks(1)
                .build();

        new Runner(opt).run();
    }

    @Benchmark
    public String customExceptionWithStackTraceProcessing() {
        String generatedValue = String.valueOf(ThreadLocalRandom.current().nextInt());
        try {
            throw new MyException(generatedValue);
        } catch (MyException e) {
            e.fillInStackTrace();
        }
        return generatedValue;
    }

    @Benchmark
    public String customExceptionWithStackTracePrinting() {
        String generatedValue = String.valueOf(ThreadLocalRandom.current().nextInt());
        try {
            throw new MyException(generatedValue);
        } catch (MyException e) {
            e.printStackTrace();
        }
        return generatedValue;
    }

    @Benchmark
    public String runtimeExceptionWithStackTraceProcessing() {
        String generatedValue = String.valueOf(ThreadLocalRandom.current().nextInt());
        try {
            throw new RuntimeException(generatedValue);
        } catch (RuntimeException e) {
            e.fillInStackTrace();
        }
        return generatedValue;
    }

    @Benchmark
    public String runtimeExceptionWithStackTracePrinting() {
        String generatedValue = String.valueOf(ThreadLocalRandom.current().nextInt());
        try {
            throw new RuntimeException(generatedValue);
        } catch (RuntimeException e) {
            e.printStackTrace();
        }
        return generatedValue;
    }

    @Benchmark
    public String custonExceptionWithoutStackTrace() throws RuntimeException {
        String generatedValue = String.valueOf(ThreadLocalRandom.current().nextInt());
        try {
            throw new MyException(generatedValue);
        } catch (MyException e) {

        }
        return generatedValue;
    }


}
