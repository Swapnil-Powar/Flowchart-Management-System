import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Set;
@RestController
@RequestMapping("/flowcharts")
public class FlowchartController 
{
    private final FlowchartService flowchartService;
    public FlowchartController(FlowchartService flowchartService) 
    {
        this.flowchartService = flowchartService;
    }
    @PostMapping
    public ResponseEntity<Flowchart> createFlowchart(@RequestBody Flowchart flowchart) 
    {
        return ResponseEntity.ok(flowchartService.createFlowchart(flowchart));
    }
    @GetMapping("/{id}")
    public ResponseEntity<Flowchart> getFlowchart(@PathVariable String id) 
    {
        Flowchart flowchart = flowchartService.getFlowchart(id);
        if (flowchart == null) 
        {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(flowchart);
    }
    @PutMapping("/{id}")
    public ResponseEntity<Flowchart> updateFlowchart(@PathVariable String id, @RequestBody Flowchart updatedFlowchart) 
    {
        return ResponseEntity.ok(flowchartService.updateFlowchart(id, updatedFlowchart));
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteFlowchart(@PathVariable String id) 
    {
        flowchartService.deleteFlowchart(id);
        return ResponseEntity.noContent().build();
    }
    @GetMapping("/{id}/edges/{node}")
    public ResponseEntity<List<Edge>> getOutgoingEdges(@PathVariable String id, @PathVariable String node) 
    {
        return ResponseEntity.ok(flowchartService.getOutgoingEdges(id, node));
    }
    @GetMapping("/{id}/connections/{node}")
    public ResponseEntity<Set<String>> getConnectedNodes(@PathVariable String id, @PathVariable String node) 
    {
        return ResponseEntity.ok(flowchartService.getConnectedNodes(id, node));
    }
}
