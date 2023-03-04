package ru.itis.master.party.dormdeals.services.impl.ProductServicesImpl;



import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import ru.itis.master.party.dormdeals.dto.ProductDto.NewProduct;
import ru.itis.master.party.dormdeals.dto.ProductDto.ProductDto;
import ru.itis.master.party.dormdeals.dto.ProductDto.ProductsPage;
import ru.itis.master.party.dormdeals.dto.ProductDto.UpdateProduct;
import ru.itis.master.party.dormdeals.exceptions.NotFoundException;
import ru.itis.master.party.dormdeals.models.Product;
import ru.itis.master.party.dormdeals.models.Shop;
import ru.itis.master.party.dormdeals.repositories.ProductsRepository;
import ru.itis.master.party.dormdeals.repositories.ShopsRepository;
import ru.itis.master.party.dormdeals.services.ProductServices.ProductService;

import static ru.itis.master.party.dormdeals.dto.ProductDto.ProductDto.from;

@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {
    private final ProductsRepository productsRepository;
    private final ShopsRepository shopsRepository;

    @Value("${default.page-size}")
    private int defaultPageSize;

    @Override
    public ProductsPage getAllProducts(int page) {
        PageRequest pageRequest = PageRequest.of(page, defaultPageSize);
        Page<Product> productsPage = productsRepository.findAllByStateOrderById(Product.State.ACTIVE, pageRequest);

        return ProductsPage.builder()
                .products(from(productsPage.getContent()))
                .totalPageCount(productsPage.getTotalPages())
                .build();
    }

    @Override
    public ProductDto addProduct(NewProduct newProduct, Long shopId) {
        Shop shop = shopsRepository.findById(shopId).orElseThrow(() -> new NotFoundException("Магазин не найден"));

        Product product = Product.builder()
                .name(newProduct.getName())
                .description(newProduct.getDescription())
                .category(newProduct.getCategory())
                .price(newProduct.getPrice())
                .count_in_storage(newProduct.getCount_in_storage())
                .shop(shop)
                .state(Product.State.ACTIVE)
                .build();

        productsRepository.save(product);

        return from(product);
    }

    @Override
    public ProductDto getProduct(Long productId) {
        Product product = getProductOrThrow(productId);
        return from(product);
    }

    @Override
    public ProductDto updateProduct(Long productId, UpdateProduct updatedProduct) {
        Product productForUpdate = getProductOrThrow(productId);

        productForUpdate.setName(updatedProduct.getName());
        productForUpdate.setDescription(updatedProduct.getDescription());
        productForUpdate.setPrice(updatedProduct.getPrice());
        productForUpdate.setCount_in_storage(updatedProduct.getCount_in_storage());

        productsRepository.save(productForUpdate);

        return from(productForUpdate);
    }

    @Override
    public void deleteProduct(Long productId) {
        Product productForDelete = getProductOrThrow(productId);

        productForDelete.setState(Product.State.DELETED);
        productsRepository.save(productForDelete);
    }

    @Override
    public ProductsPage getAllProductsByShop(int page, Long shopId) {
        PageRequest pageRequest = PageRequest.of(page, defaultPageSize);
        Page<Product> productsPage = productsRepository.findAllByShopIdAndStateOrderById(shopId, Product.State.ACTIVE, pageRequest);

        return ProductsPage.builder()
                .products(from(productsPage.getContent()))
                .totalPageCount(productsPage.getTotalPages())
                .build();
    }


    private Product getProductOrThrow(Long productId) {
        return productsRepository.findById(productId)
                .orElseThrow(() -> new NotFoundException("Товар с идентификатором <" + productId + "> не найден"));
    }

}
