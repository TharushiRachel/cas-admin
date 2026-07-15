package lk.sampath.casadminPortalms.controller;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.Arrays;
import java.util.List;
import lk.sampath.casadminportalms.CasAdminPortalMsApplication;
import lk.sampath.casadminportalms.controller.CreditFacilityTemplateController;
import lk.sampath.casadminportalms.controller.basecontroller.StandardResponse;
import lk.sampath.casadminportalms.dto.creditfacilitytemplate.CreditFacilityTemplateDTO;
import lk.sampath.casadminportalms.service.CreditFacilityTemplateService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

@WebMvcTest(CreditFacilityTemplateController.class)
@ContextConfiguration(classes = CasAdminPortalMsApplication.class)
class CreditFacilityTemplateControllerTest {

  @Value("${app.endpoint.getAllCftTemp}")
  private String getAllCreditFacilityTemplatesTemp;

  @Value("${app.endpoint.getCftTempById}")
  private String getCreditFacilityTemplateTempById;

  @Value("${app.endpoint.getAllCftMaster}")
  private String getCreditFacilityTemplates;

  @Value("${app.endpoint.getCftById}")
  private String getCreditFacilityTemplateById;

  @Value("${app.endpoint.saveAndUpdateCftTemp}")
  private String saveCreditFacilityTemplateTemp;

  @Value("${app.endpoint.updateCftTemp}")
  private String updateCreditFacilityTemplateTemp;

  @Value("${app.endpoint.authorizeCft}")
  private String authorizeCreditFacilityTemplate;

  @Value("${app.endpoint.updateCft}")
  private String updateCreditFacilityTemplate;

  @Value("${app.endpoint.deleteCftTemp}")
  private String deleteCreditFacilityTemplateTemp;

  @Autowired private MockMvc mockMvc;

  @MockitoBean private CreditFacilityTemplateService creditFacilityTemplateService;

  private static final ObjectMapper objectMapper = new ObjectMapper();

  @BeforeEach
  public void setUp() {}

  @Test
  public void testGetAllCreditFacilityTemplatesTemp() throws Exception {
    // Arrange
    CreditFacilityTemplateDTO creditFacilityTemplateDTO = new CreditFacilityTemplateDTO();
    creditFacilityTemplateDTO.setCreditFacilityTemplateID(1);
    creditFacilityTemplateDTO.setCreditFacilityName("Template1");
    creditFacilityTemplateDTO.setDescription("Test Description");

    CreditFacilityTemplateDTO creditFacilityTemplateDTO2 = new CreditFacilityTemplateDTO();
    creditFacilityTemplateDTO2.setCreditFacilityTemplateID(
        2); // Use creditFacilityTemplateDTO2 here
    creditFacilityTemplateDTO2.setCreditFacilityName("Template2");
    creditFacilityTemplateDTO2.setDescription("Test Description2");

    List<CreditFacilityTemplateDTO> mockTemplates =
        Arrays.asList(creditFacilityTemplateDTO, creditFacilityTemplateDTO2);
    StandardResponse<List<CreditFacilityTemplateDTO>> response =
        new StandardResponse<>(true, "Success", mockTemplates);
    when(creditFacilityTemplateService.getAllCreditFacilityTemplatesTemp())
        .thenReturn(new ResponseEntity<>(response, HttpStatus.OK));

    mockMvc
        .perform(MockMvcRequestBuilders.get("/api/creditFacilityTemplates/getAllCftTemp"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.response", hasSize(2)));
  }

  @Test
  public void testGetCreditFacilityTemplateTempById() throws Exception {
    // Arrange
    CreditFacilityTemplateDTO creditFacilityTemplateDTO = new CreditFacilityTemplateDTO();
    creditFacilityTemplateDTO.setCreditFacilityTemplateID(1);
    creditFacilityTemplateDTO.setCreditFacilityName("Template1");
    creditFacilityTemplateDTO.setDescription("Test Description");

    StandardResponse<CreditFacilityTemplateDTO> response =
        new StandardResponse<>(true, "Success", creditFacilityTemplateDTO);
    when(creditFacilityTemplateService.getCreditFacilityTemplateTempByID(1))
        .thenReturn(new ResponseEntity<>(response, HttpStatus.OK));

    // Act & Assert
    mockMvc
        .perform(MockMvcRequestBuilders.get("/api/creditFacilityTemplates/getCftTempById/1"))
        .andExpect(status().isOk());
  }

  @Test
  public void testGetAllCreditFacilityTemplates() throws Exception {
    // Arrange
    CreditFacilityTemplateDTO creditFacilityTemplateDTO = new CreditFacilityTemplateDTO();
    creditFacilityTemplateDTO.setCreditFacilityTemplateID(1);
    creditFacilityTemplateDTO.setCreditFacilityName("Template1");
    creditFacilityTemplateDTO.setDescription("Test Description");

    CreditFacilityTemplateDTO creditFacilityTemplateDTO2 = new CreditFacilityTemplateDTO();
    creditFacilityTemplateDTO2.setCreditFacilityTemplateID(
        2); // Use creditFacilityTemplateDTO2 here
    creditFacilityTemplateDTO2.setCreditFacilityName("Template2");
    creditFacilityTemplateDTO2.setDescription("Test Description2");

    List<CreditFacilityTemplateDTO> mockTemplates =
        Arrays.asList(creditFacilityTemplateDTO, creditFacilityTemplateDTO2);
    StandardResponse<List<CreditFacilityTemplateDTO>> response =
        new StandardResponse<>(true, "Success", mockTemplates);
    when(creditFacilityTemplateService.getAllCreditFacilityTemplates())
        .thenReturn(new ResponseEntity<>(response, HttpStatus.OK));

    mockMvc
        .perform(MockMvcRequestBuilders.get("/api/creditFacilityTemplates/getAllCft"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.response", hasSize(2)));
  }

  @Test
  public void testGetCreditFacilityTemplateById() throws Exception {
    CreditFacilityTemplateDTO creditFacilityTemplateDTO = new CreditFacilityTemplateDTO();
    creditFacilityTemplateDTO.setCreditFacilityTemplateID(1);
    creditFacilityTemplateDTO.setCreditFacilityName("Template1");
    creditFacilityTemplateDTO.setDescription("Test Description");

    StandardResponse<CreditFacilityTemplateDTO> response =
        new StandardResponse<>(true, "Success", creditFacilityTemplateDTO);
    when(creditFacilityTemplateService.getCreditFacilityTemplateByID(1))
        .thenReturn(new ResponseEntity<>(response, HttpStatus.OK));

    mockMvc
        .perform(MockMvcRequestBuilders.get("/api/creditFacilityTemplates/getCftById/1"))
        .andExpect(status().isOk());
  }

  @Test
  public void testSaveCreditFacilityTemplateTemp() throws Exception {
    CreditFacilityTemplateDTO mockTemplate = new CreditFacilityTemplateDTO();
    mockTemplate.setCreditFacilityTemplateID(1);
    mockTemplate.setCreditFacilityName("Template1");
    mockTemplate.setDescription("Test Description");

    StandardResponse<CreditFacilityTemplateDTO> response =
        new StandardResponse<>(true, "Success", mockTemplate);
    when(creditFacilityTemplateService.saveCreditFacilityTemplateTemp(
            any(CreditFacilityTemplateDTO.class)))
        .thenReturn(new ResponseEntity<>(response, HttpStatus.OK));

    mockMvc
        .perform(
            MockMvcRequestBuilders.post("/api/creditFacilityTemplates/saveAndUpdateCftTemp")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(mockTemplate)))
        .andExpect(status().isOk());
  }
}
