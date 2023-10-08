package app.botia.android.customers.api.responses

import app.botia.android.customers.api.dto.CompanyDTO
import com.squareup.moshi.Json

class UserCompanyResponse(
    @field:Json(name = "response_status") val responseStatus: String,
    val company: CompanyDTO,
)