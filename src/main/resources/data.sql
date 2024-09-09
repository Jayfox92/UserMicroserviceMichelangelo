/*

Klistras just nu in i sql-workbench för att mata in data i databasen, observera att ny databas används som måste skapas

USE michelangelouser;
SELECT * FROM media_user;
SELECT * FROM stream_history;
SELECT * FROM thumbs_up_and_down;
SELECT * FROM media_user_stream_history;
SELECT * FROM media_user_thumbs_up_and_down;

INSERT INTO media_user (user_name, email) VALUES ("Kalle","Kalle@live.se"),
                                                 ("John","John@gmail.com"),
                                                 ("Sara","Sara@hotmail.com");


INSERT INTO stream_history (media_id, stream_history_count) VALUES (1,1),
                                                                  (2,3),
                                                                  (3,0);

INSERT INTO thumbs_up_and_down (media_id, thumbs_up, thumbs_down) VALUES (1,1,0),
                                                                    (2,0,1),
                                                                    (6,0,0);
INSERT INTO media_user_stream_history (media_user_id, stream_history_id) VALUES (1,1),
                                                                       (2,2),
                                                                       (3,3);
INSERT INTO media_user_thumbs_up_and_down (media_user_id, thumbs_up_and_down_id) VALUES (1,1),
                                                                                     (2,2),
                                                                                     (3,3);




*/


