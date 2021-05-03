package com.dsm.rosa.domain.company.service

import com.dsm.rosa.domain.company.controller.response.MultipleCompanyResponse
import com.dsm.rosa.domain.company.repository.CompanyQueryDSLRepository
import com.dsm.rosa.domain.stock.exception.StockNotFoundException
import com.dsm.rosa.global.attribute.CompanySortingCondition
import com.dsm.rosa.global.attribute.CompanySortingMethod
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service
import java.time.LocalDate

@Service
class CompanySearchService(
    private val companyQueryDSLRepository: CompanyQueryDSLRepository,
) {

    fun searchCompany(
        pageInformation: Pageable,
        sortingCondition: CompanySortingCondition,
        sortingMethod: CompanySortingMethod,
    ) = getCompany(
        pageInformation = pageInformation,
        sortingCondition = sortingCondition,
        sortingMethod = sortingMethod,
    ).map {
        val todayStock = it
            .stocks
            .singleOrNull { stock -> stock.date == LocalDate.now() }
            ?: throw StockNotFoundException(it.tickerSymbol)

        val averagePositivity = it
            .news
            .map { news -> news.positivity }
            .average()

        MultipleCompanyResponse.CompanyResponse(
            name = it.name,
            averagePositivity = averagePositivity,
            currentPrice = todayStock.closingPrice,
            differenceFromYesterday = todayStock.differenceFromYesterday,
            fluctuationRate = todayStock.fluctuationRate,
        )
    }

    private fun getCompany(
        pageInformation: Pageable,
        sortingCondition: CompanySortingCondition,
        sortingMethod: CompanySortingMethod,
    ) = companyQueryDSLRepository.findBySortingCondition(
        pageable = createPageRequest(
            pageInformation = pageInformation,
        ),
        sortingCondition = sortingCondition,
        sortingMethod = sortingMethod,
    )

    private fun createPageRequest(
        pageInformation: Pageable,
    ) = PageRequest.of(
        pageInformation.pageNumber,
        pageInformation.pageSize,
    )
}