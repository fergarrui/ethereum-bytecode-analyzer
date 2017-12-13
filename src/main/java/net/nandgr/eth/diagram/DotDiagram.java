package net.nandgr.eth.diagram;

import net.nandgr.eth.Opcode;
import net.nandgr.eth.bytecode.beans.BytecodeChunk;
import net.nandgr.eth.bytecode.symexecution.evm.EVMState;
import net.nandgr.eth.exceptions.GraphException;
import net.nandgr.eth.utils.Lists;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

public class DotDiagram {

    private static final Logger logger = LoggerFactory.getLogger(DotDiagram.class);
    private static final List<String> supportedFormats = Lists.of("png", "svg");

    public static void buildDotFile(Map<Integer, BytecodeChunk> chunks, Map<Opcode, EVMState> executions, String fileName, String format) throws GraphException {
        String s = buildDotFormat(chunks, executions);
        String dotFilename = fileName + ".dot";
        try {
            Files.write(Paths.get(dotFilename), s.getBytes());
        } catch (IOException e) {
            logger.error("Error when creating .dot file", e);
        }
        convertDotTo(dotFilename, format);
    }

    private static String buildDotFormat(Map<Integer, BytecodeChunk> chunks, Map<Opcode, EVMState> executions) {
        StringBuilder sb = new StringBuilder("digraph {").append(System.lineSeparator());
        sb.append("node [shape=box];").append(System.lineSeparator());
        for (BytecodeChunk chunk : chunks.values()) {
            sb.append(chunk.getId() + "[label=\"").append(buildLabelFromChunk(chunk)).append("\"];").append(System.lineSeparator());
            if (chunk.getBranchA() != null) {
                sb.append(chunk.getId()).append("->").append(chunk.getBranchA().getId()).append(";");
            }
            if (chunk.getBranchB() != null) {
                sb.append(chunk.getId()).append("->").append(chunk.getBranchB().getId()).append(";");
            }
        }
        sb.append("}");
        return sb.toString();
    }

    private static String buildLabelFromChunk(BytecodeChunk bytecodeChunk) {
        StringBuilder sb = new StringBuilder();
        for (Opcode opcode : bytecodeChunk.getOpcodes()) {
            sb.append(opcode.toString());
            sb.append("\\l");
        }
        return sb.toString();
    }

    private static void convertDotTo(String dotFile, String format) throws GraphException {
        if (!supportedFormats.contains(format.toLowerCase())) {
            throw new GraphException("Format: " + format + " is not supported." );
        }
        if (!checkDotInClasspath()) {
            throw new GraphException("Graphviz DOT is not in the classpath. So the graph cannot be created. The file.dot will be kept");
        }
        File file = new File(dotFile);
        if (!file.exists()) {
            throw new GraphException("Provided file does not exist: " + dotFile);
        }
        String fmt = "-T" + format;
        String convertedFileName = file.getName().replace(".dot", "." + format);
        String[] command = {"dot", fmt, file.getName(), "-o", convertedFileName};
        try {
            Runtime.getRuntime().exec(command);
        } catch (IOException e) {
            logger.error("Error when converting the graph", e);
            throw new GraphException("Error when converting the graph: " + e.getMessage());
        }
    }

    private static boolean checkDotInClasspath() {
        final String DOT_EXEC_NAME = "dot";
        return Arrays.stream(System.getenv("PATH").split(Pattern.quote(File.pathSeparator)))
                .map(Paths::get)
                .anyMatch(path -> Files.exists(path.resolve(DOT_EXEC_NAME)));
    }
}
