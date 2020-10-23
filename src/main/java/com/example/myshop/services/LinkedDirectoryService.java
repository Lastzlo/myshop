package com.example.myshop.services;

import com.example.myshop.domain.DirectoryType;
import com.example.myshop.domain.LinkedDirectory;
import com.example.myshop.domain.Product;
import com.example.myshop.repos.LinkedDirectoryRepo;
import com.example.myshop.repos.ProductRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

@Service
public class LinkedDirectoryService {

    @Autowired
    private ProductRepo productRepo;
    @Autowired
    private LinkedDirectoryRepo directoryRepo;

    public LinkedDirectory getCore () {
        LinkedDirectory directory = directoryRepo.findByDirectoryType (DirectoryType.CATEGORY_LIST.toString ());
        if (directory==null){
            directory = new LinkedDirectory ("Категории", DirectoryType.CATEGORY_LIST.toString ());
            directory = directoryRepo.save (directory);
        }
        return directory;
    }

    public Set<Product> getProductsByLinkedDirectoryId(String id){
        Optional<LinkedDirectory> optionalLinkedDirectory = directoryRepo.findById (Long.valueOf (id));
        if (optionalLinkedDirectory.isPresent ()){
            return optionalLinkedDirectory.get ().getProducts ();
        } else return new HashSet<> ();

    }

    public LinkedDirectory getOne (String id) {
        return directoryRepo.getOne(Long.valueOf(id));
    }

    public LinkedDirectory create(
            LinkedDirectory linkedDirectory
    ){
        LinkedDirectory father = directoryRepo.getOne (linkedDirectory.getFather ().getId ());

        //устонавливаем контейнер для связанных дерикторий
        linkedDirectory.setRelatedDirectories (new HashSet<> ());

        LinkedDirectory child = linkedDirectory;

        if(father!=null){
            String fatherDirectoryType = father.getDirectoryType ();

            child.setFather (father);

            //проверка что отец CATEGORY_LIST
            if(fatherDirectoryType.equals (DirectoryType.CATEGORY_LIST.toString ())){
                child.setDirectoryType (DirectoryType.CATEGORY.toString ());
                child = directoryRepo.save (child);

                //добавляем в него дочернюю директорию Бренды
                LinkedDirectory brandList = new LinkedDirectory (
                        "Бренды",
                        DirectoryType.BRAND_LIST.toString ()
                );
                brandList.setFather (child);
                brandList = directoryRepo.save (brandList);

                //добавляем в него дочернюю директорию Параметры
                LinkedDirectory parameterList = new LinkedDirectory (
                        "Параметры",
                        DirectoryType.PARAMETER_LIST.toString ()
                );
                parameterList.setFather (child);
                parameterList = directoryRepo.save (parameterList);

                child.addChild (brandList);
                child.addChild (parameterList);
            }

            //проверка что отец BRAND_LIST
            if(fatherDirectoryType.equals (DirectoryType.BRAND_LIST.toString ())){

                child.setDirectoryType (DirectoryType.BRAND.toString ());
            }

            //проверка что отец PARAMETER_LIST
            if(fatherDirectoryType.equals (DirectoryType.PARAMETER_LIST.toString ())){

                child.setDirectoryType (DirectoryType.PARAMETER.toString ());
            }

            child = directoryRepo.save (child);

            father.addChild (child);
            directoryRepo.save (father);

            return child;
        } else {
            return directoryRepo.save (linkedDirectory);
        }
    }


    public LinkedDirectory update(
            String id,
            LinkedDirectory directory
    ){
        if(directoryRepo.findById(Long.valueOf(id)).isPresent ()){
            LinkedDirectory directoryFromDb = directoryRepo.findById(Long.valueOf(id)).get();

            directoryFromDb.setName (directory.getName ());
            return directoryRepo.save (directoryFromDb);
        }
        return directory;
    }

    public void delete(String id){
        directoryRepo.findById (Long.valueOf (id)).ifPresent (
                linkedDirectory -> {
                    //удаление тега из всех товаров
                    linkedDirectory.getProducts ().forEach (
                            product -> {
                                product.deleteTag (linkedDirectory);
                                productRepo.save (product);
                            }
                    );

                    //удаление связей с другими тегами
                    linkedDirectory.getRelatedDirectories ().forEach (
                            reletaedDirectory -> {
                                //удалили reletaedDirectory с linkedDirectory
                                reletaedDirectory.getRelatedDirectories ().remove (linkedDirectory);
                                reletaedDirectory.getRelatedDirectoryIds ().remove (linkedDirectory.getId ());

                                directoryRepo.save (reletaedDirectory);
                            }
                    );
                    linkedDirectory.getRelatedDirectories ().clear ();
                    linkedDirectory.getRelatedDirectoryIds ().clear ();

                    //удаление у родителя
                    if(linkedDirectory.getFather ()!=null){
                        LinkedDirectory father = linkedDirectory.getFather ();

                        father.deleteChild (linkedDirectory);
                        directoryRepo.save (father);
                    }

                    //удаление всех детей
                    if(linkedDirectory.getChildren ().size () != 0){
                        Set<LinkedDirectory> children = linkedDirectory.getChildren ();

                        children.forEach (item-> item.setFather (null));
                        linkedDirectory.getChildren ().clear ();

                        children.forEach (item-> {
                            //удаление тега из всех товаров
                            item.getProducts ().forEach (
                                    product -> {
                                        product.deleteTag (item);
                                        productRepo.save (product);
                                    }
                            );

                            directoryRepo.delete (item);
                        });
                    }

                    directoryRepo.delete (linkedDirectory);
                }
        );
    }
}
