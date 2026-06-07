CREATE TABLE IF NOT EXISTS operation_records (
  id INT AUTO_INCREMENT PRIMARY KEY,
  module_name VARCHAR(120) NOT NULL,
  owner_name VARCHAR(80) NOT NULL,
  status VARCHAR(40) NOT NULL,
  metric VARCHAR(40) NOT NULL,
  created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

INSERT INTO operation_records (module_name, owner_name, status, metric)
VALUES ('机位/包厢实时状态看板', '运营组', 'ready', '100%');

CREATE TABLE IF NOT EXISTS seats (
  id INT AUTO_INCREMENT PRIMARY KEY,
  seat_code VARCHAR(40) NOT NULL UNIQUE,
  area_name VARCHAR(80) NOT NULL,
  seat_status VARCHAR(20) NOT NULL DEFAULT 'AVAILABLE',
  config_info VARCHAR(500),
  created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

INSERT INTO seats (seat_code, area_name, seat_status, config_info) VALUES
('A001', 'A区-竞技区', 'AVAILABLE', 'RTX4090+i9-14900K+32G'),
('A002', 'A区-竞技区', 'AVAILABLE', 'RTX4090+i9-14900K+32G'),
('A003', 'A区-竞技区', 'OCCUPIED', 'RTX4090+i9-14900K+32G'),
('A004', 'A区-竞技区', 'AVAILABLE', 'RTX4090+i9-14900K+32G'),
('A005', 'A区-竞技区', 'RESERVED', 'RTX4090+i9-14900K+32G'),
('B001', 'B区-休闲区', 'AVAILABLE', 'RTX3070+i5-13400F+16G'),
('B002', 'B区-休闲区', 'AVAILABLE', 'RTX3070+i5-13400F+16G'),
('B003', 'B区-休闲区', 'MAINTENANCE', 'RTX3070+i5-13400F+16G'),
('B004', 'B区-休闲区', 'AVAILABLE', 'RTX3070+i5-13400F+16G'),
('C001', 'C区-包厢', 'AVAILABLE', 'RTX4080+i7-14700K+32G+情侣座'),
('C002', 'C区-包厢', 'OCCUPIED', 'RTX4080+i7-14700K+32G+情侣座'),
('C003', 'C区-包厢', 'AVAILABLE', 'RTX4080+i7-14700K+32G+情侣座');

CREATE TABLE IF NOT EXISTS maintenance_orders (
  id INT AUTO_INCREMENT PRIMARY KEY,
  order_no VARCHAR(40) NOT NULL UNIQUE,
  seat_id INT NOT NULL,
  fault_type VARCHAR(30) NOT NULL,
  description VARCHAR(500),
  reporter_name VARCHAR(80) NOT NULL,
  assignee_group VARCHAR(40) DEFAULT '技术组',
  assignee_name VARCHAR(80),
  order_status VARCHAR(20) NOT NULL DEFAULT 'PENDING',
  priority VARCHAR(20) NOT NULL DEFAULT 'MEDIUM',
  process_result VARCHAR(500),
  timeout_minutes INT NOT NULL DEFAULT 60,
  notified_timeout BOOLEAN DEFAULT FALSE,
  created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  assigned_at TIMESTAMP NULL,
  completed_at TIMESTAMP NULL
);

CREATE TABLE IF NOT EXISTS notifications (
  id INT AUTO_INCREMENT PRIMARY KEY,
  recipient_role VARCHAR(40) NOT NULL,
  recipient_name VARCHAR(80),
  title VARCHAR(120) NOT NULL,
  content VARCHAR(500) NOT NULL,
  notification_type VARCHAR(30) NOT NULL,
  related_order_id INT,
  is_read BOOLEAN DEFAULT FALSE,
  created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);
