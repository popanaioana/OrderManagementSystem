CREATE DATABASE OrdersManagement

USE OrdersManagement;
GO

CREATE TABLE Client (
    id INT IDENTITY(1,1) PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    address VARCHAR(200),
    email VARCHAR(100) NOT NULL
);

CREATE TABLE Product (
    id INT IDENTITY(1,1) PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    price FLOAT NOT NULL,
    stock INT NOT NULL
);

CREATE TABLE Orders (
    id INT IDENTITY(1,1) PRIMARY KEY,
    clientId INT NOT NULL,
    productId INT NOT NULL,
    quantity INT NOT NULL,
    totalPrice FLOAT NOT NULL,
    orderDate DATETIME DEFAULT GETDATE(),

    FOREIGN KEY (clientId) REFERENCES Client(id),
    FOREIGN KEY (productId) REFERENCES Product(id)
);

CREATE TABLE Log (
    id INT IDENTITY(1,1) PRIMARY KEY,
    orderId INT NOT NULL,
    clientName VARCHAR(100),
    productName VARCHAR(100),
    quantity INT,
    totalPrice FLOAT,
    orderDate DATETIME DEFAULT GETDATE()
);

INSERT INTO Client (name, address, email) VALUES
('Ana Pop', 'Cluj-Napoca, Str. Memorandumului 10', 'ana.pop@email.com'),
('Mihai Ionescu', 'Bucuresti, Calea Victoriei 25', 'mihai.ionescu@email.com'),
('Ioana Radu', 'Timisoara, Str. Libertatii 7', 'ioana.radu@email.com'),
('Andrei Stan', 'Iasi, Bd. Independentei 14', 'andrei.stan@email.com'),
('Maria Dumitrescu', 'Brasov, Str. Lunga 33', 'maria.dumitrescu@email.com');

INSERT INTO Product (name, price, stock) VALUES
('Laptop Lenovo ThinkPad', 4200.00, 12),
('Mouse Logitech', 95.50, 40),
('Keyboard Redragon', 180.00, 25),
('Monitor Dell 24 inch', 850.00, 15),
('USB-C Hub', 120.00, 30),
('External SSD 1TB', 390.00, 18),
('Office Chair', 730.00, 8);
GO

SELECT * FROM Client;
SELECT * FROM Product;