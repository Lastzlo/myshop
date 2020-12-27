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

    //сохраняем товар с картинками
    public Product saveProductWithFile (Product product, Optional<MultipartFile[]> files) {
        //сюда можно добавить проверку полей продукта

        //добавляем фото к товару
        addPhotosToProduct (product, files);

        //добавляем теги(директории) к товару
        addTegsToProduct (product);

        //устонавливаем время добавления
        product.setCreationDate (LocalDateTime.now ());

        //сохраняем товар в бд
        final Product finalProduct = productRepo.save(product);

        //добавляем товар из бд к тегам(директориям)
        addProductToTags (finalProduct);

        return finalProduct;

    }

    //добавляем фото к товару
    private void addPhotosToProduct (Product product, Optional<MultipartFile[]> files) {
        //сохраняем файлы на сервере
        Set<Photo> photosSet = saveFilesOnServer (product, files);

        //добавляем к продукту все фото
        photosSet.forEach (product::addPhoto);

    }

    //сохраняем файлы на сервере
    private Set<Photo> saveFilesOnServer (Product product, Optional<MultipartFile[]> files){
        //сохраняем файлы на сервере
        Set<Photo> photosSet = new HashSet<> ();

        if(files.isPresent ()){

            final MultipartFile[] multipartFiles = files.get ();

            File uploadDir = new File (uploadPath);

            //если загрузочная директория не найдена то создаеться
            if(!uploadDir.exists ()){
                uploadDir.mkdir ();
            }

            //инициализирует массив для фото
            product.setPhotos (new HashSet<> ());

            //рандомный индентификатор
            String uuidFile = UUID.randomUUID ().toString ();

            //получаем путь (можно и заранье)
            Path rootLocation = Paths.get(String.valueOf(uploadDir));
            //System.out.println("rootLocation = "+rootLocation.toString());

            for (MultipartFile multipartFile: multipartFiles
            ) {
                //дополняем имя рандомный индентификатором чтобы не возникало коллизий в именах файлов
                String resultFilename = uuidFile + "." + multipartFile.getOriginalFilename ();

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

                //сохраняем метаданные в новый обьект
                //сохраняем информацию в БД
                Photo photo = photoRepo.save (new Photo (resultFilename, src));

                photosSet.add (photo);
            }

        }
        return photosSet;
    }

    //добавляем теги(директории) к товару
    private void addTegsToProduct (Product product) {
        //проверяме пришли ли теги с товаром
        if(product.getTags () != null){
            //записываем спикос тегов которые пришли с товаром
            Set<LinkedDirectory> tags = new HashSet<> (){{addAll (product.getTags ());}};
            //очищаем список тегов
            product.getTags ().clear ();
            //проходим все теги которые пришли с товаром
            tags.forEach (tag -> {
                        //ести тег есть в бд
                        this.directoryRepo.findById (tag.getId ()).ifPresent (
                                tagFromDb -> {
                                    //добавляем к тег товару
                                    product.addTag (tagFromDb);
                                }
                        );
                    }
            );
        }
    }

    //добавляем товар из бд к тегам(директориям)
    private void addProductToTags (Product finalProduct) {
        finalProduct.getTags ().forEach (tag -> {
            //добавляем продукт
            tag.addProduct (finalProduct);

            //обновляем количество продуктов связных с тегом
            tag.setProductsCount ((long) tag.getProducts ().size ());

            //проверка что проверям что тип директории PARAMETER или BRAND
            if(
                    tag.getDirectoryType ().equals (DirectoryType.PARAMETER.toString ())
                            || tag.getDirectoryType ().equals (DirectoryType.BRAND.toString ())
            ){
                // связываем директории, для этого в связанные директории
                // добавляем другие теги даного продукта
                linkingDirectories (finalProduct.getTags (), tag);

            }

            //сохраняем тег в БД
            directoryRepo.save (tag);

        });
    }

    // связываем директории, для этого в связанные директории
    // добавляем другие теги даного продукта
    private void linkingDirectories (Set<LinkedDirectory> tagsFromDb, LinkedDirectory tag) {
        // связываем директории, для этого в связанные директории
        // добавляем другие теги даного продукта
        tagsFromDb.forEach (relatedDirectory -> {
            if (
                    tag != relatedDirectory
                            && (
                            relatedDirectory.getDirectoryType ()
                                    .equals (DirectoryType.PARAMETER.toString ())
                                    ||
                                    relatedDirectory.getDirectoryType ()
                                            .equals (DirectoryType.BRAND.toString ()))
            ) {

                //плюс сета в том что не будет повторений
                tag.addRelatedDirectory (relatedDirectory);
                tag.addRelatedDirectoryId (relatedDirectory.getId ());

            }
        });
    }


    public void deleteProduct (Long id) {
        productRepo.findById (id).ifPresent (
                product -> {
                    product.getTags ().forEach (
                            tag -> {
                                //уменьшем количество привязаных товаров к тегу
                                tag.deleteProduct (product);
                                tag.setProductsCount ((long) tag.getProducts ().size ());

                                //если количество привязаных товаров к тегу 0 то убираем связи тега с другими тегами
                                if(tag.getProductsCount () == 0){
//                                    System.out.println ("tag не имеет привязаных продуктов, можно удалять связи с tag.getRelatedDirectories ()");

                                    tag.getRelatedDirectories ().forEach (
                                            relatedDirectory -> {
//                                                System.out.println ("//удалили tag с relatedDirectory");
                                                relatedDirectory.getRelatedDirectories ().remove (tag);
                                                relatedDirectory.getRelatedDirectoryIds ().remove (tag.getId ());

                                                //обновляем relatedDirectory в бд
                                                directoryRepo.save (relatedDirectory);
                                            }
                                    );

                                    //очищаем tag.getRelatedDirectories ()
                                    tag.getRelatedDirectories ().clear ();
                                    tag.getRelatedDirectoryIds ().clear ();

                                } else {
//                                    System.out.println ("tag имеет ("+tag.getProductsCount ()+") привязаных продуктов");

                                    //создаем копию чтобы можно было удалять элементы из сета tag.getRelatedDirectories ()
                                    final Set<LinkedDirectory> tagRelatedDirectoriesCopy = new HashSet<> () {{
                                        addAll (tag.getRelatedDirectories ());
                                    }};

                                    tagRelatedDirectoriesCopy.forEach (
                                            relatedDirectory -> {
                                                //встречаеться ли хоть один раз relatedDirectory в tag.getProducts ().product1.getTags
                                                boolean isRelatedDirectoryInTagProductsTags = false;

                                                for (Product product1: tag.getProducts ()
                                                ) {
                                                    if(product1.getTags ().contains (relatedDirectory)){
                                                        isRelatedDirectoryInTagProductsTags = true;

                                                        break;
                                                    }
                                                }

                                                //встречаеться ли хоть один раз relatedDirectory в tag.getProducts ().product1.getTags
                                                if(!isRelatedDirectoryInTagProductsTags){
//                                                    System.out.println ("!isRelatedDirectoryInTagProductsTags");
//                                                    System.out.println ("Нет, не встречаеться relatedDirectory в tag.getProducts ().product1.getTags");

//                                                    System.out.println ("relatedDirectory = "+relatedDirectory);
//                                                    System.out.println ("tag = "+tag);

//                                                    System.out.println ("//удаляем tag с relatedDirectory");

                                                    //удаляем tagToDelete с oldNeededTagId
                                                    relatedDirectory.getRelatedDirectories ().remove (tag);
                                                    relatedDirectory.getRelatedDirectoryIds ().remove (tag.getId ());

                                                    //обновляем relatedDirectory в бд
                                                    directoryRepo.save (relatedDirectory);

//                                                    System.out.println ("//удаляем relatedDirectory с tag");
                                                    //удаляем oldNeededTagId с tagToDelete
                                                    tag.getRelatedDirectories ().remove (relatedDirectory);
                                                    tag.getRelatedDirectoryIds ().remove (relatedDirectory.getId ());
                                                } else {
//                                                    System.out.println ("isRelatedDirectoryInTagProductsTags");
//                                                    System.out.println ("Да встречаеться relatedDirectory в tag.getProducts ().product1.getTags");
                                                }

                                            }
                                    );

                                }

                                //обновляем tag в бд
                                directoryRepo.save (tag);
                            }
                    );
                }
        );

        //удаление с БД
        productRepo.deleteById(id);
    }

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

                //проверка что oldtagsFromDb и recivedTags равны
                if(oldtagsFromDb.equals (recivedTags)){
                    //если нет отличий то пропускаем
                    System.out.println ("oldtagsFromDb.equals (recivedTags)");
                }else {
                    //находим теги которых нет в recivedTags
                    //эти теги потом надо будет убрать из продукта
                    final Set<LinkedDirectory> tagsToDeleteFromProduct = Sets.difference(oldtagsFromDb, recivedTags);

                    System.out.println ("tagsToDeleteFromProduct:");

                    tagsToDeleteFromProduct.forEach (
                            tag-> {

                                System.out.println ("tagId = "+tag.getId ()+" tagName = "+tag.getName ());

                                //удаляем с товара лишние теги
                                productFromDb.deleteTag (tag);

                                //уменьшем количество привязаных товаров к тегу
                                tag.deleteProduct (productFromDb);
                                tag.setProductsCount ((long) tag.getProducts ().size ());

                            }
                    );

                    //список актуальных, нужных тегов
                    Set<LinkedDirectory> oldNeededTags = Sets.difference(oldtagsFromDb, tagsToDeleteFromProduct);

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
                            // связываем директории, для этого в связанные директории
                            // добавляем другие теги даного продукта
                            linkingDirectories (newtags, tag);

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
                    try {
                        try (InputStream inputStream = multipartFile.getInputStream()) {
                            Files.copy(inputStream, rootLocation.resolve(resultFilename),
                                    StandardCopyOption.REPLACE_EXISTING);
                        }
                    }
                    catch (IOException e) {
                        System.out.println ("Failed to store file ");
                    }

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
//                        System.out.println ("tagToDelete не имеет привязаных продуктов, можно удалялть связи с oldNeededTags");

                        oldNeededTags.forEach (
                                oldNeededTag ->{

//                                    System.out.println ("//удалили tagToDelete с oldNeededTagId");
                                    oldNeededTag.getRelatedDirectories ().remove (tagToDelete);
                                    oldNeededTag.getRelatedDirectoryIds ().remove (tagToDelete.getId ());

//                                    System.out.println ("//удалили oldNeededTagId с tagToDelete");
                                    tagToDelete.getRelatedDirectories ().remove (oldNeededTag);
                                    tagToDelete.getRelatedDirectoryIds ().remove (oldNeededTag.getId ());

                                }

                        );

                    }
                    else {
//                        System.out.println ("tagToDelete имеет ("+tagToDelete.getProductsCount ()+") привязаных продуктов");

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
//                                        System.out.println ("!isOldNeededTagInTagToDeleteProductsTags");
//                                        System.out.println ("Нет, не встречаеться oldNeededTag в tagToDelete.getProducts.Product.getTags");

//                                        System.out.println ("oldNeededTag = "+oldNeededTag);
//                                        System.out.println ("tagToDelete = "+tagToDelete);

//                                        System.out.println ("//удалили tagToDelete с oldNeededTagId");
                                        oldNeededTag.getRelatedDirectories ().remove (tagToDelete);
                                        oldNeededTag.getRelatedDirectoryIds ().remove (tagToDelete.getId ());

//                                        System.out.println ("//удалили oldNeededTagId с tagToDelete");
                                        tagToDelete.getRelatedDirectories ().remove (oldNeededTag);
                                        tagToDelete.getRelatedDirectoryIds ().remove (oldNeededTag.getId ());
                                    } else {
//                                        System.out.println ("isOldNeededTagInTagToDeleteProductsTags");
//                                        System.out.println ("Да встречаеться oldNeededTag в tagToDelete.getProducts.Product.getTags");
                                    }

                                }
                        );


                    }

                }
        );
    }

}
