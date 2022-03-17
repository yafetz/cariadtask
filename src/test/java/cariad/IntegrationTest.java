package cariad;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(SpringExtension.class)
@WebMvcTest(Controller.class)
class IntegrationTest {

    @Autowired
    private MockMvc mvc;


    @Test
    void queryParameter() throws Exception {
        mvc.perform(get("http://localhost:8080/strings?/u=http://127.0.0.1:8090/fibo&u=http://127.0.0.1:8090/primes")).andExpect(content().string("\"strings\": \"\\\"[\\\"eleven\\\", \\\"five\\\", \\\"seven\\\", \\\"thirteen\\\", \\\"three\\\", \\\"two\\\"]\\\"\""));
    }
}