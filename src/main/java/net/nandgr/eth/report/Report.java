package net.nandgr.eth.report;

import net.nandgr.eth.ContractAnalyzer;
import net.nandgr.eth.Opcode;
import net.nandgr.eth.Parameters;
import net.nandgr.eth.bytecode.beans.BytecodeChunk;
import net.nandgr.eth.bytecode.symexecution.SymExecutor;
import net.nandgr.eth.bytecode.symexecution.evm.EVMEnvironment;
import net.nandgr.eth.exceptions.ReportException;
import net.nandgr.eth.report.beans.Edge;
import net.nandgr.eth.report.beans.Font;
import net.nandgr.eth.report.beans.Node;
import org.apache.commons.io.FileUtils;
import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;
import org.codehaus.jackson.map.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.io.File;
import java.io.IOException;
import java.io.StringWriter;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.stream.Collectors;

public class Report {

    private static final Logger logger = LoggerFactory.getLogger(Report.class);
    private static final String REPORT_FILE_NAME = "report.html";
    private static final String TEMPLATE_FILE = "template/report_template.html";
    private final ContractAnalyzer contractAnalyzer;
    private String reportPath = Parameters.REPORT_DIR + "_" + LocalDateTime.now().toString();

    public Report(ContractAnalyzer contractAnalyzer) {
        this.contractAnalyzer = contractAnalyzer;
    }

    public void createReport() throws ReportException {
        File reportsDir = new File(reportPath);
        if(!reportsDir.exists()) {
            boolean dirCreated = reportsDir.mkdir();
            if (!dirCreated) {
                throw new ReportException("Report directory cannot be created");
            }
        }

        Map<Integer, BytecodeChunk> chunks = contractAnalyzer.getChunks();
        Map<EVMEnvironment, SymExecutor> executions = contractAnalyzer.getExecutions();
        List<Node> nodes = buildNodes(chunks);
        List<Edge> edges = buildEdges(chunks);

        StringWriter stringWriter = buildStringWriter(nodes, edges, executions);

        File reportFile = new File(reportsDir.getAbsolutePath() + File.separator + REPORT_FILE_NAME);
        try {
            reportFile.createNewFile();
        } catch (IOException e) {
            logger.error("Error creating report file", e);
            throw new ReportException("Report file cannot be created: " + e.getCause());
        }
        try {
            FileUtils.writeStringToFile(reportFile, stringWriter.toString());
        } catch (IOException e) {
            logger.error("Error writing to report file", e);
            throw new ReportException("Report file cannot be written: " + e.getCause());
        }
    }

    private static StringWriter buildStringWriter(List<Node> nodes, List<Edge> edges, Map<EVMEnvironment, SymExecutor> executions) throws ReportException {

        VelocityEngine velocityEngine = new VelocityEngine();
        Properties p = new Properties();
        p.setProperty("resource.loader", "class");
        p.setProperty("class.resource.loader.class", "org.apache.velocity.runtime.resource.loader.ClasspathResourceLoader");
        velocityEngine.init(p);
        Template template = velocityEngine.getTemplate(TEMPLATE_FILE);
        VelocityContext velocityContext = new VelocityContext();
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            velocityContext.put("nodes", objectMapper.writeValueAsString(nodes));
            velocityContext.put("edges", objectMapper.writeValueAsString(edges));
            velocityContext.put("executions", executions);
        } catch (IOException e) {
            logger.error("Error building the report: " + e);
            throw new ReportException("Failed creating ReportItem");
        }
        StringWriter stringWriter = new StringWriter();
        template.merge(velocityContext, stringWriter);
        return stringWriter;
    }

    private static List<Edge> buildEdges(Map<Integer, BytecodeChunk> chunks) {
        List<Edge> edges = new ArrayList<>();
        for (BytecodeChunk chunk : chunks.values()) {
            BytecodeChunk branchA = chunk.getBranchA();
            BytecodeChunk branchB = chunk.getBranchB();
            if (branchA != null) {
                edges.add(buildEdge(chunk.getId(), branchA));
            }
            if (branchB != null) {
                edges.add(buildEdge(chunk.getId(), branchB));
            }
        }
        return edges;
    }

    private static Edge buildEdge(int id, BytecodeChunk branch) {
        Edge edge = new Edge();
        edge.setFrom(id);
        edge.setTo(branch.getId());
        return edge;
    }

    private static List<Node> buildNodes(Map<Integer, BytecodeChunk> chunks) {
        List<Node> nodes = new ArrayList<>();
        for (BytecodeChunk chunk : chunks.values()) {
            Node node = buildNode(chunk.getId(), chunk);
            nodes.add(node);
        }
        return nodes;
    }

    private static Node buildNode(int id, BytecodeChunk chunk) {
        Node node = new Node();
        node.setId(id);
        String label = chunk.getOpcodes().stream()
                .map(Opcode::toString)
                .collect(Collectors.joining("\n"));
        node.setLabel(label);
        node.setShape("box");
        Font font = new Font();
        font.setAlign("left");
        font.setFace("monospace");
        node.setFont(font);
        return node;
    }
}
