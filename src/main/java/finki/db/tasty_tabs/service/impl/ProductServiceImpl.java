package finki.db.tasty_tabs.service.impl;


import finki.db.tasty_tabs.entity.Inventory;
import finki.db.tasty_tabs.entity.Product;
import finki.db.tasty_tabs.entity.exceptions.CategoryNotFoundException;
import finki.db.tasty_tabs.entity.exceptions.ProductNotFoundException;
import finki.db.tasty_tabs.repository.InventoryRepository;
import finki.db.tasty_tabs.repository.ProductRepository;
import finki.db.tasty_tabs.service.CategoryService;
import finki.db.tasty_tabs.service.ProductService;
import finki.db.tasty_tabs.web.dto.CreateProductDto;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;
    private final CategoryService categoryService;
    private final InventoryRepository inventoryRepository;


    public ProductServiceImpl(ProductRepository productRepository, CategoryService categoryService,InventoryRepository inventoryRepository) {
        this.productRepository = productRepository;
        this.categoryService = categoryService;
        this.inventoryRepository=inventoryRepository;
    }

    @Override
    public Optional<Product> findById(Long id) {
        return productRepository.findById(id);
    }

    @Override
    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

    @Override
    public Optional<Product> updateProduct(Long id, CreateProductDto dto) {
        Optional<Product> productTmp=productRepository.findById(id);
        if(productTmp.isEmpty()){
            throw new ProductNotFoundException(id);
        }

        if(categoryService.findById(dto.categoryId()).isEmpty()){
            throw new CategoryNotFoundException();
        }

        return productRepository.findById(id).map(existingProduct -> {
            if (dto.name() != null) {
                existingProduct.setName(dto.name());
            }
            if (dto.price() != null) {
                existingProduct.setPrice(dto.price());
            }
            if(dto.taxClass()!=null){
                existingProduct.setTaxClass(dto.taxClass());
            }
            existingProduct.setCategory(categoryService.findById(dto.categoryId()).get());
            existingProduct.setDescription(dto.description());
            if(dto.manageInventory()==Boolean.TRUE){
                if(existingProduct.getManageInventory()==Boolean.FALSE){
                    inventoryRepository.save(new Inventory(id,dto.quantity(),dto.restockLevel()));
                }
                else{
                    Inventory inventorytmp=inventoryRepository.findById(id).get();
                    inventorytmp.setQuantity(dto.quantity());
                    inventorytmp.setRestockLevel(dto.restockLevel());
                    inventoryRepository.save(inventorytmp);
                }
            }
            if(dto.manageInventory()!=null){
                existingProduct.setManageInventory(dto.manageInventory());
            }
            return productRepository.save(existingProduct);
        });
    }

    @Override
    public void deleteProduct(Long id) {
        productRepository.deleteById(id);
    }

    @Override
    public List<Product> getProductsByName(String name) {

        return productRepository.findAllByName(name);
    }

    @Override
    public Optional<Product> createProduct(CreateProductDto dto) {
        if(categoryService.findById(dto.categoryId()).isEmpty()){
            throw new CategoryNotFoundException();
        }
        Product productTmp=new Product();
        if (dto.name() != null) {
            productTmp.setName(dto.name());
        }
        if (dto.price() != null) {
            productTmp.setPrice(dto.price());
        }
        if(dto.taxClass()!=null){
            productTmp.setTaxClass(dto.taxClass());
        }
        productTmp.setCategory(categoryService.findById(dto.categoryId()).get());
        productTmp.setDescription(dto.description());

        if(dto.manageInventory()!=null){
            productTmp.setManageInventory(dto.manageInventory());
        }
        Product product=productRepository.save(productTmp);
        if(product.getManageInventory()==Boolean.TRUE){
            inventoryRepository.save(new Inventory(product.getId(),dto.quantity(),dto.restockLevel()));
        }
        return Optional.of(product);
    }
}
