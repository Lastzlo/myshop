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

        if(product.getTags () != null){
            Set<LinkedDirectory> tags = new HashSet<> (){{addAll (product.getTags ());}};
            product.getTags ().clear ();
            tags.forEach (tag -> {
                        this.directoryRepo.findById (tag.getId ()).ifPresent (
                                tagFromDb -> product.addTag (tagFromDb)
                        );
                    }
            );
        }

        return productRepo.save(product);
    }


    public Product saveProductWithFile (Product product, Optional<MultipartFile[]> files) {

        if(files.isPresent ()){

            final MultipartFile[] multipartFiles = files.get ();

            File uploadDir = new File (uploadPath);

            if(!uploadDir.exists ()){
                uploadDir.mkdir ();
            }

//            String uuidFile = UUID.randomUUID ().toString ();
//            String resultFilename = uuidFile + "." + multipartFile.getOriginalFilename ();

            //иницыализирует массив для фото
            product.setPhotos (new HashSet<> ());

            //получаем путь
            //Path rootLocation = Paths.get(uploadPath);

            for (MultipartFile multipartFile: multipartFiles
            ) {
                String resultFilename = multipartFile.getOriginalFilename ();
//                try {
//                    try (InputStream inputStream = multipartFile.getInputStream()) {
//                        Files.copy(inputStream, rootLocation.resolve(resultFilename),
//                                StandardCopyOption.REPLACE_EXISTING);
//                    }
//                }
//                catch (IOException e) {
//                    System.out.println ("Failed to store file ");
//                }
                try {
                    multipartFile.transferTo (new File (uploadPath + "/" + resultFilename));
                } catch (IOException e) {
                    System.out.println ("файл не переместили в папку ");
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
        productRepo.deleteById(id);
    }


    public Product updateProduct (Product product) {

        Product productFromDb = productRepo.findById (product.getId ()).get ();

        if(productFromDb!= null){
            BeanUtils.copyProperties (product, productFromDb, "id", "tags", "photos", "creationDate");
            productFromDb.setTags (product.getTags ());
            return productRepo.save (productFromDb);
        }

        return product;
    }


    public Product updateProductWithFile (Product product, Optional<MultipartFile[]> files) {
        Product productFromDb = productRepo.findById (product.getId ()).get ();

        if(productFromDb!= null){
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


//                if(product.getPhotos () == null){
//                    product.setPhotos (new HashSet<> ());
//                }

                //получаем путь
                //Path rootLocation = Paths.get(uploadPath);

                for (MultipartFile multipartFile: multipartFiles
                ) {
                    //дополняем имя файла чтобы не возникало коллизий
                    String resultFilename = uuidFile + "." + multipartFile.getOriginalFilename ();
                    //String resultFilename = multipartFile.getOriginalFilename ();
//                try {
//                    try (InputStream inputStream = multipartFile.getInputStream()) {
//                        Files.copy(inputStream, rootLocation.resolve(resultFilename),
//                                StandardCopyOption.REPLACE_EXISTING);
//                    }
//                }
//                catch (IOException e) {
//                    System.out.println ("Failed to store file ");
//                }
                    try {
                        multipartFile.transferTo (new File (uploadPath + "/" + resultFilename));
                    } catch (IOException e) {
                        System.out.println ("файл не переместили в папку ");
                    }

                    //путь по которому можна получить картинку
                    String src = "/img/" + resultFilename;
                    Photo photo = new Photo (resultFilename, src);

                    //сначала сохраняем информацию о фото в БД
                    //потом записываем в продукт
                    productFromDb.addPhoto(photoRepo.save (photo));
                }

            }
        }

        return productRepo.save (productFromDb);
    }
}
