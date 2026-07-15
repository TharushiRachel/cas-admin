package lk.sampath.casadminPortalms.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import lk.sampath.casadminportalms.controller.basecontroller.StandardResponse;
import lk.sampath.casadminportalms.dto.common.ApproveRejectRQ;
import lk.sampath.casadminportalms.dto.deviation.DeviationDTO;
import lk.sampath.casadminportalms.dto.deviation.DeviationTypeDTO;
import lk.sampath.casadminportalms.dto.usersession.UserContext;
import lk.sampath.casadminportalms.entity.deviation.Deviation;
import lk.sampath.casadminportalms.entity.deviation.DeviationAud;
import lk.sampath.casadminportalms.entity.deviation.DeviationType;
import lk.sampath.casadminportalms.entity.deviation.TempDeviation;
import lk.sampath.casadminportalms.enums.ErrorEnums;
import lk.sampath.casadminportalms.enums.MasterDataApproveStatus;
import lk.sampath.casadminportalms.exception.ApiRequestException;
import lk.sampath.casadminportalms.repository.deviation.DeviationAudRepository;
import lk.sampath.casadminportalms.repository.deviation.DeviationRepository;
import lk.sampath.casadminportalms.repository.deviation.DeviationTempRepository;
import lk.sampath.casadminportalms.repository.deviation.DeviationTypeRepository;
import lk.sampath.casadminportalms.service.impl.DeviationServiceImpl;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

@ExtendWith(MockitoExtension.class)
class DeviationServiceImplTest {

    @Mock private DeviationTypeRepository deviationTypeRepository;

    @Mock private DeviationRepository deviationRepository;

    @Mock private DeviationTempRepository deviationTempRepository;

    @Mock private DeviationAudRepository deviationAudRepository;

    @InjectMocks private DeviationServiceImpl deviationService;

    private DeviationType deviationType;

    private DeviationTypeDTO deviationTypeDTO;

    private TempDeviation tempDeviation;

    private Deviation deviation;

    private DeviationDTO deviationDTO;

    private ApproveRejectRQ approveRejectRQ;

    @BeforeEach
    void setup() {
        UserContext.setUsername("testUser");

        deviationType = new DeviationType();
        deviationType.setDeviationTypeId(1);
        deviationType.setDeviationType("Documentation Deviation");
        deviationType.setStatus("ACT");
        deviationType.setCreatedDate(new Date());
        deviationType.setCreatedBy("admin");

        deviationTypeDTO = new DeviationTypeDTO();
        deviationTypeDTO.setDeviationTypeId(1);
        deviationTypeDTO.setDeviationType("Documentation Deviation");
        deviationTypeDTO.setStatus("ACT");

        tempDeviation = new TempDeviation();
        tempDeviation.setDeviationId(1);
        tempDeviation.setDeviationType("Documentation Deviation");
        tempDeviation.setDescription("Temp deviation description");
        tempDeviation.setStatus("ACT");
        tempDeviation.setApproveStatus(MasterDataApproveStatus.PENDING);
        tempDeviation.setCreatedDate(new Date());
        tempDeviation.setCreatedBy("admin");

        deviation = new Deviation();
        deviation.setDeviationId(1);
        deviation.setDeviationType("Documentation Deviation");
        deviation.setDescription("Master deviation description");
        deviation.setStatus("ACT");
        deviation.setApproveStatus(MasterDataApproveStatus.APPROVED);

        deviationDTO = new DeviationDTO();
        deviationDTO.setDeviationId(1);
        deviationDTO.setDeviationType("Documentation Deviation");
        deviationDTO.setDescription("Updated deviation description");
        deviationDTO.setStatus("ACT");
        deviationDTO.setApproveStatus(MasterDataApproveStatus.APPROVED);
        deviationDTO.setModifiedBy("admin");

        approveRejectRQ = new ApproveRejectRQ();
        approveRejectRQ.setApproveRejectDataID(1);
        approveRejectRQ.setApproveStatus(MasterDataApproveStatus.APPROVED);
    }

    @AfterEach
    void tearDown() {
        UserContext.clear();
    }

