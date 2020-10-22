package com.example.myshop.services;

import com.example.myshop.domain.DirectoryType;
import com.example.myshop.domain.LinkedDirectory;
import com.example.myshop.domain.Photo;
import com.example.myshop.domain.Product;
import com.example.myshop.repos.LinkedDirectoryRepo;
import com.example.myshop.repos.PhotoRepo;
import com.example.myshop.repos.ProductRepo;
import com.google.common.collect.Sets;
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
import java.util.concurrent.atomic.AtomicBoolean;

@Service
public class ProductService {

    @Autowired
    private LinkedDirectoryService directoryService;
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

                                    //добавляем к товару тег
                                    product.addTag (tagFromDb);

                                    //заполнем tagsFromDb
                                    tagsFromDb.add (tagFromDb);

                                }
                        );
                    }
            );
        }
        final Product finalProduct = productRepo.save(product);

        //Обновляем теги
        tagsFromDb.forEach (tag -> {

            //добавляем продукт
            tag.addProduct (finalProduct);

            //обновляем количество продуктов связных с тегом
            tag.setProductsCount ((long) tag.getProducts ().size ());

            //проверка что DirectoryType of tag is PARAMETER or BRAND
            if(
                    tag.getDirectoryType ().equals (DirectoryType.PARAMETER.toString ())
                            || tag.getDirectoryType ().equals (DirectoryType.BRAND.toString ())
            ){
                //добавляем в тег свзязаные с продуктом теги
                tagsFromDb.forEach (relatedDirectory -> {

                    /*if(tag!=relatedDirectory)*/
                    if(
                            tag!=relatedDirectory
                                    && (
                                    relatedDirectory.getDirectoryType ()
                                            .equals (DirectoryType.PARAMETER.toString ())
                                            ||
                                            relatedDirectory.getDirectoryType ()
                                                    .equals (DirectoryType.BRAND.toString ()))
                    ){

                        //плюс сета в том что не будет повторений
                        tag.addRelatedDirectory (relatedDirectory);
                        tag.addRelatedDirectoryId (relatedDirectory.getId ());

                    }
                });
            }

            //сохраняем тег в БД
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

                //Set<LinkedDirectory> oldtagsFromDb = productFromDb.getTags ();
                Set<LinkedDirectory> oldtagsFromDb = new HashSet<> (){{
                    addAll (productFromDb.getTags ());
                }};

                //received tags
                Set<LinkedDirectory> recivedTags = new HashSet<>();
                product.getTags ().forEach (tag -> {
                            this.directoryRepo.findById (tag.getId ()).ifPresent (
                                    tagFromDb -> recivedTags.add (tagFromDb)
                            );
                        }
                );

                if(oldtagsFromDb.equals (recivedTags)){
                    //если нет отличий то пропускаем
                    System.out.println ("oldtagsFromDb.containsAll (recivedTags)");
                }else {
                    //находим теги которых нет recivedTags
                    //эти теги потом надо будет убрать из продукта
                    final Set<LinkedDirectory> tagsToDeleteFromProduct = Sets.difference(oldtagsFromDb, recivedTags);
                    /*//решил перезаписать все в Сет ведь при изминении oldtagsFromDb выпадает ошибка
                    final Set<LinkedDirectory> tagsToDeleteFromProduct = new HashSet<> (){{
                        addAll (Sets.difference(oldtagsFromDb, recivedTags));
                    }};*/

                    System.out.println ("tagsToDeleteFromProduct:");
//                    Set<Long> idsOfTagsToDeleteFromProduct = new HashSet<> ();

                    tagsToDeleteFromProduct.forEach (
                            tag-> {
//                                //заполняем idsOfTagsToDeleteFromProduct
//                                idsOfTagsToDeleteFromProduct.add (tag.getId ());
                                //выводим

                                System.out.println ("tagId = "+tag.getId ()+" tagName = "+tag.getName ());

                                //вот оно то место изза которого была ошибка
                                //удаляем с товара лишние теги
                                productFromDb.deleteTag (tag);

                                //уменьшем количество привязаных товаров к тегу
                                tag.deleteProduct (productFromDb);
                                tag.setProductsCount ((long) tag.getProducts ().size ());

                            }
                    );

                    //список актуальных, нужных тегов
                    Set<LinkedDirectory>
                            oldNeededTags = Sets.difference(oldtagsFromDb, tagsToDeleteFromProduct);

                    //список id oldNeededTags
//                    Set<Long> idsOfOldNeededTags = new HashSet<> ();

                    //убираем у oldNeededTags связи с ненужными tagsToDeleteFromProduct тегами
                    dislinkOldNeededTagsWithDontUsedTagsToDeleteFromProduct (tagsToDeleteFromProduct, oldNeededTags);

                    //обновляем tagsToDeleteFromProduct в БД
                    tagsToDeleteFromProduct.forEach (tag ->directoryRepo.save (tag));


                    //новые теги которые еще не имеют связей, не обработаные теги
                    Set<LinkedDirectory> newtags = Sets.difference(recivedTags, oldNeededTags);

                    //добавляем связи в новые теги newtags
                    newtags.forEach (tag -> {

                        //добавляем продукт
                        tag.addProduct (productFromDb);

                        //добавляем к товару тег
                        productFromDb.addTag (tag);

                        //обновляем количество продуктов связных с тегом
                        tag.setProductsCount ((long) tag.getProducts ().size ());

                        //проверка что DirectoryType of tag is PARAMETER or BRAND
                        if(
                                tag.getDirectoryType ().equals (DirectoryType.PARAMETER.toString ())
                                        || tag.getDirectoryType ().equals (DirectoryType.BRAND.toString ())
                        ){
                            //добавляем в тег свзязаные с продуктом  НОВЫЕ теги newtags
                            newtags.forEach (relatedDirectory -> {

                                if(
                                        tag!=relatedDirectory
                                                && (
                                                relatedDirectory.getDirectoryType ()
                                                        .equals (DirectoryType.PARAMETER.toString ())
                                                        ||
                                                        relatedDirectory.getDirectoryType ()
                                                                .equals (DirectoryType.BRAND.toString ()))
                                ){

                                    //плюс сета в том что не будет повторений
                                    tag.addRelatedDirectory (relatedDirectory);
                                    tag.addRelatedDirectoryId (relatedDirectory.getId ());

                                }

                            });

                            //добавляем в тег свзязаные с продуктом  НОВЫЕ теги oldNeededTags
                            oldNeededTags.forEach (relatedDirectory -> {

                                if(
                                        tag!=relatedDirectory
                                                && (
                                                relatedDirectory.getDirectoryType ()
                                                        .equals (DirectoryType.PARAMETER.toString ())
                                                        ||
                                                        relatedDirectory.getDirectoryType ()
                                                                .equals (DirectoryType.BRAND.toString ()))
                                ){

                                    //плюс сета в том что не будет повторений
                                    tag.addRelatedDirectory (relatedDirectory);
                                    tag.addRelatedDirectoryId (relatedDirectory.getId ());

                                    //добавляем связь между oldNeededTag и tag
                                    relatedDirectory.addRelatedDirectory (tag);
                                    relatedDirectory.addRelatedDirectoryId (tag.getId ());

                                }

                            });

                        }

                        //сохраняем tag в БД
                        directoryRepo.save (tag);

                    });

                    //обновляем oldNeededTags в БД
                    oldNeededTags.forEach (tag ->directoryRepo.save (tag));

                    System.out.println ("finish work with tag!");
                }

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

            //сохраняем продукт в бд
            return productRepo.save (productFromDb);
        }

        //нужно обработать ответ если вдруг небыло такого продукта
        return null;
    }


    //убираем у oldNeededTags связи с ненужными tagsToDeleteFromProduct тегами
    private void dislinkOldNeededTagsWithDontUsedTagsToDeleteFromProduct (Set<LinkedDirectory> tagsToDeleteFromProduct, Set<LinkedDirectory> oldNeededTags) {
        tagsToDeleteFromProduct.forEach (
                tagToDelete->{

                    //если tagToDelete не имеет привязаных продуктов
                    if(tagToDelete.getProductsCount () == 0){
                        System.out.println ("tagToDelete не имеет привязаных продуктов, можно удалялть связи с oldNeededTags");

                        oldNeededTags.forEach (
                                oldNeededTag ->{

                                    System.out.println ("//удалили tagToDelete с oldNeededTagId");
                                    //удалили tagToDelete с oldNeededTagId
                                    oldNeededTag.getRelatedDirectories ().remove (tagToDelete);
                                    oldNeededTag.getRelatedDirectoryIds ().remove (tagToDelete.getId ());

                                    System.out.println ("//удалили oldNeededTagId с tagToDelete");
                                    //удалили oldNeededTagId с tagToDelete
                                    tagToDelete.getRelatedDirectories ().remove (oldNeededTag);
                                    tagToDelete.getRelatedDirectoryIds ().remove (oldNeededTag.getId ());

                                }

                        );

                    }
                    else {
                        System.out.println ("tagToDelete имеет ("+tagToDelete.getProductsCount ()+") привязаных продуктов");

                        oldNeededTags.forEach (
                                oldNeededTag ->{
                                    //встречаеться ли хоть один раз oldNeededTag в tagToDelete.getProducts.Product.getTags
                                    boolean isOldNeededTagInTagToDeleteProductsTags = false;

                                    for (Product product1: tagToDelete.getProducts ()
                                    ) {
                                        if(product1.getTags ().contains (oldNeededTag)){
                                            isOldNeededTagInTagToDeleteProductsTags = true;

                                            break;
                                        }

                                    }


                                    //встречаеться ли хоть один раз oldNeededTag в tagToDelete.getProducts.Product.getTags
                                    if(!isOldNeededTagInTagToDeleteProductsTags){
                                        System.out.println ("!isOldNeededTagInTagToDeleteProductsTags");
                                        System.out.println ("Нет, не встречаеться oldNeededTag в tagToDelete.getProducts.Product.getTags");

                                        System.out.println ("oldNeededTag = "+oldNeededTag);
                                        System.out.println ("tagToDelete = "+tagToDelete);

                                        System.out.println ("//удалили tagToDelete с oldNeededTagId");
                                        //удалили tagToDelete с oldNeededTagId
                                        oldNeededTag.getRelatedDirectories ().remove (tagToDelete);
                                        oldNeededTag.getRelatedDirectoryIds ().remove (tagToDelete.getId ());

                                        System.out.println ("//удалили oldNeededTagId с tagToDelete");
                                        //удалили oldNeededTagId с tagToDelete
                                        tagToDelete.getRelatedDirectories ().remove (oldNeededTag);
                                        tagToDelete.getRelatedDirectoryIds ().remove (oldNeededTag.getId ());
                                    } else {
                                        System.out.println ("isOldNeededTagInTagToDeleteProductsTags");
                                        System.out.println ("Да встречаеться oldNeededTag в tagToDelete.getProducts.Product.getTags");
                                    }

                                }
                        );


                    }

                }
        );
    }
}
