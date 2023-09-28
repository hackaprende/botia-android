package com.hackaprende.botia.core.api.dto

import com.hackaprende.botia.core.model.Company

class CompanyDTOMapper {
    fun fromCompanyDTOToCompanyDomain(companyDTO: CompanyDTO) =
        Company(companyDTO.id, companyDTO.phoneNumber)
}