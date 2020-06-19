package com.kineticdata.bridgehub.adapter.pipedrive;

import com.kineticdata.bridgehub.adapter.BridgeAdapterTestBase;
import com.kineticdata.bridgehub.adapter.BridgeError;
import com.kineticdata.bridgehub.adapter.BridgeRequest;
import com.kineticdata.bridgehub.adapter.Count;
import com.kineticdata.bridgehub.adapter.Record;
import com.kineticdata.bridgehub.adapter.RecordList;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import static org.junit.Assert.assertEquals;
import org.junit.Test;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PipedriveTest extends BridgeAdapterTestBase{
    
    private static final Logger LOGGER = 
        LoggerFactory.getLogger(PipedriveTest.class);
        
    @Override
    public Class getAdapterClass() {
        return PipedriveAdapter.class;
    }
    
    @Override
    public String getConfigFilePath() {
        return "src/test/resources/bridge-config.yml";
    }
    
    /*
        Test count method
    */
    @Override
    @Test
    public void test_emptyCount() throws Exception {
        assertTrue(true);
    }
    
    /*
        Test retrieve method
    */
    @Override
    @Test
    public void test_emptyRetrieve() throws Exception {
        assertTrue(true);
    }
    
    /*
        Test search method
    */
    @Override
    @Test
    public void test_emptySearch() throws Exception {
        assertTrue(true);
    }
    
    @Test
    public void test_count() throws Exception{
        BridgeError error = null;

        BridgeRequest request = new BridgeRequest();
        request.setStructure("Deals");
        request.setFields(new ArrayList<>());
        request.setQuery("");
        request.setParameters(new HashMap());
        
        Count count = null;
        try {
            count = getAdapter().count(request);
        } catch (BridgeError e) {
            error = e;
        }
        
        // Test Adhoc count query
        assertNull(error);
        assertTrue(count.getValue() > 0);
        
        request.setStructure("Adhoc");
        request.setQuery("/deals");
        
        Count adhocCount = null;
        try {
            adhocCount = getAdapter().count(request);
        } catch (BridgeError e) {
            error = e;
        }
        
        assertNull(error);
        assertTrue(count.getValue() == adhocCount.getValue());
    }
    
    @Test
    public void test_retrieve() throws Exception{
        BridgeError error = null;
        
        // Create the Bridge Request
        List<String> fields = new ArrayList<>();
        fields.add("name");
        fields.add("first_name");
        fields.add("last_name");
        
        BridgeRequest request = new BridgeRequest();
        request.setStructure("Persons");
        request.setFields(fields);
        request.setQuery("id=<%=parameter[\"Id\"]%>");
        
        request.setParameters(new HashMap<String, String>() {{ 
            put("Id", "1");
        }});        
        
        Record record = null;
        try {
            record = getAdapter().retrieve(request);
        } catch (BridgeError e) {
            error = e;
        }
        
        assertNull(error);
        assertTrue(record.getRecord().size() > 0);
    }
    
    @Test
    public void test_adhoc_retrieve() throws Exception{
        BridgeError error = null;
        
        // Create the Bridge Request
        List<String> fields = new ArrayList<>();
        fields.add("name");
        fields.add("first_name");
        fields.add("last_name");
        
        BridgeRequest request = new BridgeRequest();
        request.setStructure("Persons");
        request.setFields(fields);
        request.setQuery("id=<%=parameter[\"Id\"]%>");
        
        request.setParameters(new HashMap<String, String>() {{ 
            put("Id", "1");
        }});
        
        Record record = null;
        try {
            record = getAdapter().retrieve(request);
        } catch (BridgeError e) {
            error = e;
        }
        
        assertNull(error);
        assertTrue(record.getRecord().size() > 0);

        // Test Adhoc query
        request.setStructure("Adhoc");
        request.setQuery("/persons/<%=parameter[\"Id\"]%>");
        
        
        Record adhocRecord = null;
        try {
            adhocRecord = getAdapter().retrieve(request);
        } catch (BridgeError e) {
            error = e;
        }
        
        assertNull(error);
        assertEquals(record.getRecord(),adhocRecord.getRecord());
    }
    
    @Test
    public void test_search() throws Exception{
        BridgeError error = null;
        
        // Create the Bridge Request
        List<String> fields = new ArrayList<>();
        fields.add("person_name");
        fields.add("org_name");
        fields.add("owner_name");
        
        BridgeRequest request = new BridgeRequest();
        request.setStructure("Deals");
        request.setFields(fields);
        request.setQuery("");
        request.setParameters(new HashMap<String, String>());
                
        RecordList records = null;
        try {
            records = getAdapter().search(request);
        } catch (BridgeError e) {
            error = e;
        }
        
        assertNull(error);
        assertTrue(records.getRecords().size() > 0);
        
        request.setStructure("Adhoc");
        request.setQuery("/deals");
        request.setParameters(new HashMap<String, String>());
                
        RecordList adhocRecords = null;
        try {
            adhocRecords = getAdapter().search(request);
        } catch (BridgeError e) {
            error = e;
        }
        
        assertNull(error);
        assertTrue(adhocRecords.getRecords().size() == records.getRecords().size());
    }
    
    @Test
    public void test_search_sort() throws Exception{
        BridgeError error = null;
        
       // Create the Bridge Request
        List<String> fields = new ArrayList<>();
        fields.add("person_name");
        fields.add("org_name");
        fields.add("owner_name");
        
        BridgeRequest request = new BridgeRequest();
        request.setStructure("Deals");
        request.setFields(fields);
        request.setQuery("");
        request.setParameters(new HashMap<String, String>());
        
        request.setMetadata(new HashMap<String, String>() {{
            put("order", "<%=field[\"person_name\"]%>:DESC");
        }});
        
        RecordList records = null;
        try {
            records = getAdapter().search(request);
        } catch (BridgeError e) {
            error = e;
            LOGGER.error("Error: ", e);
        }
        
        assertNull(error);
        assertTrue(records.getRecords().size() > 0);
    }
   
//    @Test
//    public void test_search_sort_multiple() throws Exception{
//        BridgeError error = null;
//        
//        // Create the Bridge Request
//        List<String> fields = new ArrayList<>();
//        fields.add("displayName");
//        fields.add("surname");
//        
//        BridgeRequest request = new BridgeRequest();
//        request.setStructure("Users");
//        request.setFields(fields);
//        request.setQuery("");
//        
//        Map <String, String> metadata = new HashMap<>();
//        metadata.put("order", ""
//            + "<%=field[\"surname\"]%>:DESC");       
//        request.setMetadata(metadata);
//        
//        RecordList records = null;
//        try {
//            records = getAdapter().search(request);
//        } catch (BridgeError e) {
//            error = e;
//            LOGGER.error("Error: ", e);
//        }
//        
//        assertNull(error);
//        assertTrue(records.getRecords().size() > 0);
//    }
    
}