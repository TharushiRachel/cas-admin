package lk.sampath.casadminportalms.service.impl;

import com.querydsl.core.BooleanBuilder;

import lk.sampath.casadminportalms.controller.basecontroller.StandardResponse;
import lk.sampath.casadminportalms.dto.common.ApproveRejectRQ;
import lk.sampath.casadminportalms.dto.role.RoleDTO;
import lk.sampath.casadminportalms.entity.role.*;
import lk.sampath.casadminportalms.enums.ErrorEnums;
import lk.sampath.casadminportalms.enums.MasterDataApproveStatus;
import lk.sampath.casadminportalms.enums.Status;
import lk.sampath.casadminportalms.exception.ApiRequestException;
import lk.sampath.casadminportalms.repository.jdbc.RoleRepositoryJdbc;
import lk.sampath.casadminportalms.repository.role.*;
import lk.sampath.casadminportalms.service.RoleService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Service
@Transactional
public class RoleServiceImpl implements RoleService {

    private static final Logger LOG = LoggerFactory.getLogger(RoleServiceImpl.class);

    private static final String ROLE_WITH_ID = "Role with ID ";

    private static final String DOES_NOT_EXISTS = "Does not exists";

    private static final String NOT_NULL = "Upc Template cannot be empty or null";
    @Autowired
    private PrivilegeRepository privilegeRepository;

    @Autowired
    private PrivilegeCategoryRepository privilegeCategoryRepository;

    @Autowired
    private RoleTempRepository roleTempRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private RoleRepositoryJdbc roleRepositoryJdbc;

    @Autowired
    private RoleAudRepository roleAudRepository;

    @Autowired
    private RolePrivilegeAudRepository rolePrivilegeAudRepository;

    @Override
    @Transactional(readOnly = true)
    public ResponseEntity<StandardResponse<List<PrivilegeCategory>>> findAllPrivilegeCategories(int page, int size) throws ApiRequestException {

        List<PrivilegeCategory> privilegeCategoryList = privilegeCategoryRepository.findAll(PageRequest.of(page, size)).getContent();

        Map<String, List<Privilege>> privilegeCategoryMap = new LinkedHashMap<>();

        for (PrivilegeCategory privilegeCategory : privilegeCategoryList) {
            List<Privilege> privilegeList = privilegeRepository.findByPrivilegeCategoryPrivilegeCategoryID(
                    privilegeCategory.getPrivilegeCategoryID()
            );

            List<Privilege> privileges = new ArrayList<>();
            for (Privilege privilege : privilegeList) {
                Privilege privilegeData = new Privilege();
                privilegeData.setPrivilegeID(privilege.getPrivilegeID());
                privilegeData.setPrivilegeName(privilege.getPrivilegeName());
                privilegeData.setCode(privilege.getCode());
                privilegeData.setDescription(privilege.getDescription());
                privilegeData.setStatus(privilege.getStatus());
                privileges.add(privilegeData);
            }

            privilegeCategoryMap.put(privilegeCategory.getCategory(), privileges);
        }

        StandardResponse<List<PrivilegeCategory>> response = new StandardResponse<>(ErrorEnums.SUCCESS_CODE.getStatus(),ErrorEnums.SUCCESS_CODE.getLabel(),privilegeCategoryMap);
        return ResponseEntity.ok().body(response);
    }


    @Override
    @Transactional(readOnly = true)
    public ResponseEntity<StandardResponse<Object>> findRolesTempByID(Integer roleID) throws ApiRequestException {
        RoleTemp roleTemp = roleTempRepository.findById(roleID).orElseThrow(() -> {
            throw new ApiRequestException("Role Temp with" + roleID + DOES_NOT_EXISTS);
        });

        RoleDTO roleTempDTO = new RoleDTO();
        roleTempDTO.setRoleID(roleTemp.getRoleID());
        roleTempDTO.setRoleName(roleTemp.getRoleName());
        roleTempDTO.setUpmPrivilegeCode(roleTemp.getUpmPrivilegeCode());
        roleTempDTO.setStatus(roleTemp.getStatus());
        roleTempDTO.setApproveStatus(roleTemp.getApproveStatus());
        roleTempDTO.setApprovedDate(roleTemp.getApprovedDate());
        roleTempDTO.setApprovedBy(roleTemp.getApprovedBy());
        roleTempDTO.setCreatedDate(roleTemp.getCreatedDate());
        roleTempDTO.setCreatedBy(roleTemp.getCreatedBy());
        roleTempDTO.setLastModifiedDate(roleTemp.getLastModifiedDate());
        roleTempDTO.setModifiedBy(roleTemp.getModifiedBy());
        roleTempDTO.setPrivileges(roleTemp.getPrivileges().stream().map(Privilege::getPrivilegeID).toList());

        StandardResponse<Object> response = new StandardResponse<>(ErrorEnums.SUCCESS_CODE.getStatus(), ErrorEnums.SUCCESS_CODE.getLabel(), roleTempDTO);
        return ResponseEntity.ok().body(response);
    }

