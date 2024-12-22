import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
public class FlowchartControllerTests 
{
    @Autowired
    private MockMvc mockMvc;
    @Test
    void testCreateFlowchart() throws Exception 
    {
        String flowchartJson = """
            {
                "id": "1",
                "nodes": ["A", "B", "C"],
                "edges": [{"from": "A", "to": "B"}, {"from": "B", "to": "C"}]
            }
        """;
        mockMvc.perform(MockMvcRequestBuilders.post("/flowcharts")
                .contentType(MediaType.APPLICATION_JSON)
                .content(flowchartJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value("1"));
    }
    @Test
    void testGetFlowchart() throws Exception 
    {
        // Create a flowchart to test retrieval
        testCreateFlowchart();

        mockMvc.perform(MockMvcRequestBuilders.get("/flowcharts/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value("1"))
                .andExpect(jsonPath("$.nodes").isArray())
                .andExpect(jsonPath("$.edges").isArray());
    }
    @Test
    void testUpdateFlowchart() throws Exception 
    {
        testCreateFlowchart();
        String updatedFlowchartJson = """
            {
                "id": "1",
                "nodes": ["A", "B", "C", "D"],
                "edges": [{"from": "A", "to": "B"}, {"from": "B", "to": "C"}, {"from": "C", "to": "D"}]
            }
        """;
        mockMvc.perform(MockMvcRequestBuilders.put("/flowcharts/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(updatedFlowchartJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nodes.length()").value(4))
                .andExpect(jsonPath("$.edges.length()").value(3));
    }
    @Test
    void testDeleteFlowchart() throws Exception 
    {
        testCreateFlowchart();
        mockMvc.perform(MockMvcRequestBuilders.delete("/flowcharts/1"))
                .andExpect(status().isNoContent());
        // Ensure the flowchart is deleted
        mockMvc.perform(MockMvcRequestBuilders.get("/flowcharts/1"))
                .andExpect(status().isNotFound());
    }
    @Test
    void testGetOutgoingEdges() throws Exception 
    {
        testCreateFlowchart();
        mockMvc.perform(MockMvcRequestBuilders.get("/flowcharts/1/edges/A"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].from").value("A"))
                .andExpect(jsonPath("$[0].to").value("B"));
    }
    @Test
    void testGetConnectedNodes() throws Exception 
    {
        String flowchartJson = """
            {
                "id": "2",
                "nodes": ["A", "B", "C", "D"],
                "edges": [{"from": "A", "to": "B"}, {"from": "B", "to": "C"}, {"from": "C", "to": "D"}]
            }
        """;
        mockMvc.perform(MockMvcRequestBuilders.post("/flowcharts")
                .contentType(MediaType.APPLICATION_JSON)
                .content(flowchartJson))
                .andExpect(status().isOk());
        mockMvc.perform(MockMvcRequestBuilders.get("/flowcharts/2/connections/A"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[?(@ == 'B')]").exists())
                .andExpect(jsonPath("$[?(@ == 'C')]").exists())
                .andExpect(jsonPath("$[?(@ == 'D')]").exists());
    }
    @Test
    void testValidationForDuplicateNodes() throws Exception 
    {
        String invalidFlowchartJson = """
            {
                "id": "3",
                "nodes": ["A", "B", "A"], 
                "edges": [{"from": "A", "to": "B"}]
            }
        """;
        mockMvc.perform(MockMvcRequestBuilders.post("/flowcharts")
                .contentType(MediaType.APPLICATION_JSON)
                .content(invalidFlowchartJson))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Duplicate nodes found."));
    }
    @Test
    void testValidationForInvalidEdges() throws Exception 
    {
        String invalidFlowchartJson = """
            {
                "id": "4",
                "nodes": ["A", "B"], 
                "edges": [{"from": "A", "to": "C"}]
            }
        """;
        mockMvc.perform(MockMvcRequestBuilders.post("/flowcharts")
                .contentType(MediaType.APPLICATION_JSON)
                .content(invalidFlowchartJson))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Edge refers to non-existing nodes."));
    }
}
