CREATE TABLE file_upload (
    id BIGINT(20) PRIMARY KEY AUTO_INCREMENT,
    created_at TIMESTAMP NULL DEFAULT NULL,
    altered_at TIMESTAMP NULL DEFAULT NULL,
    created_by BIGINT(20),
    altered_by BIGINT(20),
    name VARCHAR(255),
    type INT(4),
    object_id BIGINT(20),
    file_data LONGTEXT
) ENGINE=InnoDB DEFAULT CHARSET=utf8;