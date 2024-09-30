-- Table for MediaUser
CREATE TABLE media_user (
                            id BIGINT AUTO_INCREMENT PRIMARY KEY,
                            user_name VARCHAR(35),
                            email VARCHAR(50)
);

-- Table for StreamHistory
CREATE TABLE stream_history (
                                id BIGINT AUTO_INCREMENT PRIMARY KEY,
                                media_id BIGINT NOT NULL,
                                stream_history_count INT NOT NULL,
                                media_user_id BIGINT,
                                CONSTRAINT fk_media_user_stream_history FOREIGN KEY (media_user_id)
                                    REFERENCES media_user(id) ON DELETE SET NULL
);

-- Table for ThumbsUpAndDown
CREATE TABLE thumbs_up_and_down (
                                    id BIGINT AUTO_INCREMENT PRIMARY KEY,
                                    media_id BIGINT NOT NULL,
                                    thumbs_up BOOLEAN NOT NULL,
                                    thumbs_down BOOLEAN NOT NULL,
                                    media_user_id BIGINT,
                                    CONSTRAINT fk_media_user_thumbs FOREIGN KEY (media_user_id)
                                        REFERENCES media_user(id) ON DELETE SET NULL
);