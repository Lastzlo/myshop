package com.example.myshop.services;

import com.example.myshop.domain.LinkedDirectory;
import com.example.myshop.domain.Photo;
import com.example.myshop.domain.Product;
import com.example.myshop.repos.LinkedDirectoryRepo;
import com.example.myshop.repos.PhotoRepo;
import com.example.myshop.repos.ProductRepo;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.LocalDateTime;
import java.util.*;

@Service
public class ProductService {

    @Autowired
    private LinkedDirectoryRepo directoryRepo;
    @Autowired
    private ProductRepo productRepo;
    @Autowired
    private PhotoRepo photoRepo;

    @Value("${upload.path}")
    private String uploadPath;

    public List<Product> getAllProducts () {
        return productRepo.findAll();
    }

    public Product getProduct (Long id) {
        return productRepo.getOne(id);
    }

    public Product saveProduct (Product product) {
        product.setCreationDate (LocalDateTime.now ());


        Set<LinkedDirectory> tagsFromDb = new HashSet<>();

        if(product.getTags () != null){
            Set<LinkedDirectory> tags = new HashSet<> (){{addAll (product.getTags ());}};
            product.getTags ().clear ();
            tags.forEach (tag -> {
                        this.directoryRepo.findById (tag.getId ()).ifPresent (
                                tagFromDb -> {

                                    product.addTag (tagFromDb);
                                    tagsFromDb.add (tagFromDb);
                                    //tagFromDb.addProduct ();
                                }
                        );
                    }
            );
        }
        final Product finalProduct = productRepo.save(product);

        //Добавляет товар в каждый тег
        tagsFromDb.forEach (tag -> {
            tag.addProduct (finalProduct);
            directoryRepo.save (tag);

        });

        return finalProduct;
    }

    public Product saveProductWithFile (Product product, Optional<MultipartFile[]> files) {

        if(files.isPresent ()){

            final MultipartFile[] multipartFiles = files.get ();

            File uploadDir = new File (uploadPath);

            if(!uploadDir.exists ()){
                uploadDir.mkdir ();
            }

            //иницыализирует массив для фото
            product.setPhotos (new HashSet<> ());

            //рандомный индентификатор
            String uuidFile = UUID.randomUUID ().toString ();

            //получаем путь
            Path rootLocation = Paths.get(String.valueOf(uploadDir));
            //System.out.println("rootLocation = "+rootLocation.toString());

            for (MultipartFile multipartFile: multipartFiles
            ) {

                //дополняем имя файла чтобы не возникало коллизий
                String resultFilename = uuidFile + "." + multipartFile.getOriginalFilename ();
                //String resultFilename = multipartFile.getOriginalFilename ();
                try {
                    try (InputStream inputStream = multipartFile.getInputStream()) {
                        Files.copy(inputStream, rootLocation.resolve(resultFilename),
                                StandardCopyOption.REPLACE_EXISTING);
                    }
                }
                catch (IOException e) {
                    System.out.println ("Failed to store file ");
                }

                String src = "/img/" + resultFilename;
                Photo photo = new Photo (resultFilename, src);

                //сначала сохраняем информацию о фото в БД
                //потом записываем в продукт
                product.addPhoto(photoRepo.save (photo));
            }

        }

        return saveProduct (product);
    }

    public void deleteProduct (Long id) {
        productRepo.findById (id).ifPresent (
                product -> {
                    product.getTags ().forEach (
                            tag -> {
                                tag.deleteProduct (product);
                                directoryRepo.save (tag);
                            }
                    );
                }
        );
        productRepo.deleteById(id);
    }


    /*public Product updateProduct (Product product) {

        Product productFromDb = productRepo.findById (product.getId ()).get ();

        if(productFromDb!= null){
            BeanUtils.copyProperties (product, productFromDb, "id", "tags", "photos", "creationDate");
            productFromDb.setTags (product.getTags ());
            return productRepo.save (productFromDb);
        }

        return product;
    }*/

    public Product updateProductWithFile (Product product, Optional<MultipartFile[]> files) {
        Optional<Product> optionalProductFromDb = productRepo.findById (product.getId ());

        if(optionalProductFromDb.isPresent ()){
            final Product productFromDb = optionalProductFromDb.get ();

            BeanUtils.copyProperties (product, productFromDb, "id", "photos", "photoToDelete", "tags","creationDate");

            //Работа с тегами
            if(product.getTags () != null){
                //очистить все теги
                productFromDb.getTags ().clear ();
                //поиск тегов в бд, если есть то добавлять
                product.getTags ().forEach (tag -> {
                            this.directoryRepo.findById (tag.getId ()).ifPresent (
                                    tagFromDb -> productFromDb.addTag (tagFromDb)
                            );
                        }
                );
            }

            //удаление фото с продукта и потом с хранилища
            if(product.getPhotoToDelete () != null){
                product.getPhotoToDelete ().forEach (
                        photo -> {
                            photoRepo.findById (photo.getId ()).ifPresent (
                                    item -> {
                                        //удаление фото с продукта
                                        productFromDb.deletePhoto(item);
                                        photoRepo.delete (item);

                                        //удалить с хранилища
                                        //хранилище.удалить(item)
                                        /*if(new File (uploadPath + "/" + item.getName ()).delete ()){
                                            System.out.println("Deleted the file: " + item.getName ());
                                        } else {
                                            System.out.println("Failed to delete the file.");
                                        }*/
                                        new File (uploadPath + "/" + item.getName ()).delete ();
                                    }
                            );
                        }
                );
            }

            //проверкачто мы передали новые фото
            if(files.isPresent ()){

                final MultipartFile[] multipartFiles = files.get ();

                File uploadDir = new File (uploadPath);
                //эту проверку в будущем убрать она, должна выполняться во время старта приложения
                if(!uploadDir.exists ()){
                    uploadDir.mkdir ();
                }

                //рандомный индентификатор
                String uuidFile = UUID.randomUUID ().toString ();

                //получаем путь
                Path rootLocation = Paths.get(String.valueOf(uploadDir));
                //System.out.println("rootLocation = "+rootLocation.toString());

                for (MultipartFile multipartFile: multipartFiles
                ) {
                    //дополняем имя файла чтобы не возникало коллизий
                    String resultFilename = uuidFile + "." + multipartFile.getOriginalFilename ();
                    //String resultFilename = multipartFile.getOriginalFilename ();
                    try {
                        try (InputStream inputStream = multipartFile.getInputStream()) {
                            Files.copy(inputStream, rootLocation.resolve(resultFilename),
                                    StandardCopyOption.REPLACE_EXISTING);
                        }
                    }
                    catch (IOException e) {
                        System.out.println ("Failed to store file ");
                    }

//                    try {
//                        multipartFile.transferTo (new File (uploadPath + "/" + resultFilename));
//                    } catch (IOException e) {
//                        System.out.println ("файл не переместили в папку ");
//                    }

                    //путь по которому можна получить картинку
                    String src = "/img/" + resultFilename;
                    Photo photo = new Photo (resultFilename, src);

                    //сначала сохраняем информацию о фото в БД
                    //потом записываем в продукт
                    productFromDb.addPhoto(photoRepo.save (photo));
                }

            }

            return productRepo.save (productFromDb);
        }

        //нужно обработать ответ если вдруг небыло такого продукта
        return null;
    }
}
