package vn.edu.tdtu.springcommerce.repository;

import vn.edu.tdtu.springcommerce.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
    @Query("SELECT p.id,p.name,p.brand,p.color,p.price,p.description,p.imageName,p.weight FROM Product p WHERE p.id IN (select pc.product.id from ProductCategory pc where pc.category.id in (select c.id from Category c where (:category is null or c.name like :category))) and (:color is null or p.color like :color) and (:brand is null or p.brand like :brand) and (:name is null or p.name like CONCAT('%', :name, '%')) and (:minPrice is null or p.price >= :minPrice) and (:maxPrice is null or p.price <= :maxPrice)")
    List<Object[]> searchByManyCondition(@Param("category") String category, @Param("name") String name, @Param("brand") String brand, @Param("color") String color,@Param("minPrice") Double minPrice,@Param("maxPrice") Double maxPrice);


    List<Product> findAllByCategories_Id(Long id);

}