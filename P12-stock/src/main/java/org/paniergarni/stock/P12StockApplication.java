package org.paniergarni.stock;

import net.bytebuddy.utility.RandomString;
import org.modelmapper.ModelMapper;
import org.paniergarni.stock.dao.CategoryRepository;
import org.paniergarni.stock.dao.FarmerRepository;
import org.paniergarni.stock.dao.ProductRepository;
import org.paniergarni.stock.entities.Category;
import org.paniergarni.stock.entities.Farmer;
import org.paniergarni.stock.entities.Measure;
import org.paniergarni.stock.entities.Product;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.Bean;

import java.util.List;
import java.util.Random;

@SpringBootApplication
@EnableDiscoveryClient
public class P12StockApplication implements CommandLineRunner {

    public static void main(String[] args) {
        SpringApplication.run(P12StockApplication.class, args);
    }

    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private CategoryRepository categoryRepository;
    @Autowired
    private FarmerRepository farmerRepository;

    @Bean
    public ModelMapper modelMapper() {
        return new ModelMapper();
    }


    @Override
    public void run(String... args) throws Exception {

        Category category = new Category();
        category.setName("Ordinateur");
        Category category2 = new Category();
        category2.setName("Imprimante");
        Category category3 = new Category();
        category3.setName("Téléphone");

        categoryRepository.save(category);
        categoryRepository.save(category2);
        categoryRepository.save(category3);

        Farmer farmer = new Farmer();
        farmer.setName("Farmer1");
        Farmer farmer2 = new Farmer();
        farmer2.setName("Farmer2");
        Farmer farmer3 = new Farmer();
        farmer3.setName("Farmer3");

        farmerRepository.save(farmer);
        farmerRepository.save(farmer2);
        farmerRepository.save(farmer3);

        List<Farmer> farmers = farmerRepository.findAll();
        List<Measure> measures = Measure.getListMeasure();
        Random rm = new Random();

        categoryRepository.findAll().forEach(c -> {
            for (int i = 0; i < 10; i++) {
                Product p = new Product();
                p.setName(RandomString.make(18));
                p.setPrice(100 + rm.nextInt(1000));
                p.setCapacity(100 + rm.nextDouble());
                p.setQuantity(rm.nextInt(50));
                p.setAvailable(rm.nextBoolean());
                p.setPromotion(rm.nextBoolean());
                if (p.isPromotion())
                    p.setOldPrice(100 + rm.nextInt(1000));
               // p.setPhoto("angular.png");
                p.setCategory(c);
                p.setFarmer(farmers.get(rm.nextInt(farmers.size())));
                p.setMeasure(measures.get(rm.nextInt(measures.size())));
                productRepository.save(p);
            }
        });

    }
}
