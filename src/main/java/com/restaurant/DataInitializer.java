package com.restaurant;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DataInitializer {

@Bean
CommandLineRunner loadDefaultMenu(RestaurantRepository repository) {
    return args -> {
        if (repository.count() == 0) {
            addItem(repository, "Chicken Biryani", "Main Course", 220.0);
            addItem(repository, "Veg Biryani", "Main Course", 160.0);
            addItem(repository, "Paneer Butter Masala", "Main Course", 190.0);
            addItem(repository, "Masala Dosa", "Breakfast", 80.0);
            addItem(repository, "Idli (2 pieces)", "Breakfast", 40.0);
            addItem(repository, "Veg Fried Rice", "Chinese", 140.0);
            addItem(repository, "Chicken Fried Rice", "Chinese", 180.0);
            addItem(repository, "Fresh Lime Soda", "Beverages", 50.0);
            addItem(repository, "Gulab Jamun", "Dessert", 40.0);

            System.out.println("Default food menu inserted successfully!");
        }
    };
}

private void addItem(
        RestaurantRepository repository,
        String name,
        String category,
        double price) {

    Restaurant item = new Restaurant();
    item.setName(name);
    item.setCategory(category);
    item.setPrice(price);
    repository.save(item);
}

}