CREATE TABLE IF NOT EXISTS users (
                                     id BIGSERIAL PRIMARY KEY,
                                     email VARCHAR(255) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    phone_number VARCHAR(50) UNIQUE,
    street VARCHAR(255),
    city VARCHAR(255)
    );

CREATE TABLE IF NOT EXISTS employees (
                                         user_id BIGSERIAL PRIMARY KEY,
                                         net_salary DECIMAL(10,2),
    gross_salary DECIMAL(10,2),
    FOREIGN KEY (user_id) REFERENCES users (id) ON DELETE CASCADE ON UPDATE CASCADE
    );

CREATE TABLE IF NOT EXISTS customers (
                                         user_id BIGSERIAL PRIMARY KEY,
                                         FOREIGN KEY (user_id) REFERENCES users (id) ON DELETE CASCADE ON UPDATE CASCADE
    );

CREATE TABLE IF NOT EXISTS staff_roles (
                                           id BIGSERIAL PRIMARY KEY,
                                           name VARCHAR(255) NOT NULL
    );

CREATE TABLE IF NOT EXISTS front_staff (
                                           employee_id BIGSERIAL PRIMARY KEY,
                                           tip_percent DECIMAL(10,2),
    staff_role_id BIGINT NOT NULL,
    FOREIGN KEY (employee_id) REFERENCES employees (user_id) ON DELETE CASCADE ON UPDATE CASCADE,
    FOREIGN KEY (staff_role_id) REFERENCES staff_roles (id) ON DELETE CASCADE ON UPDATE CASCADE
    );

CREATE TABLE IF NOT EXISTS back_staff (
                                          employee_id BIGSERIAL PRIMARY KEY,
                                          staff_role_id BIGINT NOT NULL,
                                          FOREIGN KEY (employee_id) REFERENCES employees (user_id) ON DELETE CASCADE ON UPDATE CASCADE,
    FOREIGN KEY (staff_role_id) REFERENCES staff_roles (id) ON DELETE CASCADE ON UPDATE CASCADE
    );

CREATE TABLE IF NOT EXISTS managers (
                                        employee_id BIGSERIAL PRIMARY KEY,
                                        FOREIGN KEY (employee_id) REFERENCES employees (user_id) ON DELETE CASCADE ON UPDATE CASCADE
    );

CREATE TABLE IF NOT EXISTS shifts (
                                      id BIGSERIAL PRIMARY KEY,
                                      date DATE NOT NULL,
                                      start_time TIME NOT NULL,
                                      end_time TIME NOT NULL,
                                      manager_id BIGINT NOT NULL,
                                      FOREIGN KEY (manager_id) REFERENCES managers (employee_id) ON DELETE CASCADE ON UPDATE CASCADE
    );

CREATE TABLE IF NOT EXISTS assignments (
                                           id BIGSERIAL PRIMARY KEY,
                                           clock_in_time TIME,
                                           clock_out_time TIME,
                                           manager_id BIGINT NOT NULL,
                                           employee_id BIGINT NOT NULL,
                                           shift_id BIGINT NOT NULL,
                                           FOREIGN KEY (manager_id) REFERENCES managers (employee_id) ON DELETE CASCADE ON UPDATE CASCADE,
    FOREIGN KEY (employee_id) REFERENCES employees (user_id) ON DELETE CASCADE ON UPDATE CASCADE,
    FOREIGN KEY (shift_id) REFERENCES shifts (id) ON DELETE CASCADE ON UPDATE CASCADE
    );

CREATE TABLE IF NOT EXISTS tables (
                                      table_number BIGSERIAL PRIMARY KEY,
                                      seat_capacity INT NOT NULL
);

CREATE TABLE IF NOT EXISTS reservations (
                                            id BIGSERIAL PRIMARY KEY,
                                            user_id BIGINT NOT NULL,
                                            creation_timestamp TIMESTAMP NOT NULL,
                                            datetime TIMESTAMP NOT NULL,
                                            number_of_people BIGINT NOT NULL,
                                            stay_length DECIMAL(10,2) NULL, --hours
    FOREIGN KEY (user_id) REFERENCES users (id) ON DELETE CASCADE ON UPDATE CASCADE
    );

