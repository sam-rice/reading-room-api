package com.samrice.readingroomapi.dtos;

import java.util.List;

public record AuthorResultsPageDto(int totalResults, int pageSize, int pageNum, List<AuthorResultDto> results) {
}