    /** saveDeviationType * */
    @Test
    void saveDeviationType_whenNewType_shouldCreateAndReturnSuccess() {
        deviationTypeDTO.setDeviationTypeId(null);

        ResponseEntity<StandardResponse<DeviationTypeDTO>> response =
                deviationService.saveDeviationType(deviationTypeDTO);

        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(ErrorEnums.SUCCESS_CODE.getStatus(), response.getBody().getSuccess());

        DeviationTypeDTO saved = (DeviationTypeDTO) response.getBody().getResponse();
        assertEquals("Documentation Deviation", saved.getDeviationType());

        verify(deviationTypeRepository, never()).findById(any());
        verify(deviationTypeRepository).save(any(DeviationType.class));
    }

    @Test
    void saveDeviationType_whenIdIsZero_shouldCreateNewType() {
        deviationTypeDTO.setDeviationTypeId(0);

        ResponseEntity<StandardResponse<DeviationTypeDTO>> response =
                deviationService.saveDeviationType(deviationTypeDTO);

        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        verify(deviationTypeRepository, never()).findById(any());
        verify(deviationTypeRepository).save(any(DeviationType.class));
    }

    @Test
    void saveDeviationType_whenExistingId_shouldUpdateType() {
        when(deviationTypeRepository.findById(1)).thenReturn(Optional.of(deviationType));
        deviationTypeDTO.setDeviationType("Updated Deviation Type");

        ResponseEntity<StandardResponse<DeviationTypeDTO>> response =
                deviationService.saveDeviationType(deviationTypeDTO);

        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        DeviationTypeDTO saved = (DeviationTypeDTO) response.getBody().getResponse();
        assertEquals("Updated Deviation Type", saved.getDeviationType());

        ArgumentCaptor<DeviationType> captor = ArgumentCaptor.forClass(DeviationType.class);
        verify(deviationTypeRepository).save(captor.capture());
        assertNotNull(captor.getValue().getLastModifiedDate());
        verify(deviationTypeRepository).findById(1);
    }

    @Test
    void saveDeviationType_whenIdNotFound_shouldThrowApiRequestException() {
        when(deviationTypeRepository.findById(99)).thenReturn(Optional.empty());
        deviationTypeDTO.setDeviationTypeId(99);

        ApiRequestException exception =
                assertThrows(
                        ApiRequestException.class,
                        () -> deviationService.saveDeviationType(deviationTypeDTO));

        assertEquals("Deviation Type not found with ID: 99", exception.getMessage());
        verify(deviationTypeRepository, never()).save(any(DeviationType.class));
    }

    @Test
    void saveDeviationType_whenSaveThrowsUnexpectedException_shouldWrapAsApiRequestException() {
        deviationTypeDTO.setDeviationTypeId(null);
        when(deviationTypeRepository.save(any(DeviationType.class)))
                .thenThrow(new RuntimeException("DB connection lost"));

        ApiRequestException exception =
                assertThrows(
                        ApiRequestException.class,
                        () -> deviationService.saveDeviationType(deviationTypeDTO));

        assertEquals("Failed to save deviation type", exception.getMessage());
    }

    @Test
    void saveDeviationType_shouldMapDeviationTypeAndStatusFields() {
        deviationTypeDTO.setDeviationTypeId(null);
        deviationTypeDTO.setDeviationType("New Category");
        deviationTypeDTO.setStatus("INA");

        deviationService.saveDeviationType(deviationTypeDTO);

        ArgumentCaptor<DeviationType> captor = ArgumentCaptor.forClass(DeviationType.class);
        verify(deviationTypeRepository).save(captor.capture());
        assertEquals("New Category", captor.getValue().getDeviationType());
        assertEquals("INA", captor.getValue().getStatus());
        assertNotNull(captor.getValue().getCreatedDate());
    }

    /** getAllDeviationTypes * */
    @Test
    void getAllDeviationTypes_whenListNotEmpty_shouldReturnMappedList() {
        when(deviationTypeRepository.findAll()).thenReturn(List.of(deviationType));

        ResponseEntity<StandardResponse<List<DeviationTypeDTO>>> response =
                deviationService.getAllDeviationTypes();

        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        List<DeviationTypeDTO> list = (List<DeviationTypeDTO>) response.getBody().getResponse();
        assertEquals(1, list.size());
        assertEquals(deviationType.getDeviationType(), list.get(0).getDeviationType());
        verify(deviationTypeRepository).findAll();
    }

    @Test
    void getAllDeviationTypes_whenListEmpty_shouldReturnEmptyList() {
        when(deviationTypeRepository.findAll()).thenReturn(Collections.emptyList());

        ResponseEntity<StandardResponse<List<DeviationTypeDTO>>> response =
                deviationService.getAllDeviationTypes();

        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        List<DeviationTypeDTO> list = (List<DeviationTypeDTO>) response.getBody().getResponse();
        assertNotNull(list);
        assertTrue(list.isEmpty());
        verify(deviationTypeRepository).findAll();
    }

