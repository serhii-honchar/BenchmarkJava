package ua.kiev.sa;

import org.openjdk.jmh.annotations.*;
import org.openjdk.jmh.infra.Blackhole;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.RunnerException;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;

import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/*
    Benchmark                                                    (streamSize)  Mode  Cnt    Score    Error  Units
StreamBenchmark.forIterator                               1000000  avgt   20    6,404 ±  0,145  ms/op
StreamBenchmark.forIteratorWithOperation                  1000000  avgt   20  175,898 ±  1,634  ms/op
StreamBenchmark.parallelStreamIterator                    1000000  avgt   20   24,502 ±  6,040  ms/op
StreamBenchmark.parallelStreamIteratorWithOperation       1000000  avgt   20  118,390 ± 11,538  ms/op
StreamBenchmark.streamIterator                            1000000  avgt   20    6,514 ±  0,109  ms/op
StreamBenchmark.streamIteratorWithOperation               1000000  avgt   20  184,903 ±  2,542  ms/op
*/

@State(Scope.Benchmark)
@OutputTimeUnit(TimeUnit.MILLISECONDS)
@Warmup(iterations = 5)
@BenchmarkMode(Mode.AverageTime)
public class StreamBenchmark {



    @Param({"1000000"})
    private int streamSize;

    private List<String> TEST_DATA;

    public static void main(String[] args) throws RunnerException {

        Options opt = new OptionsBuilder()
                .include(StreamBenchmark.class.getSimpleName())
                .forks(1)
                .build();

        new Runner(opt).run();
    }


    @Setup
    public void setup() {
        TEST_DATA = createData();
    }


    @Benchmark
    public void forIterator(Blackhole bh) {
        for (String s : TEST_DATA) {
            bh.consume(s);
        }
    }

    @Benchmark
    public void forIteratorWithOperation(Blackhole bh) {
        for (String s : TEST_DATA) {
            double d = Double.parseDouble(s.replace("Number", ""));
            if (d % 2 == 0) {
                double result = Math.cos(d);
                bh.consume(d);
            }
        }
    }

    @Benchmark
    public void streamIteratorWithOperation(Blackhole bh) {
        TEST_DATA.stream()
                .map(x -> Double.parseDouble(x.replace("Number", "")))
                .filter(x -> x % 2 == 0)
                .map(Math::cos).forEach(bh::consume);

    }

    @Benchmark
    public void streamIterator(Blackhole bh) {
        TEST_DATA.stream()
                .forEach(bh::consume);
    }

    @Benchmark
    public void parallelStreamIteratorWithOperation(Blackhole bh) {
        TEST_DATA.parallelStream()
                .map(x -> Double.parseDouble(x.replace("Number", "")))
                .filter(x -> x % 2 == 0)
                .map(Math::cos)
                .forEach(bh::consume);
    }

    @Benchmark
    public void parallelStreamIterator(Blackhole bh) {
        TEST_DATA.parallelStream()
                .forEach(bh::consume);
    }


    private List<String> createData() {
        return IntStream.rangeClosed(0, streamSize)
                .mapToObj(x -> "Number" + x)
                .collect(Collectors.toList());
    }

}
