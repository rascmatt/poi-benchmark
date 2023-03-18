package org.apache.poi.xssf.streaming;

import org.apache.poi.util.IOUtils;
import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.BenchmarkMode;
import org.openjdk.jmh.annotations.Fork;
import org.openjdk.jmh.annotations.Level;
import org.openjdk.jmh.annotations.Mode;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.Setup;
import org.openjdk.jmh.annotations.State;
import org.openjdk.jmh.annotations.TearDown;

import java.io.IOException;

@Fork(value = 3)
@State(Scope.Benchmark)
public class Main
{
    private SheetDataWriter writer;

    @Setup(Level.Iteration)
    public void setUp() throws IOException
    {
        writer = new SheetDataWriter();
    }

    @TearDown(Level.Iteration)
    public void tearDown()
    {
        IOUtils.closeQuietly(writer);
    }

    @Benchmark
    @BenchmarkMode(Mode.Throughput)
    public void bench_original() throws IOException
    {
        writer.outputEscapedString("This is a moderately long test string");
    }

    //@Benchmark
    //@BenchmarkMode(Mode.Throughput)
    public void bench_405() throws IOException
    {
        writer.outputEscapedString405("This is a moderately long test string");
    }

    //@Benchmark
    //@BenchmarkMode(Mode.Throughput)
    public void bench_66532_1() throws IOException
    {
        writer.outputEscapedString66532_1("This is a moderately long test string");
    }

    @Benchmark
    @BenchmarkMode(Mode.Throughput)
    public void bench_66532_2() throws IOException
    {
        writer.outputEscapedString66532_2("This is a moderately long test string");
    }

    @Benchmark
    @BenchmarkMode(Mode.Throughput)
    public void bench_66532_final() throws IOException
    {
        writer.outputEscapedString66532_final("This is a moderately long test string");
    }
}