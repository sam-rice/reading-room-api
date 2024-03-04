package com.samrice.readingroomapi.dtos;

import java.util.List;

public record BookResultsPageDto(int totalResults, int pageSize, int pageNum, List<BookResultDto> results) {
}
