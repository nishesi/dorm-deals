package ru.itis.master.party.dormdeals.services.impl;

import lombok.RequiredArgsConstructor;
import org.apache.tika.Tika;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;
import ru.itis.master.party.dormdeals.models.File;
import ru.itis.master.party.dormdeals.models.Product;
import ru.itis.master.party.dormdeals.models.Shop;
import ru.itis.master.party.dormdeals.models.User;
import ru.itis.master.party.dormdeals.repositories.ProductsRepository;
import ru.itis.master.party.dormdeals.repositories.ShopRepository;
import ru.itis.master.party.dormdeals.repositories.UserRepository;
import ru.itis.master.party.dormdeals.services.ResourceService;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ResourceServiceImpl implements ResourceService {
    private final UserRepository userRepository;
    private final ProductsRepository productsRepository;
    private final ShopRepository shopRepository;
    private final Tika tika;
    @Value("${storage.path}")
    private String storagePath;

    @Override
    public void uploadResource(MultipartFile file, Long id, File.FileDtoType dtoType) {
        String fileNameWithExtension = "";
        try {
            String mimeType = tika.detect(file.getBytes());
            String fileType = mimeType.substring(0, mimeType.indexOf("/"));
            String extension = "." + mimeType.substring(mimeType.indexOf("/") + 1);
            fileNameWithExtension = StringUtils.cleanPath(fileType + "id" + id + extension);
            saveResourceInStorage(file, dtoType.toString().toLowerCase(), fileNameWithExtension);
        } catch (IOException e) {
            e.printStackTrace();
        }


        if (dtoType.equals(File.FileDtoType.SHOP)) {
            Shop shop = shopRepository.findById(id).orElseThrow();
            shop.setResource(fileNameWithExtension);
            shopRepository.save(shop);

        } else if (dtoType.equals(File.FileDtoType.USER)) {
            User user = userRepository.findById(id).orElseThrow();
            user.setResource(fileNameWithExtension);
            userRepository.save(user);
        }
    }

    @Override
    public void uploadResource(MultipartFile[] files, Long id, File.FileDtoType dtoType) {
        int resourceCounter = 1;
        List<String> fileNames = new ArrayList<>();
        for (MultipartFile file : files) {
            try {
                String mimeType = tika.detect(file.getBytes());
                String fileType = mimeType.substring(0, mimeType.indexOf("/"));
                String extension = "." + mimeType.substring(mimeType.indexOf("/") + 1);
                String fileNameWithExtension = StringUtils.cleanPath(fileType + "id" + id + "-" + resourceCounter + extension);
                saveResourceInStorage(file, dtoType.toString().toLowerCase(), fileNameWithExtension);
                resourceCounter++;
                fileNames.add(fileNameWithExtension);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        Product product = productsRepository.findById(id).orElseThrow();
        product.setResources(fileNames);
        productsRepository.save(product);
    }

    @Override
    public String getFileName(String dtoType, Long id) {
        return switch (dtoType) {
            case "shop" -> shopRepository.getResourceById(id);
            case "user" -> userRepository.getResourceById(id);
            default -> null;
        };
    }

    @Override
    public String getFileName(Long id, Integer numberResource) {
        return productsRepository.getResourceById(id)
                .stream()
                .filter(resource -> {
                    String[] parts = resource.split("-");
                    String numberPart = parts[1].split("\\.")[0];
                    return numberPart.equals(numberResource.toString());
                })
                .findFirst()
                .orElse(null);
    }

    private void saveResourceInStorage(MultipartFile file, String dtoType, String fileName) throws IOException {
        try {
            Path filePath = Path.of(storagePath, dtoType, fileName);
            Path directoryPath = filePath.getParent();
            if (!Files.exists(directoryPath)) {
                Files.createDirectories(directoryPath);
            }
            Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);

        } catch (IOException e) {
            throw new IOException("error");
        }
    }
}
