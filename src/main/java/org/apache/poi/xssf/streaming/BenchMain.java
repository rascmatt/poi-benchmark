package org.apache.poi.xssf.streaming;

import org.apache.poi.ss.SpreadsheetVersion;
import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.BenchmarkMode;
import org.openjdk.jmh.annotations.Fork;
import org.openjdk.jmh.annotations.Mode;

import java.io.IOException;

public class BenchMain
{

    @Benchmark
    @Fork(value = 3)
    @BenchmarkMode(Mode.Throughput)
    public void bench_unit_original(StateOriginal state) throws IOException
    {
        state.sheet._writer.outputEscapedString("This is a moderately long test string");
    }

    @Benchmark
    @Fork(value = 3)
    @BenchmarkMode(Mode.Throughput)
    public void bench_unit_improved(StateImproved state) throws IOException
    {
        state.sheet._writer.outputEscapedString("This is a moderately long test string");
    }

    @Benchmark
    @Fork(value = 3)
    @BenchmarkMode(Mode.Throughput)
    public void bench_integration_original(StateOriginal state) throws IOException
    {
        if (state.sheet.getLastRowNum() >= SpreadsheetVersion.EXCEL2007.getLastRowIndex())
        {
            state.sheet = state.wb.createSheet();
        }

        SXSSFRow row = state.sheet.createRow(state.sheet.getLastRowNum() + 1);

        for (int i = 0; i < 20; i++)
        {
            SXSSFCell cell = row.createCell(0);
            cell.setCellValue("Test string number " + i);
        }
    }

    @Benchmark
    @Fork(value = 3)
    @BenchmarkMode(Mode.Throughput)
    public void bench_integration_improved(StateImproved state) throws IOException
    {
        if (state.sheet.getLastRowNum() >= SpreadsheetVersion.EXCEL2007.getLastRowIndex())
        {
            state.sheet = state.wb.createSheet();
        }

        SXSSFRow row = state.sheet.createRow(state.sheet.getLastRowNum() + 1);

        for (int i = 0; i < 20; i++)
        {
            SXSSFCell cell = row.createCell(0);
            cell.setCellValue("Test string number " + i);
        }
    }
}
