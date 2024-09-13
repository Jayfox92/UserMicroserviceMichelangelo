/*

/// TA BORT

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

/* MediaUser tabell  */
INSERT INTO media_user (id, user_name, email)
VALUES (1, 'Kalle','Kalle@live.se'),
       (2, 'John','John@gmail.com'),
       (3, 'Sara','Sara@hotmail.com');


/* StreamHistory tabell  */
INSERT INTO stream_history (media_id, stream_history_count, media_user_id)
VALUES (1, 2, 1), -- MediaUser 1 Kalle har streamat media med id 1 2 gånger
       (2, 3, 1),
       (3, 4, 1),
       (7, 2, 1),
       (8,10, 1),
       (12, 8, 1),

       (1, 5, 2),
       (2, 10, 2),
       (3, 2, 2),
       (6, 7, 2),
       (8, 13, 2),

       (1, 10, 3),
       (2, 5, 3),
       (3, 1, 3),
       (9, 15, 3),
       (10, 2, 3);


/* ThumbsUpAndDown tabell */
INSERT INTO thumbs_up_and_down (media_id, thumbs_up, thumbs_down, media_user_id)
VALUES (1, 1, 0, 1), -- MediaUser 1 Kalle har tryckt tumme upp på media med id 1
       (2, 0, 1, 1),
       (3, 0, 1, 1),

       (1, 0, 0, 2),
       (2, 1, 0, 2),
       (3, 1, 0, 2),

       (1, 1, 0, 3),
       (2, 1, 0, 3),
       (3, 0, 1, 3);







