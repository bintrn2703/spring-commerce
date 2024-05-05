package vn.edu.tdtu.springcommerce.api;

import vn.edu.tdtu.springcommerce.model.Product;
import vn.edu.tdtu.springcommerce.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Controller
@RestController
@RequestMapping("/api/products")
public class ProductAPI {

    @Autowired
    private ProductService productService;

    //[GET] /api/products/  ---  get tất cả sản phẩm
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    @GetMapping()
    public List<Product> getAllProduct() {
        return productService.getAllProduct();
    }

    //[GET] /api/products/category/{id}  --- get sản phẩm theo category
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    @GetMapping("/category/{id}")
    public List<Product> getAllProductByCategoryId(@PathVariable Long id){
        return productService.getAllProductByCategoryId(id);
    }

    //[GET] /api/products/{id}  ---  get sản phẩm theo id
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    @GetMapping("/{id}")
    public ResponseEntity<Product> getProductById(@PathVariable Long id) throws ChangeSetPersister.NotFoundException {
        Optional<Product> product = productService.getProductById(id);
        if (product.isPresent()) {
            return ResponseEntity.ok(product.get());
        } else {
            throw new ChangeSetPersister.NotFoundException();
        }
    }
    @ExceptionHandler(ChangeSetPersister.NotFoundException.class)
    public ResponseEntity<String> handleNotFoundException(ChangeSetPersister.NotFoundException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
    }

    //[DELETE] /api/products/{id}  ---  xóa sản phẩm dựa trên id truyền vào
    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<String> removeProductById(@PathVariable Long id){
        boolean isDeleted = productService.removeProductById(id);
        if (isDeleted) {
            return ResponseEntity.ok("Xóa sản phẩm thành công");
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Không tìm thấy sản phẩm với ID = " + id);
        }
    }

    //[PUT] /api/products/  ---  chỉnh sửa sản phẩm dựa vào id truyền vào
    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping()
    public ResponseEntity<Object> updateProduct(@RequestBody Map<String, Object> productMap) {
        if (productMap.get("id") == null) {
            return ResponseEntity.badRequest().body("Please provide product id");
        }
        Long productId = Long.parseLong(productMap.get("id").toString());
        Optional<Product> optionalProduct = productService.getProductById(productId);
        if (optionalProduct.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        Product product = optionalProduct.get();
        if (productMap.containsKey("name")) {
            product.setName(productMap.get("name").toString());
        }
        if (productMap.containsKey("description")) {
            product.setDescription(productMap.get("description").toString());
        }
        if (productMap.containsKey("color")) {
            product.setColor(productMap.get("color").toString());
        }
        if (productMap.containsKey("brand")) {
            product.setBrand(productMap.get("brand").toString());
        }
        if (productMap.containsKey("weight")) {
            product.setWeight(Double.parseDouble(productMap.get("weight").toString()));
        }
        if (productMap.containsKey("price")) {
            product.setPrice(Double.parseDouble(productMap.get("price").toString()));
        }
        if (productMap.containsKey("imageName")) {
            product.setImageName(productMap.get("imageName").toString());
        }
        Product updatedProduct = productService.addOrUpdateProduct(product);
        return ResponseEntity.ok(updatedProduct);
    }

    //[POST] /api/products/  ---  thêm sản phẩm
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping()
    public ResponseEntity<Product> postProduct(@RequestBody Product product) {
        Product updatedProduct = productService.addOrUpdateProduct(product);
        return ResponseEntity.ok(updatedProduct);
    }

}
