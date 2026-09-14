package br.edu.fatecfranca.api.entities;

import java.math.BigDecimal;
import java.time.LocalDate;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity @Table(name = "cars") public class Cars {

   public Long getId() { return id; }
   public void setId(Long id) { this.id = id; }

   public String getBrand() { return brand; }
   public void setBrand(String brand) { this.brand = brand; }

   public String getModel() { return model; }
   public void setModel(String model) { this.model = model; }

   public String getColor() { return color; }
   public void setColor(String color) { this.color = color; }

   public Integer getYearManufacture() { return yearManufacture; }
   public void setYearManufacture(Integer yearManufacture) { this.yearManufacture = yearManufacture; }

   public Boolean getImported() { return imported; }
   public void setImported(Boolean imported) { this.imported = imported; }

   public String getPlates() { return plates; }
   public void setPlates(String plates) { this.plates = plates; }

   public LocalDate getSellingDate() { return sellingDate; }
   public void setSellingDate(LocalDate sellingDate) { this.sellingDate = sellingDate; }

   public BigDecimal getSellingPrice() { return sellingPrice; }
   public void setSellingPrice(BigDecimal sellingPrice) { this.sellingPrice = sellingPrice; }

   public Customers getCustomer() { return customer; }
   public void setCustomer(Customers customer) { this.customer = customer; }

   @Id @GeneratedValue(strategy = GenerationType.IDENTITY) private Long id;
   @Column(name = "brand", nullable = false) private String brand;
   @Column(name = "model", nullable = false) private String model;
   @Column(name = "color", nullable = false) private String color;
   @Column(name = "year_manufacture", nullable = false) private Integer yearManufacture;
   @Column(name = "imported", nullable = false) private Boolean imported;
   @Column(name = "plates", nullable = false, unique = true) private String plates;
   @Column(name = "selling_date") private LocalDate sellingDate;
   @Column(name = "selling_price", precision = 12, scale = 2) private BigDecimal sellingPrice;
   @ManyToOne(fetch = FetchType.LAZY) 
   @JoinColumn(name = "customer_id") 
   private Customers customer;

   public Cars() {}
}
