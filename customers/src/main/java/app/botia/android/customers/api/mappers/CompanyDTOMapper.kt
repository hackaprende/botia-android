package app.botia.android.customers.api.mappers

import app.botia.android.customers.api.dto.CompanyDTO
import app.botia.android.customers.model.Company


class CompanyDTOMapper {
    fun fromCompanyDTOToCompanyDomain(companyDTO: CompanyDTO) =
        Company(companyDTO.id, companyDTO.phoneNumber, companyDTO.timezone)
}