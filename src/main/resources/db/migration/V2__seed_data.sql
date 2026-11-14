-- Reference data. Carried over unchanged from the original data.sql
-- (known data errors are tracked in REFACTORING_ROADMAP.md, task 4).

INSERT INTO roles (name) VALUES ('ROLE_USER');
INSERT INTO roles (name) VALUES ('ROLE_ADMIN');

INSERT INTO city (city_name) VALUES ('Bucuresti');
INSERT INTO city (city_name) VALUES ('London');
INSERT INTO city (city_name) VALUES ('Paris');
INSERT INTO city (city_name) VALUES ('Timisoara');

INSERT INTO airport (airport_name, airport_iata_code, city_id) VALUES ('Henri Coanda International Airport', 'OTP', 1);
INSERT INTO airport (airport_name, airport_iata_code, city_id) VALUES ('London Heathrow Airport', 'LHR', 2);
INSERT INTO airport (airport_name, airport_iata_code, city_id) VALUES ('Charles de Gaulle International Airport', 'CHD', 3);
INSERT INTO airport (airport_name, airport_iata_code, city_id) VALUES ('Timisoara Traian Vuia Airport', 'TVT', 4);

INSERT INTO airline (name, iso) VALUES ('Tarom', 'OTO');
INSERT INTO airline (name, iso) VALUES ('British Airways', 'OTO');
INSERT INTO airline (name, iso) VALUES ('Air France', 'OTO');
INSERT INTO airline (name, iso) VALUES ('Wizz Air', 'OTO');

INSERT INTO airplane (manufacturer, model, number_of_seats, speed, airline_id) VALUES ('Boeing', 'B373', 160, 800, 1);
INSERT INTO airplane (manufacturer, model, number_of_seats, speed, airline_id) VALUES ('Boeing', 'B737', 180, 700, 2);
INSERT INTO airplane (manufacturer, model, number_of_seats, speed, airline_id) VALUES ('Boeing', 'B747', 200, 900, 3);
INSERT INTO airplane (manufacturer, model, number_of_seats, speed, airline_id) VALUES ('Boeing', 'B777', 190, 750, 4);
INSERT INTO airplane (manufacturer, model, number_of_seats, speed, airline_id) VALUES ('Airbus', 'A320', 150, 800, 5);
INSERT INTO airplane (manufacturer, model, number_of_seats, speed, airline_id) VALUES ('Airbus', 'A300', 160, 800, 6);
INSERT INTO airplane (manufacturer, model, number_of_seats, speed, airline_id) VALUES ('Airbus', 'A340', 180, 900, 7);

INSERT INTO flight (airline_id, arrival_airport_id, arrival_time, departure_date, departure_airport_id, departure_time, price)
VALUES (1, 1, '06:30:00', '2020-11-01', 2, '09:30:00', 35);
INSERT INTO flight (airline_id, arrival_airport_id, arrival_time, departure_date, departure_airport_id, departure_time, price)
VALUES (2, 2, '06:30:00', '2020-11-01', 1, '09:30:00', 35);
INSERT INTO flight (airline_id, arrival_airport_id, arrival_time, departure_date, departure_airport_id, departure_time, price)
VALUES (3, 1, '06:30:00', '2020-11-01', 3, '09:30:00', 35);
INSERT INTO flight (airline_id, arrival_airport_id, arrival_time, departure_date, departure_airport_id, departure_time, price)
VALUES (1, 3, '06:30:00', '2020-11-01', 1, '09:30:00', 35);
INSERT INTO flight (airline_id, arrival_airport_id, arrival_time, departure_date, departure_airport_id, departure_time, price)
VALUES (4, 2, '06:30:00', '2020-11-01', 4, '09:30:00', 35);
