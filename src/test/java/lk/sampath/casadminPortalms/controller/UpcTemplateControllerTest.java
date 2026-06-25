package lk.sampath.casadminPortalms.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import lk.sampath.casadminportalms.CasAdminPortalMsApplication;
import lk.sampath.casadminportalms.controller.UpcTemplateController;
import lk.sampath.casadminportalms.controller.basecontroller.StandardResponse;
import lk.sampath.casadminportalms.dto.common.ApproveRejectRQ;
import lk.sampath.casadminportalms.dto.upctemplate.UpcTemplateDTO;
import lk.sampath.casadminportalms.service.UpcTemplateService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(UpcTemplateController.class)
@ContextConfiguration(classes = CasAdminPortalMsApplication.class)
class UpcTemplateControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private UpcTemplateService upcTemplateService;

    @Test
    void testFindAllUpcTemplateTempList() throws Exception {
        List<UpcTemplateDTO> mockResponse = Arrays.asList(new UpcTemplateDTO());
        StandardResponse<List<UpcTemplateDTO>> response = new StandardResponse<>(true, "Success", mockResponse);
        when(upcTemplateService.findAllUpcTemplateTempList()).thenReturn(ResponseEntity.ok(response));
        
        mockMvc.perform(get("/api/upcTemplate/upcTemplateTemp/UpcTemplateTempList"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true));
    }

    @Test
    void testUpcTemplateTempById() throws Exception {
        Integer templateId = 1;
        StandardResponse<Object> mockResponse = new StandardResponse<>(true,"Success", new UpcTemplateDTO());
        when(upcTemplateService.findUpcTemplateTempById(templateId)).thenReturn(ResponseEntity.ok(mockResponse));

        mockMvc.perform(get("/api/upcTemplate/upcTemplateTemp/UpcTemplateTempById/{upcTemplateID}", templateId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true));
    }

    @Test
    void testFindAllApprovedUpcTemplateList() throws Exception {

        List<UpcTemplateDTO> mockResponse = Arrays.asList(new UpcTemplateDTO());
        StandardResponse<List<UpcTemplateDTO>> response = new StandardResponse<>(true, "Success",mockResponse);
        when(upcTemplateService.findAllApprovedUpcTemplates()).thenReturn(ResponseEntity.ok(response));

        mockMvc.perform(get("/api/upcTemplate/upcTemplateTemp/findAllApprovedUpcTemplates"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true));
    }

    @Test
    void testFindApprovedUpcTemplateById() throws Exception {

        Integer templateId = 1;
        StandardResponse<Object> mockResponse = new StandardResponse<>(true,"Success", new UpcTemplateDTO());
        when(upcTemplateService.findApprovedUpcTemplateById(templateId)).thenReturn(ResponseEntity.ok(mockResponse));

        mockMvc.perform(get("/api/upcTemplate/findApprovedUpcTemplateById/{upcTemplateID}", templateId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true));
    }

    @Test
    void testSaveUpcTemplate() throws Exception {

        UpcTemplateDTO dto = new UpcTemplateDTO();
        StandardResponse<Object> response = new StandardResponse<>(true,"Success", dto);
        when(upcTemplateService.saveUpcTemplate(any(UpcTemplateDTO.class))).thenReturn(ResponseEntity.ok(response));

        mockMvc.perform(MockMvcRequestBuilders.post("/api/upcTemplate")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(dto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true));
    }

    @Test
    void testUpdateUpcTemplate() throws Exception {

        Integer templateId = 1;
        UpcTemplateDTO dto = new UpcTemplateDTO();
        StandardResponse<Object> response = new StandardResponse<>(true,"Success", dto);
        when(upcTemplateService.updateUpcTemplateTemp(eq(templateId), any(UpcTemplateDTO.class)))
                .thenReturn(ResponseEntity.ok(response));

        mockMvc.perform(MockMvcRequestBuilders.post("/api/upcTemplate/updateUpcTemplateTemp/{upcTemplateID}", templateId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(dto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true));
    }

    @Test
    void testApproveRejectUpcTemplate() throws Exception {

        ApproveRejectRQ request = new ApproveRejectRQ();
        StandardResponse<Object> response = new StandardResponse<>(true, "Success",null);
        when(upcTemplateService.approveRejectUpcTemplate(any(ApproveRejectRQ.class)))
                .thenReturn(ResponseEntity.ok(response));

        mockMvc.perform(MockMvcRequestBuilders.post("/api/upcTemplate/approveRejectUpcTemplate")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true));
    }

    @Test
    void testUpdateApprovedUpcTemplate() throws Exception {

        Integer templateId = 1;
        UpcTemplateDTO dto = new UpcTemplateDTO();
        StandardResponse<Object> response = new StandardResponse<>(true,"Success", dto);
        when(upcTemplateService.updateApprovedUpcTemplate(eq(templateId), any(UpcTemplateDTO.class)))
                .thenReturn(ResponseEntity.ok(response));

        mockMvc.perform(MockMvcRequestBuilders.post("/api/upcTemplate/updateUpcTemplate/{upcTemplateID}", templateId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(dto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true));
    }

    @Test
    void testDeleteUpcTemplateFromTemp() throws Exception {

        UpcTemplateDTO dto = new UpcTemplateDTO();
        dto.setUpcTemplateID(1);
        StandardResponse<Void> response = new StandardResponse<>(true,"Success", null);
        when(upcTemplateService.deleteUpcTemplateFromTemp(1)).thenReturn(ResponseEntity.ok(response));

        mockMvc.perform(MockMvcRequestBuilders.post("/api/upcTemplate/upcTemplateTemp/deleteUpcTemplateFromTemp")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(dto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true));
    }
}