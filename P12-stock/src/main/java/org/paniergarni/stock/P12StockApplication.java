package org.paniergarni.stock;

import org.modelmapper.ModelMapper;
import org.paniergarni.stock.dao.CategoryRepository;
import org.paniergarni.stock.dao.FarmerRepository;
import org.paniergarni.stock.dao.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.Bean;

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
    public void run(String... args) throws Exception { }
}
