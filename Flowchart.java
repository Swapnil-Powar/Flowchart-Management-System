import java.util.List;
class Flowchart 
{
    private String id;
    private List<String> nodes;
    private List<Edge> edges;
    public Flowchart(String id, List<String> nodes, List<Edge> edges) 
    {
        this.id = id;
        this.nodes = nodes;
        this.edges = edges;
    }
    public String getId() 
    {
        return id;
    }
    public void setId(String id) 
    {
        this.id = id;
    }
    public List<String> getNodes() 
    {
        return nodes;
    }
    public void setNodes(List<String> nodes) 
    {
        this.nodes = nodes;
    }
    public List<Edge> getEdges() 
    {
        return edges;
    }
    public void setEdges(List<Edge> edges) 
    {
        this.edges = edges;
    }
}
class Edge 
{
    private String from;
    private String to;
    public Edge(String from, String to) 
    {
        this.from = from;
        this.to = to;
    }
    public String getFrom() 
    {
        return from;
    }
    public void setFrom(String from) 
    {
        this.from = from;
    }
    public String getTo() 
    {
        return to;
    }
    public void setTo(String to) 
    {
        this.to = to;
    }
}
