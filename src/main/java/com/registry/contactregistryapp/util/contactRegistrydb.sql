CREATE DATABASE contact_registry_db;

USE contact_registry_db;


CREATE TABLE contacts (
id INT PRIMARY KEY AUTO_INCREMENT,
full_name VARCHAR(100) NOT NULL,
phone VARCHAR(20),
email VARCHAR(100),
id_number VARCHAR(100) UNIQUE, 
date_of_birth DATE,
gender ENUM('Male', 'Female'),
county_residence VARCHAR(50),
created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
 
);

-- Show the structure of the table
DESCRIBE contacts;

-- Select all data from the table
SELECT * FROM contacts;

-- Insert sample data
INSERT INTO contacts (full_name, phone, email, id_number, date_of_birth, gender, county_residence)
VALUES
    ('Junior Doe', '+254722345679', 'john.doe@example.com', '882345678', '1990-01-15', 'Male', 'Nairobi'),
    ('Jane Smith', '+254723456789', 'jane.smith@example.com', '99456789', '1992-05-20', 'Female', 'Mombasa'),
    ('Michael Johnson', '+254734567890', 'michael.j@example.com', '34567890', '1985-11-10', 'Male', 'Kisumu'),
    ('Sarah Williams', '+254745678901', 'sarah.w@example.com', '45678901', '1988-07-25', 'Female', 'Nakuru'),
    ('Robert Brown', '+254756789012', 'robert.b@example.com', '56789012', '1995-03-05', 'Male', 'Eldoret');

       