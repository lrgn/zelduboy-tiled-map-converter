package fr.lrgn.zelduboy;

import java.io.IOException;
import java.io.Writer;

public abstract class AbstractCodeGenerator
{
    protected static void write(Writer writer, String format) throws IOException
    {
        writer.write(format);
    }

    protected static void write(Writer writer, String format, Object... args) throws IOException
    {
        writer.write(String.format(format, args));
    }
}