package com.example.myshop.services;

import com.example.myshop.domain.LinkedDirectory;
import com.example.myshop.domain.Product;
import com.example.myshop.repos.LinkedDirectoryRepo;
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
public class GeneralServiceImpl implements  GeneralService{
    @Autowired
    private LinkedDirectoryRepo directoryRepo;
    @Autowired
    private ProductRepo productRepo;

    @Value("${upload.path}")
    private String uploadPath;

    @Override
    public List<Product> getAllProducts () {
        return productRepo.findAll();
    }

    @Override
    public Product getProduct (Long id) {
        return productRepo.getOne(id);
    }

    @Override
    public Product saveProduct (Product product) {
        product.setCreationDate (LocalDateTime.now ());

        Set<LinkedDirectory> tags = new HashSet<> (){{addAll (product.getTags ());}};
        product.getTags ().clear ();
        tags.forEach (tag -> {
                    this.directoryRepo.findById (tag.getId ()).ifPresent (
                            tagFromDb -> product.addTag (tagFromDb)
                    );
                }
        );

        return productRepo.save(product);
    }

    @Override
    public Product saveProductWithFile (Product product, Optional<MultipartFile[]> files) {

        if(files.isPresent ()){

            final MultipartFile[] multipartFiles = files.get ();

            File uploadDir = new File (uploadPath);

            if(!uploadDir.exists ()){
                uploadDir.mkdir ();
            }

//            String uuidFile = UUID.randomUUID ().toString ();
//            String resultFilename = uuidFile + "." + multipartFile.getOriginalFilename ();

            product.setPhotos (new HashSet<> ());

            for (MultipartFile multipartFile: multipartFiles
            ) {
                String resultFilename = multipartFile.getOriginalFilename ();

                try {
                    multipartFile.transferTo (new File (uploadPath + "/" + resultFilename));
                } catch (IOException e) {
                    System.out.println ("файл не переместили в папку ");
                    //e.printStackTrace ();
                }

                product.addPhoto(resultFilename);
            }

        }

        return saveProduct (product);
    }

    @Override
    public void deleteProduct (Long id) {
        productRepo.deleteById(id);
    }

    @Override
    public Product updateProduct (Product product) {

        Product productFromDb = productRepo.findById (product.getId ()).get ();

        if(productFromDb!= null){
            BeanUtils.copyProperties (product, productFromDb, "id", "tags", "photos", "creationDate");
            productFromDb.setTags (product.getTags ());
            return productRepo.save (productFromDb);
        }

        return product;
    }
}
