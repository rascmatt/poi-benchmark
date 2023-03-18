package org.apache.poi.xssf.streaming;

import org.apache.poi.util.IOUtils;
import org.openjdk.jmh.annotations.Fork;
import org.openjdk.jmh.annotations.Level;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.Setup;
import org.openjdk.jmh.annotations.State;
import org.openjdk.jmh.annotations.TearDown;

import java.io.IOException;

@State(Scope.Benchmark)
public class StateImproved
{
    public SXSSFWorkbook wb;
    public SXSSFSheet sheet;

    @Setup(Level.Iteration)
    public void setUp() throws IOException
    {
        wb = new SXSSFWorkbook(){
            @Override
            protected SheetDataWriter createSheetDataWriter() throws IOException
            {
                return new SheetDataWriterImproved(_sharedStringSource);
            }
        };
        sheet = wb.createSheet();
    }

    @TearDown(Level.Iteration)
    public void tearDown()
    {
        wb.dispose();
        IOUtils.closeQuietly(wb);
    }
}
