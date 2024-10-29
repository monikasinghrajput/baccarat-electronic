package model.baccarat.data

case class LicenseData(name: String = "Baccarat",
                       client: String = "BigB",
                       install: String = "2023-01-31",
                       macs: List[String] = List.empty[String],
                       validProductCode: Boolean = true,
                       validProfitCode: Boolean = true,
                       toBeExpired: Boolean = false,
                       productCode: String = "2509330773",
                       profitCode: String = "4260441857")
