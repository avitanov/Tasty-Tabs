package finki.db.tasty_tabs.service.impl;

import finki.db.tasty_tabs.entity.RestaurantTable;
import finki.db.tasty_tabs.entity.exceptions.TableNotFoundException;
import finki.db.tasty_tabs.entity.exceptions.TableNumberAlreadyExistsException;
import finki.db.tasty_tabs.repository.RestaurantTableRepository;
import finki.db.tasty_tabs.service.RestaurantTableService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RestaurantTableServiceImpl implements RestaurantTableService {

    private final RestaurantTableRepository tableRepository;

    public RestaurantTableServiceImpl(RestaurantTableRepository tableRepository) {
        this.tableRepository = tableRepository;
    }

    @Override
    public RestaurantTable findById(Long id) {
        return tableRepository.findById(id).orElseThrow(()->new TableNotFoundException(id));
    }

    @Override
    public List<RestaurantTable> getAll() {
        return tableRepository.findAll();
    }

    @Override
    public RestaurantTable updateTable(Long id, RestaurantTable restaurantTable) {
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
        }).orElseThrow(()->new TableNotFoundException(id));
    }

    @Override
    public void deleteTable(Long id) {
        tableRepository.deleteById(id);
    }


    @Override
    public List<RestaurantTable> getAllBySeatCapacity(Integer seatCapacity) {
        return tableRepository.findAllBySeatCapacity(seatCapacity);
    }

    @Override
    public RestaurantTable createTable(RestaurantTable restaurantTable) {
        if(tableRepository.findById(restaurantTable.getTableNumber()).isPresent()){
            throw new TableNumberAlreadyExistsException(restaurantTable.getTableNumber());
        }
        return tableRepository.save(restaurantTable);
    }
}