    @Transactional(readOnly = true)
    @Override
    public ResponseEntity<StandardResponse<List<RoleDTO>>> findAllRolesTempList(int page, int size) throws ApiRequestException {
        List<RoleTemp> roleTempList = roleTempRepository.findAll(PageRequest.of(page, size)).getContent();
        List<RoleDTO> roleDTOList = new ArrayList<>();

        for(RoleTemp roleTemp : roleTempList){
            RoleDTO roleTempDTO = new RoleDTO();
            roleTempDTO.setRoleID(roleTemp.getRoleID());
            roleTempDTO.setRoleName(roleTemp.getRoleName());
            roleTempDTO.setUpmPrivilegeCode(roleTemp.getUpmPrivilegeCode());
            roleTempDTO.setStatus(roleTemp.getStatus());
            roleTempDTO.setApproveStatus(roleTemp.getApproveStatus());
            roleTempDTO.setCreatedDate(roleTemp.getCreatedDate());
            roleTempDTO.setCreatedBy(roleTemp.getCreatedBy());
            roleTempDTO.setPrivileges(roleTemp.getPrivileges().stream().map(Privilege::getPrivilegeID).toList());

            roleDTOList.add(roleTempDTO);
        }

        StandardResponse<List<RoleDTO>> response = new StandardResponse<>(ErrorEnums.SUCCESS_CODE.getStatus(), ErrorEnums.SUCCESS_CODE.getLabel(), roleDTOList);
        return ResponseEntity.ok().body(response);
    }

