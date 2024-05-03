package vn.edu.tdtu.springcommerce.service;

import vn.edu.tdtu.springcommerce.model.Product;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public interface ProductService {

	List<Product> getAllProductByCategoryId(Long id);

	Optional<Product> getProductById(Long id);

	boolean removeProductById(Long id);

	Product addOrUpdateProduct(Product product);

	List<Product> getAllProduct();

	List<Product> searchByManyCondition(String category, String name,String brand,Double minPrice,Double maxPrice,String color);

}