    @Test
    void getAllDeviationTypes_whenRepositoryThrowsException_shouldThrowApiRequestException() {
        when(deviationTypeRepository.findAll()).thenThrow(new RuntimeException("DB error"));

        ApiRequestException exception =
                assertThrows(ApiRequestException.class, () -> deviationService.getAllDeviationTypes());

        assertEquals("Failed to get all deviation types", exception.getMessage());
    }

    @Test
    void getAllDeviationTypes_withMultipleTypes_shouldReturnAllMapped() {
        DeviationType secondType = new DeviationType();
        secondType.setDeviationTypeId(2);
        secondType.setDeviationType("Financial Deviation");
        secondType.setStatus("ACT");

        when(deviationTypeRepository.findAll()).thenReturn(List.of(deviationType, secondType));

        ResponseEntity<StandardResponse<List<DeviationTypeDTO>>> response =
                deviationService.getAllDeviationTypes();

        List<DeviationTypeDTO> list = (List<DeviationTypeDTO>) response.getBody().getResponse();
        assertEquals(2, list.size());
    }

    @Test
    void getAllDeviationTypes_shouldCallRepositoryFindAllExactlyOnce() {
        when(deviationTypeRepository.findAll()).thenReturn(List.of(deviationType));

        deviationService.getAllDeviationTypes();

        verify(deviationTypeRepository).findAll();
        verifyNoMoreInteractions(deviationTypeRepository);
    }

    /** getDeviationTypeById * */
    @Test
    void getDeviationTypeById_whenFound_shouldReturnDto() {
        when(deviationTypeRepository.findById(1)).thenReturn(Optional.of(deviationType));

        ResponseEntity<StandardResponse<DeviationTypeDTO>> response =
                deviationService.getDeviationTypeById(1);

        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        DeviationTypeDTO dto = (DeviationTypeDTO) response.getBody().getResponse();
        assertEquals(deviationType.getDeviationType(), dto.getDeviationType());
        verify(deviationTypeRepository).findById(1);
    }

    @Test
    void getDeviationTypeById_whenNotFound_shouldThrowApiRequestException() {
        when(deviationTypeRepository.findById(99)).thenReturn(Optional.empty());

        ApiRequestException exception =
                assertThrows(
                        ApiRequestException.class, () -> deviationService.getDeviationTypeById(99));

        assertEquals("Deviation Type not found with ID: 99", exception.getMessage());
    }

    @Test
    void getDeviationTypeById_shouldCallRepositoryFindByIdWithGivenId() {
        when(deviationTypeRepository.findById(1)).thenReturn(Optional.of(deviationType));

        deviationService.getDeviationTypeById(1);

        verify(deviationTypeRepository).findById(1);
    }

    @Test
    void getDeviationTypeById_withDifferentId_shouldReturnCorrectDto() {
        DeviationType other = new DeviationType();
        other.setDeviationTypeId(2);
        other.setDeviationType("Operational Deviation");
        other.setStatus("ACT");
        when(deviationTypeRepository.findById(2)).thenReturn(Optional.of(other));

        ResponseEntity<StandardResponse<DeviationTypeDTO>> response =
                deviationService.getDeviationTypeById(2);

        DeviationTypeDTO dto = (DeviationTypeDTO) response.getBody().getResponse();
        assertEquals(2, dto.getDeviationTypeId());
        assertEquals("Operational Deviation", dto.getDeviationType());
    }

    @Test
    void getDeviationTypeById_whenNotFound_shouldNotInteractWithOtherRepositories() {
        when(deviationTypeRepository.findById(5)).thenReturn(Optional.empty());

        assertThrows(ApiRequestException.class, () -> deviationService.getDeviationTypeById(5));

        verifyNoInteractions(deviationRepository, deviationTempRepository, deviationAudRepository);
    }

    /** saveOrUpdateDeviation * */
    @Test
    void saveOrUpdateDeviation_whenNewDeviation_shouldCreateWithSequenceId() {
        deviationDTO.setDeviationId(null);
        when(deviationTempRepository.getCurrentSequenceValue()).thenReturn(10);

        ResponseEntity<StandardResponse<DeviationDTO>> response =
                deviationService.saveOrUpdateDeviation(deviationDTO);

        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        DeviationDTO saved = (DeviationDTO) response.getBody().getResponse();
        assertEquals(10, saved.getDeviationId());
        verify(deviationTempRepository).getCurrentSequenceValue();
        verify(deviationTempRepository).save(any(TempDeviation.class));
    }

