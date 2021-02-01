package me.doflamingo.demorestapi.index;

import me.doflamingo.demorestapi.common.RestDocsCustomizer;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureRestDocs
@Import(RestDocsCustomizer.class)
@ActiveProfiles("test")
class IndexControllerTest {

  @Autowired
  MockMvc mockMvc;

  @Test
  public void rootTest() throws Exception {
    //given

    //when
    mockMvc.perform(get("/api"))
    //then
    .andDo(print())
    .andExpect(status().isOk())
    .andExpect(jsonPath("_links.events").exists());
  }
}