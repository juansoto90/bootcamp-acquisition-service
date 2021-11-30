package com.nttdata.acquisition.model.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Document("acquisition")
public class Acquisition {
    @Id
    private String id;
    private String idCustomer;
    private Customer Customer;
    private String productType;
    private List<Customer> customerOwner;
    private List<Customer> customerAuthorizedSigner;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime acquisitionDate = LocalDateTime.now();
    private String status;
}
