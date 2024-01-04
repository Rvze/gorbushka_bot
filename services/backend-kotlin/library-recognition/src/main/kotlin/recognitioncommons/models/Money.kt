package recognitioncommons.models

import com.fasterxml.jackson.annotation.JsonProperty
import java.math.BigDecimal

data class Money(
    @field:JsonProperty("amount", required = true) val amount: BigDecimal,
    @field:JsonProperty("currency", required = true) val currency: String,
)
