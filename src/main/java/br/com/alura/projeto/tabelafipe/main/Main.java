package br.com.alura.projeto.tabelafipe.main;

import br.com.alura.projeto.tabelafipe.models.Data;
import br.com.alura.projeto.tabelafipe.models.Models;
import br.com.alura.projeto.tabelafipe.models.Vehicle;
import br.com.alura.projeto.tabelafipe.services.ApiConsumer;
import br.com.alura.projeto.tabelafipe.services.DataConverter;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Scanner;

public class Main {
    private final Scanner sc = new Scanner(System.in);
    private final String URL_BASE = "https://parallelum.com.br/fipe/api/v1/";
    private final ApiConsumer consumer = new ApiConsumer();
    private final DataConverter dataConverter = new DataConverter();

    public void showMenu() {
        var menu ="""
            *** OPTIONS ***
            Select the type of Vehicle:
                - Car
                - Motorcycle
                - Truck
            ***************************
            Write your choice below:
        """;

        System.out.println(menu);

        var option = sc.nextLine();
        String address = "";

        if (option.toLowerCase().contains("car")) {
            address = URL_BASE + "carros/marcas";
        }
        if (option.toLowerCase().contains("mot")) {
            address = URL_BASE + "motos/marcas";
        }
        if (option.toLowerCase().contains("tru")) {
            address = URL_BASE + "caminhoes/marcas";
        }

        var json = consumer.getData(address);
        System.out.println(json);

        var brands = dataConverter.getList(json, Data.class);
        brands.stream()
                .sorted(Comparator.comparing(Data::codigo))
                .forEach(System.out::println);
        System.out.println("Inform the code of the brand to search:");
        var brandCode = sc.nextLine();
        address = address + "/" + brandCode + "/modelos";
        json = consumer.getData(address);

        var modelsList = dataConverter.getData(json, Models.class);
        System.out.println("Available models:");
        modelsList.modelos().stream()
                .sorted(Comparator.comparing(Data::codigo))
                .forEach(System.out::println);

        System.out.println("\n Search for the name of the car you are looking for:");
        var vehicleName = sc.nextLine();

        List<Data> filteredModels = modelsList.modelos().stream()
                .filter(m -> m.nome().toLowerCase().contains(vehicleName.toLowerCase()))
                .toList();
        System.out.println("Filtered models:");
        filteredModels.forEach(System.out::println);

        System.out.println("\n Inform the code of the model you are looking for:");
        var modelCode = sc.nextLine();

        address = address + "/" + modelCode + "/anos";
        json = consumer.getData(address);

        List<Data> years = dataConverter.getList(json, Data.class);
        List<Vehicle> vehicles = new ArrayList<>();

        for (int i = 0; i < years.size(); i++) {
            var addressYears = address + "/" + years.get(i).codigo();
            json = consumer.getData(addressYears);
            Vehicle vehicle = dataConverter.getData(json, Vehicle.class);
            vehicles.add(vehicle);
        }
        System.out.println("All vehicles found:");
        vehicles.forEach(System.out::println);
    }
}
