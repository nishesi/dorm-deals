package ru.itis.master.party.dormdeals.services.impl;



import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import ru.itis.master.party.dormdeals.dto.NewProduct;
import ru.itis.master.party.dormdeals.dto.ProductDto;
import ru.itis.master.party.dormdeals.dto.ProductsPage;
import ru.itis.master.party.dormdeals.dto.UpdateProduct;
import ru.itis.master.party.dormdeals.exceptions.NotFoundException;
import ru.itis.master.party.dormdeals.models.Product;
import ru.itis.master.party.dormdeals.repositories.ProductsRepository;
import ru.itis.master.party.dormdeals.services.ProductService;

import java.awt.print.Pageable;

import static ru.itis.master.party.dormdeals.dto.ProductDto.from;

@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {
    private final ProductsRepository productsRepository;
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
    public ProductDto addProduct(NewProduct newProduct) {

        Product product = Product.builder()
                .name(newProduct.getName())
                .description(newProduct.getDescription())
                .category(newProduct.getCategory())
                .price(newProduct.getPrice())
                .count_in_storage(newProduct.getCount_in_storage())
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


    private Product getProductOrThrow(Long productId) {
        return productsRepository.findById(productId)
                .orElseThrow(() -> new NotFoundException("Товар с идентификатором <" + productId + "> не найден"));
    }

}
