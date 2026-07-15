package lk.sampath.casadminPortalms.controller;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import lk.sampath.casadminportalms.CasAdminPortalMsApplication;
import lk.sampath.casadminportalms.controller.basecontroller.StandardResponse;
import lk.sampath.casadminportalms.dto.committetype.CommitteeTypeDTO;
import lk.sampath.casadminportalms.entity.committeetype.CommitteeType;
import lk.sampath.casadminportalms.enums.Status;
import lk.sampath.casadminportalms.service.CommitteeTypeService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

@ContextConfiguration(classes = CasAdminPortalMsApplication.class)
class CommitteeTypeControllerTest {

  @MockitoBean CommitteeTypeService committeeTypeService;

  @Autowired private MockMvc mockMvc;

  private CommitteeTypeDTO committeeTypeDTO;

  @Autowired private ObjectMapper objectMapper;

  @BeforeEach
  public void setUp() {
    Date newDate = new Date();
    committeeTypeDTO = new CommitteeTypeDTO();
    committeeTypeDTO.setCommitteeTypeId(1);
    committeeTypeDTO.setCommitteeType("Unit Testing");
    committeeTypeDTO.setCommitteeTypeDescription("Unit Testing Description");
    committeeTypeDTO.setCreatedDate(newDate);
    committeeTypeDTO.setCreatedBy("User");
    committeeTypeDTO.setStatus(Status.ACT);
    committeeTypeDTO.setModifiedDate(newDate);
    committeeTypeDTO.setModifiedBy("User");
    committeeTypeDTO.setIsSystem(1);
  }

  @Test
  void testGetCommitteeTypes() throws Exception {
    List<CommitteeType> mockList = List.of(new CommitteeType(), new CommitteeType());
    StandardResponse<List<CommitteeType>> response =
        new StandardResponse<>(true, "Success", mockList);
    when(committeeTypeService.getCommitteeTypes(any())).thenReturn(response);

    mockMvc
        .perform(get("/api/committeeType/getCommitteeType"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.response").isArray());
    verify(committeeTypeService, times(1)).getCommitteeTypes();
  }

  @Test
  void testSaveCommitteeType() throws Exception {
    CommitteeTypeDTO mockObj = new CommitteeTypeDTO();
    List<CommitteeTypeDTO> committeeTypeDTOS = new ArrayList<>();
    StandardResponse<List<CommitteeTypeDTO>> response =
        new StandardResponse<>(true, "Success", committeeTypeDTOS);
    when(committeeTypeService.saveCommitteeType(mockObj))
        .thenReturn(ResponseEntity.ok().body(response));

    mockMvc
        .perform(
            post("/api/committeeType/saveCommitteeType")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(mockObj)))
        .andExpect(status().isOk());
    verify(committeeTypeService, times(1)).saveCommitteeType(mockObj);
  }

  @Test
  void testUpdateCommitteeType() throws Exception {
    int mockId = 1;
    CommitteeTypeDTO mockObj = new CommitteeTypeDTO();
    List<CommitteeTypeDTO> committeeTypeDTOS = new ArrayList<>();
    mockObj.setCommitteeTypeId(1);
    StandardResponse<List<CommitteeTypeDTO>> response =
        new StandardResponse<>(true, "Success", committeeTypeDTOS);
    when(committeeTypeService.updateCommitteeType(mockId, mockObj))
        .thenReturn(ResponseEntity.ok().body(response));

    mockMvc
        .perform(
            post("/api/committeeType/updateCommitteeType/" + mockId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(mockObj)))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.response.committeeTypeId").value(mockId));
    verify(committeeTypeService, times(1)).updateCommitteeType(mockId, mockObj);
  }
}
