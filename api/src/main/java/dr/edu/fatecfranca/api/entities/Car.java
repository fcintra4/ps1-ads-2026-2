package dr.edu.fatecfranca.api.entities;

import java.math.BigDecimal;
import java.time.LocalDate;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "cars")
public class Car {
   @Id
   @GeneratedValue(strategy = GenerationType.IDENTITY)
   private Long id;

   @Column(name = "brand", nullable = false)
   private String brand;

   @Column(name = "model", nullable = false)
   private String model;

   @Column(name = "color", nullable = false)
   private String color;

   @Column(name = "year_manufacture", nullable = false)
   private Integer year_manufacture;

   @Column(name = "imported", nullable = false)
   private Boolean imported;

   @Column(name = "plates", nullable = false)
   private String plates;

   @Column(name = "selling_date", nullable = true)
   private LocalDate selling_date;

   @Column(name = "selling_price", precision = 12, scale = 2)
   private BigDecimal sellingPrice;

   //@Column(name = "customer_id", nullable = false)
   //private Long customer_id;

   @ManyToOne
   @JoinColumn(name = "customer_id")
   private Customer customer;

   public Long getId() {
    return id;
   }

   public void setId(Long id) {
    this.id = id;
   }

   public String getBrand() {
    return brand;
   }

   public void setBrand(String brand) {
    this.brand = brand;
   }

   public String getModel() {
    return model;
   }

   public void setModel(String model) {
    this.model = model;
   }

   public String getColor() {
    return color;
   }

   public void setColor(String color) {
    this.color = color;
   }

   public Integer getYear_manufacture() {
    return year_manufacture;
   }

   public void setYear_manufacture(Integer year_manufacture) {
    this.year_manufacture = year_manufacture;
   }

   public Boolean getImported() {
    return imported;
   }

   public void setImported(Boolean imported) {
    this.imported = imported;
   }

   public String getPlates() {
    return plates;
   }

   public void setPlates(String plates) {
    this.plates = plates;
   }

   public LocalDate getSelling_date() {
    return selling_date;
   }

   public void setSelling_date(LocalDate selling_date) {
    this.selling_date = selling_date;
   }

   public BigDecimal getSellingPrice() {
    return sellingPrice;
   }

   public void setSellingPrice(BigDecimal sellingPrice) {
    this.sellingPrice = sellingPrice;
   }

   //public Long getCustomer_id() {
    //return customer_id;
   //}

   //public void setCustomer_id(Long customer_id) {
    //this.customer_id = customer_id;
   //}

   // customerId agora é obtido a partir do relacionamento
   public Long getCustomerId() {
    return customer != null ? customer.getId() : null;
   }


   public void setCustomer(Customer customer) {
    this.customer = customer;
   }

}