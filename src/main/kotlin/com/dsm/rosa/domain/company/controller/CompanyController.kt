package com.dsm.rosa.domain.company.controller

import com.dsm.rosa.domain.company.repository.CompanyQueryDSLRepository
import com.dsm.rosa.domain.company.service.CompanySearchService
import com.dsm.rosa.global.attribute.CompanySortingColumn
import com.dsm.rosa.global.attribute.CompanySortingMethod
import org.springframework.data.domain.Pageable
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
class CompanyController(
    private val companySearchService: CompanySearchService,
    private val companyQueryDSLRepository: CompanyQueryDSLRepository,
) {

    @GetMapping("/company")
    fun searchCompany(
        @RequestParam("sorting-column")
        sortingColumn: CompanySortingColumn,
        @RequestParam("sorting-method")
        sortingMethod: CompanySortingMethod,
        pageInformation: Pageable,
    ) = companySearchService.searchCompany(
        pageInformation = pageInformation,
        sortingColumn = sortingColumn,
        sortingMethod = sortingMethod,
    )

    @GetMapping("/test")
    fun test(pageable: Pageable) {
        companyQueryDSLRepository.findBySortingCondition(pageable)
    }
}