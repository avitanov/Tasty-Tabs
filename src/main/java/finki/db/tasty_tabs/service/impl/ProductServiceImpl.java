package finki.db.tasty_tabs.service.impl;


import finki.db.tasty_tabs.entity.Inventory;
import finki.db.tasty_tabs.entity.Product;
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
    public Product findById(Long id) {
        return productRepository.findById(id)
                .orElseThrow(() -> new ProductNotFoundException(id));
    }

    @Override
    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

    @Override
    public Product updateProduct(Long id, CreateProductDto dto) {
        Product existingProduct = productRepository.findById(id)
                .orElseThrow(() -> new ProductNotFoundException(id));

        // Update product fields
        if (dto.name() != null) {
            existingProduct.setName(dto.name());
        }
        if (dto.price() != null) {
            existingProduct.setPrice(dto.price());
        }
        if (dto.taxClass() != null) {
            existingProduct.setTaxClass(dto.taxClass());
        }
        if (dto.categoryId() != null) {
            existingProduct.setCategory(categoryService.findById(dto.categoryId()));
        }
        if (dto.description() != null) {
            existingProduct.setDescription(dto.description());
        }

        // Manage inventory
        if (Boolean.TRUE.equals(dto.manageInventory())) {
            // ако производот претходно не менаџирал inventory -> креирај нов
            if (Boolean.FALSE.equals(existingProduct.getManageInventory())) {
                Inventory inventory = new Inventory(existingProduct, dto.quantity(), dto.restockLevel());
                inventoryRepository.save(inventory);
            } else {
                // ако веќе постои -> update
                Inventory inventory = inventoryRepository.findById(id)
                        .orElseThrow(() -> new RuntimeException("Inventory not found for product " + id));
                if (dto.quantity() != null) {
                    inventory.setQuantity(dto.quantity());
                }
                if (dto.restockLevel() != null) {
                    inventory.setRestockLevel(dto.restockLevel());
                }
                inventoryRepository.save(inventory);
            }
        } else if (Boolean.FALSE.equals(dto.manageInventory())) {
            // ако од TRUE се префрла на FALSE -> бриши го inventory-то
            inventoryRepository.findById(id).ifPresent(inventoryRepository::delete);
        }

        // на крај секогаш го сетирај manageInventory флагот
        if (dto.manageInventory() != null) {
            existingProduct.setManageInventory(dto.manageInventory());
        }

        return productRepository.save(existingProduct);
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
    public Product createProduct(CreateProductDto dto) {
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
        productTmp.setCategory(categoryService.findById(dto.categoryId()));
        productTmp.setDescription(dto.description());

        if(dto.manageInventory()!=null){
            productTmp.setManageInventory(dto.manageInventory());
        }
        Product product=productRepository.save(productTmp);
        if(product.getManageInventory()==Boolean.TRUE){
            Inventory inventory = new Inventory(product, dto.quantity(), dto.restockLevel());
            inventoryRepository.save(inventory);
        }
        return product;
    }
}
