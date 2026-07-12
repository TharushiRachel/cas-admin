package lk.sampath.casadminportalms.service.impl;

import lk.sampath.casadminportalms.controller.basecontroller.StandardResponse;
import lk.sampath.casadminportalms.dto.committeePool.CommitteePoolDTO;
import lk.sampath.casadminportalms.entity.committeepool.CommitteePool;
import lk.sampath.casadminportalms.entity.committeepool.CommitteePoolHistory;
import lk.sampath.casadminportalms.entity.committeepool.CommitteePoolTemp;
import lk.sampath.casadminportalms.enums.ErrorEnums;
import lk.sampath.casadminportalms.enums.MasterDataApproveStatus;
import lk.sampath.casadminportalms.enums.Status;
import lk.sampath.casadminportalms.exception.ApiRequestException;
import lk.sampath.casadminportalms.repository.CommitteePoolHistoryRepository;
import lk.sampath.casadminportalms.repository.CommitteePoolRepository;
import lk.sampath.casadminportalms.repository.CommitteePoolTempRepository;
import lk.sampath.casadminportalms.service.CommitteePoolService;
import lombok.extern.log4j.Log4j2;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.data.domain.Page;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
@Log4j2
public class CommitteePoolServiceImpl implements CommitteePoolService {
    
    private final CommitteePoolRepository committeePoolRepository;
    private final CommitteePoolTempRepository committeePoolTempRepository;
    private final CommitteePoolHistoryRepository committeePoolHistoryRepository;
    private final ModelMapper modelMapper;

    public CommitteePoolServiceImpl(CommitteePoolRepository committeePoolRepository, CommitteePoolTempRepository committeePoolTempRepository,
                                    CommitteePoolHistoryRepository committeePoolHistoryRepository,ModelMapper modelMapper) {
        this.committeePoolRepository = committeePoolRepository;
        this.committeePoolTempRepository = committeePoolTempRepository;
        this.committeePoolHistoryRepository = committeePoolHistoryRepository;
        this.modelMapper = modelMapper;
    }

    @Override
    public ResponseEntity<StandardResponse<List<CommitteePoolDTO>>> getTempCommitteePool() throws ApiRequestException {
        StandardResponse<List<CommitteePoolDTO>> response;
        try {
            List<CommitteePoolTemp> committeePoolTemps = committeePoolTempRepository.findAll();
            response = new StandardResponse<>(ErrorEnums.SUCCESS_CODE.getStatus(), ErrorEnums.SUCCESS_CODE.getLabel(), committeePoolTemps);
        } catch(Exception e){
            log.error("ERROR : Get All Temp Pool Users: ",e);
            throw new ApiRequestException("An error occurred.");
        }

        return ResponseEntity.ok().body(response);
    }

