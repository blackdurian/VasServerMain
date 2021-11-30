package com.fyp.vasclinicserver.repository;

import com.fyp.vasclinicserver.model.Invoice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface InvoiceRepository extends JpaRepository<Invoice,String> {
}
