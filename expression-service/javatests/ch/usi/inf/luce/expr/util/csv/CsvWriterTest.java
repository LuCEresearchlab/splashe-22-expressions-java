package ch.usi.inf.luce.expr.util.csv;

import java.util.List;
import org.junit.Assert;
import org.junit.Test;

public class CsvWriterTest {

    @Test
    public void writeWithHeader() {
        final CsvWriter writer = new CsvWriter();
        writer.writeHeader("a", "b", "c");

        Assert.assertEquals("a,b,c\r\n", writer.getCsv());

        writer.writeRow(1, true, 2.3f);
        writer.writeRow(2, false, 4.53);

        Assert.assertEquals("a,b,c\r\n1,true,2.3\r\n2,false,4.53\r\n", writer.getCsv());
    }

    @Test
    public void writeWithoutHeader() {
        final CsvWriter writer = new CsvWriter();
        writer.writeRow('q', 'w', 'e', 'r', 't', 'y');
        writer.writeRow('a', 's', 'd', 'f', 'g', 'h');

        Assert.assertEquals("q,w,e,r,t,y\r\na,s,d,f,g,h\r\n", writer.getCsv());
    }

    @Test(expected = IllegalStateException.class)
    public void writeHeadersTwice() {
        final CsvWriter writer = new CsvWriter();
        writer.writeHeader("A", "B", "C");
        writer.writeHeader("a", "b", "c");
    }

    @Test(expected = IllegalStateException.class)
    public void writeHeadersAfterFirstRow() {
        final CsvWriter writer = new CsvWriter();
        writer.writeRow(List.of(1, 2, 3, 4));
        writer.writeHeader("a", "b", "c", "d");
    }

    @Test(expected = IllegalArgumentException.class)
    public void writeRowHeaderLengthMismatch() {
        final CsvWriter writer = new CsvWriter();
        writer.writeHeader("A", "B", "C");
        writer.writeRow(1, 2, 3, 4);
    }

    @Test(expected = IllegalArgumentException.class)
    public void writeRowLengthMismatch() {
        final CsvWriter writer = new CsvWriter();
        writer.writeRow(1, 2, 3, 4);
        writer.writeRow(5, 6, 7);
    }

    @Test
    public void writeEscapedRowStrings() {
        final CsvWriter writer = new CsvWriter();
        writer.writeRow("\"hello\"", "a,b", "c\rc", "d\nd", "f\r\ng", "h:i");
        Assert.assertEquals("\"\"\"hello\"\"\",\"a,b\",\"c\rc\",\"d\nd\",\"f\r\ng\",h:i\r\n", writer.getCsv());
    }

    @Test
    public void writeEscapedHeaderStrings() {
        final CsvWriter writer = new CsvWriter();
        writer.writeHeader("\"hello\"", "a,b", "c\rc", "d\nd", "f\r\ng", "h:i");
        Assert.assertEquals("\"\"\"hello\"\"\",\"a,b\",\"c\rc\",\"d\nd\",\"f\r\ng\",h:i\r\n", writer.getCsv());
    }
}