    @Test
    void saveOrUpdateDeviation_whenIdIsZero_shouldCreateNewWithSequenceId() {
        deviationDTO.setDeviationId(0);
        when(deviationTempRepository.getCurrentSequenceValue()).thenReturn(11);

        ResponseEntity<StandardResponse<DeviationDTO>> response =
                deviationService.saveOrUpdateDeviation(deviationDTO);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        verify(deviationTempRepository).getCurrentSequenceValue();
        verify(deviationTempRepository, never()).findById(any());
    }

    @Test
    void saveOrUpdateDeviation_whenExistingId_shouldUpdateTempDeviation() {
        when(deviationTempRepository.findById(1)).thenReturn(Optional.of(tempDeviation));
        deviationDTO.setDeviationId(1);
        deviationDTO.setDescription("Updated description");

        ResponseEntity<StandardResponse<DeviationDTO>> response =
                deviationService.saveOrUpdateDeviation(deviationDTO);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        DeviationDTO saved = (DeviationDTO) response.getBody().getResponse();
        assertEquals("Updated description", saved.getDescription());

        ArgumentCaptor<TempDeviation> captor = ArgumentCaptor.forClass(TempDeviation.class);
        verify(deviationTempRepository).save(captor.capture());
        assertNotNull(captor.getValue().getLastModifiedDate());
        verify(deviationTempRepository, never()).getCurrentSequenceValue();
    }

    @Test
    void saveOrUpdateDeviation_whenIdNotFound_shouldThrowApiRequestException() {
        when(deviationTempRepository.findById(50)).thenReturn(Optional.empty());
        deviationDTO.setDeviationId(50);

        ApiRequestException exception =
                assertThrows(
                        ApiRequestException.class,
                        () -> deviationService.saveOrUpdateDeviation(deviationDTO));

        assertEquals("Deviation Type not found with ID: 50", exception.getMessage());
    }

    @Test
    void saveOrUpdateDeviation_whenSaveThrowsUnexpectedException_shouldWrapAsApiRequestException() {
        deviationDTO.setDeviationId(null);
        when(deviationTempRepository.getCurrentSequenceValue()).thenReturn(1);
        when(deviationTempRepository.save(any(TempDeviation.class)))
                .thenThrow(new RuntimeException("DB error"));

        ApiRequestException exception =
                assertThrows(
                        ApiRequestException.class,
                        () -> deviationService.saveOrUpdateDeviation(deviationDTO));

        assertEquals("Failed to save deviation type", exception.getMessage());
    }

    @Test
    void saveOrUpdateDeviation_shouldMapAllDeviationFields() {
        deviationDTO.setDeviationId(null);
        deviationDTO.setDeviationType("Credit Deviation");
        deviationDTO.setDescription("Some description");
        deviationDTO.setStatus("ACT");
        deviationDTO.setApproveStatus(MasterDataApproveStatus.DRAFT);
        when(deviationTempRepository.getCurrentSequenceValue()).thenReturn(20);

        deviationService.saveOrUpdateDeviation(deviationDTO);

        ArgumentCaptor<TempDeviation> captor = ArgumentCaptor.forClass(TempDeviation.class);
        verify(deviationTempRepository).save(captor.capture());
        TempDeviation savedEntity = captor.getValue();
        assertEquals("Credit Deviation", savedEntity.getDeviationType());
        assertEquals("Some description", savedEntity.getDescription());
        assertEquals("ACT", savedEntity.getStatus());
        assertEquals(MasterDataApproveStatus.DRAFT, savedEntity.getApproveStatus());
    }

    /** approveOrRejectDeviation * */
    @Test
    void approveOrRejectDeviation_whenRequestIsNull_shouldThrowApiRequestException() {
        ApiRequestException exception =
                assertThrows(
                        ApiRequestException.class, () -> deviationService.approveOrRejectDeviation(null));

        assertEquals("Deviation ID is required for approval/rejection", exception.getMessage());
        verifyNoInteractions(deviationTempRepository);
    }

