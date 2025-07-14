package finki.db.tasty_tabs.service.impl;

import finki.db.tasty_tabs.entity.RestaurantTable;
import finki.db.tasty_tabs.entity.exceptions.TableNumberAlreadyExistsException;
import finki.db.tasty_tabs.repository.RestaurantTableRepository;
import finki.db.tasty_tabs.service.RestaurantTableService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class RestaurantTableServiceImpl implements RestaurantTableService {

    private final RestaurantTableRepository tableRepository;

    public RestaurantTableServiceImpl(RestaurantTableRepository tableRepository) {
        this.tableRepository = tableRepository;
    }

    @Override
    public Optional<RestaurantTable> findById(Integer id) {
        return tableRepository.findById(id);
    }

    @Override
    public List<RestaurantTable> getAll() {
        return tableRepository.findAll();
    }

    @Override
    public Optional<RestaurantTable> updateTable(Integer id, RestaurantTable restaurantTable) {
        if(tableRepository.findById(restaurantTable.getTableNumber()).isPresent() && id!=restaurantTable.getTableNumber()){
            throw new TableNumberAlreadyExistsException(restaurantTable.getTableNumber());
        }
        return tableRepository.findById(id).map(existingTable -> {
            if (restaurantTable.getTableNumber() != null) {
                existingTable.setTableNumber(restaurantTable.getTableNumber());
            }
            if (restaurantTable.getSeatCapacity() != null) {
                existingTable.setSeatCapacity(restaurantTable.getSeatCapacity());
            }
            return tableRepository.save(existingTable);
        });
    }

    @Override
    public void deleteTable(Integer id) {
        tableRepository.deleteById(id);
    }


    @Override
    public List<RestaurantTable> getAllBySeatCapacity(Integer seatCapacity) {
        return tableRepository.findAllBySeatCapacity(seatCapacity);
    }

    @Override
    public Optional<RestaurantTable> createTable(RestaurantTable restaurantTable) {
        if(tableRepository.findById(restaurantTable.getTableNumber()).isPresent()){
            throw new TableNumberAlreadyExistsException(restaurantTable.getTableNumber());
        }
        return Optional.of(tableRepository.save(restaurantTable));
    }
}
