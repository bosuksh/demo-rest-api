package me.doflamingo.demorestapi.index;

import me.doflamingo.demorestapi.common.BaseControllerTest;
import org.junit.jupiter.api.Test;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


class IndexControllerTest extends BaseControllerTest {


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