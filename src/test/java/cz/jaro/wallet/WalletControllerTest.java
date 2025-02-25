package cz.jaro.wallet;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest
@AutoConfigureMockMvc
public class WalletControllerTest {

    private static final Logger log = LoggerFactory.getLogger(WalletControllerTest.class);

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

//    @Test
//    void all() throws Exception {
//        mockMvc.perform(get("/transaction/all"))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$").isArray());
//    }
//
//    @Test
//    void count() throws Exception {
//        MvcResult result = mockMvc.perform(get("/transaction/count"))
//                .andExpect(status().isOk())
//                .andReturn();
//        String content = result.getResponse().getContentAsString();
//        long count = Long.parseLong(content);
//        assertThat(count).isNotNegative();
//    }
//
//    @Test
//    void getNonexistent() throws Exception {
//        long id = -1;
//        mockMvc.perform(get("/transaction/" + id))
//                .andExpect(status().isNotFound());
//    }
//
//    @Test
//    void postCompleteLifeCycle() throws Exception {
//        long id = new Random().nextInt(10000);
//        long suffix = id % 5;
//        User user = new User(id, System.currentTimeMillis(), "type" + suffix, "actor" + suffix, new ArrayList<>());
//        user.addAccount(new Account(null, null, "key" + suffix, "value" + suffix));
//
//        long count1 = getCount();
//
//        // Test: Read (not exiting entity)
//        mockMvc.perform(get("/transaction/" + id))
//                .andExpect(status().isNotFound());
//
//        // Test: Create
//        mockMvc.perform(post("/transaction")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(objectMapper.writeValueAsString(user))
//                        .accept(MediaType.APPLICATION_JSON)
//                )
//                .andExpect(status().isCreated())
//                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
//                .andExpect(jsonPath("$.id").value(id));
//
//        long count2 = getCount();
//        assertThat(count2).isEqualTo(count1 + 1);
//
//        // Test: Create a duplicate
//        mockMvc.perform(post("/transaction")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(objectMapper.writeValueAsString(user))
//                        .accept(MediaType.APPLICATION_JSON)
//                )
//                .andExpect(status().isConflict());
//
//        // Test: Read
//        mockMvc.perform(get("/transaction/" + id))
//                .andExpect(status().is2xxSuccessful())
//                .andExpect(jsonPath("$.id").value(user.getId()))
//                .andExpect(jsonPath("$.timestamp").value(user.getTimestamp()))
//                .andExpect(jsonPath("$.type").value(user.getType()))
//                .andExpect(jsonPath("$.actor").value(user.getActor()))
//                .andExpect(jsonPath("$.data[0].key").value(user.getAccounts().get(0).getKey()))
//                .andExpect(jsonPath("$.data[0].value").value(user.getAccounts().get(0).getCurency()))
//        ;
//
//        // Test: Update
//        user.setActor(user.getActor() + "updated");
//        mockMvc.perform(put("/transaction")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(objectMapper.writeValueAsString(user))
//                        .accept(MediaType.APPLICATION_JSON)
//                )
//                .andExpect(status().isAccepted())
//                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
//                .andExpect(jsonPath("$.id").value(id))
//                .andExpect(jsonPath("$.actor").value(user.getActor()));
//
//        // Test: Read
//        mockMvc.perform(get("/transaction/" + id))
//                .andExpect(status().is2xxSuccessful())
//                .andExpect(jsonPath("$.id").value(user.getId()))
//                .andExpect(jsonPath("$.timestamp").value(user.getTimestamp()))
//                .andExpect(jsonPath("$.type").value(user.getType()))
//                .andExpect(jsonPath("$.actor").value(user.getActor()))
//                .andExpect(jsonPath("$.data[0].key").value(user.getAccounts().get(0).getKey()))
//                .andExpect(jsonPath("$.data[0].value").value(user.getAccounts().get(0).getCurency()))
//        ;
//
//        // Test: Delete
//        mockMvc.perform(delete("/transaction/" + id))
//                .andExpect(status().is2xxSuccessful());
//
//        long count3 = getCount();
//        assertThat(count3).isEqualTo(count1);
//
//        // Test: Read (not exiting entity)
//        mockMvc.perform(get("/transaction/" + id))
//                .andExpect(status().isNotFound());
//    }
//
//    long getCount() throws Exception {
//        MvcResult result = mockMvc.perform(get("/transaction/count"))
//                .andExpect(status().isOk())
//                .andReturn();
//        String content = result.getResponse().getContentAsString();
//        return Long.parseLong(content);
//    }
//
//    @Test
//    void createWithWrongDataType() throws Exception {
//        mockMvc.perform(post("/transaction")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content("{\"id\": 1, \"timestamp\": \"not_a_number\"}")
//                        .accept(MediaType.APPLICATION_JSON)
//                )
//                .andExpect(status().isBadRequest());
//    }
//
//    @Test
//    void createWithNoId() throws Exception {
//        mockMvc.perform(post("/transaction")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content("{\"timestamp\": 1}")
//                        .accept(MediaType.APPLICATION_JSON)
//                )
//                .andExpect(status().isBadRequest());
//    }
//
//    @Test
//    void createWithInvalidJSON() throws Exception {
//        mockMvc.perform(post("/transaction")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content("X")
//                        .accept(MediaType.APPLICATION_JSON)
//                )
//                .andExpect(status().isBadRequest());
//    }
//
//    @Test
//    void updateWithNonexistentId() throws Exception {
//        mockMvc.perform(put("/transaction")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content("{\"id\": -1}")
//                        .accept(MediaType.APPLICATION_JSON)
//                )
//                .andExpect(status().isNotFound());
//    }
//
//    @Test
//    void deleteWithNonexistentId() throws Exception {
//        mockMvc.perform(delete("/transaction/-1"))
//                .andExpect(status().isNotFound());
//    }
//
//    @Test
//    void searchTimestampBetween() throws Exception {
//        long id = new Random().nextInt(10000);
//        User user = new User(id, 101L, "type", "actor", new ArrayList<>());
//
//        List<User> transactions1 = getResponseAsArray("/transaction/search/timestamp/100/200");
//        long count1 = transactions1.size();
//
//        createTransaction(user);
//
//        List<User> transactions2 = getResponseAsArray("/transaction/search/timestamp/100/200");
//        long count2 = transactions2.size();
//
//        assertThat(count2).isEqualTo(count1 + 1);
//
//        transactions2.forEach(t ->
//                assertThat(t.getTimestamp()).isBetween(100L, 200L)
//        );
//
//        deleteTransaction(id);
//    }
//
//    @Test
//    void searchTimestampBetweenNegative() throws Exception {
//        long id = new Random().nextInt(10000);
//        User user = new User(id, 201L, "type", "actor", new ArrayList<>());
//
//        List<User> transactions1 = getResponseAsArray("/transaction/search/timestamp/100/200");
//        long count1 = transactions1.size();
//
//        createTransaction(user);
//
//        List<User> transactions2 = getResponseAsArray("/transaction/search/timestamp/100/200");
//        long count2 = transactions2.size();
//
//        assertThat(count2).isEqualTo(count1);
//
//        transactions2.forEach(t ->
//                assertThat(t.getTimestamp()).isBetween(100L, 200L)
//        );
//
//        deleteTransaction(id);
//    }
//
//    @Test
//    void searchType() throws Exception {
//        long id = new Random().nextInt(10000);
//        User user = new User(id, 201L, "type", "actor", new ArrayList<>());
//
//        List<User> transactions1 = getResponseAsArray("/transaction/search/type/type");
//        long count1 = transactions1.size();
//
//        createTransaction(user);
//
//        List<User> transactions2 = getResponseAsArray("/transaction/search/type/type");
//        long count2 = transactions2.size();
//
//        assertThat(count2).isEqualTo(count1 + 1);
//
//        transactions2.forEach(t ->
//                assertThat(t.getType()).isEqualTo("type")
//        );
//
//        deleteTransaction(id);
//    }
//
//    @Test
//    void searchDataKey() throws Exception {
//        long id = new Random().nextInt(10000);
//        User user = new User(id, System.currentTimeMillis(), "type", "actor", new ArrayList<>());
//        user.addAccount(new Account(null, null, "key", "value"));
//
//        List<User> transactions1 = getResponseAsArray("/transaction/search/data/key");
//        long count1 = transactions1.size();
//
//        createTransaction(user);
//
//        List<User> transactions2 = getResponseAsArray("/transaction/search/data/key");
//        long count2 = transactions2.size();
//
//        assertThat(count2).isEqualTo(count1 + 1);
//
//        transactions2.forEach(t -> {
//            long count = 0;
//            for (Account account : t.getAccounts()) {
//                if (account.getKey().equals("key"))
//                    count++;
//            }
//            assertThat(count).isEqualTo(1);
//        });
//
//        deleteTransaction(id);
//    }
//
//    private List<User> getResponseAsArray(String path) throws Exception {
//        MvcResult result = mockMvc.perform(get(path))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$").isArray())
//                .andReturn();
//        String content = result.getResponse().getContentAsString();
//        return objectMapper.readValue(content, new TypeReference<>() {
//        });
//    }
//
//    private void createTransaction(User user) throws Exception {
//        log.trace("Creating transaction with id " + user.getId());
//
//        mockMvc.perform(post("/transaction")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(objectMapper.writeValueAsString(user))
//                        .accept(MediaType.APPLICATION_JSON)
//                )
//                .andExpect(status().isCreated());
//    }
//
//    private void deleteTransaction(long id) throws Exception {
//        log.trace("Deleting transaction with id " + id);
//
//        mockMvc.perform(delete("/transaction/" + id))
//                .andExpect(status().is2xxSuccessful());
//    }

}
