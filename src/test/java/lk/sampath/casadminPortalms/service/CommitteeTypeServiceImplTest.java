package lk.sampath.casadminPortalms.service;

import lk.sampath.casadminportalms.controller.basecontroller.StandardResponse;
import lk.sampath.casadminportalms.dto.committetype.CommitteeTypeDTO;
import lk.sampath.casadminportalms.entity.committeetype.CommitteeType;
import lk.sampath.casadminportalms.enums.ErrorEnums;
import lk.sampath.casadminportalms.enums.Status;
import lk.sampath.casadminportalms.exception.ApiRequestException;
import lk.sampath.casadminportalms.repository.committeetype.CommitteeTypeRepository;
import lk.sampath.casadminportalms.service.impl.CommitteeTypeServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.modelmapper.ModelMapper;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class CommitteeTypeServiceImplTest {

    @Mock
    CommitteeTypeRepository committeeTypeRepository;

    @Mock
    CommitteeTypeDTO committeeTypeDTO;

    @Mock
    CommitteeType committeeType;

    private final ModelMapper modelMapper = Mockito.mock(ModelMapper.class);

    @InjectMocks
    CommitteeTypeServiceImpl committeeTypeServiceImpl;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
        Date date = new Date();

        committeeTypeDTO = new CommitteeTypeDTO();
        committeeTypeDTO.setCommitteeTypeId(1);
        committeeTypeDTO.setCommitteeType("BCC");
        committeeTypeDTO.setCommitteeTypeDescription("Board Credit Committee");
        committeeTypeDTO.setCreatedBy("SYSTEM");
        committeeTypeDTO.setCreatedDate(date);
        committeeTypeDTO.setStatus(Status.ACT);
        committeeTypeDTO.setCreatedUserDisplayName("SYSTEM");
        committeeTypeDTO.setAuthorizerDisplayName("SYSTEM");
        committeeTypeDTO.setModifiedDate(date);
        committeeTypeDTO.setModifiedBy("SYSTEM");
        committeeTypeDTO.setVersion(1);
        committeeTypeDTO.setIsSystem(1);

        committeeType = new CommitteeType();
        committeeType.setCommitteeTypeId(1);
        committeeType.setCommitteeType("BCC");
        committeeType.setCommitteeTypeDescription("Board Credit Committee");
        committeeType.setCreatedBy("SYSTEM");
        committeeType.setCreatedDate(date);
        committeeType.setStatus("ACT");
        committeeType.setCreatedUserDisplayName("SYSTEM");
        committeeType.setAuthorizerDisplayName("SYSTEM");
        committeeType.setModifiedDate(date);
        committeeType.setModifiedBy("SYSTEM");
        committeeType.setVersion(1);
        committeeType.setIsSystem(1);
    }

    @Test
    void testGetCommitteeTypes() {
        List<CommitteeType> mockCommiteeTypeList = List.of(new CommitteeType(), new CommitteeType());
        when(committeeTypeRepository.findAll()).thenReturn(mockCommiteeTypeList);
        StandardResponse<List<CommitteeType>> response = committeeTypeServiceImpl.getCommitteeTypes();
        verify(committeeTypeRepository, times(1)).findAll();
        assertNotNull(response);
        assertEquals(new StandardResponse<>(ErrorEnums.SUCCESS_CODE.getStatus(), ErrorEnums.SUCCESS_CODE.getLabel(),
                mockCommiteeTypeList), response);
    }

    @Test
    void testSaveCommitteeType() {
        when(modelMapper.map(committeeTypeDTO, CommitteeType.class)).thenReturn(committeeType);
        when(committeeTypeRepository.save(committeeType)).thenReturn(committeeType);
        when(modelMapper.map(committeeType, CommitteeTypeDTO.class)).thenReturn(committeeTypeDTO);

        committeeTypeServiceImpl.saveCommitteeType(committeeTypeDTO);
        List<CommitteeType> mockCommiteeTypeList = List.of(committeeType);
        when(committeeTypeRepository.findByCommitteeType("BCC")).thenReturn(mockCommiteeTypeList);

        ApiRequestException exception = assertThrows(ApiRequestException.class, () -> {
            committeeTypeServiceImpl.saveCommitteeType(committeeTypeDTO);
        });

        assertEquals("Committee type is already exists", exception.getMessage());
    }

    @Test
    void testUpdateCommitteeType() {
        List<CommitteeType> mockCommittee = List.of(committeeType);

        when(committeeTypeRepository.save(committeeType)).thenReturn(committeeType);
        when(committeeTypeRepository.findById(1)).thenReturn(Optional.ofNullable(committeeType));
        when(committeeTypeRepository.findByCommitteeType("BCC")).thenReturn(mockCommittee);
        ApiRequestException exception = assertThrows(ApiRequestException.class, () -> {
            committeeTypeServiceImpl.updateCommitteeType(100, committeeTypeDTO);
        });

        assertEquals("Committee Type with ID 100 does not exist", exception.getMessage());
        when(modelMapper.map(committeeTypeDTO, CommitteeType.class)).thenReturn(committeeType);
        when(committeeTypeRepository.save(committeeType)).thenReturn(committeeType);
        when(modelMapper.map(committeeType, CommitteeTypeDTO.class)).thenReturn(committeeTypeDTO);
        StandardResponse<CommitteeTypeDTO> response = committeeTypeServiceImpl.updateCommitteeType(1,committeeTypeDTO);

        assertNotNull(response);
        assertEquals(new StandardResponse<>(ErrorEnums.SUCCESS_CODE.getStatus(), ErrorEnums.SUCCESS_CODE.getLabel(),
                committeeTypeDTO), response);
    }

}
