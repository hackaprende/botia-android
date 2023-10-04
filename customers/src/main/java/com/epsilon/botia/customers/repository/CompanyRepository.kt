package com.epsilon.botia.customers.repository

import com.epsilon.botia.core.api.ApiResponseStatus
import com.epsilon.botia.core.api.Network
import com.epsilon.botia.customers.api.services.CompanyApiService
import com.epsilon.botia.customers.api.mappers.CompanyDTOMapper
import com.epsilon.botia.customers.model.Company
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