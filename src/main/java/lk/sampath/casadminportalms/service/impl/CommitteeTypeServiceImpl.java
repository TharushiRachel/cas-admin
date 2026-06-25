package lk.sampath.casadminportalms.service.impl;

import lk.sampath.casadminportalms.controller.basecontroller.StandardResponse;
import lk.sampath.casadminportalms.dto.committetype.CommitteeTypeDTO;
import lk.sampath.casadminportalms.entity.committeetype.CommitteeType;
import lk.sampath.casadminportalms.enums.ErrorEnums;
import lk.sampath.casadminportalms.exception.ApiRequestException;
import lk.sampath.casadminportalms.repository.committeetype.CommitteeTypeRepository;
import lk.sampath.casadminportalms.service.CommitteeTypeService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import java.util.Date;
import java.util.List;

@Service
@Transactional
public class CommitteeTypeServiceImpl implements CommitteeTypeService {

    private final CommitteeTypeRepository committeeTypeRepository;

    private final ModelMapper modelMapper;

    @Autowired
    public CommitteeTypeServiceImpl(CommitteeTypeRepository committeeTypeRepository, ModelMapper modelMapper) {
        this.committeeTypeRepository = committeeTypeRepository;
        this.modelMapper = modelMapper;
    }

    /**
     * get Committee Types service
     *
     * @return List<CommitteeTypeDTO>
     * @throws ApiRequestException
     */
    @Override
    @Transactional(readOnly = true)
    public  StandardResponse<List<CommitteeType>> getCommitteeTypes(Pageable pageable) throws ApiRequestException {
        List<CommitteeType> committeeTypeList = this.committeeTypeRepository.findAll(pageable).getContent();
        return new StandardResponse<>(ErrorEnums.SUCCESS_CODE.getStatus(), ErrorEnums.SUCCESS_CODE.getLabel(), committeeTypeList);
    }

    /**
     * save Committee Types service
     *
     * @param request
     * @return CommitteeTypeCreateResponse
     * @throws ApiRequestException
     */
    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW, rollbackFor = ApiRequestException.class)
    public StandardResponse<CommitteeTypeDTO> saveCommitteeType(CommitteeTypeDTO request) throws ApiRequestException {
        Date newDate = new Date();
        CommitteeType newCommitteeType = modelMapper.map(request, CommitteeType.class);
        newCommitteeType.setCreatedDate(newDate);
        newCommitteeType.setModifiedDate(newDate);

        List<CommitteeType> duplicateCommitteeType = this.committeeTypeRepository.findByCommitteeType(request.getCommitteeType());

        if (!duplicateCommitteeType.isEmpty()) {
            throw new ApiRequestException("Committee type is already exists");
        }
        CommitteeType resCommitteeType = this.committeeTypeRepository.save(newCommitteeType);
        return new StandardResponse<>
                (ErrorEnums.SUCCESS_CODE.getStatus(), ErrorEnums.SUCCESS_CODE.getLabel(), modelMapper.map(resCommitteeType, CommitteeTypeDTO.class));
    }

    /**
     * update Committee Types service
     *
     * @param committeeTypeID
     * @param request
     * @return
     * @throws ApiRequestException
     */
    @Override
    public StandardResponse<CommitteeTypeDTO> updateCommitteeType(int committeeTypeID, CommitteeTypeDTO request) throws ApiRequestException {
        Date newDate = new Date();
        CommitteeType tempCommitteeType = this.committeeTypeRepository.findById(committeeTypeID)
                .orElseThrow(() -> new ApiRequestException("Committee Type with ID " + committeeTypeID + " does not exist"));
        tempCommitteeType.setCommitteeTypeDescription(request.getCommitteeTypeDescription());
        tempCommitteeType.setCommitteeType(request.getCommitteeType());
        tempCommitteeType.setStatus(String.valueOf(request.getStatus()));
        tempCommitteeType.setCreatedUserDisplayName(request.getCreatedUserDisplayName());
        tempCommitteeType.setModifiedDate(newDate);
        tempCommitteeType.setModifiedBy(request.getModifiedBy());
        tempCommitteeType.setIsSystem(request.getIsSystem());
        CommitteeType resCommitteeType = this.committeeTypeRepository.save(tempCommitteeType);
        return new StandardResponse<>
                (ErrorEnums.SUCCESS_CODE.getStatus(), ErrorEnums.SUCCESS_CODE.getLabel(), modelMapper.map(resCommitteeType, CommitteeTypeDTO.class));
    }

}