CREATE TABLE IF NOT EXISTS frontstaff_managed_reservations (
                                                               id BIGSERIAL PRIMARY KEY,
                                                               reservation_id BIGINT NOT NULL,
                                                               front_staff_id BIGINT NOT NULL,
                                                               table_number BIGINT NOT NULL,
                                                               FOREIGN KEY (reservation_id) REFERENCES reservations (id) ON DELETE CASCADE ON UPDATE CASCADE,
    FOREIGN KEY (front_staff_id) REFERENCES front_staff (employee_id) ON DELETE CASCADE ON UPDATE CASCADE,
    FOREIGN KEY (table_number) REFERENCES tables (table_number) ON DELETE CASCADE ON UPDATE CASCADE
    );

CREATE TABLE IF NOT EXISTS categories (
                                          id BIGSERIAL PRIMARY KEY,
                                          name VARCHAR(255) NOT NULL,
    is_available BOOLEAN NOT NULL DEFAULT TRUE
    );

CREATE TABLE IF NOT EXISTS products (
                                        id BIGSERIAL PRIMARY KEY,
                                        name VARCHAR(255) NOT NULL,
    description VARCHAR(1000) NOT NULL,
    price DECIMAL(10,2) NOT NULL,
    category_id BIGINT NOT NULL,
    manage_inventory BOOLEAN NOT NULL DEFAULT FALSE,
    tax_class VARCHAR(4) NOT NULL,
    FOREIGN KEY (category_id) REFERENCES categories (id) ON DELETE CASCADE ON UPDATE CASCADE
    );

CREATE TABLE IF NOT EXISTS inventories (
                                           product_id BIGSERIAL PRIMARY KEY,
                                           quantity INT NOT NULL,
                                           restock_level  INT NULL,
                                           FOREIGN KEY (product_id) REFERENCES products (id) ON DELETE CASCADE ON UPDATE CASCADE
    );

CREATE TABLE IF NOT EXISTS orders (
                                      id BIGSERIAL PRIMARY KEY,
                                      status VARCHAR(255) NOT NULL DEFAULT 'PENDING',
    datetime TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
    );

CREATE TABLE IF NOT EXISTS order_items (
                                           id BIGSERIAL PRIMARY KEY,
                                           order_id BIGINT NOT NULL,
                                           product_id BIGINT NOT NULL,
                                           is_processed BOOLEAN NOT NULL DEFAULT FALSE,
                                           quantity INT NOT NULL,
                                           price DECIMAL(10,2) NOT NULL,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (order_id) REFERENCES orders (id) ON DELETE CASCADE ON UPDATE CASCADE,
    FOREIGN KEY (product_id) REFERENCES products (id) ON DELETE CASCADE ON UPDATE CASCADE
    );

CREATE TABLE IF NOT EXISTS tab_orders (
                                          order_id BIGSERIAL PRIMARY KEY,
                                          front_staff_id BIGINT NOT NULL,
                                          table_number BIGINT NOT NULL,
                                          FOREIGN KEY (order_id) REFERENCES orders (id) ON DELETE CASCADE ON UPDATE CASCADE,
    FOREIGN KEY (table_number) REFERENCES tables (table_number) ON DELETE CASCADE ON UPDATE CASCADE,
    FOREIGN KEY (front_staff_id) REFERENCES front_staff (employee_id) ON DELETE CASCADE ON UPDATE CASCADE
    );

CREATE TABLE IF NOT EXISTS online_orders (
                                             order_id BIGSERIAL PRIMARY KEY,
                                             delivery_address VARCHAR(255) NOT NULL,
    customer_id BIGINT NOT NULL,
    FOREIGN KEY (customer_id) REFERENCES customers (user_id) ON DELETE CASCADE ON UPDATE CASCADE,
    FOREIGN KEY (order_id) REFERENCES orders (id) ON DELETE CASCADE ON UPDATE CASCADE
    );

