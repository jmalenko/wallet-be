package cz.jaro.transaction;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import cz.jaro.transaction.controller.TransactionController;
import cz.jaro.transaction.model.KeyValue;
import cz.jaro.transaction.model.Transaction;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class TransactionControllerTest {

    private static final Logger log = LoggerFactory.getLogger(TransactionController.class);

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void all() throws Exception {
        this.mockMvc.perform(get("/transaction/all"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray());
    }

    @Test
    void count() throws Exception {
        MvcResult result = mockMvc.perform(get("/transaction/count"))
                .andExpect(status().isOk())
                .andReturn();
        String content = result.getResponse().getContentAsString();
        long count = Long.parseLong(content);
        assertThat(count).isNotNegative();
    }

    @Test
    void getNonexistent() throws Exception {
        long id = -1;
        mockMvc.perform(get("/transaction/" + id))
                .andExpect(status().isNotFound());
    }

    @Test
    void postCompleteLifeCycle() throws Exception {
        long id = new Random().nextInt(10000);
        long suffix = id % 5;
        Transaction transaction = new Transaction(id, System.currentTimeMillis(), "type" + suffix, "actor" + suffix, new ArrayList<>());
        transaction.addData(new KeyValue(null, null, "key" + suffix, "value" + suffix));

        long count1 = getCount();

        // Test: Read (not exiting entity)
        mockMvc.perform(get("/transaction/" + id))
                .andExpect(status().isNotFound());

        // Test: Create
        mockMvc.perform(post("/transaction")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(transaction))
                        .accept(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(id));

        long count2 = getCount();
        assertThat(count2).isEqualTo(count1 + 1);

        // Test: Create a duplicate
        mockMvc.perform(post("/transaction")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(transaction))
                        .accept(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isConflict());

        // Test: Read
        mockMvc.perform(get("/transaction/" + id))
                .andExpect(status().is2xxSuccessful())
                .andExpect(jsonPath("$.id").value(transaction.getId()))
                .andExpect(jsonPath("$.timestamp").value(transaction.getTimestamp()))
                .andExpect(jsonPath("$.type").value(transaction.getType()))
                .andExpect(jsonPath("$.actor").value(transaction.getActor()))
                .andExpect(jsonPath("$.data[0].key").value(transaction.getData().get(0).getKey()))
                .andExpect(jsonPath("$.data[0].value").value(transaction.getData().get(0).getValue()))
        ;

        // Test: Update
        transaction.setActor(transaction.getActor() + "updated");
        mockMvc.perform(put("/transaction")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(transaction))
                        .accept(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isAccepted())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(id))
                .andExpect(jsonPath("$.actor").value(transaction.getActor()));

        // Test: Read
        mockMvc.perform(get("/transaction/" + id))
                .andExpect(status().is2xxSuccessful())
                .andExpect(jsonPath("$.id").value(transaction.getId()))
                .andExpect(jsonPath("$.timestamp").value(transaction.getTimestamp()))
                .andExpect(jsonPath("$.type").value(transaction.getType()))
                .andExpect(jsonPath("$.actor").value(transaction.getActor()))
                .andExpect(jsonPath("$.data[0].key").value(transaction.getData().get(0).getKey()))
                .andExpect(jsonPath("$.data[0].value").value(transaction.getData().get(0).getValue()))
        ;

        // Test: Delete
        mockMvc.perform(delete("/transaction/" + id))
                .andExpect(status().is2xxSuccessful());

        long count3 = getCount();
        assertThat(count3).isEqualTo(count1);

        // Test: Read (not exiting entity)
        mockMvc.perform(get("/transaction/" + id))
                .andExpect(status().isNotFound());
    }

    long getCount() throws Exception {
        MvcResult result = mockMvc.perform(get("/transaction/count"))
                .andExpect(status().isOk())
                .andReturn();
        String content = result.getResponse().getContentAsString();
        return Long.parseLong(content);
    }

    @Test
    void createWithWrongDataType() throws Exception {
        mockMvc.perform(post("/transaction")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"id\": 1, \"timestamp\": \"not_a_number\"}")
                        .accept(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isBadRequest());
    }

    @Test
    void createWithNoId() throws Exception {
        mockMvc.perform(post("/transaction")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"timestamp\": 1}")
                        .accept(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isBadRequest());
    }

    @Test
    void createWithInvalidJSON() throws Exception {
        mockMvc.perform(post("/transaction")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("X")
                        .accept(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isBadRequest());
    }

    @Test
    void updateWithNonexistentId() throws Exception {
        mockMvc.perform(put("/transaction")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"id\": -1}")
                        .accept(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isNotFound());
    }

    @Test
    void deleteWithNonexistentId() throws Exception {
        mockMvc.perform(delete("/transaction/-1"))
                .andExpect(status().isNotFound());
    }

    @Test
    void searchTimestampBetween() throws Exception {
        long id = new Random().nextInt(10000);
        Transaction transaction = new Transaction(id, 101L, "type", "actor", new ArrayList<>());

        List<Transaction> transactions1 = getResponseAsArray("/transaction/search/timestamp/100/200");
        long count1 = transactions1.size();

        createTransaction(transaction);

        List<Transaction> transactions2 = getResponseAsArray("/transaction/search/timestamp/100/200");
        long count2 = transactions2.size();

        assertThat(count2).isEqualTo(count1 + 1);

        transactions2.forEach(t -> {
            assertThat(t.getTimestamp()).isBetween(100L, 200L);
        });

        deleteTransaction(id);
    }

    @Test
    void searchTimestampBetweenNegative() throws Exception {
        long id = new Random().nextInt(10000);
        Transaction transaction = new Transaction(id, 201L, "type", "actor", new ArrayList<>());

        List<Transaction> transactions1 = getResponseAsArray("/transaction/search/timestamp/100/200");
        long count1 = transactions1.size();

        createTransaction(transaction);

        List<Transaction> transactions2 = getResponseAsArray("/transaction/search/timestamp/100/200");
        long count2 = transactions2.size();

        assertThat(count2).isEqualTo(count1);

        transactions2.forEach(t -> {
            assertThat(t.getTimestamp()).isBetween(100L, 200L);
        });

        deleteTransaction(id);
    }

    @Test
    void searchType() throws Exception {
        long id = new Random().nextInt(10000);
        Transaction transaction = new Transaction(id, 201L, "type", "actor", new ArrayList<>());

        List<Transaction> transactions1 = getResponseAsArray("/transaction/search/type/type");
        long count1 = transactions1.size();

        createTransaction(transaction);

        List<Transaction> transactions2 = getResponseAsArray("/transaction/search/type/type");
        long count2 = transactions2.size();

        assertThat(count2).isEqualTo(count1 + 1);

        transactions2.forEach(t -> {
            assertThat(t.getType()).isEqualTo("type");
        });

        deleteTransaction(id);
    }

    @Test
    void searchDataKey() throws Exception {
        long id = new Random().nextInt(10000);
        Transaction transaction = new Transaction(id, System.currentTimeMillis(), "type", "actor", new ArrayList<>());
        transaction.addData(new KeyValue(null, null, "key", "value"));

        List<Transaction> transactions1 = getResponseAsArray("/transaction/search/data/key");
        long count1 = transactions1.size();

        createTransaction(transaction);

        List<Transaction> transactions2 = getResponseAsArray("/transaction/search/data/key");
        long count2 = transactions2.size();

        assertThat(count2).isEqualTo(count1 + 1);

        transactions2.forEach(t -> {
            long count = 0;
            for (KeyValue keyValue : t.getData()) {
                if (keyValue.getKey().equals("key"))
                    count++;
            }
            assertThat(count).isEqualTo(1);
        });

        deleteTransaction(id);
    }

    private List<Transaction> getResponseAsArray(String path) throws Exception {
        MvcResult result = mockMvc.perform(get(path))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andReturn();
        String content = result.getResponse().getContentAsString();
        List<Transaction> transactions = objectMapper.readValue(content, new TypeReference<List<Transaction>>() {
        });
        return transactions;
    }

    private void createTransaction(Transaction transaction) throws Exception {
        log.trace("Creating transaction with id " + transaction.getId());

        mockMvc.perform(post("/transaction")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(transaction))
                        .accept(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isCreated());
    }

    private void deleteTransaction(long id) throws Exception {
        log.trace("Deleting transaction with id " + id);

        mockMvc.perform(delete("/transaction/" + id))
                .andExpect(status().is2xxSuccessful());
    }

}
