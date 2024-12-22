import org.springframework.stereotype.Service;
import java.util.*;
@Service
public class FlowchartService 
{
    private final Map<String, Flowchart> flowchartDB = new HashMap<>();
    public Flowchart createFlowchart(Flowchart flowchart) 
    {
        if (flowchartDB.containsKey(flowchart.getId())) 
        {
            throw new IllegalArgumentException("Flowchart with this ID already exists.");
        }
        validateGraph(flowchart);
        flowchartDB.put(flowchart.getId(), flowchart);
        return flowchart;
    }
    public Flowchart getFlowchart(String id) 
    {
        return flowchartDB.getOrDefault(id, null);
    }
    public Flowchart updateFlowchart(String id, Flowchart updatedFlowchart) 
    {
        Flowchart existingFlowchart = flowchartDB.get(id);
        if (existingFlowchart == null) 
        {
            throw new NoSuchElementException("Flowchart not found.");
        }
        validateGraph(updatedFlowchart);
        existingFlowchart.setNodes(updatedFlowchart.getNodes());
        existingFlowchart.setEdges(updatedFlowchart.getEdges());
        return existingFlowchart;
    }
    public void deleteFlowchart(String id) 
    {
        if (!flowchartDB.containsKey(id)) 
        {
            throw new NoSuchElementException("Flowchart not found.");
        }
        flowchartDB.remove(id);
    }
    public List<Edge> getOutgoingEdges(String id, String node) 
    {
        Flowchart flowchart = flowchartDB.get(id);
        if (flowchart == null) 
        {
            throw new NoSuchElementException("Flowchart not found.");
        }
        List<Edge> outgoingEdges = new ArrayList<>();
        for (Edge edge : flowchart.getEdges()) 
        {
            if (edge.getFrom().equals(node)) 
            {
                outgoingEdges.add(edge);
            }
        }
        return outgoingEdges;
    }
    public Set<String> getConnectedNodes(String id, String node) 
    {
        Flowchart flowchart = flowchartDB.get(id);
        if (flowchart == null) 
        {
            throw new NoSuchElementException("Flowchart not found.");
        }
        Set<String> visited = new HashSet<>();
        traverseGraph(flowchart, node, visited);
        return visited;
    }
    private void traverseGraph(Flowchart flowchart, String node, Set<String> visited) 
    {
        if (!visited.contains(node)) 
        {
            visited.add(node);
            for (Edge edge : flowchart.getEdges()) 
            {
                if (edge.getFrom().equals(node)) 
                {
                    traverseGraph(flowchart, edge.getTo(), visited);
                }
            }
        }
    }
    private void validateGraph(Flowchart flowchart) 
    {
        Set<String> nodeSet = new HashSet<>(flowchart.getNodes());
        if (nodeSet.size() != flowchart.getNodes().size()) 
        {
            throw new IllegalArgumentException("Duplicate nodes found.");
        }
        for (Edge edge : flowchart.getEdges()) 
        {
            if (!nodeSet.contains(edge.getFrom()) || !nodeSet.contains(edge.getTo())) 
            {
                throw new IllegalArgumentException("Edge refers to non-existing nodes.");
            }
        }
    }
}