    @Test
    void approveOrRejectDeviation_whenApproveRejectDataIdIsNull_shouldThrowApiRequestException() {
        approveRejectRQ.setApproveRejectDataID(null);

        ApiRequestException exception =
                assertThrows(
                        ApiRequestException.class,
                        () -> deviationService.approveOrRejectDeviation(approveRejectRQ));

        assertEquals("Deviation ID is required for approval/rejection", exception.getMessage());
    }

    @Test
    void approveOrRejectDeviation_whenTempDeviationNotFound_shouldThrowApiRequestException() {
        when(deviationTempRepository.findById(1)).thenReturn(Optional.empty());

        ApiRequestException exception =
                assertThrows(
                        ApiRequestException.class,
                        () -> deviationService.approveOrRejectDeviation(approveRejectRQ));

        assertEquals("Deviation not found with ID: 1", exception.getMessage());
    }

    @Test
    void approveOrRejectDeviation_whenApprovedAndNoExistingMasterRecord_shouldCreateNewDeviation() {
        when(deviationTempRepository.findById(1)).thenReturn(Optional.of(tempDeviation));
        when(deviationRepository.findById(1)).thenReturn(Optional.empty());
        when(deviationRepository.save(any(Deviation.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        ResponseEntity<StandardResponse<DeviationDTO>> response =
                deviationService.approveOrRejectDeviation(approveRejectRQ);

        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        DeviationDTO dto = (DeviationDTO) response.getBody().getResponse();
        assertFalse(dto.isTempRecord());

        ArgumentCaptor<Deviation> captor = ArgumentCaptor.forClass(Deviation.class);
        verify(deviationRepository).save(captor.capture());
        assertEquals(1, captor.getValue().getDeviationId());
        verify(deviationTempRepository).delete(tempDeviation);
    }

    @Test
    void approveOrRejectDeviation_whenApprovedAndExistingMasterRecordPresent_shouldUpdateExisting() {
        when(deviationTempRepository.findById(1)).thenReturn(Optional.of(tempDeviation));
        when(deviationRepository.findById(1)).thenReturn(Optional.of(deviation));
        when(deviationRepository.save(any(Deviation.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        ResponseEntity<StandardResponse<DeviationDTO>> response =
                deviationService.approveOrRejectDeviation(approveRejectRQ);

        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        verify(deviationRepository).save(deviation);
        verify(deviationTempRepository).delete(tempDeviation);
    }

    @Test
    void approveOrRejectDeviation_whenRejected_shouldSaveAuditAndReturnTempBasedDto() {
        approveRejectRQ.setApproveStatus(MasterDataApproveStatus.REJECTED);
        when(deviationTempRepository.findById(1)).thenReturn(Optional.of(tempDeviation));

        ResponseEntity<StandardResponse<DeviationDTO>> response =
                deviationService.approveOrRejectDeviation(approveRejectRQ);

        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        DeviationDTO dto = (DeviationDTO) response.getBody().getResponse();
        assertTrue(dto.isTempRecord());

        verify(deviationAudRepository).save(any(DeviationAud.class));
        verify(deviationRepository, never()).save(any(Deviation.class));
        verify(deviationTempRepository, never()).delete(any(TempDeviation.class));
    }

    @Test
    void approveOrRejectDeviation_whenApproveStatusIsInvalid_shouldThrowApiRequestException() {
        approveRejectRQ.setApproveStatus(MasterDataApproveStatus.PENDING);
        when(deviationTempRepository.findById(1)).thenReturn(Optional.of(tempDeviation));

        ApiRequestException exception =
                assertThrows(
                        ApiRequestException.class,
                        () -> deviationService.approveOrRejectDeviation(approveRejectRQ));

        assertEquals("Invalid approval status", exception.getMessage());
    }

    @Test
    void approveOrRejectDeviation_whenApproved_shouldSaveAuditRecordWithMatchingFields() {
        when(deviationTempRepository.findById(1)).thenReturn(Optional.of(tempDeviation));
        when(deviationRepository.findById(1)).thenReturn(Optional.empty());
        when(deviationRepository.save(any(Deviation.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        deviationService.approveOrRejectDeviation(approveRejectRQ);

        ArgumentCaptor<DeviationAud> captor = ArgumentCaptor.forClass(DeviationAud.class);
        verify(deviationAudRepository).save(captor.capture());
        DeviationAud audit = captor.getValue();
        assertEquals(tempDeviation.getDeviationId(), audit.getDeviationId());
        assertEquals(tempDeviation.getDeviationType(), audit.getDeviationType());
        assertEquals(tempDeviation.getDescription(), audit.getDescription());
        assertEquals(MasterDataApproveStatus.APPROVED, audit.getApproveStatus());
    }

    @Test
    void approveOrRejectDeviation_shouldSetApprovedByFromUserContextBeforeSavingTemp() {
        when(deviationTempRepository.findById(1)).thenReturn(Optional.of(tempDeviation));
        when(deviationRepository.findById(1)).thenReturn(Optional.empty());
        when(deviationRepository.save(any(Deviation.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        deviationService.approveOrRejectDeviation(approveRejectRQ);

        ArgumentCaptor<TempDeviation> captor = ArgumentCaptor.forClass(TempDeviation.class);
        verify(deviationTempRepository).save(captor.capture());
        assertEquals("testUser", captor.getValue().getApprovedBy());
        assertNotNull(captor.getValue().getApprovedDate());
        assertEquals(MasterDataApproveStatus.APPROVED, captor.getValue().getApproveStatus());
    }

    /** updateApprovedDiversion * */
    @Test
    void updateApprovedDiversion_whenFound_shouldMapAndSaveTemp() {
        when(deviationRepository.findById(1)).thenReturn(Optional.of(deviation));
        when(deviationTempRepository.saveAndFlush(any(TempDeviation.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        ResponseEntity<StandardResponse<DeviationDTO>> response =
                deviationService.updateApprovedDiversion(deviationDTO);

        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        DeviationDTO dto = (DeviationDTO) response.getBody().getResponse();
        assertTrue(dto.isTempRecord());
        assertEquals(deviationDTO.getDescription(), dto.getDescription());
        verify(deviationTempRepository).saveAndFlush(any(TempDeviation.class));
    }

    @Test
    void updateApprovedDiversion_whenNotFound_shouldThrowApiRequestException() {
        deviationDTO.setDeviationId(77);
        when(deviationRepository.findById(77)).thenReturn(Optional.empty());

        ApiRequestException exception =
                assertThrows(
                        ApiRequestException.class,
                        () -> deviationService.updateApprovedDiversion(deviationDTO));

        assertEquals("Deviation not found with ID: 77", exception.getMessage());
        verify(deviationTempRepository, never()).saveAndFlush(any(TempDeviation.class));
    }

    @Test
    void updateApprovedDiversion_shouldPreserveCreatedFieldsFromExistingDeviation() {
        deviation.setCreatedBy("originalCreator");
        Date originalCreatedDate = new Date(System.currentTimeMillis() - 100000);
        deviation.setCreatedDate(originalCreatedDate);
        when(deviationRepository.findById(1)).thenReturn(Optional.of(deviation));
        when(deviationTempRepository.saveAndFlush(any(TempDeviation.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        deviationService.updateApprovedDiversion(deviationDTO);

        ArgumentCaptor<TempDeviation> captor = ArgumentCaptor.forClass(TempDeviation.class);
        verify(deviationTempRepository).saveAndFlush(captor.capture());
        assertEquals("originalCreator", captor.getValue().getCreatedBy());
        assertEquals(originalCreatedDate, captor.getValue().getCreatedDate());
    }

    @Test
    void updateApprovedDiversion_shouldSetNewModifiedFieldsFromDto() {
        deviationDTO.setModifiedBy("editorUser");
        when(deviationRepository.findById(1)).thenReturn(Optional.of(deviation));
        when(deviationTempRepository.saveAndFlush(any(TempDeviation.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        deviationService.updateApprovedDiversion(deviationDTO);

        ArgumentCaptor<TempDeviation> captor = ArgumentCaptor.forClass(TempDeviation.class);
        verify(deviationTempRepository).saveAndFlush(captor.capture());
        assertEquals("editorUser", captor.getValue().getModifiedBy());
        assertNotNull(captor.getValue().getLastModifiedDate());
    }

    @Test
    void updateApprovedDiversion_shouldCallFindByIdWithDeviationIdFromDto() {
        when(deviationRepository.findById(1)).thenReturn(Optional.of(deviation));
        when(deviationTempRepository.saveAndFlush(any(TempDeviation.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        deviationService.updateApprovedDiversion(deviationDTO);

        verify(deviationRepository).findById(1);
        verify(deviationTempRepository).saveAndFlush(any(TempDeviation.class));
    }

    /** getAllDeviationTempList * */
    @Test
    void getAllDeviationTempList_whenListNotEmpty_shouldReturnMappedList() {
        when(deviationTempRepository.findAll()).thenReturn(List.of(tempDeviation));

        ResponseEntity<StandardResponse<List<DeviationDTO>>> response =
                deviationService.getAllDeviationTempList();

        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        List<DeviationDTO> list = (List<DeviationDTO>) response.getBody().getResponse();
        assertEquals(1, list.size());
        verify(deviationTempRepository).findAll();
    }

    @Test
    void getAllDeviationTempList_whenListEmpty_shouldReturnEmptyList() {
        when(deviationTempRepository.findAll()).thenReturn(Collections.emptyList());

        ResponseEntity<StandardResponse<List<DeviationDTO>>> response =
                deviationService.getAllDeviationTempList();

        List<DeviationDTO> list = (List<DeviationDTO>) response.getBody().getResponse();
        assertNotNull(list);
        assertTrue(list.isEmpty());
    }

    @Test
    void getAllDeviationTempList_withMultipleRecords_shouldReturnAllMapped() {
        TempDeviation second = new TempDeviation();
        second.setDeviationId(2);
        second.setDeviationType("Second Type");
        second.setStatus("ACT");
        when(deviationTempRepository.findAll()).thenReturn(List.of(tempDeviation, second));

        ResponseEntity<StandardResponse<List<DeviationDTO>>> response =
                deviationService.getAllDeviationTempList();

        List<DeviationDTO> list = (List<DeviationDTO>) response.getBody().getResponse();
        assertEquals(2, list.size());
    }

    @Test
    void getAllDeviationTempList_shouldMarkAllRecordsAsTempRecord() {
        when(deviationTempRepository.findAll()).thenReturn(List.of(tempDeviation));

        ResponseEntity<StandardResponse<List<DeviationDTO>>> response =
                deviationService.getAllDeviationTempList();

        List<DeviationDTO> list = (List<DeviationDTO>) response.getBody().getResponse();
        assertTrue(list.get(0).isTempRecord());
    }

    @Test
    void getAllDeviationTempList_shouldCallFindAllExactlyOnce() {
        when(deviationTempRepository.findAll()).thenReturn(List.of(tempDeviation));

        deviationService.getAllDeviationTempList();

        verify(deviationTempRepository).findAll();
    }

    /** getDeviationTempById * */
    @Test
    void getDeviationTempById_whenFound_shouldReturnDto() {
        when(deviationTempRepository.findById(1)).thenReturn(Optional.of(tempDeviation));

        ResponseEntity<StandardResponse<DeviationDTO>> response =
                deviationService.getDeviationTempById(1);

        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        DeviationDTO dto = (DeviationDTO) response.getBody().getResponse();
        assertEquals(tempDeviation.getDescription(), dto.getDescription());
        assertTrue(dto.isTempRecord());
    }

    @Test
    void getDeviationTempById_whenNotFound_shouldThrowApiRequestException() {
        when(deviationTempRepository.findById(88)).thenReturn(Optional.empty());

        ApiRequestException exception =
                assertThrows(
                        ApiRequestException.class, () -> deviationService.getDeviationTempById(88));

        assertEquals("Deviation Temp not found with ID: 88", exception.getMessage());
    }

    @Test
    void getDeviationTempById_shouldCallFindByIdWithGivenId() {
        when(deviationTempRepository.findById(1)).thenReturn(Optional.of(tempDeviation));

        deviationService.getDeviationTempById(1);

        verify(deviationTempRepository).findById(1);
    }

    @Test
    void getDeviationTempById_withDifferentId_shouldReturnCorrectDto() {
        TempDeviation other = new TempDeviation();
        other.setDeviationId(3);
        other.setDeviationType("Other Type");
        other.setDescription("Other description");
        when(deviationTempRepository.findById(3)).thenReturn(Optional.of(other));

        ResponseEntity<StandardResponse<DeviationDTO>> response =
                deviationService.getDeviationTempById(3);

        DeviationDTO dto = (DeviationDTO) response.getBody().getResponse();
        assertEquals(3, dto.getDeviationId());
        assertEquals("Other description", dto.getDescription());
    }

    @Test
    void getDeviationTempById_whenNotFound_shouldNotInteractWithOtherRepositories() {
        when(deviationTempRepository.findById(4)).thenReturn(Optional.empty());

        assertThrows(ApiRequestException.class, () -> deviationService.getDeviationTempById(4));

        verifyNoInteractions(deviationRepository, deviationTypeRepository, deviationAudRepository);
    }

    /** getAllDeviationMasterList * */
    @Test
    void getAllDeviationMasterList_whenListNotEmpty_shouldReturnMappedList() {
        when(deviationRepository.findAll()).thenReturn(List.of(deviation));

        ResponseEntity<StandardResponse<List<DeviationDTO>>> response =
                deviationService.getAllDeviationMasterList();

        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        List<DeviationDTO> list = (List<DeviationDTO>) response.getBody().getResponse();
        assertEquals(1, list.size());
        verify(deviationRepository).findAll();
    }

    @Test
    void getAllDeviationMasterList_whenListEmpty_shouldReturnEmptyList() {
        when(deviationRepository.findAll()).thenReturn(Collections.emptyList());

        ResponseEntity<StandardResponse<List<DeviationDTO>>> response =
                deviationService.getAllDeviationMasterList();

        List<DeviationDTO> list = (List<DeviationDTO>) response.getBody().getResponse();
        assertNotNull(list);
        assertTrue(list.isEmpty());
    }

    @Test
    void getAllDeviationMasterList_withMultipleRecords_shouldReturnAllMapped() {
        Deviation second = new Deviation();
        second.setDeviationId(2);
        second.setDeviationType("Second Master Type");
        second.setStatus("ACT");
        when(deviationRepository.findAll()).thenReturn(List.of(deviation, second));

        ResponseEntity<StandardResponse<List<DeviationDTO>>> response =
                deviationService.getAllDeviationMasterList();

        List<DeviationDTO> list = (List<DeviationDTO>) response.getBody().getResponse();
        assertEquals(2, list.size());
    }

    @Test
    void getAllDeviationMasterList_shouldMarkRecordsAsNotTempRecord() {
        when(deviationRepository.findAll()).thenReturn(List.of(deviation));

        ResponseEntity<StandardResponse<List<DeviationDTO>>> response =
                deviationService.getAllDeviationMasterList();

        List<DeviationDTO> list = (List<DeviationDTO>) response.getBody().getResponse();
        assertFalse(list.get(0).isTempRecord());
    }

    @Test
    void getAllDeviationMasterList_shouldCallFindAllExactlyOnce() {
        when(deviationRepository.findAll()).thenReturn(List.of(deviation));

        deviationService.getAllDeviationMasterList();

        verify(deviationRepository).findAll();
    }

    /** getDeviationMasterById * */
    @Test
    void getDeviationMasterById_whenFound_shouldReturnDto() {
        when(deviationRepository.findById(1)).thenReturn(Optional.of(deviation));

        ResponseEntity<StandardResponse<DeviationDTO>> response =
                deviationService.getDeviationMasterById(1);

        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        DeviationDTO dto = (DeviationDTO) response.getBody().getResponse();
        assertEquals(deviation.getDescription(), dto.getDescription());
        assertFalse(dto.isTempRecord());
    }

    @Test
    void getDeviationMasterById_whenNotFound_shouldThrowApiRequestException() {
        when(deviationRepository.findById(66)).thenReturn(Optional.empty());

        ApiRequestException exception =
                assertThrows(
                        ApiRequestException.class, () -> deviationService.getDeviationMasterById(66));

        assertEquals("Deviation Master not found with ID: 66", exception.getMessage());
    }

    @Test
    void getDeviationMasterById_shouldCallFindByIdWithGivenId() {
        when(deviationRepository.findById(1)).thenReturn(Optional.of(deviation));

        deviationService.getDeviationMasterById(1);

        verify(deviationRepository).findById(1);
    }

    @Test
    void getDeviationMasterById_withDifferentId_shouldReturnCorrectDto() {
        Deviation other = new Deviation();
        other.setDeviationId(9);
        other.setDeviationType("Another Type");
        other.setDescription("Another description");
        when(deviationRepository.findById(9)).thenReturn(Optional.of(other));

        ResponseEntity<StandardResponse<DeviationDTO>> response =
                deviationService.getDeviationMasterById(9);

        DeviationDTO dto = (DeviationDTO) response.getBody().getResponse();
        assertEquals(9, dto.getDeviationId());
        assertEquals("Another description", dto.getDescription());
    }

    @Test
    void getDeviationMasterById_whenNotFound_shouldNotInteractWithOtherRepositories() {
        when(deviationRepository.findById(10)).thenReturn(Optional.empty());

        assertThrows(ApiRequestException.class, () -> deviationService.getDeviationMasterById(10));

        verifyNoInteractions(
                deviationTempRepository, deviationTypeRepository, deviationAudRepository);
    }
}