-- DROP TABLE IF EXISTS board ;

CREATE TABLE board (
    board_id LONG NOT NULL AUTO_INCREMENT,
    title VARCHAR(45) NULL,
    content VARCHAR(1000) NULL,
    user_id VARCHAR(45) NULL,
    create_dt DATETIME NULL,
    update_dt DATETIME NULL,
    PRIMARY KEY (board_id)
);

-- -----------------------------------------------------
-- Table board_images
-- -----------------------------------------------------
-- DROP TABLE IF EXISTS board_image ;

CREATE TABLE board_image (
    image_id LONG NOT NULL,
    board_id LONG NOT NULL,
    origin_filename VARCHAR(100) NOT NULL,
    url VARCHAR(100) NOT NULL,
    create_dt DATETIME NULL,
    update_dt DATETIME NULL,
    PRIMARY KEY (image_id, board_id)
);