package com.imannuel.movin.commonservice.dto.response.template;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CommonResponse<T> {
    private boolean success;

    private String message;

    private T data;

    private PaginationResponse pagination;
}
