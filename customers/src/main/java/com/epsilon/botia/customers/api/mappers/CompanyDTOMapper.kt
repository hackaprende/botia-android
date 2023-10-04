package com.epsilon.botia.customers.api.mappers

import com.epsilon.botia.customers.api.dto.CompanyDTO
import com.epsilon.botia.customers.model.Company

class CompanyDTOMapper {
    fun fromCompanyDTOToCompanyDomain(companyDTO: CompanyDTO) =
        Company(companyDTO.id, companyDTO.phoneNumber, companyDTO.timezone)
}