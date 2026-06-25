package lk.sampath.casadminportalms.service;

import lk.sampath.casadminportalms.controller.basecontroller.StandardResponse;
import lk.sampath.casadminportalms.dto.committetype.CommitteeTypeDTO;
import lk.sampath.casadminportalms.entity.committeetype.CommitteeType;
import lk.sampath.casadminportalms.exception.ApiRequestException;
import org.springframework.data.domain.Pageable;
import java.util.List;

public interface CommitteeTypeService {

    /**
     * get Committee Types service
     * @return List<CommitteeTypeViewResponse>
     * @throws ApiRequestException
     */
    StandardResponse<List<CommitteeType>> getCommitteeTypes(Pageable pageable) throws ApiRequestException;

    /**
     * save Committee Types service
     * @param committeeTypeDTO
     * @return CommitteeTypeCreateResponse
     * @throws ApiRequestException
     */
    StandardResponse<CommitteeTypeDTO> saveCommitteeType(CommitteeTypeDTO committeeTypeDTO) throws ApiRequestException;

    /**
     * update Committee Types service
     *
     * @param committeeTypeID
     * @param request
     * @return
     * @throws ApiRequestException
     */
    StandardResponse<CommitteeTypeDTO> updateCommitteeType(int committeeTypeID,CommitteeTypeDTO request) throws ApiRequestException;
}
