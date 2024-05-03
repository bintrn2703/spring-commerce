package vn.edu.tdtu.springcommerce.service.impl;

import vn.edu.tdtu.springcommerce.model.Product;
import vn.edu.tdtu.springcommerce.repository.ProductRepository;
import vn.edu.tdtu.springcommerce.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Component
public class ProductServiceImpl implements ProductService{
    @Autowired
    ProductRepository productRepository;

    @Override //search
    public List<Product> searchByManyCondition(String category, String name,String brand,Double minPrice,Double maxPrice,String color){
        List<Object[]> temp = productRepository.searchByManyCondition(category,name,brand,color,minPrice,maxPrice);
        List<Product> products = new ArrayList<>();
        for (Object[] obj : temp) {
            Product product = new Product();
            product.setId((Long) obj[0]);
            product.setName((String) obj[1]);
            product.setBrand((String) obj[2]);
            product.setColor((String) obj[3]);
            product.setPrice((Double) obj[4]);
            product.setDescription((String) obj[5]);
            product.setImageName((String) obj[6]);
            product.setWeight((Double) obj[7]);
            // set các thông tin khác của sản phẩm
            products.add(product);
        }
        return products;
    }

    @Override
	public List<Product> getAllProduct() {
        return productRepository.findAll();
    }//findAll

    @Override
	public Product addOrUpdateProduct(Product product) {
        return productRepository.save(product);
    }//add or update (tuy vao pri-key)

    @Override
    public boolean removeProductById(Long id) {
        Optional<Product> optionalProduct = productRepository.findById(id);
        if (optionalProduct.isPresent()) {
            productRepository.deleteById(id);
            return true;
        } else {
            return false;
        }
    }//delete dua vao pri-key

    @Override
    public Optional<Product> getProductById(Long id) {
        return productRepository.findById(id);
    }

    @Override
    public List<Product> getAllProductByCategoryId(Long id) {
        return productRepository.findAllByCategories_Id(id);
    }
    //findList theo ProductDTO.categoryId

}
