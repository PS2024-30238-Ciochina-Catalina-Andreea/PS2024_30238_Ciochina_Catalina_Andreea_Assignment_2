package com.example.flowerShop.repository;

import com.example.flowerShop.entity.Product;;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface ProductRepository extends JpaRepository<Product, UUID> {

    Optional<Product> findById(@Param("id") UUID id);

    Optional<Product> findByName(String name);

    List<Product> findProjectedByIdIn(List<UUID> orderItemIds);
}
