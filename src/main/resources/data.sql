/* Tabell: MediaUser */
INSERT INTO media_user (id, user_name, email)
VALUES (1, 'Kalle','Kalle@live.se'),
       (2, 'John','John@gmail.com'),
       (3, 'Sara','Sara@hotmail.com');


/* Tabell: StreamHistory */
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


/* Tabell: ThumbsUpAndDown */
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