    @Transactional(readOnly = true)
    @Override
    public ResponseEntity<StandardResponse<List<RoleDTO>>> findAllApprovedRoles(int page, int size) throws ApiRequestException {
        List<Role> roleList = roleRepository.findAll(PageRequest.of(page, size)).getContent();
        List<RoleDTO> roleDTOList = new ArrayList<>();

        for(Role role : roleList){
            RoleDTO roleDTO = new RoleDTO();
            roleDTO.setRoleID(role.getRoleID());
            roleDTO.setRoleName(role.getRoleName());
            roleDTO.setUpmPrivilegeCode(role.getUpmPrivilegeCode());
            roleDTO.setStatus(role.getStatus());
            roleDTO.setApproveStatus(role.getApproveStatus());
            roleDTO.setApprovedDate(role.getApprovedDate());
            roleDTO.setApprovedBy(role.getApprovedBy());
            roleDTO.setCreatedDate(role.getCreatedDate());
            roleDTO.setCreatedBy(role.getCreatedBy());
            roleDTO.setLastModifiedDate(role.getLastModifiedDate());
            roleDTO.setModifiedBy(role.getModifiedBy());
            roleDTO.setPrivileges(role.getPrivileges().stream().map(Privilege::getPrivilegeID).toList());

            roleDTOList.add(roleDTO);
        }

        StandardResponse<List<RoleDTO>> response = new StandardResponse<>(ErrorEnums.SUCCESS_CODE.getStatus(), ErrorEnums.SUCCESS_CODE.getLabel(), roleDTOList);
        return ResponseEntity.ok().body(response);
    }
    @Transactional(readOnly = true)
    @Override
    public ResponseEntity<StandardResponse<Object>> findApprovedRoleById(int roleID) throws ApiRequestException {
        Role role = roleRepository.findById(roleID).orElseThrow(() -> {
            throw new ApiRequestException(" Role with" + roleID + DOES_NOT_EXISTS);
        });

        RoleDTO roleDTO = new RoleDTO();
        roleDTO.setRoleID(role.getRoleID());
        roleDTO.setRoleName(role.getRoleName());
        roleDTO.setUpmPrivilegeCode(role.getUpmPrivilegeCode());
        roleDTO.setStatus(role.getStatus());
        roleDTO.setApproveStatus(role.getApproveStatus());
        roleDTO.setApprovedDate(role.getApprovedDate());
        roleDTO.setApprovedBy(role.getApprovedBy());
        roleDTO.setCreatedDate(role.getCreatedDate());
        roleDTO.setCreatedBy(role.getCreatedBy());
        roleDTO.setLastModifiedDate(role.getLastModifiedDate());
        roleDTO.setModifiedBy(role.getModifiedBy());

        roleDTO.setPrivileges(role.getPrivileges().stream().map(Privilege::getPrivilegeID).toList());

        StandardResponse<Object> response = new StandardResponse<>(ErrorEnums.SUCCESS_CODE.getStatus(), ErrorEnums.SUCCESS_CODE.getLabel(), roleDTO);
        return ResponseEntity.ok().body(response);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW, rollbackFor = ApiRequestException.class)
    public ResponseEntity<StandardResponse<Object>> saveRoleTemp(RoleDTO roleDTO) throws ApiRequestException {

        LOG.info("START: Save Role Temp :{}", roleDTO);
        Date date = new Date();
        RoleTemp roleTempSave = new RoleTemp();

        BooleanBuilder booleanBuilder = new BooleanBuilder();
        booleanBuilder.and(QRoleTemp.roleTemp.roleName.eq(roleDTO.getRoleName()));
        List<RoleTemp> roleTemps = (List<RoleTemp>) roleTempRepository.findAll(booleanBuilder);

        BooleanBuilder booleanBuilderForMaster = new BooleanBuilder();
        booleanBuilderForMaster.and(QRole.role.roleName.eq(roleDTO.getRoleName()));
        List<Role> roleList = (List<Role>)roleRepository.findAll(booleanBuilderForMaster);

        if( roleDTO.getRoleName() == null || roleDTO.getRoleName().trim().isEmpty()) {
            throw new ApiRequestException(NOT_NULL);
        }

        validateRoleNameUniqueness(roleDTO.getRoleName(), null);

        if(roleTemps.isEmpty() && roleList.isEmpty()){

            roleTempSave.setRoleID(roleTempRepository.getCurrentSequenceValue());
            roleTempSave.setStatus(Status.ACT);
            roleTempSave.setCreatedDate(date);
            roleTempSave.setRoleName(roleDTO.getRoleName());
            roleTempSave.setCreatedBy(roleDTO.getCreatedBy());
            roleTempSave.setApproveStatus(roleDTO.getApproveStatus());
            roleTempSave.setApprovedDate(roleDTO.getApprovedDate());
            roleTempSave.setApprovedBy(roleDTO.getApprovedBy());
            roleTempSave.setModifiedBy(roleDTO.getModifiedBy());
            roleTempSave.setLastModifiedDate(date);
            roleTempSave.setUpmPrivilegeCode(roleDTO.getUpmPrivilegeCode());

            Set<Privilege> privileges = privilegeRepository.findByPrivilegeIDIn(roleDTO.getPrivileges());
            roleTempSave.setPrivileges(privileges);

            if (roleDTO.getAddedPrivileges() != null) {
                Set<Privilege> addedPrivileges = privilegeRepository.findByPrivilegeIDIn(roleDTO.getAddedPrivileges());
                roleTempSave.getPrivileges().addAll(addedPrivileges);
            }

            if (roleDTO.getDeletedPrivileges() != null) {
                Set<Privilege> deletedPrivileges = privilegeRepository.findByPrivilegeIDIn(roleDTO.getDeletedPrivileges());
                roleTempSave.getPrivileges().removeAll(deletedPrivileges);
            }
            LOG.info("roleTempSave {}", roleTempSave);
            roleTempRepository.save(roleTempSave);
        }
        else if(!roleTemps.isEmpty()){
            throw new ApiRequestException("Role Name Already Exists in Temp Table");
        }
        else if(!roleList.isEmpty()){
            throw new ApiRequestException("Roles Already Exists in Master Table");
        }
        else if( roleDTO.getRoleName() == null || roleDTO.getRoleName().trim().isEmpty()){
            throw new ApiRequestException("NOT_NUL");
        }

        RoleDTO role = new RoleDTO(roleTempSave);
        StandardResponse<Object> response = new StandardResponse<>(ErrorEnums.SUCCESS_CODE.getStatus(), ErrorEnums.SUCCESS_CODE.getLabel(), role);
        return ResponseEntity.ok().body(response);
    }


    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW, rollbackFor = ApiRequestException.class)
    public ResponseEntity<StandardResponse<Object>> updateRoleTemp(Integer roleID, RoleDTO roleDTO) throws ApiRequestException {

        RoleTemp roleDb = roleTempRepository.findById(roleID).orElseThrow(() ->
                new ApiRequestException(ROLE_WITH_ID + roleID + " does not exist")
        );

        if( roleDTO.getRoleName() == null || roleDTO.getRoleName().isEmpty()){
            throw new ApiRequestException("NOT_NUL");
        }

        validateRoleNameUniqueness(roleDTO.getRoleName(), roleID);

        roleDb.setRoleName(roleDTO.getRoleName());
        roleDb.setUpmPrivilegeCode(roleDTO.getUpmPrivilegeCode());
        roleDb.setStatus(roleDTO.getStatus());
        roleDb.setApproveStatus(roleDTO.getApproveStatus());

        Set<Privilege> privileges = privilegeRepository.findByPrivilegeIDIn(roleDTO.getPrivileges());
        roleDb.setPrivileges(privileges);

        if (roleDTO.getAddedPrivileges() != null) {
            Set<Privilege> addedPrivileges = privilegeRepository.findByPrivilegeIDIn(roleDTO.getAddedPrivileges());
            roleDb.getPrivileges().addAll(addedPrivileges);
        }

        if (roleDTO.getDeletedPrivileges() != null) {
            Set<Privilege> deletedPrivileges = privilegeRepository.findByPrivilegeIDIn(roleDTO.getDeletedPrivileges());
            roleDb.getPrivileges().removeAll(deletedPrivileges);
        }


        roleTempRepository.save(roleDb);

        RoleDTO role = new RoleDTO(roleDb);
        StandardResponse<Object> response = new StandardResponse<>(ErrorEnums.SUCCESS_CODE.getStatus(), ErrorEnums.SUCCESS_CODE.getLabel(), role);
        return ResponseEntity.ok().body(response);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW, rollbackFor = ApiRequestException.class)
    public ResponseEntity<StandardResponse<Object>> approveRejectRole(ApproveRejectRQ approveRejectRQ) throws ApiRequestException {
        LOG.info("START: Approve/Reject Role: {}", approveRejectRQ);
        Date date = new Date();
        if(approveRejectRQ == null || approveRejectRQ.getApproveRejectDataID() == null){
            throw new ApiRequestException("Invalid ApproveRejectRQ: DataID cannot be null");
        }

        RoleTemp roleTemp = roleTempRepository.findById(approveRejectRQ.getApproveRejectDataID()).orElseThrow(() -> {
            throw new ApiRequestException(ROLE_WITH_ID + approveRejectRQ.getApproveRejectDataID() + DOES_NOT_EXISTS);
        });

        Optional<Role> optionalRole = roleRepository.findById(roleTemp.getRoleID());
        Role findRole = optionalRole.orElse(null);

        roleTemp.setApprovedDate(date);
        roleTemp.setApproveStatus(approveRejectRQ.getApproveStatus());
        RoleTemp savedTemp = roleTempRepository.saveAndFlush(roleTemp);

        ResponseEntity<StandardResponse<Object>> response;

        if(approveRejectRQ.getApproveStatus().equals(MasterDataApproveStatus.APPROVED)){
            response = handleApproval(roleTemp, findRole);
        } else if (approveRejectRQ.getApproveStatus().equals(MasterDataApproveStatus.REJECTED)){
            response = handleRejection(savedTemp);
        } else {
            throw new ApiRequestException("Unknown approval status: "+ approveRejectRQ.getApproveStatus());
        }

       return response;
    }

    public ResponseEntity<StandardResponse<Object>> handleApproval(RoleTemp temp, Role existingTemplate){
        RoleDTO savedRole;
        if(existingTemplate != null && existingTemplate.getRoleID().equals(temp.getRoleID())){
            savedRole = updateRoleToMaster(temp, existingTemplate);
        } else {
            savedRole = saveRoleToMaster(temp);
        }

        saveRoleAudit(temp);
        roleTempRepository.delete(temp);

        StandardResponse<Object> response = new StandardResponse<>(ErrorEnums.SUCCESS_CODE.getStatus(), ErrorEnums.SUCCESS_CODE.getLabel(), savedRole);
        return ResponseEntity.ok().body(response);
    }

    public ResponseEntity<StandardResponse<Object>> handleRejection(RoleTemp temp){
        LOG.info("Handling rejection for UPC Template Temp ID: {}", temp.getRoleID());

        saveRoleAudit(temp);

        RoleDTO roleDTO = new RoleDTO(temp);
        StandardResponse<Object> response = new StandardResponse<>(ErrorEnums.SUCCESS_CODE.getStatus(), ErrorEnums.SUCCESS_CODE.getLabel(), roleDTO);
        return ResponseEntity.ok().body(response);
    }

    public RoleDTO saveRoleToMaster(RoleTemp temp) {

        Role role = new Role();
        role.setRoleID(temp.getRoleID());
        role.setRoleName(temp.getRoleName());
        role.setPrivileges(temp.getPrivileges());
        role.setStatus(temp.getStatus());
        role.setUpmPrivilegeCode(temp.getUpmPrivilegeCode());
        role.setApproveStatus(temp.getApproveStatus());
        role.setApprovedBy(temp.getApprovedBy());
        role.setApprovedDate(temp.getApprovedDate());
        role.setCreatedBy(temp.getCreatedBy());
        role.setCreatedDate(temp.getCreatedDate());
        role.setLastModifiedDate(temp.getLastModifiedDate());
        role.setModifiedBy(temp.getModifiedBy());

        roleRepository.save(role);

        return new RoleDTO(role);
    }

    public RoleDTO updateRoleToMaster(RoleTemp roleTemp, Role existingRole){

        Role role = (existingRole != null) ?  existingRole : new Role();

        role.setPrivileges(roleTemp.getPrivileges() != null ? roleTemp.getPrivileges() : new HashSet<>());

        role.setRoleID(roleTemp.getRoleID());
        role.setRoleName(roleTemp.getRoleName());
        role.setPrivileges(roleTemp.getPrivileges());
        role.setStatus(roleTemp.getStatus());
        role.setUpmPrivilegeCode(roleTemp.getUpmPrivilegeCode());
        role.setApproveStatus(roleTemp.getApproveStatus());
        role.setApprovedBy(roleTemp.getApprovedBy());
        role.setApprovedDate(roleTemp.getApprovedDate());
        role.setCreatedBy(roleTemp.getCreatedBy());
        role.setCreatedDate(roleTemp.getCreatedDate());
        role.setLastModifiedDate(roleTemp.getLastModifiedDate());
        role.setModifiedBy(roleTemp.getModifiedBy());

        roleRepository.save(role);

        return new RoleDTO(role);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW, rollbackFor = ApiRequestException.class)
    public ResponseEntity<StandardResponse<Object>> updateApprovedRole(Integer roleID, RoleDTO roleDTO) throws ApiRequestException {
        LOG.info("START: Update Approved Role :{}", roleDTO);
        Date date = new Date();

        Role roleDb = roleRepository.findById(roleID).orElseThrow( () -> {
            throw new ApiRequestException(ROLE_WITH_ID + roleID + DOES_NOT_EXISTS);
        });

        LOG.info("roleDb :{}", roleDb);

        if(!roleDb.getRoleName().equals(roleDTO.getRoleName())){
            validateRoleNameUniqueness(roleDTO.getRoleName(), roleID);
        } else if( roleDTO.getRoleName() == null || roleDTO.getRoleName().trim().isEmpty()){
            throw new ApiRequestException(NOT_NULL);
        }

        RoleTemp roleTemp = new RoleTemp();
        roleTemp.setRoleID(roleDb.getRoleID());
        roleTemp.setStatus(roleDTO.getStatus());
        roleTemp.setCreatedDate(date);
        roleTemp.setRoleName(roleDTO.getRoleName());
        roleTemp.setCreatedDate(roleDTO.getCreatedDate());
        roleTemp.setCreatedBy(roleDTO.getCreatedBy());
        roleTemp.setApprovedBy(roleDTO.getApprovedBy());
        roleTemp.setApprovedDate(roleDTO.getApprovedDate());
        roleTemp.setApproveStatus(roleDTO.getApproveStatus());
        roleTemp.setUpmPrivilegeCode(roleDTO.getUpmPrivilegeCode());

        Set<Privilege> privileges = privilegeRepository.findByPrivilegeIDIn(roleDTO.getPrivileges());
        roleTemp.setPrivileges(privileges);

        if (roleDTO.getAddedPrivileges() != null) {
            Set<Privilege> addedPrivileges = privilegeRepository.findByPrivilegeIDIn(roleDTO.getAddedPrivileges());
            roleTemp.getPrivileges().addAll(addedPrivileges);
        }

        if (roleDTO.getDeletedPrivileges() != null) {
            Set<Privilege> deletedPrivileges = privilegeRepository.findByPrivilegeIDIn(roleDTO.getDeletedPrivileges());
            roleTemp.getPrivileges().removeAll(deletedPrivileges);
        }

        LOG.info("END : GET Role {} ",roleTemp);
        roleTempRepository.save(roleTemp);
        RoleDTO role = new RoleDTO(roleTemp);
        StandardResponse<Object> response = new StandardResponse<>(ErrorEnums.SUCCESS_CODE.getStatus(), ErrorEnums.SUCCESS_CODE.getLabel(), role);
        return ResponseEntity.ok().body(response);
    }

    public void validateRoleNameUniqueness(String roleName, Integer roleID) throws ApiRequestException{
        BooleanBuilder tempBuilder = new BooleanBuilder();
        tempBuilder.and(QRoleTemp.roleTemp.roleName.eq(roleName));
        if(roleID != null) {
            tempBuilder.and(QRoleTemp.roleTemp.roleID.ne(roleID));
        }
        boolean existsInTemp = roleTempRepository.exists(tempBuilder);

        BooleanBuilder masterBuilder = new BooleanBuilder();
        masterBuilder.and(QRole.role.roleName.eq(roleName));
        if(roleID != null) {
            masterBuilder.and(QRole.role.roleID.ne(roleID));
        }
        boolean existsInMaster = roleRepository.exists(masterBuilder);

        if(existsInTemp || existsInMaster) {
            throw new ApiRequestException("Role name '" + roleName + "' already exists in the system.");
        }
    }

    @Override
    @Transactional(readOnly = true)
    public ResponseEntity<StandardResponse<List<Privilege>>> findAllPrivileges(int page, int size) throws ApiRequestException {
        List<Privilege> privileges = privilegeRepository.findAll(PageRequest.of(page, size)).getContent();

        StandardResponse<List<Privilege>> response = new StandardResponse<>(ErrorEnums.SUCCESS_CODE.getStatus(), ErrorEnums.SUCCESS_CODE.getLabel(), privileges);
        return ResponseEntity.ok().body(response);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW, rollbackFor = ApiRequestException.class)
    public ResponseEntity<StandardResponse<Void>> deleteRoleTempById(int roleID) throws ApiRequestException {
        roleTempRepository.deleteById(roleID);
        StandardResponse<Void> response = new StandardResponse<>(ErrorEnums.SUCCESS_CODE.getStatus(), ErrorEnums.SUCCESS_CODE.getLabel(), roleID);
        return ResponseEntity.ok().body(response);
    }

    public void saveRoleAudit(RoleTemp roleTemp) {
        RoleAud audit = new RoleAud();

        audit.setRoleID(roleTemp.getRoleID());
        audit.setStatus(roleTemp.getStatus());
        audit.setCreatedDate(roleTemp.getCreatedDate());
        audit.setRoleName(roleTemp.getRoleName());
        audit.setCreatedBy(roleTemp.getCreatedBy());
        audit.setApproveStatus(roleTemp.getApproveStatus());
        audit.setApprovedDate(roleTemp.getApprovedDate());
        audit.setApprovedBy(roleTemp.getApprovedBy());
        audit.setModifiedBy(roleTemp.getModifiedBy());
        audit.setLastModifiedDate(roleTemp.getLastModifiedDate());
        audit.setUpmPrivilegeCode(roleTemp.getUpmPrivilegeCode());

        Set<RolePrivilegeAud> rolePrivilegeAudSet = roleTemp.getPrivileges().stream()
                .map(privilege -> {
                    Privilege privilegeFromDb = privilegeRepository.findById(privilege.getPrivilegeID())
                            .orElseThrow(() -> new ApiRequestException("Privilege not found: " + privilege.getPrivilegeID()));

                    RolePrivilegeAud rolePrivilegeAud = new RolePrivilegeAud();
                    rolePrivilegeAud.setRole(audit);
                    rolePrivilegeAud.setPrivilege(privilegeFromDb);
                    rolePrivilegeAud.setActionDate(audit.getLastModifiedDate());

                    return rolePrivilegeAud;
                })
                .collect(Collectors.toSet());

        audit.setRolePrivilegeAudSet(rolePrivilegeAudSet);

        roleAudRepository.save(audit);
        rolePrivilegeAudRepository.saveAll(rolePrivilegeAudSet);

        LOG.info("Saved audit record for Role ID: {}", roleTemp.getRoleID());
    }

}
