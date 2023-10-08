package app.botia.android.customers.repository

import app.botia.android.core.api.ApiResponseStatus
import app.botia.android.core.api.Network
import app.botia.android.customers.api.mappers.CompanyDTOMapper
import app.botia.android.customers.api.services.CompanyApiService
import app.botia.android.customers.model.Company
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

interface CompanyRepository {
    fun getUserCompany(): Flow<ApiResponseStatus<Company>>
}

class CompanyRepositoryImpl @Inject constructor(
    private val companyApiService: CompanyApiService,
    private val network: Network
) : CompanyRepository {
    override fun getUserCompany() =
        network.makeNetworkCall {
            val userCompanyResponse = companyApiService.getUserCompany()

            val responseStatus = userCompanyResponse.responseStatus
            if (responseStatus != "success") {
                throw Exception(responseStatus)
            }

            val companyDTO = userCompanyResponse.company
            val companyDTOMapper = CompanyDTOMapper()
            companyDTOMapper.fromCompanyDTOToCompanyDomain(companyDTO)
        }
}