CREATE TABLE IF NOT EXISTS payments (
                                        id BIGSERIAL PRIMARY KEY,
                                        order_id BIGINT NOT NULL,
                                        amount DECIMAL(10,2) NOT NULL,
    payment_type VARCHAR(32) NOT NULL,
    tip_amount DECIMAL(10,2) NOT NULL DEFAULT 0,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (order_id) REFERENCES orders (id) ON DELETE CASCADE ON UPDATE CASCADE
    );

-- DML
INSERT INTO users(id, email, password, phone_number, street, city)
VALUES
    (1, 'test@hotmail.com', 'password1', '070003005', 'Mladinska 3', 'Strumica'),
    (2, 'test2@hotmail.com', 'password2', '070001002', 'Marsal Tito 10', 'Strumica'),
    (3, 'test3@hotmail.com', 'password1', '070003003', 'Mladinska 5', 'Strumica'),
    (4, 'test4@hotmail.com', 'password2', '070004004', 'Marsal Tito 11', 'Strumica'),
    (5, 'test5@hotmail.com', 'password1', '070005005', 'Mladinska 12', 'Strumica');

INSERT INTO employees(user_id, net_salary, gross_salary)
VALUES
    (1, 30000, 40000),
    (3, 50000, 62000),
    (4, 35000, 46000),
    (5, 28000, 37000);

INSERT INTO managers(employee_id)
VALUES
    (3);

INSERT INTO staff_roles(id, name)
VALUES
    (1, 'Server'),
    (2, 'Chef'),
    (3, 'Bartender'),
    (4, 'Hostess');

INSERT INTO front_staff(employee_id, tip_percent, staff_role_id)
VALUES
    (1, .4, 1),
    (5, 0.1, 4);

INSERT INTO back_staff(employee_id, staff_role_id)
VALUES
    (4, 2);

INSERT INTO customers(user_id)
VALUES
    (2);

INSERT INTO shifts (id, date, start_time, end_time, manager_id)
VALUES
    (1, current_date, '09:00:00', '17:00:00', 3);

INSERT INTO assignments(id, clock_in_time, clock_out_time, manager_id, employee_id, shift_id)
VALUES
    (1, NULL, NULL, 3, 1, 1);

INSERT INTO tables(table_number, seat_capacity)
VALUES
    (1, 4),
    (2, 8);

INSERT INTO  reservations(id, user_id, creation_timestamp, datetime, stay_length, number_of_people)
VALUES
    (1, 2, now(), now(), NULL, 4);

INSERT INTO frontstaff_managed_reservations(id, reservation_id, front_staff_id, table_number)
VALUES
    (1, 1, 5, 1);

INSERT INTO categories(id, name)
VALUES
    (1, 'Drinks'),
    (2, 'Appetizers'),
    (3, 'Entrees');

INSERT INTO products(id, name, description, price, category_id, manage_inventory, tax_class)
VALUES
    (1, 'Coca Cola', 'A classic carbonated soft drink.', 100, 1, TRUE, 'A'),
    (2, 'Pomfrit so sirenje', 'Crispy french fries topped with melted cheese.', 250, 2, FALSE, 'A');

INSERT INTO inventories(product_id, quantity)
VALUES
    (1, 100);

INSERT INTO orders(id, status, datetime)
VALUES
    (1, 'PENDING', '2025-01-05 10:00:00'),
    (2, 'ACCEPTED', '2025-01-05 10:00:00'),
    (3, 'CONFIRMED', '2025-01-05 11:00:00');

INSERT INTO order_items (id, order_id, product_id, is_processed, quantity, price)
SELECT 1, 1, 1, TRUE, 3, price FROM products WHERE id = 1
UNION ALL
SELECT 2, 1, 2, FALSE, 1, price FROM products WHERE id = 2
UNION ALL
SELECT 3, 3, 2, FALSE, 2, price FROM products WHERE id = 2
UNION ALL
SELECT 4, 3, 1, FALSE, 2, price FROM products WHERE id = 1;

INSERT INTO tab_orders(order_id, front_staff_id, table_number)
VALUES
    (1, 1, 1);

INSERT INTO online_orders(order_id, delivery_address, customer_id)
VALUES
    (3, 'Leninova 5', 2);

INSERT INTO payments(id, order_id, amount, payment_type, tip_amount)
VALUES
    (1, 3, 700, 'cash', 10);