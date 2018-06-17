package fr.lrgn.zelduboy;

import java.io.IOException;
import java.io.Writer;

public abstract class AbstractCodeGenerator
{
    protected static final String DEFINE = "#define %1$s %2$s\n";
    protected static final String INCLUDE = "#include \"%1$s\"\n";
    protected static final String EMPTY_LINE = "\n";

    protected static void write(Writer writer, String format) throws IOException
    {
        writer.write(format);
    }

    protected static void write(Writer writer, String format, Object... args) throws IOException
    {
        writer.write(String.format(format, args));
    }
}