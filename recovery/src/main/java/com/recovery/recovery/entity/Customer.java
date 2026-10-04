package com.recovery.recovery.entity;

// Customer.java
import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "customer")
@Getter @Setter @NoArgsConstructor
public class Customer {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String shopifyCustomerId;

    private String email;
    private String name;
    private String phone;

    @Column(updatable = false)
    private LocalDateTime createdAt;

    @OneToMany(mappedBy = "customer", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Checkout> checkouts = new ArrayList<>();

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getShopifyCustomerId() { return shopifyCustomerId; }
    public void setShopifyCustomerId(String shopifyCustomerId) { this.shopifyCustomerId = shopifyCustomerId; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
    public List<Checkout> getCheckouts() { return checkouts; }
    public void setCheckouts(List<Checkout> checkouts) { this.checkouts = checkouts; }

    @PrePersist
    void onCreate() { createdAt = LocalDateTime.now(); }
}