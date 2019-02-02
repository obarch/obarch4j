package org.qjson.junit.md;

import org.mdkt.compiler.CompilationException;
import org.mdkt.compiler.SourceCode;

import javax.tools.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public interface CompileClasses {

    static Path $(List<String> sources) {
        Pattern packageNamePattern = Pattern.compile("package\\s+(.*)\\s*\\;");
        Pattern classNamePattern = Pattern.compile("class\\s+([A-Za-z][A-Za-z0-9_]*)\\s*");
        try {
            JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();
            List<SourceCode> compilationUnits = new ArrayList<>();
            for (String source : sources) {
                Matcher matcher = packageNamePattern.matcher(source);
                if (!matcher.find()) {
                    throw new RuntimeException("invalid java code: missing package name");
                }
                String packageName = matcher.group(1);
                matcher = classNamePattern.matcher(source);
                if (!matcher.find()) {
                    throw new RuntimeException("invalid java code: missing class name");
                }
                String className = matcher.group(1);
                SourceCode compilationUnit =
                        new SourceCode(packageName + "." + className, source);
                compilationUnits.add(compilationUnit);
            }
            Path temp = Files.createTempDirectory("testdata");
            DiagnosticCollector<JavaFileObject> collector = new DiagnosticCollector<>();
            StandardJavaFileManager fileManager = compiler.getStandardFileManager(collector, null, null);
            JavaCompiler.CompilationTask compilationTask = compiler.getTask(
                    null, fileManager, null, Arrays.asList("-d", temp.toString()), null, compilationUnits);
            boolean success = compilationTask.call();
            if (success) {
                return temp;
            }
            Rmtree.$(temp);
            StringBuffer exceptionMsg = new StringBuffer();
            exceptionMsg.append("Unable to compile the source");
            for (Diagnostic<? extends JavaFileObject> d : collector.getDiagnostics()) {
                exceptionMsg.append("\n").append("[kind=").append(d.getKind());
                exceptionMsg.append(", ").append("line=").append(d.getLineNumber());
                exceptionMsg.append(", ").append("message=").append(d.getMessage(Locale.US)).append("]");
            }
            throw new CompilationException(exceptionMsg.toString());
        } catch (CompilationException e) {
            throw e;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
