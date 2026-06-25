package lk.sampath.casadminPortalms.service;

import com.querydsl.core.BooleanBuilder;
import lk.sampath.casadminportalms.controller.basecontroller.StandardResponse;
import lk.sampath.casadminportalms.dto.common.ApproveRejectRQ;
import lk.sampath.casadminportalms.dto.role.RoleDTO;
import lk.sampath.casadminportalms.entity.role.*;
import lk.sampath.casadminportalms.enums.ErrorEnums;
import lk.sampath.casadminportalms.enums.MasterDataApproveStatus;
import lk.sampath.casadminportalms.enums.Status;
import lk.sampath.casadminportalms.exception.ApiRequestException;
import lk.sampath.casadminportalms.repository.role.*;
import lk.sampath.casadminportalms.service.impl.RoleServiceImpl;
import org.apache.commons.lang3.ObjectUtils;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RoleServiceImplTest {

    @Mock
    private PrivilegeCategoryRepository privilegeCategoryRepository;

    @Mock
    private PrivilegeRepository privilegeRepository;

    @InjectMocks
    private RoleServiceImpl roleService;

    @Mock
    private RoleTempRepository roleTempRepository;

    @Mock
    private RoleRepository roleRepository;

    @Mock
    private RoleAudRepository roleAudRepository;

    @Mock
    private RolePrivilegeAudRepository rolePrivilegeAudRepository;


    /**
     * Test when the findAllPrivilegeCategories()
     */
    @Test
    void testFindAllPrivilegeCategories_NoCategories() {
        when(privilegeCategoryRepository.findAll()).thenReturn(Collections.emptyList());

        ResponseEntity<StandardResponse<List<PrivilegeCategory>>> response = roleService.findAllPrivilegeCategories();

        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());
        assertTrue(ObjectUtils.isEmpty(response.getBody().getResponse()));
    }

    @Test
    void testFindAllPrivilegeCategories_CategoriesWithoutPrivileges() {
        PrivilegeCategory category = new PrivilegeCategory();
        category.setPrivilegeCategoryID(1);
        category.setCategory("Category 1");

        when(privilegeCategoryRepository.findAll()).thenReturn(Collections.singletonList(category));
        when(privilegeRepository.findByPrivilegeCategoryPrivilegeCategoryID(1)).thenReturn(Collections.emptyList());

        ResponseEntity<StandardResponse<List<PrivilegeCategory>>> response = roleService.findAllPrivilegeCategories();

        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());
        assertTrue(ObjectUtils.isEmpty(response.getBody().getResponse().getClass().getFields()));
    }

    @Test
    void testFindAllPrivilegeCategories_LargeDataSet() {

        List<PrivilegeCategory> categories = new ArrayList<>();

        for (int i = 0; i < 1000; i++) {
            PrivilegeCategory category = new PrivilegeCategory();
            category.setPrivilegeCategoryID(i);
            category.setCategory("Category " + i);
            categories.add(category);
        }

        List<Privilege> privileges = new ArrayList<>();
        for (int i = 0; i < 1000; i++) {
            Privilege privilege = new Privilege();
            privilege.setPrivilegeID(i);
            privilege.setPrivilegeName("Privilege " + i);
            privilege.setCode("CODE" + i);
            privilege.setDescription("Description " + i);
            privileges.add(privilege);
        }

        when(privilegeCategoryRepository.findAll()).thenReturn(categories);
        when(privilegeRepository.findByPrivilegeCategoryPrivilegeCategoryID(anyInt())).thenReturn(privileges);

        ResponseEntity<StandardResponse<List<PrivilegeCategory>>> response = roleService.findAllPrivilegeCategories();

        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());

        StandardResponse<?> standardResponse = response.getBody();
        assertNotNull(standardResponse);
        assertNotNull(standardResponse.getResponse());

        Map<String, List<Privilege>> responseMap = (Map<String, List<Privilege>>) standardResponse.getResponse();
        assertEquals(1000, responseMap.size());
    }


    /**
     * Test when the findAllUpcTemplateTempList()
     */

    @Test
    void testFindRolesTempByID_Success() {

        Integer roleID = 1;
        RoleTemp mockRoleTemp = new RoleTemp();
        mockRoleTemp.setRoleID(roleID);
        mockRoleTemp.setRoleName("Admin");
        mockRoleTemp.setUpmPrivilegeCode("UPM001");
        mockRoleTemp.setStatus(Status.ACT);
        mockRoleTemp.setApproveStatus(MasterDataApproveStatus.APPROVED);
        mockRoleTemp.setApprovedDate(new Date());
        mockRoleTemp.setApprovedBy("Admin");
        mockRoleTemp.setCreatedDate(new Date());
        mockRoleTemp.setCreatedBy("Admin");
        mockRoleTemp.setLastModifiedDate(new Date());
        mockRoleTemp.setModifiedBy("Admin");
        Privilege privilege = new Privilege();
        privilege.setPrivilegeID(101);
        mockRoleTemp.setPrivileges(Set.of(privilege));

        when(roleTempRepository.findById(roleID)).thenReturn(Optional.of(mockRoleTemp));

        ResponseEntity<StandardResponse<Object>> response = roleService.findRolesTempByID(roleID);

        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());
        assertNotNull(response.getBody());

        StandardResponse<Object> standardResponse = response.getBody();
        assertNotNull(standardResponse);
        assertTrue(standardResponse.getSuccess());
        assertNotNull(standardResponse.getResponse());

        RoleDTO roleTempDTO = (RoleDTO) standardResponse.getResponse();
        assertEquals(roleID, roleTempDTO.getRoleID());
        assertEquals("Admin", roleTempDTO.getRoleName());
        assertEquals("UPM001", roleTempDTO.getUpmPrivilegeCode());
        assertEquals(Status.ACT, roleTempDTO.getStatus());
        assertEquals(1, roleTempDTO.getPrivileges().size());
        assertEquals(101, roleTempDTO.getPrivileges().get(0));
    }

    @Test
    void testFindRolesTempByID_NotFound() {

        Integer roleID = 1;

        when(roleTempRepository.findById(roleID)).thenReturn(Optional.empty());

        ApiRequestException exception = assertThrows(ApiRequestException.class, () -> {
            roleService.findRolesTempByID(roleID);
        });

        assertEquals("Role Temp with" + roleID + "Does not exists", exception.getMessage());
        verify(roleTempRepository, times(1)).findById(roleID);
    }

    /**
     * Test when the findAllRolesTempList()
     */

    @Test
    void testFindAllRolesTempList_Success() {
        List<RoleTemp> roleTempList = new ArrayList<>();

        for (int i = 0; i < 3; i++) {
            RoleTemp roleTemp = new RoleTemp();
            roleTemp.setRoleID(i);
            roleTemp.setRoleName("Role " + i);
            roleTemp.setUpmPrivilegeCode("UPM" + i);
            roleTemp.setStatus(Status.ACT);
            roleTemp.setApproveStatus(MasterDataApproveStatus.APPROVED);
            roleTemp.setCreatedDate(new Date());
            roleTemp.setCreatedBy("Admin");

            Privilege privilege = new Privilege();
            privilege.setPrivilegeID(100 + i);
            roleTemp.setPrivileges(Set.of(privilege));

            roleTempList.add(roleTemp);
        }

        when(roleTempRepository.findAll()).thenReturn(roleTempList);

        ResponseEntity<StandardResponse<List<RoleDTO>>> response = roleService.findAllRolesTempList();

        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());
        assertNotNull(response.getBody());

        StandardResponse<List<RoleDTO>> standardResponse = response.getBody();
        assertTrue(standardResponse.getSuccess());
        assertNotNull(standardResponse.getResponse());

        List<RoleDTO> roleDTOList = (List<RoleDTO>) standardResponse.getResponse();
        assertEquals(3, roleDTOList.size());

        for (int i = 0; i < 3; i++) {
            RoleDTO roleDTO = roleDTOList.get(i);
            assertEquals(i, roleDTO.getRoleID());
            assertEquals("Role " + i, roleDTO.getRoleName());
            assertEquals("UPM" + i, roleDTO.getUpmPrivilegeCode());
            assertEquals(Status.ACT, roleDTO.getStatus());
            assertEquals(1, roleDTO.getPrivileges().size());
            assertEquals(100 + i, roleDTO.getPrivileges().get(0));
        }
    }

    @Test
    void testFindAllRolesTempList_EmptyList() {

        when(roleTempRepository.findAll()).thenReturn(Collections.emptyList());
        ResponseEntity<StandardResponse<List<RoleDTO>>> response = roleService.findAllRolesTempList();

        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());
        assertNotNull(response.getBody());

        StandardResponse<List<RoleDTO>> standardResponse = response.getBody();
        assertTrue(standardResponse.getSuccess());
        assertNotNull(standardResponse.getResponse());

        List<RoleDTO> roleDTOList = (List<RoleDTO>) standardResponse.getResponse();
        assertTrue(roleDTOList.isEmpty());
    }

    /**
     * Test when the findAllApprovedRoles()
     */

    @Test
    void testFindAllApprovedRoles_Success() {

        List<Role> roleList = new ArrayList<>();

        for (int i = 0; i < 3; i++) {
            Role role = new Role();
            role.setRoleID(i);
            role.setRoleName("Role " + i);
            role.setUpmPrivilegeCode("UPM" + i);
            role.setStatus(Status.ACT);
            role.setApproveStatus(MasterDataApproveStatus.APPROVED);
            role.setApprovedDate(new Date());
            role.setApprovedBy("Admin");
            role.setCreatedDate(new Date());
            role.setCreatedBy("Admin");
            role.setLastModifiedDate(new Date());
            role.setModifiedBy("Admin");

            Privilege privilege = new Privilege();
            privilege.setPrivilegeID(100 + i);
            role.setPrivileges(Set.of(privilege));

            roleList.add(role);
        }

        when(roleRepository.findAll()).thenReturn(roleList);

        ResponseEntity<StandardResponse<List<RoleDTO>>> response = roleService.findAllApprovedRoles();

        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());
        assertNotNull(response.getBody());

        StandardResponse<List<RoleDTO>> standardResponse = response.getBody();
        assertTrue(standardResponse.getSuccess());
        assertNotNull(standardResponse.getResponse());

        List<RoleDTO> roleDTOList = (List<RoleDTO>) standardResponse.getResponse();
        assertEquals(3, roleDTOList.size());

        for (int i = 0; i < 3; i++) {
            RoleDTO roleDTO = roleDTOList.get(i);
            assertEquals(i, roleDTO.getRoleID());
            assertEquals("Role " + i, roleDTO.getRoleName());
            assertEquals("UPM" + i, roleDTO.getUpmPrivilegeCode());
            assertEquals(Status.ACT, roleDTO.getStatus());
            assertEquals(MasterDataApproveStatus.APPROVED, roleDTO.getApproveStatus());
            assertEquals(1, roleDTO.getPrivileges().size());
            assertEquals(100 + i, roleDTO.getPrivileges().get(0));
        }
    }

    @Test
    void testFindAllApprovedRoles_EmptyList() {

        when(roleRepository.findAll()).thenReturn(Collections.emptyList());

        ResponseEntity<StandardResponse<List<RoleDTO>>> response = roleService.findAllApprovedRoles();

        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());
        assertNotNull(response.getBody());

        StandardResponse<List<RoleDTO>> standardResponse = response.getBody();
        assertTrue(standardResponse.getSuccess());
        assertNotNull(standardResponse.getResponse());

        List<RoleDTO> roleDTOList = (List<RoleDTO>) standardResponse.getResponse();
        assertTrue(roleDTOList.isEmpty());
    }

    /**
     * Test when the findApprovedRoleById()
     */

    @Test
    void testFindApprovedRoleById_Success() {

        int roleID = 1;
        Role role = new Role();
        role.setRoleID(roleID);
        role.setRoleName("Role " + roleID);
        role.setUpmPrivilegeCode("UPM" + roleID);
        role.setStatus(Status.ACT);
        role.setApproveStatus(MasterDataApproveStatus.APPROVED);
        role.setApprovedDate(new Date());
        role.setApprovedBy("Admin");
        role.setCreatedDate(new Date());
        role.setCreatedBy("Admin");
        role.setLastModifiedDate(new Date());
        role.setModifiedBy("Admin");

        Privilege privilege = new Privilege();
        privilege.setPrivilegeID(100 + roleID);
        role.setPrivileges(Set.of(privilege));

        when(roleRepository.findById(roleID)).thenReturn(Optional.of(role));

        ResponseEntity<StandardResponse<Object>> response = roleService.findApprovedRoleById(roleID);

        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());
        assertNotNull(response.getBody());

        StandardResponse<Object> standardResponse = response.getBody();
        assertTrue(standardResponse.getSuccess());
        assertNotNull(standardResponse.getResponse());

        RoleDTO roleDTO = (RoleDTO) standardResponse.getResponse();
        assertEquals(roleID, roleDTO.getRoleID());
        assertEquals("Role " + roleID, roleDTO.getRoleName());
        assertEquals("UPM" + roleID, roleDTO.getUpmPrivilegeCode());
        assertEquals(Status.ACT, roleDTO.getStatus());
        assertEquals(MasterDataApproveStatus.APPROVED, roleDTO.getApproveStatus());
        assertEquals(1, roleDTO.getPrivileges().size());
        assertEquals(100 + roleID, roleDTO.getPrivileges().get(0));
    }

    @Test
    void testFindApprovedRoleById_RoleNotFound() {

        int roleID = 1;
        when(roleRepository.findById(roleID)).thenReturn(Optional.empty());

        ApiRequestException thrown = assertThrows(ApiRequestException.class, () -> {
            roleService.findApprovedRoleById(roleID);
        });

        assertEquals(" Role with1Does not exists", thrown.getMessage());
    }

    /**
     * Test when the saveRoleTemp()
     */
    @Test
    void testSaveRoleTemp_Success() {
        RoleDTO roleDTO = new RoleDTO();
        roleDTO.setRoleID(1);
        roleDTO.setStatus(Status.ACT);
        roleDTO.setCreatedDate(new Date());
        roleDTO.setRoleName("admin");
        roleDTO.setCreatedBy("user1");
        roleDTO.setApproveStatus(MasterDataApproveStatus.PENDING);
        roleDTO.setApprovedDate(new Date());
        roleDTO.setApprovedBy("user2");
        roleDTO.setUpmPrivilegeCode("10");
        roleDTO.setPrivileges(List.of(1, 2, 3));

        when(roleTempRepository.findAll(any(BooleanBuilder.class))).thenReturn(Collections.emptyList());
        when(roleRepository.findAll(any(BooleanBuilder.class))).thenReturn(Collections.emptyList());
        when(privilegeRepository.findByPrivilegeIDIn(anyList())).thenReturn(new HashSet<>());
        when(roleTempRepository.getCurrentSequenceValue()).thenReturn(1);

        ResponseEntity<StandardResponse<Object>> response = roleService.saveRoleTemp(roleDTO);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
    }

    @Test
    void testSaveRoleTemp_RoleNameExistsInTempTable() {
        RoleDTO roleDTO = new RoleDTO();
        roleDTO.setRoleID(1);
        roleDTO.setStatus(Status.ACT);
        roleDTO.setCreatedDate(new Date());
        roleDTO.setRoleName("admin");
        roleDTO.setCreatedBy("user1");
        roleDTO.setApproveStatus(MasterDataApproveStatus.PENDING);
        roleDTO.setApprovedDate(new Date());
        roleDTO.setApprovedBy("user2");
        roleDTO.setUpmPrivilegeCode("10");
        roleDTO.setPrivileges(List.of(1, 2, 3));

        when(roleTempRepository.findAll(any(BooleanBuilder.class))).thenReturn(List.of(new RoleTemp()));
        when(roleRepository.findAll(any(BooleanBuilder.class))).thenReturn(Collections.emptyList());

        assertThrows(ApiRequestException.class, () -> roleService.saveRoleTemp(roleDTO));
    }

    @Test
    void testSaveRoleTemp_RoleNameExistsInMasterTable() {
        RoleDTO roleDTO = new RoleDTO();
        roleDTO.setRoleID(1);
        roleDTO.setStatus(Status.ACT);
        roleDTO.setCreatedDate(new Date());
        roleDTO.setRoleName("admin");
        roleDTO.setCreatedBy("user1");
        roleDTO.setApproveStatus(MasterDataApproveStatus.PENDING);
        roleDTO.setApprovedDate(new Date());
        roleDTO.setApprovedBy("user2");
        roleDTO.setUpmPrivilegeCode("10");
        roleDTO.setPrivileges(List.of(1, 2, 3));

        when(roleTempRepository.findAll(any(BooleanBuilder.class))).thenReturn(Collections.emptyList());
        when(roleRepository.findAll(any(BooleanBuilder.class))).thenReturn(List.of(new Role()));

        assertThrows(ApiRequestException.class, () -> roleService.saveRoleTemp(roleDTO));
    }

    @Test
     void testSaveRoleTemp_WithPrivileges() {
        // Mock input data
        RoleDTO roleDTO = new RoleDTO();
        roleDTO.setRoleName("Test Role");
        roleDTO.setCreatedBy("Admin");
        roleDTO.setPrivileges(List.of(1, 2));
        roleDTO.setAddedPrivileges(List.of(3));
        roleDTO.setDeletedPrivileges(List.of(2));
        roleDTO.setApproveStatus(MasterDataApproveStatus.PENDING);

        Privilege privilege1 = new Privilege();
        privilege1.setPrivilegeID(1);
        Privilege privilege2 = new Privilege();
        privilege2.setPrivilegeID(2);
        Privilege privilege3 = new Privilege();
        privilege3.setPrivilegeID(3);

        Set<Privilege> privileges = new HashSet<>(Set.of(privilege1, privilege2));
        Set<Privilege> addedPrivileges = new HashSet<>(Set.of(privilege3));
        Set<Privilege> deletedPrivileges = new HashSet<>(Set.of(privilege2));

        when(roleTempRepository.findAll(any(BooleanBuilder.class))).thenReturn(Collections.emptyList());
        when(roleRepository.findAll(any(BooleanBuilder.class))).thenReturn(Collections.emptyList());
        when(roleTempRepository.getCurrentSequenceValue()).thenReturn(101);
        when(privilegeRepository.findByPrivilegeIDIn(List.of(1, 2))).thenReturn(privileges);
        when(privilegeRepository.findByPrivilegeIDIn(List.of(3))).thenReturn(addedPrivileges);
        when(privilegeRepository.findByPrivilegeIDIn(List.of(2))).thenReturn(deletedPrivileges);

        ResponseEntity<StandardResponse<Object>> response = roleService.saveRoleTemp(roleDTO);

        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(ErrorEnums.SUCCESS_CODE.getStatus(), response.getBody().getSuccess());
        RoleDTO savedRole = (RoleDTO) response.getBody().getResponse();
        assertEquals("Test Role", savedRole.getRoleName());

        verify(roleTempRepository).save(any(RoleTemp.class));
    }

    @Test
     void testSaveRoleTemp_NullRoleDTO_ThrowsNullPointerException() {

        RoleDTO roleDTO = null;

        NullPointerException exception = assertThrows(NullPointerException.class, () -> {
            roleService.saveRoleTemp(roleDTO);
        });
        assertEquals("Cannot invoke \"lk.sampath.casadminportalms.dto.role.RoleDTO.getRoleName()\" because \"roleDTO\" is null", exception.getMessage());
    }

    @Test
     void testSaveRoleTemp_NullRoleName_ThrowsIllegalArgumentException() {

        RoleDTO roleDTO = new RoleDTO();
        roleDTO.setRoleName(null);

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            roleService.saveRoleTemp(roleDTO);
        });

        assertEquals("eq(null) is not allowed. Use isNull() instead", exception.getMessage());
    }

    @Test
     void testSaveRoleTemp_EmptyRoleName_NoExceptionThrown() {

        RoleDTO roleDTO = new RoleDTO();
        roleDTO.setRoleName("Upc Template cannot be empty or null");

        assertDoesNotThrow(() -> {
            roleService.saveRoleTemp(roleDTO);
        });
    }



    /**
     * Test when the updateRoleTemp()
     */

    @Test
    void testUpdateRoleTemp_Success() {
        Integer roleID = 1;
        RoleDTO roleDTO = new RoleDTO();
        roleDTO.setRoleID(roleID);
        roleDTO.setStatus(Status.ACT);
        roleDTO.setCreatedDate(new Date());
        roleDTO.setRoleName("admin");
        roleDTO.setCreatedBy("user1");
        roleDTO.setApproveStatus(MasterDataApproveStatus.PENDING);
        roleDTO.setApprovedDate(new Date());
        roleDTO.setApprovedBy("user2");
        roleDTO.setUpmPrivilegeCode("10");
        roleDTO.setPrivileges(List.of(1, 2, 3));

        RoleTemp roleDb = new RoleTemp();
        roleDb.setRoleID(roleID);
        roleDb.setRoleName("admin");
        roleDb.setPrivileges(new HashSet<>());

        when(roleTempRepository.findById(roleID)).thenReturn(Optional.of(roleDb));
        when(privilegeRepository.findByPrivilegeIDIn(anyList())).thenReturn(new HashSet<>());

        ResponseEntity<StandardResponse<Object>> response = roleService.updateRoleTemp(roleID, roleDTO);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("admin", ((RoleDTO) response.getBody().getResponse()).getRoleName());
    }

    @Test
    void testUpdateRoleTemp_RoleNotFound() {
        Integer roleID = 1;
        RoleDTO roleDTO = new RoleDTO();
        roleDTO.setRoleID(roleID);
        roleDTO.setStatus(Status.ACT);
        roleDTO.setCreatedDate(new Date());
        roleDTO.setRoleName("admin");
        roleDTO.setCreatedBy("user1");
        roleDTO.setApproveStatus(MasterDataApproveStatus.PENDING);
        roleDTO.setApprovedDate(new Date());
        roleDTO.setApprovedBy("user2");
        roleDTO.setUpmPrivilegeCode("10");

        when(roleTempRepository.findById(roleID)).thenReturn(Optional.empty());

        ApiRequestException exception = assertThrows(ApiRequestException.class, () -> roleService.updateRoleTemp(roleID, roleDTO));
        assertEquals("Role with ID 1 does not exist", exception.getMessage());
    }

    @Test
    void testUpdateRoleTemp_WithPrivileges() {
        Integer roleID = 1;
        RoleDTO roleDTO = new RoleDTO();
        roleDTO.setRoleID(roleID);
        roleDTO.setStatus(Status.ACT);
        roleDTO.setCreatedDate(new Date());
        roleDTO.setRoleName("admin");
        roleDTO.setCreatedBy("user1");
        roleDTO.setApproveStatus(MasterDataApproveStatus.PENDING);
        roleDTO.setApprovedDate(new Date());
        roleDTO.setApprovedBy("user2");
        roleDTO.setUpmPrivilegeCode("10");
        roleDTO.setPrivileges(List.of(1, 2));
        roleDTO.setAddedPrivileges(List.of(3));
        roleDTO.setDeletedPrivileges(List.of(2));

        RoleTemp roleDb = new RoleTemp();
        roleDb.setRoleID(roleID);
        roleDb.setRoleName("admin");

        Privilege privilege1 = new Privilege();
        privilege1.setPrivilegeID(1);
        Privilege privilege2 = new Privilege();
        privilege2.setPrivilegeID(2);
        Privilege privilege3 = new Privilege();
        privilege3.setPrivilegeID(3);

        // Use mutable sets for privileges
        Set<Privilege> privileges = new HashSet<>(Set.of(privilege1, privilege2));
        Set<Privilege> addedPrivileges = new HashSet<>(Set.of(privilege3));
        Set<Privilege> deletedPrivileges = new HashSet<>(Set.of(privilege2));

        when(roleTempRepository.findById(roleID)).thenReturn(Optional.of(roleDb));
        when(privilegeRepository.findByPrivilegeIDIn(List.of(1, 2))).thenReturn(privileges);
        when(privilegeRepository.findByPrivilegeIDIn(List.of(3))).thenReturn(addedPrivileges);
        when(privilegeRepository.findByPrivilegeIDIn(List.of(2))).thenReturn(deletedPrivileges);


        ResponseEntity<StandardResponse<Object>> response = roleService.updateRoleTemp(roleID, roleDTO);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("admin", ((RoleDTO) response.getBody().getResponse()).getRoleName());
    }

    /**
     * Test when the saveRoleToMaster()
     */

    @Test
     void testSaveRoleToMaster_Success() {

        RoleTemp temp = new RoleTemp();
        temp.setRoleID(101);
        temp.setRoleName("Admin Role");
        temp.setStatus(Status.ACT);
        temp.setUpmPrivilegeCode("UPM123");
        temp.setApproveStatus(MasterDataApproveStatus.APPROVED);
        temp.setApprovedBy("Admin");
        temp.setApprovedDate(new Date());
        temp.setCreatedBy("Creator");
        temp.setCreatedDate(new Date());
        temp.setLastModifiedDate(new Date());
        temp.setModifiedBy("Modifier");

        Privilege privilege1 = new Privilege();
        privilege1.setPrivilegeID(1);
        privilege1.setPrivilegeName("Privilege 1");

        Privilege privilege2 = new Privilege();
        privilege2.setPrivilegeID(2);
        privilege2.setPrivilegeName("Privilege 2");

        temp.setPrivileges(Set.of(privilege1, privilege2));

        Role savedRole = new Role();
        savedRole.setRoleID(temp.getRoleID());
        savedRole.setRoleName(temp.getRoleName());
        savedRole.setPrivileges(temp.getPrivileges());
        savedRole.setStatus(temp.getStatus());
        savedRole.setUpmPrivilegeCode(temp.getUpmPrivilegeCode());
        savedRole.setApproveStatus(temp.getApproveStatus());
        savedRole.setApprovedBy(temp.getApprovedBy());
        savedRole.setApprovedDate(temp.getApprovedDate());
        savedRole.setCreatedBy(temp.getCreatedBy());
        savedRole.setCreatedDate(temp.getCreatedDate());
        savedRole.setLastModifiedDate(temp.getLastModifiedDate());
        savedRole.setModifiedBy(temp.getModifiedBy());

        when(roleRepository.save(any(Role.class))).thenReturn(savedRole);

        RoleDTO result = roleService.saveRoleToMaster(temp);

        assertNotNull(result);
        assertEquals("Admin Role", result.getRoleName());
        assertEquals(Status.ACT, result.getStatus());
        assertEquals(2, result.getPrivileges().size());
        assertEquals("UPM123", result.getUpmPrivilegeCode());
        assertEquals(MasterDataApproveStatus.APPROVED, result.getApproveStatus());

        verify(roleRepository).save(any(Role.class));
    }

    /**
     * Test when the updateRoleToMaster()
     */

    @Test
     void testUpdateRoleToMaster_WithExistingRole() {

        RoleTemp roleTemp = new RoleTemp();
        roleTemp.setRoleID(101);
        roleTemp.setRoleName("Admin Role");
        roleTemp.setStatus(Status.ACT);
        roleTemp.setUpmPrivilegeCode("UPM123");
        roleTemp.setApproveStatus(MasterDataApproveStatus.APPROVED);
        roleTemp.setApprovedBy("Admin");
        roleTemp.setApprovedDate(new Date());
        roleTemp.setCreatedBy("Creator");
        roleTemp.setCreatedDate(new Date());
        roleTemp.setLastModifiedDate(new Date());
        roleTemp.setModifiedBy("Modifier");

        Privilege privilege1 = new Privilege();
        privilege1.setPrivilegeID(1);
        Privilege privilege2 = new Privilege();
        privilege2.setPrivilegeID(2);

        roleTemp.setPrivileges(Set.of(privilege1, privilege2));

        Role existingRole = new Role();
        existingRole.setRoleID(101);
        existingRole.setRoleName("Old Role Name");

        when(roleRepository.save(any(Role.class))).thenReturn(existingRole);

        RoleDTO result = roleService.updateRoleToMaster(roleTemp, existingRole);

        assertNotNull(result);
        assertEquals("Admin Role", result.getRoleName());
        assertEquals(Status.ACT, result.getStatus());
        assertEquals(2, result.getPrivileges().size());
        assertEquals("UPM123", result.getUpmPrivilegeCode());
        assertEquals(MasterDataApproveStatus.APPROVED, result.getApproveStatus());

        verify(roleRepository).save(existingRole);
    }

    @Test
     void testUpdateRoleToMaster_WithNewRole() {

        RoleTemp roleTemp = new RoleTemp();
        roleTemp.setRoleID(102);
        roleTemp.setRoleName("User Role");
        roleTemp.setStatus(Status.ACT);
        roleTemp.setUpmPrivilegeCode("UPM456");
        roleTemp.setApproveStatus(MasterDataApproveStatus.PENDING);
        roleTemp.setApprovedBy("Reviewer");
        roleTemp.setApprovedDate(new Date());
        roleTemp.setCreatedBy("Admin");
        roleTemp.setCreatedDate(new Date());
        roleTemp.setLastModifiedDate(new Date());
        roleTemp.setModifiedBy("Admin");

        Privilege privilege1 = new Privilege();
        privilege1.setPrivilegeID(3);

        roleTemp.setPrivileges(Set.of(privilege1));

        Role savedRole = new Role();
        savedRole.setRoleID(102);
        savedRole.setRoleName("User Role");

        when(roleRepository.save(any(Role.class))).thenReturn(savedRole);

        RoleDTO result = roleService.updateRoleToMaster(roleTemp, null);

        assertNotNull(result);
        assertEquals("User Role", result.getRoleName());
        assertEquals(Status.ACT, result.getStatus());
        assertEquals(1, result.getPrivileges().size());
        assertEquals("UPM456", result.getUpmPrivilegeCode());
        assertEquals(MasterDataApproveStatus.PENDING, result.getApproveStatus());

        verify(roleRepository).save(any(Role.class));
    }

    /**
     * Test when the saveRoleAudit()
     */

    @Test
    void testSaveRoleAudit_Success() {
        // Mock RoleTemp
        RoleTemp roleTemp = new RoleTemp();
        roleTemp.setRoleID(1);
        roleTemp.setStatus(Status.ACT);
        roleTemp.setRoleName("Admin");
        roleTemp.setUpmPrivilegeCode("PRIV_001");
        roleTemp.setCreatedBy("User1");
        roleTemp.setApproveStatus(MasterDataApproveStatus.APPROVED);
        roleTemp.setApprovedBy("Approver1");
        roleTemp.setModifiedBy("Modifier1");

        // Mock Privileges
        Privilege privilege1 = new Privilege();
        privilege1.setPrivilegeID(101);

        Privilege privilege2 = new Privilege();
        privilege2.setPrivilegeID(102);

        roleTemp.setPrivileges(Set.of(privilege1, privilege2));

        // ✅ Mock privilegeRepository behavior to return the privilege objects
        when(privilegeRepository.findById(101)).thenReturn(Optional.of(privilege1));
        when(privilegeRepository.findById(102)).thenReturn(Optional.of(privilege2));

        // Execute the method
        roleService.saveRoleAudit(roleTemp);

        // Capture saved RoleAud
        ArgumentCaptor<RoleAud> roleAudCaptor = ArgumentCaptor.forClass(RoleAud.class);
        verify(roleAudRepository, times(1)).save(roleAudCaptor.capture());

        RoleAud savedRoleAud = roleAudCaptor.getValue();
        assertEquals(roleTemp.getRoleID(), savedRoleAud.getRoleID());
        assertEquals(roleTemp.getRoleName(), savedRoleAud.getRoleName());
        assertEquals(roleTemp.getUpmPrivilegeCode(), savedRoleAud.getUpmPrivilegeCode());

        // Verify RolePrivilegeAud is saved
        verify(rolePrivilegeAudRepository, times(1)).saveAll(anySet());
    }

    @Test
    void testSaveRoleAudit_NullRoleTemp() {
        // Expect exception when roleTemp is null
        assertThrows(NullPointerException.class, () -> roleService.saveRoleAudit(null));

        // Ensure no data is saved
        verify(roleAudRepository, times(0)).save(any(RoleAud.class));
        verify(rolePrivilegeAudRepository, times(0)).saveAll(anySet());
    }

    @Test
    void testSaveRoleAudit_EmptyPrivileges() {
        // Mock RoleTemp with no privileges
        RoleTemp roleTemp = new RoleTemp();
        roleTemp.setRoleID(1);
        roleTemp.setRoleName("Admin");
        roleTemp.setPrivileges(Collections.emptySet());

        // Execute the method
        roleService.saveRoleAudit(roleTemp);

        // Ensure RoleAud is still saved
        verify(roleAudRepository, times(1)).save(any(RoleAud.class));

        // Ensure RolePrivilegeAud is NOT saved
        verify(rolePrivilegeAudRepository, times(1)).saveAll(Collections.emptySet());
    }

    /**
     * Test when the handleApproval()
     */
    @Test
     void testHandleApproval_WithExistingTemplate() {
        // Mock input data
        RoleTemp roleTemp = new RoleTemp();
        roleTemp.setRoleID(1);
        roleTemp.setRoleName("Test Role");
        roleTemp.setPrivileges(Set.of());
        roleTemp.setApproveStatus(MasterDataApproveStatus.APPROVED);

        Role existingRole = new Role();
        existingRole.setRoleID(1);
        existingRole.setRoleName("Existing Role");

        ResponseEntity<StandardResponse<Object>> response = roleService.handleApproval(roleTemp, existingRole);

        assertNotNull(response);
        assertEquals(ErrorEnums.SUCCESS_CODE.getStatus(), response.getBody().getSuccess());

        verify(roleRepository).save(existingRole);

        verify(roleTempRepository).delete(roleTemp);
    }


    @Test
    void handleApproval_existingRoleDoesNotMatch() {
        RoleTemp roleTemp = new RoleTemp();
        roleTemp.setRoleID(1);
        roleTemp.setRoleName("Test Role");
        roleTemp.setPrivileges(Set.of());
        roleTemp.setApproveStatus(MasterDataApproveStatus.APPROVED);

        Role anotherRole = new Role();
        anotherRole.setRoleID(2);
        anotherRole.setRoleName("Another Role");

        ResponseEntity<StandardResponse<Object>> response = roleService.handleApproval(roleTemp, anotherRole);

        assertNotNull(response);
        assertEquals(ErrorEnums.SUCCESS_CODE.getStatus(), response.getBody().getSuccess());

        verify(roleRepository, times(1)).save(any(Role.class));

        verify(roleTempRepository).delete(roleTemp);
    }

    @Test
    void handleApproval_existingRoleIsNull() {

        RoleTemp roleTemp = new RoleTemp();
        roleTemp.setRoleID(1);
        roleTemp.setRoleName("Test Role");
        roleTemp.setPrivileges(Set.of());
        roleTemp.setApproveStatus(MasterDataApproveStatus.APPROVED);

        ResponseEntity<StandardResponse<Object>> response = roleService.handleApproval(roleTemp, null);

        assertNotNull(response);
        assertEquals(ErrorEnums.SUCCESS_CODE.getStatus(), response.getBody().getSuccess());

        verify(roleRepository, times(1)).save(any(Role.class));

        verify(roleTempRepository).delete(roleTemp);
    }

    /**
     * Test when the handleRejection()
     */

    @Test
     void testHandleRejection_WithExistingTemplate() {

        RoleTemp roleTemp = new RoleTemp();
        roleTemp.setRoleID(1);
        roleTemp.setRoleName("Test Role");
        roleTemp.setPrivileges(Set.of());
        roleTemp.setApproveStatus(MasterDataApproveStatus.APPROVED);

        ResponseEntity<StandardResponse<Object>> response = roleService.handleRejection(roleTemp);

        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());
        assertNotNull(response.getBody());
        assertEquals(ErrorEnums.SUCCESS_CODE.getStatus(), response.getBody().getSuccess());
        assertTrue(response.getBody().getResponse() instanceof RoleDTO);

        RoleDTO roleDTO = (RoleDTO) response.getBody().getResponse();
        assertEquals(roleTemp.getRoleID(), roleDTO.getRoleID());
        assertEquals(roleTemp.getRoleName(), roleDTO.getRoleName());
    }

    @Test
    void testHandleRejection_SaveAuditCalled() {

        RoleTemp roleTemp = new RoleTemp();
        roleTemp.setRoleID(1);
        roleTemp.setRoleName("Test Role");
        roleTemp.setPrivileges(Set.of());
        roleTemp.setApproveStatus(MasterDataApproveStatus.APPROVED);

        RoleServiceImpl spyRoleService = spy(roleService);

        spyRoleService.handleRejection(roleTemp);

        verify(spyRoleService).saveRoleAudit(roleTemp);
    }

    @Test
    void testHandleRejection_ResponseIncludesCorrectData() {

        RoleTemp roleTemp = new RoleTemp();
        roleTemp.setRoleID(1);
        roleTemp.setRoleName("Test Role");
        roleTemp.setPrivileges(Set.of());
        roleTemp.setApproveStatus(MasterDataApproveStatus.APPROVED);

        ResponseEntity<StandardResponse<Object>> response = roleService.handleRejection(roleTemp);

        RoleDTO roleDTO = (RoleDTO) response.getBody().getResponse();
        assertNotNull(roleDTO);
        assertEquals(roleTemp.getRoleID(), roleDTO.getRoleID());
        assertEquals(roleTemp.getRoleName(), roleDTO.getRoleName());
        assertEquals(roleTemp.getApproveStatus(), roleDTO.getApproveStatus());
    }

    @Test
    void testHandleRejection_AuditSaving() {

        RoleTemp roleTemp = new RoleTemp();
        roleTemp.setRoleID(1);
        roleTemp.setRoleName("Test Role");
        roleTemp.setPrivileges(Set.of());
        roleTemp.setApproveStatus(MasterDataApproveStatus.APPROVED);

        roleService.handleRejection(roleTemp);

        verify(roleAudRepository).save(any(RoleAud.class));
    }

    /**
     * Test when the approveRejectRole()
     */

    @Test
    void testApproveRejectRole_WithValidApproval() {

        ApproveRejectRQ approveRejectRQ = new ApproveRejectRQ();
        approveRejectRQ.setApproveRejectDataID(1);
        approveRejectRQ.setApproveStatus(MasterDataApproveStatus.APPROVED);

        RoleTemp roleTemp = new RoleTemp();
        roleTemp.setRoleID(1);
        roleTemp.setRoleName("Test Role");
        roleTemp.setApproveStatus(MasterDataApproveStatus.PENDING);
        roleTemp.setPrivileges(Set.of());

        Role existingRole = new Role();
        existingRole.setRoleID(1);
        existingRole.setRoleName("Existing Role");

        when(roleTempRepository.findById(approveRejectRQ.getApproveRejectDataID())).thenReturn(Optional.of(roleTemp));
        when(roleRepository.findById(roleTemp.getRoleID())).thenReturn(Optional.of(existingRole));
        when(roleTempRepository.saveAndFlush(any(RoleTemp.class))).thenReturn(roleTemp);

        ResponseEntity<StandardResponse<Object>> response = roleService.approveRejectRole(approveRejectRQ);

        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());
        assertEquals(ErrorEnums.SUCCESS_CODE.getStatus(), response.getBody().getSuccess());

        verify(roleTempRepository).findById(approveRejectRQ.getApproveRejectDataID());
        verify(roleRepository).findById(roleTemp.getRoleID());
        verify(roleTempRepository).saveAndFlush(roleTemp);
    }

    @Test
    void testApproveRejectRole_WithValidRejection() {

        ApproveRejectRQ approveRejectRQ = new ApproveRejectRQ();
        approveRejectRQ.setApproveRejectDataID(1);
        approveRejectRQ.setApproveStatus(MasterDataApproveStatus.REJECTED);

        RoleTemp roleTemp = new RoleTemp();
        roleTemp.setRoleID(1);
        roleTemp.setRoleName("Test Role");
        roleTemp.setApproveStatus(MasterDataApproveStatus.PENDING);
        roleTemp.setPrivileges(Set.of());

        Role existingRole = new Role();
        existingRole.setRoleID(1);
        existingRole.setRoleName("Existing Role");

        when(roleTempRepository.findById(approveRejectRQ.getApproveRejectDataID())).thenReturn(Optional.of(roleTemp));
        when(roleTempRepository.saveAndFlush(any(RoleTemp.class))).thenReturn(roleTemp);

        ResponseEntity<StandardResponse<Object>> response = roleService.approveRejectRole(approveRejectRQ);

        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());
        assertEquals(ErrorEnums.SUCCESS_CODE.getStatus(), response.getBody().getSuccess());

        verify(roleTempRepository).findById(approveRejectRQ.getApproveRejectDataID());
        verify(roleTempRepository).saveAndFlush(roleTemp);
    }

    @Test
    void testApproveRejectRole_WithNullRequest() {

        ApiRequestException exception = assertThrows(ApiRequestException.class, () ->
                roleService.approveRejectRole(null)
        );

        assertEquals("Invalid ApproveRejectRQ: DataID cannot be null", exception.getMessage());
    }

    @Test
    void testApproveRejectRole_WithNullDataID() {
        ApproveRejectRQ approveRejectRQ = new ApproveRejectRQ();
        approveRejectRQ.setApproveRejectDataID(null);
        approveRejectRQ.setApproveStatus(MasterDataApproveStatus.REJECTED);

        ApiRequestException exception = assertThrows(ApiRequestException.class, () ->
                roleService.approveRejectRole(approveRejectRQ)
        );

        assertEquals("Invalid ApproveRejectRQ: DataID cannot be null", exception.getMessage());
    }

    @Test
    void testApproveRejectRole_WithNonExistentRoleTemp() {

        ApproveRejectRQ approveRejectRQ = new ApproveRejectRQ();
        approveRejectRQ.setApproveRejectDataID(1);
        approveRejectRQ.setApproveStatus(MasterDataApproveStatus.APPROVED);

        when(roleTempRepository.findById(approveRejectRQ.getApproveRejectDataID())).thenReturn(Optional.empty());

        ApiRequestException exception = assertThrows(ApiRequestException.class, () ->
                roleService.approveRejectRole(approveRejectRQ)
        );

        assertEquals("Role with ID 1Does not exists", exception.getMessage());
    }

    @Test
    void testApproveRejectRole_WithRoleTempWithoutRole() {

        ApproveRejectRQ approveRejectRQ = new ApproveRejectRQ();
        approveRejectRQ.setApproveRejectDataID(1);
        approveRejectRQ.setApproveStatus(MasterDataApproveStatus.APPROVED);

        RoleTemp roleTemp = new RoleTemp();
        roleTemp.setRoleID(1);
        roleTemp.setRoleName("Test Role");
        roleTemp.setApproveStatus(MasterDataApproveStatus.PENDING);
        roleTemp.setPrivileges(Set.of());

        when(roleTempRepository.findById(approveRejectRQ.getApproveRejectDataID())).thenReturn(Optional.of(roleTemp));
        when(roleRepository.findById(roleTemp.getRoleID())).thenReturn(Optional.empty());
        when(roleTempRepository.saveAndFlush(any(RoleTemp.class))).thenReturn(roleTemp);

        ResponseEntity<StandardResponse<Object>> response = roleService.approveRejectRole(approveRejectRQ);

        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());
        assertEquals(ErrorEnums.SUCCESS_CODE.getStatus(), response.getBody().getSuccess());
    }

    @Test
    void testApproveRejectRole_WithUnknownApproveStatus() {
        ApproveRejectRQ approveRejectRQ = new ApproveRejectRQ();
        approveRejectRQ.setApproveRejectDataID(1);
        approveRejectRQ.setApproveStatus(MasterDataApproveStatus.DRAFT);

        RoleTemp roleTemp = new RoleTemp();
        roleTemp.setRoleID(1);
        roleTemp.setRoleName("Test Role");
        roleTemp.setApproveStatus(MasterDataApproveStatus.PENDING);
        roleTemp.setPrivileges(Set.of());

        when(roleTempRepository.findById(approveRejectRQ.getApproveRejectDataID())).thenReturn(Optional.of(roleTemp));
        when(roleTempRepository.saveAndFlush(any(RoleTemp.class))).thenReturn(roleTemp);

        ApiRequestException exception = assertThrows(ApiRequestException.class, () ->
                roleService.approveRejectRole(approveRejectRQ)
        );

        assertEquals("Unknown approval status: DRAFT", exception.getMessage());
    }

    /**
     * Test when the validateRoleNameUniqueness()
     */

    @Test
    void testValidateRoleNameUniqueness_WhenRoleNameDoesNotExist() {

        when(roleTempRepository.exists(any(BooleanBuilder.class))).thenReturn(false);
        when(roleRepository.exists(any(BooleanBuilder.class))).thenReturn(false);

        assertDoesNotThrow(() -> roleService.validateRoleNameUniqueness("UniqueRoleName",1));

        verify(roleTempRepository).exists(any(BooleanBuilder.class));
        verify(roleRepository).exists(any(BooleanBuilder.class));
    }



    @Test
    void testValidateRoleNameUniqueness_WhenRoleNameExistsInMasterRecords() {

        when(roleTempRepository.exists(any(BooleanBuilder.class))).thenReturn(false);
        when(roleRepository.exists(any(BooleanBuilder.class))).thenReturn(true);

        ApiRequestException exception = assertThrows(ApiRequestException.class, () ->
                roleService.validateRoleNameUniqueness("DuplicateRoleName",1)
        );

        assertEquals("Role name 'DuplicateRoleName' already exists in the system.", exception.getMessage());

        verify(roleTempRepository).exists(any(BooleanBuilder.class));
        verify(roleRepository).exists(any(BooleanBuilder.class));
    }

    /**
     * Test when the updateApprovedRole()
     */

    @Test
    void testUpdateApprovedRole_WhenRoleDoesNotExist() {

        Integer roleId = 1;
        RoleDTO roleDTO = new RoleDTO();
        when(roleRepository.findById(roleId)).thenReturn(Optional.empty());

        ApiRequestException exception = assertThrows(ApiRequestException.class, () ->
                roleService.updateApprovedRole(roleId, roleDTO)
        );

        assertEquals("Role with ID 1Does not exists", exception.getMessage());
        verify(roleRepository).findById(roleId);
        verifyNoInteractions(roleTempRepository, privilegeRepository);
    }

    @Test
    void testUpdateApprovedRole_WhenRoleNameIsUnique() {

        Integer roleId = 1;
        RoleDTO roleDTO = new RoleDTO();
        roleDTO.setStatus(Status.ACT);
        roleDTO.setCreatedDate(new Date());
        roleDTO.setRoleName("admin");
        roleDTO.setCreatedBy("user1");
        roleDTO.setApproveStatus(MasterDataApproveStatus.PENDING);
        roleDTO.setApprovedDate(new Date());
        roleDTO.setApprovedBy("user2");
        roleDTO.setUpmPrivilegeCode("10");
        roleDTO.setPrivileges(List.of(1, 2));
        roleDTO.setAddedPrivileges(List.of(3));
        roleDTO.setDeletedPrivileges(List.of(2));

        Role existingRole = new Role();
        existingRole.setRoleID(roleId);
        existingRole.setRoleName("Existing Role Name");

        Privilege privilege1 = new Privilege();
        privilege1.setPrivilegeID(1);
        Privilege privilege2 = new Privilege();
        privilege2.setPrivilegeID(2);
        Privilege privilege3 = new Privilege();
        privilege3.setPrivilegeID(3);

        Set<Privilege> privileges = new HashSet<>(Set.of(privilege1, privilege2));
        Set<Privilege> addedPrivileges = new HashSet<>(Set.of(privilege3));
        Set<Privilege> deletedPrivileges = new HashSet<>(Set.of(privilege2));

        when(roleRepository.findById(roleId)).thenReturn(Optional.of(existingRole));
        when(privilegeRepository.findByPrivilegeIDIn(List.of(1, 2))).thenReturn(privileges);
        when(privilegeRepository.findByPrivilegeIDIn(List.of(3))).thenReturn(addedPrivileges);
        when(privilegeRepository.findByPrivilegeIDIn(List.of(2))).thenReturn(deletedPrivileges);

        ResponseEntity<StandardResponse<Object>> response = roleService.updateApprovedRole(roleId, roleDTO);

        assertNotNull(response);
        assertEquals(ErrorEnums.SUCCESS_CODE.getStatus(), response.getBody().getSuccess());
        verify(roleTempRepository).save(any(RoleTemp.class));
    }

    @Test
    void testUpdateApprovedRole_WhenAddedAndDeletedPrivilegesExist() {

        Integer roleId = 1;
        RoleDTO roleDTO = new RoleDTO();
        roleDTO.setRoleName("Valid Role Name");
        roleDTO.setPrivileges(List.of(1, 2));
        roleDTO.setAddedPrivileges(List.of(3));
        roleDTO.setDeletedPrivileges(List.of(1));

        Role existingRole = new Role();
        existingRole.setRoleID(roleId);
        existingRole.setRoleName("Valid Role Name");

        Privilege privilege1 = new Privilege();
        privilege1.setPrivilegeID(1);
        Privilege privilege2 = new Privilege();
        privilege2.setPrivilegeID(2);
        Privilege privilege3 = new Privilege();
        privilege3.setPrivilegeID(3);

        Set<Privilege> privileges = new HashSet<>(Set.of(privilege1, privilege2));
        Set<Privilege> addedPrivileges = new HashSet<>(Set.of(privilege3));
        Set<Privilege> deletedPrivileges = new HashSet<>(Set.of(privilege1));

        when(roleRepository.findById(roleId)).thenReturn(Optional.of(existingRole));
        when(privilegeRepository.findByPrivilegeIDIn(List.of(1, 2))).thenReturn(privileges);
        when(privilegeRepository.findByPrivilegeIDIn(List.of(3))).thenReturn(addedPrivileges);
        when(privilegeRepository.findByPrivilegeIDIn(List.of(1))).thenReturn(deletedPrivileges);

        ResponseEntity<StandardResponse<Object>> response = roleService.updateApprovedRole(roleId, roleDTO);

        assertNotNull(response);
        assertEquals(ErrorEnums.SUCCESS_CODE.getStatus(), response.getBody().getSuccess());
        verify(roleTempRepository).save(any(RoleTemp.class));
    }

    /**
     * Test when the findAllPrivileges()
     */

    @Test
    void testFindAllPrivileges_WhenPrivilegesExist() {

        List<Privilege> privileges = List.of(
                new Privilege(1),
                new Privilege(2)
        );
        when(privilegeRepository.findAll()).thenReturn(privileges);

        ResponseEntity<StandardResponse<List<Privilege>>> response = roleService.findAllPrivileges();

        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());
        assertNotNull(response.getBody());
        assertEquals(ErrorEnums.SUCCESS_CODE.getStatus(), response.getBody().getSuccess());
        assertEquals(privileges, response.getBody().getResponse());

        verify(privilegeRepository).findAll();
    }

    @Test
    void testFindAllPrivileges_WhenNoPrivilegesExist() {

        when(privilegeRepository.findAll()).thenReturn(List.of());

        ResponseEntity<StandardResponse<List<Privilege>>> response = roleService.findAllPrivileges();

        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());
        assertNotNull(response.getBody());
        assertEquals(ErrorEnums.SUCCESS_CODE.getStatus(), response.getBody().getSuccess());

        verify(privilegeRepository).findAll();
    }

    @Test
    void testFindAllPrivileges_WhenRepositoryThrowsException() {

        when(privilegeRepository.findAll()).thenThrow(new RuntimeException("Database connection error"));

        RuntimeException exception = assertThrows(RuntimeException.class, () ->
                roleService.findAllPrivileges()
        );

        assertEquals("Database connection error", exception.getMessage());
        verify(privilegeRepository).findAll();
    }

    /**
     * Test when the deleteRoleTempById()
     */

    @Test
    void testDeleteRoleTempById_Success() {

        int roleID = 1;

        ResponseEntity<StandardResponse<Void>> response = roleService.deleteRoleTempById(roleID);

        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());
        assertNotNull(response.getBody());
        assertEquals(ErrorEnums.SUCCESS_CODE.getStatus(), response.getBody().getSuccess());
        assertEquals(roleID, response.getBody().getResponse());

        verify(roleTempRepository).deleteById(roleID);
    }

    @Test
    void testDeleteRoleTempById_WhenRoleDoesNotExist() {

        int roleID = 1;
        doThrow(new IllegalArgumentException("RoleTemp with ID " + roleID + " does not exist"))
                .when(roleTempRepository).deleteById(roleID);

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () ->
                roleService.deleteRoleTempById(roleID)
        );

        assertEquals("RoleTemp with ID " + roleID + " does not exist", exception.getMessage());

        verify(roleTempRepository).deleteById(roleID);
    }

    @Test
    void testDeleteRoleTempById_WhenRepositoryThrowsException() {

        int roleID = 1;
        doThrow(new RuntimeException("Database error"))
                .when(roleTempRepository).deleteById(roleID);

        RuntimeException exception = assertThrows(RuntimeException.class, () ->
                roleService.deleteRoleTempById(roleID)
        );

        assertEquals("Database error", exception.getMessage());

        verify(roleTempRepository).deleteById(roleID);
    }
}