    @Override
    public ResponseEntity<StandardResponse<List<CommitteePoolDTO>>> getCommitteePool() throws ApiRequestException {
        StandardResponse<List<CommitteePoolDTO>> response;
        try {
            List<CommitteePool> committeePools = committeePoolRepository.findAll();
            response = new StandardResponse<>(ErrorEnums.SUCCESS_CODE.getStatus(), ErrorEnums.SUCCESS_CODE.getLabel(), committeePools);
        } catch (Exception e){
            log.error("ERROR : Get All Pool Users: ",e);
            throw new ApiRequestException("An error occurred.");
        }
        return ResponseEntity.ok().body(response);
    }

    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = ApiRequestException.class)
    public ResponseEntity<StandardResponse<List<CommitteePoolDTO>>> saveCommitteePoolUsers(List<CommitteePoolDTO> committeePoolUsers)throws ApiRequestException {
        StandardResponse<List<CommitteePoolDTO>> response;
        try {
            Date date = new Date();
            List<CommitteePoolDTO> prevUsers = new ArrayList<>(committeePoolRepository.findAll().stream().map(pool -> modelMapper.map(pool, CommitteePoolDTO.class)).toList());

            for (CommitteePoolDTO committeePoolUser : committeePoolUsers) {
                CommitteePool committeePool = new CommitteePool();

                committeePool.setUserId(committeePoolUser.getUserId());
                committeePool.setUserName(committeePoolUser.getUserName());
//                committeePool.setDesignation(committeePoolUser.getDesignation());
//                committeePool.setWorkClass(committeePoolUser.getWorkClass());
                committeePool.setUserDisplayName(committeePoolUser.getUserDisplayName());
                committeePool.setCreatedDate(date);
//                committeePool.setStatus(Status.ACT);
                committeePool.setApproveStatus(MasterDataApproveStatus.APPROVED);
                committeePool.setApprovedDate(date);

                committeePool = committeePoolRepository.save(committeePool);

                prevUsers.add(new CommitteePoolDTO(committeePool));

                //save to history
                saveCommitteePoolHistory(new CommitteePoolDTO(committeePool));
            }
            response = new StandardResponse<>(ErrorEnums.SUCCESS_CODE.getStatus(), ErrorEnums.SUCCESS_CODE.getLabel(), prevUsers);
        } catch (Exception e){
            log.error("ERROR : Save Pool Users: ",e);
            throw new ApiRequestException("Pool user(s) adding has been failed.");
        }
        return ResponseEntity.ok().body(response);
    }

    private <T> List<T> paginate(List<T> data, Pageable pageable) {
        int fromIndex = Math.toIntExact(Math.min(pageable.getOffset(), data.size()));
        int toIndex = Math.min(fromIndex + pageable.getPageSize(), data.size());
        return data.subList(fromIndex, toIndex);
    }

    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = ApiRequestException.class)
    public ResponseEntity<StandardResponse<List<CommitteePoolDTO>>> saveTempCommitteePoolUser(CommitteePoolDTO committeePoolUser)throws ApiRequestException {

        StandardResponse<List<CommitteePoolDTO>> response;
        try {
            Date date = new Date();
            CommitteePoolTemp committeePool = new CommitteePoolTemp();

//            committeePool.setCommitteePoolId(committeePoolUser.getCommitteePoolId());
            committeePool.setUserId(committeePoolUser.getUserId());
            committeePool.setUserName(committeePoolUser.getUserName());
//            committeePool.setDesignation(committeePoolUser.getDesignation());
//            committeePool.setWorkClass(committeePoolUser.getWorkClass());
            committeePool.setUserDisplayName(committeePoolUser.getUserDisplayName());
            committeePool.setCreatedDate(committeePoolUser.getCreatedDate());
            committeePool.setCreatedBy(committeePoolUser.getCreatedBy());
//            committeePool.setStatus(committeePoolUser.getStatus());
            committeePool.setLastModifiedDate(date);
            committeePool.setApproveStatus(MasterDataApproveStatus.PENDING);

            committeePool = committeePoolTempRepository.save(committeePool);

            List<CommitteePoolDTO> payload = new ArrayList<>();
            payload.add(new CommitteePoolDTO(committeePool));
            response = new StandardResponse<>(ErrorEnums.SUCCESS_CODE.getStatus(), ErrorEnums.SUCCESS_CODE.getLabel(), payload);
        } catch(Exception e){
            log.error("ERROR : Save Temp Pool Users: ",e);
            throw new ApiRequestException("Pool user adding has been failed.");
        }
        return ResponseEntity.ok().body(response);
    }

    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = ApiRequestException.class)
    public ResponseEntity<StandardResponse<CommitteePoolDTO>> approveRejectPoolUser(CommitteePoolDTO committeePoolUser) throws ApiRequestException {

        StandardResponse<CommitteePoolDTO> response;
        try {
            Date date = new Date();
            CommitteePool committeePool = null;
            if (committeePoolRepository.findById(committeePoolUser.getCommitteePoolId()).isPresent()) {
                committeePool = committeePoolRepository.getReferenceById(committeePoolUser.getCommitteePoolId());

                if (committeePoolUser.getApproveStatus() == MasterDataApproveStatus.APPROVED) {
//                    committeePool.setStatus(committeePoolUser.getStatus());
                    committeePool.setApproveStatus(committeePoolUser.getApproveStatus());
                    committeePool.setLastModifiedDate(date);
                    committeePool.setApprovedDate(date);

                    committeePool = committeePoolRepository.save(committeePool);

                    //save to history
                    this.saveCommitteePoolHistory(new CommitteePoolDTO(committeePool));
                } else {
                    CommitteePoolTemp committeePoolTemp = committeePoolTempRepository.getReferenceById(committeePoolUser.getCommitteePoolId());
                    committeePoolTemp.setApproveStatus(committeePoolUser.getApproveStatus());
                    committeePoolTemp.setApprovedDate(date);
                    committeePoolTemp.setLastModifiedDate(date);

                    //save to history
                    this.saveCommitteePoolHistory(new CommitteePoolDTO(committeePoolTemp));
                }

                //remove temp record
                committeePoolTempRepository.deleteById(committeePoolUser.getCommitteePoolId());
            } else {
                throw new ApiRequestException("Committee pool user not found.");
            }
            response = new StandardResponse<>(ErrorEnums.SUCCESS_CODE.getStatus(), ErrorEnums.SUCCESS_CODE.getLabel(), committeePoolUser);
        } catch(Exception e){
            log.error("ERROR : Approve or Reject Pool Users: ",e);
            throw new ApiRequestException("Pool user approve/reject has been failed.");
        }
        return ResponseEntity.ok().body(response);
    }

    public void saveCommitteePoolHistory(CommitteePoolDTO committeePoolUser)throws ApiRequestException {

        CommitteePoolHistory committeePool = new CommitteePoolHistory();

        committeePool.setCommitteePoolId(committeePoolUser.getCommitteePoolId());
        committeePool.setUserId(committeePoolUser.getUserId());
        committeePool.setUserName(committeePoolUser.getUserName());
        committeePool.setDesignation(committeePoolUser.getDesignation());
        committeePool.setWorkClass(committeePoolUser.getWorkClass());
        committeePool.setUserDisplayName(committeePoolUser.getUserDisplayName());
        committeePool.setCreatedDate(committeePoolUser.getCreatedDate());
        committeePool.setCreatedBy(committeePoolUser.getCreatedBy());
        committeePool.setStatus(committeePoolUser.getStatus());
        committeePool.setLastModifiedDate(committeePoolUser.getLastModifiedDate());
        committeePool.setModifiedBy(committeePoolUser.getModifiedBy());
        committeePool.setApproveStatus(committeePoolUser.getApproveStatus());
        committeePool.setApprovedBy(committeePoolUser.getApprovedBy());
        committeePool.setApprovedDate(committeePoolUser.getApprovedDate());

        committeePoolHistoryRepository.save(committeePool);
    }

}
