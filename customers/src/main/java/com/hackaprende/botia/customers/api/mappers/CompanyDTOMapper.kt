package com.hackaprende.botia.customers.api.mappers

import com.hackaprende.botia.customers.api.dto.CompanyDTO
import com.hackaprende.botia.customers.model.Company

class CompanyDTOMapper {
    fun fromCompanyDTOToCompanyDomain(companyDTO: CompanyDTO) =
        Company(companyDTO.id, companyDTO.phoneNumber)
}