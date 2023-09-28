package com.hackaprende.botia.customers.repository

import com.hackaprende.botia.core.api.ApiResponseStatus
import com.hackaprende.botia.core.api.ApiService
import com.hackaprende.botia.core.api.Network
import com.hackaprende.botia.core.api.dto.CompanyDTOMapper
import com.hackaprende.botia.core.model.Company
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

interface CompanyRepository {
    fun getUserCompany(): Flow<ApiResponseStatus<Company>>
}

class CompanyRepositoryImpl @Inject constructor(
    private val apiService: ApiService,
    private val network: Network
) : CompanyRepository {
    override fun getUserCompany() =
        network.makeNetworkCall {
            val userCompanyResponse = apiService.getUserCompany()

            val responseStatus = userCompanyResponse.responseStatus
            if (responseStatus != "success") {
                throw Exception(responseStatus)
            }

            val companyDTO = userCompanyResponse.company
            val companyDTOMapper = CompanyDTOMapper()
            companyDTOMapper.fromCompanyDTOToCompanyDomain(companyDTO)
        }
}