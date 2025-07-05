package org.saavatech.inventorymgtsystem.dtos;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.saavatech.inventorymgtsystem.enums.TransactionStatus;
import org.saavatech.inventorymgtsystem.enums.TransactionType;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class TransactionDto {

    private Long id;

    private Integer totalProducts;
    private BigDecimal totalPrice;

    private TransactionType transactionType;

    private TransactionStatus status;

    private String description;
    private String note;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    private ProductDto product;

    private UserDto user;

    private SupplierDto supplier;


}
